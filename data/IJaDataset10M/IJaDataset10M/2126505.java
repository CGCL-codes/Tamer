package net.sourceforge.jenesis4java.impl;

import net.sourceforge.jenesis4java.BooleanLiteral;
import net.sourceforge.jenesis4java.ByteLiteral;
import net.sourceforge.jenesis4java.CharLiteral;
import net.sourceforge.jenesis4java.ClassLiteral;
import net.sourceforge.jenesis4java.ClassType;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.DoubleLiteral;
import net.sourceforge.jenesis4java.False;
import net.sourceforge.jenesis4java.FloatLiteral;
import net.sourceforge.jenesis4java.IntLiteral;
import net.sourceforge.jenesis4java.Literal;
import net.sourceforge.jenesis4java.LongLiteral;
import net.sourceforge.jenesis4java.Null;
import net.sourceforge.jenesis4java.OctalLiteral;
import net.sourceforge.jenesis4java.ScientificLiteral;
import net.sourceforge.jenesis4java.ShortLiteral;
import net.sourceforge.jenesis4java.StringLiteral;
import net.sourceforge.jenesis4java.True;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.UnicodeLiteral;

/**
 * Standard <code>Literal</code> implementations.
 */
public abstract class MLiteral extends MVM.MCodeable implements Literal {

    static class MBooleanLiteral extends MLiteral implements BooleanLiteral {

        MBooleanLiteral(MVM vm, boolean val) {
            super(vm, vm.BOOLEAN, Boolean.valueOf(val), val ? "true" : "false");
        }

        public boolean toBoolean() {
            return ((Boolean) this.val).booleanValue();
        }
    }

    static class MByteLiteral extends MLiteral implements ByteLiteral {

        MByteLiteral(MVM vm, byte val) {
            super(vm, vm.BYTE, Byte.valueOf(val), "(byte)" + val);
        }

        public byte toByte() {
            return ((Byte) this.val).byteValue();
        }
    }

    static class MCharLiteral extends MLiteral implements CharLiteral {

        MCharLiteral(MVM vm, char val) {
            super(vm, vm.CHAR, Character.valueOf(val), escapedValue(val));
        }

        protected static StringBuffer escape(char c, StringBuffer b) {
            if (c >= 0x20 && c < 0x7f) {
                if (c == '"' || c == '\'' || c == '\\') {
                    b.append("\\");
                }
                b.append(c);
            } else {
                switch(c) {
                    case '\b':
                        b.append("\\b");
                        break;
                    case '\t':
                        b.append("\\t");
                        break;
                    case '\n':
                        b.append("\\n");
                        break;
                    case '\f':
                        b.append("\\f");
                        break;
                    case '\r':
                        b.append("\\r");
                        break;
                    default:
                        {
                            String charString = Integer.toHexString(c);
                            b.append("\\u");
                            b.append("0000".substring(charString.length()));
                            b.append(charString);
                        }
                }
            }
            return b;
        }

        private static String escapedValue(char val) {
            StringBuffer b = new StringBuffer();
            b.append("'");
            escape(val, b);
            b.append("'");
            return b.toString();
        }

        public char toChar() {
            return ((Character) this.val).charValue();
        }
    }

    static class MClassLiteral extends MLiteral implements ClassLiteral {

        MClassLiteral(MVM vm, ClassType val) {
            super(vm, val, val, val.toString() + ".class");
        }

        public Class<?> toClass() {
            return null;
        }
    }

    static class MDoubleLiteral extends MLiteral implements DoubleLiteral {

        MDoubleLiteral(MVM vm, double val) {
            super(vm, vm.DOUBLE, new Double(val), Double.toString(val) + "D");
        }

        public double toDouble() {
            return ((Double) this.val).doubleValue();
        }
    }

    static class MFalse extends MBooleanLiteral implements False {

        MFalse(MVM vm) {
            super(vm, false);
        }
    }

    static class MFloatLiteral extends MLiteral implements FloatLiteral {

        MFloatLiteral(MVM vm, float val) {
            super(vm, vm.FLOAT, new Float(val), Float.toString(val) + "F");
        }

        public float toFloat() {
            return ((Float) this.val).floatValue();
        }
    }

    static class MIntLiteral extends MLiteral implements IntLiteral {

        MIntLiteral(MVM vm, int val) {
            super(vm, vm.INT, Integer.valueOf(val), Integer.toString(val));
        }

        public int toInt() {
            return ((Integer) this.val).intValue();
        }
    }

    static class MLongLiteral extends MLiteral implements LongLiteral {

        MLongLiteral(MVM vm, long val) {
            super(vm, vm.LONG, Long.valueOf(val), Long.toString(val) + 'L');
        }

        public long toLong() {
            return ((Long) this.val).longValue();
        }
    }

    static class MNull extends MLiteral implements Null {

        MNull(MVM vm) {
            super(vm, vm.NULL, null, "null");
        }
    }

    static class MOctalLiteral extends MCharLiteral implements OctalLiteral {

        MOctalLiteral(MVM vm, char val) {
            super(vm, val);
            this.label = "'\\" + Integer.toOctalString(val) + "'";
        }
    }

    static class MScientificLiteral extends MLiteral implements ScientificLiteral {

        int precision;

        int scale;

        int exponent;

        MScientificLiteral(MVM vm, int precision, int scale, int exponent) {
            super(vm, vm.DOUBLE, null, "" + precision + "." + scale + "e" + exponent);
            this.precision = precision;
            this.scale = scale;
            this.exponent = exponent;
        }

        public int getExponent() {
            return this.exponent;
        }

        public int getPrecision() {
            return this.precision;
        }

        public int getScale() {
            return this.scale;
        }
    }

    static class MShortLiteral extends MLiteral implements ShortLiteral {

        MShortLiteral(MVM vm, short val) {
            super(vm, vm.SHORT, Short.valueOf(val), "(short)" + val);
        }

        public short toShort() {
            return ((Short) this.val).shortValue();
        }
    }

    static class MStringLiteral extends MLiteral implements StringLiteral {

        MStringLiteral(MVM vm, String val) {
            super(vm, vm.STRING, val, escapeValue(val));
        }

        private static void escape(String s, StringBuffer b) {
            int len = s.length();
            for (int i = 0; i < len; i++) {
                MCharLiteral.escape(s.charAt(i), b);
            }
        }

        private static String escapeValue(String val) {
            StringBuffer b = new StringBuffer();
            b.append("\"");
            escape(val, b);
            b.append("\"");
            return b.toString();
        }

        @Override
        public String toString() {
            return (String) this.val;
        }
    }

    static class MTrue extends MBooleanLiteral implements True {

        MTrue(MVM vm) {
            super(vm, true);
        }
    }

    static class MUnicodeLiteral extends MCharLiteral implements UnicodeLiteral {

        MUnicodeLiteral(MVM vm, char val) {
            super(vm, val);
            this.label = "'\\u" + Integer.toHexString(val) + "'";
        }
    }

    Comment comment;

    Type type;

    Object val;

    String label;

    MLiteral(MVM vm, Type type, Object val, String label) {
        super(vm);
        this.type = type;
        this.val = val;
        this.label = label;
    }

    @Override
    public Comment getComment() {
        return this.comment;
    }

    public Type getType() {
        return this.type;
    }

    public MLiteral setComment(Comment comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        out.queue(this.comment);
        out.write(this.label);
        return out;
    }

    public Object toObject() {
        return this.val;
    }
}
