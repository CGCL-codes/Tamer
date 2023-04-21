package net.sourceforge.dotclipse.document;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.IDocument;
import att.grappa.Parser;

/**
 * @author Leo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicalDocument {

    private IDocument document;

    private EditPart editPartRoot;

    public GraphicalDocument(IDocument doc) {
        Assert.isNotNull(doc);
        document = doc;
        String text = doc.get();
    }

    public EditPart getEditPartRoot() {
        if (editPartRoot == null) {
            editPartRoot = new ScalableRootEditPart();
        }
        return editPartRoot;
    }
}
