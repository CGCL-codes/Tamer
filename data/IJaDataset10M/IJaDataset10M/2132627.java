package apollo.gui.detailviewers.exonviewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.text.*;
import apollo.datamodel.*;
import apollo.main.Apollo;
import apollo.seq.*;
import apollo.config.Config;
import apollo.util.*;
import org.apache.log4j.*;
import org.bdgp.util.RangeHash;
import org.bdgp.swing.FastTranslatedGraphics;
import apollo.gui.BaseScrollable;

/** Pos refers to row/column position, basePair refers to the actual base pair,
 * on forward strand basePair = pos + lowestBase
 */
public class SeqAlignPanel extends JPanel implements Scrollable, BaseScrollable {

    protected static final Logger logger = LogManager.getLogger(SeqAlignPanel.class);

    public static final int NO_BOUNDARY = 0;

    public static final int LEFT_BOUNDARY = 1;

    public static final int RIGHT_BOUNDARY = 2;

    public static final int NO_TYPE = -1;

    public static final int STOP_CODON = 1;

    public static final int START_CODON = 2;

    public static final int INTRON = 4;

    public static final int EXON = 5;

    /** Holds a sequence (SeqWrapper) for every tier, element # is tier number */
    protected Vector sequences;

    protected JComponent defaultRowHeader;

    public int cols;

    protected int stripeWidth = 5;

    protected int rowMargin = 10;

    protected boolean horizontalMode = false;

    public boolean reverseStrand = false;

    private AutoscrollThread horizontalScrollThread;

    private AutoscrollThread verticalScrollThread;

    private boolean autoscroll = false;

    /** The base pair the sequence starts at in outside world(chromosomal) */
    protected int lowestBase;

    protected int highestBase;

    protected int longest_seq = 0;

    public void setAutoscroll(boolean autoscroll) {
        this.autoscroll = autoscroll;
        if (autoscroll) {
            ScrollMouseListener listener = new ScrollMouseListener();
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }
    }

    public void setReverseStrand(boolean in) {
        reverseStrand = in;
    }

    public boolean getReverseStrand() {
        return reverseStrand;
    }

    public void setStripeWidth(int width) {
        stripeWidth = width;
    }

    public int getRowMargin() {
        return rowMargin;
    }

    public int getTierCount() {
        return sequences.size();
    }

    public void addSequence(SequenceI seq, int phase, String seq_type, int index) {
        if (longest_seq == 0 || (seq.getRange() != null && seq.getRange().getHigh() > longest_seq) || (seq.getRange() == null && seq.getLength() > longest_seq)) {
            if (seq.getRange() != null) longest_seq = seq.getRange().getHigh(); else longest_seq = seq.getLength();
        }
        if (index >= sequences.size()) sequences.addElement(new SeqWrapper(seq, phase, seq_type, this)); else sequences.insertElementAt(new SeqWrapper(seq, phase, seq_type, this), index);
    }

    public void attachRendererToSequence(int seqIndex, BaseRenderer br) {
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(seqIndex);
        sw.setRenderer(br);
    }

    protected BaseRenderer renderer = new DefaultBaseRenderer(getCharWidth(), getCharHeight());

    /** Inner class for row numbering, BaseEditorPanel overides this
      not sure how this hooks up with drawing heirarchy */
    public class DefaultRowHeader extends JPanel {

        protected int low = 1;

        /** This is where the row numbers JLabels get created */
        public void addNotify() {
            removeAll();
            GridLayout layout = new GridLayout(getRowCount(), 1, 0, 0);
            setLayout(layout);
            Font font = new Font("Dialog", Font.PLAIN, 12);
            setPreferredSize(new Dimension(66, getRowCount() * (getRowHeight() + getRowMargin())));
            setSize(getPreferredSize());
            for (int i = 0; i < getRowCount(); i++) {
                JLabel b = new JLabel("" + (i * getColumnCount() + low + lowestBase));
                b.setFont(font);
                add(b);
            }
            super.addNotify();
        }

        public void setLowest(int low) {
            this.low = low;
        }
    }

    protected boolean isOneLevelAnnot(SeqFeatureI ann) {
        return ann.isAnnotTop() && !ann.hasKids();
    }

