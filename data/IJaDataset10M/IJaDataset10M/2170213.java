package jmax.editors.patcher.objects;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import jmax.commons.*;
import jmax.fts.*;
import jmax.utils.*;
import jmax.toolkit.*;
import jmax.editors.patcher.*;

public class Comment extends MultiLineEditable {

    FtsProperty commentProperty;

    public Comment(ErmesSketchPad theSketchPad, FtsObject theFtsObject) {
        super(theSketchPad, theFtsObject);
    }

    void setFtsObject(FtsObject theFtsObject) {
        commentProperty = theFtsObject.getStringProperty(FtsProperty.OBJECT_COMMENT);
        super.setFtsObject(theFtsObject);
    }

    public String getArgs() {
        return commentProperty.getStringValue();
    }

    public void redefine(String text) throws MaxError {
        commentProperty.setStringValue(text, true);
        super.redefine(text);
    }

    protected void computeRenderer() {
        String args = getArgs();
        Icon icon = null;
        try {
            if ((args != null) && (args.length() > 0) && args.startsWith("jmax://")) icon = Icons.get(args);
        } catch (MaxError e) {
            e.notifyError();
        }
        if (icon != null) {
            renderer = new IconRenderer(this, icon);
        } else if (!(renderer instanceof MultilineTextRenderer)) {
            renderer = new MultilineTextRenderer(this);
        }
    }

    private static final int TEXT_BOTTOM_OFFSET = 2;

    private static final int TEXT_LEFT_OFFSET = 4;

    public int getTextBottomOffset() {
        return TEXT_BOTTOM_OFFSET;
    }

    public int getTextLeftOffset() {
        return TEXT_LEFT_OFFSET;
    }

    public Color getTextBackground() {
        if (itsSketchPad.isLocked()) return Color.white; else {
            if (isSelected()) return Color.gray; else return itsSketchPad.getBackground();
        }
    }

    public void paint(Graphics2D g) {
        if (!itsSketchPad.isLocked()) {
            if (isSelected()) g.setColor(Color.gray); else g.setColor(itsSketchPad.getBackground());
            g.fill3DRect(getX(), getY(), getWidth(), getHeight(), true);
        }
        drawContent(g, itsSketchPad.isLocked());
    }

    public Dimension getMinimumSize() {
        return null;
    }
}
