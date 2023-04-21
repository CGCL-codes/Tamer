package apollo.gui.genomemap;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.List;
import java.util.*;
import java.io.*;
import javax.swing.*;
import org.apache.log4j.*;
import org.bdgp.util.DNAUtils;
import apollo.datamodel.*;
import apollo.datamodel.seq.SRSSequence;
import apollo.config.Config;
import apollo.config.FeatureProperty;
import apollo.gui.ControlledObjectI;
import apollo.gui.Controller;
import apollo.gui.Selection;
import apollo.gui.SelectionItem;
import apollo.gui.SelectionManager;
import apollo.gui.Transformer;
import apollo.gui.drawable.DrawableSetI;
import apollo.gui.drawable.DrawableFeatureSet;
import apollo.gui.drawable.DrawableUtil;
import apollo.gui.drawable.SiteCodon;
import apollo.gui.event.*;
import apollo.util.FeatureList;

/**
 * A view to display sites (start and stop codons).
 * Displays 3 stop tiers/Sites and 3 start tiers/Sites.
 * Took out implementing of SelectViewI. SelectViewI is for handling external 
 * selections, but presently there is no way to externally select a codon,
 * except that codons will select when transcripts are selected, but this is 
 * handled through handlefeatureSelection(it's easier that way)
 */
public class SiteView extends TierView {

    protected static final Logger logger = LogManager.getLogger(SiteView.class);

    public static final char[] startCodon = { 'A', 'T', 'G' };

    public static final int startCodonHashCode = primitiveHash(startCodon);

    private Codon[] stopCodons = null;

    protected int height = 0;

    /** [0,1,2] start sites, [3,4,5] for stop sites (3 frames for each) */
    protected Sites[] sitesArray = new Sites[0];

    protected CurationSet curation;

    /** For TranslationStartStopSelectionListener */
    protected ResultView resultView;

    /** component is ApolloPanel */
    public SiteView(JComponent ap, String name, Controller controller, SelectionManager selectionManager) {
        super(ap, name, selectionManager);
        initStopCodons();
        setController(controller);
        setTierManager(new SiteTierManager(controller));
        controller.addListener(new TranslationStartStopSelectionListener());
        setBackgroundColour(Color.white);
    }

    /** these are dependent on genetic code */
    private void initStopCodons() {
        List stopCharArrays = DNAUtils.getStopCodonsAsCharArrayList();
        stopCodons = new Codon[stopCharArrays.size()];
        for (int i = 0; i < stopCharArrays.size(); i++) {
            char[] stop = (char[]) stopCharArrays.get(i);
            stopCodons[i] = new Codon(stop);
        }
    }

    private class Codon {

        private int hashCode;

        private char[] chars;

        private Codon(char[] chars) {
            this.chars = chars;
            hashCode = primitiveHash(chars);
        }

        private boolean matches(int hashCode, char seqi, char seqip1) {
            return hashCode == this.hashCode && chars[0] == seqi && chars[1] == seqip1;
        }
    }

    public void setCurationSet(CurationSet set) {
        this.curation = set;
        if (set == null) return;
        if (set.length() > 0) {
            this.curation = set;
        } else {
            logger.error("Null sequence in SiteView setSequence");
        }
    }

    public void paintDrawables() {
        ((SiteTierManager) manager).updateUserCoordBoundaries();
        Color colour;
        for (int i = 0; i < sitesArray.length; i++) {
            FeatureProperty fp;
            if (i < 3) {
                int frame = i + 1;
                fp = Config.getPropertyScheme().getFeatureProperty("startcodon_frame" + frame);
            } else {
                int frame = i - 3 + 1;
                fp = Config.getPropertyScheme().getFeatureProperty("stopcodon_frame" + frame);
            }
            if (!fp.getTier().isVisible()) continue;
            colour = fp.getColour();
            SiteTier tier = (SiteTier) (manager.getTier(i));
            for (int j = 0; j < sitesArray[i].size(); j++) {
                int site = sitesArray[i].elementAt(j);
                boolean selected = sitesArray[i].isSelected(site);
                drawSite(tier, site, colour, selected);
            }
        }
    }

    private void drawSite(SiteTier tier, int position, Color colour, boolean selected) {
        Rectangle box = getDrawRectangle(tier, position);
        graphics.setColor(colour);
        graphics.fillRect(box.x, box.y, box.width, box.height);
        if (selected) {
            graphics.setColor(Color.red);
            graphics.drawRect(box.x - 2, box.y - 2, box.width + 4, box.height + 4);
        }
    }

