package org.drftpd.transfer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import org.apache.oro.text.regex.MalformedPatternException;
import org.drftpd.GlobalContext;
import org.drftpd.PassiveConnection;
import org.drftpd.exceptions.FileExistsException;
import org.drftpd.exceptions.TransferFailedException;
import org.drftpd.io.AddAsciiOutputStream;
import org.drftpd.util.HostMask;
import org.drftpd.vfs.root.RootCollection;
import se.mog.io.File;

/**
 * @author zubov
 * @version $Id: Transfer.java 1832 2007-11-15 02:05:23Z zubov $
 */
public class Transfer {

    private String _abortReason = null;

    private CRC32 _checksum = null;

    private Connection _conn;

    private char _direction;

    private long _finished = 0;

    private InputStream _in;

    private char _mode = 'I';

    private OutputStream _out;

    private Socket _sock;

    private long _started = 0;

    private long _transfered = 0;

    public static final char TRANSFER_RECEIVING_UPLOAD = 'R';

    public static final char TRANSFER_SENDING_DOWNLOAD = 'S';

    public static final char TRANSFER_UNKNOWN = 'U';

    private File _file = null;

    /**
     * Start undefined transfer.
     */
    public Transfer(Connection conn) {
        if (conn == null) {
            throw new RuntimeException();
        }
        _conn = conn;
        synchronized (this) {
            _direction = Transfer.TRANSFER_UNKNOWN;
        }
    }

