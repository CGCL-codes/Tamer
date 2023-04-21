package cunei.hypothesis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cunei.collections.MergedItem;
import cunei.collections.ScoredItem;
import cunei.config.Configuration;
import cunei.config.IntegerParameter;
import cunei.config.SystemConfig;
import cunei.decode.SentenceModel;
import cunei.document.Phrase;
import cunei.lm.LanguageModelBidirectionalContext;
import cunei.lm.LanguageModelContext;
import cunei.lm.LanguageModels;
import cunei.model.LogLinearModel;
import cunei.model.SumLogLinearModel;
import cunei.translate.AnnotationModel;
import cunei.translate.ChunkTranslation;
import cunei.translate.FragmentTranslation;
import cunei.translate.SequenceModel;
import cunei.translate.Translation;
import cunei.type.SequenceType;
import cunei.util.Log;

public class Derivation implements ScoredItem, MergedItem<Derivation>, Cloneable {

    private static final Configuration CFG_DECODE = SystemConfig.getInstance("Derivation");

    private static final Configuration CFG_REPL = SystemConfig.getInstance("Replacement");

    private static final IntegerParameter CFG_DECODE_DERIV_MAX = IntegerParameter.get(CFG_DECODE, "Derivation.Maximum", 8);

    private static Hypothesis EMPTY_HYPOTHESIS = new Hypothesis(new FragmentTranslation(1, new Phrase(0)), new SumLogLinearModel());

    private static class ReplacementSourceAnnotationModel extends AnnotationModel {

        private static int[] FEATURE_IDS = getFeatureIds(CFG_REPL, "Phrase.Source");

        public ReplacementSourceAnnotationModel(final int coverage, final Phrase phraseA, final Phrase phraseB) {
            super(FEATURE_IDS, coverage, phraseA, phraseB);
        }
    }

    private static class ReplacementTargetAnnotationModel extends AnnotationModel {

        private static int[] FEATURE_IDS = getFeatureIds(CFG_REPL, "Phrase.Target");

        public ReplacementTargetAnnotationModel(final int coverage, final Phrase phraseA, final Phrase phraseB) {
            super(FEATURE_IDS, coverage, phraseA, phraseB);
        }
    }

    private static class ReplacementSourceSequenceModel extends SequenceModel {

        private static int[] FEATURE_IDS = getFeatureIds(CFG_REPL, "Phrase.Source");

        public ReplacementSourceSequenceModel(final int coverage, final Phrase phraseA, final Phrase phraseB) {
            super(FEATURE_IDS, coverage, phraseA, phraseB);
        }
    }

    private static class ReplacementTargetSequenceModel extends SequenceModel {

        private static int[] FEATURE_IDS = getFeatureIds(CFG_REPL, "Phrase.Target");

        public ReplacementTargetSequenceModel(final int coverage, final Phrase phraseA, final Phrase phraseB) {
            super(FEATURE_IDS, coverage, phraseA, phraseB);
        }
    }

    private Segments segments;

    private float segmentsLogScore;

    private float bestSegmentsOffset;

    private int sourceChars;

    private int sourceWords;

    private int targetChars;

    protected int targetWords;

    private boolean hasEmptyHead;

    private boolean isReordered;

    protected LogLinearModel derivationModel;

    private CoveragePattern pattern;

    private LanguageModelContext[] lmContext;

    private DerivationContext context;

