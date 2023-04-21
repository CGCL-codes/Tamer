package org.moltools.lib.seq.io.impl;

import org.moltools.lib.seq.io.SequenceInputFormatter;
import org.moltools.lib.seq.io.SequenceOutputFormatter;

/**
 * A database format for delimited text table format (name+tab+sequence). 
 * @author Johan Stenberg
 * @version 1.0
 */
public class TextTableDBFormat extends AbstractDBFormat {

    protected static TextTableFormatter formatter = new TextTableFormatter();

    public TextTableDBFormat() {
        super(null, formatter, null, formatter);
    }

    public SequenceInputFormatter getInFormatter() {
        return formatter;
    }

    public SequenceOutputFormatter getSequenceOutputFormatter() {
        return formatter;
    }
}
