package com.byterefinery.rmbench.export.text;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.util.PropertyChangeEvent;
import com.byterefinery.rmbench.preferences.PreferenceHandler;

/**
 * configures syntax coloring for the DDL source viewer 
 *  
 * @author cse
 */
public class DDLSourceViewerConfiguration extends SourceViewerConfiguration {

    public static final String[] CONTENT_TYPES = { DDLPartitionScanner.DDL_SINGLELINE_COMMENT, DDLPartitionScanner.DDL_MULTILINE_COMMENT, DDLPartitionScanner.DDL_CODE };

    private RuleBasedScanner commentScanner;

    private RuleBasedScanner codeScanner;

    private final Token commentToken, stringToken, codeToken, kwToken;

    public DDLSourceViewerConfiguration() {
        commentToken = new Token(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_COMMENTCOLOR)));
        stringToken = new Token(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_STRINGCOLOR)));
        codeToken = new Token(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_CODECOLOR)));
        kwToken = new Token(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_KWCOLOR)));
    }

    public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
        return CONTENT_TYPES;
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer ddr;
        ddr = new DefaultDamagerRepairer(getCommentScanner());
        reconciler.setDamager(ddr, DDLPartitionScanner.DDL_MULTILINE_COMMENT);
        reconciler.setRepairer(ddr, DDLPartitionScanner.DDL_MULTILINE_COMMENT);
        reconciler.setDamager(ddr, DDLPartitionScanner.DDL_SINGLELINE_COMMENT);
        reconciler.setRepairer(ddr, DDLPartitionScanner.DDL_SINGLELINE_COMMENT);
        ddr = new DefaultDamagerRepairer(getCodeScanner());
        reconciler.setDamager(ddr, DDLPartitionScanner.DDL_CODE);
        reconciler.setRepairer(ddr, DDLPartitionScanner.DDL_CODE);
        return reconciler;
    }

    /**
     * @return the scanner for SQL comment
     */
    protected ITokenScanner getCommentScanner() {
        if (commentScanner == null) {
            commentScanner = new RuleBasedScanner();
            commentScanner.setRules(new IRule[0]);
            commentScanner.setDefaultReturnToken(commentToken);
        }
        return commentScanner;
    }

    /**
     * @return the scanner for DDL code
     */
    protected ITokenScanner getCodeScanner() {
        if (codeScanner == null) {
            codeScanner = new RuleBasedScanner();
            IRule[] rules = new IRule[3];
            rules[0] = new SingleLineRule("'", "'", stringToken);
            rules[1] = new SingleLineRule("\"", "\"", codeToken);
            rules[2] = new DDLKeywordRule(kwToken);
            codeScanner.setRules(rules);
            codeScanner.setDefaultReturnToken(codeToken);
        }
        return codeScanner;
    }

    /**
     * handle changes to preferences maintained by this object
     * 
     * @param event the property change event
     * @return true if the change was relevant and applied
     */
    public boolean handlePreferenceChange(PropertyChangeEvent event) {
        if (PreferenceHandler.PREF_DDL_COMMENTCOLOR.equals(event.getProperty())) {
            commentToken.setData(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_COMMENTCOLOR)));
            return true;
        } else if (PreferenceHandler.PREF_DDL_STRINGCOLOR.equals(event.getProperty())) {
            stringToken.setData(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_STRINGCOLOR)));
            return true;
        } else if (PreferenceHandler.PREF_DDL_CODECOLOR.equals(event.getProperty())) {
            codeToken.setData(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_CODECOLOR)));
            return true;
        } else if (PreferenceHandler.PREF_DDL_KWCOLOR.equals(event.getProperty())) {
            kwToken.setData(new TextAttribute(PreferenceHandler.getColor(PreferenceHandler.PREF_DDL_KWCOLOR)));
            return true;
        }
        return false;
    }
}
