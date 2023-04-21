package com.bluebrim.layout.impl.client;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import com.bluebrim.base.shared.*;
import com.bluebrim.swing.client.*;

/**
 * Component for displaying a set of CoViews and an optinal decoration below it.
 *
 * @author: Dennis
 */
public class CoViewPanel extends CoAbstractViewPanel implements CoZoomPanel.Zoomable {

    protected Dimension m_preferredSize;

    protected double m_requestedScale = 1;

    protected double m_scale = 1;

    protected Map m_renderingHints = new HashMap();

    ;

    public interface Decoration {

        int getSpan(CoViewPanel p);

        void paint(Graphics g, CoViewPanel p);
    }

    ;

    public static class LabelDecoration implements Decoration {

        public int getSpan(CoViewPanel p) {
            return (int) p.getFontMetrics(p.getFont()).getHeight();
        }

        public void paint(Graphics g, CoViewPanel p) {
            Insets i = p.getInsets();
            int w = getSpan(p);
            g.setColor(p.getBackground());
            g.fillRect(0, 0, p.getWidth(), i.bottom + w);
            g.setColor(p.getForeground());
            g.setFont(p.getFont());
            g.drawString(getLabel(p), i.left, w);
        }

        public String getLabel(CoViewPanel p) {
            CoView v = p.getView();
            return (v == null) ? "" : v.toString();
        }
    }

    ;

    protected Decoration m_decoration = createDefaultDecoration();

    public CoViewPanel() {
        this((CoView) null, 1.0);
    }

    public CoViewPanel(CoView view) {
        this(view, 1);
    }

    public CoViewPanel(CoView view, double scale) {
        super();
        if (view != null) m_views.add(view);
        setOpaque(true);
        m_requestedScale = scale;
    }

    protected Decoration createDefaultDecoration() {
        return null;
    }

    public Decoration getDecoration() {
        return m_decoration;
    }

    public Dimension getPreferredSize() {
        if (m_preferredSize == null) updatePreferredSize();
        return m_preferredSize;
    }

    protected double getPreferredViewHeight() {
        return getViewsHeight() * m_requestedScale;
    }

    protected double getPreferredViewWidth() {
        return getViewsWidth() * m_requestedScale;
    }

    public double getScale() {
        return m_requestedScale / m_screenResolution;
    }

    public CoView getView() {
        return (m_views.size() == 1) ? (CoView) m_views.get(0) : null;
    }

    public void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
        updateScale(getWidth(), getHeight());
        if ((m_scale > 0) && m_views.size() > 0) {
            Graphics2D g2d = (Graphics2D) g;
            Map tmp = g2d.getRenderingHints();
            g2d.setRenderingHints(m_renderingHints);
            Insets i = getInsets();
            g2d.translate(i.left, i.top);
            g2d.translate(1, 1);
            g2d.scale(m_scale, m_scale);
            CoScreenPaintable p = CoScreenPaintable.wrap(g);
            paintViews(p);
            p.releaseDelegate();
            g2d.scale(1 / m_scale, 1 / m_scale);
            g2d.translate(-1, -1);
            g2d.translate(-i.left, -i.top);
            g2d.setRenderingHints(tmp);
        }
        if (m_decoration != null) {
            Insets i = getInsets();
            int h = getHeight() - i.bottom - m_decoration.getSpan(this);
            g.translate(0, h);
            g.setColor(getForeground());
            m_decoration.paint(g, this);
            g.translate(0, -h);
        }
    }

    public void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);
    }

    public void setDecoration(Decoration r) {
        m_decoration = r;
    }

    public void setRenderingHints(Map h) {
        m_renderingHints = h;
        repaint();
    }

    public void setScale(double scale) {
        m_requestedScale = scale * m_screenResolution;
        m_preferredSize = null;
        invalidate();
        revalidate();
        repaint();
    }

    public void setView(CoView view) {
        m_views.clear();
        if (view != null) m_views.add(view);
        postSetViews();
    }

    public void to(Point2D p) {
        Insets i = getInsets();
        p.setLocation((p.getX() - i.left) / m_scale, (p.getY() - i.top) / m_scale);
    }

    protected void updatePreferredSize() {
        double w = 0;
        double h = 0;
        if (m_views.size() > 0) {
            w = getPreferredViewWidth();
            h = getPreferredViewHeight();
        }
        if (m_decoration != null) h += m_decoration.getSpan(this);
        Insets i = getInsets();
        m_preferredSize = new Dimension((int) (1 + w + i.left + i.right), (int) (1 + h + i.top + i.bottom));
    }

    protected void updateScale(int w, int h) {
        double W = getViewsWidth();
        double H = getViewsHeight();
        if (W > 0 && H > 0) {
            Insets i = getInsets();
            w -= i.left + i.right + 2;
            h -= i.top + i.bottom + 2;
            if (w < 0) w = 0;
            if (h < 0) h = 0;
            m_scale = Math.min(w / W, h / H);
        }
    }

    public static final double m_screenResolution = Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;

    protected List m_views = new ArrayList();

    public CoViewPanel(List views) {
        this(views, 1);
    }

    public CoViewPanel(List views, double scale) {
        super();
        m_views.addAll(views);
        setOpaque(true);
        m_requestedScale = scale;
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public List getViews() {
        return m_views;
    }

    protected double getViewsHeight() {
        double h = 0;
        int I = m_views.size();
        for (int i = 0; i < I; i++) {
            h = Math.max(h, ((CoView) m_views.get(i)).getHeight());
        }
        return h;
    }

    protected double getViewsWidth() {
        double w = 0;
        int I = m_views.size();
        for (int i = 0; i < I; i++) {
            w = Math.max(w, ((CoView) m_views.get(i)).getWidth());
        }
        return w;
    }

    protected void paintViews(CoPaintable p) {
        int I = m_views.size();
        for (int i = 0; i < I; i++) {
            ((CoView) m_views.get(i)).paint(p);
        }
    }

    private void postSetViews() {
        m_preferredSize = null;
        invalidate();
        revalidate();
        repaint();
    }

    public void setViews(List views) {
        m_views.clear();
        m_views.addAll(views);
        postSetViews();
    }
}