    public int getBoundaryType(int pos, int tier) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        if (sw == null) return NO_BOUNDARY; else return sw.getBoundaryType(pos);
    }

    public SeqFeatureI getFeatureAtPosition(int position, int tier) {
        if (tier >= sequences.size()) return null;
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(tier);
        return sw.getFeatureAtPosition(position);
    }

    public Vector getFeaturesInRange(int startpos, int endpos, int tier) {
        if (tier >= sequences.size()) return null;
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(tier);
        return sw.getFeaturesInRange(startpos, endpos);
    }

    public SeqFeatureI getFeatureForPixelPosition(int x, int y) {
        return getFeatureForPixelPosition(x, y, false);
    }

    public SeqFeatureI getFeatureForPixelPosition(int x, int y, boolean noSet) {
        int tier = getTierForPixelPosition(y);
        int row = getRowForPixelPosition(y);
        int col = getColForPixelPosition(x);
        int pos = cols * row + col + 1;
        if (noSet) return getFeatureAtPosition(pos, tier); else return getFeatureSetAtPosition(pos, tier);
    }

    public FeatureSetI getFeatureSetAtPosition(int position, int tier) {
        if (tier >= sequences.size()) return null;
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(tier);
        return sw.getFeatureSetAtPosition(position);
    }

    protected SeqWrapper getSeqWrapperForTier(int tier) {
        if (tier >= 0 && tier < sequences.size()) return (SeqWrapper) sequences.elementAt(tier); else return null;
    }

    public int getTypeAtPosition(int tier, int base) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        return sw.getTypeAtPosition(base);
    }

    public double[] getRangeAtPosition(int tier, int pos) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        if (sw == null) return null; else return sw.getRangeAtPosition(pos);
    }

    public int getRangeIndex(int tier, int low, int high) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        return sw.getRangeIndex(low, high);
    }

    public int getExonRangeIndex(int tier, int low, int high) {
        return getExonRangeIndex(tier, low, high, false);
    }

    public int getExonRangeIndex(int tier, int low, int high, boolean exact) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        return sw.getExonRangeIndex(low, high, exact);
    }

    public SeqFeatureI getFeatureAtIndex(int tier, int index) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        return sw.getFeatureAtIndex(index);
    }

    public int getFeatureCount(int tier) {
        SeqWrapper sw = getSeqWrapperForTier(tier);
        return sw.getFeatureCount();
    }

    /** Need limits for offset of sequence */
    public SeqAlignPanel(int cols, int lowestBase, int highestBase) {
        super();
        this.cols = cols;
        horizontalMode = false;
        sequences = new Vector();
        longest_seq = 0;
        defaultRowHeader = new DefaultRowHeader();
        attachListeners();
        this.lowestBase = lowestBase;
        this.highestBase = highestBase;
    }

    public void setHorizontalMode(boolean mode) {
        this.horizontalMode = mode;
    }

    private void attachListeners() {
        if (autoscroll) {
            ScrollMouseListener listener = new ScrollMouseListener();
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }
    }

    public JComponent getDefaultHeader() {
        return defaultRowHeader;
    }

    /** Checks if there are any overlaps between features in sw and feature,
   * if so return false, else true
   */
    protected boolean canBeAddedToSequence(SeqWrapper sw, SeqFeatureI feature) {
        Vector features = sw.getFeatures();
        for (int i = 0; i < features.size(); i++) {
            FeatureWrapper fw = (FeatureWrapper) features.elementAt(i);
            SeqFeatureI currentFeature = fw.getFeature();
            if (feature.contains(currentFeature.getLow()) || feature.contains(currentFeature.getHigh()) || currentFeature.contains(feature.getHigh()) || currentFeature.contains(feature.getLow())) return false;
        }
        return true;
    }

    public void clear() {
        sequences.removeAllElements();
        longest_seq = 0;
    }

    /** Adds feature to an existing SeqWrapper if it doesnt overlap with it,
   * otherwise creates a new SeqWrapper for it,
   * @return The element number in sequences,
   * which is synonomous with tier number
   */
    public int addAlignFeature(SequenceI seq, SeqFeatureI feature) {
        SeqWrapper wrapper = null;
        int seqNum = -1;
        for (int seqIndex = 0; seqIndex < sequences.size(); seqIndex++) {
            SeqWrapper sw = (SeqWrapper) sequences.elementAt(seqIndex);
            if (sw.getSequence() == seq && canBeAddedToSequence(sw, feature)) {
                wrapper = sw;
                seqNum = seqIndex;
                break;
            }
        }
        if (wrapper == null) {
            seqNum = sequences.size();
            addSequence(seq, -1, SequenceI.DNA, seqNum);
            attachRendererToSequence(seqNum, new DefaultBaseRenderer(getCharWidth(), getCharHeight()));
            wrapper = (SeqWrapper) sequences.elementAt(seqNum);
        }
        wrapper.addFeature(feature);
        return seqNum;
    }

    /** Removes feature from an existing SeqWrapper
   */
    protected void removeFeature(FeatureSetI feature, int tier) {
        if (tier >= sequences.size()) {
            logger.warn("SeqAlignPanel.removeFeature: requested tier " + tier + " is bigger than max tier " + sequences.size());
            return;
        }
        if (tier == -1) {
            logger.error("EDE trying to remove feature that it can't find (possible cloning issue)!", new Throwable());
            return;
        }
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(tier);
        sw.removeFeature(feature);
        if (sw.getFeatureCount() == 0) {
            sequences.removeElement(sw);
        }
    }

    public void moveSequence(int oldpos, int newpos) {
        SeqWrapper sequence = (SeqWrapper) sequences.elementAt(oldpos);
        sequences.removeElementAt(oldpos);
        sequences.insertElementAt(sequence, newpos);
        reformat();
    }

    public int getSequenceCount() {
        return sequences.size();
    }

    public char getCharAt(int tier, int pos) {
        return ((SeqWrapper) sequences.elementAt(tier)).getCharAt(pos);
    }

    public SequenceI getSequenceForTier(int tier) {
        return ((SeqWrapper) sequences.elementAt(tier)).getSequence();
    }

    public void setVisibilityForSequence(int seqIndex, boolean visible) {
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(seqIndex);
        sw.setVisible(visible);
    }

    public BaseRenderer getRendererAt(int seqIndex) {
        if (seqIndex < 0 || seqIndex >= sequences.size()) seqIndex = 0;
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(seqIndex);
        return sw.getRenderer();
    }

    public void attachFeatureToSequence(int seqIndex, SeqFeatureI sf) {
        SeqWrapper sw = (SeqWrapper) sequences.elementAt(seqIndex);
        sw.addFeature(sf);
    }

    /** Returns tier number that SeqFeatureI is in. SeqFeatureI can be an exon
      or a transcript as it searches on both the feature itself and its 
      refFeature. */
    protected int getTierForFeature(SeqFeatureI sf) {
        if (sf == null) {
            logger.error("getTierForFeature called on null SeqFeatureI", new Throwable());
            return -1;
        }
        for (int i = 0; i < sequences.size(); i++) {
            SeqWrapper sw = (SeqWrapper) sequences.elementAt(i);
            Vector tier_features = sw.getFeatures();
            for (int j = 0; j < tier_features.size(); j++) {
                SeqFeatureI tier_feature = ((FeatureWrapper) tier_features.elementAt(j)).getFeature();
                if (tier_feature == sf || tier_feature == sf.getRefFeature()) {
                    return i;
                }
                if (tier_feature.isClone() && tier_feature.getCloneSource() == sf || sf.isClone() && sf.getCloneSource() == tier_feature) {
                    return i;
                }
                if (tier_feature.isIdentical(sf)) {
                    logger.debug("EDE found clone of feature, not actual feature." + " Double check this is right. " + sf.getName(), new Throwable());
                    return i;
                }
            }
        }
        logger.error("EDE failed to find tier for feature " + sf.getName() + ", returning -1", new Throwable());
        return -1;
    }

    protected static String padPerChar(String target, String padString) {
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < target.length(); i++) {
            out.append(target.charAt(i) + padString);
        }
        return out.toString();
    }

    public int getCharHeight() {
        return getFont().getSize();
    }

    public int getLongestSeq() {
        return longest_seq;
    }

    /** 
   * This fixes a resize bug on solaris and linux. When the ede was resizing 
   sometimes the SeqAlignPanel was not resizing. I dont understand this but
   in the JViewPort/Container.validateTree it would call either validate() or
   validateTree() on SeqAlignPanel depending on if it was valid. It is was valid
   validate was called, and validate below caused a reformat and everything was ok.
   If it was not valid, validateTree was called which previously just went to the
   superclass and the needed reformat did not get called. So I added this 
   validateTree that just emulates validate. Why it sometimes is valid and sometimes
   not on resize I dont know. But this did fix the bug. Very strange. 
      
   This doesnt seem to make a difference in terms of the 1.4 jdk bug, but it seems
   like if we are commenting out validate we should also comment out validateTree
   
*/
    public void validateTree() {
        int visible = getVisibleBase();
        reformat();
        super.validateTree();
        scrollToBase(visible);
    }

    public void validate() {
        int visible = getVisibleBase();
        reformat();
        super.validate();
        scrollToBase(visible);
    }

    protected void reformat() {
        Component c = getParent();
        int width = -1;
        int height = -1;
        if (horizontalMode) cols = getLongestSeq();
        if (c != null && c instanceof JViewport) {
            Component sc = c.getParent();
            height = sc.getSize().height;
            if (sc instanceof JScrollPane) {
                if (((JScrollPane) sc).getHorizontalScrollBarPolicy() == JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
                    horizontalMode = false;
                    width = ((JViewport) c).getViewRect().width;
                    cols = width / getCharWidth();
                    if (cols < 1) cols = 100;
                }
            }
        }
        int desiredHeight = getRowCount() * (getRowHeight() + getRowMargin());
        int desiredWidth = cols * getCharWidth();
        height = desiredHeight;
        if (logger.isDebugEnabled()) {
            logger.debug("width,height=" + width + "," + height);
            logger.debug("Desired=" + desiredWidth + "," + desiredHeight);
            logger.debug("CurrentPreferred=" + getPreferredSize().width + "," + getPreferredSize().height);
            logger.debug("CurrentSize=" + getSize().width + "," + getSize().height);
        }
        if ((desiredWidth != getPreferredSize().width) && (desiredHeight != getPreferredSize().height)) {
            logger.debug("Setting size and preferredsize.");
            setPreferredSize(new Dimension(desiredWidth, desiredHeight));
            if (width == -1) setSize(getPreferredSize()); else setSize(desiredWidth, getPreferredSize().height);
        }
    }

    public void addNotify() {
        Component c = getParent();
        if (c != null && c instanceof JViewport) {
            Component sc = c.getParent();
            if (sc instanceof JScrollPane) {
                if (((JScrollPane) sc).getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_NEVER) {
                    horizontalMode = true;
                }
            }
        }
        reformat();
        super.addNotify();
    }

    public int getRowHeight() {
        return getCharHeight() * sequences.size();
    }

    public int getCharWidth() {
        return getFont().getSize();
    }

    public Rectangle getRectangleForCharPosition(int pos, int tier) {
        Point p = getPixelLocForPosition(pos, tier);
        return new Rectangle(p.x, p.y, getCharWidth(), getCharHeight());
    }

    /** @param pos row/col coord, not bp */
    public Point getPixelLocForPosition(int pos, int tier) {
        if (tier == -1) tier = sequences.size() - 1;
        int colNum = getColForPosition(pos);
        int rowNum = getRowForPosition(pos);
        int tierHeight = getRowHeight();
        int x = colNum * getCharWidth();
        int y = rowNum * (tierHeight + getRowMargin()) + tier * getCharHeight();
        return new Point(x, y);
    }

    public Rectangle getRectangleForTierPosition(int pos) {
        Point p = getPixelLocForPosition(pos, 0);
        return new Rectangle(p.x, p.y, getCharWidth(), getRowHeight());
    }

    public int getRowForPosition(int pos) {
        return pos / cols;
    }

    public int getColumnCount() {
        return cols;
    }

    public int getRowCount() {
        int length = residueToPos(getLongestSeq()) + 1;
        return (int) Math.ceil((double) length / (double) cols);
    }

    public int getColForPosition(int pos) {
        return pos % cols;
    }

    public int getRowForPixelPosition(int y) {
        int rowNum = y / (getRowHeight() + getRowMargin());
        return rowNum;
    }

    public int getTierForPixelPosition(int y) {
        int loc = (y % (getRowHeight() + getRowMargin())) / getCharHeight();
        return loc;
    }

    protected Point getPixelLocForColAndRow(int col, int row) {
        return new Point(col * getCharWidth(), row * (getRowHeight() + getRowMargin()));
    }

    public void scrollToRow(int row) {
        JViewport view = SwingMissingUtil.getViewportAncestor(this);
        if (view != null) {
            Point point = getPixelLocForColAndRow(0, row);
            if (point.x + view.getViewRect().width > getSize().width) point.x = getSize().width - view.getViewRect().width;
            if (point.x + view.getViewRect().height > getSize().height) point.y = getSize().height - view.getViewRect().height;
            view.setViewPosition(point);
        }
    }

    /** From BaseScrollable interface. Returns basepair that SeqAlignPanel is
      currently at. SZAP queries this to know which base to highlight */
    public int getVisibleBase() {
        JViewport view = SwingMissingUtil.getViewportAncestor(this);
        if (view == null) return -1;
        Rectangle r = view.getViewRect();
        int row = getRowForPixelPosition(r.y);
        int col = getColForPixelPosition(r.x);
        int pos = col + row * getColumnCount();
        return posToBasePair(pos);
    }

    /** Takes a row/col 1 based pos and converts to global base pairs */
    public int posToBasePair(int pos) {
        if (reverseStrand) return highestBase - pos; else return pos + lowestBase;
    }

    public int basePairToPos(int basePair) {
        if (reverseStrand) return highestBase - basePair; else return basePair - lowestBase;
    }

    /** Takes a row/col zero based pos and converts it to offset
      into the dna/aa string */
    public int posToResidue(int pos) {
        if (reverseStrand) return pos; else return pos + lowestBase;
    }

    /** Takes a row/col zero based pos and converts it to offset
      into the dna/aa string */
    public int residueToPos(int residue) {
        if (reverseStrand) return residue; else return residue - lowestBase;
    }

    /** takes a zero based string offset for the residues
      and converts it to an offset in zero based pos space
  */
    public int residueToBasePair(int residue) {
        if (reverseStrand) return residue; else return residue - lowestBase;
    }

    public int getLowestBase() {
        return (int) lowestBase;
    }

    public int getHighestBase() {
        return (int) highestBase;
    }

    public int getVisibleBaseCount() {
        JViewport view = SwingMissingUtil.getViewportAncestor(this);
        if (view == null) return -1;
        Rectangle r = view.getViewRect();
        int height = (horizontalMode ? 0 : getRowForPixelPosition(r.height));
        int width = getColForPixelPosition(r.width);
        return getColumnCount() * height + width;
    }

    /** from BaseScrollable interface, there is both a scrollToBase and a
      scrollToPos.
      scrollToBase calls scrollToPosition after transforming into
      window sequence coordinates
  */
    public void scrollToBase(int basepair) {
        scrollToPosition(basePairToPos(basepair));
    }

    public void scrollToPosition(int pos) {
        JViewport view = SwingMissingUtil.getViewportAncestor(this);
        if (view != null) {
            Point point = getPixelLocForPosition(pos, 0);
            if (point.x + view.getViewRect().width > getSize().width) point.x = getSize().width - view.getViewRect().width;
            if (!horizontalMode && point.y + view.getViewRect().height > getSize().height) {
                point.y = getSize().height - view.getViewRect().height;
            }
            view.setViewPosition(point);
        }
    }

    public void scrollToFeature(SeqFeatureI feature) {
        JViewport view = SwingMissingUtil.getViewportAncestor(this);
        if (feature != null && view != null) {
            Rectangle r = view.getViewRect();
            int first_row = getRowForPixelPosition(r.y);
            int first_col = getColForPixelPosition(r.x);
            int first_pos = first_col + (first_row * getColumnCount());
            int first_base = posToBasePair(first_pos);
            int last_row = getRowForPixelPosition(r.y + r.height);
            int last_col = getColForPixelPosition(r.x + r.width);
            int last_pos = last_col + (last_row * getColumnCount());
            int last_base = posToBasePair(last_pos);
            if ((!reverseStrand && (feature.getStart() > last_base || feature.getEnd() < first_base)) || (reverseStrand && (feature.getStart() < first_base || feature.getEnd() > last_base))) scrollToBase((int) feature.getStart());
        }
    }

    public int getColForPixelPosition(int x) {
        int colNum = x / getCharWidth();
        return colNum;
    }

    /** Converts a pixel rectangle to a pos(row/col) rectangle */
    public Rectangle getPosRectangleForPixels(Rectangle r) {
        int rowStart = getRowForPixelPosition(r.y);
        int rowEnd = getRowForPixelPosition(r.y + r.height) - rowStart + 1;
        int colStart = getColForPixelPosition(r.x);
        int colEnd = getColForPixelPosition(r.x + r.width) - colStart + 1;
        return new Rectangle(colStart, rowStart, colEnd, rowEnd);
    }

    public void repaint(int startPos, int endPos) {
        if (endPos < startPos) {
            int temp = endPos;
            endPos = startPos;
            startPos = temp;
        }
        endPos = endPos + 1;
        int startCol = getColForPosition(startPos);
        int startRow = getRowForPosition(startPos);
        int endCol = getColForPosition(endPos);
        int endRow = getRowForPosition(endPos);
        if (endRow < startRow) {
            int t = endRow;
            endRow = startRow;
            startRow = t;
        }
        if (endCol < startCol) {
            int t = endCol;
            endCol = startCol;
            startCol = t;
        }
        if (startRow == endRow) {
            repaint(startCol, startRow, endCol - startCol, 1);
        } else {
            repaint(0, startRow, cols, endRow - startRow + 1);
        }
    }

    public void repaint(int x, int y, int width, int height) {
        Point start = getPixelLocForColAndRow(x, y);
        int drawWidth = width * getCharWidth();
        int drawHeight = height * (getRowHeight() + getRowMargin());
        RepaintManager.currentManager(this).addDirtyRegion(this, start.x, start.y, drawWidth, drawHeight);
    }

    /** called by paint(Graphics), x,y,width,height are in
   * row/column numbers, not pixels!  paint(Graphics) converts
   * pixels to coords via getPosRectangleForPixels
   * @param x visible start column,
   * @param y visible start row
   * @param width number of visible columns
   * @param height number of visible rows
   * column and row does not necasarily correspond with base pair if the
   * sequence is offset
   */
    private void paint(Graphics g, int x, int y, int width, int height) {
        int charWidth = getCharWidth();
        FastTranslatedGraphics transG = new FastTranslatedGraphics(g);
        for (int j = y; j < y + height; j++) {
            for (int i = x; i < x + width; i++) {
                g.setFont(getFont());
                int pos = i + j * cols;
                g.setColor(getBackgroundColor(i));
                Point p = getPixelLocForPosition(pos, 0);
                g.fillRect(p.x, p.y, charWidth, getRowHeight() + getRowMargin());
                for (int n = 0; n < sequences.size(); n++) {
                    SeqWrapper sw = (SeqWrapper) sequences.elementAt(n);
                    if (sw.isVisible()) {
                        char ch = sw.getCharAt(pos);
                        if (ch != '\0') {
                            Component c = sw.getRenderer().getBaseRendererComponent(ch, pos, n, sw.getSequence());
                            Point pl = getPixelLocForPosition(pos, n);
                            transG.translateAbsolute(pl.x, pl.y);
                            c.paint(transG);
                        }
                    }
                }
            }
        }
    }

    static int i = 0;

    public void paint(Graphics g) {
        super.paint(g);
        Rectangle clipBounds = g.getClipBounds();
        Rectangle clipPos = getPosRectangleForPixels(clipBounds);
        int x = clipPos.x;
        int y = clipPos.y;
        int width = clipPos.width;
        int height = clipPos.height;
        paint(g, x, y, width, height);
    }

    /** Return background color for the column */
    private Color getBackgroundColor(int column) {
        if ((column % (stripeWidth * 2)) >= stripeWidth) return getBackgroundColor2(); else return getBackgroundColor1();
    }

    /** For now these just get configs ede bg color - this means alignment
      viewer gets same colors as ede, i alignment wants to be
      different needs to override these methods */
    protected Color getBackgroundColor1() {
        return Config.getExonDetailEditorBackgroundColor1();
    }

    protected Color getBackgroundColor2() {
        return Config.getExonDetailEditorBackgroundColor2();
    }

    protected Graphics createGraphics(Graphics g, int x, int y, int w, int h) {
        Graphics out = g.create();
        out.translate(x, y);
        return out;
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == Adjustable.HORIZONTAL) return (visibleRect.width * 3) / 4; else return visibleRect.height;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == Adjustable.HORIZONTAL) return getCharWidth(); else if (horizontalMode) return getCharHeight(); else return getRowHeight() + getRowMargin();
    }

    protected class ScrollMouseListener extends javax.swing.event.MouseInputAdapter {

        public void mouseReleased(MouseEvent e) {
            haltThread(verticalScrollThread);
            haltThread(horizontalScrollThread);
        }

        public void mouseDragged(MouseEvent e) {
            handleAutoscroll(e.getX(), e.getY());
        }
    }

    protected class AutoscrollThread extends Thread implements Directions {

        private int timeInterval;

        private int distance;

        private JViewport viewport;

        private Component comp;

        private int direction;

        protected boolean halt;

        public AutoscrollThread(int timeInterval, int distance, JViewport viewport, int direction) {
            this.timeInterval = timeInterval;
            this.distance = distance;
            this.viewport = viewport;
            this.direction = direction;
            this.comp = viewport.getView();
            halt = false;
        }

        public void run() {
            Apollo.setLog4JDiagnosticContext();
            do {
                Point p = viewport.getViewPosition();
                int x = (int) p.getX();
                int y = (int) p.getY();
                int verticaloffset = 0;
                int horizontaloffset = 0;
                if (direction == NORTH) {
                    verticaloffset = -distance;
                } else if (direction == SOUTH) {
                    verticaloffset = distance;
                } else if (direction == EAST) {
                    horizontaloffset = -distance;
                } else if (direction == WEST) {
                    horizontaloffset = distance;
                }
                x = x + horizontaloffset;
                y = y + verticaloffset;
                Dimension scrollerSize = viewport.getSize();
                Dimension viewSize = viewport.getView().getSize();
                if (x >= 0 && y >= 0 && x + scrollerSize.getWidth() <= viewSize.getWidth() && y + scrollerSize.getHeight() <= viewSize.getHeight()) {
                    Point newPos = new Point(x, y);
                    viewport.setViewPosition(newPos);
                    comp.repaint();
                } else {
                    halt = true;
                    return;
                }
                try {
                    sleep(timeInterval);
                } catch (InterruptedException e) {
                    halt = true;
                    return;
                }
            } while (!halt);
            Apollo.clearLog4JDiagnosticContext();
        }

        public boolean getHalt() {
            return halt;
        }

        public void halt() {
            halt = true;
        }
    }

    protected void haltThread(AutoscrollThread thread) {
        if (thread == null) {
            return;
        }
        if (thread.getHalt()) {
            return;
        }
        thread.halt();
        thread.interrupt();
    }

    protected void handleAutoscroll(int x, int y) {
        int threshold = 20;
        int interval = 100;
        int distance = getCharHeight();
        Component parent = getParent();
        if (parent != null && parent instanceof JViewport) {
            JViewport view = (JViewport) parent;
            Rectangle rect = view.getViewRect();
            boolean upperHotspot = (y < rect.getY() + threshold);
            boolean leftHotspot = (x < rect.getX() + threshold);
            boolean lowerHotspot = (y > rect.getY() + rect.getHeight() - threshold);
            boolean rightHotspot = (x > rect.getX() + rect.getWidth() - threshold);
            if (!leftHotspot && !rightHotspot) {
                haltThread(horizontalScrollThread);
            }
            if (!upperHotspot && !lowerHotspot) {
                haltThread(verticalScrollThread);
            }
            if (leftHotspot) {
                haltThread(horizontalScrollThread);
                horizontalScrollThread = new AutoscrollThread(interval, distance, view, AutoscrollThread.EAST);
                horizontalScrollThread.start();
            }
            if (rightHotspot) {
                haltThread(horizontalScrollThread);
                horizontalScrollThread = new AutoscrollThread(interval, distance, view, AutoscrollThread.WEST);
                horizontalScrollThread.start();
            }
            if (upperHotspot) {
                haltThread(verticalScrollThread);
                verticalScrollThread = new AutoscrollThread(interval, distance, view, AutoscrollThread.NORTH);
                verticalScrollThread.start();
            }
            if (lowerHotspot) {
                haltThread(verticalScrollThread);
                verticalScrollThread = new AutoscrollThread(interval, distance, view, AutoscrollThread.SOUTH);
                verticalScrollThread.start();
            }
        }
    }
}
