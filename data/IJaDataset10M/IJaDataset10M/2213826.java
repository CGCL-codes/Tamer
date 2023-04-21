package cunei.corpus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import cunei.bits.ScoreArrayBuilder;
import cunei.document.DocumentReader;
import cunei.document.Phrase;
import cunei.document.Sentence;
import cunei.model.PartialLogLinearModel;
import cunei.type.Annotation;
import cunei.type.AnnotationSet;
import cunei.type.AnnotationType;
import cunei.type.SequenceType;
import cunei.type.Type;
import cunei.type.TypeSequence;
import cunei.type.TypesOfTypes;
import cunei.util.Bounds;
import cunei.util.IntegerBoundIndex;
import cunei.util.Log;

public class MonolingualCorpus implements Serializable {

    private static final long serialVersionUID = 1L;

    private CorpusInformation corpusInformation;

    private IntegerBoundIndex sentencePositions;

    private SequenceIndex[] sequenceIndexes;

    private AnnotationIndex[] annotationIndexes;

    private transient Language language;

    public MonolingualCorpus(Language language) {
        this(language, null);
    }

    public MonolingualCorpus(Language language, CorpusInformation info) {
        this.language = language;
        corpusInformation = info == null ? new CorpusInformation("Corpus." + language.toString()) : info;
        sentencePositions = new IntegerBoundIndex();
    }

    protected void finishIndexing(String directory, SequenceIndexBuilder sequenceBuilders[]) {
        sentencePositions = sentencePositions.save(directory, language.toString().toLowerCase() + "-sentences");
        sentencePositions.load(directory);
        sequenceIndexes = new SequenceIndex[TypesOfTypes.SEQUENCE.size()];
        for (SequenceType type : TypesOfTypes.SEQUENCE.values()) {
            final int typeId = type.getId();
            final String indexName = language.toString().toLowerCase() + "-" + type.toString().toLowerCase();
            Log.getInstance().info("Building " + indexName + " index");
            SequenceIndex sequenceIndex = sequenceBuilders[typeId].toIndex();
            sequenceIndex = sequenceIndex.save(directory, indexName);
            sequenceIndex.load(directory);
            sequenceIndexes[typeId] = sequenceIndex;
        }
        if (annotationIndexes != null) {
            for (AnnotationType type : TypesOfTypes.ANNOTATION.values()) {
                AnnotationIndex annotationIndex = getAnnotationIndex(type);
                if (annotationIndex == null) continue;
                final String indexName = language.toString().toLowerCase() + "-" + type.toString().toLowerCase();
                Log.getInstance().info("Building " + indexName + " index");
                annotationIndex = annotationIndex.save(directory, indexName);
                annotationIndex.load(directory);
            }
        }
        Log.getInstance().info(language.toString() + " index contains " + getTotalTokens(SequenceType.LEXICAL) + " tokens and " + getTotalTypes(SequenceType.LEXICAL) + " types");
    }

    protected final AnnotationIndex getAnnotationIndex(AnnotationType type) {
        if (annotationIndexes == null) return null;
        final int typeId = type.getIndexId();
        if (typeId >= annotationIndexes.length) return null;
        return annotationIndexes[typeId];
    }

    public final Bounds getBounds(SequenceType type) {
        SequenceIndex index = getSequenceIndex(type);
        return index == null ? null : index.getBounds();
    }

    public final int getCount(SequenceType sequenceType, Type type) {
        SequenceIndex index = getSequenceIndex(sequenceType);
        return index == null ? 0 : index.count(type);
    }

    public final int getCount(SequenceType sequenceType, TypeSequence sequence) {
        if (sequence == null || sequence.size() == 0) return 0;
        SequenceIndex index = getSequenceIndex(sequenceType);
        return index == null ? 0 : index.count(sequence);
    }

    public final Phrase getPhrase(int sentence) {
        Bounds bounds = getSentenceBounds(sentence);
        return getPhrase(bounds.getLower(), bounds.getRange());
    }

    public final Phrase getPhrase(int pos, int length) {
        Phrase result = new Phrase(length);
        for (SequenceType type : TypesOfTypes.SEQUENCE.values()) {
            final TypeSequence sequence = getSequence(pos, length, type);
            if (sequence != null) result.set(type, sequence);
        }
        for (AnnotationType type : TypesOfTypes.ANNOTATION.values()) {
            final AnnotationIndex annotationIndex = getAnnotationIndex(type);
            if (annotationIndex != null) {
                final Map<Integer, Annotation> annotationHash = new HashMap<Integer, Annotation>();
                for (int i = 0; i < length; i++) result.addAnnotations(type, i, annotationIndex.get(pos + i, annotationHash));
            }
        }
        return result;
    }

