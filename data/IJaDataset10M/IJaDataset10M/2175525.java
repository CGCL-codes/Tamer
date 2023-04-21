package net.sourceforge.blueonjop;

import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import joprt.RtThread;

/**
 * Class for reading replies from BISM
 * @author Kasper Hansen
 *
 */
public class ReplyReader {

    public static String[] list, newList;

    public static int c;

    public static StringBuffer buf;

    public static String OK = "OK";

    public static String CONNECT = "CONNECT";

    public static String INVALID_ADDRESS = "INVALID ADDRESS";

    public static String NO_CARRIER = "NO CARRIER";

    public static String ERROR = "ERROR";

    public static String RX = "RX";

    public static String RX_OK = "RX\"A0\\00\\07\\10\\00\\11\\14\"";

    public static String RX_CONTINUE = "RX\"\\90\\00\\03\"";

    public static StringBuffer foo;

    public static char ch;

    public static int index;

    public static String[] bar;

    /**
	 * Read the reply from an inquiry
	 * @param listener
	 * @return the parsed reply
	 */
    public static String[] readInq(DiscoveryListener listener) {
        buf = new StringBuffer();
        list = null;
        for (; ; ) {
            RtThread.sleepMs(20);
            c = (char) System.in.read();
            if (c == 13) break;
        }
        boolean doBreak = false;
        for (; ; ) {
            RtThread.sleepMs(20);
            if (System.in.available() > 0) {
                c = System.in.read();
            } else c = 13;
            if (c != 13) {
                if (c != 10) buf.append((char) c); else {
                    if (buf.toString().regionMatches(0, OK, 0, OK.length()) || buf.toString().regionMatches(0, ERROR, 0, ERROR.length())) {
                        doBreak = true;
                    }
                    if (buf.length() > 0) {
                        listAdd(buf.toString());
                        if (!doBreak) {
                            foo = new StringBuffer();
                            bar = new String[3];
                            index = 0;
                            for (int j = 0; j < buf.toString().length(); j++) {
                                ch = buf.toString().charAt(j);
                                if (ch != ',' && j != buf.toString().length() - 1) {
                                    if (ch != '"') foo.append(ch);
                                } else {
                                    bar[index] = foo.toString();
                                    foo.delete(0, foo.length());
                                    index = index + 1;
                                }
                            }
                            listener.deviceDiscovered(new RemoteDevice(bar[2], bar[0]), bar[1]);
                        }
                        buf.delete(0, buf.length());
                    }
                }
            }
            if (doBreak) break;
        }
        while (System.in.available() > 0) {
            System.in.read();
            RtThread.sleepMs(10);
        }
        return list;
    }

    /**
	 * Read the reply from a service search
	 * @return the parsed reply
	 */
    public static String[] readSer() {
        buf = new StringBuffer();
        list = null;
        for (; ; ) {
            RtThread.sleepMs(20);
            c = (char) System.in.read();
            if (c == 13) break;
        }
        boolean doBreak = false;
        for (; ; ) {
            RtThread.sleepMs(20);
            if (System.in.available() > 0) {
                c = System.in.read();
            } else c = 13;
            if (c != 13) {
                if (c != 10) buf.append((char) c); else {
                    if (buf.toString().regionMatches(0, OK, 0, OK.length()) || buf.toString().regionMatches(0, ERROR, 0, ERROR.length()) || buf.toString().regionMatches(0, INVALID_ADDRESS, 0, INVALID_ADDRESS.length())) doBreak = true;
                    if (buf.length() > 0) listAdd(buf.toString());
                    buf.delete(0, buf.length());
                }
            }
            if (doBreak) break;
        }
        while (System.in.available() > 0) {
            System.in.read();
            RtThread.sleepMs(10);
        }
        return list;
    }

