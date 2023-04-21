package net.sf.jnclib.tp.ssh2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InterruptedIOException;
import net.sf.jnclib.tp.ssh2.crai.CraiCipher;
import net.sf.jnclib.tp.ssh2.crai.CraiDigest;
import net.sf.jnclib.tp.ssh2.crai.CraiException;
import net.sf.jnclib.tp.ssh2.crai.CraiRandom;

class Packetizer {

    public Packetizer(InputStream in, OutputStream out, CraiRandom random) throws IOException {
        mInStream = in;
        mOutStream = out;
        mRandom = random;
        mClosed = false;
        mLog = new NullLog();
        mDumpPackets = false;
        mInitCount = 0;
        mKeepAliveInterval = 0;
        mSequenceNumberOut = 0;
        mSequenceNumberIn = 0;
        mWriteLock = new Object();
        mReadBuffer = new byte[64];
    }

    public void setLog(LogSink log) {
        mLog = log;
    }

    public void setDumpPackets(boolean dump) {
        mDumpPackets = dump;
    }

    public synchronized void close() {
        mClosed = true;
    }

    public synchronized void setKeepAlive(int interval, KeepAliveHandler handler) {
        mKeepAliveInterval = interval;
        mKeepAliveHandler = handler;
        mKeepAliveLast = System.currentTimeMillis();
    }

    /**
     * Set the number of bytes that can be sent or received before the
     * packetizer decides it's time to renegotiate keys.  Normally there is
     * no reason to chnage the default value, but it can be useful for unit
     * tests.
     * 
     * @param bytes bytes to be send/received before rekeying
     */
    public void setRekeyBytes(int bytes) {
        mRekeyBytes = bytes;
    }

    /**
     * Return true if it's time to re-negotiate keys on this session.  This
     * needs to be done after about every 1GB of traffic.
     * 
     * @return true if it's time to rekey
     */
    public synchronized boolean needRekey() {
        return mNeedRekey;
    }

    public String readline(int timeout_ms) throws IOException {
        StringBuffer line = new StringBuffer();
        long deadline = (timeout_ms > 0) ? System.currentTimeMillis() + timeout_ms : 0;
        while (true) {
            try {
                int c = mInStream.read();
                if (c < 0) {
                    return null;
                }
                if ((char) c == '\n') {
                    if ((line.length() > 0) && (line.charAt(line.length() - 1) == '\r')) {
                        line.setLength(line.length() - 1);
                    }
                    return line.toString();
                }
                line.append((char) c);
            } catch (InterruptedIOException x) {
            }
            if ((deadline > 0) && (System.currentTimeMillis() >= deadline)) {
                throw new InterruptedIOException("timeout");
            }
            synchronized (this) {
                if (mClosed) {
                    return line.toString();
                }
            }
        }
    }

    public void writeline(String s) throws IOException {
        mOutStream.write(s.getBytes());
    }

    public void setOutboundCipher(CraiCipher cipher, int blockSize, CraiDigest mac, int macSize) {
        synchronized (mWriteLock) {
            mBlockEngineOut = cipher;
            mBlockSizeOut = blockSize;
            mMacEngineOut = mac;
            mMacSizeOut = macSize;
            mSentBytes = 0;
            mSentPackets = 0;
            mInitCount |= 1;
            if (mInitCount == 3) {
                mInitCount = 0;
                triggerRekey(false);
            }
            mMacBufferOut = new byte[32];
        }
    }

    public void setInboundCipher(CraiCipher cipher, int blockSize, CraiDigest mac, int macSize) {
        mBlockEngineIn = cipher;
        mBlockSizeIn = blockSize;
        mMacEngineIn = mac;
        mMacSizeIn = macSize;
        mReceivedBytes = 0;
        mReceivedPackets = 0;
        mReceivedPacketsOverflow = 0;
        mInitCount |= 2;
        if (mInitCount == 3) {
            mInitCount = 0;
            triggerRekey(false);
        }
        mMacBufferIn = new byte[32];
    }

