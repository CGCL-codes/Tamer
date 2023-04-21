package co.edu.unal.ungrid.services.client.applet.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import co.edu.unal.ungrid.core.Point2D;
import co.edu.unal.ungrid.image.AbstractImage;
import co.edu.unal.ungrid.image.GrayIntImage;
import co.edu.unal.ungrid.services.client.applet.bimler.util.BimlerServiceOptions.ColorOption;
import co.edu.unal.ungrid.services.client.applet.bimler.view.BimlerView;
import co.edu.unal.ungrid.services.client.applet.bimler.view.BimlerViewParamSet;
import co.edu.unal.ungrid.services.client.applet.bimler.view.ViewEventListener;
import co.edu.unal.ungrid.services.client.applet.bimler.view.ViewEventListener.ViewEvent;
import co.edu.unal.ungrid.services.client.service.ServiceFactory;
import co.edu.unal.ungrid.services.client.util.ServiceOptions;
import co.edu.unal.ungrid.transformation.AffineTransform;
import co.edu.unal.ungrid.transformation.Transform;
import co.edu.unal.ungrid.util.OpenGlUtil.OpenGlCanvas;

public class OpenGlWrapper {

    public static class Layer {

        public Layer() {
            reset();
        }

        public void restore() {
            img = imgSave;
            resetTexture();
        }

        public void resetImage() {
            img = null;
            imgSave = null;
        }

        public void resetTexture() {
            txtId = 0;
        }

        public void reset() {
            txtSize = new Dimension();
            txtOrigin = new Point2D();
            imgScale = 1.0;
            fOpacity = 1.0f;
            resetImage();
            resetTexture();
        }

        public int getImgWidth() {
            return (img == null ? 0 : img.getWidth());
        }

        public int getImgHeight() {
            return (img == null ? 0 : img.getHeight());
        }

        public double getImgHeightWidthRatio() {
            double h = getImgHeight();
            double w = getImgWidth();
            return (w > 0.0 ? h / w : 1.0);
        }

        public int getImgWidthMm() {
            return (img == null ? 0 : img.getWidthMm());
        }

        public int getImgHeightMm() {
            return (img == null ? 0 : img.getHeightMm());
        }

        public int getOrgWidth() {
            return (img == null ? 0 : img.getOrgWidth());
        }

        public int getOrgHeight() {
            return (img == null ? 0 : img.getOrgHeight());
        }

        public double getTxtWidth() {
            return txtSize.getWidth();
        }

        public double getTxtHeight() {
            return txtSize.getHeight();
        }

        public double getTxtHeightWidthRatio() {
            double h = getTxtHeight();
            double w = getTxtWidth();
            return (w > 0.0 ? h / w : 1.0);
        }

        public double getTxtLeft() {
            return txtOrigin.x;
        }

        public double getTxtTop() {
            return txtOrigin.y;
        }

        public Point2D getTxtOrigin() {
            return txtOrigin;
        }

        public GrayIntImage img;

        public GrayIntImage imgSave;

        public int txtId;

        public Dimension txtSize;

        public Point2D txtOrigin;

        public double imgScale;

        public float fOpacity;
    }

    public OpenGlWrapper(final BimlerView view) {
        m_view = view;
        m_position = new Point();
        m_size = new Dimension();
        m_center = new Point2D();
        m_changeListeners = new ArrayList<ViewEventListener>();
        m_clr = new Color[ColorOption.values().length];
        m_fPointSize = DEF_POINT_SIZE;
    }

