package joptsimple;

import java.util.Collection;

/**
 * <p>Specification of an option that accepts an optional argument.</p>
 *
 * @param <V> represents the type of the arguments this option accepts
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 * @version $Id: OptionalArgumentOptionSpec.java,v 1.15 2009/03/06 20:35:08 pholser Exp $
 */
class OptionalArgumentOptionSpec<V> extends ArgumentAcceptingOptionSpec<V> {

    OptionalArgumentOptionSpec(String option) {
        super(option, false);
    }

    OptionalArgumentOptionSpec(Collection<String> options, String description) {
        super(options, false, description);
    }

    @Override
    protected void detectOptionArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
        if (arguments.hasMore()) {
            String nextArgument = arguments.peek();
            if (!parser.looksLikeAnOption(nextArgument)) handleOptionArgument(parser, detectedOptions, arguments); else if (isArgumentOfNumberType() && canConvertArgument(nextArgument)) addArguments(detectedOptions, arguments.next()); else detectedOptions.add(this);
        } else detectedOptions.add(this);
    }

    private void handleOptionArgument(OptionParser parser, OptionSet detectedOptions, ArgumentList arguments) {
        if (parser.posixlyCorrect()) {
            detectedOptions.add(this);
            parser.noMoreOptions();
        } else addArguments(detectedOptions, arguments.next());
    }

    @Override
    void accept(OptionSpecVisitor visitor) {
        visitor.visit(this);
    }
}
