package com.jpatch.boundary.tools;

import static javax.media.opengl.GL.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.media.opengl.*;
import javax.swing.*;
import javax.vecmath.*;
import trashcan.*;
import com.jpatch.afw.attributes.*;
import com.jpatch.afw.control.*;
import com.jpatch.afw.vecmath.*;
import com.jpatch.boundary.*;
import com.jpatch.boundary.tools.MouseSelector.*;
import com.jpatch.entity.*;
import com.jpatch.entity.sds2.*;

public class ExtrudeTool_old implements VisibleTool {

    private TransformUtil transformUtil = new TransformUtil();

    private MouseMotionListener[] mouseMotionListeners;

    private MouseListener[] mouseListeners;

    private HitObject hitObject;

    private Selection hitSelection = new Selection();

    private boolean drag;

    private Point hitPoint;

    private Point3d localStart = new Point3d();

    private TextureUpdater textureUpdater;

    private BooleanAttr keepConnectedAttr = new BooleanAttr(true);

    private BooleanAttr constrainToNormal = new BooleanAttr(true);

    public BooleanAttr getKeepConnectedAttribute() {
        return keepConnectedAttr;
    }

    public BooleanAttr getConstrainToNormalAttribute() {
        return constrainToNormal;
    }

    public void draw(Viewport viewport) {
    }

    public void registerListeners(Viewport[] viewports) {
        Main.getInstance().getSelection().clear(null);
        Main.getInstance().repaintViewports();
        mouseListeners = new MouseListener[viewports.length];
        mouseMotionListeners = new MouseMotionListener[viewports.length];
        for (int i = 0; i < viewports.length; i++) {
            mouseListeners[i] = new ExtrudeMouseListener((ViewportGl) viewports[i]);
            viewports[i].getComponent().addMouseListener(mouseListeners[i]);
            mouseMotionListeners[i] = new ExtrudeMouseMotionListener((ViewportGl) viewports[i]);
            viewports[i].getComponent().addMouseMotionListener(mouseMotionListeners[i]);
        }
        textureUpdater = new TextureUpdater(viewports);
        textureUpdater.start();
    }

    public void unregisterListeners(Viewport[] viewports) {
        for (int i = 0; i < viewports.length; i++) {
            viewports[i].getComponent().removeMouseListener(mouseListeners[i]);
            viewports[i].getComponent().removeMouseMotionListener(mouseMotionListeners[i]);
        }
        textureUpdater.stop();
    }

    private void highlightHitObject(ViewportGl viewport) {
        GLAutoDrawable glDrawable = (GLAutoDrawable) viewport.getComponent();
        glDrawable.getContext().makeCurrent();
        GL gl = glDrawable.getGL();
        viewport.validateScreenShotTexture();
        viewport.drawScreenShot(0, 0, glDrawable.getWidth(), glDrawable.getHeight(), 1.0f);
        viewport.spatialMode();
        viewport.getViewDef().configureTransformUtil(transformUtil);
        hitObject.node.getLocal2WorldTransform(transformUtil, TransformUtil.LOCAL);
        viewport.setModelViewMatrix(transformUtil);
        viewport.drawSelection(hitSelection, new Color3f(1, 1, 0));
        glDrawable.swapBuffers();
        glDrawable.getContext().release();
    }

    private class ExtrudeMouseListener extends MouseAdapter {

        final ViewportGl viewport;

        private ExtrudeMouseListener(ViewportGl viewport) {
            this.viewport = viewport;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (hitObject == null) {
                return;
            }
            if (e.getButton() == MouseEvent.BUTTON1) {
                snapPointer(viewport);
                Selection selection = Main.getInstance().getSelection();
                Sds sds = selection.getSdsModel().getSds();
                selection.set(hitSelection);
                List<JPatchUndoableEdit> editList = new ArrayList<JPatchUndoableEdit>();
                Operations.extrude(sds, selection, editList);
                Main.getInstance().getUndoManager().addEdit("extrude", editList);
                selection.getTransformable().begin();
                drag = true;
                localStart.set(hitObject.screenPosition);
                transformUtil.projectFromScreen(TransformUtil.LOCAL, localStart, localStart);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                Selection selection = Main.getInstance().getSelection();
                selection.getTransformable().end(new ArrayList<JPatchUndoableEdit>());
                Main.getInstance().getSelection().clear(null);
                Main.getInstance().repaintViewports();
                drag = false;
            }
        }
    }

    private void updateSelection(Selection selection, HitObject hitObject) {
        if (hitObject instanceof MouseSelector.HitEdge) {
            selection.setEdge(((HitEdge) hitObject).halfEdge, null);
        } else if (hitObject instanceof MouseSelector.HitFace) {
            selection.setFace(((HitFace) hitObject).face, null);
        }
    }

    private void addToSelection(Selection selection, HitObject hitObject) {
        if (hitObject instanceof MouseSelector.HitEdge) {
            selection.addEdge(((HitEdge) hitObject).halfEdge, null);
        } else if (hitObject instanceof MouseSelector.HitFace) {
            selection.addFace(((HitFace) hitObject).face, null);
        }
    }

    private class ExtrudeMouseMotionListener implements MouseMotionListener {

        final ViewportGl viewport;

        Point3d mouse = new Point3d();

        Vector3d vector = new Vector3d();

        private ExtrudeMouseMotionListener(ViewportGl viewport) {
            this.viewport = viewport;
        }

        public void mouseDragged(MouseEvent e) {
            if (drag) {
                if (hitPoint != null && hitPoint.x == e.getX() && hitPoint.y == e.getY()) {
                    hitPoint = null;
                    return;
                }
                mouse.set(e.getX(), e.getY(), hitObject.screenPosition.z);
                transformUtil.projectFromScreen(TransformUtil.LOCAL, mouse, mouse);
                vector.sub(mouse, localStart);
                Selection selection = Main.getInstance().getSelection();
                selection.getTransformable().translate(vector);
                Main.getInstance().syncRepaintViewport(viewport);
            }
        }

        public void mouseMoved(MouseEvent e) {
            SdsModel sdsModel = Main.getInstance().getSelection().getSdsModel();
            if (sdsModel != null) {
                int level = sdsModel.getEditLevelAttribute().getInt();
                int selectionType = Sds.Type.FACE;
                HitObject newHitObject = MouseSelector.getObjectAt(viewport, e.getX(), e.getY(), Double.MAX_VALUE, sdsModel, level, selectionType, null);
                if (newHitObject != null && !newHitObject.equals(hitObject)) {
                    hitObject = newHitObject;
                    if (e.isShiftDown()) {
                        addToSelection(hitSelection, hitObject);
                    } else {
                        updateSelection(hitSelection, hitObject);
                    }
                    highlightHitObject(viewport);
                }
            }
        }
    }

    ;

    private void snapPointer(Viewport viewport) {
        Point point = new Point((int) Math.round(hitObject.screenPosition.x), (int) Math.round(hitObject.screenPosition.y));
        hitPoint = new Point(point);
        SwingUtilities.convertPointToScreen(point, viewport.getComponent());
        Main.getInstance().getRobot().mouseMove(point.x, point.y);
    }
}
