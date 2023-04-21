package com.google.gwt.eclipse.core.uibinder.sse.css.model;

import org.eclipse.wst.css.core.internal.parser.CSSSourceParser;
import org.eclipse.wst.css.core.internal.parser.ICSSTokenizer;
import org.eclipse.wst.sse.core.internal.ltk.parser.RegionParser;

/**
 * Constructs a list of structured document regions from the tokenizer. This
 * version is CSS Resource-aware, meaning it can handle the custom CSS Resource
 * at-rules.
 * <p>
 * This is required for the {@link CssResourceAwareTokenizer} to be used.
 */
@SuppressWarnings("restriction")
public class CssResourceAwareSourceParser extends CSSSourceParser {

    private ICSSTokenizer tokenizer;

    @Override
    public ICSSTokenizer getTokenizer() {
        if (tokenizer == null) {
            tokenizer = new CssResourceAwareTokenizer();
        }
        return tokenizer;
    }

    @Override
    public RegionParser newInstance() {
        return new CssResourceAwareSourceParser();
    }
}