    public synchronized void abort(String reason) {
        try {
            _abortReason = reason;
        } finally {
            if (_conn != null) {
                _conn.abort();
            }
            if (_sock != null) {
                try {
                    _sock.close();
                } catch (IOException e) {
                }
            }
            if (_out != null) {
                try {
                    _out.close();
                } catch (IOException e) {
                }
            }
            if (_in != null) {
                try {
                    _in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public long getChecksum() {
        if (_checksum == null) {
            return 0;
        }
        return _checksum.getValue();
    }

    public long getElapsed() {
        if (_finished == 0) {
            return System.currentTimeMillis() - _started;
        }
        return _finished - _started;
    }

    public int getLocalPort() {
        if (_conn instanceof PassiveConnection) {
            return ((PassiveConnection) _conn).getLocalPort();
        }
        throw new IllegalStateException("getLocalPort() called on a non-passive transfer");
    }

    public Connection getTransferConnection() {
        return _conn;
    }

    public char getState() {
        return _direction;
    }

    public TransferStatus getTransferStatus() {
        return new TransferStatus(getElapsed(), getTransfered(), getChecksum(), isFinished());
    }

    public boolean isFinished() {
        return (_finished != 0 || _abortReason != null);
    }

    public long getTransfered() {
        return _transfered;
    }

    public int getXferSpeed() {
        long elapsed = getElapsed();
        if (_transfered == 0) {
            return 0;
        }
        if (elapsed == 0) {
            return 0;
        }
        return (int) (_transfered / ((float) elapsed / (float) 1000));
    }

    public char getTransferDirection() {
        return _direction;
    }

    public boolean isReceivingUploading() {
        return _direction == Transfer.TRANSFER_RECEIVING_UPLOAD;
    }

    public boolean isSendingUploading() {
        return _direction == Transfer.TRANSFER_SENDING_DOWNLOAD;
    }

    public void receiveFile(String pathFile, char type, long position, String inetAddress) throws IOException {
        RootCollection roots = GlobalContext.getGlobalContext().getRootsBasket();
        try {
            roots.getFile(pathFile);
            throw new FileExistsException("File " + pathFile + " exists");
        } catch (FileNotFoundException ex) {
        }
        String path = pathFile.substring(0, pathFile.lastIndexOf("/"));
        String filename = pathFile.substring(pathFile.lastIndexOf("/") + 1);
        String root = roots.getARootFileDir(roots.getARoot(), path).getPath();
        System.out.println("ReceiveFile : " + root + File.separator + filename);
        try {
            _out = new FileOutputStream(_file = new File(root + File.separator + filename));
            _checksum = new CRC32();
            _out = new CheckedOutputStream(_out, _checksum);
            accept(GlobalContext.getConfig().getCipherSuites(), 68512);
            _in = _sock.getInputStream();
            synchronized (this) {
                _direction = Transfer.TRANSFER_RECEIVING_UPLOAD;
            }
            transfer();
        } catch (IOException e) {
            throw e;
        } finally {
            if (_sock != null) {
                try {
                    _sock.close();
                } catch (IOException e) {
                }
            }
            if (_out != null) {
                try {
                    _out.close();
                } catch (IOException e) {
                }
            }
            if (_in != null) {
                try {
                    _in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void sendFile(String path, char type, long resumePosition, String inetAddress) throws IOException {
        try {
            RootCollection roots = GlobalContext.getGlobalContext().getRootsBasket();
            File fileToDn = roots.getFile(path);
            System.out.println("SendFile : " + fileToDn.getPath());
            _in = new FileInputStream(fileToDn);
            _checksum = new CRC32();
            _in = new CheckedInputStream(_in, _checksum);
            _in.skip(resumePosition);
            accept(GlobalContext.getConfig().getCipherSuites(), 68512);
            _out = _sock.getOutputStream();
            synchronized (this) {
                _direction = Transfer.TRANSFER_SENDING_DOWNLOAD;
            }
            transfer();
        } finally {
            if (_sock != null) {
                try {
                    _sock.close();
                } catch (IOException e) {
                }
            }
            if (_out != null) {
                try {
                    _out.close();
                } catch (IOException e) {
                }
            }
            if (_in != null) {
                try {
                    _in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void accept(String[] cipherSuites, int bufferSize) throws IOException {
        _sock = _conn.connect(cipherSuites, bufferSize);
        _conn = null;
    }

    /**
     * Call sock.connect() and start sending.
     * 
     * Read about buffers here:
     * http://groups.google.com/groups?hl=sv&lr=&ie=UTF-8&oe=UTF-8&threadm=9eomqe%24rtr%241%40to-gate.itd.utech.de&rnum=22&prev=/groups%3Fq%3Dtcp%2Bgood%2Bbuffer%2Bsize%26start%3D20%26hl%3Dsv%26lr%3D%26ie%3DUTF-8%26oe%3DUTF-8%26selm%3D9eomqe%2524rtr%25241%2540to-gate.itd.utech.de%26rnum%3D22
     * 
     * Quote: Short answer is: if memory is not limited make your buffer big;
     * TCP will flow control itself and only use what it needs.
     * 
     * Longer answer: for optimal throughput (assuming TCP is not flow
     * controlling itself for other reasons) you want your buffer size to at
     * least be
     * 
     * channel bandwidth * channel round-trip-delay.
     * 
     * So on a long slow link, if you can get 100K bps throughput, but your
     * delay -s 8 seconds, you want:
     * 
     * 100Kbps * / bits-per-byte * 8 seconds = 100 Kbytes
     * 
     * That way TCP can keep transmitting data for 8 seconds before it would
     * have to stop and wait for an ack (to clear space in the buffer for new
     * data so it can put new TX data in there and on the line). (The idea is to
     * get the ack before you have to stop transmitting.)
     */
    private void transfer() throws IOException {
        try {
            _started = System.currentTimeMillis();
            if (_mode == 'A') {
                _out = new AddAsciiOutputStream(_out);
            }
            byte[] buff = new byte[32768];
            int count;
            long currentTime = System.currentTimeMillis();
            try {
                while (true) {
                    if (_abortReason != null) {
                        throw new TransferFailedException("Transfer was aborted - " + _abortReason, getTransferStatus());
                    }
                    count = _in.read(buff);
                    if (count == -1) {
                        break;
                    }
                    if ((System.currentTimeMillis() - currentTime) >= 1000) {
                        TransferStatus ts = getTransferStatus();
                        if (ts.isFinished()) {
                            throw new TransferFailedException("Transfer was aborted - " + _abortReason, ts);
                        }
                        currentTime = System.currentTimeMillis();
                    }
                    _transfered += count;
                    _out.write(buff, 0, count);
                }
                _out.flush();
            } catch (IOException e) {
                throw new TransferFailedException(e, getTransferStatus());
            }
        } finally {
            _finished = System.currentTimeMillis();
        }
    }

    private boolean checkMasks(String maskString, InetAddress connectedAddress) {
        HostMask mask = new HostMask(maskString);
        try {
            return mask.matchesHost(connectedAddress);
        } catch (MalformedPatternException e) {
            return false;
        }
    }
}
