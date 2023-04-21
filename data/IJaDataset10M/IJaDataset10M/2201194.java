package net.sourceforge.pmd.eclipse.ui.quickfix;

import org.eclipse.jface.text.Document;

/**
 * 
 * @author Brian Remedios
 *
 */
public class CommentOutLineFix extends AbstractFix {

    public CommentOutLineFix() {
        super("Comment out the line");
    }

    /**
     * @see net.sourceforge.pmd.eclipse.Fix#fix(java.lang.String, int)
     */
    public String fix(String sourceCode, int lineNumber) {
        final Document document = new Document(sourceCode);
        return document.get();
    }
}
