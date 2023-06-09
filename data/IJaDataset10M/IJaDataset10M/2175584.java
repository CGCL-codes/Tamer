package se.kth.cid.conzilla.map.graphics;

import java.awt.Dimension;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Vector;
import se.kth.cid.component.Component;
import se.kth.cid.component.EditEvent;
import se.kth.cid.concept.Concept;
import se.kth.cid.config.ConfigurationManager;
import se.kth.cid.conzilla.map.MapDisplayer;
import se.kth.cid.conzilla.map.MapEvent;
import se.kth.cid.conzilla.map.MapObjectImpl;
import se.kth.cid.conzilla.map.MapStoreManager;
import se.kth.cid.conzilla.properties.ColorTheme;
import se.kth.cid.conzilla.properties.ColorTheme.Colors;
import se.kth.cid.layout.ConceptLayout;
import se.kth.cid.layout.DrawerLayout;
import se.kth.cid.style.StyleManager;
import se.kth.cid.util.LocaleManager;

public abstract class DrawerMapObject extends MapObjectImpl {

    protected DrawerLayout drawLayout;

    protected Concept concept;

    protected java.util.List styleStack;

    protected StyleManager styleManager;

    BoxDrawer boxDrawer;

    TitleDrawer titleDrawer;

    boolean editable = false;

    public DrawerMapObject(DrawerLayout drawLayout, MapDisplayer displayer) {
        super(displayer);
        this.drawLayout = drawLayout;
        MapStoreManager manager = displayer.getStoreManager();
        styleManager = manager.getStyleManager();
        styleStack = styleManager.getStylesForDrawer(drawLayout);
        this.concept = manager.getConcept(drawLayout.getURI());
        boxDrawer = new BoxDrawer(this);
        titleDrawer = new TitleDrawer(this, displayer);
        drawLayout.addEditListener(this);
        if (concept != null) {
            concept.addEditListener(this);
        }
        titleDrawer.updateTitle();
        updateBox();
        if (concept == null) {
            initMark(new Mark(Colors.FOREGROUND, null, null), this);
            ConfigurationManager.getConfiguration().addPropertyChangeListener(ColorTheme.COLORTHEME, colorListener);
        } else {
            initMark(new Mark(Colors.FOREGROUND, Colors.CONCEPT_BACKGROUND, Colors.FOREGROUND), this);
            ConfigurationManager.getConfiguration().addPropertyChangeListener(ColorTheme.COLORTHEME, colorListener);
        }
        colorUpdate();
    }

    public Concept getConcept() {
        return concept;
    }

    public DrawerLayout getDrawerLayout() {
        return drawLayout;
    }

    public java.util.List getStyleStack() {
        return styleStack;
    }

    public StyleManager getStyleManager() {
        return styleManager;
    }

    public boolean getErrorState() {
        return concept == null;
    }

    public Dimension getPreferredSize() {
        return titleDrawer.getPreferredSize();
    }

    public TitleDrawer getTitleDrawer() {
        return titleDrawer;
    }

    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals(LocaleManager.DEFAULT_LOCALE_PROPERTY)) {
            titleDrawer.updateTitle();
            updateBox();
        }
    }

    public void updateBox() {
        boxDrawer.update(this);
        titleDrawer.updateBox(boxDrawer.getInnerBoundingBox());
    }

    public void componentEdited(EditEvent e) {
        dumpCache();
        switch(e.getEditType()) {
            case Component.ATTRIBUTES_EDITED:
                titleDrawer.updateTitle();
                updateBox();
                displayer.repaint();
                break;
            case ConceptLayout.BOUNDINGBOX_EDITED:
            case ConceptLayout.BODYVISIBLE_EDITED:
            case ConceptLayout.VERTICAL_TEXT_ANCHOR_EDITED:
            case ConceptLayout.HORIZONTAL_TEXT_ANCHOR_EDITED:
                updateBox();
                break;
            case se.kth.cid.component.Resource.METADATA_EDITED:
                titleDrawer.updateTitle();
                break;
            case DrawerLayout.DRAWERLAYOUT_ADDED:
            case DrawerLayout.DRAWERLAYOUT_REMOVED:
            case Concept.TRIPLE_ADDED:
            case Concept.TRIPLE_REMOVED:
        }
    }

    public void setEditable(boolean editable, MapEvent e) {
        if (editable) {
            titleDrawer.setEditable(true, e);
        } else titleDrawer.setEditable(false, e);
        this.editable = editable;
    }

    public boolean getEditable() {
        return editable;
    }

    public void setScale(double scale) {
        titleDrawer.setScale(scale);
    }

    public void colorUpdate() {
        if (titleDrawer != null) {
            titleDrawer.colorUpdate(getMark());
        }
    }

    public void setDisplayLanguageDiscrepancy(boolean b) {
        titleDrawer.setDisplayLanguageDiscrepancy(b);
    }

    public void paint(Graphics g) {
        if (drawLayout.getBodyVisible()) {
            if (boxDrawer != null) {
                boxDrawer.coloredPaint(g, this);
            }
            if (titleDrawer != null) {
                titleDrawer.coloredPaint(g, this);
            }
        }
    }

    public boolean checkAndFillHit(MapEvent m) {
        if (drawLayout.getBodyVisible()) {
            if (titleDrawer != null && titleDrawer.didHit(m)) {
                m.mapObject = this;
                m.hitType = MapEvent.HIT_BOXTITLE;
                return true;
            }
            if (boxDrawer != null && boxDrawer.didHit(m)) {
                m.mapObject = this;
                m.hitType = MapEvent.HIT_BOX;
                return true;
            }
        }
        return false;
    }

    public final Collection getBoundingboxes() {
        if (boundingboxes == null) {
            boundingboxes = new Vector();
            if (drawLayout.getBodyVisible()) {
                if (boxDrawer.getBoundingBox() != null) boundingboxes.addElement(boxDrawer.getOuterBorderedBoundingBox());
            }
            boundingboxes.addAll(getBoundingBoxesImpl());
        }
        return boundingboxes;
    }

    protected abstract Collection getBoundingBoxesImpl();

    public void detachImpl() {
        if (concept != null) {
            concept.removeEditListener(this);
        }
        ConfigurationManager.getConfiguration().removePropertyChangeListener(ColorTheme.COLORTHEME, colorListener);
        drawLayout.removeEditListener(this);
        boxDrawer = null;
        titleDrawer = null;
    }
}
