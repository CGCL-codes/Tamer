package urban.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class KappaFormatter extends AbstractDeclarativeFormatter {

    @Override
    protected void configureFormatting(FormattingConfig c) {
        urban.services.KappaGrammarAccess f = (urban.services.KappaGrammarAccess) getGrammarAccess();
        c.setNoSpace().before(f.getExpressionAccess().getCommaKeyword_1_0());
        c.setNoSpace().after(f.getAgentAccess().getLeftParenthesisKeyword_1());
        c.setNoSpace().after(f.getAgentAccess().getIdAssignment_0());
        c.setNoSpace().after(f.getAgentAccess().getCommaKeyword_2_1_0());
        c.setNoSpace().after(f.getSiteAccess().getIdAssignment_0());
        c.setNoSpace().after(f.getSiteAccess().getTildeKeyword_1_0());
        c.setNoSpace().after(f.getSiteAccess().getGroup_1());
        c.setNoSpace().after(f.getSiteAccess().getStateMrkParserRuleCall_1_1_0());
        c.setNoSpace().after(f.getMrkAccess().getNINTTerminalRuleCall_0_0());
        c.setNoSpace().after(f.getMrkAccess().getIdAssignment_1());
        c.setNoSpace().after(f.getLinkAccess().getExclamationMarkKeyword_0_0());
        c.setNoSpace().after(f.getLinkAccess().getCQuestionMarkKeyword_1_0());
        c.setNoSpace().after(f.getLinkAccess().getNINTTerminalRuleCall_0_1_0_0());
        c.setNoSpace().after(f.getLinkAccess().getC_Keyword_0_1_1_0());
        c.setLinewrap().before(f.getModelAccess().getElementsLineParserRuleCall_0());
        c.setLinewrap().after(f.getModelAccess().getElementsLineParserRuleCall_0());
        c.setAutoLinewrap(99999999);
    }
}
