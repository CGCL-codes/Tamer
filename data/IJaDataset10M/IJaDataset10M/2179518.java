package de.maramuse.soundcomp.parser;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

public class Preprocessor extends Reader {

    private List<String> includePath = new ArrayList<String>();

    private static boolean dodebug = true;

    private class Condition {

        Condition next = null, previous = null;

        boolean ignore;

        boolean elseSeen = false;

        boolean eclipsed = false;
    }

    private Condition currentCondition = null;

    class Macro {

        String name;

        String definition;

        String file;

        int line;

        String replacementFormat;

        List<String> paramList = new ArrayList<String>();

        MacroDefParser.ParserVal pv = null;

        public Macro(String definition, String file, int line) throws IOException {
            if (dodebug) debug("creating macro " + definition);
            this.definition = definition;
            this.file = file;
            this.line = line;
            parseDefinition();
        }

        private void parseDefinition() throws IOException {
            MacroDefParser p = new MacroDefParser(definition);
            p.yyparse();
            pv = p.yyval;
            if (pv.paramlist != null) paramList = pv.paramlist;
            @SuppressWarnings("unused") List<String> replacement = pv.replacement;
            if (macros.containsKey(pv.text)) {
                Macro m = macros.get(pv.text);
                if (m.definition.equals(definition)) return;
                throw new IOException(MessageFormat.format("Macro ''$1'' definition overwritten in $2($3); Original $4($5)", pv.text, file, line, m.file, m.line));
            }
            Map<String, String> paramMap = new TreeMap<String, String>();
            int pix = 1;
            for (String pp : paramList) {
                if (pp.length() == 0) {
                    throw new IOException(MessageFormat.format("Macro ''$1'' contains nameless parameter in $3($4)", pv.text, file, line));
                }
                if (paramMap.keySet().contains(pp)) {
                    throw new IOException(MessageFormat.format("Macro ''$1'' contains duplicate parameter ''$2'' in $3($4)", pv.text, pp, file, line));
                }
                paramMap.put(pp, "{" + pix++ + "}");
            }
            replacementFormat = "";
            if (pv.replacement != null) for (String fragment : pv.replacement) {
                if (fragment.equals("##")) fragment = "";
                fragment = fragment.replaceAll("'", "''").replaceAll("\\{", "'\\{'");
                if (paramMap.containsKey(fragment)) fragment = paramMap.get(fragment);
                replacementFormat += fragment;
            }
            name = pv.text;
            macros.put(pv.text, this);
        }

        private String callWithParams(List<String> params) throws IOException {
            if (dodebug) debug("calling macro " + name + " with parameter list");
            if (paramList.size() != params.size() - 1) throw new IOException("Macro " + name + " called with " + params.size() + " parameters instead of " + paramList.size());
            return MessageFormat.format(replacementFormat, params.toArray());
        }

        @SuppressWarnings("unused")
        private String callWithParams(Object[] params) throws IOException {
            if (dodebug) debug("calling macro " + name + " with parameter array");
            if (paramList.size() != params.length) throw new IOException("Macro " + name + " called with " + params.length + " parameters instead of " + paramList.size());
            return MessageFormat.format(replacementFormat, params);
        }

        @SuppressWarnings("unused")
        private String callWithoutParams() {
            if (dodebug) debug("calling macro " + name + " without parameter");
            if (paramList.isEmpty()) return replacementFormat;
            throw new IllegalArgumentException("Macro " + name + " called without parameters, needs " + paramList.size());
        }
    }

    private Map<String, Macro> macros = new TreeMap<String, Macro>();

    private class FileStackEntry {

        FileStackEntry() {
            if (dodebug) debug("creating file stack entry");
        }

        Reader reader;

        FileStackEntry previous = null;

        FileStackEntry next = null;

        private char[] buffer = new char[16384];

        private int bufferIndexWrite = 0;

        private int bufferIndexRead = 0;

        private boolean lineHasStarted = true;

