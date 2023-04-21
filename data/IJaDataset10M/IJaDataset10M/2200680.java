package it.uniroma1.dis.omega.upnpqosmedia.video.util;

/**
 * A utility class to parse the session addresses.
 */
public class SessionLabel {

    public String addr = null;

    public int port;

    public int ttl = 1;

    public SessionLabel(String session) throws IllegalArgumentException {
        int off;
        String portStr = null, ttlStr = null;
        if (session != null && session.length() > 0) {
            while (session.length() > 1 && session.charAt(0) == '/') session = session.substring(1);
            off = session.indexOf('/');
            if (off == -1) {
                if (!session.equals("")) addr = session;
            } else {
                addr = session.substring(0, off);
                session = session.substring(off + 1);
                off = session.indexOf('/');
                if (off == -1) {
                    if (!session.equals("")) portStr = session;
                } else {
                    portStr = session.substring(0, off);
                    session = session.substring(off + 1);
                    off = session.indexOf('/');
                    if (off == -1) {
                        if (!session.equals("")) ttlStr = session;
                    } else {
                        ttlStr = session.substring(0, off);
                    }
                }
            }
        }
        if (addr == null) throw new IllegalArgumentException();
        if (portStr != null) {
            try {
                Integer integer = Integer.valueOf(portStr);
                if (integer != null) port = integer.intValue();
            } catch (Throwable t) {
                throw new IllegalArgumentException();
            }
        } else throw new IllegalArgumentException();
        if (ttlStr != null) {
            try {
                Integer integer = Integer.valueOf(ttlStr);
                if (integer != null) ttl = integer.intValue();
            } catch (Throwable t) {
                throw new IllegalArgumentException();
            }
        }
    }
}
