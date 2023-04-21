package com.testonica.kickelhahn.core.formats.svf;

/**
 * Different types of vector templates
 * 
 * @author Sergei Devadze
 */
public final class VectorTemplate {

    /** DR Header */
    public static final VectorTemplate HDR = new VectorTemplate(SVFCommand.COMMAND_HDR);

    /** IR Header */
    public static final VectorTemplate HIR = new VectorTemplate(SVFCommand.COMMAND_HIR);

    /** DR Trailer */
    public static final VectorTemplate TDR = new VectorTemplate(SVFCommand.COMMAND_TDR);

    /** IR Trailer */
    public static final VectorTemplate TIR = new VectorTemplate(SVFCommand.COMMAND_TIR);

    private final String template;

    private VectorTemplate(String s) {
        template = s;
    }

    public String toString() {
        return template;
    }
}
