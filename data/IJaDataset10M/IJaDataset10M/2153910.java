package freets.gui.console;

import freets.data.*;
import freets.data.settings.*;
import freets.tools.*;
import freets.eibinterf.*;
import eibstack.*;
import eibstack.layer7.*;
import java.util.*;
import java.io.*;
import javax.swing.Timer;
import java.awt.event.*;

/**
 * The main class of the EIBConsole
 * 
 * @author T. F�rster
 * @version $Revision: 1.1.1.1 $
 */
public class EIBConsole implements Commands {

    public static final String VERSION_STRING = "EIB-console V0.1 beta (2000-10-17)";

    public static final String GREETINGS = "Welcome to EIB-console V0.1 beta  � 2000 T. F�rster & W. Sauter\n" + "This software is part of\n" + "freeTS - A Free Toolsoftware for Home and Building Automation\n" + "Enter \"help\" to get an overview.";

    protected Vector listeners;

    protected OutputListener logListener;

    protected PrintWriter logPrintWriter;

    protected EIBStack stack;

    protected CommonEIBTools tools;

    protected String deviceName;

    protected Busmonitor busmonitor;

    protected ScannerThread scannerThread;

    protected Vector blinkerThreads;

    protected boolean showPrompt;

    /**
     * Create new EIBConsole.
     * @param showPrompt <i>true</i> if an input prompt should be shown after command execution
     */
    public EIBConsole(boolean showPrompt) {
        listeners = new Vector();
        blinkerThreads = new Vector();
        this.showPrompt = showPrompt;
    }

    /**
     * Create new EIBConsole.
     */
    public EIBConsole() {
        this(true);
    }

