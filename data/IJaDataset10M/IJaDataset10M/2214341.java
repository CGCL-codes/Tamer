package com.ryanm.trace;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import com.rugl.geom.ColouredShape;
import com.rugl.geom.Shape;
import com.rugl.geom.ShapeBuilder;
import com.rugl.geom.ShapeUtil;
import com.rugl.geom.TexturedShape;
import com.rugl.input.KeyPress;
import com.rugl.renderer.StackedRenderer;
import com.rugl.renderer.proc.Alpha;
import com.rugl.util.Colour;
import com.rugl.util.Tesselator;
import com.ryanm.config.imp.ConfigurableType;
import com.ryanm.config.imp.Variable;
import com.ryanm.util.geom.BoundingCuboid;
import com.ryanm.util.geom.MatrixUtils;
import com.ryanm.util.math.Range;
import com.ryanm.util.math.Trig;

/**
 * Handles drawing the
 * 
 * @author ryanm
 */
@ConfigurableType("Buttons")
public class Buttons {

    private static KeyPress enter = new KeyPress(true, Keyboard.KEY_RETURN);

    private static KeyPress escape = new KeyPress(true, Keyboard.KEY_ESCAPE);

    private static KeyPress left = new KeyPress(true, Keyboard.KEY_LEFT);

    private static KeyPress right = new KeyPress(true, Keyboard.KEY_RIGHT);

    private static KeyPress up = new KeyPress(true, Keyboard.KEY_UP);

    private static KeyPress down = new KeyPress(true, Keyboard.KEY_DOWN);

    @Variable
    private static Vector3f retArrowPos = new Vector3f(700, 565, 1);

    @Variable
    private static Vector3f retArrowSize = new Vector3f(50, 15, 1);

    @Variable
    private static Vector3f retArrowShape = new Vector3f(10, 5, 1);

    @Variable
    private static Vector3f retPos = new Vector3f(687.5f, 447.5f, 1);

    @Variable
    private static Vector3f retSize = new Vector3f(100, 140, 1);

    @Variable
    private static Vector3f retShape = new Vector3f(20, 80, 1);

    @Variable
    private static Vector3f arrowSize = new Vector3f(20, 40, 1);

    @Variable
    private static float arrowheight = 20;

    @Variable
    private static Vector3f arrowShape = new Vector3f(0.25f, 0.5f, 1);

    @Variable
    private static Vector3f escPos = new Vector3f(17.5f, 567.5f, 1);

    @Variable
    private static float escSize = 20;

    @Variable
    private static Vector3f escStart = new Vector3f(10, 510, 1);

    @Variable
    private static float thickness = 5;

    @Variable
    private static float size = 80;

    @Variable
    private static float cursorSep = 10;

    private static TexturedShape escText;

    /***/
    @Variable
    public static float alphaLoss = 4f;

    /***/
    @Variable
    public static float disabledAlpha = 0.05f;

    /***/
    @Variable
    public static float inactiveAlpha = 0.5f;

    /***/
    @Variable
    public static float mouseOverAlpha = 0.8f;

    private static int[] keyCodes = new int[] { Keyboard.KEY_ESCAPE, Keyboard.KEY_RETURN, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT };

    /**
	 * esc, enter, up, down, left, right
	 */
    private static ColouredShape[] buttons;

    private static Shape[] mouseShapes;

    private static String[] texts = new String[6];

    private static TexturedShape[] textShapes = new TexturedShape[] { null, null, null, null, null, null };

    private static TexturedShape title;

    /**
	 * Whether the buttons are visible or not
	 */
    public static boolean visible = true;

    private static Alpha dimmer = new Alpha(0);

    private static MouseWarn mouseWarn = new MouseWarn();

