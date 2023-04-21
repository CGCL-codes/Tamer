package apollo.gui.detailviewers.sequencealigner;

import java.awt.Adjustable;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeSet;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Color;
import apollo.datamodel.*;
import apollo.editor.AnnotationEditor;
import org.bdgp.swing.FastTranslatedGraphics;
import org.bdgp.util.Range;
import org.bdgp.util.RangeHolder;
import apollo.gui.BaseScrollable;
import apollo.gui.detailviewers.sequencealigner.TierI.Level;
import apollo.gui.detailviewers.sequencealigner.renderers.BaseRendererI;
import apollo.gui.synteny.GuiCurationState;
import apollo.util.SwingMissingUtil;

/**
 * 
 * 
 */
public class MultiTierPanel extends JPanel implements Scrollable, BaseScrollable {

    private FeaturePlaceFinder placeFinder;

    private Map<TierI, AbstractTierPanel> tierMap;

    private List<TierI> tiers;

    public MultiTierPanel(FeaturePlaceFinder.Type type) {
        this.setBackground(Color.black);
        this.setBorder(null);
        this.setOpaque(true);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        placeFinder = FeaturePlaceFinder.createFinder(type);
        tiers = new ArrayList<TierI>();
        tierMap = new HashMap<TierI, AbstractTierPanel>();
    }

    public boolean canAddFeature(SeqFeatureI feature) {
        TierI tier = findTierForFeature(feature);
        return tier != null;
    }

    public AbstractTierPanel addFeature(SeqFeatureI feature) {
        if (!canAddFeature(feature)) {
            throw new IllegalStateException("Can't add feature " + feature);
        }
        TierI tier = findTierForFeature(feature);
        tier.addFeature(feature);
        return this.tierMap.get(tier);
    }

    public Collection<SeqFeatureI> getFeatures() {
        ArrayList<SeqFeatureI> features = new ArrayList<SeqFeatureI>();
        for (TierI tier : tiers) {
            features.addAll(tier.getFeatures(TierI.Level.TOP));
        }
        return features;
    }

    /**
   * Adds a new
   * @param tier
   * @param panel
   * @return
   */
    public AbstractTierPanel addPanel(AbstractTierPanel panel) {
        this.tiers.add(panel.getTier());
        this.tierMap.put(panel.getTier(), panel);
        return panel;
    }

    public List<AbstractTierPanel> getPanels() {
        ArrayList<AbstractTierPanel> panels = new ArrayList(numTiers());
        for (TierI tier : tiers) {
            panels.add(tierMap.get(tier));
        }
        return panels;
    }

    public List<TierI> getTiers() {
        ArrayList<TierI> t = new ArrayList(numTiers());
        for (TierI tier : tiers) {
            t.add(tier);
        }
        return t;
    }

    public void removeTier(TierI tier) {
        tiers.remove(tier);
        tierMap.remove(tier);
    }

    public void removePanel(AbstractTierPanel panel) {
        tiers.remove(panel.getTier());
        tierMap.remove(panel.getTier());
    }

    public int numTiers() {
        return this.tiers.size();
    }

    public void paint(Graphics g) {
        super.paint(g);
    }

    /** calculate size of components, calculate size of self, add components */
    public void reformat(boolean isRecursive) {
        this.removeAll();
        if (isRecursive) {
            for (AbstractTierPanel panel : this.tierMap.values()) {
                panel.reformat(isRecursive);
            }
        }
        Dimension preferredSize = calculateSize();
        setPreferredSize(preferredSize);
        for (TierI tier : tiers) {
            AbstractTierPanel panel = this.tierMap.get(tier);
            this.add(panel);
        }
    }

    public TierI getTier(int i) {
        return tiers.get(i);
    }

    public AbstractTierPanel getPanel(int i) {
        return tierMap.get(getTier(i));
    }

    public int tierForPixel(Point p) {
        return p.y / getPanel(0).getBaseHeight();
    }

    /** Converts from base pair to tier position */
    public int basePairToTierPosition(int basePair) {
        return this.getPanel(0).getTier().getPosition(basePair);
    }

    /** Converts from  tier position to base pair */
    public int tierPositionToBasePair(int position) {
        return this.getPanel(0).getTier().getBasePair(position);
    }

    /** Converts from pixel position to tier position */
    public int pixelPositionToTierPosition(int p) {
        return this.getPanel(0).pixelPositionToTierPosition(p);
    }

    /** Converts from tier position to pixel position */
    public int tierPositionToPixelPosition(int p) {
        return this.getPanel(0).tierPositionToPixelPosition(p);
    }

    public Strand getStrand() {
        return this.getPanel(0).getTier().getStrand();
    }

    public SequenceType getType() {
        return this.getTier(0).getType();
    }

    public Point getPixelForPosition(int p) {
        return this.getPanel(0).getPixelForPosition(p);
    }

    public int getPositionForPixel(Point p) {
        return this.getPanel(0).getPositionForPixel(p);
    }

