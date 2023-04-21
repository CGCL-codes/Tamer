package net.sf.etl.parsers.internal.term_parser.compiler.nodes;

import java.util.Collections;
import java.util.Set;
import net.sf.etl.parsers.SyntaxRole;
import net.sf.etl.parsers.Terms;
import net.sf.etl.parsers.TokenKey;
import net.sf.etl.parsers.Tokens;
import net.sf.etl.parsers.internal.term_parser.compiler.StateMachineBuilder;
import net.sf.etl.parsers.internal.term_parser.states.AdvanceState;
import net.sf.etl.parsers.internal.term_parser.states.LookAheadSet;
import net.sf.etl.parsers.internal.term_parser.states.PhraseKindChoice;
import net.sf.etl.parsers.internal.term_parser.states.ReportErrorState;
import net.sf.etl.parsers.internal.term_parser.states.ReportToken;
import net.sf.etl.parsers.internal.term_parser.states.SkipIgnorableState;
import net.sf.etl.parsers.internal.term_parser.states.State;
import net.sf.etl.parsers.internal.term_parser.states.buildtime.NopState;

/**
 * Token reporting node.
 * 
 * @author const
 */
public class TokenNode extends Node {

    /** a kind of token */
    private final Terms termKind;

    /** a lexical kind of token */
    private final TokenKey tokenKey;

    /** a role of token */
    private final SyntaxRole role;

    /** a text */
    private final String text;

    /**
	 * A constructor from fields
	 * 
	 * @param role
	 *            a token role
	 * @param termKind
	 *            a term kind for the token
	 * @param text
	 *            a text of token
	 * @param tokenKey
	 *            a token kind
	 */
    public TokenNode(Terms termKind, SyntaxRole role, TokenKey tokenKey, String text) {
        super();
        this.role = role;
        this.termKind = termKind;
        this.text = text;
        this.tokenKey = tokenKey;
    }

    /**
	 * @return Returns the role.
	 */
    public SyntaxRole getRole() {
        return role;
    }

    /**
	 * @return numeric suffix
	 */
    public String getNummericSuffix() {
        return tokenKey.suffix();
    }

    /**
	 * @return a quote
	 */
    public String getQuote() {
        return tokenKey.startQuote();
    }

    /**
	 * @return Returns the termKind.
	 */
    public Terms getTermKind() {
        return termKind;
    }

    /**
	 * @return Returns the text.
	 */
    public String getText() {
        return text;
    }

    /**
	 * @return Returns the tokenKind.
	 */
    public TokenKey getTokenKind() {
        return tokenKey;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public State buildStates(StateMachineBuilder b, State normalExit, State errorExit) {
        final String errorId;
        final String arg;
        if (text != null) {
            errorId = "syntax.UnexpectedToken.expectingText";
            arg = text;
        } else {
            errorId = "syntax.UnexpectedToken.expectingKind";
            arg = tokenKey == null ? "*" : tokenKey.toString();
        }
        final ReportErrorState choiceErrorExit = new ReportErrorState(errorExit, errorId, new Object[] { arg });
        State head = new SkipIgnorableState(normalExit, tokenKey == null || tokenKey.kind() != Tokens.DOC_COMMENT);
        head = new AdvanceState(head);
        final ReportToken tk = new ReportToken(head, termKind, role);
        head = tk;
        final State exit = new NopState(choiceErrorExit);
        final PhraseKindChoice choice = new PhraseKindChoice(exit);
        final LookAheadSet set = buildLookAhead(Collections.<StateMachineBuilder>emptySet());
        set.buildChoiceStates(choice, head);
        return choice;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected boolean calcMatchesEmpty() {
        return false;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected LookAheadSet createLookAhead(Set<StateMachineBuilder> visitedBuilders) {
        if (text == null) {
            return LookAheadSet.get(tokenKey);
        } else {
            return LookAheadSet.getWithText(tokenKey, text);
        }
    }
}
