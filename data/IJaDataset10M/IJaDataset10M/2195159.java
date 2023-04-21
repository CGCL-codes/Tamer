package de.maramuse.soundcomp.parser;

public class LineComment extends Comment {

    public LineComment(String s) {
        super(SCParser.LINECOMMENT, s);
    }
}
