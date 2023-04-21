package org.tn5250j;

import java.util.*;
import java.text.*;
import java.net.Socket;
import java.net.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.*;
import org.tn5250j.tools.CodePage;

public final class tnvt implements Runnable, TN5250jConstants {

    Socket sock;

    BufferedInputStream bin;

    BufferedOutputStream bout;

    DataStreamQueue dsq;

    Stream5250 bk;

    DataStreamProducer producer;

    private Screen5250 screen52;

    private boolean waitingForInput;

    boolean invited;

    private boolean dumpBytes = false;

    private boolean negotiated = false;

    private Thread me;

    private Thread pthread;

    private int readType;

    private boolean enhanced = true;

    private Session controller;

    private boolean cursorOn = false;

    private String session = "";

    private int port = 23;

    private boolean connected = false;

    private boolean support132 = true;

    ByteArrayOutputStream baosp = null;

    ByteArrayOutputStream baosrsp = null;

    byte[] saveStream;

    private boolean proxySet = false;

    private String proxyHost = null;

    private String proxyPort = "1080";

    private int devSeq = -1;

    private String devName;

    private String devNameUsed;

    private boolean[] dataIncluded;

    private CodePage codePage;

    private FileOutputStream fw;

    private BufferedOutputStream dw;

    tnvt(Screen5250 screen52) {
        this(screen52, false, false);
    }

    tnvt(Screen5250 screen52, boolean type, boolean support132) {
        enhanced = type;
        this.support132 = support132;
        setCodePage("37");
        this.screen52 = screen52;
        dataIncluded = new boolean[24];
        baosp = new ByteArrayOutputStream();
        baosrsp = new ByteArrayOutputStream();
    }

    public String getHostName() {
        return session;
    }

    public void setController(Session c) {
        controller = c;
    }

    public void setDeviceName(String name) {
        devName = name;
    }

    public String getDeviceName() {
        return devName;
    }

    public String getAllocatedDeviceName() {
        return devNameUsed;
    }

    public boolean isConnected() {
        return connected;
    }

    public final boolean connect() {
        return connect(session, port);
    }

    public final void setProxy(String proxyHost, String proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        proxySet = true;
        Properties systemProperties = System.getProperties();
        systemProperties.put("socksProxySet", "true");
        systemProperties.put("socksProxyHost", proxyHost);
        systemProperties.put("socksProxyPort", proxyPort);
        System.setProperties(systemProperties);
    }

