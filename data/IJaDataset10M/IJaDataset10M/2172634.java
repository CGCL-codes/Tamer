package net.sf.elbe.ui.editors.filter;

import net.sf.elbe.core.model.filter.parser.LdapFilterParser;
import net.sf.elbe.core.model.filter.parser.LdapFilterToken;
import net.sf.elbe.ui.ELBEUIPlugin;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.RGB;

public class FilterDamagerRepairer implements IPresentationDamager, IPresentationRepairer {

    private static final TextAttribute DEFAULT_TEXT_ATTRIBUTE = new TextAttribute(ELBEUIPlugin.getDefault().getColor(new RGB(0, 0, 0)));

    private static final TextAttribute AND_OR_NOT_TEXT_ATTRIBUTE = new TextAttribute(ELBEUIPlugin.getDefault().getColor(new RGB(0, 127, 0)), null, SWT.BOLD);

    private static final TextAttribute ATTRIBUTE_TEXT_ATTRIBUTE = new TextAttribute(ELBEUIPlugin.getDefault().getColor(new RGB(127, 0, 85)));

    private static final TextAttribute FILTER_TYPE_TEXT_ATTRIBUTE = new TextAttribute(ELBEUIPlugin.getDefault().getColor(new RGB(255, 0, 0)), null, SWT.BOLD);

    private static final TextAttribute VALUE_TEXT_ATTRIBUTE = new TextAttribute(ELBEUIPlugin.getDefault().getColor(new RGB(0, 0, 127)));

    private static final TextAttribute PARENTHESIS_TEXT_ATTRIBUTE = new TextAttribute(ELBEUIPlugin.getDefault().getColor(new RGB(0, 0, 0)), null, SWT.BOLD);

    private SourceViewer sourceViewer;

    private LdapFilterParser parser;

    private IDocument document;

    public FilterDamagerRepairer(SourceViewer sourceViewer, LdapFilterParser parser) {
        super();
        this.sourceViewer = sourceViewer;
        this.parser = parser;
        this.document = null;
    }

    public void setDocument(IDocument document) {
        this.document = document;
    }

    public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event, boolean documentPartitioningChanged) {
        return partition;
    }

    public void createPresentation(TextPresentation presentation, ITypedRegion damage) {
        this.parser.parse(this.document.get());
        LdapFilterToken[] tokens = this.parser.getModel().getTokens();
        for (int i = 0; i < tokens.length; i++) {
            switch(tokens[i].getType()) {
                case LdapFilterToken.LPAR:
                case LdapFilterToken.RPAR:
                    this.addStyleRange(presentation, tokens[i], PARENTHESIS_TEXT_ATTRIBUTE);
                    break;
                case LdapFilterToken.AND:
                case LdapFilterToken.OR:
                case LdapFilterToken.NOT:
                    this.addStyleRange(presentation, tokens[i], AND_OR_NOT_TEXT_ATTRIBUTE);
                    break;
                case LdapFilterToken.EQUAL:
                case LdapFilterToken.GREATER:
                case LdapFilterToken.LESS:
                case LdapFilterToken.APROX:
                case LdapFilterToken.PRESENT:
                    this.addStyleRange(presentation, tokens[i], FILTER_TYPE_TEXT_ATTRIBUTE);
                    break;
                case LdapFilterToken.ATTRIBUTE:
                    this.addStyleRange(presentation, tokens[i], ATTRIBUTE_TEXT_ATTRIBUTE);
                    break;
                case LdapFilterToken.VALUE:
                    this.addStyleRange(presentation, tokens[i], VALUE_TEXT_ATTRIBUTE);
                    break;
                default:
                    this.addStyleRange(presentation, tokens[i], DEFAULT_TEXT_ATTRIBUTE);
            }
        }
    }

    private void addStyleRange(TextPresentation presentation, LdapFilterToken token, TextAttribute textAttribute) {
        StyleRange range = new StyleRange(token.getOffset(), token.getLength(), textAttribute.getForeground(), textAttribute.getBackground(), textAttribute.getStyle());
        presentation.addStyleRange(range);
    }
}
