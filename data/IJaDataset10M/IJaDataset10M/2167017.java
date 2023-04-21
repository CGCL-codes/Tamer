package com.volantis.styling.compiler;

import com.volantis.styling.StylingFactory;

/**
 * A factory for creating style sheet compilers for style sheets generated from
 * inline style values.
 */
public class InlineStyleSheetCompilerFactory extends AbstractStyleSheetCompilerFactory {

    /**
     * Create the styling factor and the configuration.
     * The configuration uses a specialised SpecificityCalculator for the inline
     * style values
     * @param resolver
     */
    public InlineStyleSheetCompilerFactory(final FunctionResolver resolver) {
        this(StyleSheetSource.THEME, resolver);
    }

    /**
     * Create the styling factor and the configuration.
     * The configuration uses a specialised SpecificityCalculator for the inline
     * style values
     * @param resolver
     */
    public InlineStyleSheetCompilerFactory(Source source, final FunctionResolver resolver) {
        super(source, resolver);
        configuration.setSpecificityCalculator(StylingFactory.getDefaultInstance().createInlineSpecificityCalculator());
    }

    public StyleSheetCompiler createStyleSheetCompiler() {
        return stylingFactory.createStyleSheetCompiler(configuration);
    }
}