    public final AnnotationSet[] getAnnotations(Example example) {
        final int pos = example.getPosition();
        final AnnotationSet[] result = new AnnotationSet[example.getLength()];
        for (int i = 0; i < result.length; i++) result[i] = new AnnotationSet();
        for (AnnotationType type : TypesOfTypes.ANNOTATION.values()) {
            final AnnotationIndex annotationIndex = getAnnotationIndex(type);
            if (annotationIndex != null) {
                final Map<Integer, Annotation> annotationHash = new HashMap<Integer, Annotation>();
                for (int i = 0; i < result.length; i++) result[i].add(type, annotationIndex.get(pos + i, annotationHash));
            }
        }
        return result;
    }

    public final Set<Annotation> getSentenceAnnotations(AnnotationType type, int sentenceId) {
        return corpusInformation.getSentenceAnnotations(type, sentenceId);
    }

    public final AnnotationSet getSentenceAnnotations(int sentenceId) {
        return corpusInformation.getSentenceAnnotations(sentenceId);
    }

    public final Bounds getSentenceBounds(int sentenceId) {
        return sentencePositions.getBounds(sentenceId);
    }

    public final int getSentenceId(Example e) {
        return getSentenceId(e.getPosition());
    }

    public final int getSentenceId(int pos) {
        return sentencePositions.getId(pos);
    }

    public final PartialLogLinearModel getSentenceGroupModel(int sentenceId) {
        return corpusInformation.getSentenceGroupModel(sentenceId);
    }

    public final PartialLogLinearModel getSentenceScoresModel(int sentenceId) {
        return corpusInformation.getSentenceScoreModel(sentenceId);
    }

    public final int getSentenceTokens(int sentId) {
        return getSentenceBounds(sentId).getRange();
    }

    public final TypeSequence getSequence(Example example, SequenceType type) {
        return getSequence(example.getPosition(), example.getLength(), type);
    }

    public final TypeSequence getSequence(int pos, int len, SequenceType type) {
        SequenceIndex index = getSequenceIndex(type);
        return index == null || pos > index.totalPositions() ? null : index.getSequenceByPosition(pos, len);
    }

    public final SequenceIndex getSequenceIndex(SequenceType type) {
        int typeId = type.getIndexId();
        return typeId < sequenceIndexes.length ? sequenceIndexes[typeId] : null;
    }

    public final int getTotalSentences() {
        return sentencePositions.size();
    }

    public final long getTotalTokens(SequenceType type) {
        SequenceIndex index = getSequenceIndex(type);
        return index == null ? 0 : index.totalLocations();
    }

    public final int getTotalTypes(SequenceType type) {
        SequenceIndex index = getSequenceIndex(type);
        return index == null ? 0 : index.totalTypes();
    }

    public final boolean inBounds(SequenceType type, int pos, Bounds bounds) {
        if (bounds == null || !bounds.isActive()) return false;
        SequenceIndex index = getSequenceIndex(type);
        return index != null && bounds.contains(index.getLocationAtPosition(pos));
    }

    public void index(String directory, DocumentReader<Phrase> reader) {
        Log.getInstance().info("Indexing monolingual corpus");
        ScoreArrayBuilder[] sentenceScoreBuilders = null;
        SequenceIndexBuilder sequenceBuilders[] = getSequenceBuilders();
        int sentenceId = 0;
        for (Sentence<Phrase> sentence = reader.next(); sentence != null; sentence = reader.next()) {
            sentenceScoreBuilders = corpusInformation.addSentence(sentenceScoreBuilders, sentenceId, sentence.getScores(), sentence.getAnnotations());
            addPhrase(sequenceBuilders, sentence.get());
            if (++sentenceId % 100000 == 0) Log.getInstance().info("Processed " + sentenceId + " sentences");
        }
        Log.getInstance().info("Completed " + sentenceId + " sentences");
        corpusInformation.finishIndexing(directory, sentenceScoreBuilders);
        finishIndexing(directory, sequenceBuilders);
    }

    protected static SequenceIndexBuilder[] getSequenceBuilders() {
        SequenceIndexBuilder sequenceBuilders[] = new SequenceIndexBuilder[TypesOfTypes.SEQUENCE.size()];
        for (SequenceType type : TypesOfTypes.SEQUENCE.values()) sequenceBuilders[type.getId()] = new SequenceIndexBuilder(type);
        return sequenceBuilders;
    }

