package org.monet.backmobile.view.fields.widget;

import org.monet.backmobile.view.fields.FieldEditorWidget;
import android.content.Context;
import android.util.AttributeSet;

public class FieldBooleanEditor extends FieldBooleanViewer implements FieldEditorWidget {

    public FieldBooleanEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FieldBooleanEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldBooleanEditor(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (this.isInEditMode()) return;
    }

    public void clear() {
    }

    public void save() {
    }

    public void setNewValue(String imageId, String localId) {
    }
}