    private Rectangle getDrawRectangle(SiteTier tier, int position) {
        int low = tier.getDrawLow();
        int high = tier.getDrawHigh();
        Point lowpos;
        if (transformer.getXOrientation() == Transformer.LEFT && getStrand() == 1) lowpos = transformer.toPixel(position - 1, low); else if (transformer.getXOrientation() == Transformer.LEFT && getStrand() == -1) lowpos = transformer.toPixel(position - 3, low); else if (transformer.getXOrientation() == Transformer.RIGHT && getStrand() == 1) lowpos = transformer.toPixel(position + 2, low); else lowpos = transformer.toPixel(position, low);
        Point highpos = transformer.toPixel(position, high);
        int width = (int) ((transformer.getXPixelsPerCoord() * 3) < 1 ? 1 : transformer.getXPixelsPerCoord() * 3);
        return new Rectangle(lowpos.x, lowpos.y, width, highpos.y - lowpos.y);
    }

    public void setVisible(boolean state) {
        super.setVisible(state);
        if (isVisible()) {
            changeSites();
        }
    }

    private void changeSites() {
        _createSites(transformer.getXVisibleRange());
        manager.setTierData(sitesArray);
        manager.fireTierManagerEvent(TierManagerEvent.LAYOUT_CHANGED);
        setInvalidity(true);
    }

    public void setCentre(int val) {
        super.setCentre(val);
        if (isVisible()) {
            changeSites();
        }
    }

    protected boolean needsTextAvoidUpdate() {
        return false;
    }

    public void setZoomFactor(double fac) {
        super.setZoomFactor(fac);
        if (isVisible()) {
            int siteShowLim = Config.getSiteShowLimit();
            boolean showSites = transformer.getXCoordsPerPixel() < siteShowLim;
            if (!showSites) {
                sitesArray = new Sites[0];
            } else {
                changeSites();
            }
        }
    }

    private static int primitiveHash(char[] array) {
        int hashCode = 0;
        if (array.length < 3) {
            logger.error("primitiveHash expects an array at least 3 long");
        }
        for (int i = 0; i < 3; i++) {
            hashCode += array[i];
        }
        return hashCode;
    }

    public void _createSites(int[] visRange) {
        if (curation == null) {
            sitesArray = new Sites[0];
            return;
        }
        int seqLow = curation.getLow();
        int seqHigh = curation.getHigh();
        if (seqHigh <= 0) {
            sitesArray = new Sites[0];
            return;
        }
        int min = visRange[0] < visRange[1] ? (int) visRange[0] : (int) visRange[1];
        int max = visRange[0] > visRange[1] ? (int) visRange[0] : (int) visRange[1];
        min = min < seqLow ? seqLow : min;
        max = max > seqHigh ? seqHigh : max;
        int len = max - min + 1;
        if (len <= 0) {
            sitesArray = new Sites[0];
            return;
        }
        int remainder = len % 3;
        int extension = Math.abs(3 - remainder);
        if (getStrand() == 1) {
            if ((max + extension) <= seqHigh) {
                max += extension;
            } else {
            }
        } else {
            if ((min - extension) >= seqLow) {
                min -= extension;
            } else {
            }
        }
        char[] seq_chars = {};
        SequenceI seq = curation.getRefSequence();
        String dna = (getStrand() == 1 ? seq.getResidues(min, max) : seq.getResidues(max, min));
        if (dna == null || dna.length() == 0) {
            logger.info("No sequence to make stops & starts from");
            return;
        }
        if (Character.isLowerCase(dna.charAt(0))) dna = dna.toUpperCase();
        seq_chars = dna.toCharArray();
        if (seq_chars.length < 3) {
            sitesArray = new Sites[0];
            return;
        }
        int frame = getFrame(min, max);
        if (sitesArray.length == 0) {
            sitesArray = new Sites[6];
            for (int i = 0; i < 6; i++) sitesArray[i] = new Sites();
        } else {
            for (int i = 0; i < 6; i++) sitesArray[i].clearPositions();
        }
        char seqi = 0;
        char seqip1 = seq_chars[0];
        char seqip2 = seq_chars[1];
        int hashCode3 = seqip1 + seqip2;
        for (int i = 0; i < seq_chars.length - 2; i++) {
            hashCode3 -= seqi;
            seqi = seqip1;
            seqip1 = seqip2;
            seqip2 = seq_chars[i + 2];
            hashCode3 += seqip2;
            if (seqi != 'G' && seqi != 'C') {
                if (hashCode3 == startCodonHashCode && startCodon[0] == seqi && startCodon[1] == seqip1) {
                    int pos = (getStrand() == 1) ? i + min : max - i;
                    sitesArray[frame].addSite(pos);
                } else if (isStop(hashCode3, seqi, seqip1)) {
                    int pos = (getStrand() == 1) ? i + min : max - i;
                    sitesArray[frame + 3].addSite(pos);
                }
            }
            ++frame;
            if (frame == 3) frame = 0;
        }
    }

