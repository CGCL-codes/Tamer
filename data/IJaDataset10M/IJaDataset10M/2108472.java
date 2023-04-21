package net.sf.smbt.scripts.thingm.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

/**
 * This class contains custom formatting description.
 * 
 * see : http://wiki.eclipse.org/Xtext/Documentation#Formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class dslFormatter extends AbstractDeclarativeFormatter {

    @Override
    protected void configureFormatting(FormattingConfig c) {
        net.sf.smbt.scripts.thingm.services.dslGrammarAccess f = (net.sf.smbt.scripts.thingm.services.dslGrammarAccess) getGrammarAccess();
    }
}
