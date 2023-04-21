package apollo.gui.detailviewers.sequencealigner.renderers;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import apollo.datamodel.ExonI;
import apollo.datamodel.FeatureSet;
import apollo.datamodel.FeatureSetI;
import apollo.datamodel.SeqFeatureI;
import apollo.datamodel.SequenceI;
import apollo.datamodel.Transcript;
import apollo.datamodel.TranslationI;
import apollo.gui.detailviewers.sequencealigner.Orientation;
import apollo.gui.detailviewers.sequencealigner.ReadingFrame;
import apollo.gui.detailviewers.sequencealigner.RegionSelectableI;
import apollo.gui.detailviewers.sequencealigner.SequenceType;
import apollo.gui.detailviewers.sequencealigner.Strand;
import apollo.gui.detailviewers.sequencealigner.TierI;
import apollo.gui.detailviewers.sequencealigner.TierI.Level;
import apollo.gui.drawable.DrawableSeqFeature;
import apollo.gui.drawable.DrawableUtil;
import apollo.gui.synteny.GuiCurationState;

public class AnnotationRenderer extends TestBaseRenderer implements RegionSelectableI {

    private static enum State {

        MISS, INTRON, EXON, OUT, X, SELECTED, SHIFT, ERROR, TRANSLATION_START, TRANSLATION_END, UTR
    }

    ;

    private static final Map<State, Color> bgColorMap = new HashMap<State, Color>();

    private static final Map<State, Color> lineColorMap = new HashMap<State, Color>();

    static {
        bgColorMap.put(State.MISS, Color.LIGHT_GRAY);
        bgColorMap.put(State.INTRON, Color.black);
        bgColorMap.put(State.UTR, Color.black);
        bgColorMap.put(State.EXON, Color.blue);
        bgColorMap.put(State.OUT, Color.black);
        bgColorMap.put(State.X, Color.pink);
        bgColorMap.put(State.SELECTED, Color.cyan);
        bgColorMap.put(State.SHIFT, Color.ORANGE);
        bgColorMap.put(State.ERROR, Color.pink);
        bgColorMap.put(State.TRANSLATION_START, Color.green);
        bgColorMap.put(State.TRANSLATION_END, Color.red);
        bgColorMap.put(State.UTR, new Color(70, 130, 180));
    }

    ;

    static {
        lineColorMap.put(State.INTRON, Color.blue);
        lineColorMap.put(State.UTR, new Color(70, 130, 180));
    }

    private State state;

    private int selectionStart, selectionEnd;

    private GuiCurationState curationState;

    private Transcript currentParent;

    private Transcript currentParentClone;

    private Transcript currentParentClone2;

    private int currentStart;

    private int featureStart;

    private int featureEnd;

    private Transcript transcript;

    public AnnotationRenderer(GuiCurationState curationState) {
        super();
        this.curationState = curationState;
        this.state = State.OUT;
        selectionStart = -1;
        selectionEnd = -1;
        transcript = null;
        currentParent = null;
        currentParentClone = null;
        currentParentClone2 = null;
        currentStart = -1;
        featureStart = -1;
        featureEnd = -1;
    }

