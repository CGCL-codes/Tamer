package apollo.gui.genomemap;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import apollo.datamodel.*;
import apollo.seq.*;
import apollo.config.Config;
import apollo.gui.TierManagerI;
import apollo.gui.Transformer;
import apollo.gui.drawable.Drawable;
import apollo.gui.drawable.DrawableSetI;
import apollo.gui.event.*;
import apollo.gui.menus.*;
import org.apache.log4j.*;

/**
 * An extension of LinearView for drawing analysis results.
 */
public abstract class ManagedView extends LinearView implements ManagedViewI {

    protected static final Logger logger = LogManager.getLogger(ManagedView.class);

    protected TierManagerI manager = null;

    protected Vector visibleDrawables = null;

    protected int leadSpaceSize = 2;

    protected int dropSpaceSize = 0;

    public ManagedView(JComponent ap, String name, boolean visible) {
        super(ap, name, visible);
    }

    public void setTierManager(TierManagerI ftm) {
        this.manager = ftm;
        this.manager.setView(this);
        updateManagerHeight();
        setInvalidity(true);
        if (isVisible()) getComponent().repaint();
    }

    public TierManagerI getTierManager() {
        return this.manager;
    }

    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        updateManagerHeight();
    }

    protected void updateManagerHeight() {
        if (manager != null) {
            manager.setViewHeight(transformer.getPixelBounds().height - getDropSpaceSize());
            transformer.setYRange(getYRange());
        }
    }

    protected int[] getYRange() {
        int[] managerrange = manager.getYRange();
        managerrange[0] -= getLeadSpaceSize() * TierManagerI.Y_PIXELS_PER_FEATURE;
        managerrange[1] += getDropSpaceSize() * TierManagerI.Y_PIXELS_PER_FEATURE;
        return managerrange;
    }

    public int getLeadSpaceSize() {
        return leadSpaceSize;
    }

    public void setLeadSpaceSize(int size) {
        leadSpaceSize = size;
    }

    public int getDropSpaceSize() {
        return dropSpaceSize;
    }

    public void setDropSpaceSize(int size) {
        dropSpaceSize = size;
    }

    public void setXOrientation(int direction) {
        switch(direction) {
            case Transformer.LEFT:
            case Transformer.RIGHT:
                getTransform().setXOrientation(direction);
                break;
            default:
                logger.error("Unknown direction in setXOrientation");
        }
        setInvalidity(true);
    }

    /** YOrientation gets flipped with revcomping */
    public void setYOrientation(int direction) {
        switch(direction) {
            case Transformer.UP:
            case Transformer.DOWN:
                getTransform().setYOrientation(direction);
                break;
            default:
                logger.error("Unknown direction in setYOrientation");
        }
    }

    public void clear() {
        this.visibleDrawables = null;
        this.manager = null;
    }

    public Vector getVisibleDrawables() {
        Vector them = null;
        if (manager != null) {
            try {
                int[] visible_range = transformer.getXVisibleRange();
                them = ((DrawableTierManagerI) manager).getVisibleDrawables(visible_range);
            } catch (Exception e) {
                logger.error("Manager for view " + this.getName() + " " + this.getClass().getName() + " is not a DrawableTierManagerI, but is a " + manager.getClass().getName(), e);
            }
        } else {
            logger.error("The manager is null!");
        }
        return them;
    }

    public void paintView() {
        if (graphics == null) {
            logger.debug("MV.pV null graphics - who knows why");
            return;
        }
        graphics.setColor(getBackgroundColour());
        if (!transparent) {
            graphics.fillRect(transformer.getPixelBounds().x, transformer.getPixelBounds().y, transformer.getPixelBounds().width, transformer.getPixelBounds().height);
        }
        paintDrawables();
    }

    protected void paintDrawables() {
        if (isInvalid()) visibleDrawables = getVisibleDrawables();
        int visFeatSize = (visibleDrawables == null ? 0 : visibleDrawables.size());
        if (visFeatSize == 0) return;
        int i = 0;
        int maxTierNum = manager.getMaxVisibleTierNumber();
        PixelMaskI mask = new PixelMask(maxTierNum, transformer.getPixelBounds().width);
        for (i = 0; i < visFeatSize; i++) {
            Vector curVis = (Vector) visibleDrawables.elementAt(i);
            int curVisSize = (curVis != null ? curVis.size() : 0);
            for (int j = 0; j < curVisSize; j++) {
                Drawable dsf = (Drawable) curVis.elementAt(j);
                dsf.draw(graphics, transformer, manager, mask);
            }
        }
    }
}
