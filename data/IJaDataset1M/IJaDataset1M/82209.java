package org.npsnet.v.views.j3d;

import com.sun.j3d.audioengines.javasound.*;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.*;
import org.npsnet.v.kernel.Module;
import org.npsnet.v.kernel.ModuleContainer;
import org.npsnet.v.properties.model.EntityModel;
import org.npsnet.v.properties.model.Transformable;
import org.npsnet.v.properties.model.camera.Camera;
import org.npsnet.v.properties.view.AWTViewport;
import org.npsnet.v.properties.view.EntityView;
import org.npsnet.v.properties.view.Viewport3D;
import org.npsnet.v.services.controller.user.awt.AWTViewportEventPublisher;
import org.npsnet.v.services.controller.user.awt.AWTViewportKeyEvent;
import org.npsnet.v.services.controller.user.awt.AWTViewportMouseEvent;
import org.npsnet.v.services.controller.user.awt.AWTViewportMouseWheelEvent;
import org.npsnet.v.services.gui.ModuleContextMenuFactory;
import org.npsnet.v.services.gui.InvokerInfo;
import org.npsnet.v.services.gui.TransferableModule;
import org.npsnet.v.services.gui.TransferableResourceDescriptor;
import org.npsnet.v.services.resource.ModuleClassDescriptor;
import org.npsnet.v.services.resource.ResourceDescriptor;
import org.npsnet.v.services.time.TimeManager;

/**
 * A Java 3D viewport.
 *
 * @author Andrzej Kapolka
 */
public class J3DViewport implements AWTViewport, Viewport3D, Runnable {

    /**
     * The current camera.
     */
    protected Camera camera;

    /**
     * The canvas component.
     */
    protected Canvas3D canvasComponent;

    /**
     * The render thread.
     */
    private Thread renderThread;

    /**
     * The render flag.
     */
    protected boolean renderFlag;

    /**
     * The view object.
     */
    protected View view;

    /**
     * The viewing platform.
     */
    protected ViewingPlatform viewingPlatform;

    /**
     * The locale object.
     */
    private javax.media.j3d.Locale locale;

    /**
     * The pick canvas.
     */
    protected PickCanvas pickCanvas;

    /**
     * The owning <code>J3DViewCore</code>.
     */
    protected J3DViewCore owner;

    /**
     * The viewport's unique identifier.
     */
    private String id;

    /**
     * The last entity "touched," which is always in input focus.
     */
    private EntityModel lastEntityTouched;

    /**
     * Entities that have been explicitly added to the input focus.
     */
    private Vector focusedEntities;

    /**
     * A transform object to recycle.
     */
    protected Transform3D transform;