    public void reshape(final GLAutoDrawable drawable, int x, int y, int width, int height) {
        if (m_bDebug) System.out.println("BimlerOpenGlView::reshape() called");
        m_position.x = x;
        m_position.y = y;
        m_size.width = width;
        m_size.height = height;
        m_center.x = (double) width / 2;
        m_center.y = (double) height / 2;
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0f, m_size.getWidth(), m_size.getHeight(), 0.0f, -1.0f, 1.0f);
        gl.glPointSize(m_fPointSize);
    }

    public void displayChanged(final GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
        if (m_bDebug) System.out.println("BimlerOpenGlView::displayChanged() called");
    }

    public void dispose(final GLAutoDrawable drawable) {
        if (m_bDebug) System.out.println("BimlerOpenGlView::dispose() called");
    }

    protected void setOpenGlCanvas(final OpenGlCanvas canvas) {
        assert canvas != null;
        m_oglCanvas = canvas;
    }

    public OpenGlCanvas getOpenGlCanvas() {
        return m_oglCanvas;
    }

    public void repaint() {
        m_oglCanvas.repaint();
    }

    public void update() {
        m_oglCanvas.requestFocus();
        m_oglCanvas.repaint();
    }

    public void setCursor(int type) {
        assert m_oglCanvas != null;
        m_oglCanvas.setCursor(Cursor.getPredefinedCursor(type));
    }

    public void setWaitCursor() {
        setCursor(Cursor.WAIT_CURSOR);
    }

    public void setDefCursor() {
        setCursor(Cursor.DEFAULT_CURSOR);
    }

    public void requestFocus() {
        if (m_oglCanvas.hasFocus() == false) {
            m_oglCanvas.requestFocus();
        }
    }

    protected AffineTransform getTransform() {
        return m_view.getTransform();
    }

    public void copyTransform(final Transform t) {
        assert t != null;
        t.copy(t);
        update();
    }

    public Transform getTransformCopy() {
        Transform t = getTransform();
        return t.getCopy();
    }

    public double getTranslateX() {
        return m_view.getParam(BimlerViewParamSet.TRANSLATE_X);
    }

    public double getTranslateY() {
        return m_view.getParam(BimlerViewParamSet.TRANSLATE_Y);
    }

    public void setTranslateX(double f) {
        m_view.setParam(BimlerViewParamSet.TRANSLATE_X, f);
    }

    public void setTranslateY(double f) {
        m_view.setParam(BimlerViewParamSet.TRANSLATE_Y, f);
    }

    public double getZoomLevel() {
        return m_view.getZoomLevel();
    }

    public void setZoomLevel(double f) {
        assert f > 0.0;
        m_view.setParam(BimlerViewParamSet.ZOOM_LEVEL, f);
        notifyChangeListeners(ViewEvent.ZOOM_CHANGED);
    }

    protected void setSize(final Dimension size) {
        m_size.width = size.width;
        m_size.height = size.height;
    }

    public Dimension getSize() {
        return m_size;
    }

    protected double getAspectRatio() {
        return (m_size.width > 0.0 ? (double) m_size.height / m_size.width : 1.0);
    }

    protected Dimension getTextureSize(final Layer layer, final Dimension dimCanvas) {
        Dimension txtSize = new Dimension();
        AbstractImage<?> img = layer.img;
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        double canWidth = dimCanvas.getWidth();
        double canHeight = dimCanvas.getHeight();
        if (imgWidth == imgHeight) {
            if (canWidth > canHeight) {
                txtSize.width = (int) (canHeight);
            } else {
                txtSize.width = (int) (canWidth);
            }
            txtSize.height = txtSize.width;
        } else {
            if (imgWidth > imgHeight) {
                if (canWidth > canHeight) {
                    txtSize.width = (int) (canWidth);
                    txtSize.height = (int) (txtSize.width * img.getHeightWidthRatio());
                    if (txtSize.height > canHeight) {
                        txtSize.height = (int) (canHeight);
                        txtSize.width = (int) (txtSize.height * img.getWidthHeightRatio());
                    }
                } else {
                    txtSize.width = (int) (canWidth);
                    txtSize.height = (int) (txtSize.width * img.getHeightWidthRatio());
                }
            } else {
                if (canHeight > canWidth) {
                    txtSize.height = (int) (canHeight);
                    txtSize.width = (int) (txtSize.height * img.getWidthHeightRatio());
                    if (txtSize.width > canWidth) {
                        txtSize.width = (int) (canWidth);
                        txtSize.height = (int) (txtSize.width * img.getHeightWidthRatio());
                    }
                } else {
                    txtSize.height = (int) (canHeight);
                    txtSize.width = (int) (txtSize.height * img.getWidthHeightRatio());
                }
            }
        }
        return txtSize;
    }

    protected Point2D getTexturePos(final Layer layer, final Dimension dimCanvas) {
        Point2D pt = new Point2D();
        double txtWidth = layer.getTxtWidth();
        double txtHeight = layer.getTxtHeight();
        double canWidth = dimCanvas.getWidth();
        double canHeight = dimCanvas.getHeight();
        double z = getZoomLevel();
        double zx = z * txtWidth;
        double zy = z * txtHeight;
        pt.x = (canWidth - zx) / 2;
        pt.y = (canHeight - zy) / 2;
        return pt;
    }

    public void addChangeListener(final ViewEventListener vcl) {
        assert vcl != null;
        if (!m_changeListeners.contains(vcl)) {
            m_changeListeners.add(vcl);
        }
    }

    public void removeChangeListener(final ViewEventListener vcl) {
        assert vcl != null;
        m_changeListeners.remove(vcl);
    }

    protected void notifyChangeListeners(final ViewEvent ve) {
        for (ViewEventListener vrl : m_changeListeners) {
            vrl.processViewEvent(ve);
        }
    }

    public void valueChanged(final ServiceOptions opts) {
        updateOptions();
    }

    protected void updateOptions() {
        ColorOption[] co = ColorOption.values();
        for (int i = 0; i < co.length; i++) {
            m_clr[i] = ServiceFactory.getColor(co[i].id);
        }
    }

    protected BimlerView m_view;

    protected OpenGlCanvas m_oglCanvas;

    protected Point m_position;

    protected Dimension m_size;

    protected Point2D m_center;

    protected float m_fPointSize;

    protected boolean m_bDebug;

    private ArrayList<ViewEventListener> m_changeListeners;

    protected Color[] m_clr;

    public static final int BORDER_CLR = ColorOption.BORDER_CLR.ordinal();

    public static final int DRAG_CLR = ColorOption.DRAG_CLR.ordinal();

    public static final int ZOOM_CLR = ColorOption.ZOOM_CLR.ordinal();

    public static final int LABEL_CLR = ColorOption.LABEL_CLR.ordinal();

    public static final int VALUE_CLR = ColorOption.VALUE_CLR.ordinal();

    public static final int AXES_CLR = ColorOption.AXES_CLR.ordinal();

    public static final int COLORS = 6;

    public static final int BORDER = 10;

    public static final float DEF_POINT_SIZE = 6.0f;
}
