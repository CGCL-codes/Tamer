package de.walware.statet.r.ui.text.r;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import de.walware.eclipsecommons.preferences.IPreferenceAccess;
import de.walware.eclipsecommons.ui.util.ColorManager;
import de.walware.statet.ext.ui.text.DefaultWhitespaceDetector;
import de.walware.statet.ext.ui.text.OperatorRule;
import de.walware.statet.ext.ui.text.StatextTextScanner;
import de.walware.statet.r.core.rlang.RTokens;
import de.walware.statet.r.internal.ui.RIdentifierGroups;
import de.walware.statet.r.internal.ui.RUIPlugin;
import de.walware.statet.r.ui.RUIPreferenceConstants;

/**
 * Scanner for usual R code (except: strings, infix-operator).
 */
public class RCodeScanner extends StatextTextScanner {

    private RWordRule fIdentifierRule;

    public RCodeScanner(final ColorManager colorManager, final IPreferenceStore preferenceStore, final IPreferenceAccess prefs) {
        super(colorManager, preferenceStore, RUIPreferenceConstants.R.TS_CONTEXT_ID);
        initialize();
    }

    @Override
    protected List<IRule> createRules() {
        setDefaultReturnToken(getToken(IRTextTokens.UNDEFINED));
        final List<IRule> rules = new ArrayList<IRule>();
        rules.add(new WhitespaceRule(new DefaultWhitespaceDetector()));
        final OperatorRule opRule = new OperatorRule(RTokens.SEPARATOR_CHARS);
        opRule.addOps(RTokens.SEPARATORS, getToken(IRTextTokens.SEPARATORS));
        opRule.addOps(new String[] { "<-", "->", "<<-", "->>" }, getToken(IRTextTokens.ASSIGNMENT));
        opRule.addOp("=", getToken(IRTextTokens.ASSIGNMENT_SUB_EQUALSIGN));
        opRule.addOps(RTokens.DEFAULT_OPERATORS, getToken(IRTextTokens.OPERATORS));
        opRule.addOps(RTokens.OPERATORS_LOGICAL, getToken(IRTextTokens.OPERATORS_SUB_LOGICAL));
        opRule.addOps(RTokens.OPERATORS_RELATIONAL, getToken(IRTextTokens.OPERATORS_SUB_RELATIONAL));
        opRule.addOps(RTokens.GROUPING, getToken(IRTextTokens.GROUPING));
        opRule.addOps(RTokens.SUBELEMENT_ACCESS, getToken(IRTextTokens.INDEXING));
        opRule.addOps(RTokens.NAMESPACE_ACCESS, getToken(IRTextTokens.INDEXING));
        rules.add(opRule);
        rules.add(new RNumberRule(getToken(IRTextTokens.NUMBERS), getToken(IRTextTokens.UNDEFINED)));
        fIdentifierRule = new RWordRule(getToken(IRTextTokens.DEFAULT), getToken(IRTextTokens.UNDEFINED));
        updateIdentifiers();
        rules.add(fIdentifierRule);
        return rules;
    }

    private void updateIdentifiers() {
        final RIdentifierGroups groups = RUIPlugin.getDefault().getRIdentifierGroups();
        groups.getReadLock().lock();
        try {
            fIdentifierRule.addSpecialWords(RTokens.SPECIAL_CONSTANTS, getToken(IRTextTokens.SPECIAL_CONSTANTS));
            fIdentifierRule.addSpecialWords(RTokens.LOGICAL_CONSTANTS, getToken(IRTextTokens.LOGICAL_CONSTANTS));
            fIdentifierRule.addSpecialWords(RTokens.FLOWCONTROL_RESERVED_WORDS, getToken(IRTextTokens.FLOWCONTROL));
            fIdentifierRule.addSpecialWords(groups.getAssignmentIdentifiers(), getToken(IRTextTokens.IDENTIFIER_SUB_ASSIGNMENT));
            fIdentifierRule.addSpecialWords(groups.getLogicalIdentifiers(), getToken(IRTextTokens.IDENTIFIER_SUB_LOGICAL));
            fIdentifierRule.addSpecialWords(groups.getFlowcontrolIdentifiers(), getToken(IRTextTokens.IDENTIFIER_SUB_FLOWCONTROL));
            fIdentifierRule.addSpecialWords(groups.getCustom1Identifiers(), getToken(IRTextTokens.IDENTIFIER_SUB_CUSTOM1));
            fIdentifierRule.addSpecialWords(groups.getCustom2Identifiers(), getToken(IRTextTokens.IDENTIFIER_SUB_CUSTOM2));
        } finally {
            groups.getReadLock().unlock();
        }
    }

    @Override
    public boolean handleSettingsChanged(final Set<String> contexts, final Object options) {
        if (contexts.contains(RIdentifierGroups.CONTEXT_ID)) {
            fIdentifierRule.resetSpecialWords();
            updateIdentifiers();
        }
        return super.handleSettingsChanged(contexts, options);
    }
}
