package com.atolsystems.atolutilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author atol systems
 */
public class CommandLine {

    private static final String ARGFILE_PATH_PREFERENCE_KEY = "com.atolsystems.atolutilities.CommandLine.ArgFilePath";

    public static final String ARG_ARGFILE = "argFile:";

    public static final String ARG_LAUNCH = "launch:";

    public static final String ARG_SKIP_START = "/*";

    public static final String ARG_SKIP_END = "*/";

    public static final String ARG_SKIP_ONE = "//";

    LinkedList<ArgDef> argDefs = new LinkedList<ArgDef>();

    HashMap<Integer, Collection<? extends ArgDef>> toRemove = new HashMap<Integer, Collection<? extends ArgDef>>();

    ArrayList<Arg> args = new ArrayList<Arg>();

    ArrayList<File> argFiles = new ArrayList<File>();

    File argDirectory;

    int curArgIndex;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(": Current arg index=");
        sb.append(curArgIndex);
        sb.append(", number of arguments=");
        sb.append(args.size());
        sb.append("\n");
        int i = 0;
        for (Arg arg : args) {
            sb.append("\t");
            sb.append("[");
            sb.append(i++);
            sb.append("]");
            sb.append(arg.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private void initArgDirctory() throws IOException {
        File temp = new File("a");
        temp = temp.getCanonicalFile();
        argDirectory = temp.getParentFile();
    }

    AddJarArgHandler addJarArgHandler;

    public Class getPlugInClass(String className) throws MalformedURLException, ClassNotFoundException {
        return addJarArgHandler.getPlugInClass(className);
    }

    public PlugInLoader getPlugInLoader() {
        return addJarArgHandler.getPlugInLoader();
    }

    private void initArgHandlers() throws IOException {
        addJarArgHandler = new AddJarArgHandler();
        argDefs.addAll(addJarArgHandler.getArgDefs());
    }

    public CommandLine() throws IOException {
        initArgDirctory();
        initArgHandlers();
        curArgIndex = -1;
    }

    public CommandLine(CommandLine cl) {
        this.toRemove.putAll(cl.toRemove);
        for (ArgDef a : cl.argDefs) {
            argDefs.add(new ArgDef(a));
        }
        for (Arg a : cl.args) {
            args.add(new Arg(a));
        }
        for (File f : cl.argFiles) {
            argFiles.add(AFileUtilities.hardCopy(f));
        }
        argDirectory = AFileUtilities.hardCopy(cl.argDirectory);
        curArgIndex = cl.curArgIndex;
        addJarArgHandler = cl.addJarArgHandler;
    }

    public CommandLine(String args[]) throws IOException {
        curArgIndex = -1;
        initArgHandlers();
        initArgDirctory();
        addNewArgs(args);
    }

    public CommandLine(File argFile) throws IOException {
        curArgIndex = -1;
        initArgHandlers();
        argDirectory = argFile.getParentFile();
        addNewArgs(argFile);
    }

    public CommandLine subCommandLine(int offset, int length) {
        CommandLine out = new CommandLine(this);
        out.args = (ArrayList<Arg>) args.subList(offset, offset + length);
        out.curArgIndex = -1;
        return out;
    }

    public final void addNewArgs(String args[]) throws IOException {
        int startIndex = this.args.size();
        for (int i = 0; i < args.length; i++) {
            Arg arg = new Arg(this.argDirectory, args[i], this);
            this.args.add(arg);
        }
        processCommandLineArgs(startIndex);
    }

    public final void addNewArgs(File argFile) throws IOException {
        addNewArgs(argFile, args.size());
    }

    public void addNewArgs(File argFile, int offset) throws IOException {
        ArrayList<Arg> newArgs = new ArrayList<Arg>();
        FileInputStream argStream = null;
        try {
            argStream = new FileInputStream(argFile);
        } catch (FileNotFoundException e) {
            throw new InvalidCommandLineException(e);
        }
        File directory = argFile.getParentFile();
        if (args.size() > offset) {
            for (int i = 0; i < offset + 1; i++) {
                Arg arg = args.get(i);
                if (false == arg.processed) {
                    newArgs.add(arg);
                } else {
                    if (i <= curArgIndex) {
                        curArgIndex--;
                    }
                }
            }
        }
        int startIndex = newArgs.size();
        try {
            BufferedReader argReader = new BufferedReader(new InputStreamReader(argStream, "UTF-8"));
            String argLine = argReader.readLine();
            while (null != argLine) {
                if (false == argLine.isEmpty()) {
                    Arg arg = new Arg(directory, argLine, this);
                    newArgs.add(arg);
                }
                argLine = argReader.readLine();
            }
            argReader.close();
        } finally {
            argStream.close();
        }
        for (int i = offset + 1; i < args.size(); i++) {
            Arg arg = args.get(i);
            if (false == arg.processed) {
                newArgs.add(arg);
            }
        }
        args = newArgs;
        processCommandLineArgs(startIndex);
    }

    public void removeProcessed() {
        ArrayList<Arg> newArgs = new ArrayList<Arg>();
        for (int i = 0; i < args.size(); i++) {
            Arg arg = args.get(i);
            if (false == arg.processed) {
                newArgs.add(arg);
            }
        }
        args = newArgs;
    }

    public void setCurArgIndex(int curArgIndex) {
        this.curArgIndex = curArgIndex;
    }

    public int getCurArgIndex() {
        return curArgIndex;
    }

    /**
     *
     * @param mark
     * @return the first occurence of the arg, or null if not found or already processed
     */
    public Arg getArg(String mark) {
        for (Arg arg : args) {
            if (false == arg.processed) {
                if (arg.value.startsWith(mark)) {
                    return arg;
                }
            }
        }
        return null;
    }

    public Arg getArg(String mark, int offset) {
        return getArg(mark, offset, args.size() - offset);
    }

    public Arg getArg(String mark, int offset, int length) {
        for (int i = offset; i < offset + length; i++) {
            Arg arg = args.get(i);
            if (false == arg.processed) {
                if (arg.value.startsWith(mark)) {
                    return arg;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param mark
     * @return all unprocessed occurences of the arg
     */
    public ArrayList<Arg> getArgs(String mark) {
        return getArgs(mark, 0, args.size());
    }

    public ArrayList<Arg> getArgs(String mark, int offset, int length) {
        ArrayList<Arg> out = new ArrayList<Arg>();
        for (int i = offset; i < offset + length; i++) {
            Arg arg = args.get(i);
            if (false == arg.processed) {
                if (arg.value.startsWith(mark)) {
                    out.add(arg);
                }
            }
        }
        return out;
    }

    /**
     * Implement a basic argument processing loop in two phase:
     * 1. recognise arguments
     * 2. process arguments
     * Normal termination can be checked by using <code>isAllArgsProcessed</code>
     */
    public void processArgs() {
        boolean argsRemain;
        try {
            do {
                argsRemain = processArg(true);
            } while (argsRemain);
            if (false == isAllArgsRecognized()) {
                throw new InvalidCommandLineException("Not all arguments were recognized:\n" + toString());
            }
        } catch (InvalidCommandLineException ex) {
            throw new InvalidCommandLineException("Argument " + getCurArgIndex() + " is not valid\n" + "Command line dump:\n" + toString() + "\n" + ex.getMessage(), ex);
        } catch (ExitException ex) {
            return;
        } catch (StopRequestFromUserException ex) {
            return;
        } catch (Throwable ex) {
            throw new RuntimeException("Error happened during evaluation of argument " + getCurArgIndex() + "\nCommand line dump:\n" + toString(), ex);
        }
        setCurArgIndex(-1);
        do {
            argsRemain = processArg(false);
        } while (argsRemain);
    }

    /**
     *
     * @return true as long as the end of args have not been reached
     */
    public boolean processArg(boolean recognizeArgsOnly) {
        Arg arg = null;
        while (true) {
            curArgIndex++;
            Collection<? extends ArgDef> toRemoveNow = toRemove.remove(curArgIndex);
            if (null != toRemoveNow) {
                argDefs.removeAll(toRemoveNow);
            }
            if (args.size() == curArgIndex) {
                for (ArgDef argDef : argDefs) {
                    if (recognizeArgsOnly) {
                        argDef.handler.endOfCommandLineInspection(this);
                    } else {
                        argDef.handler.endOfCommandLineProcessing(this);
                    }
                }
                return false;
            }
            if (args.size() < curArgIndex) {
                throw new RuntimeException("args.size()<curArgIndex: args.size()=" + args.size() + ", curArgIndex=" + curArgIndex);
            }
            arg = args.get(curArgIndex);
            if (false == arg.processed) {
                break;
            }
        }
        Iterator<ArgDef> it = argDefs.descendingIterator();
        while (it.hasNext()) {
            ArgDef argDef = it.next();
            if (null == argDef.mark) {
                continue;
            }
            if (arg.value.startsWith(argDef.mark)) {
                if (recognizeArgsOnly) {
                    boolean status = argDef.handler.inspectArg(arg, this);
                    if (status) {
                        arg.recognized = true;
                        break;
                    }
                } else {
                    boolean status = argDef.handler.processArg(arg, this);
                    if (status) {
                        arg.recognized = true;
                        arg.processed = true;
                        break;
                    }
                }
            }
        }
        return true;
    }

    public boolean isAllArgsRecognized() {
        for (Arg arg : args) {
            if (false == arg.recognized) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllArgsProcessed() {
        for (Arg arg : args) {
            if (false == arg.processed) {
                return false;
            }
        }
        return true;
    }

    public void addArgDef(ArgDef argDef) {
        argDefs.add(argDef);
    }

    public void removeArgDef(ArgDef argDef) {
        argDefs.remove(argDef);
    }

    public void addArgDefs(Collection<? extends ArgDef> argDefs) {
        this.argDefs.addAll(argDefs);
    }

    public void addEphemeralArgDefs(Collection<? extends ArgDef> argDefs, int nArgs) {
        this.argDefs.addAll(argDefs);
        this.toRemove.put(curArgIndex + nArgs + 1, argDefs);
    }

    public void removeArgDefs(Collection<? extends ArgDef> argDefs) {
        this.argDefs.removeAll(argDefs);
    }

    public void removeAllArgDefs() {
        argDefs.clear();
    }

    public void removeAllNonStdArgDefs() {
        argDefs.clear();
        argDefs.addAll(addJarArgHandler.getArgDefs());
    }

    private void processCommandLineArgs(int startIndex) throws IOException {
        for (int i = startIndex; i < this.args.size(); i++) {
            Arg arg = this.args.get(i);
            if (arg.processed) {
                continue;
            }
            if (arg.value.startsWith(ARG_SKIP_ONE)) {
                arg.recognized = true;
                arg.processed = true;
            } else if (arg.value.startsWith(ARG_SKIP_START)) {
                arg.recognized = true;
                arg.processed = true;
                int open = 1;
                for (int j = i + 1; j < this.args.size(); j++) {
                    arg = this.args.get(j);
                    if (arg.value.startsWith(ARG_SKIP_START)) {
                        open++;
                    } else if (arg.value.startsWith(ARG_SKIP_END)) {
                        open--;
                    }
                    arg.recognized = true;
                    arg.processed = true;
                    if (0 == open) {
                        i = j;
                        break;
                    }
                }
                if (0 != open) {
                    throw new InvalidCommandLineException("argument " + i + ": \"" + ARG_SKIP_START + "\" requires a matching \"" + ARG_SKIP_END + "\"");
                }
            } else if (arg.value.startsWith(ARG_ARGFILE)) {
                arg.recognized = true;
                arg.processed = true;
                File argFile = arg.getOptionalFile(ARG_ARGFILE, null, "Choose an arg file", null, false);
                addNewArgs(argFile, i);
            }
        }
    }

    public boolean askForArgFile() throws IOException {
        List<FileNameExtensionFilter> customFilters = null;
        return askForArgFile(customFilters, true, true, true);
    }

    public boolean askForArgFile(FileNameExtensionFilter customFilter, boolean txtFilter, boolean argFilter, boolean acceptAll) throws IOException {
        ArrayList<FileNameExtensionFilter> customFilters = new ArrayList<FileNameExtensionFilter>();
        customFilters.add(customFilter);
        return askForArgFile(customFilters, txtFilter, argFilter, acceptAll);
    }

    /**
     * Display a file selection dialog box
     * @param customFilters
     * @param txtFilter
     * @param argFilter
     * @param acceptAll
     * @return true if the user chose a file
     * @throws IOException
     */
    public boolean askForArgFile(List<FileNameExtensionFilter> customFilters, boolean txtFilter, boolean argFilter, boolean acceptAll) throws IOException {
        List<FileNameExtensionFilter> filters = new ArrayList<FileNameExtensionFilter>();
        FileNameExtensionFilter filter;
        if (null != customFilters) {
            filters.addAll(customFilters);
        }
        if (argFilter) {
            filter = new FileNameExtensionFilter("arg files", "arg");
            filters.add(filter);
        }
        if (txtFilter) {
            filter = new FileNameExtensionFilter("txt files", "txt");
            filters.add(filter);
        }
        String title = "Choose a file with command line arguments";
        File argFile = AFileChooser.askForFile(filters, acceptAll, title, ARGFILE_PATH_PREFERENCE_KEY, true, false, false);
        if (null == argFile) {
            return false;
        }
        addNewArgs(argFile);
        return true;
    }

    /**
     * Return the argument list.
     */
    public ArrayList<Arg> getArgs() {
        return args;
    }

    /**
     * Returns <tt>true</tt> if this command line as no arguments.
     *
     * @return <tt>true</tt> if this command line as no arguments
     */
    public boolean isEmpty() {
        return args.isEmpty();
    }

    public String help() {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> helpList = new ArrayList<String>(argDefs.size());
        for (ArgDef argDef : argDefs) {
            helpList.add(argDef.help());
        }
        sb.append(ARG_ARGFILE + "<file>\n" + "   Load an arg file: this argument is replaced by the arguments contained in <file>.\n" + "   <file> may contain argFile itself, however circular reference are not supported.\n" + "   Relative path in <file> are relative to the its location.\n" + "   About \"arg files\":\n" + "   Arg files are simple text files which contain command line arguments.\n" + "   Each argument is written on one line, blank lines are allowed.\n" + "   They may contain zero or more of any valid command line argument.\n" + "   NOTE: arg files use the UTF-8 character encoding. This encoding is partially\n" + "   compatible with ASCII encoding however it is recommended to use UTF-8.\n");
        sb.append(ARG_SKIP_ONE + " (only in arg files)\n" + "   Single line comment.\n");
        sb.append(ARG_SKIP_START + " (only in arg files)\n" + "   Start a multi line comment: Similar to C style comments except that they can be nested.\n");
        sb.append(ARG_SKIP_END + " (only in arg files)\n" + "   End a multi line comment: It MUST be at a beginning of a new line.\n");
        for (String help : helpList) {
            if (!help.isEmpty()) {
                sb.append(help);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public interface ArgHandler {

        /**
         * if this method don't set the arg as processed, another handler will be searched
         * @param arg
         * @return true if the arg is handled (recognized/processed) by the handler
         */
        public boolean inspectArg(Arg arg, CommandLine cl);

        public void endOfCommandLineInspection(CommandLine cl);

        public boolean processArg(Arg arg, CommandLine cl);

        public void endOfCommandLineProcessing(CommandLine cl);
    }

    public interface ArgHandlerProvider {

        public LinkedList<ArgDef> getArgDefs();
    }

    public static class ArgDef implements Serializable, SelfDocumented {

        static final long serialVersionUID = 5846933207890438743L;

        public final String mark;

        public final ArgHandler handler;

        public ArgDef(String mark, ArgHandler handler) {
            this.mark = mark;
            this.handler = handler;
        }

        public ArgDef(ArgDef a) {
            this.mark = a.mark;
            this.handler = a.handler;
        }

        @Override
        public String toString() {
            return "ArgDef: mark=" + mark + ", handler=" + handler;
        }

        public String help() {
            String help;
            if (handler instanceof SelfDocumented) {
                SelfDocumented handlerHelp = (SelfDocumented) handler;
                help = handlerHelp.help();
            } else if (null != this.mark) {
                help = this.mark;
            } else {
                help = "";
            }
            return help;
        }
    }

    public static class Arg implements Serializable {

        static final long serialVersionUID = -2909664676191790094L;

        public final String value;

        public final File refDirectory;

        public final CommandLine cl;

        boolean recognized = false;

        boolean processed = false;

        public Arg(File refDirectory, String value, CommandLine cl) {
            this.refDirectory = refDirectory;
            this.value = value;
            this.cl = cl;
        }

        public Arg(Arg a) {
            this.processed = a.processed;
            this.recognized = a.recognized;
            this.refDirectory = AFileUtilities.hardCopy(a.refDirectory);
            StringBuilder sb = new StringBuilder(a.value);
            this.value = sb.toString();
            this.cl = a.cl;
        }

        public File getFile(String fileName) {
            if (fileName.isEmpty()) {
                return null;
            }
            File f = AFileUtilities.newFile(refDirectory, fileName);
            return f;
        }

        public String getSingleFileName(String mark) {
            String fileName = this.value.substring(mark.length());
            fileName = AFileUtilities.adaptPathSeparator(fileName);
            return fileName;
        }

        public File getSingleFile(String mark) {
            return getFile(getSingleFileName(mark));
        }

        /**
         * Retrieve a file name in the argument.
         * If the file name is missing, a dialog box ask for it
         * If instead of the file name a string starting with ":" is found, the string
         * is displayed in the title of the dialog box
         * @param mark
         * @param filters
         * @param prompt String to display in dialog box title
         * @return the file specified in argument or chosen by user
         * @throws IOException
         */
        public File getOptionalFile(String mark, List<FileNameExtensionFilter> filters, String prompt, String pathKey, boolean warnIfExist) {
            String fileName = getSingleFileName(mark);
            File f;
            try {
                if (fileName.isEmpty()) {
                    f = AFileChooser.askForFile(filters, true, prompt, pathKey, true, false, warnIfExist);
                } else if (fileName.startsWith(":")) {
                    f = AFileChooser.askForFile(filters, true, prompt + fileName, pathKey, true, false, warnIfExist);
                } else {
                    f = AFileUtilities.newFile(refDirectory, fileName);
                }
            } catch (IOException ex) {
                f = null;
            }
            return f;
        }

        public String getString(String mark) {
            return this.value.substring(mark.length());
        }

        public Boolean getBoolean(String mark) {
            String num = value.substring(mark.length()).toUpperCase().trim();
            if (num.isEmpty()) {
                return null;
            }
            if (num.equals("TRUE") | num.equals("1") | num.equals("ON")) return true;
            if (num.equals("FALSE") | num.equals("0") | num.equals("OFF")) return false;
            try {
                int i = Integer.decode(num);
                return i != 0;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        public Integer getInteger(String mark) {
            String num = value.substring(mark.length()).trim();
            if (num.isEmpty()) {
                return null;
            }
            return Integer.decode(num);
        }

        public Long getLong(String mark) {
            String num = value.substring(mark.length()).trim();
            if (num.isEmpty()) {
                return null;
            }
            return Long.decode(num);
        }

        public Integer[] getComaSeparatedIntegers(String mark) {
            String nums = value.substring(mark.length()).trim();
            if (nums.isEmpty()) {
                return new Integer[0];
            }
            String[] splitedNums = nums.split(",");
            Integer[] out = new Integer[splitedNums.length];
            for (int i = 0; i < splitedNums.length; i++) {
                out[i] = Integer.decode(splitedNums[i]);
            }
            return out;
        }

        public boolean isRecognized() {
            return recognized;
        }

        public boolean isProcessed() {
            return processed;
        }

        public void setProcessed() {
            processed = true;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (recognized) {
                sb.append(" ");
            } else {
                sb.append("?");
            }
            if (processed) {
                sb.append("done ");
            } else {
                sb.append("todo ");
            }
            sb.append("\"");
            sb.append(value);
            sb.append("\" (dir: \"");
            try {
                sb.append(refDirectory.getCanonicalFile());
            } catch (IOException ex) {
                Logger.getLogger(CommandLine.class.getName()).log(Level.SEVERE, null, ex);
                sb.append("unavailable, IOException occured");
            }
            sb.append("\")");
            return sb.toString();
        }

        public Class getPlugInClass(String className) throws MalformedURLException, ClassNotFoundException {
            return cl.getPlugInClass(className);
        }
    }

    public static void checkArgs(LinkedList<ArgDef> argDefs, String[] args) throws IOException {
        CommandLine cl = new CommandLine(args);
        System.out.println("command line after arg file expansion:");
        System.out.println(cl);
        cl.removeProcessed();
        System.out.println("Command line after clean up:");
        System.out.println(cl);
        cl.addArgDefs(argDefs);
        boolean argsRemain;
        try {
            do {
                argsRemain = cl.processArg(true);
            } while (argsRemain);
            if (false == cl.isAllArgsRecognized()) {
                throw new InvalidCommandLineException("Not all arguments were recognized:\n" + cl.toString());
            }
        } catch (InvalidCommandLineException ex) {
            throw new InvalidCommandLineException("Argument " + cl.getCurArgIndex() + " is not valid\n" + "Command line dump:\n" + cl.toString(), ex);
        } catch (Throwable ex) {
            throw new RuntimeException("Error happened during evaluation of argument " + cl.getCurArgIndex() + "\nCommand line dump:\n" + cl.toString(), ex);
        }
    }

    public static class LongNumericArgHandler extends SimpleArgHandler {

        public final MutableLong parameter;

        public final String mark;

        public LongNumericArgHandler(MutableLong parameter, String mark) {
            this.parameter = parameter;
            this.mark = mark;
        }

        @Override
        public boolean inspectArg(Arg arg, CommandLine cl) {
            parameter.value = arg.getLong(mark);
            return true;
        }

        public boolean processArg(Arg arg, CommandLine cl) {
            parameter.value = arg.getLong(mark);
            return true;
        }
    }

    public static class LongArrayArgHandler extends SimpleArgHandler {

        public final Long parameter[];

        public final String mark;

        public LongArrayArgHandler(Long parameter[], String mark) {
            this.parameter = parameter;
            this.mark = mark;
        }

        void readParameters(Arg arg) {
            String array = arg.getString(mark).trim();
            String items[] = array.split("[, ]+");
            if (items.length != parameter.length) throw new InvalidCommandLineException(parameter.length + " elements expected, but " + items.length + " elements found");
            for (int i = 0; i < parameter.length; i++) {
                parameter[i] = Long.decode(items[i]);
            }
        }

        @Override
        public boolean inspectArg(Arg arg, CommandLine cl) {
            readParameters(arg);
            return true;
        }

        public boolean processArg(Arg arg, CommandLine cl) {
            readParameters(arg);
            return true;
        }
    }

    public static class BooleanValueArgHandler extends SimpleArgHandler {

        public final MutableBoolean parameter;

        public final String mark;

        public BooleanValueArgHandler(MutableBoolean parameter, String mark) {
            this.parameter = parameter;
            this.mark = mark;
        }

        @Override
        public boolean inspectArg(Arg arg, CommandLine cl) {
            parameter.value = arg.getBoolean(mark);
            return true;
        }

        public boolean processArg(Arg arg, CommandLine cl) {
            parameter.value = arg.getBoolean(mark);
            return true;
        }
    }

    public static class IntegerNumericArgHandler extends SimpleArgHandler {

        public final MutableInteger parameter;

        public final String mark;

        public IntegerNumericArgHandler(MutableInteger parameter, String mark) {
            this.parameter = parameter;
            this.mark = mark;
        }

        @Override
        public boolean inspectArg(Arg arg, CommandLine cl) {
            parameter.value = arg.getInteger(mark);
            return true;
        }

        public boolean processArg(Arg arg, CommandLine cl) {
            parameter.value = arg.getInteger(mark);
            return true;
        }
    }

    public static void main(String[] args) {
        try {
            CommandLine cl = new CommandLine(args);
            System.out.println(cl);
            TestArgHandler1 testArgHandler1 = new TestArgHandler1();
            cl.addArgDef(testArgHandler1.testArgDef1);
            boolean argsRemain;
            do {
                argsRemain = cl.processArg(false);
            } while (argsRemain);
            System.out.println(cl);
            System.out.println("cl.isAllArgsProcessed=" + cl.isAllArgsProcessed());
        } catch (IOException ex) {
            Logger.getLogger(CommandLine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static class TestArgHandler1 implements ArgHandler {

        public final ArgDef testArgDef1 = new ArgDef("arg1", this);

        public boolean processArg(Arg arg, CommandLine cl) {
            System.out.println(this + ".handleArg(" + arg + ")");
            return true;
        }

        public void endOfCommandLineInspection(CommandLine cl) {
            System.out.println(this + ".endOfCommandLineInspection()");
        }

        public boolean inspectArg(Arg arg, CommandLine cl) {
            return true;
        }

        public void endOfCommandLineProcessing(CommandLine cl) {
            System.out.println(this + ".endOfCommandLineProcessing()");
        }
    }
}
