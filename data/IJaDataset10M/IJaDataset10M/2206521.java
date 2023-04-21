package org.xtext.cg2009.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.xtext.cg2009.services.EntitiesGrammarAccess;

/**
 * This class contains custom formatting description.
 * 
 * see : http://wiki.eclipse.org/Xtext/Documentation#Formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class EntitiesFormatter extends AbstractDeclarativeFormatter {

    @Override
    protected void configureFormatting(FormattingConfig c) {
        EntitiesGrammarAccess f = (EntitiesGrammarAccess) getGrammarAccess();
        c.setLinewrap(1).after(f.getEntityAccess().getLeftCurlyBracketKeyword_3());
        c.setLinewrap(2).after(f.getEntityAccess().getRightCurlyBracketKeyword_5());
        c.setLinewrap().after(f.getSimplePropertyAccess().getTypeAssignment_3());
        c.setLinewrap().after(f.getReferencePropertyAccess().getTypeEntityCrossReference_3_0());
        c.setIndentation(f.getEntityAccess().getLeftCurlyBracketKeyword_3(), f.getEntityAccess().getRightCurlyBracketKeyword_5());
    }
}
