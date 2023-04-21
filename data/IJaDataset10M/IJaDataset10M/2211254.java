package org.spbu.pldoctoolkit.refactor.InfElemPattern;

import org.spbu.pldoctoolkit.refactor.InfElemPattern.TextFragments.TextFragment;

public class EndElement extends PatternElement {

    private TextFragment textFragment;

    public EndElement(TextFragment textFragment, PatternElement parent) {
        this.parent = parent;
        this.textFragment = textFragment;
        textFragment.setEndElement(this);
    }

    public TextFragment getTextFragment() {
        return textFragment;
    }
}
