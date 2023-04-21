package org.xith3d.test.util;

import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformNode;
import net.jtank.input.MouseDevice;
import net.jtank.input.MouseListener;

/**
 * Handles Mouse rotation and applies it to a TransformNode (View)
 * 
 * @author Jens  Lehmann
 * @author Abdul Bezrati
 * @author William Denniss
 */
public class MouseRotation {

    public static MouseRotation mouseRotations = new MouseRotation();

    private static TransformNode transformGroup = null;

    private static Transform3D transform3D = null;

    private static boolean isRotationScheduled = false;

    private static float rotX = 0, rotY = 0, rotXTmp = 0, rotYTmp = 0;

    private static int startDragX = 0, startDragY = 0;

    public MouseRotation() {
    }

    public static void setRotationNode(TransformNode transformGroup, MouseDevice mouse) {
        mouseRotations.setTransformNode(transformGroup, mouse);
    }

    public void setTransformNode(TransformNode trans, MouseDevice mouse) {
        transformGroup = trans;
        try {
            transformGroup.getTransform(transform3D);
        } catch (NullPointerException e) {
            transform3D = new Transform3D();
        }
        float[] elements = new float[16];
        transform3D.get(elements);
        rotX = rotXTmp = (float) Math.asin(elements[2]);
        rotY = rotYTmp = (float) Math.asin(elements[9]);
        mouse.registerListener(new Mouse());
    }

    /**
     * Invoked when a mouse button is pressed. This is triggered before
     * any mouse dragged event (if it occurs). We use this to save the
     * start position for dragging.
     *
     * @param e The triggered mouse event.
     */
    private static void mousePressed(int x, int y) {
        startDragX = x;
        startDragY = y;
    }

    /**
     * Invoked when a mouse button is released. We write back the
     * temporary rotation and translation values here.
     *
     * @param e The triggered mouse event.
     */
    private static void mouseReleased(int x, int y) {
        rotX = x;
        rotY = y;
    }

    /**
     * Called when the left mouse button is pressed during dragging.
     * Moving the mouse left/right rotates the cube around the x-axis.
     * Moving up/down rotates it around the y-axis.
     */
    private static void leftDrag(int x, int y) {
        rotXTmp = rotX + (x - startDragX) / 100f;
        rotYTmp = rotY + (y - startDragY) / 100f;
        isRotationScheduled = true;
    }

    private static void performRotation() {
        transform3D.rotXYZ(rotYTmp, rotXTmp, 0);
        transformGroup.setTransform(transform3D);
    }

    public static void checkRotationFlag() {
        if (isRotationScheduled) {
            performRotation();
            isRotationScheduled = false;
        }
    }

    private class Mouse implements MouseListener {

        boolean pressed = false;

        public void onMouseButtonPressed(int button, int x, int y) {
            mousePressed(x, y);
            pressed = true;
        }

        public void onMouseButtonReleased(int button, int x, int y) {
            mouseReleased(x, y);
            pressed = false;
        }

        public void onMouseMoved(int x, int y) {
            if (pressed) {
                leftDrag(x, y);
            }
        }

        public void onMouseMovedDelta(int dX, int dY) {
        }

        public void onMouseWheelMoved(int delta, int x, int y) {
        }

        public void onMouseStopped(int x, int y) {
        }
    }
}
