package org.isistan.flabot.edit.ucmeditor.figures;

import java.util.Hashtable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.isistan.flabot.edit.editor.figures.FixedConnectionAnchor;

/**
 * ThreeConnectionFigure
 * -	Used as a common class for fork and joins.
 * 
 * @author $Author: franco $
 *
 */
public abstract class ThreeConnectionFigure extends ResponsibilityNodeFigure {

    public static final String LEFT = "Left";

    public static final String RIGHT = "Right";

    public static final String UP = "Up";

    public static final String DOWN = "Down";

    public static final String TERMINAL_LEFT1 = "Terminal.Left1";

    public static final String TERMINAL_LEFT2 = "Terminal.Left2";

    public static final String TERMINAL_RIGHT = "Terminal.Right";

    public static final String TERMINAL = "Terminal";

    public static Hashtable anchors = new Hashtable();

    static {
        Figure f = new Figure();
        f.setSize(20, 40);
        Hashtable left = new Hashtable();
        FixedConnectionAnchor rightAnchor = new FixedConnectionAnchor(f);
        FixedConnectionAnchor left1Anchor = new FixedConnectionAnchor(f);
        FixedConnectionAnchor left2Anchor = new FixedConnectionAnchor(f);
        FixedConnectionAnchor anchor = new FixedConnectionAnchor(f);
        rightAnchor.setOffsetH(20);
        rightAnchor.setOffsetV(20);
        left1Anchor.setOffsetH(0);
        left1Anchor.setOffsetV(10);
        left2Anchor.setOffsetH(0);
        left2Anchor.setOffsetV(30);
        anchor.setOffsetH(10);
        anchor.setOffsetV(20);
        left.put(TERMINAL_RIGHT, rightAnchor);
        left.put(TERMINAL_LEFT1, left1Anchor);
        left.put(TERMINAL_LEFT2, left2Anchor);
        left.put(TERMINAL, anchor);
        anchors.put(LEFT, left);
        f = new Figure();
        f.setSize(20, 40);
        Hashtable right = new Hashtable();
        rightAnchor = new FixedConnectionAnchor(f);
        left1Anchor = new FixedConnectionAnchor(f);
        left2Anchor = new FixedConnectionAnchor(f);
        anchor = new FixedConnectionAnchor(f);
        rightAnchor.setOffsetH(0);
        rightAnchor.setOffsetV(20);
        left1Anchor.setOffsetH(20);
        left1Anchor.setOffsetV(10);
        left2Anchor.setOffsetH(20);
        left2Anchor.setOffsetV(30);
        anchor.setOffsetH(10);
        anchor.setOffsetV(20);
        right.put(TERMINAL_RIGHT, rightAnchor);
        right.put(TERMINAL_LEFT1, left1Anchor);
        right.put(TERMINAL_LEFT2, left2Anchor);
        right.put(TERMINAL, anchor);
        anchors.put("", right);
        anchors.put(RIGHT, right);
        f = new Figure();
        f.setSize(40, 20);
        Hashtable up = new Hashtable();
        rightAnchor = new FixedConnectionAnchor(f);
        left1Anchor = new FixedConnectionAnchor(f);
        left2Anchor = new FixedConnectionAnchor(f);
        anchor = new FixedConnectionAnchor(f);
        rightAnchor.setOffsetH(20);
        rightAnchor.setOffsetV(20);
        left1Anchor.setOffsetH(10);
        left1Anchor.setOffsetV(0);
        left2Anchor.setOffsetH(30);
        left2Anchor.setOffsetV(0);
        anchor.setOffsetH(20);
        anchor.setOffsetV(10);
        up.put(TERMINAL_RIGHT, rightAnchor);
        up.put(TERMINAL_LEFT1, left1Anchor);
        up.put(TERMINAL_LEFT2, left2Anchor);
        up.put(TERMINAL, anchor);
        anchors.put(UP, up);
        f = new Figure();
        f.setSize(40, 20);
        Hashtable down = new Hashtable();
        rightAnchor = new FixedConnectionAnchor(f);
        left1Anchor = new FixedConnectionAnchor(f);
        left2Anchor = new FixedConnectionAnchor(f);
        anchor = new FixedConnectionAnchor(f);
        rightAnchor.setOffsetH(20);
        rightAnchor.setOffsetV(0);
        left1Anchor.setOffsetH(10);
        left1Anchor.setOffsetV(20);
        left2Anchor.setOffsetH(30);
        left2Anchor.setOffsetV(20);
        anchor.setOffsetH(20);
        anchor.setOffsetV(10);
        down.put(TERMINAL_RIGHT, rightAnchor);
        down.put(TERMINAL_LEFT1, left1Anchor);
        down.put(TERMINAL_LEFT2, left2Anchor);
        down.put(TERMINAL, anchor);
        anchors.put(DOWN, down);
    }

    public static Dimension getPreferedSize(String rotation) {
        return ((FixedConnectionAnchor) ((Hashtable) anchors.get(rotation)).get(TERMINAL)).getOwner().getSize();
    }

    public static FixedConnectionAnchor getAnchor(String rotation, String name) {
        return (FixedConnectionAnchor) ((Hashtable) anchors.get(rotation)).get(name);
    }

    protected String rotation = RIGHT;

    /**
	 * Instantiates an instance of ThreeConnectionFigure
	 * 
	 * @param roration the figure rotation
	 * @param fC the foreground color of the figure
	 */
    public ThreeConnectionFigure(String rotation, RGB fc, IFigure parent) {
        super(parent, fc);
        setLayoutManager(new StackLayout());
        setRotation(rotation);
        setBackgroundColor(ColorConstants.black);
        setForegroundColor(new Color(Display.getCurrent(), fc));
    }

    /**
	 * Sets the rotation of the figure and updated the size
	 * 
	 * @param r the new rotation
	 */
    public void setRotation(String r) {
        if (r.equals("")) this.rotation = RIGHT; else this.rotation = r;
        if (rotation.equals(LEFT)) setSize(20, 40); else if (rotation.equals(RIGHT)) setSize(20, 40); else if (rotation.equals(UP)) setSize(40, 20); else if (rotation.equals(DOWN)) setSize(40, 20);
    }

    public Dimension getPreferredSize(int wHint, int hHint) {
        return getSize();
    }
}