    /**
     * Creates a viewport without its own frame.
     *
     * @param pOwner the owning <code>J3DViewCore</code>
     * @param pLocale the <code>Locale</code> that contains the
     * scene graph to display
     * @param pID the viewport's unique identifier
     */
    public J3DViewport(J3DViewCore pOwner, javax.media.j3d.Locale pLocale, String pID) {
        owner = pOwner;
        locale = pLocale;
        id = pID;
        focusedEntities = new Vector();
        transform = new Transform3D();
        view = new View();
        view.setPhysicalBody(new PhysicalBody());
        PhysicalEnvironment pe = new PhysicalEnvironment();
        pe.setAudioDevice(new JavaSoundMixer(pe));
        view.setPhysicalEnvironment(pe);
        viewingPlatform = new ViewingPlatform();
        locale.addBranchGraph(viewingPlatform);
        view.attachViewPlatform(viewingPlatform.getViewPlatform());
        GraphicsConfigTemplate3D gct = new GraphicsConfigTemplate3D();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration cfg = ge.getDefaultScreenDevice().getBestConfiguration(gct);
        canvasComponent = createCanvasComponent(cfg);
        canvasComponent.setDropTarget(new DropTarget() {

            Transform3D tmpTrans = new Transform3D();

            public void drop(DropTargetDropEvent dtde) {
                ModuleContainer mc;
                Point3d pt = new Point3d();
                EntityView ev = getViewAt(dtde.getLocation().x, dtde.getLocation().y, pt);
                if (ev != null && ev.getTarget() instanceof ModuleContainer) {
                    mc = (ModuleContainer) ev.getTarget();
                } else {
                    mc = (ModuleContainer) owner.getTarget();
                }
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        dtde.acceptDrop(dtde.getDropAction());
                        java.util.List fl = (java.util.List) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        Iterator it = fl.iterator();
                        while (it.hasNext()) {
                            Module m = mc.createModule(((File) it.next()).toURL());
                            if (m instanceof Transformable) {
                                ((Transformable) m).setTransform(determineTransform(dtde.getLocation()));
                            }
                        }
                        dtde.dropComplete(true);
                    }
                    if (dtde.isDataFlavorSupported(TransferableModule.prototypeFlavor)) {
                        dtde.acceptDrop(dtde.getDropAction());
                        InputStream is = (InputStream) dtde.getTransferable().getTransferData(TransferableModule.prototypeFlavor);
                        Module m = mc.createModule(null, is);
                        if (m instanceof Transformable) {
                            ((Transformable) m).setTransform(determineTransform(dtde.getLocation()));
                        }
                        dtde.dropComplete(true);
                    } else if (dtde.isDataFlavorSupported(TransferableResourceDescriptor.descriptorFlavor)) {
                        dtde.acceptDrop(dtde.getDropAction());
                        ResourceDescriptor rd = (ResourceDescriptor) dtde.getTransferable().getTransferData(TransferableResourceDescriptor.descriptorFlavor);
                        if (rd instanceof ModuleClassDescriptor) {
                            Module m = mc.newModule((ModuleClassDescriptor) rd);
                            mc.registerModule(m);
                            if (m instanceof Transformable) {
                                ((Transformable) m).setTransform(determineTransform(dtde.getLocation()));
                            }
                            dtde.dropComplete(true);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(canvasComponent, e, "Error Transferring Module", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        canvasComponent.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent ke) {
                publishKeyEvent(ke);
            }

            public void keyReleased(KeyEvent ke) {
                publishKeyEvent(ke);
            }

            public void keyTyped(KeyEvent ke) {
                publishKeyEvent(ke);
            }
        });
        canvasComponent.addMouseListener(new MouseListener() {

            public void mouseEntered(MouseEvent me) {
                publishMouseEvent(me);
            }

            public void mouseExited(MouseEvent me) {
                publishMouseEvent(me);
            }

            public void mousePressed(MouseEvent me) {
                AWTViewportMouseEvent avme = new AWTViewportMouseEvent(me, J3DViewport.this);
                EntityView ev = avme.getEntityViewUnderCursor();
                if (ev != null) {
                    lastEntityTouched = (EntityModel) ev.getTarget();
                }
                AWTViewportEventPublisher avep = (AWTViewportEventPublisher) owner.getServiceProvider(AWTViewportEventPublisher.class);
                avep.publishAWTViewportMouseEvent(avme);
            }

            public void mouseReleased(MouseEvent me) {
                publishMouseEvent(me);
            }

            public void mouseClicked(MouseEvent me) {
                AWTViewportMouseEvent avme = new AWTViewportMouseEvent(me, J3DViewport.this);
                EntityView ev = avme.getEntityViewUnderCursor();
                ModuleContextMenuFactory mcmf = (ModuleContextMenuFactory) owner.getServiceProvider(ModuleContextMenuFactory.class);
                if (me.getButton() == MouseEvent.BUTTON1 && avme.getClickCount() == 2) {
                    mcmf.selectDefaultContextMenuItem(ev.getTarget(), new InvokerInfo(avme.getSourceViewport().getCanvasComponent(), avme.getSourceViewport(), avme.getPoint()));
                } else if (me.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu contextMenu = mcmf.newContextMenu(ev.getTarget(), new InvokerInfo(avme.getSourceViewport().getCanvasComponent(), avme.getSourceViewport(), avme.getPoint()));
                    contextMenu.show(avme.getSourceViewport().getCanvasComponent(), avme.getX(), avme.getY());
                } else {
                    AWTViewportEventPublisher avep = (AWTViewportEventPublisher) owner.getServiceProvider(AWTViewportEventPublisher.class);
                    avep.publishAWTViewportMouseEvent(avme);
                }
            }
        });
        canvasComponent.addMouseMotionListener(new MouseMotionListener() {

            public void mouseMoved(MouseEvent me) {
                publishMouseEvent(me);
            }

            public void mouseDragged(MouseEvent me) {
                publishMouseEvent(me);
            }
        });
        canvasComponent.addMouseWheelListener(new MouseWheelListener() {

            public void mouseWheelMoved(MouseWheelEvent mwe) {
                publishMouseWheelEvent(mwe);
            }
        });
        pickCanvas = new PickCanvas(canvasComponent, locale);
        pickCanvas.setMode(PickCanvas.GEOMETRY);
        view.stopView();
        view.addCanvas3D(canvasComponent);
    }

    /**
     * Determines the transform that corresponds to the specified
     * window location.
     *
     * @param location the window location
     * @return the corresponding transform
     */
    private Transform3D determineTransform(Point location) {
        Transform3D tmpTrans = new Transform3D();
        camera.getTransform(tmpTrans, true);
        double distanceScale = (40.0 * Math.tan(camera.getHorizontalFOV() / 2.0)) / canvasComponent.getWidth();
        Point3d pt = new Point3d((location.x - canvasComponent.getWidth() / 2) * distanceScale, (canvasComponent.getHeight() / 2 - location.y) * distanceScale, -20.0);
        tmpTrans.transform(pt);
        tmpTrans.setTranslation(new Vector3d(pt));
        return tmpTrans;
    }

    /**
     * Publishes a key event, sending it to all interested parties.
     *
     * @param ke the key event to publish
     */
    private void publishKeyEvent(KeyEvent ke) {
        AWTViewportEventPublisher avep = (AWTViewportEventPublisher) owner.getServiceProvider(AWTViewportEventPublisher.class);
        avep.publishAWTViewportKeyEvent(new AWTViewportKeyEvent(ke, this));
    }

    /**
     * Publishes a mouse event, sending it to all interested parties.
     *
     * @param me the mouse event to publish
     */
    private void publishMouseEvent(MouseEvent me) {
        AWTViewportEventPublisher avep = (AWTViewportEventPublisher) owner.getServiceProvider(AWTViewportEventPublisher.class);
        avep.publishAWTViewportMouseEvent(new AWTViewportMouseEvent(me, this));
    }

    /**
     * Publishes a mouse wheel event, sending it to all interested parties.
     *
     * @param me the mouse wheel event to publish
     */
    private void publishMouseWheelEvent(MouseWheelEvent mwe) {
        AWTViewportEventPublisher avep = (AWTViewportEventPublisher) owner.getServiceProvider(AWTViewportEventPublisher.class);
        avep.publishAWTViewportMouseWheelEvent(new AWTViewportMouseWheelEvent(mwe, this));
    }

    /**
     * Creates and returns a <code>Canvas3D</code> appropriate for this
     * viewport.
     *
     * @param cfg the <code>GraphicsConfiguration</code> to use
     */
    protected Canvas3D createCanvasComponent(GraphicsConfiguration cfg) {
        return new Canvas3D(cfg) {

            public Dimension getMinimumSize() {
                return new Dimension(0, 0);
            }
        };
    }

    /**
     * Returns the unique identifier of this viewport.
     *
     * @return the unique identifier of this viewport
     */
    public String getID() {
        return id;
    }

    /**
     * The render thread.
     */
    public void run() {
        while (renderFlag) {
            TimeManager tm = (TimeManager) owner.getServiceProvider(TimeManager.class);
            if (tm != null) tm.advanceTime();
            if (camera != null) {
                camera.getTransform(transform, true);
                viewingPlatform.getViewPlatformTransform().setTransform(transform);
                view.setFieldOfView(camera.getHorizontalFOV());
                view.setFrontClipDistance(camera.getNearClipDistance());
                view.setBackClipDistance(camera.getFarClipDistance());
                if (camera.getProjectionType() == Camera.PERSPECTIVE) view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION); else view.setProjectionPolicy(View.PARALLEL_PROJECTION);
            }
            owner.notifyPreRenderListeners(this);
            if (!view.isViewRunning()) view.renderOnce();
            if (tm != null) tm.releaseTimeLock();
            Thread.yield();
        }
    }

    /**
     * Starts the renderer.
     */
    public void startRenderer() {
        if (!renderFlag) {
            renderFlag = true;
            renderThread = new Thread(this);
            renderThread.start();
        }
    }

    /**
     * Stops the renderer.
     */
    public void stopRenderer() {
        renderFlag = false;
    }

    /**
     * Returns this viewport's canvas component (the
     * component that it renders to).
     *
     * @return this viewport's canvas component
     */
    public Component getCanvasComponent() {
        return canvasComponent;
    }

    /**
     * Closes this viewport.
     */
    public void close() {
        stopRenderer();
        owner.viewportClosed(this);
    }

    /**
     * Returns the <code>EntityView</code> located at
     * the specified window position and computes the
     * entity intersection point, if applicable.
     *
     * @param x the x coordinate of the window position
     * @param y the y coordinate of the window position
     * @param ipResult a <code>Point3d</code> to hold the intersection
     * point (in view coordinates), if applicable; if not applicable,
     * this object will not be modified 
     * @return the <code>EntityView</code> located at the
     * specified window position
     */
    public EntityView getViewAt(int x, int y, Point3d ipResult) {
        pickCanvas.setShapeLocation(x, y);
        PickResult pr = pickCanvas.pickClosest();
        if (pr == null) {
            return owner;
        } else {
            PickIntersection pi = pr.getClosestIntersection(pickCanvas.getStartPosition());
            if (pi == null) {
                return owner;
            }
            ipResult.set(pi.getPointCoordinatesVW());
            if (camera != null) {
                camera.getTransform(transform, true);
                transform.invert();
                transform.transform(ipResult);
            }
            return owner.getViewFromPath(pr.getSceneGraphPath());
        }
    }

    /**
     * Adds the specified entity to the input focus.
     *
     * @param em the entity model to add
     */
    public void addToFocus(EntityModel em) {
        focusedEntities.add(em);
    }

    /**
     * Removes the specified entity from the input focus.
     *
     * @param em the entity model to remove
     */
    public void removeFromFocus(EntityModel em) {
        focusedEntities.remove(em);
    }

    /**
     * Checks whether the specified entity is in focus (that is,
     * is capable of receiving input events).  Entities in focus typically
     * include selected entities, the last entity to have been clicked upon,
     * cameras or viewing windows associated with this viewport, and any
     * entities that have been explicitly focused.
     *
     * @param em the entity model to check
     * @return <code>true</code> if the entity is in focus, <code>false</code>
     * otherwise
     */
    public boolean isInFocus(EntityModel em) {
        if (em == lastEntityTouched || focusedEntities.contains(em) || camera != null && camera.isInFocus(em)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets this viewport's camera.
     *
     * @param pCamera the new camera
     */
    public void setCamera(Camera pCamera) {
        camera = pCamera;
    }

    /**
     * Returns this viewport's camera.
     *
     * @return the current camera
     */
    public Camera getCamera() {
        return camera;
    }
}