    /**
   * Get component to be rendered, if pos outside of current range
   * getFeatureAtPosition and reset currentRange, if feature is non null and not
   * an instance of FeatureSetI then its an exon, and set isExon flag
   */
    public Component getBaseComponent(int position, TierI tier, Orientation o) {
        transcript = null;
        int bp = tier.getBasePair(position);
        int oldPos = position;
        if (tier.getType() == SequenceType.AA) {
            int offset = getOffset(position, tier);
            bp += offset * tier.getStrand().toInt();
            position = tier.getPosition(bp);
        }
        char base = tier.charAt(position);
        if (tier.featureExsitsAt(position, TierI.Level.BOTTOM)) {
            this.state = State.EXON;
            SeqFeatureI feature = tier.featureAt(position, TierI.Level.BOTTOM);
            Transcript parentClone = null;
            Transcript otherParentClone = null;
            if (tier.getType() == SequenceType.AA && feature.getParent() != null && feature.getParent().isTranscript()) {
                FeatureSetI parent = feature.getParent();
                int index = parent.getFeatureIndex(feature);
                int start = parent.getEnd();
                if (parent.canHaveChildren()) {
                    for (int i = 0; i < parent.size(); i++) {
                        SeqFeatureI sf = parent.getFeatureAt(i);
                        if ((Strand.valueOf(sf.getStrand()) == Strand.FORWARD && sf.getStart() < start) || (Strand.valueOf(sf.getStrand()) == Strand.REVERSE && sf.getStart() > start)) start = sf.getStart();
                    }
                }
                int translationStart = parent.getTranslationStart();
                int translationEnd = parent.getTranslationEnd();
                int translationPos = parent.getFeaturePosition(translationStart);
                int oldstart = start;
                start += ((translationPos - 1) % 3) * parent.getStrand();
                if (feature.getParent() == currentParent && start == currentStart && featureStart == feature.getStart() && featureEnd == feature.getEnd()) {
                    parentClone = currentParentClone;
                    otherParentClone = currentParentClone2;
                } else {
                    parentClone = (Transcript) feature.getParent().clone();
                    otherParentClone = (Transcript) feature.getParent().clone();
                    if (!parentClone.setTranslationStart(start)) {
                        start = oldstart;
                        TranslationI cds = otherParentClone.getTranslation();
                        cds.calcTranslationStartForLongestPeptide();
                        translationStart = otherParentClone.getTranslationStart();
                        translationPos = otherParentClone.getFeaturePosition(translationStart);
                        start += ((translationPos - 1) % 3) * otherParentClone.getStrand();
                        parentClone.setTranslationStart(start);
                    }
                    parentClone.setTranslationEnd(parentClone.getEnd());
                    parentClone.setPeptideSequence(null);
                    String translation = parentClone.translate();
                    SequenceI sequence = parentClone.getPeptideSequence();
                    currentParent = (Transcript) feature.getParent();
                    currentParentClone = parentClone;
                    currentParentClone2 = otherParentClone;
                    currentStart = start;
                    featureStart = feature.getStart();
                    featureEnd = feature.getEnd();
                    if (otherParentClone.getFeatureContaining(translationEnd) == null) {
                        TranslationI cds = otherParentClone.getTranslation();
                        cds.setTranslationEndFromStart();
                    }
                }
                SeqFeatureI sf = parentClone.getFeatureAt(index);
                if (bp >= sf.getLow() && bp <= sf.getHigh()) {
                    base = getPeptide(bp, sf);
                } else {
                    base = '\0';
                }
                if (otherParentClone != null) {
                    feature = otherParentClone.getFeatureAt(index);
                }
            } else if (tier.getType() == SequenceType.AA && feature.getParent() != null && !feature.getParent().isTranscript()) {
                base = '\0';
            }
            if (isUTR(bp, feature, tier)) {
                this.state = State.UTR;
            }
            if (isTranslationStart(bp, feature)) {
                this.state = State.TRANSLATION_START;
            }
            if (isTranslationEnd(bp, feature)) {
                this.state = State.TRANSLATION_END;
            }
            if (isSequencingErrorPosition(bp, feature, tier.getType())) {
                this.state = State.ERROR;
            }
            if (isShiftPosition(bp, feature, tier.getType())) {
                this.state = State.SHIFT;
            }
        } else if (tier.featureExsitsAt(position, TierI.Level.TOP)) {
            SeqFeatureI feature = tier.featureAt(position, Level.TOP);
            this.state = State.INTRON;
            if (tier.getType() == SequenceType.AA) {
                base = '\0';
            }
            int start = feature.getEnd();
            int end = feature.getStart();
            if (feature.canHaveChildren()) {
                for (int i = 0; i < feature.size(); i++) {
                    SeqFeatureI sf = feature.getFeatureAt(i);
                    if ((Strand.valueOf(sf.getStrand()) == Strand.FORWARD && sf.getStart() < start) || (Strand.valueOf(sf.getStrand()) == Strand.REVERSE && sf.getStart() > start)) start = sf.getStart();
                    if ((Strand.valueOf(sf.getStrand()) == Strand.FORWARD && sf.getEnd() > end) || (Strand.valueOf(sf.getStrand()) == Strand.REVERSE && sf.getEnd() < end)) end = sf.getEnd();
                }
            }
            if (bp <= Math.min(start, end) || bp >= Math.max(start, end)) {
                this.state = State.OUT;
                base = '\0';
            }
        } else {
            this.state = State.OUT;
            base = '\0';
        }
        if (!(state == State.SHIFT || state == State.ERROR) && getRegionLow() <= position && position <= getRegionHigh()) {
            this.state = State.SELECTED;
            base = tier.charAt(oldPos);
        }
        init(base);
        return this;
    }

