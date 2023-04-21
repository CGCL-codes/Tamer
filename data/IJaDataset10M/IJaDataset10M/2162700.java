package org.jcryptool.editor.text.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.ui.IEditorPart;
import org.jcryptool.core.operations.editors.AbstractEditorService;
import org.jcryptool.editor.text.editor.JCTTextEditor;

/**
 * This class must be implemented to contribute the 
 * extension point <i>editorServices</i>. 
 * It implements the abstract method of the the 
 * class <i>org.jcryptool.core.operations.service.AbstractEditorService</i>
 * 
 * @author amro
 * @version 0.1
 */
public class JCTEditorService extends AbstractEditorService {

    public JCTEditorService() {
        super(JCTTextEditor.ID);
    }

    /**
	 * Getter of the active editorPart's content
	 * @param editorPart the active editorPart the content will be retrieved from
	 * @return the content of editorPart as a String object
	 */
    public String getContentOfEditorAsString(IEditorPart editorPart) {
        JCTTextEditor editor = (JCTTextEditor) editorPart;
        return editor.getDocument().get();
    }

    public byte[] getContentOfEditorAsBytes(IEditorPart editorPart) {
        JCTTextEditor editor = (JCTTextEditor) editorPart;
        return editor.getDocument().get().getBytes();
    }

    /**
	 * Sets a new content for active editor
	 * @param editorPart the active editor
	 * @param content the new content
	 */
    public void setContentOfEditor(IEditorPart editorPart, String content) {
    }

    public InputStream getContentOfEditorAsInputStream(IEditorPart editorPart) {
        JCTTextEditor editor = (JCTTextEditor) editorPart;
        return new BufferedInputStream(new ByteArrayInputStream(editor.getDocument().get().getBytes()));
    }
}
