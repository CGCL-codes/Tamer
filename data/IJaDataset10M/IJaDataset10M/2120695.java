package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;

/**
 * This class represents a block of text in the dom.
 * <p>
 * For efficiency reasons sometimes it is useful to be able to store contents
 * which contain encoded entities.
 * </p>
 */
public class TextImpl extends CharacterNodeImpl implements Text {

    public TextImpl(DOMFactory factory) {
        super(factory);
        contents = new ReusableStringBuffer();
    }

    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public void accept(DOMVisitor visitor) {
        visitor.visit(this);
    }
}
