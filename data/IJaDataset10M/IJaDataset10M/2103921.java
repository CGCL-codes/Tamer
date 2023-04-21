package com.volantis.mcs.protocols.css.reference;

import com.volantis.mcs.protocols.OutputBuffer;

/**
 * A reference to some external CSS that is imported by some inline CSS.
 *
 * @todo This is not currently supported.
 */
public class ImportCSSReference implements CssReference {

    public void writePlaceHolderMarkup(OutputBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    public void updateMarkup(String css) {
        throw new UnsupportedOperationException();
    }
}
