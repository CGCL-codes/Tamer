package org.equanda.util.barcode;

/**
 * Class for iterating the AI's of a EAN 128 barcode
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class EAN128Iterator {

    String remainder;

    /**
     * create a EAN128 barcode iterator which can be used to get all the AI's and their
     * value
     *
     * @param barcode barcode to split
     */
    public EAN128Iterator(String barcode) {
        if (barcode == null) {
            throw new NullPointerException("EAN128Iterator constructor needs a non-null barcode to parse");
        }
        if (barcode.startsWith("]C0]C1")) barcode = barcode.substring(6);
        if (barcode.startsWith("]C1")) barcode = barcode.substring(3);
        if (barcode.startsWith("]C2")) barcode = barcode.substring(3);
        if (barcode.startsWith("]CA")) barcode = barcode.substring(3);
        remainder = barcode;
    }

    public boolean hasNext() {
        return !remainder.equals("");
    }

    public EAN128Part next() throws EAN128InvalidData {
        if (remainder.equals("")) return null;
        if (remainder.charAt(0) == '(') {
            int pos = remainder.indexOf('(', 1);
            String ai, value;
            if (pos != -1) {
                ai = remainder.substring(1, pos);
                remainder = remainder.substring(pos);
            } else {
                ai = remainder.substring(1);
                remainder = "";
            }
            pos = ai.indexOf(')');
            if (pos == -1) throw new EAN128InvalidData("remainder " + remainder + " of EAN 128 barcode invalid");
            value = ai.substring(pos + 1);
            ai = ai.substring(0, pos);
            return new EAN128Part(ai, value);
        }
        try {
            String ai, value;
            String tai = remainder.substring(0, 2);
            int len = EAN128Support.getFixedLength(tai);
            if (len != -1) {
                int ailen = 2;
                ai = tai;
                if (!EAN128Support.isValidAI(tai)) {
                    ailen = 3;
                    if (!EAN128Support.isValidAI(remainder.substring(0, ailen))) ailen = 4;
                    ai = remainder.substring(0, ailen);
                }
                value = remainder.substring(ailen, len);
                if ((remainder.length() > len) && isSeparator(remainder.charAt(len))) len++;
                remainder = remainder.substring(len);
            } else {
                for (len = 2; len < remainder.length() && !isSeparator(remainder.charAt(len)); len++) {
                }
                int ailen = 0;
                if (EAN128Support.isValidAI(remainder.substring(0, 2))) {
                    ailen = 2;
                } else if (EAN128Support.isValidAI(remainder.substring(0, 3))) {
                    ailen = 3;
                } else if (EAN128Support.isValidAI(remainder.substring(0, 4))) ailen = 4;
                if (ailen == 0) throw new EAN128InvalidData("remainder " + remainder + " starts with unknown AI");
                ai = remainder.substring(0, ailen);
                value = remainder.substring(ailen, len);
                if (len < remainder.length()) {
                    remainder = remainder.substring(len + 1);
                } else {
                    remainder = remainder.substring(len);
                }
            }
            return new EAN128Part(ai, value);
        } catch (Exception ex) {
            throw new EAN128InvalidData(ex, "remainder " + remainder + " of EAN 128 barcode invalid");
        }
    }

    /**
     * check for seperator characters, we assume anything which can not be
     * represented by the barcode characters set as seperator, plus GS (0x1d)
     *
     * @param c character to check
     * @return true if possible seperator
     */
    private boolean isSeparator(char c) {
        return !(c >= 0 && c <= 127 && c != 0x1d);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
