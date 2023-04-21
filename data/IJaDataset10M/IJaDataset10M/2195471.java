package org.scoja.util.escaping;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CharSet {

    protected final int[] fromAscii;

    protected Set pureUnicode;

    public CharSet() {
        this.fromAscii = new int[128 / 32];
        this.pureUnicode = null;
    }

    public CharSet(final String init) {
        this();
        if (init != null) this.add(init);
    }

    public void add(final char c) {
        if (c < 128) this.fromAscii[c / 32] |= 1 << (c % 32);
        if (this.pureUnicode == null) this.pureUnicode = new HashSet();
        this.pureUnicode.add(new Character(c));
    }

    public void add(final String cs) {
        final int len = cs.length();
        for (int i = 0; i < len; i++) this.add(cs.charAt(i));
    }

    public boolean contains(final char c) {
        if (c < 128) return (this.fromAscii[c / 32] & (1 << (c % 32))) != 0;
        return this.pureUnicode != null && this.pureUnicode.contains(new Character(c));
    }

    @Override
    public String toString() {
        return this.toString(CLike.minimal());
    }

    public String toString(final Escaper esc) {
        final StringBuffer sb = new StringBuffer("{");
        boolean first = true;
        for (int i = 0; i < 32 * this.fromAscii.length; i++) {
            final boolean present = (this.fromAscii[i / 32] & (1 << (i % 32))) != 0;
            if (!present) continue;
            if (first) first = false; else sb.append(',');
            sb.append('\'');
            esc.escape((char) i, sb);
            sb.append('\'');
        }
        if (this.pureUnicode != null) {
            for (Iterator it = this.pureUnicode.iterator(); it.hasNext(); ) {
                final char c = ((Character) it.next()).charValue();
                if (first) first = false; else sb.append(',');
                sb.append('\'');
                esc.escape(c, sb);
                sb.append('\'');
            }
        }
        return sb.append("}").toString();
    }
}
