package elliott803.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import elliott803.machine.Instruction;
import elliott803.machine.Word;
import elliott803.telecode.CharToTelecode;
import elliott803.telecode.Telecode;

/**
 * This class provides a simple assembler that can take a basic 803 machine code language
 * input file and create a binary tape suitable for loading by the initial instructions.
 *
 * Usage:
 *    Assemble inputfile outputtape
 *
 * where:
 *    inputfile: is the assembler source input file name
 *    outputtape: is the binary tape output file name
 *
 * Assembler syntax is a sequence of lines.  Each line can have a label which corresponds to
 * the address of that line and then one of:
 *  - a directive
 *  - a constant
 *  - a string
 *  - an instruction
 *  Anything after a * is a comment and is ignored.  Whitespace is also largely ignored.
 *
 * Label:  axxxx:   (ie some text starting with a letter followed by a colon)
 *
 * Directives: =addr
 *                means load at address addr (loads at top of store if omitted)
 *             @addr
 *                means add trigger to entry point addr
 *
 * Constant:   [+/-]nnnn or label
 *
 * String:  'xxxxxxx'
 *             becomes a sequnece of characters (including any necessary shifts)
 *
 * Instruction:  op1 addr1 b op2 addr2
 *
 *             op is a octal opcode, addr is a decimal address or label, b is : or /
 *
 * eg
 *       *
 *       * Simple Hello World program
 *       *
 *                =128                 * Load starting at address 128
 *
 *       loop:   22 index / 30 hello   * Get next character
 *               42 end   : 20 char    * Check for zero at end of string or save character
 *               00 char  / 74 4096    * Write character to teletype
 *               40 loop               * Loop until done  
 *       end:    74 4125  : 74 4126    * Write a CR and LF
 *               72 8191               * And exit
 *
 *       char:    0                    * Character workspace
 *       index:  -1                    * index into string
 *       hello:  'Hello World?'        * Text - will be {LS}HELLO WORLD{FS}? in telecode
 *                0                    * zero marks end of string
 *
 * TODO:
 *   Add constant symbols and simple arithmetic expressions to support eg
 *        CR = 29         * Define symbol
 *        74 4096+CR      * Print CR
 *
 * Note: the syntax of this assembler is loosely based on my faded memory of the real 803
 *        assembler, but it is definitely not correct!  It does enough however to allow some
 *        binary test tapes to be created.
 *
 * @author Baldwin
 */
public class Assembler {

    public static void main(String[] args) throws Exception {
        Args parms = new Args("Assembler", "inputfile outputtape", args, null);
        File inputFile = parms.getInputFile(1);
        File outputFile = parms.getOutputFile(2);
        if (inputFile == null || outputFile == null) {
            parms.usage();
        }
        LineNumberReader input = new LineNumberReader(new FileReader(inputFile));
        FileOutputStream output = new FileOutputStream(outputFile);
        Assembler assembler = new Assembler(input, output);
        assembler.run();
        input.close();
        output.close();
    }

    Assembler(LineNumberReader in, OutputStream out) {
        input = in;
        output = out;
    }

    private static final String COMMENT_CHAR = "*";

    private static final String LABEL_CHAR = ":";

    private static final String ADDRESS_CHAR = "=";

    private static final String TRIGGER_CHAR = "@";

    private static final String STRING_CHAR = "'";

    private static final String LABEL_PATTERN = "[A-Za-z]\\w*";

    private static final String NUMBER_PATTERN = "\\d+";

    private static final String CONSTANT_PATTERN = "[\\+-]?" + NUMBER_PATTERN;

    private static final String VALUE_PATTERN = "((" + LABEL_PATTERN + ")|(" + NUMBER_PATTERN + "))";

    private static final String LOAD_PATTERN = ADDRESS_CHAR + NUMBER_PATTERN;

    private static final String TRIGGER_PATTERN = TRIGGER_CHAR + VALUE_PATTERN;

    private static final String OP_PATTERN = "[01234567]{2}";

    private static final String B_PATTERN = "[:/]";

    private static final String INSTR_PATTERN = OP_PATTERN + "\\s+" + VALUE_PATTERN;

    private static final String LABEL_LINE = LABEL_PATTERN + "\\s*" + LABEL_CHAR + ".*";

    private static final String DIRECTIVE_LINE = "((" + LOAD_PATTERN + ")|(" + TRIGGER_PATTERN + "))";

    private static final String CONSTANT_LINE = "((" + CONSTANT_PATTERN + ")|(" + LABEL_PATTERN + "))";

    private static final String STRING_LINE = STRING_CHAR + ".*" + STRING_CHAR;

    private static final String CODE_LINE = INSTR_PATTERN + "(\\s*" + B_PATTERN + "(\\s*" + INSTR_PATTERN + ")?)?";

    private LineNumberReader input;

    private OutputStream output;

    private Map<String, Integer> symbols;

    private List<SourceLine> sourceCode;

    private List<Long> objectCode;

    private SourceLine loadDirective, triggerDirective;

    private int loadAddress, triggerAddress;

    void run() throws IOException {
        symbols = new HashMap<String, Integer>();
        sourceCode = new ArrayList<SourceLine>();
        buildSymbolsAndSource();
        loadAddress = -1;
        triggerAddress = -1;
        setLoadAndEntryAddress();
        objectCode = new ArrayList<Long>();
        generateObjectCode();
        writeOutputTape();
        if (triggerAddress != -1) {
            writeTrigger();
        }
    }