    /**
     * Write an SSH2 message to the stream.  The message will be packetized
     * (padded up to the block size), and if the outbound cipher is on, the
     * message will also be enciphered.
     * 
     * @param msg the message to send
     * @throws IOException if an exception is thrown while writing data
     */
    public void write(Message msg) throws IOException {
        synchronized (mWriteLock) {
            int origLength = msg.getPosition();
            String desc = msg.getCommandDescription();
            int contentLength = msg.getPosition();
            msg.packetize(mRandom, mBlockSizeOut, (mBlockEngineOut != null));
            byte[] packet = msg.toByteArray();
            int length = msg.getPosition();
            if (origLength != contentLength) {
                mLog.debug("Write packet <" + desc + ">, length " + contentLength + " (orig length " + origLength + ")");
            } else {
                mLog.debug("Write packet <" + desc + ">, length " + contentLength);
            }
            if (mDumpPackets) {
                mLog.dump("OUT", packet, 0, length);
            }
            if (mBlockEngineOut != null) {
                new Message(mMacBufferOut).putInt(mSequenceNumberOut);
                mMacEngineOut.reset();
                mMacEngineOut.update(mMacBufferOut, 0, 4);
                mMacEngineOut.update(packet, 0, length);
                try {
                    mMacEngineOut.finish(mMacBufferOut, 0);
                } catch (CraiException x) {
                    throw new IOException("mac error: " + x);
                }
                try {
                    mBlockEngineOut.process(packet, 0, length, packet, 0);
                } catch (CraiException x) {
                    throw new IOException("encipher error: " + x);
                }
            }
            mSequenceNumberOut++;
            write(packet, 0, length);
            if (mBlockEngineOut != null) {
                write(mMacBufferOut, 0, mMacSizeOut);
            }
            mSentBytes += length;
            mSentPackets++;
            if (((mSentPackets >= mRekeyPackets) || (mSentBytes >= mRekeyBytes)) && !needRekey()) {
                mLog.debug("Rekeying (hit " + mSentPackets + " packets, " + mSentBytes + " bytes sent)");
                mReceivedPacketsOverflow = 0;
                triggerRekey(true);
            }
        }
    }

    public Message read() throws IOException {
        if (read(mReadBuffer, 0, mBlockSizeIn, true) < 0) {
            return null;
        }
        if (mBlockEngineIn != null) {
            try {
                mBlockEngineIn.process(mReadBuffer, 0, mBlockSizeIn, mReadBuffer, 0);
            } catch (CraiException x) {
                throw new IOException("decode error: " + x);
            }
        }
        if (mDumpPackets) {
            mLog.dump("IN", mReadBuffer, 0, mBlockSizeIn);
        }
        int length = new Message(mReadBuffer).getInt();
        int leftover = mBlockSizeIn - 5;
        if ((length + 4) % mBlockSizeIn != 0) {
            throw new IOException("Invalid packet blocking");
        }
        int padding = (int) mReadBuffer[4] & 255;
        byte[] packet = new byte[length - 1];
        System.arraycopy(mReadBuffer, 5, packet, 0, leftover);
        int remainderLen = length - leftover - 1;
        if (remainderLen > 0) {
            if (read(packet, leftover, remainderLen, false) < 0) {
                return null;
            }
            if (mBlockEngineIn != null) {
                try {
                    mBlockEngineIn.process(packet, leftover, remainderLen, packet, leftover);
                } catch (CraiException x) {
                    throw new IOException("decode error: " + x);
                }
            }
            if (mDumpPackets) {
                mLog.dump("IN", packet, leftover, remainderLen);
            }
        }
        if (mBlockEngineIn != null) {
            new Message(mMacBufferIn).putInt(mSequenceNumberIn);
            mMacEngineIn.reset();
            mMacEngineIn.update(mMacBufferIn, 0, 4);
            mMacEngineIn.update(mReadBuffer, 0, 5);
            mMacEngineIn.update(packet, 0, length - 1);
            try {
                mMacEngineIn.finish(mMacBufferIn, 0);
            } catch (CraiException x) {
                throw new IOException("mac error: " + x);
            }
            if (read(mReadBuffer, 0, mMacSizeIn, false) < 0) {
                return null;
            }
            for (int i = 0; i < mMacSizeIn; i++) {
                if (mReadBuffer[i] != mMacBufferIn[i]) {
                    throw new IOException("mac mismatch");
                }
            }
        }
        Message msg = null;
        msg = new Message(packet, 0, length - padding - 1, mSequenceNumberIn);
        mLog.debug("Read packet <" + msg.getCommandDescription() + ">, length " + (length - padding - 1));
        mSequenceNumberIn++;
        mReceivedBytes += length + mMacSizeIn + 4;
        mReceivedPackets++;
        if (needRekey()) {
            mReceivedPacketsOverflow++;
            if (mReceivedPacketsOverflow >= 20) {
                throw new IOException("rekey requests are being ignored");
            }
        } else if ((mReceivedPackets >= mRekeyPackets) || (mReceivedBytes >= mRekeyBytes)) {
            mLog.debug("Rekeying (hit " + mReceivedPackets + " packets, " + mReceivedBytes + " bytes received)");
            mReceivedPacketsOverflow = 0;
            triggerRekey(true);
        }
        return msg;
    }

