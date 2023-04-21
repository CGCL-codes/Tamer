package avrora.syntax.raw;

import avrora.Main;
import avrora.arch.AbstractArchitecture;
import avrora.core.Program;
import avrora.core.ProgramReader;
import cck.text.StringUtil;
import cck.util.Arithmetic;
import cck.util.Util;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

/**
 * @author Ben L. Titzer
 */
public class RAWReader extends ProgramReader {

    protected class Record {

        protected final int addr;

        protected boolean code;

        protected List bytes;

        protected List strings;

        protected Record(int addr) {
            this.addr = addr;
            bytes = new ArrayList(4);
            strings = new ArrayList(1);
        }
    }

    boolean inCode;

    public RAWReader() {
        super("The \"raw\" program format reader reads programs that consist of small records of " + "bytes and instructions.");
    }

    public Program read(String[] args) throws Exception {
        if (args.length == 0) Util.userError("no input files");
        if (args.length != 1) Util.userError("input type \"raw\" accepts only one file at a time.");
        AbstractArchitecture arch = getArchitecture();
        String fname = args[0];
        List records = parseFile(fname);
        Program p = createProgram(arch, records);
        loadProgram(p, records);
        return p;
    }

    private List parseFile(String fname) throws Exception {
        Main.checkFileExists(fname);
        BufferedReader reader = new BufferedReader(new FileReader(fname));
        List records = new LinkedList();
        int cntr = 1;
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            Record r = parse(cntr++, line);
            if (r != null) records.add(r);
        }
        return records;
    }

    private Program createProgram(AbstractArchitecture arch, List records) {
        boolean init = false;
        int min = 0;
        int max = 0;
        Iterator i = records.iterator();
        while (i.hasNext()) {
            Record r = (Record) i.next();
            if (init) {
                min = Arithmetic.min(min, r.addr);
                max = Arithmetic.max(max, r.addr + r.bytes.size());
            } else {
                init = true;
                min = r.addr;
                max = r.addr + r.bytes.size();
            }
        }
        return new Program(arch, min, max);
    }

    private void loadProgram(Program p, List records) {
        Iterator i = records.iterator();
        while (i.hasNext()) {
            Record r = (Record) i.next();
            loadBytes(r, p);
            loadInstr(r, p);
        }
    }

    private void loadBytes(Record r, Program p) {
        int pos = r.addr;
        Iterator b = r.bytes.iterator();
        while (b.hasNext()) {
            Byte by = (Byte) b.next();
            p.writeProgramByte(by.byteValue(), pos++);
        }
    }

    private void loadInstr(Record r, Program p) {
        if (r.code) {
            for (int pos = r.addr; pos < r.addr + r.bytes.size(); pos += 2) p.disassembleInstr(pos);
        }
    }

    protected Record parse(int lineno, String line) throws Exception {
        CharacterIterator i = new StringCharacterIterator(line);
        StringUtil.skipWhiteSpace(i);
        char ch = i.current();
        if (ch == CharacterIterator.DONE) return null;
        if (ch == ';') return null;
        if (ch == '.') return readDirective(i); else return readRecord(ch, lineno, i);
    }

    private Record readRecord(char ch, int lineno, CharacterIterator i) throws Exception {
        if (!StringUtil.isHexDigit(ch)) Util.userError("syntax error @ " + lineno + ':' + i.getIndex());
        Record record = new Record(readAddress(i, ch));
        record.code = inCode;
        StringUtil.expectChar(i, ':');
        while (true) {
            StringUtil.skipWhiteSpace(i);
            ch = i.current();
            if (StringUtil.isHexDigit(ch)) readByte(record, i); else if (ch == '"') readString(record, i); else if (ch == ';') break; else if (ch == CharacterIterator.DONE) break; else Util.userError("syntax error at " + i.getIndex());
        }
        return record;
    }

    private Record readDirective(CharacterIterator i) {
        i.next();
        String dir = StringUtil.readIdentifier(i);
        if ("code".equals(dir)) inCode = true; else if ("data".equals(dir)) inCode = false;
        return null;
    }

    private int readAddress(CharacterIterator i, char ch) {
        if (ch == '0') {
            i.next();
            StringUtil.peekAndEat(i, 'x');
        }
        return StringUtil.readHexValue(i, 8);
    }

    private void readByte(Record record, CharacterIterator i) {
        int readByte = StringUtil.readHexValue(i, 2);
        record.bytes.add(new Byte((byte) readByte));
        if (StringUtil.isHexDigit(i.current())) Util.userError("constant too long");
    }

    private void readString(Record record, CharacterIterator i) {
        char ch;
        StringBuffer buf = new StringBuffer();
        while ((ch = i.next()) != CharacterIterator.DONE) {
            if (ch == '"') {
                i.next();
                break;
            }
            buf.append(ch);
        }
        record.strings.add(buf.toString());
    }
}
