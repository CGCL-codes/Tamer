package org.akrogen.tkui.css.core.impl.dom.parsers;

import org.akrogen.tkui.css.core.dom.parsers.CSSParser;
import org.akrogen.tkui.css.core.dom.parsers.CSSParserFactory;

/**
 * {@link CSSParserFactory} implementation.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 */
public class CSSParserFactoryImpl extends CSSParserFactory {

    public CSSParser makeCSSParser() {
        return new CSSParserImpl();
    }
}
