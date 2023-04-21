package org.maverickdbms.basic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.Writer;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import org.maverickdbms.basic.term.PseudoTerminal;
import org.maverickdbms.basic.term.Termios;
import org.maverickdbms.basic.util.Tree;

/**
* The Session class is designed to be a continer to hold session
* specific data for the user.
*/
public class Session {

    private static final String PROP_AUTOEXEC = "org.maverickdbms.basic.autoexec";

    private static final String PROP_CONFIG_FILE = "maverick.config";

    private static final String PROP_EMULATION = "org.maverickdbms.basic.term_emulation";

    private static final String PROP_PROMPT = "org.maverickdbms.basic.prompt";

    private static final String PROP_SHELL = "org.maverickdbms.basic.shell";

    private static final String PROP_SHELL_ARGS = "org.maverickdbms.basic.shell_args";

    private static final String PROP_STACK_COMMON = "org.maverickdbms.basic.stack_common";

    private static final String DEFAULT_CONFIG_FILE = "maverick.properties";

    private static final String DEFAULT_EMULATION = "VT100";

    private static final String DEFAULT_PROMPT = "?";

    private static final String DEFAULT_SHELL = "/usr/bin/sh";

    private static final String DEFAULT_SHELL_ARGS = " -c {0}";

    private static final boolean DEFAULT_STACK_COMMON = true;

    public static final long DATE_OFFSET = 732;

    public static final long MILLISECS_IN_A_DAY = 86400000L;

    private static final int NO_OF_PRINT_CHANNELS = 256;

    public static final int SCREEN_CHANNEL = -1;

    public static final int SYSTEM_STATUS = 0;

    public static final int SYSTEM_PRINTER = 1;

    public static final int SYSTEM_CRTWIDE = 2;

    public static final int SYSTEM_CRTHIGH = 3;

    public static final int SYSTEM_CRTLINES = 4;

    public static final int SYSTEM_PRINTERPAGE = 5;

    public static final int SYSTEM_PRINTERLINES = 6;

    public static final int SYSTEM_TERM = 7;

    public static final int SYSTEM_TAPEBLOCKSIZE = 8;

    public static final int SYSTEM_PROGRAMTIME = 9;

    public static final int SYSTEM_STON = 10;

    public static final int SYSTEM_SELECTLIST = 11;

    public static final int SYSTEM_TIME = 12;

    public static final int SYSTEM_SLEEP = 13;

    public static final int SYSTEM_BUFFER = 14;

    public static final int SYSTEM_SOMETHING = 15;

    public static final int SYSTEM_LEVEL = 16;

    private Properties properties;

    private ProgramEntry[] commands = new ProgramEntry[10];

    private int commandCount = 0;

    private Tree common;

    private Factory factory;

    private AtVariable atVariables;

    private PseudoTerminal pty;

    private InputChannel input;

    private PrintChannel[] outputs = new PrintChannel[NO_OF_PRINT_CHANNELS];

    private PrintChannel screen;

    private ConstantString defaultPrompt;

    private boolean printerEnabled = false;

    private static final char FILE_ESCAPE_PREFIX = '_';

    private static final char[] FILE_ESCAPE_CHARS = { '.', '_', '-' };

    private static BitSet set;

    private static int min_set = 65535;

    private static int max_set = 0;

    static {
        for (int i = 0; i < FILE_ESCAPE_CHARS.length; i++) {
            char c = FILE_ESCAPE_CHARS[i];
            if (c < min_set) min_set = c;
            if (c > max_set) max_set = c;
        }
        set = new BitSet(max_set - min_set);
        for (int i = 0; i < FILE_ESCAPE_CHARS.length; i++) {
            set.set(FILE_ESCAPE_CHARS[i] - min_set);
        }
    }

    public Session() {
        properties = new Properties(System.getProperties());
        String propfile = properties.getProperty(PROP_CONFIG_FILE);
        if (propfile != null) {
            try {
                FileInputStream fis = new FileInputStream(propfile);
                properties.load(fis);
            } catch (FileNotFoundException fnfe) {
            } catch (IOException ioe) {
            }
        }
        init();
    }

    public Session(Properties properties, PseudoTerminal pty) throws MaverickException {
        this.properties = properties;
        this.pty = pty;
        init();
        pty.setTerminals(factory.getTerminals());
    }

