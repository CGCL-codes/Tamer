package joptsimple;

import static joptsimple.ParserRules.*;

/**
 * <p>Abstraction of parser state; mostly serves to model how a parser behaves depending
 * on whether end-of-options has been detected.</p>
 *
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 * @version $Id: OptionParserState.java,v 1.6 2009/03/06 20:35:08 pholser Exp $
 */
abstract class OptionParserState {

    static OptionParserState noMoreOptions() {
        return new OptionParserState() {

            @Override
            protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
                detectedOptions.addNonOptionArgument(arguments.next());
            }
        };
    }

    static OptionParserState moreOptions(final boolean posixlyCorrect) {
        return new OptionParserState() {

            @Override
            protected void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions) {
                String candidate = arguments.next();
                if (isOptionTerminator(candidate)) parser.noMoreOptions(); else if (isLongOptionToken(candidate)) parser.handleLongOptionToken(candidate, arguments, detectedOptions); else if (isShortOptionToken(candidate)) parser.handleShortOptionToken(candidate, arguments, detectedOptions); else {
                    if (posixlyCorrect) parser.noMoreOptions();
                    detectedOptions.addNonOptionArgument(candidate);
                }
            }
        };
    }

    protected abstract void handleArgument(OptionParser parser, ArgumentList arguments, OptionSet detectedOptions);
}
