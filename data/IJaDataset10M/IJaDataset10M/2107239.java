package com.bluebrim.layout.impl.client.command;

import com.bluebrim.layout.impl.shared.view.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.paint.shared.*;

/**
 */
public abstract class CoShapePageItemSetColorCommand extends CoShapePageItemSetObjectCommand {

    protected CoShapePageItemSetColorCommand(String name) {
        super(name);
    }

    public abstract CoColorIF getColor(CoShapePageItemView targetView);

    public Object getObject(CoShapePageItemView targetView) {
        return getColor(targetView);
    }

    public abstract void setColor(CoShapePageItemIF target, CoColorIF color);

    public void setObject(CoShapePageItemIF target, Object o) {
        setColor(target, (CoColorIF) o);
    }
}