    /**
     * Execute a command.
     * @param cmd the command line
     */
    public void execute(String cmd) {
        if (logListener != null) {
            logListener.output(cmd + "\n");
        }
        cmd = cmd.toLowerCase().trim();
        stopThreads();
        if (cmd.length() == 0) {
            dispatchOutput("");
            return;
        }
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].isCommand(cmd)) {
                if (ALL[i].equals(OPEN)) {
                    open(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(CLOSE)) {
                    close(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(RESET)) {
                    reset(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(RESTART)) {
                    restart(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(PROG)) {
                    prog(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(BLINK)) {
                    blink(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(PROGRAM_ADDR)) {
                    programAddr(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(LIST)) {
                    list(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(SCAN)) {
                    scan(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(MONITOR)) {
                    monitor(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(SEND)) {
                    send(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(PEEK)) {
                    peek(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(POKE)) {
                    poke(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(MASK)) {
                    mask(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(VERSION)) {
                    version(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(HELP)) {
                    help(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(LOG)) {
                    log(parseArgs(cmd));
                    return;
                } else if (ALL[i].equals(EXIT)) {
                    exit(parseArgs(cmd));
                    return;
                }
            }
        }
        error(cmd);
    }

    /**
     * Parse arguments in command line.
     * @param cmd the command line
     * @return string-array containing all arguments
     */
    private String[] parseArgs(String cmd) {
        StringTokenizer tok = new StringTokenizer(cmd);
        String[] res = new String[tok.countTokens() - 1];
        tok.nextToken();
        for (int i = 0; i < res.length; i++) {
            res[i] = tok.nextToken();
        }
        return res;
    }

    /**
     * Test, if we're connected to the bus, output error-message if not.
     * @return <i>true</i> if a connection to a device driver is valid
     */
    private boolean testIfConnected() {
        if (stack == null) {
            dispatchOutput("There is no device driver opened.");
        }
        return stack != null;
    }

    /**
     * Dispatch an error or ok message.
     * @param ok <i>true</i>, if command succeeded.
     * @param error the message to print if command failed
     */
    private void dispatchOkOrError(boolean ok, String error) {
        dispatchOutput(ok ? "Ready." : error);
    }

    /**
     * Stop scanner and busmonitor.
     */
    private void stopThreads() {
        if (scannerThread != null) {
            scannerThread.shutdown();
            scannerThread = null;
        }
        if (busmonitor != null) {
            busmonitor.shutdown();
            busmonitor = null;
        }
    }

    protected void open(String[] args) {
        if (args.length == 0) {
            dispatchError("Hardware-device must be specified.", OPEN.getUsage());
        } else if (args.length != 1) {
            dispatchError("Only one hardware-device can be specified.", OPEN.getUsage());
        } else {
            String res = "";
            if (stack != null) {
                res += "Closing old device driver first.\n";
                stack.cleanup();
            }
            String dev = args[0];
            int nr = -1;
            try {
                nr = Integer.parseInt(dev);
            } catch (NumberFormatException nfex) {
            }
            if ((nr != -1) && (nr >= 1) && (nr <= 4)) {
                if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                    dev = "COM" + nr;
                } else {
                    dev = "/dev/eib" + (nr - 1);
                }
            }
            deviceName = dev;
            try {
                res += "Opening device driver " + dev + ".";
                dispatchOutput(res, false);
                stack = new EIBStack(1, 0, 0, EIBStack.TP_TRCVR, dev);
                tools = new CommonEIBTools(stack);
            } catch (IOException ioex) {
                dispatchOutput("ERROR: Device driver cannot be openend " + "(Message: " + ioex.getMessage() + ")");
                return;
            }
            dispatchOutput("Ready.");
        }
    }

    protected void close(String[] args) {
        if (args.length != 0) {
            dispatchError("Close has no arguments.", CLOSE.getUsage());
        } else if (stack == null) {
            dispatchOutput("WARNING: There is no hardware-device opened.");
        } else {
            dispatchOutput("Closing device driver " + deviceName + ".", false);
            cleanup();
            dispatchOutput("Ready.");
        }
    }

    protected void reset(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length > 1) {
            dispatchOutput("Illegal parameters.\n" + RESET.getUsage());
            return;
        }
        if (args.length == 0) {
            dispatchOkOrError(tools.resetBCU(Tools.LOCAL_DEVICE), "Cannot reset local BCU.");
        } else {
            int addr = getAndCheckPhysicalAddress(args[0]);
            if (addr == -1) return;
            dispatchOkOrError(tools.resetBCU(addr), "Cannot reset device " + CommonEIBTools.decodePhysicalAddress(addr));
        }
    }

    protected void restart(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length != 1) {
            dispatchOutput("Illegal parameters.\nUsage: " + RESTART.getUsage());
            return;
        }
        int addr = getAndCheckPhysicalAddress(args[0]);
        if (addr == -1) return;
        dispatchOkOrError(tools.restartDevice(addr), "Cannot reset device " + CommonEIBTools.decodePhysicalAddress(addr));
    }

    private int getAndCheckPhysicalAddress(String s) {
        int addr;
        addr = CommonEIBTools.encodePhysicalAddress(s);
        if (s.equalsIgnoreCase("local")) {
            return Tools.LOCAL_DEVICE;
        }
        if (addr == -1) {
            try {
                addr = Integer.parseInt(s);
            } catch (NumberFormatException nfex) {
            }
        }
        if (addr == -1) {
            dispatchOutput("Error: Illegal physical address.");
        }
        return addr;
    }

    protected void prog(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length != 2) {
            dispatchOutput("Illegal parameters.\n" + PROG.getUsage());
            return;
        }
        boolean on;
        boolean all;
        if (args[0].equalsIgnoreCase("on")) {
            on = true;
        } else if (args[0].equalsIgnoreCase("off")) {
            on = false;
        } else {
            dispatchOutput("Illegal parameters.\n" + PROG.getUsage());
            return;
        }
        all = args[1].equalsIgnoreCase("all");
        if (!all) {
            int addr = getAndCheckPhysicalAddress(args[1]);
            if (addr == -1) return;
            dispatchOkOrError(on ? tools.programmingModeOn(addr) : tools.programmingModeOff(addr), "Cannot access device " + CommonEIBTools.decodePhysicalAddress(addr));
        } else {
            if (on) {
                dispatchOutput("\"all\" can only be used with \"on\"\n" + PROG.getUsage());
                return;
            }
            int[] addrs = tools.getDevicesInProgrammingMode();
            if (addrs == null) {
                dispatchOutput("Cannot scan devices.");
            } else if (addrs.length == 0) {
                dispatchOutput("There's no device in programming mode.");
            } else {
                dispatchOutput("Switching off " + addrs.length + ((addrs.length == 1) ? " Device." : " Devices."), false);
                for (int i = 0; i < addrs.length; i++) {
                    dispatchOutput(CommonEIBTools.decodePhysicalAddress(addrs[i]), false);
                    tools.programmingModeOff(addrs[i]);
                }
                dispatchOutput("Ready.");
            }
        }
    }

    protected void blink(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if ((args.length == 0) || (args.length > 3)) {
            dispatchOutput("Illegal parameters.\nUsage: " + BLINK.getUsage());
            return;
        }
        if ((args.length == 1) && (args[0].equalsIgnoreCase("stop"))) {
            for (int i = 0; i < blinkerThreads.size(); i++) {
                ((BlinkerThread) blinkerThreads.elementAt(i)).shutdown();
            }
            dispatchOutput("Ready.");
        } else if (args.length >= 1) {
            int delay = 1000;
            int count = 5;
            int addr = getAndCheckPhysicalAddress(args[0]);
            if (addr == -1) return;
            try {
                if (args.length >= 2) {
                    count = Integer.parseInt(args[1]);
                }
                if (args.length == 3) {
                    delay = Integer.parseInt(args[2]);
                }
            } catch (NumberFormatException nfex) {
                dispatchOutput("Illegal argument.\nUsage: " + BLINK.getUsage());
                return;
            }
            new BlinkerThread(addr, count, delay).start();
            dispatchOutput("Device " + CommonEIBTools.decodePhysicalAddress(addr) + " is blinking for " + count + " times. Call \"blink stop\" to abort.", false);
            dispatchOutput("Ready.");
        }
    }

    protected void programAddr(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length != 2) {
            dispatchOutput("Illegal parameters.\nUsage: " + PROGRAM_ADDR.getUsage());
            return;
        }
        int addr = getAndCheckPhysicalAddress(args[1]);
        if (addr == -1) return;
        if (args[0].equalsIgnoreCase("local")) {
            dispatchOkOrError(tools.writeLocalMemory(0x0117, new byte[] { (byte) (0xff & (addr >> 8)), (byte) (0xff & addr) }), "Cannot program local device.");
            return;
        } else if (!args[0].equalsIgnoreCase("selected")) {
            int oldAddr = getAndCheckPhysicalAddress(args[0]);
            if (oldAddr == -1) return;
            if (!tools.programmingModeOn(oldAddr)) {
                dispatchOutput("Cannot set programming mode of this device.");
                return;
            }
        }
        if (getAndCheckOneDeviceSelected() == -1) {
            return;
        }
        try {
            stack.writePhysicalAddress_Req(0, 6, false, addr, false);
            dispatchOutput("Ready.");
        } catch (IOException ioex) {
            dispatchOutput("Error: Cannot program this device.");
        }
    }

    private int getAndCheckOneDeviceSelected() {
        int[] addrs = tools.getDevicesInProgrammingMode();
        if (addrs == null) {
            dispatchOutput("Cannot scan devices.");
            return -1;
        } else if (addrs.length == 0) {
            dispatchOutput("There's no device in programming mode.");
            return -1;
        } else if (addrs.length > 1) {
            dispatchOutput("There's more than one device in programming mode.");
            return -1;
        }
        return addrs[0];
    }

    protected void list(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length != 0) {
            dispatchOutput("Illegal parameters.\nUsage: " + LIST.getUsage());
            return;
        }
        int[] addrs = tools.getDevicesInProgrammingMode();
        if (addrs == null) {
            dispatchOutput("Cannot scan devices.");
        } else {
            dispatchOutput(((addrs.length == 1) ? "There is one device " : "There are " + addrs.length + " devices ") + "in programming mode: ", false);
            for (int i = 0; i < addrs.length; i++) {
                dispatchOutput(CommonEIBTools.decodePhysicalAddress(addrs[i]), false);
            }
            dispatchOutput("Ready.");
        }
    }

    protected void scan(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length == 0) {
            dispatchOutput("Illegal parameters.\nUsage: " + SCAN.getUsage());
            return;
        }
        boolean shortOutput = false;
        int s = 0;
        if (args[0].equalsIgnoreCase("f")) {
            shortOutput = true;
            s = 1;
        }
        int[][] list = new int[args.length - s][2];
        for (int i = s; i < args.length; i++) {
            if (args[i].indexOf("-") != -1) {
                list[i - s][0] = getAndCheckPhysicalAddress(args[i].substring(0, args[i].indexOf("-")));
                if (list[i - s][0] == -1) return;
                list[i - s][1] = getAndCheckPhysicalAddress(args[i].substring(args[i].indexOf("-") + 1, args[i].length()));
                if (list[i - s][1] == -1) return;
            } else {
                list[i - s][0] = getAndCheckPhysicalAddress(args[i]);
                if (list[i - s][0] == -1) return;
                list[i - s][1] = list[i - s][0];
            }
        }
        dispatchOutput("Scanning bus for devices. Press enter to abort.", false);
        scannerThread = new ScannerThread(list, shortOutput);
        scannerThread.start();
    }

    protected void monitor(final String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length > 3) {
            dispatchOutput("Illegal parameters.\nUsage: " + MONITOR.getUsage());
            return;
        }
        boolean group = false;
        boolean ack = false;
        boolean raw = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("group")) group = true; else if (args[i].equalsIgnoreCase("ack")) ack = true; else if (args[i].equalsIgnoreCase("raw")) raw = true;
        }
        busmonitor = new Busmonitor(group, ack, raw);
        dispatchOutput("Busmonitor started. Press \"enter\" to abort.", false);
    }

    protected void send(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length < 2) {
            dispatchOutput("Illegal parameters.\nUsage: " + SEND.getUsage());
            return;
        }
        int ga = CommonEIBTools.encodeGroupAddress(args[0]);
        if (ga == -1) {
            dispatchOutput("Illegal group address.");
            return;
        }
        byte[] value = new byte[args.length - 1];
        for (int i = 0; i < value.length; i++) {
            try {
                if (args[i + 1].startsWith("0x")) {
                    value[i] = (byte) Integer.parseInt(args[i + 1].substring(2), 16);
                } else {
                    value[i] = (byte) Integer.parseInt(args[i + 1]);
                }
            } catch (NumberFormatException nfex) {
                dispatchOutput("Illegal format for values.");
                return;
            }
        }
        GroupDataService.AccessPoint sap = stack.subscribeGroup(ga, new GroupDataService.Listener() {

            public void writeGroupValue(int sa, int pr, int hc, byte[] data) {
            }

            public byte[] readGroupValue(int sa, int pr, int hc) {
                return null;
            }
        });
        try {
            sap.writeGroupValue(value, false);
        } catch (IOException ioex) {
            dispatchOutput("Error writing group value.");
        }
        dispatchOutput("Ready.");
    }

    protected void peek(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length != 3) {
            dispatchOutput("Illegal parameters.\nUsage: " + PEEK.getUsage());
            return;
        }
        int addr;
        int start;
        int end;
        if (args[0].equalsIgnoreCase("local")) {
            addr = Tools.LOCAL_DEVICE;
        } else {
            addr = getAndCheckPhysicalAddress(args[0]);
            if (addr == -1) return;
        }
        start = get16BitValue(args[1]);
        end = get16BitValue(args[2]);
        if ((start == -1) || (end == -1)) {
            dispatchOutput("Illegal number format.\nUsage: " + PEEK.getUsage());
            return;
        }
        byte[] mem = tools.readMemory(addr, start, end);
        if (mem == null) {
            dispatchOutput("Cannot read memory.");
        } else {
            dispatchOutput(printFormatedBytes(start, end, mem));
        }
    }

    private String printFormatedBytes(int s, int e, byte[] data) {
        int c = 0;
        int line = s & 0xfff0;
        StringBuffer output = new StringBuffer();
        output.append("        00 01 02 03 04 05 06 07   08 09 0a 0b 0c 0d 0e 0f\n" + "       --------------------------------------------------\n");
        StringBuffer offset = new StringBuffer(" ");
        offset.append(Integer.toHexString(line | 0x10000).substring(1, 5));
        offset.append("   ");
        for (int ofs = 0; ofs < (s & 0xf); ofs++) {
            offset.append((c != 8) ? "   " : "     ");
            c++;
        }
        output.append(offset.toString());
        for (int i = 0; i < e - s + 1; i++) {
            output.append(Integer.toHexString((data[i] & 0xff) | 0x100).substring(1, 3));
            c++;
            output.append((c == 8) ? "   " : " ");
            if (c == 16) {
                c = 0;
                output.append("\n ");
                line += 0x10;
                if ((i == e - s) && (e + 1 == line)) {
                    break;
                }
                output.append(Integer.toHexString(line | 0x10000).substring(1, 5));
                output.append("   ");
            }
        }
        return output.toString();
    }

    private int get16BitValue(String s) {
        int res = -1;
        try {
            if (s.startsWith("0x")) {
                res = Integer.parseInt(s.substring(2), 16);
            } else {
                res = 0xffff & Integer.parseInt(s);
            }
        } catch (NumberFormatException nfex) {
        }
        return res;
    }

    protected void help(String[] args) {
        if (args.length == 0) {
            StringBuffer buf = new StringBuffer();
            int i = 0;
            while (i < ALL.length) {
                buf.append(ALL[i].getName());
                while ((buf.length() % 10) != 0) buf.append(" ");
                if ((i % 5) == 4) buf.append("\n");
                i++;
            }
            buf.deleteCharAt(9);
            buf.insert(0, "The following commands are available:\n\n");
            dispatchOutput(buf.toString());
        } else if (args.length != 1) {
            dispatchError("Only one argument must be specified.", HELP.getUsage());
        } else {
            for (int i = 0; i < ALL.length; i++) {
                if (ALL[i].isCommand(args[0])) {
                    dispatchOutput("\nUsage: " + ALL[i].getUsage() + "\n" + ALL[i].getManual());
                    return;
                }
            }
            dispatchOutput("I don't know this command.");
        }
    }

    protected void version(String[] args) {
        dispatchOutput(VERSION_STRING);
    }

    protected void exit(String[] args) {
        dispatchOutput("Good bye.");
        cleanup();
        if (logPrintWriter != null) {
            logPrintWriter.close();
        }
        for (int i = 0; i < listeners.size(); i++) {
            ((OutputListener) listeners.elementAt(i)).exit();
        }
    }

    protected void poke(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if (args.length < 3) {
            dispatchOutput("Illegal parameters.\nUsage: " + POKE.getUsage());
            return;
        }
        int addr = getAndCheckPhysicalAddress(args[0]);
        if (addr == -1) return;
        int start = get16BitValue(args[1]);
        if (start == -1) {
            dispatchOutput("Illegal start address.");
            return;
        }
        byte[] value = new byte[args.length - 2];
        for (int i = 0; i < value.length; i++) {
            try {
                if (args[i + 2].startsWith("0x")) {
                    value[i] = (byte) Integer.parseInt(args[i + 2].substring(2), 16);
                } else {
                    value[i] = (byte) Integer.parseInt(args[i + 2]);
                }
            } catch (NumberFormatException nfex) {
                dispatchOutput("Illegal format for values.");
                return;
            }
        }
        dispatchOkOrError(tools.writeMemory(addr, start, value), "Error accessing device.");
    }

    protected void mask(String[] args) {
        if (!testIfConnected()) {
            return;
        }
        if ((args.length > 2) || (args.length == 0)) {
            dispatchOutput("Illegal parameters.\nUsage: " + MASK.getUsage());
            return;
        }
        int addr = getAndCheckPhysicalAddress(args[0]);
        if (addr == -1) return;
        int mask = tools.getMaskVersion(addr);
        if (mask == -1) {
            dispatchOutput("Cannot retrieve mask version.");
            return;
        }
        if ((args.length == 2) && ((args[1].equalsIgnoreCase("v")) || (args[1].equalsIgnoreCase("verbose")))) {
            Mask m = Settings.getMaskByVersion(mask);
            String specs = "Mask version           : " + get16BitHex(mask) + "\n" + "Mask version name      : " + m.getMaskVersionName() + "\n" + "User RAM area          : " + get16BitHex(m.getUserRamStart()) + " - " + get16BitHex(m.getUserRamEnd()) + "\n" + "User EEPROM area       : " + get16BitHex(m.getUserEepromStart()) + " - " + get16BitHex(m.getUserEepromEnd()) + "\n" + "External Memory area   : " + get16BitHex(m.getExternalMemoryStart()) + " - " + get16BitHex(m.getExternalMemoryEnd()) + "\n" + "Runtime error address  : " + get16BitHex(m.getRunErrorAddress()) + "\n" + "address table          : " + get16BitHex(m.getAddressTabAddress()) + "\n" + "association table      : " + get16BitHex(m.getAssocTabPtrAddress()) + "\n" + "comm objecttable       : " + get16BitHex(m.getCommsTabPtrAddress()) + "\n" + "manufacturer data addr : " + get16BitHex(m.getManufacturerDataAddress()) + "\n" + "manufacturer data size : " + get16BitHex(m.getManufacturerDataSize()) + "\n" + "manufacturer id addr   : " + get16BitHex(m.getManufacturerIdAddress()) + "\n" + "route counter addr     : " + get16BitHex(m.getRoutecntAddress()) + "\n";
            dispatchOutput(specs);
        } else {
            dispatchOutput("Mask version: " + get16BitHex(mask));
        }
    }

    private String get16BitHex(int i) {
        return "0x" + Integer.toHexString(0x10000 | i).substring(1);
    }

    protected void log(String[] args) {
        if (args.length != 1) {
            dispatchError("Exactly on argument is needed.", LOG.getUsage());
        } else {
            if (args[0].equals("off")) {
                if (logListener == null) {
                    dispatchOutput("Logging is not active.");
                } else {
                    removeListener(logListener);
                    logPrintWriter.close();
                    logListener = null;
                    dispatchOutput("Logging deactivated.");
                }
            } else {
                try {
                    logPrintWriter = new PrintWriter(new FileWriter(args[0]));
                    logListener = new OutputListener() {

                        public void exit() {
                            logPrintWriter.close();
                        }

                        public void output(String s) {
                            logPrintWriter.print(s);
                        }
                    };
                    addListener(logListener);
                    dispatchOutput("Logging activated.");
                } catch (IOException ioex) {
                    dispatchOutput("ERROR: Cannot open file " + "(Message: " + ioex.getMessage());
                }
            }
        }
    }

    protected void error(String cmd) {
        dispatchOutput("Illegal command. Try help for more information.");
    }

    public void addListener(OutputListener l) {
        listeners.add(l);
    }

    public void removeListener(OutputListener l) {
        listeners.remove(l);
    }

    protected void dispatchOutput(final String s, final boolean prompt) {
        String out = s + ((prompt && showPrompt) ? "\n> " : "\n");
        for (int i = 0; i < listeners.size(); i++) {
            ((OutputListener) listeners.elementAt(i)).output(out);
        }
    }

    protected void dispatchOutput(String s) {
        dispatchOutput(s, true);
    }

    protected void dispatchError(String msg, String usage) {
        dispatchOutput("ERROR: " + msg + "\nUsage: " + usage);
    }

    private void cleanup() {
        if (busmonitor != null) {
            busmonitor.shutdown();
        }
        if (scannerThread != null) {
            scannerThread.shutdown();
        }
        if (stack != null) {
            stack.cleanup();
        }
        stack = null;
        tools = null;
    }

    public static void main(String[] args) {
        Settings.initializeSettings();
        System.out.println(GREETINGS);
        EIBConsole c = new EIBConsole();
        System.out.print("> ");
        c.addListener(new OutputListener() {

            public void exit() {
                System.exit(0);
            }

            public void output(String s) {
                System.out.print(s);
            }
        });
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                if (in.ready()) {
                    c.execute(in.readLine());
                }
            } catch (IOException ioex) {
                System.out.println("--->" + ioex);
            }
        }
    }

    class ScannerThread extends Thread {

        boolean running;

        boolean shortOutput;

        int index;

        int maxRange;

        int act;

        int found;

        int[][] list;

        public ScannerThread(int[][] list, boolean shortOutput) {
            this.list = list;
            this.shortOutput = shortOutput;
            index = 0;
            found = 0;
            maxRange = -1;
            running = true;
        }

        public void run() {
            while (running && (index < list.length)) {
                if (list[index][0] == list[index][1]) {
                    scanDev(list[index][0]);
                    index++;
                } else {
                    if (maxRange == -1) {
                        maxRange = list[index][1];
                        act = list[index][0];
                    }
                    scanDev(act);
                    act++;
                    if (act == maxRange + 1) {
                        maxRange = -1;
                        index++;
                    }
                }
            }
            dispatchOutput("Found " + found + " devices.", false);
            dispatchOutput("Ready.");
        }

        private void scanDev(int a) {
            if (!running) {
                return;
            }
            String answer = CommonEIBTools.decodePhysicalAddress(a) + ":";
            if (tools.isDevicePresent(a)) {
                answer += " found.";
                found++;
            } else {
                if (shortOutput) return;
                answer += " not available.";
            }
            dispatchOutput(answer, false);
        }

        public void shutdown() {
            running = false;
        }
    }

    class Busmonitor implements BusmonitorListener {

        EIBTransceiver tr;

        boolean group;

        boolean ack;

        boolean raw;

        public Busmonitor(boolean group, boolean ack, boolean raw) {
            this.group = group;
            this.ack = ack;
            this.raw = raw;
            tr = (EIBTransceiver) stack.getTransceiver();
            tr.addBusmonitorListener(this);
            tr.setBusmonitorMode(true);
        }

        public void indication(BusmonitorEvent event) {
            if ((group && event.isGroupAddress()) || !group) {
                dispatchOutput(event.toString(raw), false);
            }
        }

        public void acknowledge(BusmonitorEvent event) {
            if (ack) {
                dispatchOutput(event.toString(false), false);
            }
        }

        public void shutdown() {
            tr.removeBusmonitorListener(this);
            tr.setBusmonitorMode(false);
            busmonitor = null;
        }
    }

    class BlinkerThread extends Thread {

        boolean running;

        int addr;

        int count;

        int delay;

        public BlinkerThread(int addr, int count, int delay) {
            this.addr = addr;
            this.count = count;
            this.delay = delay;
            blinkerThreads.addElement(this);
            running = true;
        }

        public void run() {
            while (running && (count > 0)) {
                boolean result = false;
                result = tools.programmingModeOn(addr);
                if (result == false) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ieex) {
                }
                result = tools.programmingModeOff(addr);
                if (result == false) {
                    break;
                }
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ieex) {
                }
                count--;
            }
            blinkerThreads.removeElement(this);
        }

        public void shutdown() {
            running = false;
        }
    }
}
