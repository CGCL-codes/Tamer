package org.drftpd.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Random;
import javax.net.ServerSocketFactory;
import org.apache.log4j.Logger;

/**
 * @author mog
 * @version $Id: PortRange.java 1621 2007-02-13 20:41:31Z djb61 $
 */
public class PortRange {

    private static final Logger logger = Logger.getLogger(PortRange.class);

    private int _minPort;

    private int _maxPort;

    private int _bufferSize = 0;

    Random rand = new Random();

    /**
	 * Creates a default port range for port 49152 to 65535.
	 */
    public PortRange(int bufferSize) {
        _maxPort = 0;
        _minPort = 0;
        _bufferSize = bufferSize;
    }

    public PortRange(int minPort, int maxPort, int bufferSize) {
        if (0 >= minPort || minPort > maxPort || maxPort > 65535) {
            throw new RuntimeException("0 < minPort <= maxPort <= 65535");
        }
        _maxPort = maxPort;
        _minPort = minPort;
        if (bufferSize < 0) {
            throw new RuntimeException("BufferSize cannot be < 0");
        }
        _bufferSize = bufferSize;
    }

    private ServerSocket createServerSocket(int port, ServerSocketFactory ssf) throws IOException {
        ServerSocket ss = ssf.createServerSocket();
        if (_bufferSize > 0) {
            ss.setReceiveBufferSize(_bufferSize);
        }
        ss.bind(new InetSocketAddress(port), 1);
        return ss;
    }

    public ServerSocket getPort(ServerSocketFactory ssf) {
        if (_minPort == 0) {
            try {
                return createServerSocket(0, ssf);
            } catch (IOException e) {
                logger.error("Unable to bind anonymous port", e);
                throw new RuntimeException(e);
            }
        }
        int pos = rand.nextInt(_maxPort - _minPort + 1) + _minPort;
        int initPos = pos;
        boolean retry = true;
        while (true) {
            try {
                return createServerSocket(pos, ssf);
            } catch (IOException e) {
            }
            pos++;
            if (pos > _maxPort) {
                pos = _minPort;
            }
            if (pos == initPos) {
                if (retry == false) {
                    throw new RuntimeException("PortRange exhausted");
                }
                System.runFinalization();
                retry = false;
            }
        }
    }
}