    private int read(byte[] buffer, int offset, int length, boolean checkRekey) throws IOException {
        int total = 0;
        while (true) {
            try {
                int n = mInStream.read(buffer, offset + total, length - total);
                if (n > 0) {
                    total += n;
                }
                if (n < 0) {
                    return n;
                }
            } catch (InterruptedIOException x) {
            }
            if (total == length) {
                return total;
            }
            synchronized (this) {
                if (mClosed) {
                    return -1;
                }
            }
            if (checkRekey && (total == 0) && needRekey()) {
                throw new NeedRekeyException();
            }
            checkKeepAlive();
        }
    }

    private void write(byte[] buffer, int offset, int length) throws IOException {
        mOutStream.write(buffer, offset, length);
    }

    private void checkKeepAlive() {
        if ((mKeepAliveInterval == 0) || (mBlockEngineOut == null) || needRekey()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now > mKeepAliveLast + mKeepAliveInterval) {
            mKeepAliveHandler.keepAliveEvent();
            mKeepAliveLast = now;
        }
    }

    private synchronized void triggerRekey(boolean rekey) {
        mNeedRekey = rekey;
    }

    long getBytesSent() {
        return mSentBytes;
    }

    long getBytesReceived() {
        return mReceivedBytes;
    }

    private static final int REKEY_PACKETS = 0x40000000;

    private static final int REKEY_BYTES = 0x40000000;

    private int mRekeyPackets = REKEY_PACKETS;

    private int mRekeyBytes = REKEY_BYTES;

    private InputStream mInStream;

    private OutputStream mOutStream;

    private CraiRandom mRandom;

    private LogSink mLog;

    private boolean mClosed;

    private boolean mDumpPackets;

    private boolean mNeedRekey;

    private int mInitCount;

    private Object mWriteLock;

    private byte[] mReadBuffer;

    private int mBlockSizeOut = 8;

    private int mBlockSizeIn = 8;

    private CraiCipher mBlockEngineOut;

    private CraiCipher mBlockEngineIn;

    private CraiDigest mMacEngineOut;

    private CraiDigest mMacEngineIn;

    private byte[] mMacBufferOut;

    private byte[] mMacBufferIn;

    int mMacSizeOut;

    int mMacSizeIn;

    private int mSequenceNumberOut;

    private int mSequenceNumberIn;

    private long mSentBytes;

    private long mSentPackets;

    private long mReceivedBytes;

    private long mReceivedPackets;

    private int mReceivedPacketsOverflow;

    private int mKeepAliveInterval;

    private long mKeepAliveLast;

    private KeepAliveHandler mKeepAliveHandler;
}
