package net.sourceforge.dejaviewer;

import java.io.Serializable;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class DocumentView extends View implements Serializable {

    private static final long serialVersionUID = 5199812304002823691L;

    public DocumentView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void paint(Canvas canvas, int width, int height) {
    }
}
