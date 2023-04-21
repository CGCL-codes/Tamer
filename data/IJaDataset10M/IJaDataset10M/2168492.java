package freemarker.ide.eclipse.editors.preview;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class FreemarkerPreviewEditorInput implements IEditorInput {

    private IDocument freemarkerTemplateEditorDocument;

    public FreemarkerPreviewEditorInput(IDocument freemarkerTemplateEditorDocument) {
        this.freemarkerTemplateEditorDocument = freemarkerTemplateEditorDocument;
    }

    public boolean exists() {
        return false;
    }

    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    public String getName() {
        return null;
    }

    public IPersistableElement getPersistable() {
        return null;
    }

    public String getToolTipText() {
        return null;
    }

    public Object getAdapter(Class adapter) {
        return null;
    }

    public IDocument getFreemarkerTemplateEditorDocument() {
        return freemarkerTemplateEditorDocument;
    }
}
