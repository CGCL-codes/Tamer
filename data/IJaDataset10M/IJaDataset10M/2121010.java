package net.sf.fraglets.crm114j;

/**
 * @version $Id: StringHash.java,v 1.1 2004-03-21 21:52:26 marion Exp $
 */
public class StringHash {

    public static int hash(Tokenizer tok) {
        return hash(tok.getBuffer(), tok.getOff(), tok.getLen());
    }

    public static int hash(char[] str, int off, int len) {
        int i;
        int hval;
        char chtmp;
        hval = len;
        for (i = 0; i < len; i++) {
            char c = str[i];
            hval ^= (c << 16 | c);
            hval = hval + ((hval >>> 12) & 0x0000ffff);
            hval = (hval & 0x00ffff00) | (hval << 24) | (hval >>> 24);
            hval = (hval << 3) + (hval >> 29);
        }
        return hval;
    }
}