    public Derivation(final Derivation incompleteDerivation, final Iterable<Derivation> replacementDerivations, final int startPosition, int coverage, final LanguageModels lmodels) {
        init(true, lmodels);
        final SumLogLinearModel hypothesisModel = new SumLogLinearModel();
        incompleteDerivation.segments.updateModel(hypothesisModel);
        for (Derivation derivation : replacementDerivations) derivation.segments.updateModel(hypothesisModel);
        final ChunkTranslation hypothesisTranslation = new ChunkTranslation();
        final Translation incompleteTranslation = incompleteDerivation.getTranslation();
        final BitSet chunkFragments = new BitSet();
        final Map<Translation, Integer> endPositions = new IdentityHashMap<Translation, Integer>(coverage);
        final Map<Translation, Translation> replacementTranslations = new IdentityHashMap<Translation, Translation>(coverage);
        final Iterator<Derivation> replacementDerivationIter = replacementDerivations.iterator();
        coverage = 0;
        for (Iterator<Translation> chunkTranslations = incompleteTranslation.getSourceChunksIterator(false); chunkTranslations.hasNext(); ) {
            final Translation chunkTranslation = chunkTranslations.next();
            final int chunkCoverage = chunkTranslation.getCoverage();
            if (chunkTranslation.isAligned()) {
                endPositions.put(chunkTranslation, coverage + chunkCoverage);
                if (!chunkTranslation.isComplete()) {
                    assert replacementDerivationIter.hasNext();
                    final Translation replacementTranslation = replacementDerivationIter.next().getTranslation();
                    final Phrase incompleteSourcePhrase = chunkTranslation.getSourcePhrase();
                    final Phrase replacementSourcePhrase = replacementTranslation.getSourcePhrase();
                    final Phrase incompleteTargetPhrase = chunkTranslation.getTargetPhrase();
                    final Phrase replacementTargetPhrase = replacementTranslation.getTargetPhrase();
                    hypothesisModel.join(new ReplacementSourceSequenceModel(chunkCoverage, incompleteSourcePhrase, replacementSourcePhrase));
                    hypothesisModel.join(new ReplacementSourceAnnotationModel(chunkCoverage, incompleteSourcePhrase, replacementSourcePhrase));
                    hypothesisModel.join(new ReplacementTargetSequenceModel(chunkCoverage, incompleteTargetPhrase, replacementTargetPhrase));
                    hypothesisModel.join(new ReplacementTargetAnnotationModel(chunkCoverage, incompleteTargetPhrase, replacementTargetPhrase));
                    replacementTranslations.put(chunkTranslation, replacementTranslation);
                }
            } else {
                hypothesisTranslation.addSource(chunkTranslation, 0);
                chunkFragments.set(coverage, coverage + chunkCoverage);
            }
            coverage += chunkCoverage;
        }
        assert !replacementDerivationIter.hasNext();
        for (Iterator<Translation> chunkTranslations = incompleteTranslation.getTargetChunksIterator(false); chunkTranslations.hasNext(); ) {
            Translation chunkTranslation = chunkTranslations.next();
            if (chunkTranslation.isAligned()) {
                final Integer endPosition = endPositions.get(chunkTranslation);
                final int sourceOffset = chunkFragments.get(endPosition, coverage).cardinality();
                if (!chunkTranslation.isComplete()) chunkTranslation = replacementTranslations.get(chunkTranslation);
                hypothesisTranslation.addTranslation(chunkTranslation, sourceOffset);
            } else {
                hypothesisTranslation.addTarget(chunkTranslation);
            }
        }
        final Hypothesis hypothesis = new Hypothesis(hypothesisTranslation, hypothesisModel);
        updateModels(hypothesisTranslation, coverage, lmodels);
        add(new HypothesisSegment(startPosition, hypothesis), coverage, false, false);
        hasEmptyHead = targetWords == 0;
        context = incompleteDerivation.context;
    }

    public Derivation(final ContextualHypothesis hypothesis, final int startPosition, int coverage, final LanguageModels lmodels) {
        final Translation translation = hypothesis.getTranslation();
        init(translation.isComplete(), lmodels);
        updateModels(translation, coverage, lmodels);
        add(new HypothesisSegment(startPosition, hypothesis), coverage, false, false);
        hasEmptyHead = targetWords == 0;
        context = new DerivationContext(hypothesis.getContext());
    }

