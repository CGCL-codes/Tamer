package net.sf.etl.parsers.internal.term_parser.grammar;

import net.sf.etl.parsers.TextPos;

/**
 * Base class for lightweight grammar objects
 * 
 * @author const
 */
public abstract class EObject {

    /** owner of the element */
    public EObject ownerObject;

    /** feature of the owner that has this element */
    public java.lang.reflect.Field ownerFeature;

    /** start line */
    public TextPos start;

    /** end position */
    public TextPos end;
}