        private boolean lastWasCR = false;

        private String fileName;

        private int line = 0;

        private Character pushBackChar = null;
    }

    private FileStackEntry currentFile;

    private Character lastChar = null;

    /**
   * Create a preprocessor initially reading from a defined stream
   * 
   * @param stream
   *          the stream to initially read from
   */
    public Preprocessor(InputStream stream) {
        super();
        if (dodebug) debug("creating preprocessor object on Stream");
        currentFile = new FileStackEntry();
        currentFile.fileName = "<local stream>";
        currentFile.reader = new InputStreamReader(stream);
    }

    /**
   * Create a preprocessor initially reading from a defined reader
   * 
   * @param stream
   *          the stream to initially read from
   */
    public Preprocessor(Reader reader) {
        super();
        if (dodebug) debug("creating preprocessor object on Reader");
        currentFile = new FileStackEntry();
        currentFile.fileName = "<local reader>";
        currentFile.reader = reader;
    }

    /**
   * Create a preprocessor initially reading from a given File
   * 
   * @param file
   *          the file to initially read from
   * @throws FileNotFoundException
   */
    public Preprocessor(File file) throws FileNotFoundException {
        super();
        if (dodebug) debug("creating preprocessor object on File");
        currentFile = new FileStackEntry();
        currentFile.fileName = file.getAbsolutePath();
        currentFile.reader = new FileReader(file);
    }

    /**
   * Create a preprocessor initially taking a String as input source
   * 
   * @param input
   *          the initial input String
   */
    public Preprocessor(String input) {
        super();
        if (dodebug) debug("creating preprocessor object on String");
        currentFile = new FileStackEntry();
        currentFile.fileName = "<local string>";
        currentFile.reader = new StringReader(input);
    }

    public void appendPath(String path) {
        includePath.add(path);
        if (dodebug) debug("appended '" + path + "' to include paths");
    }

    public void appendPath(Collection<String> paths) {
        for (String s : paths) appendPath(s);
    }

    public void appendPath(String[] paths) {
        for (String s : paths) appendPath(s);
    }

    boolean prereadBufferClear = false;

    char pushback = (char) -1;

    int prereadReadIndex = 0;

    class MacroDetector {

        boolean reduce;

        boolean macro;

        boolean skip;

        @SuppressWarnings("hiding")
        boolean pushback;

        public MacroDetector() {
            if (dodebug) debug("macro detector initialized");
            macro = false;
            reduce = false;
            skip = false;
            pushback = false;
        }
    }

    StringBuilder prereadBuffer = new StringBuilder();

    /**
   * reads a single character from preread, buffers and sees if there is a macro definition involved
   */
    @Override
    public int read() throws IOException {
        if (dodebug) debug("preprocessor.read() called");
        int ret = -1;
        if (prereadBufferClear) {
            ret = prereadBuffer.charAt(prereadReadIndex++);
            if (prereadReadIndex == prereadBuffer.length()) {
                prereadBuffer = new StringBuilder();
                prereadBufferClear = false;
            }
            {
                if (dodebug) debug("clearing '" + (char) ret + "' from preread buffer");
                return ret;
            }
        }
        MacroDetector md = null;
        do {
            ret = preread();
            if (dodebug) debug("read '" + (char) ret + "' from '" + currentFile.fileName + "', build '" + prereadBuffer.toString() + (char) ret + "'");
            md = mayReduce(prereadBuffer.toString() + (char) ret);
            if (dodebug) debug("mayReduce returned reduce:" + md.reduce + ", skip=" + md.skip + ", macro=" + md.macro + ", pushback=" + md.pushback);
            if (!md.skip) {
                if (md.pushback) {
                    if (dodebug) debug("pushing back char " + ret);
                    pushback = (char) ret;
                } else {
                    if (dodebug) debug("appending char " + ret + " to preread buffer");
                    prereadBuffer.append((char) ret);
                }
            }
            if (md.reduce && dodebug) debug("symbol detection lead to reduce, getting next symbol");
        } while (!md.reduce);
        if (prereadBuffer.length() == 0) {
            if (dodebug) debug("preread buffer empty, returning EOF");
            return -1;
        }
        if (dodebug) debug("we now must clear the preread buffer before prereading more symbols");
        prereadBufferClear = true;
        prereadReadIndex = 1;
        ret = prereadBuffer.charAt(0);
        if (1 == prereadBuffer.length()) {
            prereadBufferClear = false;
            prereadBuffer = new StringBuilder();
        }
        if (dodebug) debug("returning '" + (char) ret + "' as first char of preread buffer '" + prereadBuffer.toString() + "'");
        return ret;
    }