    /**
	 * Builds the button shapes etc
	 */
    public static void init() {
        if (buttons == null) {
            mouseShapes = new Shape[6];
            escText = TraceGame.font.buildTextShape("Esc", Colour.white);
            float s = escSize / 72;
            escText.scale(s, s, s);
            escText.translate(escPos.x, escPos.y, escPos.z);
            ColouredShape escButton = new ColouredShape(ShapeUtil.innerQuad(escStart.x, escStart.y, escStart.x + size, escStart.y + size, thickness, escPos.z), Colour.white, null);
            mouseShapes[0] = ShapeUtil.filledQuad(escStart.x, escStart.y, escStart.x + size, escStart.y + size, 0);
            Shape arrow = ShapeUtil.arrow(0, 0, arrowSize.x, arrowSize.y, arrowShape.x, arrowShape.x);
            arrow.translate(-arrowSize.x / 2, -arrowheight - arrowSize.y, 0);
            arrow.translate(size / 2, size, 0);
            Shape box = ShapeUtil.innerQuad(0, 0, size, size, thickness, 0);
            box = ShapeBuilder.fuse(arrow, box);
            for (int i = 2; i < mouseShapes.length; i++) {
                mouseShapes[i] = ShapeUtil.filledQuad(0, 0, size, size, 0);
            }
            Vector2f v = new Vector2f(800 - 10 - size - cursorSep - size - cursorSep - size, 10 + cursorSep + size);
            ColouredShape lb = new ColouredShape(box.clone().transform(MatrixUtils.rotateAround(Trig.PI / 2, size / 2, size / 2)).translate(v.x, v.y, 1), Colour.white, null);
            mouseShapes[4].transform(MatrixUtils.rotateAround(Trig.PI / 2, size / 2, size / 2)).translate(v.x, v.y, 1);
            v.x += 2 * (size + cursorSep);
            ColouredShape rb = new ColouredShape(box.clone().transform(MatrixUtils.rotateAround(-Trig.PI / 2, size / 2, size / 2)).translate(v.x, v.y, 1), Colour.white, null);
            mouseShapes[5].transform(MatrixUtils.rotateAround(-Trig.PI / 2, size / 2, size / 2)).translate(v.x, v.y, 1);
            v.x -= 1 * (size + cursorSep);
            v.y -= 1 * (size + cursorSep);
            ColouredShape db = new ColouredShape(box.clone().transform(MatrixUtils.rotateAround(Trig.PI, size / 2, size / 2)).translate(v.x, v.y, 1), Colour.white, null);
            mouseShapes[3].transform(MatrixUtils.rotateAround(Trig.PI, size / 2, size / 2)).translate(v.x, v.y, 1);
            v.y += 2 * (size + cursorSep);
            ColouredShape ub = new ColouredShape(box.clone().translate(v.x, v.y, 1), Colour.white, null);
            mouseShapes[2].translate(v.x, v.y, 1);
            ColouredShape ret = new ColouredShape(retShape(), Colour.white, null);
            mouseShapes[1] = retOutline();
            buttons = new ColouredShape[6];
            buttons[0] = escButton;
            buttons[1] = ret;
            buttons[2] = ub;
            buttons[3] = db;
            buttons[4] = lb;
            buttons[5] = rb;
            title = TraceGame.font.buildTextShape(">TRACE", Colour.white);
            title.scale(2, 2, 1);
            title.translate(400 - title.getBounds().getWidth() / 2, 500, 1);
        }
    }

    /**
	 * @param delta
	 */
    public static void advance(float delta) {
        if (buttons != null) {
            for (int i = 0; i < keyCodes.length; i++) {
                float alpha = (float) Colour.alphai(buttons[i].colours[0]) / 255;
                alpha -= delta * alphaLoss;
                alpha = Math.max(alpha, textShapes[i] == null ? disabledAlpha : inactiveAlpha);
                if (Keyboard.isKeyDown(keyCodes[i]) && textShapes[i] != null) {
                    alpha = 1;
                }
                Colour.withAlphai(buttons[i].colours, (int) (alpha * 255));
            }
            dimmer.mult += (visible ? 1 : -1) * delta * alphaLoss;
            dimmer.mult = Range.limit(dimmer.mult, 0, 1);
            mouseWarn.advance(delta);
        }
    }

