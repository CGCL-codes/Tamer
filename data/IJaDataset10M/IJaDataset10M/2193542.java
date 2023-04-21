package org.achup.generador.generator.basic;

/**
 * 
 * @author Marco Bassaletti Olivos.
 * 
 */
public class StringGenerator extends AbstractBasicGenerator<String> {

    private int length = 32;

    private StringBuilder res;

    /**
     *
     */
    public StringGenerator() {
        super();
        res = new StringBuilder(length);
    }

    public String getName() {
        return "String Generator";
    }

    /**
     *
     * @return
     */
    public String next() {
        int count = 0;
        res.setLength(0);
        while (count < length) {
            char ch = (char) randomData.nextInt(0, 128);
            if (Character.isLetterOrDigit(ch)) {
                res.append(ch);
                count++;
            }
        }
        return res.toString();
    }

    /**
     *
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     *
     * @param length
     */
    public void setLength(int length) {
        if (this.length != length) {
            res = new StringBuilder(length);
        }
        this.length = length;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " length=" + length;
    }
}