    public Derivation(final int startPosition, final boolean isStart, final boolean isEnd, final LanguageModels lmodels) {
        init(!isStart, lmodels);
        if (isStart) for (int i = 0; i < lmContext.length; i++) lmodels.getModel(i, lmContext[i], lmodels.getSentenceStart(i)); else if (isEnd) for (int i = 0; i < lmContext.length; i++) derivationModel.join(lmodels.getModel(i, lmContext[i], lmodels.getSentenceEnd(i))); else derivationModel.join(EpsilonModel.getInstance());
        add(new HypothesisSegment(startPosition, EMPTY_HYPOTHESIS), 1, isStart, isEnd);
        hasEmptyHead = true;
        context = new DerivationContext();
    }

    private void init(final boolean doBidirectional, final LanguageModels lmodels) {
        segments = null;
        segmentsLogScore = 0;
        bestSegmentsOffset = 0;
        sourceChars = 0;
        sourceWords = 0;
        targetChars = 0;
        targetWords = 0;
        isReordered = false;
        derivationModel = new LogLinearModel();
        pattern = null;
        lmContext = new LanguageModelContext[LanguageModels.getInstance().size()];
        for (int i = 0; i < lmContext.length; i++) lmContext[i] = lmodels.getEmptyContext(i, doBidirectional);
    }

    protected void add(final Derivation derivation, final int coverage, boolean isStart, boolean isEnd) {
        if (derivation.segments != null) segments = derivation.segments.append(segments);
        segmentsLogScore += derivation.segmentsLogScore;
        bestSegmentsOffset += derivation.bestSegmentsOffset;
        prune();
        assert Math.abs(segments.getLogScore() - segmentsLogScore) < 0.0001;
    }

    protected void add(final HypothesisSegment segment, final int coverage, boolean isStart, boolean isEnd) {
        segments = new Segments(segment, segments);
        segmentsLogScore += segment.getLogScore();
        assert Math.abs(segments.getLogScore() - segmentsLogScore) < 0.0001;
    }

    public Derivation clone() {
        Derivation result = null;
        try {
            result = (Derivation) super.clone();
        } catch (CloneNotSupportedException e) {
            Log.getInstance().severe("Error while cloning an object of class " + getClass());
        }
        result.derivationModel = derivationModel.clone();
        result.context = context.clone();
        result.lmContext = new LanguageModelContext[lmContext.length];
        for (int i = 0; i < lmContext.length; i++) result.lmContext[i] = lmContext[i].clone();
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        return equals((Derivation) obj);
    }

    protected final boolean equals(Derivation other) {
        if (this == other) return true;
        if (this == null) return false;
        return sourceWords == other.sourceWords && sourceChars == other.sourceChars && targetWords == other.targetWords && targetChars == other.targetChars && Arrays.equals(lmContext, other.lmContext) && (pattern == other.pattern || pattern != null && pattern.equals(other.pattern)) && equalsTargetPhrases(segments, other.segments);
    }

    private static boolean equalsTargetPhrases(Segments segmentsA, Segments segmentsB) {
        if (segmentsA == segmentsB) return true;
        if (segmentsA == null || segmentsB == null) return false;
        return Phrase.equals(segmentsA.getReverseTargetPhraseIterator(), segmentsB.getReverseTargetPhraseIterator());
    }

    public final void extend(final Derivation derivation, final int coverage, final boolean isStart, final boolean isEnd, final ReorderingModel reorderingModel, final LanguageModels lmodels) {
        updateModels(derivation, coverage, isStart, isEnd, lmodels);
        add(derivation, coverage, isStart, isEnd);
        derivationModel.join(reorderingModel);
        isReordered = reorderingModel != null;
    }

    public final void extend(final Derivation derivation, final CoveragePattern coveragePattern, final ReorderingModel reorderingModel, final int size, final LanguageModels lmodels) {
        final int coverage = coveragePattern.getCoverage();
        final boolean isStart = coveragePattern.isCovered(0);
        final boolean isEnd = coveragePattern.isCovered(size - 1);
        pattern = coverage == size ? null : coveragePattern;
        extend(derivation, coverage, isStart, isEnd, reorderingModel, lmodels);
    }

