package com.volantis.styling.integration.compiler;

import com.volantis.mcs.themes.InlineStyleSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.styling.impl.compiler.MatcherBuilder;
import com.volantis.styling.impl.engine.matchers.InlineStyleMatcher;
import com.volantis.styling.impl.engine.matchers.Matcher;

/**
 * Test case for building InlineStyleMatcher
 */
public class InlineStyleMatcherBuilderTestCase extends MatcherBuilderTestCaseAbstract {

    /**
     * Test the MatcherBuilder correctly creates an InlineStyleMatcher.
     */
    public void testInlineStyleMatcherBuilder() {
        InlineStyleMatcher styleMatcher = new InlineStyleMatcher(10);
        factoryMock.expects.createInlineStyleMatcher(10).returns(styleMatcher);
        StyleSheetFactory styleSheetFactory = StyleSheetFactory.getDefaultInstance();
        InlineStyleSelector selector = styleSheetFactory.createInlineStyleSelector(10);
        SelectorSequence sequence = styleSheetFactory.createSelectorSequence();
        sequence.addSelector(selector);
        MatcherBuilder matcherBuilder = this.createMatcherBuilder();
        Matcher matcher = matcherBuilder.getMatcher(sequence);
        assertSame("Selector on its own is preserved", styleMatcher, matcher);
    }
}
