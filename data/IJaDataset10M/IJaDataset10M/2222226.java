package org.posper.graphics.editorkeys;

import org.posper.datautils.Formats;

public class JEditorDouble extends JEditorNumber<Double> {

    /** Creates a new instance of JEditorDouble */
    public JEditorDouble() {
    }

    protected Formats<Double> getFormat() {
        return Formats.DOUBLE;
    }

    protected int getMode() {
        return EditorKeys.MODE_DOUBLE;
    }
}