    public void setCoveragePattern(CoveragePattern pattern) {
        this.pattern = pattern;
    }

    public final CoveragePattern getCoveragePattern() {
        return pattern;
    }

    private Translation getTranslation() {
        final Translation result;
        if (segments.getTail() == null) result = segments.getHead().getTranslation(); else {
            result = new ChunkTranslation();
            segments.updateTranslation((ChunkTranslation) result);
        }
        assert Phrase.equals(segments.getReverseTargetPhraseIterator(), result.getReverseTargetPhraseIterator());
        return result;
    }

    public Hypothesis getHypothesis() {
        final SumLogLinearModel model = new SumLogLinearModel(derivationModel);
        model.join(context.getModel());
        segments.updateModel(model);
        assert Math.abs(segmentsLogScore + context.getLogScore() + derivationModel.getLogScore() - model.getLogScore()) < 0.0001;
        return new Hypothesis(getTranslation(), model);
    }

    public final boolean hasEmptyHead() {
        return hasEmptyHead;
    }

    public final boolean isExtended() {
        return segments.isExtended();
    }

    public final boolean isReordered() {
        return isReordered;
    }

    public final int getLastStartPosition() {
        return segments.getHead().getSourcePosition();
    }

    public final int getLastCoverage() {
        return segments.getHead().getTranslation().getCoverage();
    }

    protected final Iterator<Phrase> getTargetPhraseIterator() {
        return segments.getTargetPhraseIterator();
    }

    public void merge(Derivation other) {
        final float derivationLogScore = derivationModel.getLogScore();
        final float otherDerivationLogScore = other.derivationModel.getLogScore();
        final float bestLogScore = segmentsLogScore + bestSegmentsOffset;
        final float otherBestLogScore = other.segmentsLogScore + other.bestSegmentsOffset;
        final Segments oldSegments;
        if (bestLogScore + derivationLogScore < otherBestLogScore + otherDerivationLogScore) {
            LogLinearModel offsetModel = null;
            if (derivationLogScore != otherDerivationLogScore) {
                offsetModel = derivationModel.clone();
                offsetModel.remove(other.derivationModel);
            }
            oldSegments = other.segments;
            segments = other.segments.merge(segments, offsetModel);
            segmentsLogScore = other.segmentsLogScore;
            bestSegmentsOffset = other.bestSegmentsOffset;
            derivationModel = other.derivationModel.clone();
            context = other.context.clone();
            hasEmptyHead = other.hasEmptyHead;
            isReordered = other.isReordered;
        } else {
            LogLinearModel offsetModel = null;
            if (derivationLogScore != otherDerivationLogScore) {
                offsetModel = other.derivationModel.clone();
                offsetModel.remove(derivationModel);
            }
            oldSegments = segments;
            segments = segments.merge(other.segments, offsetModel);
        }
        if (segments != oldSegments && !prune()) {
            bestSegmentsOffset += segmentsLogScore;
            segmentsLogScore = segments.getLogScore();
            bestSegmentsOffset -= segmentsLogScore;
        }
        assert otherBestLogScore + otherDerivationLogScore - segmentsLogScore - bestSegmentsOffset - derivationModel.getLogScore() < 0.0001;
        assert bestLogScore + derivationLogScore - segmentsLogScore - bestSegmentsOffset - derivationModel.getLogScore() < 0.0001;
        assert Math.abs(segments.getLogScore() - segmentsLogScore) < 0.0001;
    }