    /**
    * Set break key handling
    * <B> Note this function is not yet implemented </B>
    * @param enable whether to enable break key handling
    */
    public void BREAK(boolean enable) throws MaverickException {
        throw new MaverickException(0, "Sorry BREAK is not implemented yet.");
    }

    /**
    * Executes the specified subroutine
    * @param subr subroutine name
    * @param params subroutine parameters
    */
    public void CALL(ConstantString subr, MaverickString[] params) throws MaverickException {
        Program s = factory.getProgram(subr);
        if (s == null) {
            throw new MaverickException(0, "Sorry cannot find " + subr.toString());
        } else {
            s.run(this, params);
        }
    }

    /**
    * converts a filename from external format to internal format
    */
    public static String convertName(String name) {
        char[] arr = name.toCharArray();
        StringBuffer sb = new StringBuffer();
        int start = 0;
        int index = 0;
        while (index < arr.length) {
            char c = arr[index];
            if (c == '\\') {
                sb.append(arr, start, index - start);
                index++;
                sb.append(arr[index]);
                start = index + 1;
            } else if (c <= max_set && c >= min_set && set.get(c - min_set)) {
                sb.append(arr, start, index - start);
                start = index + 1;
                sb.append(FILE_ESCAPE_PREFIX);
                String s = Integer.toOctalString((int) c);
                int len2 = s.length();
                while (len2++ < 3) sb.append('0');
                sb.append(s);
            }
            index++;
        }
        if (start < arr.length) sb.append(arr, start, arr.length - start);
        return sb.toString();
    }

    /**
    * Returns a number containing the internal date.  The internal date
    * is the number of days since December 31 1967
    * @return the days since December 31 1967
    */
    public static MaverickString DATE(MaverickString result) {
        Calendar c = Calendar.getInstance();
        long time = c.getTime().getTime();
        time += c.get(Calendar.ZONE_OFFSET);
        time += MILLISECS_IN_A_DAY * DATE_OFFSET;
        time /= MILLISECS_IN_A_DAY;
        result.set(time);
        return result;
    }

    /**
    * Executes the specified command 
    * @param result the string to contain the programs screen output
    * @param command the command to execute
    */
    public void EXECUTE(MaverickString result, ConstantString command) {
        try {
            if (result != null) {
                result.clear();
                getChannel(SCREEN_CHANNEL).pushWriter(new StringWriter(result));
            }
            Program program = factory.getCommand(command);
            MaverickString[] args = new MaverickString[0];
            program.run(this, args);
        } catch (MaverickException e) {
            handleError(e, getStatus());
        }
        if (result != null) {
            try {
                getChannel(SCREEN_CHANNEL).popWriter();
            } catch (MaverickException mve) {
                handleError(mve, getStatus());
            }
            result.CHANGE(result, ConstantString.LINE_SEPARATOR, ConstantString.AM, ConstantString.ZERO, ConstantString.ZERO);
        }
    }

    public void EXECUTE(MaverickString result, Program program, ConstantString name, MaverickString[] args) {
        if (result != null) {
            result.clear();
            try {
                getChannel(SCREEN_CHANNEL).pushWriter(new StringWriter(result));
            } catch (MaverickException mve) {
                handleError(mve, getStatus());
            }
        }
        while (program != null) {
            boolean stackCommon = getProperty(PROP_STACK_COMMON, DEFAULT_STACK_COMMON);
            ProgramEntry pe = new ProgramEntry(factory, program, name, args, System.currentTimeMillis());
            pe.setPrompt(defaultPrompt);
            if (!stackCommon && peekCommand() != null) {
                pe.setCommon(peekCommand().getCommon());
            } else {
                pe.setCommon(factory.getCommon());
            }
            pushCommand(pe);
            try {
                program.run(this, args);
                program = null;
            } catch (ChainException ce) {
                program = ce.getProgram();
                name = factory.getConstant(program.getClass().getName());
                args = new MaverickString[0];
            } catch (StopException se) {
                String message = se.getMessage();
                if (message != null && message.length() > 0) {
                    handleError(se, getStatus());
                }
                program = null;
            } catch (MaverickException e) {
                handleError(e, getStatus());
                program = null;
            }
            popCommand();
        }
        if (result != null) {
            try {
                getChannel(SCREEN_CHANNEL).popWriter();
            } catch (MaverickException e) {
                handleError(e, getStatus());
            }
            result.CHANGE(result, ConstantString.LINE_SEPARATOR, ConstantString.AM, ConstantString.ZERO, ConstantString.ZERO);
        }
    }