    protected final void addPhrase(SequenceIndexBuilder[] sequenceBuilders, Phrase phrase) {
        final int length = phrase.getLength();
        final int sentenceId = sentencePositions.size();
        final int startPosition = sentencePositions.getLowerBound(sentenceId);
        sentencePositions.setUpperBound(sentenceId, startPosition + length);
        for (SequenceIndexBuilder sequenceBuilder : sequenceBuilders) sequenceBuilder.add(phrase);
        for (AnnotationType type : TypesOfTypes.ANNOTATION.values()) {
            final int typeId = type.getId();
            Map<Annotation, Integer> annotationHash = null;
            for (int i = 0; i < length; i++) {
                final Set<Annotation> annotations = phrase.getAnnotations(type, i);
                if (annotationIndexes == null) {
                    if (annotations == null || annotations.isEmpty()) continue;
                    annotationIndexes = new AnnotationIndex[TypesOfTypes.ANNOTATION.size()];
                }
                AnnotationIndex annotationIndex = annotationIndexes[typeId];
                if (annotationIndex == null) {
                    if (annotations == null || annotations.isEmpty()) continue;
                    annotationIndex = new AnnotationIndex(type);
                    annotationIndexes[typeId] = annotationIndex;
                }
                if (annotationHash == null) annotationHash = new IdentityHashMap<Annotation, Integer>();
                annotationIndex.set(startPosition + i, annotations, annotationHash);
            }
        }
    }

    public MonolingualCorpus load(String directory, Language language) {
        return load(directory, language, true);
    }

    protected MonolingualCorpus load(String directory, Language language, boolean enableCorpusInformation) {
        this.language = language;
        if (enableCorpusInformation) corpusInformation.load(directory);
        sentencePositions.load(directory);
        for (final SequenceType type : TypesOfTypes.SEQUENCE.values()) {
            final SequenceIndex sequenceIndex = getSequenceIndex(type);
            if (sequenceIndex != null) sequenceIndex.load(directory);
        }
        for (final AnnotationType type : TypesOfTypes.ANNOTATION.values()) {
            final AnnotationIndex annotationIndex = getAnnotationIndex(type);
            if (annotationIndex != null) annotationIndex.load(directory);
        }
        return this;
    }

    public final int findSequence(int startPosition, int endPosition, SequenceType type, TypeSequence sequence) {
        if (sequence == null) return -1;
        if (endPosition <= startPosition) return -1;
        int size = sequence.size();
        if (size == 0) return -1;
        if (endPosition - size < startPosition) return -1;
        final SequenceIndex index = getSequenceIndex(type);
        if (index == null) return -1;
        final Bounds[] sequenceTypeBounds = new Bounds[size];
        for (int i = 0; i < size; i++) sequenceTypeBounds[i] = index.getTypeBounds(sequence.get(i));
        int i = 0;
        for (int j = startPosition; j <= endPosition - size; ) {
            if (sequenceTypeBounds[i].contains(index.getLocationAtPosition(j + 1))) {
                if (++i == size) return j;
            } else {
                j++;
                i = 0;
            }
        }
        return -1;
    }

    public final Map<Integer, Integer> getSentenceCounts(Bounds bounds, SequenceType type, int max) {
        if (bounds == null || !bounds.isActive()) return null;
        final SequenceIndex index = getSequenceIndex(type);
        if (index == null) return null;
        final int[] positions = index.getPositions(bounds, max);
        if (positions == null) return null;
        final Map<Integer, Integer> result = new LinkedHashMap<Integer, Integer>();
        for (int position : positions) {
            final int sentenceId = getSentenceId(position);
            Integer value = result.get(sentenceId);
            if (value == null) value = 1; else value += 1;
            result.put(sentenceId, value);
        }
        return result;
    }

    public final Example[] makeExamples(Bounds bounds, SequenceType type, int length, int max) {
        if (bounds == null || !bounds.isActive()) return null;
        SequenceIndex index = getSequenceIndex(type);
        return index == null ? null : index.makeExamples(bounds, length, max, sentencePositions);
    }

    public final void search(Bounds bounds, SequenceType type, int skipTokens, TypeSequence sequence) {
        SequenceIndex index = getSequenceIndex(type);
        if (index == null || sequence == null) bounds.deactivate(); else index.search(bounds, skipTokens, sequence);
    }

    public String toString() {
        return "{MONOCORPUS " + language + " " + corpusInformation + "}";
    }
}
