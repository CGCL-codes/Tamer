package edu.ucsd.ncmir.jibber.ui;

import com.sun.pdfview.PDFXformCmd;
import com.sun.pdfview.display.PDFPagePanel;
import edu.ucsd.ncmir.asynchronous_event.AbstractAsynchronousEventListener;
import edu.ucsd.ncmir.asynchronous_event.AsynchronousEvent;
import edu.ucsd.ncmir.asynchronous_event.ListenerManager;
import edu.ucsd.ncmir.jibber.change.ChangeOperation;
import edu.ucsd.ncmir.jibber.core.ImageCorrespondence;
import edu.ucsd.ncmir.jibber.core.JibberImage;
import edu.ucsd.ncmir.jibber.core.Mode;
import edu.ucsd.ncmir.jibber.core.MogrificationParams;
import edu.ucsd.ncmir.jibber.core.MouseAction;
import edu.ucsd.ncmir.jibber.core.MouseDispatch;
import edu.ucsd.ncmir.jibber.core.Page;
import edu.ucsd.ncmir.jibber.core.TimeoutScheduler;
import edu.ucsd.ncmir.jibber.core.TimeoutTask;
import edu.ucsd.ncmir.jibber.events.ChangeEndpointEvent;
import edu.ucsd.ncmir.jibber.events.CheckCroppedStatusEvent;
import edu.ucsd.ncmir.jibber.events.CheckMogrifiableStatusEvent;
import edu.ucsd.ncmir.jibber.events.CreateMogrifierEvent;
import edu.ucsd.ncmir.jibber.events.CreateProgressBarEvent;
import edu.ucsd.ncmir.jibber.events.EnableOptionsEvent;
import edu.ucsd.ncmir.jibber.events.ErrorEvent;
import edu.ucsd.ncmir.jibber.events.FlipEvent.Flip;
import edu.ucsd.ncmir.jibber.events.GetCorrespondenceElementEvent;
import edu.ucsd.ncmir.jibber.events.GetCorrespondenceStringEvent;
import edu.ucsd.ncmir.jibber.events.GetImageDimensionEvent;
import edu.ucsd.ncmir.jibber.events.GetImageTransformElementEvent;
import edu.ucsd.ncmir.jibber.events.GetImageTransformEvent;
import edu.ucsd.ncmir.jibber.events.InfoDisplayEvent;
import edu.ucsd.ncmir.jibber.events.JibberChangeEvent;
import edu.ucsd.ncmir.jibber.events.MogrificationParamsQueryEvent;
import edu.ucsd.ncmir.jibber.events.MoveEvent;
import edu.ucsd.ncmir.jibber.events.ProgressUpdateEvent;
import edu.ucsd.ncmir.jibber.events.RepaintEvent;
import edu.ucsd.ncmir.jibber.events.ScaleEvent.Scale;
import edu.ucsd.ncmir.jibber.events.StatusEvent.Quittable;
import edu.ucsd.ncmir.jibber.events.StatusEvent;
import edu.ucsd.ncmir.jibber.events.UpdateEvent;
import edu.ucsd.ncmir.mogrification.Endpoint;
import edu.ucsd.ncmir.mogrification.MogrificationImageOp;
import edu.ucsd.ncmir.mogrification.MogrificationProgressReporterInterface;
import edu.ucsd.ncmir.mogrification.XYUVList;
import edu.ucsd.ncmir.mogrification.thin_plate_spline.ThinPlateSpline;
import edu.ucsd.ncmir.spl.minixml.Element;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  spl
 */
public class Canvas extends PDFPagePanel {

    private static final long serialVersionUID = 42L;

    private ListenerManager _listeners = new ListenerManager();

