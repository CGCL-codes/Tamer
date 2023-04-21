package org.gguth.sample;

import org.antlr.runtime.Parser;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.RecognizerSharedState;

/**
 * Created by IntelliJ IDEA.
 * User: jbunting
 * Date: Nov 16, 2008
 * Time: 12:04:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleParser extends Parser {

    public SampleParser(TokenStream input) {
        super(input);
    }

    public SampleParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public Object rule() {
        return null;
    }
}
