package hub.metrik.lang.eprovide.syntaxdisplay;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;

public interface ISyntaxHighlighter {

    void highlightElement(IEditorPart editorPart, EObject modelElement);

    void undoHighlightElement(IEditorPart editorPart, EObject modelElement);
}
