package org.progeeks.meta.echo2.editor;

import org.progeeks.meta.echo2.DefaultEditorFactory;
import org.progeeks.meta.echo2.editor.SimpleDateTimeEditor;

public class DateTimeEditorFactory extends DefaultEditorFactory {

    public DateTimeEditorFactory() {
        super(SimpleDateTimeEditor.class);
    }
}
