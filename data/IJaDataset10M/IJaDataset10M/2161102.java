package com.aelitis.azureus.core.networkmanager.impl.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;
import org.gudy.azureus2.core3.logging.*;
import org.gudy.azureus2.core3.util.*;
import com.aelitis.azureus.core.networkmanager.VirtualChannelSelector;
import com.aelitis.azureus.core.networkmanager.VirtualChannelSelector.VirtualSelectorListener;
import com.aelitis.azureus.core.networkmanager.impl.TransportHelper;

/**
 * 
 */
public class TCPTransportHelper implements TransportHelper {

    public static final int READ_TIMEOUT = 10 * 1000;

    public static final int CONNECT_TIMEOUT = 60 * 1000;

    public static final int MAX_PARTIAL_WRITE_RETAIN = 64;

    private long remainingBytesToScatter = 0;

    private static boolean enable_efficient_io = !Constants.JAVA_VERSION.startsWith("1.4");

    private final SocketChannel channel;

    private ByteBuffer delayed_write;

    private Map user_data;

    private boolean trace;

    public TCPTransportHelper(SocketChannel _channel) {
        channel = _channel;
    }

    public InetSocketAddress getAddress() {
        return (new InetSocketAddress(channel.socket().getInetAddress(), channel.socket().getPort()));
    }

    public String getName(boolean verbose) {
        if (verbose) {
            return ("TCP");
        } else {
            return ("");
        }
    }

    public boolean minimiseOverheads() {
        return (false);
    }

    public int getConnectTimeout() {
        return (CONNECT_TIMEOUT);
    }

    public int getReadTimeout() {
        return (READ_TIMEOUT);
    }

    public boolean delayWrite(ByteBuffer buffer) {
        if (delayed_write != null) {
            Debug.out("secondary delayed write");
            return (false);
        }
        delayed_write = buffer;
        return (true);
    }

    public boolean hasDelayedWrite() {
        return (delayed_write != null);
    }

    public int write(ByteBuffer buffer, boolean partial_write) throws IOException {
        if (channel == null) {
            Debug.out("channel == null");
            return 0;
        }
        if (partial_write && delayed_write == null) {
            if (buffer.remaining() < MAX_PARTIAL_WRITE_RETAIN) {
                ByteBuffer copy = ByteBuffer.allocate(buffer.remaining());
                copy.put(buffer);
                copy.position(0);
                delayed_write = copy;
                return (copy.remaining());
            }
        }
        long written = 0;
        if (delayed_write != null) {
            ByteBuffer[] buffers = new ByteBuffer[] { delayed_write, buffer };
            int delay_remaining = delayed_write.remaining();
            delayed_write = null;
            written = write(buffers, 0, 2);
            if (buffers[0].hasRemaining()) {
                delayed_write = buffers[0];
                written = 0;
            } else {
                written -= delay_remaining;
            }
        } else {
            written = channelWrite(buffer);
        }
        if (trace) {
            TimeFormatter.milliTrace("tcp: write " + written);
        }
        return ((int) written);
    }

