package org.hibnet.lune.format.definition;

/**
 * The segment file definition
 */
public class SegmentDefinition extends LuceneFileDefinition {

    /**
     * Constructor
     * 
     * @param format
     *            the file format
     */
    public SegmentDefinition(FileFormat format) {
        super("segment", format);
    }

    @Override
    public <T, P, X extends Exception> T accept(LuceneFileVisitor<T, P, X> visitor, P params) throws X {
        return visitor.visit(this, params);
    }

    @Override
    public String toString() {
        return "segment file";
    }
}
