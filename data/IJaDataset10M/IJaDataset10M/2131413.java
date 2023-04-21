package annone.local.linker.java;

import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LineNumberTableAttribute extends Attribute {

    private class LineNumber {

        private final int startPc;

        private final int lineNumber;

        public LineNumber(int startPc, int lineNumber) {
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }
    }

    private final List<LineNumber> lineNumbers;

    public LineNumberTableAttribute(ConstantPool constantPool) {
        super(constantPool.getUtf8Index("LineNumberTable"));
        this.lineNumbers = new ArrayList<LineNumberTableAttribute.LineNumber>(0);
    }

    @Override
    protected int getLength() {
        return 2 + lineNumbers.size() * 4;
    }

    public void addLineNumber(int startPc, int lineNumber) {
        lineNumbers.add(new LineNumber(startPc, lineNumber));
    }

    @Override
    public void writeTo(DataOutput out) throws IOException {
        super.writeTo(out);
        out.writeShort(lineNumbers.size());
        for (LineNumber ln : lineNumbers) {
            out.writeShort(ln.startPc);
            out.writeShort(ln.lineNumber);
        }
    }
}
