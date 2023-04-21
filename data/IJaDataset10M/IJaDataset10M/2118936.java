package net.sf.redsetter.action;

import net.sf.redsetter.editor.MappingEditor;
import net.sf.redsetter.gen.TemplateHandler;
import net.sf.redsetter.model.Mapping;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

/**
 * Generates the Dozer mapping
 */
public class GenerateAction extends Action {

    IEditorPart editor;

    public GenerateAction(IEditorPart editor) {
        super("Generate File", Action.AS_PUSH_BUTTON);
        this.editor = editor;
    }

    public void run() {
        if (editor instanceof MappingEditor) {
            MappingEditor mappingEditor = (MappingEditor) editor;
            Mapping mapping = mappingEditor.getMapping();
            TemplateHandler handler = new TemplateHandler(mapping);
            String filename = ((IFileEditorInput) mappingEditor.getEditorInput()).getFile().getProject().getLocation().toOSString();
            filename += "\\testDozer.xml";
            handler.createFile(filename);
        }
    }

    public void setActiveEditor(IEditorPart editor) {
        this.editor = editor;
    }
}