    public void switchStrand() {
        for (TierI tier : tiers) {
            tier.setStrand(tier.getStrand().getOpposite());
        }
    }

    public void setStrand(Strand s) {
        for (TierI tier : tiers) {
            tier.setStrand(s);
        }
    }

    public int getHigh(SeqFeatureI sf) {
        return this.getPanel(0).getHigh(sf);
    }

    public int getLow(SeqFeatureI sf) {
        return this.getPanel(0).getLow(sf);
    }

    public SequenceI getReferenceSequence() {
        return this.getPanel(0).getTier().getReference();
    }

    public void setReferenceSequence(SequenceI s) {
        for (TierI tier : tiers) {
            tier.setReference(s);
        }
    }

    public ReadingFrame getReadingFrame() {
        return this.getPanel(0).getTier().getReadingFrame();
    }

    public void setReadingFrame(ReadingFrame rf) {
        for (TierI tier : tiers) {
            tier.setReadingFrame(rf);
        }
    }

    public Orientation getOrientation() {
        return this.getPanel(0).getOrientation();
    }

    public void switchOrientation() {
        for (TierI tier : tiers) {
            AbstractTierPanel tierPanel = tierMap.get(tier);
            tierPanel.setOrientation(tierPanel.getOrientation().getOpposite());
        }
    }

    public void setOrientation(Orientation o) {
        for (TierI tier : tiers) {
            AbstractTierPanel tierPanel = tierMap.get(tier);
            tierPanel.setOrientation(o);
        }
    }

    public BaseRendererI getRenderer() {
        return this.getPanel(0).getRenderer();
    }

    public void setRenderer(BaseRendererI r) {
        for (TierI tier : tiers) {
            AbstractTierPanel tierPanel = tierMap.get(tier);
            tierPanel.setRenderer(r);
        }
    }

    public void clear() {
        for (TierI tier : tiers) {
            tier.clear();
        }
    }

    public int getBaseHeight() {
        return this.getPanel(0).getBaseHeight();
    }

    public int getBaseWidth() {
        return this.getPanel(0).getBaseWidth();
    }

    /** Given a pixel position return the next feature */
    public SeqFeatureI getNextFeature(int p) {
        SeqFeatureI result = null;
        List<SeqFeatureI> features = new ArrayList<SeqFeatureI>();
        for (AbstractTierPanel tier : tierMap.values()) {
            SeqFeatureI feature = tier.getNextFeature(p, Level.TOP);
            features.add(feature);
        }
        for (SeqFeatureI feature : features) {
            if (result == null || (feature != null && getLow(feature) < getLow(result))) {
                result = feature;
            }
        }
        return result;
    }

    /** Given a pixel position return the previous feature */
    public SeqFeatureI getPrevFeature(int p) {
        SeqFeatureI result = null;
        List<SeqFeatureI> features = new ArrayList<SeqFeatureI>();
        for (AbstractTierPanel tier : tierMap.values()) {
            SeqFeatureI feature = tier.getPrevFeature(p, Level.TOP);
            if (feature != null) {
                features.add(feature);
            }
        }
        for (SeqFeatureI feature : features) {
            if (result == null || (feature != null && getHigh(feature) > getHigh(result))) {
                result = feature;
            }
        }
        return result;
    }

    /****************************************************************************
   * These Are the methods needed to implement the Scrollable interface.
   * 
   * @see javax.swing.Scrollable 
   ***************************************************************************/
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(getPanel(0).getPreferredScrollableViewportSize().width, getBaseHeight() * this.numTiers());
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return getPanel(0).getScrollableBlockIncrement(visibleRect, orientation, direction);
    }

    public boolean getScrollableTracksViewportHeight() {
        return getPanel(0).getScrollableTracksViewportHeight();
    }

    public boolean getScrollableTracksViewportWidth() {
        return getPanel(0).getScrollableTracksViewportWidth();
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return getPanel(0).getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    public int getVisibleBase() {
        return getPanel(0).getVisibleBase();
    }

    public int getVisibleBaseCount() {
        return getPanel(0).getVisibleBaseCount();
    }

    public void scrollToBase(int pos) {
        for (TierPanelI panel : tierMap.values()) {
            panel.scrollToBase(pos);
        }
    }

    /** position is pixel position */
    public void scrollToPosition(int pos) {
        JViewport view = SwingMissingUtil.getViewportAncestor(this);
        if (view != null) {
            Point point = getPixelForPosition(pos);
            if (point.x + view.getViewRect().width > getSize().width) point.x = getSize().width - view.getViewRect().width;
            view.setViewPosition(point);
        }
    }

    private Dimension calculateSize() {
        int width = this.getPanel(0).getPreferredSize().width;
        int height = this.getPanel(0).getPreferredSize().height * numTiers();
        return new Dimension(width, height);
    }

    /**
   * 
   * Finds a tier that has room for the given feature or null
   * 
   * @param feature
   * @return
   */
    private TierI findTierForFeature(SeqFeatureI feature) {
        return placeFinder.findTierForFeature(tiers, feature);
    }
}
