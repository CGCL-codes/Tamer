package org.spantus.chart.marker;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComponent;
import org.spantus.core.marker.MarkerSet;
import org.spantus.core.marker.MarkerSetHolder;
import org.spantus.logger.Logger;
import org.spantus.utils.Assert;

/**
 * 
 * @author Mindaugas Greibus
 * @since 0.0.1
 * Created on Feb 22, 2009
 */
public class MarkerGraph extends JComponent {

    /**
	 * 
	 */
    Map<String, MarkerSetComponent> layers;

    MarkerSetHolder markerSetHolder;

    MarkerGraphCtx ctx;

    private static final long serialVersionUID = 1L;

    protected Logger log = Logger.getLogger(getClass());

    public boolean addMarker(float from, float length) {
        return false;
    }

    public void initialize() {
        Assert.isTrue(getMarkerSetHolder() != null, "Should not be null");
        setLayout(null);
        Map<String, MarkerSet> map = getMarkerSetHolder().getMarkerSets();
        for (Entry<String, MarkerSet> entry : map.entrySet()) {
            MarkerSetComponent comp = createMarkerSetComponent(entry.getValue());
            comp.setName(entry.getKey());
            getLayers().put(entry.getKey(), comp);
            add(comp);
        }
    }

    public MarkerSetComponent createMarkerSetComponent(MarkerSet markerSet) {
        MarkerSetComponent component = new MarkerSetComponent();
        component.addMouseListener(getMouseListeners()[0]);
        if (getMouseMotionListeners().length > 0) {
            component.addMouseMotionListener(getMouseMotionListeners()[0]);
        }
        if (getKeyListeners().length > 0) {
            component.addKeyListener(getKeyListeners()[0]);
        }
        component.setMarkerSet(markerSet);
        component.setCtx(getCtx());
        component.initialize();
        return component;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        int yLayer = 0;
        int layersCount = getLayers().keySet().size();
        layersCount = layersCount == 0 ? 1 : layersCount;
        int heightLayer = (getSize().height - 2) / layersCount;
        int widthLayer = getSize().width - 2;
        for (MarkerSetComponent cmp : getLayers().values()) {
            cmp.setCtx(getCtx());
            cmp.setLocation(0, yLayer);
            cmp.changeSize(new Dimension(widthLayer, heightLayer));
            yLayer += heightLayer;
            cmp.repaintIfDirty();
        }
    }

    public void resetScreenCoord() {
        for (MarkerSetComponent cmp : getLayers().values()) {
            for (MarkerComponent mc : cmp.getMarkerComponents()) {
                mc.resetScreenCoord();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        log.debug("clicked: " + getName() + findComponentAt(e.getPoint()));
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void setMarkerSetHolder(MarkerSetHolder markerSetHolder) {
        this.markerSetHolder = markerSetHolder;
    }

    public MarkerSetHolder getMarkerSetHolder() {
        return markerSetHolder;
    }

    public Map<String, MarkerSetComponent> getLayers() {
        if (layers == null) {
            layers = new LinkedHashMap<String, MarkerSetComponent>();
        }
        return layers;
    }

    public MarkerGraphCtx getCtx() {
        if (ctx == null) {
            ctx = new MarkerGraphCtx();
            ctx.setXOffset(BigDecimal.valueOf(0));
            ctx.setXScalar(BigDecimal.valueOf(1));
        }
        return ctx;
    }

    public void setCtx(MarkerGraphCtx ctx) {
        this.ctx = ctx;
    }
}
