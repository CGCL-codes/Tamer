package com.itextpdf.tool.xml.parser.state;

import com.itextpdf.tool.xml.parser.State;
import com.itextpdf.tool.xml.parser.XMLParser;

/**
 * @author redlab_b
 *
 */
public class CloseCommentState implements State {

    private final XMLParser parser;

    /**
	 * @param parser the XMLParser
	 */
    public CloseCommentState(final XMLParser parser) {
        this.parser = parser;
    }

    public void process(final char character) {
        if (character == '-') {
            this.parser.memory().comment().append('-');
        } else if (character == '>' && this.parser.memory().comment().length() == 2) {
            this.parser.memory().comment().setLength(0);
            this.parser.comment();
            this.parser.flush();
            parser.selectState().inTag();
        } else {
            this.parser.append(this.parser.memory().comment().toString());
            this.parser.memory().comment().setLength(0);
            parser.selectState().comment();
        }
    }
}
