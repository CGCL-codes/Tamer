package nl.contentanalysis.inet.editor.actions;

import nl.contentanalysis.inet.editor.AnnotationEditor;
import nl.contentanalysis.inet.model.IArticle;
import nl.contentanalysis.tools.ui.UIToolkit;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class NewAnnotationActionFirst extends Action implements ArticleAction {

    public static final String ID = "nl.contentanalysis.actions.NewAnnotationFirst";

    private static final String label = "New Annotation (First sentence)";

    private static final String tooltip = "Create a new annotation on the first sentence";

    private static final String imgLoc = "icons/actions/annotatie_first.png";

    private IArticle annotations;

    public NewAnnotationActionFirst(IWorkbenchWindow window) {
        UIToolkit.SetActionCharacteristics(this, ID, label, tooltip, AnnotationEditor.PLUGIN_ID, imgLoc);
        this.setEnabled(false);
    }

    @Override
    public void run() {
        AnnotationToolkit.firstAnnotation(annotations);
    }

    public void setInput(IArticle annotations) {
        this.annotations = annotations;
        this.setEnabled(annotations != null);
    }
}