    /**
	 * Read the reply from a connection attempt
	 * @return the parsed reply
	 */
    public static String[] readCon() {
        buf = new StringBuffer();
        list = null;
        for (; ; ) {
            RtThread.sleepMs(20);
            c = (char) System.in.read();
            if (c == 13) break;
        }
        boolean doBreak = false;
        for (; ; ) {
            RtThread.sleepMs(20);
            if (System.in.available() > 0) {
                c = System.in.read();
            } else c = 13;
            if (c != 13) {
                if (c != 10) buf.append((char) c); else {
                    if (buf.toString().regionMatches(0, CONNECT, 0, CONNECT.length()) || buf.toString().regionMatches(0, ERROR, 0, ERROR.length()) || buf.toString().regionMatches(0, NO_CARRIER, 0, NO_CARRIER.length()) || buf.toString().regionMatches(0, INVALID_ADDRESS, 0, INVALID_ADDRESS.length())) doBreak = true;
                    if (buf.length() > 0) listAdd(buf.toString());
                    buf.delete(0, buf.length());
                }
            }
            if (doBreak) break;
        }
        while (System.in.available() > 0) {
            System.in.read();
            RtThread.sleepMs(10);
        }
        return list;
    }

    /**
	 * Read the reply from a disconnection attempt
	 * @return the parsed reply
	 */
    public static String[] readDiscon() {
        buf = new StringBuffer();
        list = null;
        for (; ; ) {
            RtThread.sleepMs(20);
            c = (char) System.in.read();
            if (c == 13) break;
        }
        boolean doBreak = false;
        for (; ; ) {
            RtThread.sleepMs(20);
            if (System.in.available() > 0) {
                c = System.in.read();
            } else c = 13;
            if (c != 13) {
                if (c != 10) buf.append((char) c); else {
                    if (buf.toString().regionMatches(0, NO_CARRIER, 0, NO_CARRIER.length()) || buf.toString().regionMatches(0, ERROR, 0, ERROR.length())) doBreak = true;
                    if (buf.length() > 0) listAdd(buf.toString());
                    buf.delete(0, buf.length());
                }
            }
            if (doBreak) break;
        }
        while (System.in.available() > 0) {
            System.in.read();
            RtThread.sleepMs(10);
        }
        return list;
    }

    /**
	 * Read the reply after you go out of data mode
	 * @return the parsed reply
	 */
    public static String readEsc() {
        buf = new StringBuffer();
        while (System.in.available() > 0) {
            c = System.in.read();
            if (c != 13 && c != 10) buf.append((char) c);
            RtThread.sleepMs(20);
        }
        return buf.toString();
    }

    /**
	 * Read a standard reply where the reply would be OK or ERROR nn
	 * @return the parsed reply
	 */
    public static String[] readStandard() {
        buf = new StringBuffer();
        list = null;
        for (; ; ) {
            RtThread.sleepMs(20);
            c = (char) System.in.read();
            if (c == 13) break;
        }
        boolean doBreak = false;
        for (; ; ) {
            RtThread.sleepMs(20);
            if (System.in.available() > 0) {
                c = System.in.read();
            } else c = 13;
            if (c != 13) {
                if (c != 10) buf.append((char) c); else {
                    if (buf.toString().regionMatches(0, OK, 0, OK.length()) || buf.toString().regionMatches(0, ERROR, 0, ERROR.length())) {
                        doBreak = true;
                    }
                    if (buf.length() > 0) {
                        listAdd(buf.toString());
                        buf.delete(0, buf.length());
                    }
                }
            }
            if (doBreak) break;
        }
        System.in.read();
        RtThread.sleepMs(10);
        System.in.read();
        return list;
    }

    /**
	 * Read the received responses from the server e.g.
	 * HTTP_OBEX_CONTINUE when you transfer data to the server.
	 */
    public static String readRX2() {
        buf = new StringBuffer();
        list = null;
        boolean gotFirst = false;
        for (; ; ) {
            RtThread.sleepMs(20);
            if (System.in.available() > 0) {
                c = System.in.read();
                if (c != 13 && c != 10) buf.append((char) c);
                if (c == '"' && gotFirst) break;
                if (c == '"') gotFirst = true;
            }
        }
        return buf.toString();
    }

    /**
	 * Used to add the parsed replies to an array.
	 * @param s
	 */
    public static void listAdd(String s) {
        if (list == null) {
            list = new String[1];
            list[0] = s;
        } else {
            newList = new String[list.length + 1];
            int i = 0;
            for (; i < list.length; i++) newList[i] = list[i];
            newList[i] = s;
            list = newList;
        }
    }
}
