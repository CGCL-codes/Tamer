package com.narunas.shapes;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ContainerView extends ViewGroup {

    private Paint containerBox;

    private ChildShapeOval oval;

    private ArrayList children;

    private Paint lBox;

    private int mSize;

    private Canvas stage;

    private RectF boxSize;

    private Context con;

    public ContainerView(Context context) {
        super(context);
    }

    public ContainerView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    protected final void buildContainerShape() {
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }
}