    public Color getBackgroundBoxColor() {
        Color c = bgColorMap.get(state);
        if (state == State.TRANSLATION_START && transcript != null) {
            c = DrawableUtil.getStartCodonColor(transcript);
        }
        return c;
    }

    public Color getBackgroundLineColor() {
        return lineColorMap.get(state);
    }

    public Color getTextColor() {
        return Color.white;
    }

    public int getRegionEnd() {
        return selectionEnd;
    }

    public int getRegionStart() {
        return selectionStart;
    }

    public void setRegionEnd(int pos) {
        selectionEnd = pos;
    }

    public void setRegionStart(int pos) {
        selectionStart = pos;
    }

    public int getRegionHigh() {
        return Math.max(selectionStart, selectionEnd);
    }

    public int getRegionLow() {
        return Math.min(selectionStart, selectionEnd);
    }

    private boolean isSequencingErrorPosition(int bp, SeqFeatureI feature, SequenceType type) {
        boolean result = false;
        if (type == SequenceType.DNA) {
            result = curationState.getCurationSet().getRefSequence().isSequencingErrorPosition(bp);
        } else {
            result = curationState.getCurationSet().getRefSequence().isSequencingErrorPosition(bp) || curationState.getCurationSet().getRefSequence().isSequencingErrorPosition(bp + feature.getStrand()) || curationState.getCurationSet().getRefSequence().isSequencingErrorPosition(bp + (2 * feature.getStrand()));
        }
        return result;
    }

    private boolean isShiftPosition(int bp, SeqFeatureI feature, SequenceType type) {
        boolean result = false;
        if (feature != null) {
            FeatureSet fs = (FeatureSet) feature.getRefFeature();
            result = fs.plus1FrameShiftPosition() == bp || fs.minus1FrameShiftPosition() == bp;
            if (type == SequenceType.AA) {
                bp += feature.getStrand();
                result = result || fs.plus1FrameShiftPosition() == bp || fs.minus1FrameShiftPosition() == bp;
            }
            if (type == SequenceType.AA) {
                bp += feature.getStrand();
                result = result || fs.plus1FrameShiftPosition() == bp || fs.minus1FrameShiftPosition() == bp;
            }
        }
        return result;
    }

    private boolean isTranslationStart(int bp, SeqFeatureI feature) {
        boolean result = false;
        ExonI exon = null;
        if (feature instanceof ExonI) exon = (ExonI) feature; else if (feature instanceof DrawableSeqFeature) exon = (ExonI) ((DrawableSeqFeature) feature).getFeature();
        if (exon != null && exon.getTranscript() != null) {
            transcript = exon.getTranscript();
            int tss = (int) exon.getTranscript().getTranslationStart();
            result = bp == tss || bp == (tss + exon.getStrand()) || bp == (tss + (2 * exon.getStrand()));
        }
        return result;
    }

    private boolean isTranslationEnd(int bp, SeqFeatureI feature) {
        boolean result = false;
        ExonI exon = null;
        if (feature instanceof ExonI) exon = (ExonI) feature; else if (feature instanceof DrawableSeqFeature) exon = (ExonI) ((DrawableSeqFeature) feature).getFeature();
        if (exon != null && exon.getTranscript() != null) {
            int tes = (int) exon.getTranscript().getTranslationEnd();
            result = bp == tes || bp == (tes + exon.getStrand()) || bp == (tes + (2 * exon.getStrand()));
        }
        return result;
    }

