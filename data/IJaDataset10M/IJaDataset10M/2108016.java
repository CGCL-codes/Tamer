package net.sourceforge.olympos.dsl.platform.serializer;

import com.google.inject.Inject;
import java.util.List;
import net.sourceforge.olympos.dsl.platform.services.PlatformGrammarAccess;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;

@SuppressWarnings("restriction")
public class AbstractPlatformSyntacticSequencer extends AbstractSyntacticSequencer {

    protected PlatformGrammarAccess grammarAccess;

    protected AbstractElementAlias match_Action___LeftCurlyBracketKeyword_1_0_RightCurlyBracketKeyword_1_2__q;

    protected AbstractElementAlias match_Actions___DefaultKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q;

    protected AbstractElementAlias match_Controller___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_2__q;

    protected AbstractElementAlias match_DefaultController___LeftCurlyBracketKeyword_3_0_RightCurlyBracketKeyword_3_2__q;

    protected AbstractElementAlias match_InputType___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_3__q;

    @Inject
    protected void init(IGrammarAccess access) {
        grammarAccess = (PlatformGrammarAccess) access;
        match_Action___LeftCurlyBracketKeyword_1_0_RightCurlyBracketKeyword_1_2__q = new GroupAlias(true, false, new TokenAlias(false, false, grammarAccess.getActionAccess().getLeftCurlyBracketKeyword_1_0()), new TokenAlias(false, false, grammarAccess.getActionAccess().getRightCurlyBracketKeyword_1_2()));
        match_Actions___DefaultKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q = new GroupAlias(true, false, new TokenAlias(false, false, grammarAccess.getActionsAccess().getDefaultKeyword_3_0()), new TokenAlias(false, false, grammarAccess.getActionsAccess().getLeftCurlyBracketKeyword_3_1()), new TokenAlias(false, false, grammarAccess.getActionsAccess().getRightCurlyBracketKeyword_3_3()));
        match_Controller___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_2__q = new GroupAlias(true, false, new TokenAlias(false, false, grammarAccess.getControllerAccess().getLeftCurlyBracketKeyword_2_0()), new TokenAlias(false, false, grammarAccess.getControllerAccess().getRightCurlyBracketKeyword_2_2()));
        match_DefaultController___LeftCurlyBracketKeyword_3_0_RightCurlyBracketKeyword_3_2__q = new GroupAlias(true, false, new TokenAlias(false, false, grammarAccess.getDefaultControllerAccess().getLeftCurlyBracketKeyword_3_0()), new TokenAlias(false, false, grammarAccess.getDefaultControllerAccess().getRightCurlyBracketKeyword_3_2()));
        match_InputType___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_3__q = new GroupAlias(true, false, new TokenAlias(false, false, grammarAccess.getInputTypeAccess().getLeftCurlyBracketKeyword_2_0()), new TokenAlias(false, false, grammarAccess.getInputTypeAccess().getRightCurlyBracketKeyword_2_3()));
    }

    @Override
    protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
        return "";
    }

    @Override
    protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
        if (transition.getAmbiguousSyntaxes().isEmpty()) return;
        List<INode> transitionNodes = collectNodes(fromNode, toNode);
        for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
            List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
            if (match_Action___LeftCurlyBracketKeyword_1_0_RightCurlyBracketKeyword_1_2__q.equals(syntax)) emit_Action___LeftCurlyBracketKeyword_1_0_RightCurlyBracketKeyword_1_2__q(semanticObject, getLastNavigableState(), syntaxNodes); else if (match_Actions___DefaultKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q.equals(syntax)) emit_Actions___DefaultKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q(semanticObject, getLastNavigableState(), syntaxNodes); else if (match_Controller___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_2__q.equals(syntax)) emit_Controller___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_2__q(semanticObject, getLastNavigableState(), syntaxNodes); else if (match_DefaultController___LeftCurlyBracketKeyword_3_0_RightCurlyBracketKeyword_3_2__q.equals(syntax)) emit_DefaultController___LeftCurlyBracketKeyword_3_0_RightCurlyBracketKeyword_3_2__q(semanticObject, getLastNavigableState(), syntaxNodes); else if (match_InputType___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_3__q.equals(syntax)) emit_InputType___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_3__q(semanticObject, getLastNavigableState(), syntaxNodes); else acceptNodes(getLastNavigableState(), syntaxNodes);
        }
    }

    /**
	 * Syntax:
	 *     ('{' '}')?
	 */
    protected void emit_Action___LeftCurlyBracketKeyword_1_0_RightCurlyBracketKeyword_1_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
        acceptNodes(transition, nodes);
    }

    /**
	 * Syntax:
	 *     ('default' '{' '}')?
	 */
    protected void emit_Actions___DefaultKeyword_3_0_LeftCurlyBracketKeyword_3_1_RightCurlyBracketKeyword_3_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
        acceptNodes(transition, nodes);
    }

    /**
	 * Syntax:
	 *     ('{' '}')?
	 */
    protected void emit_Controller___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
        acceptNodes(transition, nodes);
    }

    /**
	 * Syntax:
	 *     ('{' '}')?
	 */
    protected void emit_DefaultController___LeftCurlyBracketKeyword_3_0_RightCurlyBracketKeyword_3_2__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
        acceptNodes(transition, nodes);
    }

    /**
	 * Syntax:
	 *     ('{' '}')?
	 */
    protected void emit_InputType___LeftCurlyBracketKeyword_2_0_RightCurlyBracketKeyword_2_3__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
        acceptNodes(transition, nodes);
    }
}