    private final boolean prune() {
        final int maxDerivations = CFG_DECODE_DERIV_MAX.getValue();
        if (segments.getAlternatesWithLimit(maxDerivations) < maxDerivations) return false;
        final List<Float> logScores = new ArrayList<Float>();
        segments.loadLogScores(0, logScores);
        Collections.sort(logScores);
        final float minLogScore = logScores.get(logScores.size() - maxDerivations - 1);
        segments = segments.prune(minLogScore);
        bestSegmentsOffset += segmentsLogScore;
        segmentsLogScore = segments.getLogScore();
        bestSegmentsOffset -= segmentsLogScore;
        return true;
    }

    public float getLogScore() {
        return segmentsLogScore + context.getLogScore() + derivationModel.getLogScore() + (pattern == null ? 0 : pattern.getRemainingLogScore());
    }

    protected void updateModels(final Translation translation, int coverage, final LanguageModels lmodels) {
        for (Iterator<Translation> chunkTranslations = translation.getReverseSourceChunksIterator(true); chunkTranslations.hasNext(); ) {
            final Translation chunkTranslation = chunkTranslations.next();
            if (chunkTranslation.isComplete()) {
                final Phrase sourcePhrase = chunkTranslation.getSourcePhrase();
                final int length = sourcePhrase.getLength();
                sourceWords += length;
                if (length != 0) sourceChars += sourcePhrase.get(SequenceType.LEXICAL).characterLength();
            } else {
                coverage -= chunkTranslation.getCoverage();
            }
        }
        for (Iterator<Translation> chunkTranslations = translation.getReverseTargetChunksIterator(true); chunkTranslations.hasNext(); ) {
            final Translation chunkTranslation = chunkTranslations.next();
            if (chunkTranslation.isComplete()) {
                final Phrase targetPhrase = chunkTranslation.getTargetPhrase();
                final int length = targetPhrase.getLength();
                targetWords += length;
                if (length != 0) targetChars += targetPhrase.get(SequenceType.LEXICAL).characterLength();
                for (int i = 0; i < lmContext.length; i++) derivationModel.join(lmodels.getModel(i, lmContext[i], targetPhrase));
            } else {
                for (int i = 0; i < lmContext.length; i++) lmContext[i].clear();
            }
        }
        derivationModel.set(new SentenceModel(coverage, sourceChars, sourceWords, targetChars, targetWords));
        updateTargetAnnotationModel();
    }

    protected void updateModels(final Derivation derivation, int coverage, boolean isStart, boolean isEnd, final LanguageModels lmodels) {
        context.join(isStart, segments, targetWords, derivation.context, derivation.targetWords);
        sourceWords += derivation.sourceWords;
        sourceChars += derivation.sourceChars;
        targetWords += derivation.targetWords;
        targetChars += derivation.targetChars;
        derivationModel.join(derivation.derivationModel);
        for (int i = 0; i < lmContext.length; i++) {
            final LanguageModelBidirectionalContext nextLmContext = (LanguageModelBidirectionalContext) derivation.lmContext[i];
            derivationModel.join(lmodels.getModel(i, lmContext[i], nextLmContext));
        }
        if (isStart) coverage--;
        if (isEnd) coverage--;
        derivationModel.set(new SentenceModel(coverage, sourceChars, sourceWords, targetChars, targetWords));
        updateTargetAnnotationModel();
    }

    private final void updateTargetAnnotationModel() {
    }

    public String debug(boolean showTree) {
        return segments.debug(showTree, derivationModel);
    }

    /**
     * This could be built incrementally, but it appears unnecessary as
     * profiling indicates that hashCode() is called about 1.9 times as often as
     * the hash code changes due to object modifications
     */
    public int hashCode() {
        int result = targetWords;
        result = 65599 * result + sourceWords;
        for (LanguageModelContext element : lmContext) result = 65599 * result + element.getNgramId();
        result = 65599 * result + targetChars;
        result = 65599 * result + sourceChars;
        if (pattern != null) result = 65599 * result + pattern.hashCode();
        return result ^ result >> 16;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{DER ");
        result.append(getLogScore());
        if (segments != null) {
            result.append(" ");
            result.append(segments);
        }
        result.append("}");
        return result.toString();
    }
}
