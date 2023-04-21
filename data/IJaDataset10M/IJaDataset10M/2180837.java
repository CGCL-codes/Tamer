package fr.univartois.cril.xtext.alloyplugin.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import fr.univartois.cril.xtext.alloyplugin.api.IALSFile;
import fr.univartois.cril.xtext.alloyplugin.api.IALSSignature;
import fr.univartois.cril.xtext.alloyplugin.api.Identifiable;

/**
 * Class for completion. This is loaded by ALSSourceViewerConfiguration.
 */
public class ALSCompletionProcessor implements IContentAssistProcessor {

    private final ALSEditor editor;

    public ALSCompletionProcessor(ALSEditor editor) {
        this.editor = editor;
    }

    public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
        IALSFile alsFile = editor.getALSFile();
        List<CompletionProposal> props = new ArrayList<CompletionProposal>();
        try {
            String prefix = getPrefix(viewer, offset);
            String firstWordOfLine = getFirstWordOfLine(viewer, offset);
            List<String> keywords = new ArrayList<String>();
            if (alsFile != null) {
                List<Identifiable> dynamic = new ArrayList<Identifiable>();
                if ("check".equals(firstWordOfLine)) dynamic.addAll(alsFile.getAssertions()); else if ("run".equals(firstWordOfLine)) dynamic.addAll(alsFile.getPredicates()); else {
                    List<IALSSignature> signatures = alsFile.getSignatures();
                    keywords.addAll(Arrays.asList(AlloySyntaxConstants.keywords));
                    dynamic.addAll(signatures);
                    for (IALSSignature sig : signatures) {
                        keywords.addAll(Arrays.asList(sig.getFieldsName()));
                    }
                    dynamic.addAll(alsFile.getFunctions());
                    dynamic.addAll(alsFile.getPredicates());
                    dynamic.addAll(alsFile.getAssertions());
                }
                for (Identifiable dyn : dynamic) {
                    keywords.add(dyn.getId());
                }
            }
            for (String keyword : keywords) {
                if (keyword.startsWith(prefix)) props.add(new CompletionProposal(keyword, offset - prefix.length(), prefix.length(), keyword.length()));
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        CompletionProposal[] tab = new CompletionProposal[props.size()];
        return props.toArray(tab);
    }

    public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
        return null;
    }

    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }

    public String getErrorMessage() {
        return null;
    }

    /**
	 * @see http://dev.eclipse.org/newslists/news.eclipse.modeling.gmf/msg01324.html
	 * @param viewer
	 * @param offset
	 * @return the text input by the user
	 * @throws BadLocationException
	 */
    private String getPrefix(ITextViewer viewer, int offset) throws BadLocationException {
        IDocument doc = viewer.getDocument();
        if (doc == null || offset > doc.getLength()) return null;
        int length = 0;
        while (--offset >= 0 && Character.isJavaIdentifierPart(doc.getChar(offset))) length++;
        return doc.get(offset + 1, length);
    }

    /**
	 * @author romuald druelle
	 * @param viewer
	 * @param offset
	 * @return the first Word of the line at which the character of the
	 *         specified position is located.
	 * @throws BadLocationException
	 */
    private String getFirstWordOfLine(ITextViewer viewer, int offset) throws BadLocationException {
        IDocument doc = viewer.getDocument();
        int docLength = doc.getLength();
        int lineOffset = doc.getLineOfOffset(offset);
        int start = doc.getLineOffset(lineOffset);
        if ((doc == null) || (start >= docLength)) return null;
        while (start < docLength && !Character.isJavaIdentifierPart(doc.getChar(start))) start++;
        if ((lineOffset < doc.getLineOfOffset(start))) return null;
        int length = start;
        while (Character.isJavaIdentifierPart(doc.getChar(length))) length++;
        return doc.get(start, length - start);
    }
}
