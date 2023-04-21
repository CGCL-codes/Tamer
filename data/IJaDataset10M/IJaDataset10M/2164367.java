package org.gems.designer.figures;

import java.util.Hashtable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.gems.designer.GraphicsProvider;
import org.gems.designer.ModelProvider;
import org.gems.designer.ModelRepository;
import org.gems.designer.model.Atom;
import org.gems.designer.model.GraphicsProviderImpl;
import org.gems.designer.model.ModelObject;

/**
 * @author danlee
 */
public class ContentFigure extends NodeFigure {

    private static final Dimension SIZE = new Dimension(Atom.ATOM_ICON);

    private boolean drawName_;

    /**
	 * Constructor for GroundFigure.
	 */
    public ContentFigure(ModelObject ob) {
        super(ob);
        GraphicsProvider p = getGraphicsProvider();
        Hashtable hints = (Hashtable) p.getDrawingHint(getNode());
        drawName_ = false;
        if (hints != null) {
            Boolean val = (Boolean) hints.get(DRAW_NAME_HINT);
            if (val != null) drawName_ = val.booleanValue();
        }
        setSize(getPreferredSize());
    }

    public GraphicsProvider getGraphicsProvider() {
        ModelProvider mp = getNode().getModelProvider();
        GraphicsProvider p = null;
        if (mp != null) p = mp.getGraphicsProvider();
        if (p == null) p = (GraphicsProvider) ModelRepository.getInstance().getModelProvider(getNode().getModelID()).getGraphicsProvider();
        return p;
    }

    /**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
    public Dimension getPreferredSize(int wHint, int hHint) {
        GraphicsProvider p = getGraphicsProvider();
        Dimension d = new Dimension(p.getLargeIcon(getNode()));
        if (drawName_) d.height += 15;
        return d;
    }

    /**
	 * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
	 */
    protected void paintFigure(Graphics g) {
        Rectangle r = getBounds().getCopy();
        r.height--;
        r.width--;
        g.translate(r.getLocation());
        GraphicsProvider p = getGraphicsProvider();
        Image img = p.getLargeIcon(getNode());
        int w = img.getBounds().width;
        int h = img.getBounds().height;
        double sw = ((double) w) * (((double) getNode().getZ()) / 100.00);
        double sh = ((double) h) * (((double) getNode().getZ()) / 100.00);
        g.drawImage(img, 0, 0, w, h, 0, 0, w, h);
        if (drawName_) g.drawText(getNode().getName(), 0, getSize().height - 15);
    }
}