    public MaverickString getAtVariable(MaverickString r, int n) throws MaverickException {
        return atVariables.get(r, n);
    }

    /**
     * Gets the original starting program in the session
     *
     */
    public Program getBaseProgram() {
        return (commandCount > 0) ? commands[0].getProgram() : null;
    }

    public Common getDefaultCommon() {
        return (commandCount > 0) ? commands[commandCount - 1].getCommon() : null;
    }

    public String getCommandString() {
        return peekCommand().getCommandString();
    }

    /**
     * Get specified named common for session
     * @param name name of common required
     */
    public Common getCommon(String name) {
        if (common == null) {
            common = factory.getTree(Tree.TYPE_AVL);
        }
        CommonNode cn = new CommonNode();
        cn.name = name;
        cn = (CommonNode) common.probe(cn);
        if (cn.common == null) {
            cn.common = new Common(factory);
        }
        return cn.common;
    }

    public Terminal getEmulation() throws MaverickException {
        try {
            return getPseudoTerminal().getEmulation();
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
    }

    public Factory getFactory() {
        return factory;
    }

    public InputChannel getInputChannel() throws MaverickException {
        if (input == null) {
            input = new InputChannel(this, getPseudoTerminal());
        }
        return input;
    }

    public PrintChannel getChannel(int channelNumber) throws MaverickException {
        if (channelNumber < 0 || (channelNumber == 0 && !printerEnabled)) {
            if (screen == null) {
                screen = new PrintChannel(this, getPseudoTerminal().getWriter(), true);
            }
            return screen;
        }
        if (outputs[channelNumber] == null) {
            String spooldir = "c:\\tmp";
            String filesep = System.getProperty("file.separator");
            String filename = spooldir + filesep + channelNumber;
            try {
                PrintWriter ps = new PrintWriter(new FileOutputStream(filename));
                outputs[channelNumber] = new PrintChannel(this, ps, false);
            } catch (FileNotFoundException fnfe) {
            }
        }
        return outputs[channelNumber];
    }

    public Program getProgram() {
        return (commandCount > 0) ? commands[commandCount - 1].getProgram() : null;
    }

    public ConstantString getPrompt() {
        return (commandCount > 0) ? commands[commandCount - 1].getPrompt() : defaultPrompt;
    }

    public boolean getProperty(String property, boolean dflt) {
        String value = properties.getProperty(property);
        return (value != null) ? value.equals("true") : dflt;
    }

    public int getProperty(String property, int dflt) {
        String value = properties.getProperty(property);
        return (value != null) ? Integer.parseInt(value) : dflt;
    }

    public String getProperty(String property, String dflt) {
        return properties.getProperty(property, dflt);
    }

    public Properties getProperties() {
        return properties;
    }

    PseudoTerminal getPseudoTerminal() throws MaverickException {
        if (pty == null) {
            pty = new PseudoTerminal(System.in, System.out);
            pty.setTerm(properties.getProperty(PROP_EMULATION, DEFAULT_EMULATION));
            pty.setTerminals(factory.getTerminals());
            pty.setAttribute(Termios.IGNCR, 1);
            pty.setAttribute(Termios.ICANON, 0);
            pty.setAttribute(Termios.ECHO, 0);
        }
        return pty;
    }

    public MaverickString getStatus() {
        return peekCommand().getStatus();
    }

    public void handleError(Exception e, MaverickString status) {
        try {
            getChannel(SCREEN_CHANNEL).PRINT(e, status);
        } catch (MaverickException mve) {
            mve.printStackTrace(System.err);
        }
    }

    private void init() {
        factory = new Factory();
        factory.run(this, new MaverickString[0]);
        atVariables = new AtVariable(this);
        defaultPrompt = factory.getConstant(properties.getProperty(PROP_PROMPT, DEFAULT_PROMPT));
        String autoexec = properties.getProperty(PROP_AUTOEXEC);
        if (autoexec != null && autoexec.length() > 0) {
            autoexec = autoexec.replace(',', Delimiter.AM);
            EXECUTE(null, factory.getConstant(autoexec));
        }
    }

    public static void NAP(ConstantString millis) {
        long time = millis.intValue();
        long endTime = System.currentTimeMillis() + time;
        while (time > 0) {
            try {
                Thread.sleep(time);
                time = endTime - System.currentTimeMillis();
            } catch (InterruptedException ie) {
            }
        }
    }

    public void PCPERFORM(MaverickString result, ConstantString command) throws MaverickException {
        String[] params = { command.toString() };
        String shellArgs = properties.getProperty(PROP_SHELL_ARGS, DEFAULT_SHELL_ARGS);
        String args = MessageFormat.format(shellArgs, (Object[]) params);
        if (result != null) {
            result.clear();
            getChannel(SCREEN_CHANNEL).pushWriter(new StringWriter(result));
        }
        StringTokenizer st = new StringTokenizer(args);
        String[] comm = new String[st.countTokens() + 1];
        int index = 0;
        comm[index++] = properties.getProperty(PROP_SHELL, DEFAULT_SHELL);
        while (st.hasMoreElements()) {
            comm[index++] = MessageFormat.format(st.nextToken(), (Object[]) params);
        }
        try {
            Process p = Runtime.getRuntime().exec(comm);
            Thread out = new ConnectStream(new InputStreamReader(p.getInputStream()), getChannel(SCREEN_CHANNEL).peekWriter());
            Thread err = new ConnectStream(new InputStreamReader(p.getErrorStream()), getChannel(SCREEN_CHANNEL).peekWriter());
            err.start();
            out.start();
            boolean finished = false;
            while (!finished) {
                try {
                    int exitValue = p.waitFor();
                    finished = true;
                } catch (InterruptedException ie) {
                }
            }
            try {
                out.join();
                err.join();
            } catch (InterruptedException ie) {
                throw new MaverickException(0, ie);
            }
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
        if (result != null) {
            getChannel(SCREEN_CHANNEL).popWriter();
            result.CHANGE(result, ConstantString.LINE_SEPARATOR, ConstantString.AM, ConstantString.ZERO, ConstantString.ZERO);
        }
    }

    private ProgramEntry peekCommand() {
        return (commandCount > 0) ? commands[commandCount - 1] : null;
    }

    private void popCommand() {
        ProgramEntry command = commands[commandCount - 1];
        Iterator iter = command.getFiles();
        while (iter.hasNext()) {
            FileNode file = (FileNode) iter.next();
        }
        commands[--commandCount] = null;
    }

    public void PRINTER(boolean printerEnabled) {
        this.printerEnabled = printerEnabled;
    }

    public void PRINTER_CLOSE(MaverickString status) throws MaverickException {
        for (int cli = 0; cli < NO_OF_PRINT_CHANNELS; cli++) {
            if (outputs[cli] != null) {
                outputs[cli].CLOSE(status);
                outputs[cli] = null;
            }
        }
    }

    public void PRINTER_CLOSE(ConstantString channel, MaverickString status) throws MaverickException {
        int channelNumber = channel.intValue();
        if (outputs[channelNumber] != null) {
            outputs[channelNumber].CLOSE(status);
            outputs[channelNumber] = null;
        }
    }

    public void PROMPT(ConstantString val) {
        peekCommand().setPrompt(val);
    }

    private void pushCommand(ProgramEntry command) {
        if (commandCount >= commands.length) {
            ProgramEntry[] old = commands;
            commands = new ProgramEntry[old.length + (old.length >> 1)];
            System.arraycopy(old, 0, commands, 0, commandCount);
        }
        commands[commandCount++] = command;
    }

    public void registerFileClose(Program program, File file) {
        for (int i = commandCount - 1; i >= 0; i--) {
            if (program == commands[i].getProgram()) {
                commands[i].removeFile(file);
                return;
            }
        }
    }

    public void registerFileOpen(Program program, File file) {
        for (int i = commandCount - 1; i >= 0; i--) {
            if (program == commands[i].getProgram()) {
                commands[i].addFile(file);
                return;
            }
        }
    }

    public static void RQM(ConstantString mvs) throws java.text.ParseException, InterruptedException {
        SLEEP(mvs);
    }

    public void setAtVariable(int index, ConstantString value) throws MaverickException {
        atVariables.set(index, value);
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setSystem(int sequenceNo, ConstantString value) throws MaverickException {
        switch(sequenceNo) {
            case SYSTEM_CRTHIGH:
                setAtVariable(AtVariable.CRTHIGH, value);
                break;
            case SYSTEM_CRTWIDE:
                setAtVariable(AtVariable.CRTWIDE, value);
                break;
            case SYSTEM_TERM:
                setAtVariable(AtVariable.TERM_TYPE, value);
                break;
        }
    }

    public static void SLEEP(ConstantString mvs) {
        long startTime = System.currentTimeMillis();
        String s = mvs.toString();
        int index = s.indexOf(':');
        long endTime;
        if (index != -1) {
            endTime = Integer.parseInt(s.substring(0, index)) * 60;
            s = s.substring(index + 1);
            index = s.indexOf(':');
            if (index != -1) {
                endTime += Integer.parseInt(s.substring(0, index)) * 60;
                endTime += Integer.parseInt(s.substring(index + 1));
            } else {
                endTime += Integer.parseInt(s) * 60;
            }
            endTime *= 1000;
            endTime += startTime / MILLISECS_IN_A_DAY;
            if (endTime < startTime) {
                endTime += MILLISECS_IN_A_DAY;
            }
        } else {
            endTime = startTime + Integer.parseInt(s) * 1000;
        }
        long time = endTime - startTime;
        while (time > 0) {
            try {
                Thread.sleep(time);
                time = endTime - System.currentTimeMillis();
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
    * Retrieves specified SYSTEM value.
    * @param result variable to hold result
    * @param sequenceNo index required
    * @param result
    */
    public MaverickString getSystem(MaverickString result, int sequenceNo) throws MaverickException {
        result.clear();
        switch(sequenceNo) {
            case SYSTEM_STATUS:
                result.set(peekCommand().getStatus());
                break;
            case SYSTEM_PRINTER:
                break;
            case SYSTEM_CRTWIDE:
                result.set(getAtVariable(factory.getString(), AtVariable.CRTWIDE));
                break;
            case SYSTEM_CRTHIGH:
                result.set(getAtVariable(factory.getString(), AtVariable.CRTHIGH));
                break;
            case SYSTEM_CRTLINES:
                break;
            case SYSTEM_PRINTERPAGE:
                break;
            case SYSTEM_PRINTERLINES:
                break;
            case SYSTEM_TERM:
                result.set(getAtVariable(factory.getString(), AtVariable.TERM_TYPE));
                break;
            case SYSTEM_TAPEBLOCKSIZE:
                break;
            case SYSTEM_PROGRAMTIME:
                result.set(System.currentTimeMillis() - peekCommand().getStart());
                break;
            case SYSTEM_STON:
                break;
            case SYSTEM_SELECTLIST:
                result.set((factory.containsList(0)) ? 1 : 0);
                break;
            case SYSTEM_TIME:
                result.set(System.currentTimeMillis() % MILLISECS_IN_A_DAY);
                break;
            case SYSTEM_SLEEP:
                SLEEP(factory.getConstant(1));
                break;
            case SYSTEM_BUFFER:
                break;
            case SYSTEM_SOMETHING:
                break;
            case SYSTEM_LEVEL:
                break;
        }
        return result;
    }

    /**
    * Returns the current time in internal format(seconds since midnight).
    * @return the time
    */
    public static MaverickString TIME(MaverickString result) {
        Calendar c = Calendar.getInstance();
        long time = c.get(Calendar.HOUR_OF_DAY) * 3600 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.SECOND);
        result.set(time);
        return result;
    }

    /**
    * Returns a string representation of the current time and date
    * @return MaverickString containing the time and date
    */
    public static MaverickString TIMEDATE(MaverickString result) {
        Date d = Calendar.getInstance().getTime();
        String s = DateFormat.getTimeInstance(DateFormat.SHORT).format(d) + " " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(d);
        result.set(s);
        return result;
    }
}
