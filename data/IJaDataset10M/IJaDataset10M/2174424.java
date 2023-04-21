package org.databene.document.fixedwidth;

import org.databene.commons.format.Alignment;
import org.databene.commons.format.PadFormat;

/**
 * Describes a column of a flat file.<br/>
 * <br/>
 * Created: 07.06.2007 13:06:39
 * @author Volker Bergmann
 */
public class FixedWidthColumnDescriptor {

    private String name;

    private PadFormat format;

    public FixedWidthColumnDescriptor(int width, Alignment alignment) {
        this(null, width, alignment, ' ');
    }

    public FixedWidthColumnDescriptor(String name, int width, Alignment alignment) {
        this(name, width, alignment, ' ');
    }

    public FixedWidthColumnDescriptor(int width, Alignment alignment, char padChar) {
        this(null, width, alignment, padChar);
    }

    public FixedWidthColumnDescriptor(String name, int width, Alignment alignment, char padChar) {
        this.name = name;
        this.format = new PadFormat(width, alignment, padChar);
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return format.getLength();
    }

    public Alignment getAlignment() {
        return format.getAlignment();
    }

    public char getPadChar() {
        return format.getPadChar();
    }

    public PadFormat getFormat() {
        return format;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final FixedWidthColumnDescriptor other = (FixedWidthColumnDescriptor) obj;
        if (format == null) {
            if (other.format != null) return false;
        } else if (!format.equals(other.format)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        return name + '[' + format.getLength() + format.getAlignment().getId() + format.getPadChar() + ']';
    }
}