    private boolean isStop(int hashCode, char seqi, char seqip1) {
        for (int i = 0; i < stopCodons.length; i++) {
            if (stopCodons[i].matches(hashCode, seqi, seqip1)) return true;
        }
        return false;
    }

    public boolean allowsTierDrags() {
        return false;
    }

    public Rectangle getPreferredSize() {
        int total_height = height;
        if (manager != null) {
            total_height += manager.getTotalHeight();
        }
        return new Rectangle(0, 0, 1, total_height);
    }

    public void setResultView(ResultView rv) {
        resultView = rv;
    }

    public ResultView getResultView() {
        return resultView;
    }

    public boolean handleTierManagerEvent(TierManagerEvent evt) {
        setInvalidity(true);
        if (getPreferredSize().height != getBounds().height) {
            getComponent().doLayout();
            return true;
        } else {
            return super.handleTierManagerEvent(evt);
        }
    }

    /** From PickViewI for selection, selectParents is ignored here */
    public Selection findFeaturesForSelection(Rectangle rect, boolean selectParents) {
        Selection selection = new Selection();
        Vector codons = findDrawables(rect);
        for (int i = 0; i < codons.size(); i++) {
            SiteCodon codon = (SiteCodon) codons.elementAt(i);
            SelectionItem selItem = new SelectionItem(this, codon.getFeature());
            selItem.addSelectionListener(codon);
            selection.add(selItem);
        }
        return selection;
    }

    /** Finds all the sites in rect and creates SiteCodon for them.
   * Returns vector of SiteCodons
   * The SiteCodons are not drawn.
   * Sites are notified when a SiteCodon is selected.
   */
    protected Vector findDrawables(Rectangle rect, boolean selected_only) {
        Vector matches = new Vector();
        String type;
        String namePrefix = "start codon";
        int frame;
        for (int i = 0; i < sitesArray.length; i++) {
            if (i >= 3) {
                frame = i - 2;
                type = "stopcodon_frame" + frame;
                namePrefix = "stop codon";
            } else {
                frame = i + 1;
                type = "startcodon_frame" + frame;
            }
            SiteTier tier = (SiteTier) (manager.getTier(i));
            for (int j = 0; j < sitesArray[i].size(); j++) {
                int pos = sitesArray[i].elementAt(j);
                Rectangle box = getDrawRectangle(tier, pos);
                if (rect.intersects(box)) {
                    if ((selected_only && sitesArray[i].isSelected(pos)) || (!selected_only)) {
                        int low = (getStrand() == 1) ? pos : pos - 2;
                        int high = low + 2;
                        SiteCodon site = sitesArray[i].getSiteCodon(low, high, type, getStrand(), namePrefix);
                        matches.addElement(site);
                    }
                }
            }
        }
        return matches;
    }

    /** Clear all selections in all Sites */
    public void clearSelections() {
        for (int i = 0; i < sitesArray.length; i++) {
            sitesArray[i].clearSelected();
        }
    }

    public void selectParents(Selection selection) {
    }

    private int getFrame(int bp) {
        return getFrame(bp, bp);
    }

    /** Returns frame of bp relative to curation seq as 0,1,2 (NOT 1,2,3) 
      util function? */
    private int getFrame(int rangeLow, int rangeHi) {
        if (isForwardStrand()) {
            return (rangeLow - curation.getLow()) % 3;
        } else {
            return (curation.getHigh() - rangeHi) % 3;
        }
    }

    /** Looks for transcripts in the selection and highlights the corresponding
      start and stop codons.Creates SiteCodons for selected codons and sends 
      off to SelectionManager.This is consistent with how selection is done 
      on mouse click, also SelectionManager then handles the deselecting of 
      the codons. Does this belong in ApolloPanel?
      I think its ok to have it here (i think its easier). This needs to 
      happen with every selection, internal and external. This will receive 
      selection events from ApolloPanel as well.
  */
    private class TranslationStartStopSelectionListener implements FeatureSelectionListener {