    private boolean isUTR(int bp, SeqFeatureI feature, TierI tier) {
        boolean isStartUTR = false;
        boolean isEndUTR = false;
        ExonI exon = null;
        if (feature instanceof ExonI) exon = (ExonI) feature; else if (feature instanceof DrawableSeqFeature) exon = (ExonI) ((DrawableSeqFeature) feature).getFeature();
        if (exon != null && exon.getTranscript() != null) {
            int utrStart = exon.getTranscript().getTranslationEnd() + (2 * exon.getStrand());
            int utrEnd = exon.getTranscript().getEnd();
            utrStart = tier.getPosition(utrStart);
            utrEnd = tier.getPosition(utrEnd);
            int pos = tier.getPosition(bp);
            isEndUTR = utrStart <= pos && exon.getTranscript().hasTranslationEnd();
            ;
        }
        if (exon != null && exon.getTranscript() != null) {
            int utrStart = exon.getTranscript().getStart();
            int utrEnd = exon.getTranscript().getTranslationStart();
            utrStart = tier.getPosition(utrStart);
            utrEnd = tier.getPosition(utrEnd);
            int pos = tier.getPosition(bp);
            isStartUTR = pos <= utrEnd && exon.getTranscript().hasTranslationStart();
        }
        return isStartUTR || isEndUTR;
    }

    private char getPeptide(int bp, SeqFeatureI sf) {
        char base = '\0';
        SequenceI peptides = null;
        FeatureSetI parent = sf.getParent();
        int pepPos = -1;
        if (parent != null && parent.hasPeptideSequence()) {
            peptides = parent.getPeptideSequence();
            if (parent instanceof FeatureSet) {
                FeatureSet fs = (FeatureSet) parent;
                pepPos = fs.getPeptidePosForGenomicPos(bp);
            }
            if (pepPos >= 0 && pepPos < peptides.getLength()) {
                base = peptides.getBaseAt(pepPos);
            }
        }
        return base;
    }

    /**
   * 
   * @param position
   * @param tier
   * @return
   */
    public static int getOffset(int position, TierI tier) {
        SeqFeatureI sf = null;
        int offset = 0;
        if (tier.featureExsitsAt(position, Level.BOTTOM)) {
            sf = tier.featureAt(position, Level.BOTTOM);
            ReadingFrame exonFrame = ReadingFrame.valueOf(sf.getFrame());
            ReadingFrame tierFrame = tier.getReadingFrame();
            if (exonFrame == ReadingFrame.NONE && sf.getParent() != null && sf.getParent().isTranscript()) {
                FeatureSetI parent = sf.getParent();
                Transcript parentClone = (Transcript) sf.getParent().clone();
                int start = parentClone.getEnd();
                if (parentClone.canHaveChildren()) {
                    for (int i = 0; i < parentClone.size(); i++) {
                        SeqFeatureI f = parentClone.getFeatureAt(i);
                        if ((Strand.valueOf(f.getStrand()) == Strand.FORWARD && f.getStart() < start) || (Strand.valueOf(f.getStrand()) == Strand.REVERSE && f.getStart() > start)) start = f.getStart();
                    }
                }
                int translationStart = parentClone.getTranslationStart();
                int translationPos = parentClone.getFeaturePosition(translationStart);
                int oldstart = start;
                start += ((translationPos - 1) % 3) * parentClone.getStrand();
                parentClone.setTranslationStart(start);
                parentClone.setTranslationEnd(parentClone.getEnd());
                parentClone.setPeptideSequence(null);
                String translation = parentClone.translate();
                SequenceI sequence = parentClone.getPeptideSequence();
                int index = parent.getFeatureIndex(sf);
                exonFrame = ReadingFrame.valueOf(parentClone.getFeatureAt(index).getFrame());
            }
            if (tierFrame != exonFrame && sf.getParent() != null && sf.getParent().isTranscript()) {
                if (tierFrame == ReadingFrame.THREE) {
                    offset = exonFrame == ReadingFrame.ONE ? -2 : -1;
                } else if (tierFrame == ReadingFrame.TWO) {
                    offset = exonFrame == ReadingFrame.ONE ? -1 : 1;
                } else if (tierFrame == ReadingFrame.ONE) {
                    offset = exonFrame == ReadingFrame.TWO ? 1 : 2;
                }
            }
            if (exonFrame == ReadingFrame.NONE) {
                offset = 0;
            }
        }
        return offset;
    }
}
