package org.maverickdbms.basic.term;

import java.io.IOException;

/**
 * Defines needed to interpret the pty mode values 
 *
 * Defines are from RFC 4254 Section 8
 * http://www.ietf.org/rfc/rfc4254.txt
 *
 * See also man termios(3)
 *
 */
public interface Termios {

    public static final char BELL = '\007';

    public static final char CR = '\015';

    public static final char LF = '\012';

    public static final int NO_OF_MODES = 256;

    public static final int TTY_OP_END = 0;

    public static final int VINTR = 1;

    public static final int VQUIT = 2;

    public static final int VERASE = 3;

    public static final int VKILL = 4;

    public static final int VEOF = 5;

    public static final int VEOL = 6;

    public static final int VEOL2 = 7;

    public static final int VSTART = 8;

    public static final int VSTOP = 9;

    public static final int VSUSP = 10;

    public static final int VDSUSP = 11;

    public static final int VREPRINT = 12;

    public static final int VWERASE = 13;

    public static final int VLNEXT = 14;

    public static final int VFLUSH = 15;

    public static final int VSWTCH = 16;

    public static final int VSTATUS = 17;

    public static final int VDISCARD = 18;

    public static final int IGNPAR = 30;

    public static final int PARMRK = 31;

    public static final int INPCK = 32;

    public static final int ISTRIP = 33;

    public static final int INLCR = 34;

    public static final int IGNCR = 35;

    public static final int ICRNL = 36;

    public static final int IUCLC = 37;

    public static final int IXON = 38;

    public static final int IXANY = 39;

    public static final int IXOFF = 40;

    public static final int IMAXBEL = 41;

    public static final int ISIG = 50;

    public static final int ICANON = 51;

    public static final int XCASE = 52;

    public static final int ECHO = 53;

    public static final int ECHOE = 54;

    public static final int ECHOK = 55;

    public static final int ECHONL = 56;

    public static final int NOFLSH = 57;

    public static final int TOSTOP = 58;

    public static final int IEXTEN = 59;

    public static final int ECHOCTL = 60;

    public static final int ECHOKE = 61;

    public static final int PENDIN = 62;

    public static final int OPOST = 70;

    public static final int OLCUC = 71;

    public static final int ONLCR = 72;

    public static final int OCRNL = 73;

    public static final int ONOCR = 74;

    public static final int ONLRET = 75;

    public static final int CS7 = 90;

    public static final int CS8 = 91;

    public static final int PARENB = 92;

    public static final int PARODD = 93;

    public static final int TTY_OP_ISPEED = 128;

    public static final int TTY_OP_OSPEED = 129;

    public int getAttribute(int no) throws IOException;

    public void setAttribute(int no, int value);
}
