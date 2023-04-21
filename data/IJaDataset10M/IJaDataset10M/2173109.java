package org.openXpertya.print.pdf.text;

/**
 * 
 * A special-version of <CODE>LIST</CODE> which use roman-letters.
 * 
 * @see com.lowagie.text.List
 * @version 2003-06-22
 * @author Michael Niedermair
 */
public class RomanList extends List {

    /**
	 * UpperCase or LowerCase
	 */
    protected boolean romanlower;

    /**
	 * Initialization
	 * 
	 * @param symbolIndent	indent
	 */
    public RomanList(int symbolIndent) {
        super(true, symbolIndent);
    }

    /**
	 * Initialization 
	 * @param	romanlower		roman-char in lowercase   
	 * @param 	symbolIndent	indent
	 */
    public RomanList(boolean romanlower, int symbolIndent) {
        super(true, symbolIndent);
        this.romanlower = romanlower;
    }

    /**
	 * set the roman-letters to lowercase otherwise to uppercase
	 * 
	 * @param romanlower
	 */
    public void setRomanLower(boolean romanlower) {
        this.romanlower = romanlower;
    }

    /**
	 * Checks if the list is roman-letter with lowercase
	 *
	 * @return	<CODE>true</CODE> if the roman-letter is lowercase, <CODE>false</CODE> otherwise.
	 */
    public boolean isRomanLower() {
        return romanlower;
    }

    /**
	 * Adds an <CODE>Object</CODE> to the <CODE>List</CODE>.
	 *
	 * @param	o	the object to add.
	 * @return true if adding the object succeeded
	 */
    public boolean add(Object o) {
        if (o instanceof ListItem) {
            ListItem item = (ListItem) o;
            Chunk chunk;
            if (romanlower) chunk = new Chunk(toRomanLowerCase(first + list.size()), symbol.font()); else chunk = new Chunk(toRomanUppercase(first + list.size()), symbol.font());
            chunk.append(".");
            item.setListSymbol(chunk);
            item.setIndentationLeft(symbolIndent);
            item.setIndentationRight(0);
            list.add(item);
        } else if (o instanceof List) {
            List nested = (List) o;
            nested.setIndentationLeft(nested.indentationLeft() + symbolIndent);
            first--;
            return list.add(nested);
        } else if (o instanceof String) {
            return this.add(new ListItem((String) o));
        }
        return false;
    }

    /**
	 * Array with Roman digits.
	 */
    private static final RomanDigit[] roman = { new RomanDigit('m', 1000, false), new RomanDigit('d', 500, false), new RomanDigit('c', 100, true), new RomanDigit('l', 50, false), new RomanDigit('x', 10, true), new RomanDigit('v', 5, false), new RomanDigit('i', 1, true) };

    /** 
	 * changes an int into a lower case roman number.
	 * @param number the original number
	 * @return the roman number (lower case)
	 */
    public static String toRoman(int number) {
        return toRomanLowerCase(number);
    }

    /** 
	 * Changes an int into an upper case roman number.
	 * @param number the original number
	 * @return the roman number (upper case)
	 */
    public static String toRomanUppercase(int number) {
        return toRomanLowerCase(number).toUpperCase();
    }

    /** 
	 * Changes an int into a lower case roman number.
	 * @param number the original number
	 * @return the roman number (lower case)
	 */
    public static String toRomanLowerCase(int number) {
        StringBuffer buf = new StringBuffer();
        if (number < 0) {
            buf.append('-');
            number = -number;
        }
        if (number > 3000) {
            buf.append('|');
            buf.append(toRomanLowerCase(number / 1000));
            buf.append('|');
            number = number - (number / 1000) * 1000;
        }
        int pos = 0;
        while (true) {
            RomanDigit dig = roman[pos];
            while (number >= dig.value) {
                buf.append(dig.digit);
                number -= dig.value;
            }
            if (number <= 0) {
                break;
            }
            int j = pos;
            while (!roman[++j].pre) ;
            if (number + roman[j].value >= dig.value) {
                buf.append(roman[j].digit).append(dig.digit);
                number -= dig.value - roman[j].value;
            }
            pos++;
        }
        return buf.toString();
    }

    /**
	 * Helper class for Roman Digits
	 */
    private static class RomanDigit {

        /** part of a roman number */
        public char digit;

        /** value of the roman digit */
        public int value;

        /** can the digit be used as a prefix */
        public boolean pre;

        /**
		 * Constructs a roman digit
		 * @param digit the roman digit
		 * @param value the value
		 * @param pre can it be used as a prefix
		 */
        RomanDigit(char digit, int value, boolean pre) {
            this.digit = digit;
            this.value = value;
            this.pre = pre;
        }
    }
}
