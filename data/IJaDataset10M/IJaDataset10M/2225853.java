package com.jcraft.jsch;

import java.net.*;

class ChannelX11 extends Channel {

    private static final int LOCAL_WINDOW_SIZE_MAX = 0x20000;

    private static final int LOCAL_MAXIMUM_PACKET_SIZE = 0x4000;

    private static final int TIMEOUT = 10 * 1000;

    private static String host = "127.0.0.1";

    private static int port = 6000;

    private boolean init = true;

    static byte[] cookie = null;

    private static byte[] cookie_hex = null;

    private static java.util.Hashtable faked_cookie_pool = new java.util.Hashtable();

    private static java.util.Hashtable faked_cookie_hex_pool = new java.util.Hashtable();

    private static byte[] table = { 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66 };

    private Socket socket = null;

    static int revtable(byte foo) {
        for (int i = 0; i < table.length; i++) {
            if (table[i] == foo) return i;
        }
        return 0;
    }

    static void setCookie(String foo) {
        cookie_hex = foo.getBytes();
        cookie = new byte[16];
        for (int i = 0; i < 16; i++) {
            cookie[i] = (byte) (((revtable(cookie_hex[i * 2]) << 4) & 0xf0) | ((revtable(cookie_hex[i * 2 + 1])) & 0xf));
        }
    }

    static void setHost(String foo) {
        host = foo;
    }

    static void setPort(int foo) {
        port = foo;
    }

    static byte[] getFakedCookie(Session session) {
        synchronized (faked_cookie_hex_pool) {
            byte[] foo = (byte[]) faked_cookie_hex_pool.get(session);
            if (foo == null) {
                Random random = Session.random;
                foo = new byte[16];
                synchronized (random) {
                    random.fill(foo, 0, 16);
                }
                faked_cookie_pool.put(session, foo);
                byte[] bar = new byte[32];
                for (int i = 0; i < 16; i++) {
                    bar[2 * i] = table[(foo[i] >>> 4) & 0xf];
                    bar[2 * i + 1] = table[(foo[i]) & 0xf];
                }
                faked_cookie_hex_pool.put(session, bar);
                foo = bar;
            }
            return foo;
        }
    }

    ChannelX11() {
        super();
        setLocalWindowSizeMax(LOCAL_WINDOW_SIZE_MAX);
        setLocalWindowSize(LOCAL_WINDOW_SIZE_MAX);
        setLocalPacketSize(LOCAL_MAXIMUM_PACKET_SIZE);
        type = "x11".getBytes();
        connected = true;
    }

    public void run() {
        try {
            socket = Util.createSocket(host, port, TIMEOUT);
            socket.setTcpNoDelay(true);
            io = new IO();
            io.setInputStream(socket.getInputStream());
            io.setOutputStream(socket.getOutputStream());
            sendOpenConfirmation();
        } catch (Exception e) {
            sendOpenFailure(SSH_OPEN_ADMINISTRATIVELY_PROHIBITED);
            close = true;
            disconnect();
            return;
        }
        thread = Thread.currentThread();
        Buffer buf = new Buffer(rmpsize);
        Packet packet = new Packet(buf);
        int i = 0;
        try {
            while (thread != null && io != null && io.in != null) {
                i = io.in.read(buf.buffer, 14, buf.buffer.length - 14 - 32 - 20);
                if (i <= 0) {
                    eof();
                    break;
                }
                if (close) break;
                packet.reset();
                buf.putByte((byte) Session.SSH_MSG_CHANNEL_DATA);
                buf.putInt(recipient);
                buf.putInt(i);
                buf.skip(i);
                getSession().write(packet, this, i);
            }
        } catch (Exception e) {
        }
        disconnect();
    }

    private byte[] cache = new byte[0];

    private byte[] addCache(byte[] foo, int s, int l) {
        byte[] bar = new byte[cache.length + l];
        System.arraycopy(foo, s, bar, cache.length, l);
        if (cache.length > 0) System.arraycopy(cache, 0, bar, 0, cache.length);
        cache = bar;
        return cache;
    }

    void write(byte[] foo, int s, int l) throws java.io.IOException {
        if (init) {
            Session _session = null;
            try {
                _session = getSession();
            } catch (JSchException e) {
                throw new java.io.IOException(e.toString());
            }
            foo = addCache(foo, s, l);
            s = 0;
            l = foo.length;
            if (l < 9) return;
            int plen = (foo[s + 6] & 0xff) * 256 + (foo[s + 7] & 0xff);
            int dlen = (foo[s + 8] & 0xff) * 256 + (foo[s + 9] & 0xff);
            if ((foo[s] & 0xff) == 0x42) {
            } else if ((foo[s] & 0xff) == 0x6c) {
                plen = ((plen >>> 8) & 0xff) | ((plen << 8) & 0xff00);
                dlen = ((dlen >>> 8) & 0xff) | ((dlen << 8) & 0xff00);
            } else {
            }
            if (l < 12 + plen + ((-plen) & 3) + dlen) return;
            byte[] bar = new byte[dlen];
            System.arraycopy(foo, s + 12 + plen + ((-plen) & 3), bar, 0, dlen);
            byte[] faked_cookie = null;
            synchronized (faked_cookie_pool) {
                faked_cookie = (byte[]) faked_cookie_pool.get(_session);
            }
            if (equals(bar, faked_cookie)) {
                if (cookie != null) System.arraycopy(cookie, 0, foo, s + 12 + plen + ((-plen) & 3), dlen);
            } else {
                thread = null;
                eof();
                io.close();
                disconnect();
            }
            init = false;
            io.put(foo, s, l);
            cache = null;
            return;
        }
        io.put(foo, s, l);
    }

    private static boolean equals(byte[] foo, byte[] bar) {
        if (foo.length != bar.length) return false;
        for (int i = 0; i < foo.length; i++) {
            if (foo[i] != bar[i]) return false;
        }
        return true;
    }
}
