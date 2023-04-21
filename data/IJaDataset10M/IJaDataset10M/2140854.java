package com.peterhi.client.ui.widgets.whiteboard;

import com.peterhi.client.ui.widgets.Whiteboard;
import com.peterhi.util.Editor;
import com.peterhi.util.Property;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.RGB;
import static com.peterhi.util.Property.*;
import static com.peterhi.util.Editor.*;

public class Pixels extends Shape {

    private short[] points;

    public Pixels(Whiteboard whiteboard) {
        super(whiteboard);
        ids.add(Points);
    }

    public Object get(Property id) {
        Object ret = super.get(id);
        if (ret == null) {
            switch(id) {
                case Points:
                    return points;
            }
        }
        return ret;
    }

    public Editor getEditorType(Property id) {
        Editor ret = super.getEditorType(id);
        if (ret == ReadOnly) {
            switch(id) {
                case Points:
                    return ReadOnly;
                default:
                    return ReadOnly;
            }
        }
        return ret;
    }

    public void set(Property id, Object value) throws Exception {
        switch(id) {
            case Points:
                points = (short[]) value;
                break;
            default:
                break;
        }
        super.set(id, value);
    }

    public short[] getPoints() {
        return points;
    }

    public void setPoints(short[] points) {
        this.points = points;
        update();
    }

    @Override
    public void drawShape(PaintEvent e) {
        RGB stroke = (RGB) get(Stroke);
        if (stroke != null) {
            for (int i = 0; i < points.length; i += 2) {
                e.gc.drawPoint(points[i], points[i + 1]);
            }
        }
    }

    @Override
    public String getImageResourceName() {
        return "pen.png";
    }
}