    public Canvas() {
        super();
        this.setFocusable(true);
        this._listeners.addList(new ChangePageEventListener(this), new CheckCroppedStatusEventListener(this), new CheckMogrifiableStatusEventListener(this), new ClearCorrespondencesEventListener(this), new CreateMogrifierEventListener(this), new CropDragEventListener(this), new CropPressEventListener(this), new CropReleaseEventListener(this), new DeleteReleaseEventListener(this), new DragDragEventListener(this), new DragPressEventListener(this), new DragReleaseEventListener(this), new FlipEventListener(this), new GetCorrespondenceElementEventListener(this), new GetCorrespondenceStringEventListener(this), new GetImageTransformElementEventListener(this), new GetImageTransformEventListener(this), new InfoPressEventListener(this), new LoadInitialTransformEventListener(this), new ModeEventListener(this), new MogrifyEventListener(this), new MovePressEventListener(this), new NewCorrespondenceEventListener(this.getClist()), new ParseCorrespondencesElementEventListener(this), new RepaintEventListener(this), new ReplaceEventListener(this.getClist()), new ResetClippingEventListener(this), new RestoreEventListener(this), new RotateEventListener(this), new ScaleEventListener(this), new SetDrawLabelsEventListener(this), this._set_marks_color_event_listener = new SetMarksColorEventListener(this), this._set_overlay_color_event_listener = new SetOverlayColorEventListener(this), new UpdateEventListener(this), new GetImageDimensionEventListener(this));
        this._listeners.enable();
        this.initComponents();
        this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    private ThinPlateSpline createMogrification() {
        return (this._clist.size() > 0) ? new ThinPlateSpline(new XYUVList(this._clist)) : null;
    }

    private class CreateMogrifierEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        CreateMogrifierEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            CreateMogrifierEvent cme = (CreateMogrifierEvent) event;
            cme.setMogrifier(this._canvas.createMogrification());
        }
    }

    private void disableListeners() {
        this._listeners.disable();
    }

    public void setPageAndImage(Page page, JibberImage object_image) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, IOException, ClassNotFoundException {
        this.setObjectImage(object_image);
        this.setPage(page);
    }

    private Page _atlas_page;

    private ImagePage _image_page = null;

    private PDFXformCmd _transform_cmd;

    public void setPage(Page page) {
        try {
            this._atlas_page = page;
            AffineTransform old_transform = null;
            if (this._image_page != null) old_transform = this._image_page.getPageTransform();
            this._image_page = new ImagePage(page.load(), this.getObjectImage());
            this._set_overlay_color_event_listener.updateSavedOverlayColor();
            if (old_transform != null) this._image_page.setPageTransform(old_transform);
            this.setDrawLabels(this.getDrawLabels());
            this.updateCanvas();
            new UpdateEvent().send();
        } catch (NoSuchMethodException ex) {
            new ErrorEvent().send(ex);
        } catch (InstantiationException ex) {
            new ErrorEvent().send(ex);
        } catch (IllegalAccessException ex) {
            new ErrorEvent().send(ex);
        } catch (InvocationTargetException ex) {
            new ErrorEvent().send(ex);
        } catch (IOException ex) {
            new ErrorEvent().send(ex);
        } catch (ClassNotFoundException ex) {
            new ErrorEvent().send(ex);
        }
    }

    private Page getAtlasPage() {
        return this._atlas_page;
    }

    private boolean _draw_labels = false;

    private boolean _draw_outlines = true;

    private void loadInitialTransform(AffineTransform transform) {
        this._image_page.setPageTransform(transform);
        this.updateCanvas();
    }

    private void setDrawLabels(boolean draw_labels) {
        this._image_page.enableText(this._draw_labels = draw_labels);
    }

    private boolean getDrawLabels() {
        return this._draw_labels;
    }

    private void setDrawOutlines(boolean draw_outlines) {
        this._image_page.enableOutlines(this._draw_outlines = draw_outlines);
    }

    private boolean getDrawOutlines() {
        return this._draw_outlines;
    }

    private class SetDrawLabelsEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        SetDrawLabelsEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            boolean[] states = (boolean[]) object;
            this._canvas.setDrawOutlines(states[0]);
            this._canvas.setDrawLabels(states[1]);
            new UpdateEvent().send();
        }
    }

    private boolean _mogrify = false;

    private class MogrifyEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        MogrifyEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            boolean mogrify = ((Boolean) object).booleanValue();
            if (mogrify) {
                if (this._canvas._clist.size() > 0) {
                    MogrificationParamsQueryEvent mpqe = new MogrificationParamsQueryEvent();
                    mpqe.sendWait();
                    MogrificationParams mp = mpqe.getParams();
                    this._canvas._mogrify = true;
                    JibberImage object_image = this._canvas._object_image;
                    ImagePage image_page = (ImagePage) this._canvas.getImagePage();
                    Page page = this._canvas.getAtlasPage();
                    AffineTransform image_to_unit = image_page.getImageToUnitTransform(page);
                    new CanvasEnabler(this._canvas, false);
                    ThinPlateSpline mogrification = this._canvas.createMogrification();
                    MogrificationImageOp mog_op = new MogrificationImageOp(mogrification, mp.getSampling(), object_image.getWidth(), object_image.getHeight(), image_to_unit, new Count());
                    this._canvas._image_page.setMogrification(mog_op);
                } else new ErrorEvent().send("No correspondences to mogrify.");
            } else {
                new CanvasEnabler(this._canvas, true);
                this._canvas._mogrify = false;
                this._canvas._image_page.setMogrification(null);
            }
            this._canvas.updateCanvas();
            new UpdateEvent().send();
        }
    }

    private class GetImageDimensionEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        GetImageDimensionEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            GetImageDimensionEvent gide = (GetImageDimensionEvent) event;
            ImagePage image_page = (ImagePage) this._canvas.getImagePage();
            Page page = this._canvas.getAtlasPage();
            AffineTransform image_to_unit = image_page.getImageToUnitTransform(page);
            JibberImage image = this._canvas.getObjectImage();
            double[] xy = { 0, 0, image.getWidth() - 1, image.getHeight() - 1 };
            double[] XY = new double[xy.length];
            image_to_unit.transform(xy, 0, XY, 0, 2);
            gide.setWidth(XY[2] - XY[0]);
            gide.setHeight(XY[3] - XY[1]);
        }
    }

    private class CanvasEnabler implements Runnable {

        private Canvas canvas;

        private boolean state;

        CanvasEnabler(Canvas canvas, boolean state) {
            this.canvas = canvas;
            this.state = state;
            EventQueue.invokeLater(this);
        }

        public void run() {
            this.canvas.setEnabled(this.state);
        }
    }

    private class Count implements MogrificationProgressReporterInterface {

        private int max;

        private int count = 0;

        private int _percent = 0;

        public void setMax(int max) {
            this.count = 0;
            this._percent = 0;
            this.max = max;
        }

        public void setLabel(String label) {
            new CreateProgressBarEvent(label).sendWait();
        }

        public synchronized void increment() {
            this.count++;
            double percent = 100 * (double) this.count / (double) this.max;
            if (this._percent < (int) Math.floor(percent)) new ProgressUpdateEvent().send(++this._percent);
        }
    }

    private JibberImage _object_image = null;

    public AffineTransform getUnitToDeviceTransform() {
        AffineTransform unit_to_pdf = this.getAtlasPage().getUnitToPDFScalingTransform();
        AffineTransform unit_to_device = new AffineTransform(super.getPDFToDeviceTransform());
        unit_to_device.concatenate(unit_to_pdf);
        return unit_to_device;
    }

    private final CorrList _clist = new CorrList();

    private void clearCorrespondences() {
        this.getClist().clear();
        new UpdateEvent().send();
        new EnableOptionsEvent().send();
    }

    private void updateCanvas() {
        super.showPage(this._image_page);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D buffer_graphics = (Graphics2D) g.create();
        buffer_graphics.setTransform(this.getUnitToDeviceTransform());
        Point2D p = new Point2D.Double(1.0, 0.0);
        buffer_graphics.getTransform().deltaTransform(p, p);
        float scale = 1.0f / (float) p.distance(0.0, 0.0);
        buffer_graphics.setStroke(new BasicStroke(scale));
        buffer_graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        buffer_graphics.setColor(this._marks_color);
        synchronized (this.getClist()) {
            ThinPlateSpline mogrification = null;
            try {
                if (this._mogrify) {
                    mogrification = this.createMogrification();
                    mogrification.initializeForward();
                }
                AffineTransform at = buffer_graphics.getTransform();
                AffineTransform iat = at.createInverse();
                synchronized (this.getClist()) {
                    for (ImageCorrespondence ic : this.getClist()) {
                        GeneralPath gp = new GeneralPath();
                        gp.append(ic.getPathIterator(at, mogrification), false);
                        buffer_graphics.draw(gp.createTransformedShape(iat));
                    }
                }
            } catch (Exception nte) {
            }
        }
    }

    private SetMarksColorEventListener _set_marks_color_event_listener;

    private Color _marks_color = Color.MAGENTA;

    private class SetMarksColorEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        SetMarksColorEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        private Color _saved_marks_color = Color.MAGENTA;

        public void handler(AsynchronousEvent event, Object object) {
            Color color = Color.MAGENTA;
            if (object != null) {
                if (object instanceof Color) color = (Color) object; else if (object instanceof Boolean) {
                    if (((Boolean) object).booleanValue()) color = this._saved_marks_color; else color = this._canvas._marks_color;
                }
            } else color = Color.MAGENTA;
            this._canvas._marks_color = color;
            new UpdateEvent().send();
        }
    }

    private SetOverlayColorEventListener _set_overlay_color_event_listener;

    private class SetOverlayColorEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        SetOverlayColorEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        private Color _saved_overlay_color = Color.BLACK;

        public void handler(AsynchronousEvent event, Object object) {
            Color color = Color.BLACK;
            if (object != null) {
                if (object instanceof Color) color = (Color) object; else if (object instanceof Boolean) {
                    if (((Boolean) object).booleanValue()) color = this._saved_overlay_color; else color = this._saved_overlay_color = this._canvas._image_page.getOverlayColor();
                }
            } else color = Color.BLACK;
            this._canvas._image_page.setOverlayColor(color);
            this._saved_overlay_color = color;
            new UpdateEvent().send();
        }

        public void updateSavedOverlayColor() {
            this._canvas._image_page.setOverlayColor(this._saved_overlay_color);
        }
    }

    private void initComponents() {
        setFocusTraversalPolicyProvider(true);
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addContainerListener(new java.awt.event.ContainerAdapter() {

            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                formComponentRemoved(evt);
            }
        });
        addAncestorListener(new javax.swing.event.AncestorListener() {

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                formAncestorRemoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 376, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 240, Short.MAX_VALUE));
    }

    private void formAncestorRemoved(javax.swing.event.AncestorEvent event) {
        this.disableListeners();
        this._object_image.deactivate();
    }

    private void formComponentRemoved(java.awt.event.ContainerEvent event) {
    }

    private MouseDispatch mouse_dispatch = Mode.ADD.getMouseDispatch();

    private TimeoutScheduler scheduler = new TimeoutScheduler(500L);

    private void formComponentResized(java.awt.event.ComponentEvent event) {
        if (this.isValid()) this.scheduler.schedule(new ResizeHandler());
    }

    private class ResizeHandler extends TimeoutTask {

        ResizeHandler() {
            super();
        }

        public void handler() {
            new UpdateEvent().send();
        }
    }

    private Endpoint encode(MouseEvent me) {
        return this.pointEncoder(me.getX(), me.getY());
    }

    private Endpoint pointEncoder(int x, int y) {
        Endpoint p = null;
        try {
            AffineTransform transform = this.getUnitToDeviceTransform();
            p = new Endpoint(transform.inverseTransform(new Point(x, y), null));
        } catch (NoninvertibleTransformException nte) {
            new ErrorEvent().sendWait(nte);
        }
        return p;
    }

    private void formKeyPressed(java.awt.event.KeyEvent evt) {
        if (this.isEnabled()) {
            MouseDispatch dispatch = this.getMouseDispatch();
            if (dispatch.isTweakable()) {
                Endpoint p1 = null;
                Endpoint p2 = null;
                switch(evt.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_KP_LEFT:
                        {
                            p1 = this.pointEncoder(1, 0);
                            p2 = this.pointEncoder(0, 0);
                            break;
                        }
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_KP_RIGHT:
                        {
                            p1 = this.pointEncoder(0, 0);
                            p2 = this.pointEncoder(1, 0);
                            break;
                        }
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_KP_UP:
                        {
                            p1 = this.pointEncoder(0, 1);
                            p2 = this.pointEncoder(0, 0);
                            break;
                        }
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_KP_DOWN:
                        {
                            p1 = this.pointEncoder(0, 0);
                            p2 = this.pointEncoder(0, 1);
                            break;
                        }
                }
                if ((p1 != null) && (p2 != null)) {
                    dispatch.get(MouseAction.PRESS).sendWait(p1);
                    dispatch.get(MouseAction.RELEASE).sendWait(p2);
                }
            }
        }
    }

    private class UpdateEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        UpdateEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this.canvas.updateCanvas();
        }
    }

    private class ClearCorrespondencesEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        ClearCorrespondencesEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this.canvas.clearCorrespondences();
            new StatusEvent().send(Quittable.SAVED);
        }
    }

    private class RepaintEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        RepaintEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this.canvas.updateCanvas();
        }
    }

    private class ModeEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        ModeEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Mode mode = (Mode) object;
            this.canvas.setMouse_dispatch(mode.getMouseDispatch());
            new SetCursor(this.canvas, this.canvas.getMouseDispatch().getCursor());
        }

        private class SetCursor implements Runnable {

            private Canvas canvas;

            private Cursor cursor;

            SetCursor(Canvas canvas, Cursor cursor) {
                this.canvas = canvas;
                this.cursor = cursor;
                EventQueue.invokeLater(this);
            }

            public void run() {
                this.canvas.setCursor(this.cursor);
            }
        }
    }

    private double last_x;

    private double last_y;

    private synchronized void delta(Endpoint p) {
        double x = p.getX();
        double y = p.getY();
        double delta_x = x - this.getLastX();
        double delta_y = y - this.getLastY();
        new ChangeEndpointEvent().sendWait(new Point2D.Double(delta_x, delta_y));
        try {
            AffineTransform transform = super.getPDFToDeviceTransform().createInverse();
            double[] pa = { 0, 0, 1, 0 };
            transform.deltaTransform(pa, 0, pa, 0, 2);
            double dx;
            double dy;
            dx = pa[2] - pa[0];
            dy = pa[3] - pa[1];
            double s = Math.sqrt((dx * dx) + (dy * dy));
            double[] trans = { delta_x, -delta_y };
            this.getUnitToDeviceTransform().deltaTransform(trans, 0, trans, 0, 1);
            double trans_x = trans[0];
            double trans_y = trans[1];
            ImagePage page = (ImagePage) this.getImagePage();
            page.translate(trans_x * s, trans_y * s);
            this.getObjectImage().updateOrigin(delta_x, delta_y);
            this.setLastX(x);
            this.setLastY(y);
            new UpdateEvent().send();
        } catch (NoninvertibleTransformException nte) {
        }
    }

    private class DragPressEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        DragPressEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Endpoint point = (Endpoint) object;
            this.canvas.setLastX(point.getX());
            this.canvas.setLastY(point.getY());
        }
    }

    private class DragDragEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        DragDragEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this.canvas.delta((Endpoint) object);
        }
    }

    private class DragReleaseEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        DragReleaseEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this.canvas.delta((Endpoint) object);
        }
    }

    public ImageCorrespondence findNearest(Endpoint p, double max_allowed) {
        double distance = Double.MAX_VALUE;
        ImageCorrespondence closest = null;
        synchronized (this.getClist()) {
            ImageCorrespondence[] list = this.getClist().toArray(new ImageCorrespondence[this.getClist().size()]);
            for (int i = 0; i < list.length; i++) {
                double d = list[i].getP2().distance(p);
                if (d < distance) {
                    distance = d;
                    closest = list[i];
                }
            }
        }
        distance = this.getUnitToDeviceTransform().deltaTransform(new Point2D.Double(distance, 0.0), null).distance(new Point2D.Double(0, 0));
        return distance <= max_allowed ? closest : null;
    }

    public ImageCorrespondence findNearestHead(Endpoint p, double max_allowed) {
        double distance = Double.MAX_VALUE;
        ImageCorrespondence closest = null;
        synchronized (this.getClist()) {
            ImageCorrespondence[] list = this.getClist().toArray(new ImageCorrespondence[this.getClist().size()]);
            for (int i = 0; i < list.length; i++) {
                double d = list[i].getP1().distance(p);
                if (d < distance) {
                    distance = d;
                    closest = list[i];
                }
            }
        }
        distance = this.getUnitToDeviceTransform().deltaTransform(new Point2D.Double(distance, 0.0), null).distance(new Point2D.Double(0, 0));
        return distance <= max_allowed ? closest : null;
    }

    private class MovePressEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        public MovePressEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            ImageCorrespondence nearest = this.canvas.findNearest((Endpoint) object, 10.0);
            if (nearest != null) {
                java.awt.Point apt = this.canvas.getLocationOnScreen();
                Endpoint ep = new Endpoint(nearest.getP2());
                Point2D pt = this.canvas.getUnitToDeviceTransform().transform(ep, null);
                int p = (int) (apt.getX() + pt.getX());
                int q = (int) (apt.getY() + pt.getY());
                try {
                    new Robot().mouseMove(p, q);
                    new MoveEvent(nearest).send();
                } catch (Exception ex) {
                    new ErrorEvent().sendWait("Error moving mouse!");
                }
            }
        }
    }

    private class DeleteReleaseEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        public DeleteReleaseEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            ImageCorrespondence nearest = this.canvas.findNearest((Endpoint) object, 10.0);
            if (nearest != null) {
                RemoveCorrespondenceOperation rco = new RemoveCorrespondenceOperation(this.canvas, nearest);
                new JibberChangeEvent().send(rco);
                new RepaintEvent().send();
            }
        }

        private class RemoveCorrespondenceOperation implements ChangeOperation {

            private Canvas canvas;

            private ImageCorrespondence correspondence;

            RemoveCorrespondenceOperation(Canvas canvas, ImageCorrespondence correspondence) {
                this.canvas = canvas;
                this.correspondence = correspondence.dup(false);
                synchronized (this.canvas.getClist()) {
                    this.canvas.getClist().remove(correspondence);
                }
            }

            public void undo() {
                synchronized (this.canvas.getClist()) {
                    this.canvas.getClist().add(correspondence);
                }
            }

            public void redo() {
                synchronized (this.canvas.getClist()) {
                    this.canvas.getClist().remove(correspondence);
                }
            }
        }
    }

    private class NewCorrespondenceEventListener extends AbstractAsynchronousEventListener {

        private CorrList clist;

        NewCorrespondenceEventListener(CorrList clist) {
            this.clist = clist;
        }

        public void handler(AsynchronousEvent event, Object object) {
            ImageCorrespondence ic = (ImageCorrespondence) object;
            double x = ic.getX1();
            double y = ic.getY1();
            boolean equal = false;
            for (ImageCorrespondence i : this.clist) if ((x == i.getX1()) && (y == i.getY1())) {
                equal = true;
                break;
            }
            if (equal) new ErrorEvent().send("Duplicated origin point."); else {
                AddCorrespondenceOperation aco = new AddCorrespondenceOperation(this.clist, ic);
                new JibberChangeEvent().send(aco);
                new RepaintEvent().send();
            }
        }

        private class AddCorrespondenceOperation implements ChangeOperation {

            private final CorrList clist;

            private ImageCorrespondence correspondence;

            AddCorrespondenceOperation(CorrList clist, ImageCorrespondence correspondence) {
                this.clist = clist;
                this.correspondence = correspondence.dup(true);
                synchronized (this.clist) {
                    int clist_size = this.clist.size();
                    this.clist.add(correspondence);
                    if (clist_size < 3) new EnableOptionsEvent().send();
                }
            }

            public void undo() {
                synchronized (this.clist) {
                    this.clist.remove(correspondence);
                }
            }

            public void redo() {
                synchronized (this.clist) {
                    this.clist.add(correspondence);
                }
            }
        }
    }

    private class ReplaceEventListener extends AbstractAsynchronousEventListener {

        private CorrList clist;

        ReplaceEventListener(CorrList clist) {
            this.clist = clist;
        }

        public void handler(AsynchronousEvent event, Object object) {
            ImageCorrespondence correspondence = (ImageCorrespondence) object;
            ImageCorrespondence found = this.clist.findP1(correspondence);
            if (found != null) found.setLine(found.getP1(), correspondence.getP2());
        }
    }

    class CorrList extends ArrayList<ImageCorrespondence> {

        private static final long serialVersionUID = 42L;

        Element getXML() {
            Element element = new Element("Correspondences");
            for (ImageCorrespondence c : this) element.addContent(c.getXMLElement());
            return element;
        }

        @Override
        public String toString() {
            String s = "";
            for (ImageCorrespondence c : this) s += c.toString() + "\n";
            return s;
        }

        ImageCorrespondence findP1(ImageCorrespondence correspondence) {
            ImageCorrespondence winner = null;
            synchronized (this) {
                for (ImageCorrespondence c : this) if (c.getP1().equals(correspondence.getP1())) {
                    winner = c;
                    break;
                }
            }
            return winner;
        }

        ImageCorrespondence find(ImageCorrespondence correspondence) {
            ImageCorrespondence winner = null;
            for (ImageCorrespondence c : this) if (c.equals(correspondence)) {
                winner = c;
                break;
            }
            return winner;
        }

        public boolean remove(ImageCorrespondence correspondence) {
            return super.remove(this.find(correspondence));
        }
    }

    private class InfoPressEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        public InfoPressEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Endpoint point = (Endpoint) object;
            ImageCorrespondence nearest = this._canvas.findNearestHead(point, 10.0);
            if (nearest != null) {
                Page atlas_page = this._canvas.getAtlasPage();
                new InfoDisplayEvent(nearest).send(atlas_page);
            }
        }
    }

    private class ChangePageEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        public ChangePageEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            try {
                this.canvas.setPage((Page) object);
            } catch (Exception e) {
                new ErrorEvent().send(e);
            }
        }
    }

    private class CheckMogrifiableStatusEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        public CheckMogrifiableStatusEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            CheckMogrifiableStatusEvent cmse = (CheckMogrifiableStatusEvent) event;
            cmse.setMogrifiable(this.canvas._clist.size() > 2);
        }
    }

    private JibberImage getObjectImage() {
        return _object_image;
    }

    private void setObjectImage(JibberImage object_image) {
        this._object_image = object_image;
    }

    private CorrList getClist() {
        return this._clist;
    }

    private MouseDispatch getMouseDispatch() {
        return mouse_dispatch;
    }

    private void setMouse_dispatch(MouseDispatch mouse_dispatch) {
        this.mouse_dispatch = mouse_dispatch;
    }

    private double getLastX() {
        return last_x;
    }

    private void setLastX(double last_x) {
        this.last_x = last_x;
    }

    private double getLastY() {
        return last_y;
    }

    private void setLastY(double last_y) {
        this.last_y = last_y;
    }

    private class GetCorrespondenceElementEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        GetCorrespondenceElementEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            GetCorrespondenceElementEvent gcee = (GetCorrespondenceElementEvent) event;
            gcee.setElement(this.canvas._clist.getXML());
        }
    }

    private class GetCorrespondenceStringEventListener extends AbstractAsynchronousEventListener {

        private Canvas canvas;

        GetCorrespondenceStringEventListener(Canvas canvas) {
            this.canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            GetCorrespondenceStringEvent gcee = (GetCorrespondenceStringEvent) event;
            gcee.setString(this.canvas._clist.toString());
        }
    }

    private class ParseCorrespondencesElementEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        ParseCorrespondencesElementEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Element element = (Element) object;
            List children = element.getChildren("Correspondence");
            for (Iterator i = children.iterator(); i.hasNext(); ) {
                Element e = (Element) i.next();
                Point2D p1 = this.getPoint(e.getChild("P1"));
                Point2D p2 = this.getPoint(e.getChild("P2"));
                ImageCorrespondence c = new ImageCorrespondence(p1, p2);
                synchronized (this._canvas._clist) {
                    this._canvas._clist.add(c);
                }
            }
            new UpdateEvent().send();
        }

        private Point2D getPoint(Element element) {
            Element coord = element.getChild("Coordinate");
            double x = new Double(coord.getChild("X").getText()).doubleValue();
            double y = new Double(coord.getChild("Y").getText()).doubleValue();
            return new Point2D.Double(x, y);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent event) {
        if (this.isEnabled()) this.getMouseDispatch().dispatchWait(MouseAction.PRESS, this.encode(event));
    }

    public void mouseReleased(MouseEvent event) {
        if (this.isEnabled()) this.getMouseDispatch().dispatchWait(MouseAction.RELEASE, this.encode(event));
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent event) {
        if (this.isEnabled()) this.getMouseDispatch().dispatchWait(MouseAction.DRAG, this.encode(event));
    }

    public void mouseMoved(MouseEvent e) {
    }

    private abstract class AbstractCropEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        AbstractCropEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Endpoint p = (Endpoint) object;
            double[] pq = { p.getX(), p.getY() };
            this._canvas.getUnitToDeviceTransform().transform(pq, 0, pq, 0, 1);
            this.driver(this._canvas, (int) pq[0], (int) pq[1]);
        }

        protected abstract void driver(Canvas canvas, int x, int y);
    }

    private class CropPressEventListener extends AbstractCropEventListener {

        CropPressEventListener(Canvas canvas) {
            super(canvas);
        }

        protected void driver(Canvas canvas, int x, int y) {
            canvas.initializeCropping(x, y);
        }
    }

    private class CropDragEventListener extends AbstractCropEventListener {

        private Canvas _canvas;

        CropDragEventListener(Canvas canvas) {
            super(canvas);
        }

        protected void driver(Canvas canvas, int x, int y) {
            canvas.dragCropBox(x, y, true);
        }
    }

    private class CropReleaseEventListener extends AbstractCropEventListener {

        private Canvas _canvas;

        CropReleaseEventListener(Canvas canvas) {
            super(canvas);
        }

        protected void driver(Canvas canvas, int x, int y) {
            canvas.dragCropBox(x, y, true);
            canvas.completeCropping();
            canvas.setCropped(true);
            new EnableOptionsEvent().send();
        }
    }

    private boolean _cropped = false;

    private void setCropped(boolean cropped) {
        this._cropped = cropped;
    }

    private boolean isCropped() {
        return this._cropped;
    }

    private class CheckCroppedStatusEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        CheckCroppedStatusEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            CheckCroppedStatusEvent ccse = (CheckCroppedStatusEvent) event;
            ccse.setCropped(this._canvas.isCropped());
        }
    }

    private class RotateEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        RotateEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            double rotation = ((Double) object).doubleValue();
            ImagePage page = (ImagePage) this._canvas.getImagePage();
            page.rotate(rotation);
            this._canvas.updateCanvas();
        }
    }

    private class ScaleEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        ScaleEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Scale scale = (Scale) object;
            ImagePage page = (ImagePage) this._canvas.getImagePage();
            page.scale(scale.getScale());
            this._canvas.updateCanvas();
        }
    }

    private class FlipEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        FlipEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Flip flip = (Flip) object;
            ImagePage page = (ImagePage) this._canvas.getImagePage();
            switch(flip) {
                case TOP_BOTTOM:
                    {
                        page.flip(false, true);
                        break;
                    }
                case LEFT_RIGHT:
                    {
                        page.flip(true, false);
                        break;
                    }
            }
            this._canvas.updateCanvas();
        }
    }

    private class ResetClippingEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        ResetClippingEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this._canvas.resetClip();
        }
    }

    private class RestoreEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        RestoreEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this._canvas.restore();
        }
    }

    private void restore() {
        this.setPage(this._atlas_page);
    }

    private class LoadInitialTransformEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        LoadInitialTransformEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            this._canvas.loadInitialTransform((AffineTransform) object);
        }
    }

    private class GetImageTransformElementEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        GetImageTransformElementEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            Element element = new Element("Matrix");
            AffineTransform transform = this._canvas._image_page.getPageTransform();
            double[] matrix = new double[6];
            transform.getMatrix(matrix);
            String s = "";
            for (double m : matrix) s += m + " ";
            element.setText(s);
            GetImageTransformElementEvent gitee = (GetImageTransformElementEvent) event;
            gitee.setElement(element);
        }
    }

    private class GetImageTransformEventListener extends AbstractAsynchronousEventListener {

        private Canvas _canvas;

        GetImageTransformEventListener(Canvas canvas) {
            this._canvas = canvas;
        }

        public void handler(AsynchronousEvent event, Object object) {
            ImagePage image_page = (ImagePage) this._canvas.getImagePage();
            Page page = this._canvas.getAtlasPage();
            GetImageTransformEvent gite = (GetImageTransformEvent) event;
            AffineTransform transform = image_page.getUnitPageTransform(page);
            gite.setTransform(transform);
            double[] dims = image_page.getUnitizedImageDimensions(page);
            gite.setUnitizedImageDimensions(dims);
        }
    }
}
