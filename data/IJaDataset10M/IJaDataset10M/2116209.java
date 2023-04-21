package org.monet.backmobile.view.fields.widget;

import org.monet.backmobile.model.Field;
import org.monet.backmobile.view.fields.FieldWidget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class FieldMemoViewer extends RelativeLayout implements FieldWidget {

    protected int index;

    protected String portId;

    public FieldMemoViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FieldMemoViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FieldMemoViewer(Context context) {
        super(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (this.isInEditMode()) return;
    }

    public void setTarget(String portId, Field<?, ?> targetField, int index) {
        this.index = index;
        this.portId = portId;
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void refresh(int newIndex) {
    }
}