        public boolean handleFeatureSelectionEvent(FeatureSelectionEvent e) {
            if (e.getSource() instanceof TranslationStartStopSelectionListener) return false;
            if (!isVisible()) return false;
            HashSet transSet = getTranscripts(e.getSelection());
            Selection codonSelection = new Selection();
            for (Iterator i = transSet.iterator(); i.hasNext(); ) {
                Transcript t = (Transcript) i.next();
                SelectionItem startSelectionItem = getStartSelectionItem(t);
                if (startSelectionItem != null) codonSelection.add(startSelectionItem);
                SelectionItem stopSelectionItem = getStopSelectionItem(t);
                if (stopSelectionItem != null) codonSelection.add(stopSelectionItem);
            }
            if (codonSelection.size() > 0) {
                selectionManager.addToCurrentSelection(codonSelection);
            }
            return true;
        }

        /** @return null if codon doesnt exist for trans start */
        private SelectionItem getStartSelectionItem(Transcript t) {
            if (!t.hasTranslationStart()) return null;
            return getSelectionItem(t.getTranslationStart(), true);
        }

        /** @return null if codon doesnt exist for trans stop */
        private SelectionItem getStopSelectionItem(Transcript t) {
            if (!t.hasTranslationEnd()) return null;
            return getSelectionItem(t.getTranslationEnd(), false);
        }

        /** @return null if codon doesnt exist for trans start/stop
        @param getStart - if true get start codon, false stop */
        private SelectionItem getSelectionItem(int basepair, boolean getStart) {
            if (outOfRange(basepair)) return null;
            Sites sites = getSitesForBasepair(basepair, getStart);
            if (sites == null || !sites.siteExistsAtPosition(basepair)) return null;
            String type = getTypeString(basepair, getStart);
            String prefix = (getStart ? "start" : "stop") + " codon";
            int low = (isForwardStrand()) ? basepair : basepair - 2;
            int high = low + 2;
            SiteCodon siteCodon = sites.getSiteCodon(low, high, type, getStrand(), prefix);
            SelectionItem selItem = new SelectionItem(SiteView.this, siteCodon.getFeature());
            selItem.addSelectionListener(siteCodon);
            return selItem;
        }

        private boolean outOfRange(int basepair) {
            return (basepair < curation.getLow()) || (basepair > curation.getHigh());
        }

        private Sites getSitesForBasepair(int basepair, boolean getStart) {
            if (outOfRange(basepair)) return null;
            int sitesIndex = getFrame(basepair);
            if (!getStart) sitesIndex += 3;
            if (sitesIndex >= sitesArray.length) sitesIndex = sitesArray.length - 1;
            if (sitesIndex < 0) {
                logger.error("getSitesForBasepair: can't find sites for requested basepair " + basepair);
                return null;
            }
            return sitesArray[sitesIndex];
        }

        /** This is the String that is used to match up with the tier in the tiers file,
  important to get it the same. A bit dangerous as the string in the tiers
  can be easily modified.
    */
        private String getTypeString(int basepair, boolean start) {
            int frame = getFrame(basepair) + 1;
            String startStop = start ? "start" : "stop";
            return startStop + "codon_frame" + frame;
        }

        /**	Return unique set of transcripts of all exons, trans and genes in 
  annots vector */
        private HashSet getTranscripts(Selection sel) {
            if (resultView == null) {
                logger.error("SiteView has null resultView");
                return null;
            } else if (resultView.getAnnotationView() == null) {
                logger.error("SiteView's resultView has null AnnotationView");
                return null;
            }
            FeatureSetI top = resultView.getAnnotationView().getGeneHolder();
            boolean checkForRedundantDescendants = true;
            Selection annotSel = sel.getSelectionDescendedFromModel(top, checkForRedundantDescendants);
            FeatureList annots = annotSel.getConsolidatedFeatures();
            HashSet trans = new HashSet(annots.size());
            for (int i = 0; i < annots.size(); i++) {
                SeqFeatureI ann = annots.getFeature(i);
                if (ann.isTranscript()) trans.add(ann); else if (isTopLevelAnnot(ann)) trans.addAll(ann.getFeatures());
            }
            return trans;
        }
    }

    /** Returns true if annot is top level - e.g. gene, not trans or exon */
    private boolean isTopLevelAnnot(SeqFeatureI annot) {
        if (!annot.hasAnnotatedFeature()) return false;
        return annot.getAnnotatedFeature().isAnnotTop();
    }

    public Selection getViewSelection(Selection selection) {
        Selection view_selection = new Selection();
        Vector selected = selection.getSelected();
        int sel_count = selected.size();
        for (int i = 0; i < sel_count; i++) {
            SelectionItem si = (SelectionItem) selected.elementAt(i);
            if (si.getSource() == this) {
                view_selection.add(si);
            }
        }
        return view_selection;
    }
}
