package org.rubypeople.rdt.internal.ui.search;

import org.rubypeople.rdt.core.IRubyElement;

public class RubyElementLine {

    private IRubyElement fElement;

    private int fLine;

    private String fLineContents;

    /**
	 * @param element either an IRubyScript
	 */
    public RubyElementLine(IRubyElement element, int line, String lineContents) {
        fElement = element;
        fLine = line;
        fLineContents = lineContents;
    }

    public IRubyElement getRubyElement() {
        return fElement;
    }

    public int getLine() {
        return fLine;
    }

    public String getLineContents() {
        return fLineContents;
    }
}