    public final boolean connect(String s, int port) {
        try {
            session = s;
            this.port = port;
            try {
                screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_ON, "X - Connecting");
            } catch (Exception exc) {
                System.out.println("setStatus(ON) " + exc.getMessage());
            }
            sock = new Socket(s, port);
            if (sock == null) System.out.println("I did not get a socket");
            connected = true;
            sock.setTcpNoDelay(true);
            sock.setSoLinger(false, 0);
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();
            bin = new BufferedInputStream(in, 8192);
            bout = new BufferedOutputStream(out);
            byte abyte0[];
            while (negotiate(abyte0 = readNegotiations())) ;
            negotiated = true;
            try {
                screen52.setCursorOff();
            } catch (Exception excc) {
                System.out.println("setCursorOff " + excc.getMessage());
            }
            ;
            controller.fireSessionChanged(TN5250jConstants.STATE_CONNECTED);
            dsq = new DataStreamQueue();
            producer = new DataStreamProducer(this, bin, dsq, abyte0);
            pthread = new Thread(producer);
            pthread.setPriority(pthread.NORM_PRIORITY / 2);
            pthread.start();
            try {
                screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_OFF, null);
            } catch (Exception exc) {
                System.out.println("setStatus(OFF) " + exc.getMessage());
            }
            me = new Thread(this);
            me.start();
        } catch (Exception exception) {
            if (exception.getMessage() == null) exception.printStackTrace();
            System.out.println("connect() " + exception.getMessage());
            if (sock == null) System.out.println("I did not get a socket");
            disconnect();
            return false;
        }
        return true;
    }

    public final boolean disconnect() {
        if (me != null && me.isAlive()) {
            me.interrupt();
            pthread.interrupt();
        }
        screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_ON, "X - Disconnected");
        screen52.setKeyboardLocked(false);
        try {
            if (bin != null) bin.close();
            if (bout != null) bout.close();
            if (sock != null) {
                System.out.println("Closing socket");
                sock.close();
            }
            connected = false;
            controller.fireSessionChanged(TN5250jConstants.STATE_DISCONNECTED);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            connected = false;
            devSeq = -1;
            return false;
        }
        devSeq = -1;
        return true;
    }

    private final ByteArrayOutputStream appendByteStream(byte abyte0[]) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        for (int i = 0; i < abyte0.length; i++) {
            bytearrayoutputstream.write(abyte0[i]);
            if (abyte0[i] == -1) bytearrayoutputstream.write(-1);
        }
        return bytearrayoutputstream;
    }

    private final byte[] readNegotiations() throws IOException {
        int i = bin.read();
        if (i < 0) {
            throw new IOException("Connection closed.");
        } else {
            int j = bin.available();
            byte abyte0[] = new byte[j + 1];
            abyte0[0] = (byte) i;
            bin.read(abyte0, 1, j);
            return abyte0;
        }
    }

    private final void writeByte(byte abyte0[]) throws IOException {
        bout.write(abyte0);
        bout.flush();
    }

    private final void writeByte(byte byte0) throws IOException {
        bout.write(byte0);
        bout.flush();
    }

    private final void readImmediate(int readType) {
        if (screen52.isStatusErrorCode()) {
            screen52.restoreErrorLine();
            screen52.setStatus(screen52.STATUS_ERROR_CODE, screen52.STATUS_VALUE_OFF, null);
        }
        if (!enhanced) {
            screen52.setCursorOff();
        }
        screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_ON, null);
        screen52.setKeyboardLocked(true);
        invited = false;
        screen52.getScreenFields().readFormatTable(baosp, readType, codePage);
        try {
            writeGDS(0, 3, baosp.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            baosp.reset();
        }
        baosp.reset();
    }

    public final boolean sendAidKey(int aid) {
        if (screen52.isStatusErrorCode()) {
            screen52.restoreErrorLine();
            screen52.setStatus(screen52.STATUS_ERROR_CODE, screen52.STATUS_VALUE_OFF, null);
        }
        if (!enhanced) {
            screen52.setCursorOff();
        }
        screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_ON, null);
        screen52.setKeyboardLocked(true);
        invited = false;
        baosp.write(screen52.getCurrentRow());
        baosp.write(screen52.getCurrentCol());
        baosp.write(aid);
        if (dataIncluded(aid)) screen52.getScreenFields().readFormatTable(baosp, readType, codePage);
        try {
            writeGDS(0, 3, baosp.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            baosp.reset();
            return false;
        }
        baosp.reset();
        return true;
    }

    private boolean dataIncluded(int aid) {
        switch(aid) {
            case PF1:
                return !dataIncluded[0];
            case PF2:
                return !dataIncluded[1];
            case PF3:
                return !dataIncluded[2];
            case PF4:
                return !dataIncluded[3];
            case PF5:
                return !dataIncluded[4];
            case PF6:
                return !dataIncluded[5];
            case PF7:
                return !dataIncluded[6];
            case PF8:
                return !dataIncluded[7];
            case PF9:
                return !dataIncluded[8];
            case PF10:
                return !dataIncluded[9];
            case PF11:
                return !dataIncluded[10];
            case PF12:
                return !dataIncluded[11];
            case PF13:
                return !dataIncluded[12];
            case PF14:
                return !dataIncluded[13];
            case PF15:
                return !dataIncluded[14];
            case PF16:
                return !dataIncluded[15];
            case PF17:
                return !dataIncluded[16];
            case PF18:
                return !dataIncluded[17];
            case PF19:
                return !dataIncluded[18];
            case PF20:
                return !dataIncluded[19];
            case PF21:
                return !dataIncluded[20];
            case PF22:
                return !dataIncluded[21];
            case PF23:
                return !dataIncluded[22];
            case PF24:
                return !dataIncluded[23];
            default:
                return true;
        }
    }

    /**
    * Help request -
    *
    *
    *    See notes inside method
    */
    public final void sendHelpRequest() {
        baosp.write(screen52.getCurrentRow());
        baosp.write(screen52.getCurrentCol());
        baosp.write(AID_HELP);
        try {
            writeGDS(0, 3, baosp.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        baosp.reset();
    }

    /**
    * Attention Key -
    *
    *
    *    See notes inside method
    */
    public final void sendAttentionKey() {
        try {
            writeGDS(0x40, 0, null);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public final void systemRequest() {
        systemRequest(' ');
    }

    /**
    * System request - taken from the rfc1205 - 5250 Telnet interface
    *    section 4.3
    *
    *    See notes inside method
    */
    public final void systemRequest(char sr) {
        if (sr == ' ') {
            JPanel srp = new JPanel();
            srp.setLayout(new BorderLayout());
            JLabel jl = new JLabel("Enter alternate job");
            JTextField sro = new JTextField();
            srp.add(jl, BorderLayout.NORTH);
            srp.add(sro, BorderLayout.CENTER);
            Object[] message = new Object[1];
            message[0] = srp;
            String[] options = { "SysReq", "Cancel" };
            int result = 0;
            result = JOptionPane.showOptionDialog(null, message, "System Request", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            switch(result) {
                case 0:
                    System.out.println("SYSRQS sent");
                    if (sro.getText().length() > 0) {
                        for (int x = 0; x < sro.getText().length(); x++) {
                            baosp.write(getEBCDIC(sro.getText().charAt(x)));
                        }
                        try {
                            writeGDS(4, 0, baosp.toByteArray());
                        } catch (IOException ioe) {
                            System.out.println(ioe.getMessage());
                        }
                        baosp.reset();
                    } else {
                        try {
                            writeGDS(4, 0, null);
                        } catch (IOException ioe) {
                            System.out.println(ioe.getMessage());
                        }
                    }
                    break;
                case 1:
                    break;
                default:
                    break;
            }
            controller.requestFocus();
        } else {
            baosp.write(getEBCDIC(sr));
            try {
                writeGDS(4, 0, baosp.toByteArray());
            } catch (IOException ioe) {
                baosp.reset();
                System.out.println(ioe.getMessage());
            }
            baosp.reset();
        }
    }

    /**
    * Cancel Invite - taken from the rfc1205 - 5250 Telnet interface
    *    section 4.3
    *
    *    See notes inside method
    */
    public final void cancelInvite() {
        screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_ON, null);
        try {
            writeGDS(0, 10, null);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public final void hostPrint(int aid) {
        if (screen52.isStatusErrorCode()) {
            screen52.restoreErrorLine();
            screen52.setStatus(screen52.STATUS_ERROR_CODE, screen52.STATUS_VALUE_OFF, null);
        }
        screen52.setCursorOff();
        screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_ON, null);
        baosp.write(screen52.getCurrentRow());
        baosp.write(screen52.getCurrentCol());
        baosp.write(AID_PRINT);
        try {
            writeGDS(0, 3, baosp.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        baosp.reset();
    }

    protected final void toggleDebug() {
        dumpBytes = !dumpBytes;
        if (dumpBytes) {
            try {
                if (fw == null) {
                    fw = new FileOutputStream("log.txt");
                    dw = new BufferedOutputStream(fw);
                }
            } catch (FileNotFoundException fnfe) {
                System.out.println(fnfe.getMessage());
            }
        } else {
            try {
                if (dw != null) dw.close();
                if (fw != null) fw.close();
                dw = null;
                fw = null;
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
        System.out.println("Data Stream output is now " + dumpBytes);
    }

    public final void writeGDS(int flags, int opcode, byte abyte0[]) throws IOException {
        if (bout == null) return;
        int length;
        if (abyte0 != null) length = abyte0.length + 10; else length = 10;
        baosrsp.write(length >> 8);
        baosrsp.write(length & 0xff);
        baosrsp.write(18);
        baosrsp.write(160);
        baosrsp.write(0);
        baosrsp.write(0);
        baosrsp.write(4);
        baosrsp.write(flags);
        baosrsp.write(0);
        baosrsp.write(opcode);
        if (abyte0 != null) baosrsp.write(abyte0, 0, abyte0.length);
        baosrsp = appendByteStream(baosrsp.toByteArray());
        baosrsp.write(IAC);
        baosrsp.write(EOR);
        baosrsp.writeTo(bout);
        bout.flush();
        baosrsp.reset();
    }

    public final int getOpCode() {
        return bk.getOpCode();
    }

    private final void sendNotify() throws IOException {
        writeGDS(0, 0, null);
    }

    private final void setInvited() {
        if (!screen52.isStatusErrorCode()) screen52.setStatus(screen52.STATUS_SYSTEM, screen52.STATUS_VALUE_OFF, null);
        invited = true;
    }

    public void run() {
        while (true) {
            try {
                bk = (Stream5250) dsq.get();
            } catch (InterruptedException ie) {
                System.out.println(" ie " + ie.getMessage());
            }
            me.yield();
            pthread.yield();
            invited = false;
            screen52.setCursorOff();
            switch(bk.getOpCode()) {
                case 00:
                    break;
                case 1:
                    parseIncoming();
                    screen52.setKeyboardLocked(false);
                    cursorOn = true;
                    setInvited();
                    break;
                case 2:
                    parseIncoming();
                    screen52.updateDirty();
                    break;
                case 3:
                    parseIncoming();
                    setInvited();
                    break;
                case 4:
                    parseIncoming();
                    break;
                case 5:
                    parseIncoming();
                    break;
                case 6:
                    sendAidKey(0);
                    break;
                case 7:
                    break;
                case 8:
                    try {
                        readScreen();
                    } catch (IOException ex) {
                    }
                    break;
                case 9:
                    break;
                case 10:
                    cancelInvite();
                    break;
                case 11:
                    screen52.setMessageLightOn();
                    screen52.setCursorOn();
                    break;
                case 12:
                    screen52.setMessageLightOff();
                    screen52.setCursorOn();
                    break;
                default:
                    break;
            }
            if (screen52.isUsingGuiInterface()) screen52.drawFields();
            if (screen52.isHotSpots()) {
                screen52.checkHotSpots();
            }
            try {
                screen52.updateDirty();
            } catch (Exception exd) {
                System.out.println(" tnvt.run: " + exd.getMessage());
            }
            if (cursorOn && !screen52.isKeyboardLocked()) {
                screen52.setCursorOn();
                cursorOn = false;
            }
            me.yield();
            pthread.yield();
        }
    }

    private final void readScreen() throws IOException {
        int rows = screen52.getRows();
        int cols = screen52.getCols();
        boolean att = false;
        int off = 0;
        byte abyte0[] = new byte[rows * cols];
        fillScreenArray(abyte0, rows, cols);
        writeGDS(0, 0, abyte0);
        abyte0 = null;
    }

    private final void fillScreenArray(byte[] sa, int rows, int cols) {
        int la = 32;
        int sac = 0;
        int len = rows * cols;
        for (int y = 0; y < len; y++) {
            if (screen52.screen[y].isAttributePlace()) {
                la = screen52.screen[y].getCharAttr();
                sa[sac++] = (byte) la;
            } else {
                if (screen52.screen[y].getCharAttr() != la) {
                    la = screen52.screen[y].getCharAttr();
                    sac--;
                    sa[sac++] = (byte) la;
                }
                sa[sac++] = (byte) getEBCDIC(screen52.screen[y].getChar());
            }
        }
    }

    public final void saveScreen() throws IOException {
        ByteArrayOutputStream sc = new ByteArrayOutputStream();
        sc.write(4);
        sc.write(0x12);
        sc.write(0);
        sc.write(0);
        sc.write((byte) screen52.getRows());
        sc.write((byte) screen52.getCols());
        int cp = screen52.getCurrentPos();
        sc.write((byte) (cp >> 8 & 0xff));
        sc.write((byte) (cp & 0xff));
        sc.write((byte) (screen52.homePos >> 8 & 0xff));
        sc.write((byte) (screen52.homePos & 0xff));
        int rows = screen52.getRows();
        int cols = screen52.getCols();
        byte[] sa = new byte[rows * cols];
        fillScreenArray(sa, rows, cols);
        sc.write(sa);
        sa = null;
        int sizeFields = screen52.getScreenFields().getSize();
        sc.write((byte) (sizeFields >> 8 & 0xff));
        sc.write((byte) (sizeFields & 0xff));
        if (sizeFields > 0) {
            int x = 0;
            int s = screen52.getScreenFields().getSize();
            ScreenField sf = null;
            while (x < s) {
                sf = screen52.getScreenFields().getField(x);
                sc.write((byte) sf.getAttr());
                int sp = sf.startPos();
                sc.write((byte) (sp >> 8 & 0xff));
                sc.write((byte) (sp & 0xff));
                if (sf.mdt) sc.write((byte) 1); else sc.write((byte) 0);
                sc.write((byte) (sf.getLength() >> 8 & 0xff));
                sc.write((byte) (sf.getLength() & 0xff));
                sc.write((byte) sf.getFFW1() & 0xff);
                sc.write((byte) sf.getFFW2() & 0xff);
                sc.write((byte) sf.getFCW1() & 0xff);
                sc.write((byte) sf.getFCW2() & 0xff);
                x++;
            }
            sf = null;
        }
        screen52.getScreenFields().setCurrentField(null);
        screen52.clearTable();
        try {
            writeGDS(0, 3, sc.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        sc = null;
    }

    /**
    */
    public final void restoreScreen() throws IOException {
        int which = 0;
        try {
            bk.getNextByte();
            bk.getNextByte();
            int rows = bk.getNextByte() & 0xff;
            int cols = bk.getNextByte() & 0xff;
            int pos = bk.getNextByte() << 8 & 0xff00;
            pos |= bk.getNextByte() & 0xff;
            int hPos = bk.getNextByte() << 8 & 0xff00;
            hPos |= bk.getNextByte() & 0xff;
            if (rows != screen52.getRows()) screen52.setRowsCols(rows, cols);
            screen52.clearAll();
            int b = 32;
            int la = 32;
            int len = rows * cols;
            for (int y = 0; y < len; y++) {
                b = bk.getNextByte();
                if (isAttribute(b)) {
                    screen52.screen[y].setCharAndAttr(screen52.screen[y].getChar(), b, true);
                    la = b;
                } else {
                    screen52.screen[y].setCharAndAttr(getASCIIChar(b), la, false);
                }
            }
            int numFields = bk.getNextByte() << 8 & 0xff00;
            numFields |= bk.getNextByte() & 0xff;
            if (numFields > 0) {
                int x = 0;
                int attr = 0;
                int fPos = 0;
                int fLen = 0;
                int ffw1 = 0;
                int ffw2 = 0;
                int fcw1 = 0;
                int fcw2 = 0;
                boolean mdt = false;
                ScreenField sf = null;
                while (x < numFields) {
                    attr = bk.getNextByte();
                    fPos = bk.getNextByte() << 8 & 0xff00;
                    fPos |= bk.getNextByte() & 0xff;
                    if (bk.getNextByte() == 1) mdt = true; else mdt = false;
                    fLen = bk.getNextByte() << 8 & 0xff00;
                    fLen |= bk.getNextByte() & 0xff;
                    ffw1 = bk.getNextByte();
                    ffw2 = bk.getNextByte();
                    fcw1 = bk.getNextByte();
                    fcw2 = bk.getNextByte();
                    sf = screen52.getScreenFields().setField(attr, screen52.getRow(fPos), screen52.getCol(fPos), fLen, ffw1, ffw2, fcw1, fcw2);
                    if (mdt) sf.setMDT();
                    x++;
                }
            }
            screen52.restoreScreen();
            screen52.homePos = hPos;
            screen52.goto_XY(pos);
            screen52.isInField();
            if (screen52.isUsingGuiInterface()) screen52.drawFields();
        } catch (Exception e) {
            System.out.println("error restoring screen " + which + " with " + e.getMessage());
        }
    }

    public final boolean waitingForInput() {
        return waitingForInput;
    }

    private void parseIncoming() {
        boolean controlChars = false;
        byte control0;
        byte control1;
        boolean done = false;
        boolean error = false;
        try {
            while (bk.hasNext() && !done) {
                byte b = bk.getNextByte();
                switch(b) {
                    case 0:
                    case 1:
                        break;
                    case CMD_SAVE_SCREEN:
                    case 3:
                        saveScreen();
                        break;
                    case ESC:
                        break;
                    case 7:
                        Toolkit.getDefaultToolkit().beep();
                        bk.getNextByte();
                        bk.getNextByte();
                        break;
                    case CMD_WRITE_TO_DISPLAY:
                        error = writeToDisplay(true);
                        break;
                    case CMD_RESTORE_SCREEN:
                    case 13:
                        restoreScreen();
                        break;
                    case CMD_CLEAR_UNIT_ALTERNATE:
                        int param = bk.getNextByte();
                        if (param != 0) {
                            sendNegResponse(NR_REQUEST_ERROR, 03, 01, 05, " clear unit alternate not supported");
                            done = true;
                        } else {
                            if (screen52.getRows() != 27) screen52.setRowsCols(27, 132);
                            screen52.clearAll();
                        }
                        break;
                    case CMD_WRITE_ERROR_CODE:
                        writeErrorCode();
                        error = writeToDisplay(false);
                        break;
                    case CMD_WRITE_ERROR_CODE_TO_WINDOW:
                        writeErrorCodeToWindow();
                        error = writeToDisplay(false);
                        break;
                    case CMD_READ_SCREEN_IMMEDIATE:
                    case CMD_READ_SCREEN_TO_PRINT:
                        readScreen();
                        break;
                    case CMD_CLEAR_UNIT:
                        if (screen52.getRows() != 24) screen52.setRowsCols(24, 80);
                        screen52.clearAll();
                        break;
                    case CMD_CLEAR_FORMAT_TABLE:
                        screen52.clearTable();
                        break;
                    case CMD_READ_INPUT_FIELDS:
                    case CMD_READ_MDT_FIELDS:
                        bk.getNextByte();
                        bk.getNextByte();
                        readType = b;
                        screen52.goHome();
                        waitingForInput = true;
                        screen52.setKeyboardLocked(false);
                        break;
                    case CMD_READ_MDT_IMMEDIATE_ALT:
                        readType = b;
                        readImmediate(readType);
                        break;
                    case CMD_WRITE_STRUCTURED_FIELD:
                        writeStructuredField();
                        break;
                    default:
                        done = true;
                        sendNegResponse(NR_REQUEST_ERROR, 03, 01, 01, "parseIncoming");
                        break;
                }
                if (error) done = true;
            }
        } catch (Exception exc) {
            System.out.println("incoming " + exc.getMessage());
        }
        ;
    }

    /**
    * This routine handles sending negative responses back to the host.
    *
    *    You can find a description of the types of responses to be sent back
    *    by looking at section 12.4 of the 5250 Functions Reference manual
    *
    *
    */
    private void sendNegResponse(int cat, int modifier, int uByte1, int uByte2, String from) {
        try {
            int os = bk.getByteOffset(-1) & 0xf0;
            int cp = (bk.getCurrentPos() - 1);
            System.out.println("invalid " + from + " command " + os + " at pos " + cp);
        } catch (Exception e) {
            System.out.println("Send Negative Response error " + e.getMessage());
        }
        baosp.write(cat);
        baosp.write(modifier);
        baosp.write(uByte1);
        baosp.write(uByte2);
        try {
            writeGDS(128, 0, baosp.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        baosp.reset();
    }

    public void sendNegResponse2(int ec) {
        baosp.write(0x00);
        baosp.write(ec);
        try {
            writeGDS(1, 0, baosp.toByteArray());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        baosp.reset();
    }

    private boolean writeToDisplay(boolean controlsExist) {
        int pos = 0;
        boolean error = false;
        boolean done = false;
        int attr;
        byte nextOne;
        byte control0 = 0;
        byte control1 = 0;
        int saRows = screen52.getRows();
        int saCols = screen52.getCols();
        try {
            if (controlsExist) {
                control0 = bk.getNextByte();
                control1 = bk.getNextByte();
                processCC0(control0);
            }
            while (bk.hasNext() && !done) {
                switch(bk.getNextByte()) {
                    case 1:
                        error = processSOH();
                        break;
                    case 02:
                        int row = screen52.getCurrentRow();
                        int col = screen52.getCurrentCol();
                        int toRow = bk.getNextByte();
                        int toCol = bk.getNextByte() & 0xff;
                        if (toRow >= row) {
                            int repeat = bk.getNextByte();
                            if (row == 1 && col == 2 && toRow == screen52.getRows() && toCol == screen52.getCols()) screen52.clearScreen(); else {
                                if (repeat != 0) repeat = getASCIIChar(repeat);
                                int times = ((toRow * screen52.getCols()) + toCol) - ((row * screen52.getCols()) + col);
                                while (times-- >= 0) {
                                    screen52.setChar(repeat);
                                }
                            }
                        } else {
                            sendNegResponse(NR_REQUEST_ERROR, 0x05, 0x01, 0x23, " RA invalid");
                            error = true;
                        }
                        break;
                    case 03:
                        int EArow = screen52.getCurrentRow();
                        int EAcol = screen52.getCurrentCol();
                        int toEARow = bk.getNextByte();
                        int toEACol = bk.getNextByte() & 0xff;
                        int EALength = bk.getNextByte() & 0xff;
                        while (--EALength > 0) {
                            bk.getNextByte();
                        }
                        char EAAttr = (char) 0;
                        if (EArow == 1 && EAcol == 2 && toEARow == screen52.getRows() && toEACol == screen52.getCols()) screen52.clearScreen(); else {
                            int times = ((toEARow * screen52.getCols()) + toEACol) - ((EArow * screen52.getCols()) + EAcol);
                            while (times-- >= 0) {
                                screen52.setChar(EAAttr);
                            }
                        }
                        break;
                    case 04:
                        done = true;
                        break;
                    case 16:
                        bk.getNextByte();
                        int j = bk.getNextByte();
                        while (j-- > 0) bk.getNextByte();
                        break;
                    case 17:
                        int saRow = bk.getNextByte();
                        int saCol = bk.getNextByte() & 0xff;
                        if (saRow >= 0 && saRow <= screen52.getRows() && saCol >= 0 && saCol <= screen52.getCols()) {
                            screen52.goto_XY(saRow, saCol);
                        } else {
                            sendNegResponse(NR_REQUEST_ERROR, 0x05, 0x01, 0x22, "invalid row/col order" + " saRow = " + saRow + " saRows = " + screen52.getRows() + " saCol = " + saCol);
                            error = true;
                        }
                        break;
                    case 18:
                        bk.getNextByte();
                        bk.getNextByte();
                        break;
                    case 19:
                    case 20:
                        int icX = bk.getNextByte();
                        int icY = bk.getNextByte() & 0xff;
                        if (icX >= 0 && icX <= saRows && icY >= 0 && icY <= saCols) screen52.setPendingInsert(true, icX, icY); else {
                            sendNegResponse(NR_REQUEST_ERROR, 0x05, 0x01, 0x22, " IC/IM position invalid ");
                            error = true;
                        }
                        break;
                    case 21:
                        error = writeToDisplayStructuredField();
                        break;
                    case 29:
                        int fcw1 = 0;
                        int fcw2 = 0;
                        int ffw1 = 0;
                        int ffw0 = bk.getNextByte();
                        if (!isAttribute(ffw0)) {
                            ffw1 = bk.getNextByte();
                            fcw1 = bk.getNextByte();
                            if (!isAttribute(fcw1)) {
                                fcw2 = bk.getNextByte();
                                attr = bk.getNextByte();
                            } else {
                                attr = fcw1;
                                fcw1 = 0;
                            }
                        } else {
                            attr = ffw0;
                        }
                        int fLength = (bk.getNextByte() & 0xff) << 8 | bk.getNextByte() & 0xff;
                        screen52.addField(attr, fLength, ffw0, ffw1, fcw1, fcw2);
                        break;
                    default:
                        byte byte0 = bk.getByteOffset(-1);
                        if (isAttribute(byte0)) {
                            screen52.setAttr(byte0);
                        } else {
                            if (!screen52.isStatusErrorCode()) {
                                if (!isData(byte0)) {
                                    screen52.setChar(byte0);
                                } else screen52.setChar(codePage.ebcdic2uni(byte0));
                            } else {
                                if (byte0 == 0) screen52.setChar(byte0); else screen52.setChar(getASCIIChar(byte0));
                            }
                        }
                        break;
                }
                if (error) done = true;
            }
        } catch (Exception e) {
            System.out.println("write to display " + e.getMessage());
        }
        ;
        processCC1(control1);
        return error;
    }

    private boolean processSOH() throws Exception {
        int l = bk.getNextByte();
        if (l > 0 && l <= 7) {
            bk.getNextByte();
            bk.getNextByte();
            bk.getNextByte();
            screen52.setErrorLine(bk.getNextByte());
            int byte1 = 0;
            if (l >= 5) {
                byte1 = bk.getNextByte();
                dataIncluded[23] = (byte1 & 0x80) == 0x80;
                dataIncluded[22] = (byte1 & 0x40) == 0x40;
                dataIncluded[21] = (byte1 & 0x20) == 0x20;
                dataIncluded[20] = (byte1 & 0x10) == 0x10;
                dataIncluded[19] = (byte1 & 0x8) == 0x8;
                dataIncluded[18] = (byte1 & 0x4) == 0x4;
                dataIncluded[17] = (byte1 & 0x2) == 0x2;
                dataIncluded[16] = (byte1 & 0x1) == 0x1;
            }
            if (l >= 6) {
                byte1 = bk.getNextByte();
                dataIncluded[15] = (byte1 & 0x80) == 0x80;
                dataIncluded[14] = (byte1 & 0x40) == 0x40;
                dataIncluded[13] = (byte1 & 0x20) == 0x20;
                dataIncluded[12] = (byte1 & 0x10) == 0x10;
                dataIncluded[11] = (byte1 & 0x8) == 0x8;
                dataIncluded[10] = (byte1 & 0x4) == 0x4;
                dataIncluded[9] = (byte1 & 0x2) == 0x2;
                dataIncluded[8] = (byte1 & 0x1) == 0x1;
            }
            if (l >= 7) {
                byte1 = bk.getNextByte();
                dataIncluded[7] = (byte1 & 0x80) == 0x80;
                dataIncluded[6] = (byte1 & 0x40) == 0x40;
                dataIncluded[5] = (byte1 & 0x20) == 0x20;
                dataIncluded[4] = (byte1 & 0x10) == 0x10;
                dataIncluded[3] = (byte1 & 0x8) == 0x8;
                dataIncluded[2] = (byte1 & 0x4) == 0x4;
                dataIncluded[1] = (byte1 & 0x2) == 0x2;
                dataIncluded[0] = (byte1 & 0x1) == 0x1;
            }
            screen52.clearTable();
            return false;
        } else {
            sendNegResponse(NR_REQUEST_ERROR, 0x05, 0x01, 0x2B, "invalid SOH length");
            return true;
        }
    }

    private void processCC0(byte byte0) {
        boolean lockKeyboard = true;
        boolean resetMDT = false;
        boolean resetMDTAll = false;
        boolean nullMDT = false;
        boolean nullAll = false;
        if ((byte0 & 0xE0) == 0x00) {
            lockKeyboard = false;
        }
        switch(byte0 & 0xE0) {
            case 0x40:
                resetMDT = true;
                break;
            case 0x60:
                resetMDTAll = true;
                break;
            case 0x80:
                nullMDT = true;
                break;
            case 0xA0:
                resetMDT = true;
                nullAll = true;
                break;
            case 0xC0:
                resetMDT = true;
                nullMDT = true;
                break;
            case 0xE0:
                resetMDTAll = true;
                nullAll = true;
                break;
        }
        if (lockKeyboard) {
            screen52.setKeyboardLocked(true);
        }
        if (resetMDT || resetMDTAll || nullMDT || nullAll) {
            ScreenField sf;
            int f = screen52.getScreenFields().getSize();
            for (int x = 0; x < f; x++) {
                sf = screen52.getScreenFields().getField(x);
                if (!sf.isBypassField()) {
                    if ((nullMDT && sf.mdt) || nullAll) {
                        sf.setFieldChar((char) 0x0);
                        screen52.drawField(sf);
                    }
                }
                if (resetMDTAll || (resetMDT && !sf.isBypassField())) sf.resetMDT();
            }
            sf = null;
        }
    }

    private void processCC1(byte byte1) {
        if ((byte1 & 0x04) == 0x04) {
            Toolkit.getDefaultToolkit().beep();
        }
        if ((byte1 & 0x02) == 0x02) {
            screen52.setMessageLightOff();
        }
        if ((byte1 & 0x01) == 0x01) {
            screen52.setMessageLightOn();
        }
        if ((byte1 & 0x01) == 0x01 && (byte1 & 0x02) == 0x02) {
            screen52.setMessageLightOn();
        }
        if ((byte1 & 0x20) == 0x20 && (byte1 & 0x08) == 0x00) {
            screen52.setPendingInsert(false, 1, 1);
        }
        if ((byte1 & 0x20) == 0x20 && enhanced) {
            cursorOn = true;
        }
        if (!screen52.isStatusErrorCode() && (byte1 & 0x08) == 0x08) {
            cursorOn = true;
        }
        if ((byte1 & 0x20) == 0x20 && (byte1 & 0x08) == 0x00) {
            screen52.setPendingInsert(false, 1, 1);
        }
    }

    private boolean isAttribute(int byte0) {
        int byte1 = byte0 & 0xff;
        return (byte1 & 0xe0) == 0x20;
    }

    private boolean isData(int byte0) {
        int byte1 = byte0 & 0xff;
        if (byte1 >= 64 && byte1 < 255) return true; else return false;
    }

    private boolean writeToDisplayStructuredField() {
        boolean error = false;
        boolean done = false;
        int nextone;
        try {
            int length = ((bk.getNextByte() & 0xff) << 8 | (bk.getNextByte() & 0xff));
            while (!done) {
                int s = bk.getNextByte() & 0xff;
                switch(s) {
                    case 0xD9:
                        switch(bk.getNextByte()) {
                            case 0x51:
                                boolean cr = false;
                                int rows = 0;
                                int cols = 0;
                                if ((bk.getNextByte() & 0x80) == 0x80) cr = true;
                                bk.getNextByte();
                                bk.getNextByte();
                                rows = bk.getNextByte();
                                cols = bk.getNextByte();
                                length -= 9;
                                if (length == 0) {
                                    done = true;
                                    screen52.createWindow(rows, cols, 1, true, 32, 58, '.', '.', '.', ':', ':', ':', '.', ':');
                                    break;
                                }
                                int ml = 0;
                                int type = 0;
                                int lastPos = screen52.getLastPos();
                                int mAttr = 0;
                                int cAttr = 0;
                                while (length > 0) {
                                    ml = (bk.getNextByte() & 0xff);
                                    length -= ml;
                                    type = bk.getNextByte();
                                    switch(type) {
                                        case 0x01:
                                            boolean gui = false;
                                            if ((bk.getNextByte() & 0x80) == 0x80) gui = true;
                                            mAttr = bk.getNextByte();
                                            cAttr = bk.getNextByte();
                                            char ul = '.';
                                            char upper = '.';
                                            char ur = '.';
                                            char left = ':';
                                            char right = ':';
                                            char ll = ':';
                                            char bottom = '.';
                                            char lr = ':';
                                            if (ml > 5) {
                                                ul = getASCIIChar(bk.getNextByte());
                                                if (ul == 0) ul = '.';
                                                upper = getASCIIChar(bk.getNextByte());
                                                if (upper == 0) upper = '.';
                                                ur = getASCIIChar(bk.getNextByte());
                                                if (ur == 0) ur = '.';
                                                left = getASCIIChar(bk.getNextByte());
                                                if (left == 0) left = ':';
                                                right = getASCIIChar(bk.getNextByte());
                                                if (right == 0) right = ':';
                                                ll = getASCIIChar(bk.getNextByte());
                                                if (ll == 0) ll = ':';
                                                bottom = getASCIIChar(bk.getNextByte());
                                                if (bottom == 0) bottom = '.';
                                                lr = getASCIIChar(bk.getNextByte());
                                                if (lr == 0) lr = ':';
                                            }
                                            screen52.createWindow(rows, cols, type, gui, mAttr, cAttr, ul, upper, ur, left, right, ll, bottom, lr);
                                            break;
                                        case 0x10:
                                            byte orientation = bk.getNextByte();
                                            mAttr = bk.getNextByte();
                                            cAttr = bk.getNextByte();
                                            bk.getNextByte();
                                            ml -= 6;
                                            StringBuffer hfBuffer = new StringBuffer(ml);
                                            while (ml-- > 0) {
                                                hfBuffer.append(getASCIIChar(bk.getNextByte()));
                                            }
                                            System.out.println(" orientation " + Integer.toBinaryString(orientation) + " mAttr " + mAttr + " cAttr " + cAttr + " Header/Footer " + hfBuffer);
                                            screen52.writeWindowTitle(lastPos, rows, cols, orientation, mAttr, cAttr, hfBuffer);
                                            break;
                                        default:
                                            System.out.println("Invalid Window minor structure");
                                            length = 0;
                                            done = true;
                                    }
                                }
                                done = true;
                                break;
                            case 0x53:
                                int sblen = 15;
                                byte sbflag = bk.getNextByte();
                                bk.getNextByte();
                                int totalRowScrollable = ((bk.getNextByte() & 0xff) << 8 | (bk.getNextByte() & 0xff));
                                int totalColScrollable = ((bk.getNextByte() & 0xff) << 8 | (bk.getNextByte() & 0xff));
                                int sliderRowPos = ((bk.getNextByte() & 0xff) << 8 | (bk.getNextByte() & 0xff));
                                int sliderColPos = ((bk.getNextByte() & 0xff) << 8 | (bk.getNextByte() & 0xff));
                                int sliderRC = bk.getNextByte();
                                screen52.createScrollBar(sbflag, totalRowScrollable, totalColScrollable, sliderRowPos, sliderColPos, sliderRC);
                                length -= 15;
                                done = true;
                                break;
                            case 0x5B:
                                bk.getNextByte();
                                bk.getNextByte();
                                done = true;
                                break;
                            case 0x5F:
                                int len = 4;
                                int d = 0;
                                length -= s;
                                while (--len > 0) d = bk.getNextByte();
                                screen52.clearTable();
                                done = true;
                                break;
                            case 0x60:
                                len = 6;
                                d = 0;
                                length -= 9;
                                while (--len > 0) d = bk.getNextByte();
                                if (length > 0) {
                                    len = (bk.getNextByte() & 0xff) << 8;
                                    while (--len > 0) {
                                        d = bk.getNextByte();
                                    }
                                }
                                done = true;
                                break;
                            default:
                                sendNegResponse(NR_REQUEST_ERROR, 0x03, 0x01, 0x01, "invalid wtd structured field sub command " + bk.getByteOffset(-1));
                                error = true;
                                break;
                        }
                        break;
                    default:
                        sendNegResponse(NR_REQUEST_ERROR, 0x03, 0x01, 0x01, "invalid wtd structured field command " + bk.getByteOffset(-1));
                        error = true;
                        break;
                }
                if (error) done = true;
            }
        } catch (Exception e) {
        }
        ;
        return error;
    }

    private void defineSelectionField(int majLen) {
        try {
            int flag1 = bk.getNextByte();
            int flag2 = bk.getNextByte();
            int flag3 = bk.getNextByte();
            int typeSelection = bk.getNextByte();
            int guiDevice = bk.getNextByte();
            int withMnemonic = bk.getNextByte();
            int noMnemonic = bk.getNextByte();
            bk.getNextByte();
            bk.getNextByte();
            int cols = bk.getNextByte();
            int rows = bk.getNextByte();
            int maxColChoice = bk.getNextByte();
            int padding = bk.getNextByte();
            int numSepChar = bk.getNextByte();
            int ctySepChar = bk.getNextByte();
            int cancelAID = bk.getNextByte();
            int cnt = 0;
            int minLen = 0;
            majLen -= 21;
            System.out.println(" row: " + screen52.getCurrentRow() + " col: " + screen52.getCurrentCol() + " type " + typeSelection + " gui " + guiDevice + "withMnemonic " + withMnemonic + " cols " + cols + " rows " + rows);
            do {
                minLen = bk.getNextByte();
                int minType = bk.getNextByte();
                switch(minType) {
                    case 0x01:
                        int flagCP1 = bk.getNextByte();
                        bk.getNextByte();
                        int colSelAvail = bk.getNextByte();
                        bk.getNextByte();
                        int colSelCur = bk.getNextByte();
                        bk.getNextByte();
                        int colSelNotAvail = bk.getNextByte();
                        bk.getNextByte();
                        int colAvail = bk.getNextByte();
                        bk.getNextByte();
                        int colSel = bk.getNextByte();
                        bk.getNextByte();
                        int colNotAvail = bk.getNextByte();
                        bk.getNextByte();
                        int colInd = bk.getNextByte();
                        bk.getNextByte();
                        int colNotAvailInd = bk.getNextByte();
                        break;
                    case 0x10:
                        cnt = 5;
                        int flagCT1 = bk.getNextByte();
                        int flagCT2 = bk.getNextByte();
                        int flagCT3 = bk.getNextByte();
                        int mnemOffset = 0;
                        boolean aid = false;
                        if ((flagCT1 & 0x08) == 8) {
                            System.out.println(" mnemOffset " + mnemOffset);
                            mnemOffset = bk.getNextByte();
                            cnt++;
                        }
                        if ((flagCT1 & 0x04) == 4) {
                            aid = true;
                            System.out.println(" aidKey " + aid);
                        }
                        if ((flagCT1 & 0x01) == 0x01) {
                            System.out.println(" single digit ");
                            bk.getNextByte();
                            cnt++;
                        }
                        if ((flagCT1 & 0x02) == 0x02) {
                            System.out.println(" double digit ");
                            bk.getNextByte();
                            cnt++;
                        }
                        String s = "";
                        byte byte0 = 0;
                        for (; cnt < minLen; cnt++) {
                            byte0 = bk.getNextByte();
                            s += ebcdic2uni(byte0);
                            screen52.setChar(ebcdic2uni(byte0));
                        }
                        System.out.println(s);
                        break;
                    default:
                        for (cnt = 2; cnt < minLen; cnt++) {
                            bk.getNextByte();
                        }
                }
                majLen -= minLen;
            } while (majLen > 0);
        } catch (Exception exc) {
            System.out.println(" defineSelectionField :" + exc.getMessage());
            exc.printStackTrace();
        }
    }

    private void writeStructuredField() {
        boolean done = false;
        int nextone;
        try {
            int length = ((bk.getNextByte() & 0xff) << 8 | (bk.getNextByte() & 0xff));
            while (bk.hasNext() && !done) {
                switch(bk.getNextByte()) {
                    case -39:
                        switch(bk.getNextByte()) {
                            case 112:
                                bk.getNextByte();
                                sendQueryResponse();
                                break;
                            default:
                                System.out.println("invalid structured field sub command " + bk.getByteOffset(-1));
                                break;
                        }
                        break;
                    default:
                        System.out.println("invalid structured field command " + bk.getByteOffset(-1));
                        break;
                }
            }
        } catch (Exception e) {
        }
        ;
    }

    private final void writeErrorCode() throws Exception {
        screen52.goto_XY(screen52.getErrorLine(), 1);
        screen52.saveErrorLine();
        screen52.setStatus(screen52.STATUS_ERROR_CODE, screen52.STATUS_VALUE_ON, null);
        cursorOn = true;
    }

    private final void writeErrorCodeToWindow() throws Exception {
        int fromCol = bk.getNextByte() & 0xff;
        int toCol = bk.getNextByte() & 0xff;
        screen52.goto_XY(screen52.getErrorLine(), fromCol);
        screen52.saveErrorLine();
        screen52.setStatus(screen52.STATUS_ERROR_CODE, screen52.STATUS_VALUE_ON, null);
        cursorOn = true;
    }

    /**
    * Method sendQueryResponse
    *
    * The query command is used to obtain information about the capabilities
    * of the 5250 display.
    *
    * The Query command must follow an Escape (0x04) and Write Structured
    * Field command (0xF3).
    *
    * This section is modeled after the rfc1205 - 5250 Telnet Interface section
    * 5.3
    */
    public final void sendQueryResponse() throws IOException {
        System.out.println("sending query response");
        byte abyte0[] = new byte[64];
        abyte0[0] = 0;
        abyte0[1] = 0;
        abyte0[2] = -120;
        if (enhanced == true) {
            abyte0[3] = 0;
            abyte0[4] = 64;
        } else {
            abyte0[3] = 0;
            abyte0[4] = 58;
        }
        abyte0[5] = -39;
        abyte0[6] = 112;
        abyte0[7] = -128;
        abyte0[8] = 6;
        abyte0[9] = 0;
        abyte0[10] = 1;
        abyte0[11] = 1;
        abyte0[12] = 0;
        abyte0[13] = 0;
        abyte0[14] = 0;
        abyte0[15] = 0;
        abyte0[16] = 0;
        abyte0[17] = 0;
        abyte0[18] = 0;
        abyte0[19] = 0;
        abyte0[20] = 0;
        abyte0[21] = 0;
        abyte0[22] = 0;
        abyte0[23] = 0;
        abyte0[24] = 0;
        abyte0[25] = 0;
        abyte0[26] = 0;
        abyte0[27] = 0;
        abyte0[28] = 0;
        abyte0[29] = 1;
        abyte0[30] = getEBCDIC('5');
        abyte0[31] = getEBCDIC('2');
        abyte0[32] = getEBCDIC('5');
        abyte0[33] = getEBCDIC('1');
        abyte0[34] = getEBCDIC('0');
        abyte0[35] = getEBCDIC('1');
        abyte0[36] = getEBCDIC('1');
        abyte0[37] = 2;
        abyte0[38] = 0;
        abyte0[39] = 0;
        abyte0[40] = 0;
        abyte0[41] = 36;
        abyte0[42] = 36;
        abyte0[43] = 0;
        abyte0[44] = 1;
        abyte0[45] = 0;
        abyte0[46] = 0;
        abyte0[47] = 0;
        abyte0[48] = 0;
        abyte0[49] = 1;
        abyte0[50] = 16;
        abyte0[51] = 0;
        abyte0[52] = 0;
        if (enhanced == true) {
            abyte0[53] = 0x5E;
            System.out.println("enhanced options");
        } else abyte0[53] = 0x0;
        abyte0[54] = 24;
        abyte0[55] = 0;
        abyte0[56] = 0;
        abyte0[57] = 0;
        abyte0[58] = 0;
        abyte0[59] = 0;
        abyte0[60] = 0;
        abyte0[61] = 0;
        abyte0[62] = 0;
        abyte0[63] = 0;
        writeGDS(0, 0, abyte0);
        abyte0 = null;
    }

    protected final boolean negotiate(byte abyte0[]) throws IOException {
        int i = 0;
        if (abyte0[i] == IAC) {
            while (i < abyte0.length && abyte0[i++] == -1) switch(abyte0[i++]) {
                case WONT:
                default:
                    break;
                case DO:
                    switch(abyte0[i]) {
                        case TERMINAL_TYPE:
                            baosp.write(IAC);
                            baosp.write(WILL);
                            baosp.write(TERMINAL_TYPE);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                        case OPT_END_OF_RECORD:
                            baosp.write(IAC);
                            baosp.write(WILL);
                            baosp.write(OPT_END_OF_RECORD);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                        case TRANSMIT_BINARY:
                            baosp.write(IAC);
                            baosp.write(WILL);
                            baosp.write(TRANSMIT_BINARY);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                        case TIMING_MARK:
                            baosp.write(IAC);
                            baosp.write(WONT);
                            baosp.write(TIMING_MARK);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                        case NEW_ENVIRONMENT:
                            if (devName == null) {
                                baosp.write(IAC);
                                baosp.write(WONT);
                                baosp.write(NEW_ENVIRONMENT);
                                writeByte(baosp.toByteArray());
                                baosp.reset();
                            } else {
                                System.out.println(devName);
                                baosp.write(IAC);
                                baosp.write(WILL);
                                baosp.write(NEW_ENVIRONMENT);
                                writeByte(baosp.toByteArray());
                                baosp.reset();
                            }
                            break;
                        default:
                            baosp.write(IAC);
                            baosp.write(WONT);
                            baosp.write(abyte0[i]);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                    }
                    i++;
                    break;
                case WILL:
                    switch(abyte0[i]) {
                        case OPT_END_OF_RECORD:
                            baosp.write(IAC);
                            baosp.write(DO);
                            baosp.write(OPT_END_OF_RECORD);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                        case TRANSMIT_BINARY:
                            baosp.write(IAC);
                            baosp.write(DO);
                            baosp.write(TRANSMIT_BINARY);
                            writeByte(baosp.toByteArray());
                            baosp.reset();
                            break;
                    }
                    i++;
                    break;
                case SB:
                    if (abyte0[i] == NEW_ENVIRONMENT && abyte0[i + 1] == 1) {
                        negNewEnvironment();
                        i++;
                    }
                    if (abyte0[i] == TERMINAL_TYPE && abyte0[i + 1] == 1) {
                        baosp.write(IAC);
                        baosp.write(SB);
                        baosp.write(TERMINAL_TYPE);
                        baosp.write(QUAL_IS);
                        if (!support132) baosp.write((new String("IBM-3179-2")).getBytes()); else baosp.write((new String("IBM-3477-FC")).getBytes());
                        baosp.write(IAC);
                        baosp.write(SE);
                        writeByte(baosp.toByteArray());
                        baosp.reset();
                        i++;
                    }
                    i++;
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
    * Negotiate new environment string for device name
    */
    private void negNewEnvironment() throws IOException {
        baosp.write(IAC);
        baosp.write(SB);
        baosp.write(NEW_ENVIRONMENT);
        baosp.write(IS);
        baosp.write(USERVAR);
        baosp.write((new String("DEVNAME")).getBytes());
        baosp.write(VALUE);
        baosp.write(negDeviceName().getBytes());
        baosp.write(IAC);
        baosp.write(SE);
        writeByte(baosp.toByteArray());
        baosp.reset();
    }

    /**
    * This will negotiate a device name with controller.
    *    if the sequence is less than zero then it will send the device name
    *    as specified.  On each unsuccessful attempt a sequential number is
    *    appended until we find one or the controller says no way.
    */
    private String negDeviceName() {
        if (devSeq++ == -1) {
            devNameUsed = devName;
            return devName;
        } else {
            StringBuffer sb = new StringBuffer(devName + devSeq);
            int ei = 1;
            while (sb.length() > 10) {
                sb.setLength(0);
                sb.append(devName.substring(0, devName.length() - ei++));
                sb.append(devSeq);
            }
            devNameUsed = sb.toString();
            return devNameUsed;
        }
    }

    public final void setCodePage(String cp) {
        if (this.codePage == null) {
            codePage = new CodePage(cp);
        } else {
            codePage.setCodePage(cp);
        }
    }

    public final CodePage getCodePage() {
        return codePage;
    }

    public final Dimension getPreferredSize() {
        return screen52.getPreferredSize();
    }

    public byte getEBCDIC(int index) {
        return codePage.getEBCDIC(index);
    }

    public char getEBCDICChar(int index) {
        return codePage.getEBCDICChar(index);
    }

    public byte getASCII(int index) {
        return codePage.getASCII(index);
    }

    public char getASCIIChar(int index) {
        return codePage.getASCIIChar(index);
    }

    public char ebcdic2uni(int index) {
        return codePage.ebcdic2uni(index);
    }

    public byte uni2ebcdic(char index) {
        return codePage.uni2ebcdic(index);
    }

    public void dumpScreen() {
        for (int y = 0; y < screen52.getRows(); y++) {
            System.out.print("row :" + (y + 1) + " ");
            for (int x = 0; x < screen52.getCols(); x++) {
                System.out.println("row " + (y + 1) + " col " + (x + 1) + " " + screen52.screen[y * x].toString());
            }
        }
    }

    public void dump(byte[] abyte0) {
        try {
            System.out.print("\n Buffer Dump of data from AS400: ");
            dw.write("\r\n Buffer Dump of data from AS400: ".getBytes());
            StringBuffer h = new StringBuffer();
            for (int x = 0; x < abyte0.length; x++) {
                if (x % 16 == 0) {
                    System.out.println("  " + h.toString());
                    dw.write(("  " + h.toString() + "\r\n").getBytes());
                    h.setLength(0);
                    h.append("+0000");
                    h.setLength(5 - Integer.toHexString(x).length());
                    h.append(Integer.toHexString(x).toUpperCase());
                    System.out.print(h.toString());
                    dw.write(h.toString().getBytes());
                    h.setLength(0);
                }
                char ac = getASCIIChar(abyte0[x]);
                if (ac < ' ') h.append('.'); else h.append(ac);
                if (x % 4 == 0) {
                    System.out.print(" ");
                    dw.write((" ").getBytes());
                }
                if (Integer.toHexString(abyte0[x] & 0xff).length() == 1) {
                    System.out.print("0" + Integer.toHexString(abyte0[x] & 0xff).toUpperCase());
                    dw.write(("0" + Integer.toHexString(abyte0[x] & 0xff).toUpperCase()).getBytes());
                } else {
                    System.out.print(Integer.toHexString(abyte0[x] & 0xff).toUpperCase());
                    dw.write((Integer.toHexString(abyte0[x] & 0xff).toUpperCase()).getBytes());
                }
            }
            System.out.println();
            dw.write("\r\n".getBytes());
            dw.flush();
        } catch (EOFException _ex) {
        } catch (Exception _ex) {
            System.out.println("Cannot dump from host\n\r");
        }
    }

    public void dumpBytes() {
        byte shit[] = bk.buffer;
        for (int i = 0; i < shit.length; i++) System.out.println(i + ">" + shit[i] + "< - ascii - >" + getASCIIChar(shit[i]) + "<");
    }

    public void dumpHexBytes(byte[] abyte) {
        byte shit[] = abyte;
        for (int i = 0; i < shit.length; i++) System.out.println(i + ">" + shit[i] + "< hex >" + Integer.toHexString((shit[i] & 0xff)));
    }

    private static final byte IAC = (byte) -1;

    private static final byte DONT = (byte) -2;

    private static final byte DO = (byte) -3;

    private static final byte WONT = (byte) -4;

    private static final byte WILL = (byte) -5;

    private static final byte SB = (byte) -6;

    private static final byte SE = (byte) -16;

    private static final byte EOR = (byte) -17;

    private static final byte TERMINAL_TYPE = (byte) 24;

    private static final byte OPT_END_OF_RECORD = (byte) 25;

    private static final byte TRANSMIT_BINARY = (byte) 0;

    private static final byte QUAL_IS = (byte) 0;

    private static final byte TIMING_MARK = (byte) 6;

    private static final byte NEW_ENVIRONMENT = (byte) 39;

    private static final byte IS = (byte) 0;

    private static final byte SEND = (byte) 1;

    private static final byte INFO = (byte) 2;

    private static final byte VAR = (byte) 0;

    private static final byte VALUE = (byte) 1;

    private static final byte NEGOTIATE_ESC = (byte) 2;

    private static final byte USERVAR = (byte) 3;

    private static final byte ESC = 0x04;

    private static final char char0 = 0;
}