    /**
   * attempt to determine whether the string is a valid macro call start. if so, we must wait for more input. if not so,
   * we may clear the buffer and give its content to the next parsing stage.
   * 
   * @param s
   *          the current buffer
   * @return
   */
    private MacroDetector mayReduce(String s) throws IOException {
        int state = 0;
        int nobraces = 0;
        MacroDetector md = new MacroDetector();
        int i = 0;
        int openbrace = 0;
        {
            char cc = ' ';
            if (s.length() > 0) cc = s.charAt(s.length() - 1);
            if (cc == 65535 || cc == -1) md.reduce = true;
        }
        for (char c : s.toCharArray()) {
            switch(state) {
                case 0:
                    if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                        i++;
                        continue;
                    }
                    if (Character.isJavaIdentifierStart(c)) {
                        state = 1;
                        break;
                    }
                    md.reduce = true;
                    return md;
                case 1:
                    if (Character.isJavaIdentifierPart(c)) {
                        i++;
                        continue;
                    }
                    if (Character.isWhitespace(c)) state = 2;
                    if (c == '(') {
                        String m = s.substring(0, s.indexOf("(")).trim();
                        if (macros.containsKey(m)) {
                            Macro ma = macros.get(m);
                            if (ma.paramList == null || ma.paramList.isEmpty()) {
                                prereadBuffer = new StringBuilder();
                                prereadReadIndex = 0;
                                currentFile.next = new FileStackEntry();
                                currentFile.next.previous = currentFile;
                                currentFile = currentFile.next;
                                currentFile.fileName = "macro <" + ma.name + ">";
                                currentFile.reader = new StringReader(ma.replacementFormat + "(");
                                md.macro = true;
                                md.skip = true;
                                return md;
                            }
                            nobraces = 1;
                            state = 100;
                        } else {
                            md.reduce = true;
                            return md;
                        }
                        break;
                    }
                    state = 3;
                    break;
                case 2:
                    if (Character.isWhitespace(c)) {
                        i++;
                        continue;
                    }
                    if (c == '(') {
                        String m = s.substring(0, s.indexOf("(")).trim();
                        if (macros.containsKey(m)) {
                            Macro ma = macros.get(m);
                            if (ma.paramList == null || ma.paramList.isEmpty()) {
                                prereadBuffer = new StringBuilder();
                                prereadReadIndex = 0;
                                currentFile.next = new FileStackEntry();
                                currentFile.next.previous = currentFile;
                                currentFile = currentFile.next;
                                currentFile.fileName = "macro <" + ma.name + ">";
                                currentFile.reader = new StringReader(ma.replacementFormat + "(");
                                md.macro = true;
                                md.skip = true;
                                return md;
                            }
                            nobraces = 1;
                            state = 100;
                        } else {
                            md.reduce = true;
                            md.pushback = true;
                            return md;
                        }
                        break;
                    }
                    state = 3;
                    break;
                case 3:
                    String m = s.substring(0, s.length() - 1).trim();
                    if (macros.containsKey(m)) {
                        Macro ma = macros.get(m);
                        if (ma.paramList == null || ma.paramList.isEmpty()) {
                            prereadBuffer = new StringBuilder();
                            prereadReadIndex = 0;
                            currentFile.next = new FileStackEntry();
                            currentFile.next.previous = currentFile;
                            currentFile = currentFile.next;
                            currentFile.fileName = "macro <" + ma.name + ">";
                            currentFile.reader = new StringReader(ma.replacementFormat + " ");
                            currentFile.pushBackChar = new Character(c);
                            md.macro = true;
                            md.skip = true;
                        } else throw new IOException("macro " + s + " called without required parameters");
                    } else {
                        md.reduce = true;
                        md.pushback = true;
                    }
                    return md;
                case 100:
                    if (openbrace == 0) openbrace = i;
                    if (c == '\"') {
                        state = 101;
                        break;
                    }
                    if (c == '(') nobraces++; else if (c == ')') {
                        if (--nobraces == 0) {
                            m = s.substring(0, s.indexOf("(")).trim();
                            if (macros.containsKey(m)) {
                                Macro ma = macros.get(m);
                                if (ma.paramList == null || ma.paramList.isEmpty()) prereadBuffer = new StringBuilder(ma.replacementFormat).append(s.substring(s.indexOf('('))); else {
                                    int j = openbrace;
                                    List<String> params = new ArrayList<String>();
                                    params.add("");
                                    StringBuilder b = new StringBuilder();
                                    boolean instring = false;
                                    char cc;
                                    boolean lastWasEscape = false;
                                    while (j < s.length() && (cc = s.charAt(j)) != ')') {
                                        if (nobraces > 0 || instring) b.append(cc); else {
                                            if (cc == ',') {
                                                params.add(b.toString());
                                                b = new StringBuilder();
                                            } else {
                                                b.append(cc);
                                            }
                                        }
                                        if (c == '\\') lastWasEscape = true; else if (cc == '"') {
                                            if (!lastWasEscape) instring = !instring;
                                        } else if (cc == '(') {
                                            if (!instring) nobraces++;
                                        } else if (cc == ')') {
                                            if (!instring) nobraces--;
                                        }
                                        j++;
                                    }
                                    params.add(b.toString());
                                    prereadReadIndex = 0;
                                    currentFile.next = new FileStackEntry();
                                    currentFile.next.previous = currentFile;
                                    currentFile = currentFile.next;
                                    currentFile.fileName = "macro <" + ma.name + ">";
                                    currentFile.reader = new StringReader(ma.callWithParams(params));
                                    md.macro = true;
                                    md.skip = true;
                                    prereadBuffer = new StringBuilder();
                                }
                            } else md.reduce = true;
                            return md;
                        }
                    }
                    break;
                case 101:
                    if (c == '\"') {
                        state = 100;
                        break;
                    }
                    if (c == '\\') {
                        state = 102;
                        break;
                    }
                    if (c == '\r' || c == '\n') throw new IOException(MessageFormat.format("unclosed string in {0}({1})", currentFile.fileName, currentFile.line));
                    break;
                case 102:
                    if (c == '\r') state = 103;
                    state = 101;
                    break;
                case 103:
                    if (c == '"') state = 100;
                    state = 101;
                    break;
            }
            i++;
        }
        return md;
    }

    /**
   * Reads a single character. #include, #define/#undef, #if((n)def)/#else/#endif are handled. defined macros are NOT
   * replaced, this is to be done in the next step (during read()) to make this not too complex.
   * 
   * @return The character read, or -1 if the end of the stream has been reached
   * 
   * @exception IOException
   *              If an I/O error occurs
   */
    private int preread() throws IOException {
        int c = 0;
        boolean finished = false;
        if (dodebug) debug("preread");
        if (currentFile.bufferIndexRead == currentFile.bufferIndexWrite) {
            if (dodebug) debug("read buffer empty, read ahead some more");
            boolean restart;
            do {
                restart = false;
                @SuppressWarnings("unused") boolean escapedLinebreak = false;
                boolean instring = false;
                do {
                    if (dodebug) debug("inner preread loop entered");
                    if (pushback != (char) -1) {
                        c = pushback;
                        if (dodebug) debug("character " + c + " read from pushback buffer");
                        pushback = (char) -1;
                    } else {
                        c = currentFile.reader.read();
                        if (dodebug) debug("character " + c + " read from input reader");
                    }
                    if (lastChar == null) {
                        if (c == '\\') {
                            if (dodebug) debug("escape intro detected");
                            currentFile.lastWasCR = false;
                            lastChar = Character.valueOf((char) c);
                            continue;
                        }
                    }
                    if (c == '"') {
                        if (lastChar == null) instring = !instring;
                        if (dodebug) debug("string border detected");
                    }
                    if (lastChar != null) {
                        if (c != '\n' && c != '\r') {
                            lastChar = null;
                            if (dodebug) debug("line limiter detected");
                        } else {
                            currentFile.lastWasCR = (c == '\r');
                            currentFile.line++;
                            if (dodebug) debug("escaped line break detected, counted but does not terminate");
                            c = '';
                        }
                    }
                    if (c != 13 && currentFile.lastWasCR) {
                        if (c != 10 && dodebug) debug("non-linebreak following linebreak detected");
                        currentFile.lastWasCR = false;
                        if (c == '\n') {
                            if (dodebug) debug("ignoring <LF> after <CR>");
                            continue;
                        }
                    }
                    if (c == '\n') {
                        if (dodebug) debug("<LF> replaced by <CR>");
                        c = '\r';
                    }
                    if (c != -1 && c < 65535) {
                        if (dodebug) debug("append " + c + " to buffer");
                        currentFile.buffer[currentFile.bufferIndexWrite++] = (char) c;
                    }
                    if (dodebug) {
                        if (c != -1 && c < 65535 && c != '\r') {
                            debug("valid non-CR, non-EOF found");
                            if (((currentFile.bufferIndexWrite + currentFile.buffer.length - currentFile.bufferIndexWrite) % currentFile.buffer.length) < currentFile.buffer.length - 10) {
                                debug("still place in the preread buffer, repeat inner preread loop");
                            } else {
                                debug("buffer almost full, leave inner preread loop to force reduction");
                            }
                        } else if (c == '\r') {
                            debug("<CR>, leave inner preread loop");
                        } else {
                            debug("EOF in inner loop, looking if we have to unwind");
                            if (currentCondition != null && (!currentCondition.ignore || !currentCondition.eclipsed)) {
                                debug("unwinding in inner loop as there is nothing to return (eclipsed by condition)");
                            } else {
                                debug("want to unwind as EOF is reached within hidden text, but no stack entries left");
                                finished = true;
                                c = -1;
                            }
                        }
                    }
                    if (c != -1 && c < 65535 && c != '\r') {
                    } else if (c == '\r') {
                        currentFile.lastWasCR = true;
                    } else {
                        if (currentCondition != null && (!currentCondition.ignore || !currentCondition.eclipsed)) {
                            if (currentFile.previous != null) {
                                currentFile.reader.close();
                                if (currentFile.pushBackChar != null) pushback = currentFile.pushBackChar;
                                currentFile = currentFile.previous;
                                currentFile.next.previous = null;
                                currentFile.next = null;
                                c = '\r';
                            } else {
                                finished = true;
                            }
                        }
                    }
                } while (!finished && c != -1 && c < 65535 && c != '\r' && ((currentFile.bufferIndexWrite + currentFile.buffer.length - currentFile.bufferIndexWrite) % currentFile.buffer.length) < currentFile.buffer.length - 10);
                if (dodebug) debug("inner preread loop left");
                if (instring) {
                    if (dodebug) debug("string open at EOL/EOF, throw error");
                    throw new IOException(MessageFormat.format("unterminated string in {0}({1})", currentFile.fileName, currentFile.line));
                }
                if (currentFile.lineHasStarted) {
                    if (dodebug) debug("'line has started' was set, skip leading whitespace (space and tab)");
                    int read = currentFile.bufferIndexRead;
                    while (read != currentFile.bufferIndexWrite && currentFile.buffer[read] == ' ' && currentFile.buffer[read] == '\t' && currentFile.buffer[read] != -1 && currentFile.buffer[read] != 65535) {
                        read++;
                        if (read >= currentFile.buffer.length) read = 0;
                    }
                    if (currentFile.buffer[read] == '#') {
                        if (dodebug) debug("preprocessor command start detected");
                        read++;
                        if (read >= currentFile.buffer.length) read = 0;
                        while (read != currentFile.bufferIndexWrite && currentFile.buffer[read] == ' ' && currentFile.buffer[read] == '\t' && currentFile.buffer[read] != -1 && currentFile.buffer[read] != 65535) {
                            read++;
                            if (read >= currentFile.buffer.length) read = 0;
                        }
                        StringBuilder prepCommand = new StringBuilder();
                        while (read != currentFile.bufferIndexWrite && currentFile.buffer[read] != '\r' && currentFile.buffer[read] != '\n' && currentFile.buffer[read] != -1 && currentFile.buffer[read] != 65535) {
                            prepCommand.append(currentFile.buffer[read]);
                            read++;
                            if (read >= currentFile.buffer.length) read = 0;
                        }
                        currentFile.bufferIndexRead = currentFile.bufferIndexWrite = 0;
                        restart = execute(prepCommand.toString());
                        read++;
                        if (read >= currentFile.buffer.length) read = 0;
                        currentFile.line++;
                    } else {
                        if (dodebug) {
                            debug("non-preprocessor line detected starting with '" + currentFile.buffer[read] + "'");
                            int rover = read;
                            String line = "";
                            while (rover != currentFile.bufferIndexWrite) line += currentFile.buffer[rover++];
                            debug("line is: '" + line + "'");
                        }
                        if (currentCondition != null && (currentCondition.ignore || currentCondition.eclipsed)) {
                            currentFile.bufferIndexRead = currentFile.bufferIndexWrite;
                            if (dodebug) debug("this line is eclipsed by a condition");
                        }
                    }
                } else {
                    if (dodebug) debug("lineHasStarted was unset");
                }
                if (dodebug) {
                    if (restart) debug("'restart' was set, repeat outer prescan loop");
                    if (currentCondition != null && (currentCondition.ignore || currentCondition.eclipsed)) {
                        debug("repeat outer prescan loop because outmost condition is false or eclipsed");
                    }
                }
            } while (!finished && (restart || (currentCondition != null && (currentCondition.ignore || currentCondition.eclipsed))));
            if (dodebug) debug("left outer prescan loop");
            if (finished && dodebug) debug("left because topmost EOF reached");
            currentFile.lineHasStarted = (c == '\r' || c == '\n');
            if (currentFile.lineHasStarted) {
                if (dodebug) debug("line break detected, increment line counter");
                currentFile.line++;
            }
            currentFile.lastWasCR = (c == '\r');
            if (currentFile.lastWasCR && dodebug) {
                debug("last character was <CR>, saved that state for possible following <NL>");
            }
        }
        if (currentFile.bufferIndexRead < currentFile.bufferIndexWrite) {
            if (dodebug) debug("preread buffer contains text");
            if (pushback != (char) -1) {
                c = pushback;
                pushback = (char) -1;
            } else c = currentFile.buffer[currentFile.bufferIndexRead++];
        } else {
            if (dodebug) debug("preread buffer contains no text, obviously the reader reached its end, leaving it");
            if (currentFile.previous != null) {
                currentFile.reader.close();
                if (currentFile.pushBackChar != null) pushback = currentFile.pushBackChar;
                currentFile = currentFile.previous;
                currentFile.next.previous = null;
                currentFile.next = null;
                if (dodebug) debug("there is an outer reader on the stack, close the inner, unwind and attempt to read again (recursively)");
                return '\r';
            }
        }
        if (dodebug) debug("preread returns character " + c);
        return c;
    }

    private boolean startsWithWord(String hayStack, String needle) {
        return hayStack.startsWith(needle + " ") || hayStack.startsWith(needle + "'\t") || hayStack.equals(needle);
    }

    private boolean execute(String s) throws IOException {
        debug("executing '" + s + "'");
        if (startsWithWord(s, "include")) {
            if (currentCondition == null || !currentCondition.ignore) include(s.substring(8));
            return true;
        } else if (startsWithWord(s, "define")) {
            if (currentCondition == null || !currentCondition.ignore) define(s.substring(7));
            return false;
        } else if (startsWithWord(s, "undef")) {
            if (currentCondition == null || !currentCondition.ignore) undefine(s.substring(7));
            return false;
        } else if (startsWithWord(s, "if")) {
            _if(s.substring(2).trim());
            return false;
        } else if (startsWithWord(s, "ifdef")) {
            _ifdef(s.substring(5).trim());
            return false;
        } else if (startsWithWord(s, "ifndef")) {
            _ifndef(s.substring(6).trim());
            return false;
        } else if (startsWithWord(s, "else")) {
            if (!s.trim().equals("else")) throw new IOException(MessageFormat.format("extra text after #else in {0}({1})", currentFile.fileName, currentFile.line));
            _else();
            return false;
        } else if (startsWithWord(s, "endif")) {
            if (!s.trim().equals("endif")) throw new IOException(MessageFormat.format("extra text after #endif in {0}({1})", currentFile.fileName, currentFile.line));
            _endif();
            return false;
        } else throw new IllegalArgumentException("illegal preprocessor command " + s);
    }

    private void _if(String line) throws IOException {
        boolean eclipsed = (currentCondition != null && (currentCondition.eclipsed || !currentCondition.ignore));
        if (currentCondition != null) {
            currentCondition.next = new Condition();
            currentCondition.next.previous = currentCondition;
            currentCondition = currentCondition.next;
        } else currentCondition = new Condition();
        currentCondition.eclipsed = eclipsed;
        ConditionalParser cparser = new ConditionalParser(line, macros);
        cparser.yyparse();
        ConditionalParser.ParserVal val = cparser.getResult();
        if (val == null) throw new Error("failed to parse #if: " + cparser.error);
        if (!val.isBoolean()) throw new IllegalArgumentException("#if needs a boolean argument, '" + line + "' found.");
        Boolean b = (Boolean) val.eval();
        currentCondition.ignore = (!b) || eclipsed;
    }

    private void _ifdef(String s) throws IOException {
        boolean eclipsed = (currentCondition != null && (currentCondition.eclipsed || !currentCondition.ignore));
        if (currentCondition != null) {
            currentCondition.next = new Condition();
            currentCondition.next.previous = currentCondition;
            currentCondition = currentCondition.next;
        } else currentCondition = new Condition();
        currentCondition.eclipsed = eclipsed;
        currentCondition.ignore = (!macros.containsKey(s)) || eclipsed;
    }

    private void _ifndef(String s) throws IOException {
        if (dodebug) debug("ifndef: " + s);
        boolean eclipsed = (currentCondition != null && (currentCondition.eclipsed || !currentCondition.ignore));
        if (currentCondition != null) {
            currentCondition.next = new Condition();
            currentCondition.next.previous = currentCondition;
            currentCondition = currentCondition.next;
        } else currentCondition = new Condition();
        currentCondition.eclipsed = eclipsed;
        currentCondition.ignore = macros.containsKey(s) || eclipsed;
    }

    private void _endif() throws IOException {
        if (currentCondition == null) throw new IOException(MessageFormat.format("#endif without #if((n)def) in {0}({1})", currentFile.fileName, currentFile.line));
        currentCondition = currentCondition.previous;
    }

    private void _else() throws IOException {
        if (currentCondition != null && currentCondition.eclipsed) return;
        if (currentCondition == null) throw new IOException(MessageFormat.format("#else without #if((n)def) in {0}({1})", currentFile.fileName, currentFile.line));
        if (currentCondition.elseSeen) throw new IOException(MessageFormat.format("duplicate #else in {0}({1})", currentFile.fileName, currentFile.line));
        currentCondition.ignore = !currentCondition.ignore;
        currentCondition.elseSeen = true;
    }

    private void include(String _filetarget) throws IOException {
        if (currentCondition != null && (currentCondition.eclipsed || currentCondition.ignore)) return;
        String path = "";
        boolean found = false;
        String filetarget = _filetarget;
        while (filetarget.startsWith(" ") || filetarget.startsWith("\t")) filetarget = filetarget.substring(1);
        if (new File(filetarget).exists()) {
            path = "";
            found = true;
        } else {
            for (String _path : includePath) {
                if (!_path.endsWith(File.separator) && !_path.endsWith("/")) _path += File.separator;
                if (new File(_path + filetarget).exists()) {
                    path = _path;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            throw new FileNotFoundException(filetarget);
        }
        currentFile.next = new FileStackEntry();
        currentFile.next.previous = currentFile;
        currentFile = currentFile.next;
        currentFile.fileName = path + filetarget;
        currentFile.reader = new FileReader(new File(path + filetarget));
    }

    private void define(String _macro) throws IOException {
        if (currentCondition != null && (currentCondition.eclipsed || !currentCondition.ignore)) return;
        String macro = _macro.trim();
        new Macro(macro, currentFile.fileName, currentFile.line);
    }

    private void undefine(String _macro) throws IOException {
        if (currentCondition != null && (currentCondition.eclipsed || !currentCondition.ignore)) return;
        String macro = _macro.trim();
        if (!macros.containsKey(macro)) throw new IOException(MessageFormat.format("#undef on undefined macro ''{0}'' in {1}({2})", macro, currentFile.fileName, currentFile.line));
        macros.remove(macro);
    }

    @Override
    public void close() throws IOException {
        do {
            currentFile.reader.close();
            currentFile = currentFile.previous;
        } while (currentFile != null);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int r = 0;
        int i = off;
        while (i < len + off && r != -1 && r != 13 && r != 65535) {
            r = read();
            debug("read(...) returns " + (char) r + "(" + r + ")");
            cbuf[i++] = (char) r;
        }
        return i - off;
    }

    public String getCurrentFilename() {
        return currentFile.fileName;
    }

    public int getCurrentLine() {
        return currentFile.line;
    }

    public static void main(final String[] args) {
        InputStream is = null;
        if (args.length == 0) is = System.in; else try {
            is = new FileInputStream(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("File " + args[0] + " not found!");
            System.exit(-1);
            return;
        }
        Preprocessor prep = new Preprocessor(is);
        OutputStream os = System.out;
        if (args.length >= 2) try {
            os = new BufferedOutputStream(new FileOutputStream(new File(args[1])));
        } catch (FileNotFoundException e) {
            System.err.println("File " + args[0] + " could not be created!");
            System.exit(-1);
        }
        int c = 0;
        while (c != -1 && c != 65535) {
            try {
                c = prep.read();
                if (c != -1 && c != 65535) os.write(c);
            } catch (IOException e) {
                try {
                    os.flush();
                    is.close();
                } catch (IOException e1) {
                }
                System.err.println("Error accessing file " + args[0] + " or " + args[1] + "!");
                System.exit(-1);
            }
        }
        try {
            os.flush();
            is.close();
        } catch (IOException ex) {
            System.err.println("Error accessing file " + args[0] + " or " + args[1] + "!");
            System.exit(-1);
        }
        System.exit(0);
    }

    private static void debug(String s) {
        if (dodebug) System.out.println(s);
    }

    public static void setDebug(boolean debug) {
        dodebug = debug;
    }
}