    public long write(ByteBuffer[] buffers, int array_offset, int length) throws IOException {
        if (channel == null) {
            Debug.out("channel == null");
            return 0;
        }
        long written_sofar = 0;
        if (delayed_write != null) {
            ByteBuffer[] buffers2 = new ByteBuffer[length + 1];
            buffers2[0] = delayed_write;
            int pos = 1;
            for (int i = array_offset; i < array_offset + length; i++) {
                buffers2[pos++] = buffers[i];
            }
            int delay_remaining = delayed_write.remaining();
            delayed_write = null;
            written_sofar = write(buffers2, 0, buffers2.length);
            if (buffers2[0].hasRemaining()) {
                delayed_write = buffers2[0];
                written_sofar = 0;
            } else {
                written_sofar -= delay_remaining;
            }
        } else {
            if (enable_efficient_io && remainingBytesToScatter < 1) {
                try {
                    written_sofar = channel.write(buffers, array_offset, length);
                } catch (IOException ioe) {
                    String msg = ioe.getMessage();
                    if (msg != null && msg.equals("A non-blocking socket operation could not be completed immediately")) {
                        enable_efficient_io = false;
                        Logger.log(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_WARNING, "WARNING: Multi-buffer socket write failed; " + "switching to single-buffer mode.\n" + "Upgrade to JRE 1.5 (5.0) series to fix this problem!"));
                    }
                    throw ioe;
                }
            } else {
                for (int i = array_offset; i < (array_offset + length); i++) {
                    int data_length = buffers[i].remaining();
                    int written = channelWrite(buffers[i]);
                    written_sofar += written;
                    if (written < data_length) {
                        break;
                    }
                }
            }
        }
        if (trace) {
            TimeFormatter.milliTrace("tcp: write " + written_sofar);
        }
        return written_sofar;
    }

    private static final Random rnd = new Random();

    private int channelWrite(ByteBuffer buf) throws IOException {
        int written = 0;
        while (remainingBytesToScatter > 0 && buf.remaining() > 0) {
            int currentWritten = channel.write((ByteBuffer) (buf.slice().limit(Math.min(50 + rnd.nextInt(100), buf.remaining()))));
            if (currentWritten == 0) break;
            buf.position(buf.position() + currentWritten);
            remainingBytesToScatter -= currentWritten;
            if (remainingBytesToScatter <= 0) {
                remainingBytesToScatter = 0;
                try {
                    channel.socket().setTcpNoDelay(false);
                } catch (SocketException e) {
                    Debug.printStackTrace(e);
                }
            }
            written += currentWritten;
        }
        if (buf.remaining() > 0) written += channel.write(buf);
        return written;
    }

    public int read(ByteBuffer buffer) throws IOException {
        if (channel == null) {
            Debug.out("channel == null");
            return 0;
        }
        int res = channel.read(buffer);
        if (trace) {
            TimeFormatter.milliTrace("tcp: read " + res);
        }
        return (res);
    }

    public long read(ByteBuffer[] buffers, int array_offset, int length) throws IOException {
        if (channel == null) {
            Debug.out("channel == null");
            return 0;
        }
        if (buffers == null) {
            Debug.out("read: buffers == null");
            return 0;
        }
        long bytes_read = 0;
        if (enable_efficient_io) {
            try {
                bytes_read = channel.read(buffers, array_offset, length);
            } catch (IOException ioe) {
                String msg = ioe.getMessage();
                if (msg != null && msg.equals("A non-blocking socket operation could not be completed immediately")) {
                    enable_efficient_io = false;
                    Logger.log(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_WARNING, "WARNING: Multi-buffer socket read failed; switching to single-buffer mode.\n" + "Upgrade to JRE 1.5 (5.0) series to fix this problem!"));
                }
                throw ioe;
            }
        } else {
            for (int i = array_offset; i < (array_offset + length); i++) {
                int data_length = buffers[i].remaining();
                int read = channel.read(buffers[i]);
                bytes_read += read;
                if (read < data_length) {
                    break;
                }
            }
        }
        if (bytes_read < 0) {
            throw new IOException("end of stream on socket read");
        }
        if (trace) {
            TimeFormatter.milliTrace("tcp: read " + bytes_read);
        }
        return bytes_read;
    }

    public void registerForReadSelects(final selectListener listener, Object attachment) {
        TCPNetworkManager.getSingleton().getReadSelector().register(channel, new VirtualSelectorListener() {

            public boolean selectSuccess(VirtualChannelSelector selector, SocketChannel sc, Object attachment) {
                return (listener.selectSuccess(TCPTransportHelper.this, attachment));
            }

            public void selectFailure(VirtualChannelSelector selector, SocketChannel sc, Object attachment, Throwable msg) {
                listener.selectFailure(TCPTransportHelper.this, attachment, msg);
            }
        }, attachment);
    }

    public void registerForWriteSelects(final selectListener listener, Object attachment) {
        TCPNetworkManager.getSingleton().getWriteSelector().register(channel, new VirtualSelectorListener() {

            public boolean selectSuccess(VirtualChannelSelector selector, SocketChannel sc, Object attachment) {
                if (trace) {
                    TimeFormatter.milliTrace("tcp: write select");
                }
                return (listener.selectSuccess(TCPTransportHelper.this, attachment));
            }

            public void selectFailure(VirtualChannelSelector selector, SocketChannel sc, Object attachment, Throwable msg) {
                listener.selectFailure(TCPTransportHelper.this, attachment, msg);
            }
        }, attachment);
    }

    public void cancelReadSelects() {
        TCPNetworkManager.getSingleton().getReadSelector().cancel(channel);
    }

    public void cancelWriteSelects() {
        if (trace) {
            TimeFormatter.milliTrace("tcp: cancel write selects");
        }
        TCPNetworkManager.getSingleton().getWriteSelector().cancel(channel);
    }

    public void resumeReadSelects() {
        TCPNetworkManager.getSingleton().getReadSelector().resumeSelects(channel);
    }

    public void resumeWriteSelects() {
        if (trace) {
            TimeFormatter.milliTrace("tcp: resume write selects");
        }
        TCPNetworkManager.getSingleton().getWriteSelector().resumeSelects(channel);
    }

    public void pauseReadSelects() {
        TCPNetworkManager.getSingleton().getReadSelector().pauseSelects(channel);
    }

    public void pauseWriteSelects() {
        if (trace) {
            TimeFormatter.milliTrace("tcp: pause write selects");
        }
        TCPNetworkManager.getSingleton().getWriteSelector().pauseSelects(channel);
    }

    public void close(String reason) {
        TCPNetworkManager.getSingleton().getReadSelector().cancel(channel);
        TCPNetworkManager.getSingleton().getWriteSelector().cancel(channel);
        TCPNetworkManager.getSingleton().getConnectDisconnectManager().closeConnection(channel);
    }

    public void failed(Throwable reason) {
        close(Debug.getNestedExceptionMessage(reason));
    }

    public SocketChannel getSocketChannel() {
        return channel;
    }

    public synchronized void setUserData(Object key, Object data) {
        if (user_data == null) {
            user_data = new HashMap();
        }
        user_data.put(key, data);
    }

    public synchronized Object getUserData(Object key) {
        if (user_data == null) {
            return (null);
        }
        return (user_data.get(key));
    }

    public void setTrace(boolean on) {
        trace = on;
    }

    public void setScatteringMode(long forBytes) {
        if (forBytes > 0) {
            if (remainingBytesToScatter == 0) try {
                channel.socket().setTcpNoDelay(true);
            } catch (SocketException e) {
                Debug.printStackTrace(e);
            }
            remainingBytesToScatter = forBytes;
        } else {
            if (remainingBytesToScatter > 0) try {
                channel.socket().setTcpNoDelay(false);
            } catch (SocketException e) {
                Debug.printStackTrace(e);
            }
            remainingBytesToScatter = 0;
        }
    }
}