    private void buildSymbolsAndSource() throws IOException {
        String line = input.readLine();
        while (line != null) {
            int i = line.indexOf(COMMENT_CHAR);
            if (i != -1) line = line.substring(0, i);
            line = line.trim();
            if (line.length() > 0) {
                if (line.matches(LABEL_LINE)) {
                    i = line.indexOf(LABEL_CHAR);
                    String label = line.substring(0, i).trim();
                    symbols.put(label, sourceCode.size());
                    line = line.substring(i + 1);
                    continue;
                }
                if (line.matches(DIRECTIVE_LINE)) {
                    if (line.matches(LOAD_PATTERN)) {
                        if (loadDirective == null) {
                            loadDirective = new SourceLine(line);
                        } else {
                            error(0, "Load directive already set to " + loadDirective.source);
                        }
                    } else if (line.matches(TRIGGER_PATTERN)) {
                        if (triggerDirective == null) {
                            triggerDirective = new SourceLine(line);
                        } else {
                            error(0, "Entry directive already set to " + triggerDirective.source);
                        }
                    } else {
                        error(0, "Incorrect directive: " + line);
                    }
                } else if (line.matches(CONSTANT_LINE)) {
                    sourceCode.add(new SourceLine(line));
                } else if (line.matches(CODE_LINE)) {
                    sourceCode.add(new SourceLine(line));
                } else if (line.matches(STRING_LINE)) {
                    String text = line.substring(1);
                    if (text.endsWith(STRING_CHAR)) text = text.substring(0, text.length() - 1);
                    CharToTelecode converter = new CharToTelecode();
                    byte[] tc = new byte[text.length() * 2];
                    int len = converter.convert(text.toCharArray(), text.length(), tc);
                    for (i = 0; i < len; i++) sourceCode.add(new SourceLine(Byte.toString(tc[i])));
                } else {
                    error(0, "Syntax error: " + line);
                }
            }
            line = input.readLine();
        }
    }

    private void setLoadAndEntryAddress() {
        if (loadDirective != null) {
            loadAddress = Integer.parseInt(loadDirective.source.substring(1));
        } else {
            loadAddress = 8192 - sourceCode.size();
        }
        for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
            entry.setValue(entry.getValue() + loadAddress);
        }
        if (triggerDirective != null) {
            String addr = triggerDirective.source.substring(1);
            if (addr.matches(NUMBER_PATTERN)) {
                triggerAddress = Integer.parseInt(addr);
            } else {
                if (symbols.containsKey(addr)) {
                    triggerAddress = symbols.get(addr);
                } else {
                    error(triggerDirective.lineNo, "Incorrect entry point: " + triggerDirective.source);
                }
            }
        }
    }

    private void generateObjectCode() {
        for (SourceLine sourceLine : sourceCode) {
            String source = sourceLine.source;
            long value = 0;
            if (source.matches(CONSTANT_LINE)) {
                if (source.matches(CONSTANT_PATTERN)) {
                    value = Long.parseLong(source.startsWith("+") ? source.substring(1) : source);
                } else {
                    if (symbols.containsKey(source)) {
                        value = symbols.get(source);
                    } else {
                        error(sourceLine.lineNo, "Unresolved symbol: " + source);
                    }
                }
            } else if (source.matches(CODE_LINE)) {
                int instr1 = 0, b = 0, instr2 = 0;
                StringTokenizer t1 = new StringTokenizer(source, ":/", true);
                instr1 = parseInstruction(sourceLine.lineNo, t1.nextToken().trim());
                if (t1.hasMoreTokens()) {
                    b = t1.nextToken().equals(":") ? 0 : 1;
                    if (t1.hasMoreTokens()) {
                        instr2 = parseInstruction(sourceLine.lineNo, t1.nextToken().trim());
                    }
                }
                value = Word.asInstr(instr1, b, instr2);
            } else {
                error(sourceLine.lineNo, "Incorrect source code: " + source);
            }
            objectCode.add(value);
        }
    }

    private int parseInstruction(int lineNo, String instruction) {
        int op = 0, addr = 0;
        StringTokenizer t = new StringTokenizer(instruction, " \t");
        op = Integer.parseInt(t.nextToken(), 8);
        String target = t.nextToken();
        if (target.matches(NUMBER_PATTERN)) {
            addr = Integer.parseInt(target);
        } else {
            if (symbols.containsKey(target)) {
                addr = symbols.get(target);
            } else {
                error(lineNo, "Unresolved symbol: " + target);
            }
        }
        return Instruction.asInstr(op, addr);
    }

    private void writeOutputTape() throws IOException {
        writeWord(loadAddress - 4);
        for (long value : objectCode) {
            writeWord(value);
        }
    }

    private void writeTrigger() throws IOException {
        int pad = 8192 - (loadAddress + objectCode.size());
        if (pad > 12) {
            System.err.println("WARNING: will need " + pad + " blanks words for trigger.");
            System.err.println("Suggest loading code at address " + (8192 - objectCode.size()));
        }
        for (int i = 0; i < pad + 4; i++) writeWord(0);
        writeWord(Word.asInstr(0, 0, Instruction.asInstr(022, triggerAddress - 4)));
        writeWord(0);
    }

    private void writeWord(long word) throws IOException {
        byte[] bb = new byte[8];
        for (int i = bb.length; i > 0; i--) {
            bb[i - 1] = (byte) (word & Telecode.CHAR_MASK);
            word = word >> 5;
        }
        bb[0] |= 0x10;
        output.write(bb);
    }

    private void error(int lineNo, String msg) {
        System.err.println("Line " + ((lineNo == 0) ? input.getLineNumber() : lineNo) + ": " + msg);
        System.exit(1);
    }

    private class SourceLine {

        int lineNo;

        String source;

        SourceLine(String s) {
            lineNo = input.getLineNumber();
            source = s;
        }
    }
}
