package com.jcraft.jsch;

public class HostKey {

    private static final byte[] sshdss = "ssh-dss".getBytes();

    private static final byte[] sshrsa = "ssh-rsa".getBytes();

    protected static final int GUESS = 0;

    public static final int SSHDSS = 1;

    public static final int SSHRSA = 2;

    static final int UNKNOWN = 3;

    protected String host;

    protected int type;

    protected byte[] key;

    public HostKey(String host, byte[] key) throws JSchException {
        this(host, GUESS, key);
    }

    public HostKey(String host, int type, byte[] key) throws JSchException {
        this.host = host;
        if (type == GUESS) {
            if (key[8] == 'd') {
                this.type = SSHDSS;
            } else if (key[8] == 'r') {
                this.type = SSHRSA;
            } else {
                throw new JSchException("invalid key type");
            }
        } else {
            this.type = type;
        }
        this.key = key;
    }

    public String getHost() {
        return host;
    }

    public String getType() {
        if (type == SSHDSS) {
            return new String(sshdss);
        }
        if (type == SSHRSA) {
            return new String(sshrsa);
        }
        return "UNKNOWN";
    }

    public String getKey() {
        return new String(Util.toBase64(key, 0, key.length));
    }

    public String getFingerPrint(JSch jsch) {
        HASH hash = null;
        try {
            Class c = Class.forName(jsch.getConfig("md5"));
            hash = (HASH) (c.newInstance());
        } catch (Exception e) {
            System.err.println("getFingerPrint: " + e);
        }
        return Util.getFingerPrint(hash, key);
    }

    boolean isMatched(String _host) {
        return isIncluded(_host);
    }

    private boolean isIncluded(String _host) {
        int i = 0;
        String hosts = this.host;
        int hostslen = hosts.length();
        int hostlen = _host.length();
        int j;
        while (i < hostslen) {
            j = hosts.indexOf(',', i);
            if (j == -1) {
                if (hostlen != hostslen - i) return false;
                return hosts.regionMatches(true, i, _host, 0, hostlen);
            }
            if (hostlen == (j - i)) {
                if (hosts.regionMatches(true, i, _host, 0, hostlen)) return true;
            }
            i = j + 1;
        }
        return false;
    }
}