    /**
	 * Draw the buttons
	 * 
	 * @param r
	 */
    public static void draw(StackedRenderer r) {
        if (dimmer.mult > 0) {
            r.push(dimmer);
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].render(r);
            }
            escText.render(r);
            for (int i = 0; i < textShapes.length; i++) {
                if (textShapes[i] != null) {
                    textShapes[i].render(r);
                }
            }
            title.render(r);
            mouseWarn.draw(r);
            r.popProcessor();
        }
    }

    private static Shape retShape() {
        Shape outline = ShapeUtil.outline(thickness, 1, retPos.x + retShape.x, retPos.y, retPos.x + retShape.x, retPos.y + retShape.y, retPos.x, retPos.y + retShape.y, retPos.x, retPos.y + retSize.y, retPos.x + retSize.x, retPos.y + retSize.y, retPos.x + retSize.x, retPos.y);
        Shape arrow = ShapeUtil.retArrow(retArrowPos.x, retArrowPos.y, retArrowSize.x, retArrowSize.y, retArrowShape.x, retArrowShape.y);
        Shape s = ShapeBuilder.fuse(outline, arrow);
        s.translate(0, 0, 1);
        return s;
    }

    private static Shape retOutline() {
        Shape s = Tesselator.tesselate(new float[] { retPos.x + retShape.x, retPos.y, retPos.x + retShape.x, retPos.y + retShape.y, retPos.x, retPos.y + retShape.y, retPos.x, retPos.y + retSize.y, retPos.x + retSize.x, retPos.y + retSize.y, retPos.x + retSize.x, retPos.y }, 1);
        return s;
    }

    /**
	 * Sets the button function text
	 * 
	 * @param escape
	 * @param enter
	 * @param up
	 * @param down
	 * @param left
	 * @param right
	 */
    public static void setText(String escape, String enter, String up, String down, String left, String right) {
        String[] t = new String[] { escape, enter, up, down, left, right };
        for (int i = 0; i < texts.length; i++) {
            boolean changed = texts[i] == null != (t[i] == null);
            if (texts[i] != null) {
                changed |= !texts[i].equals(t[i]);
            }
            if (t[i] != null) {
                changed |= !t[i].equals(texts[i]);
            }
            if (changed) {
                texts[i] = t[i];
                textShapes[i] = null;
                if (texts[i] != null) {
                    textShapes[i] = TraceGame.font.buildTextShape(texts[i], Colour.white);
                    textShapes[i].scale(0.5f, 0.5f, 1);
                    Vector3f c = textShapes[i].getBounds().getCenter();
                    Vector3f bc = buttons[i].getBounds().getCenter();
                    textShapes[i].translate(bc.x - c.x, bc.y - c.y, bc.z - c.z);
                    if (i == 1) {
                        c = textShapes[1].getBounds().getCenter();
                        bc = buttons[0].getBounds().getCenter();
                        textShapes[1].translate(0, bc.y - c.y, 0);
                    }
                    bounds(textShapes[i]);
                }
            }
        }
    }

    /**
	 * Limits a shape to lie within 0,0 - 800,600
	 * 
	 * @param s
	 */
    public static void bounds(Shape s) {
        BoundingCuboid c = s.getBounds();
        if (c.x.getMin() < 10) {
            s.translate(10 - c.x.getMin(), 0, 0);
        }
        if (c.x.getMax() > 790) {
            s.translate(790 - c.x.getMax(), 0, 0);
        }
        if (c.y.getMin() < 10) {
            s.translate(0, 10 - c.y.getMin(), 0);
        }
        if (c.y.getMax() > 590) {
            s.translate(0, 590 - c.y.getMax(), 0);
        }
    }

    /**
	 * @return <code>true</code> if escape has been hit
	 */
    public static boolean esc() {
        return escape.isActive();
    }

    /**
	 * @return <code>true</code> if enter has been hit
	 */
    public static boolean enter() {
        return enter.isActive();
    }

    /**
	 * @return <code>true</code> if up has been hit
	 */
    public static boolean up() {
        return up.isActive();
    }

    /**
	 * @return <code>true</code> if down has been hit
	 */
    public static boolean down() {
        return down.isActive();
    }

    /**
	 * @return <code>true</code> if left has been hit
	 */
    public static boolean left() {
        return left.isActive();
    }

    /**
	 * @return <code>true</code> if right has been hit
	 */
    public static boolean right() {
        return right.isActive();
    }
}
