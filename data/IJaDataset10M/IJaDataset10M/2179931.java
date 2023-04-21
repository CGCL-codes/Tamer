package net.sourceforge.dotclipse.editors;

import net.sourceforge.dotclipse.document.DotPartitionScanner;
import org.eclipse.jface.text.contentassist.ContentAssistant;

/**
 * @author Leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DotContentAssistant extends ContentAssistant {

    /**
     * 
     */
    public DotContentAssistant() {
        super();
        setDocumentPartitioning(DotPartitionScanner.CODE);
    }
}
