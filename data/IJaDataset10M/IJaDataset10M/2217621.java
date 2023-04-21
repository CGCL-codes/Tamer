package cunei.translate;

import java.util.Iterator;
import cunei.document.Phrase;
import cunei.type.SequenceType;
import cunei.type.TypeSequence;
import cunei.util.SingleLinkedList;

public class ChunkTranslation extends Translation {

    private abstract static class RecursiveChunksIterator implements Iterator<Translation> {

        private SingleLinkedList<Translation> chunks;

        private Iterator<Translation> subChunksIter;

        protected final boolean doReverse;

        public RecursiveChunksIterator(SingleLinkedList<Translation> chunks, boolean doReverse) {
            this.chunks = chunks;
            this.doReverse = doReverse;
        }

        protected abstract Iterator<Translation> getChunksIterator(Translation translation);

        public final boolean hasNext() {
            while (subChunksIter == null || !subChunksIter.hasNext()) {
                if (chunks == null) return false;
                subChunksIter = getChunksIterator(chunks.getHead());
                chunks = chunks.getTail();
            }
            return true;
        }

        public final Translation next() {
            return hasNext() ? subChunksIter.next() : null;
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class RecursiveSourceChunksIterator extends RecursiveChunksIterator {

        public RecursiveSourceChunksIterator(SingleLinkedList<Translation> chunks, boolean doReverse) {
            super(chunks, doReverse);
        }

        protected final Iterator<Translation> getChunksIterator(Translation translation) {
            return doReverse ? translation.getReverseSourceChunksIterator(true) : translation.getSourceChunksIterator(true);
        }
    }

    private static class RecursiveTargetChunksIterator extends RecursiveChunksIterator {

        public RecursiveTargetChunksIterator(SingleLinkedList<Translation> chunks, boolean doReverse) {
            super(chunks, doReverse);
        }

        protected final Iterator<Translation> getChunksIterator(Translation translation) {
            return doReverse ? translation.getReverseTargetChunksIterator(true) : translation.getTargetChunksIterator(true);
        }
    }

    private abstract static class PhraseTranslationIterator implements Iterator<Phrase> {

        private Iterator<Translation> chunkIter;

        private Iterator<Phrase> phraseIter;

        protected final boolean doReverse;

        public PhraseTranslationIterator(Iterator<Translation> chunkIter, boolean doReverse) {
            this.chunkIter = chunkIter;
            this.doReverse = doReverse;
        }

        protected abstract Iterator<Phrase> getPhraseIterator(Translation translation);

        public final boolean hasNext() {
            while (phraseIter == null || !phraseIter.hasNext()) {
                if (chunkIter == null || !chunkIter.hasNext()) return false;
                final Translation chunk = chunkIter.next();
                phraseIter = getPhraseIterator(chunk);
            }
            return true;
        }

        public final Phrase next() {
            return hasNext() ? phraseIter.next() : null;
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class SourcePhraseIterator extends PhraseTranslationIterator {

        public SourcePhraseIterator(Iterator<Translation> chunkIter, boolean doReverse) {
            super(chunkIter, doReverse);
        }

        protected final Iterator<Phrase> getPhraseIterator(Translation translation) {
            return doReverse ? translation.getReverseSourcePhraseIterator() : translation.getSourcePhraseIterator();
        }
    }

    private static class TargetPhraseIterator extends PhraseTranslationIterator {

        public TargetPhraseIterator(Iterator<Translation> chunkIter, boolean doReverse) {
            super(chunkIter, doReverse);
        }

        protected final Iterator<Phrase> getPhraseIterator(Translation translation) {
            return doReverse ? translation.getReverseTargetPhraseIterator() : translation.getTargetPhraseIterator();
        }
    }

    private SingleLinkedList<Translation> sourceChunks;

    private SingleLinkedList<Translation> targetChunks;

    public ChunkTranslation() {
        sourceChunks = null;
        targetChunks = null;
    }

    public final void addTranslation(Translation translation, int sourceOffset) {
        addSource(translation, sourceOffset);
        addTarget(translation);
    }

    public final void addSource(Translation translation, int sourceOffset) {
        SingleLinkedList<Translation> sourceChunksHead = null;
        SingleLinkedList<Translation> sourceChunksTail = sourceChunks;
        while (sourceOffset > 0 && sourceChunksTail != null) {
            sourceOffset -= sourceChunksTail.getHead().getCoverage();
            sourceChunksHead = sourceChunksTail;
            sourceChunksTail = sourceChunksTail.getTail();
        }
        sourceChunksTail = new SingleLinkedList<Translation>(translation, sourceChunksTail);
        if (sourceChunksHead == null) sourceChunks = sourceChunksTail; else sourceChunksHead.setTail(sourceChunksTail);
    }

    public final void addTarget(Translation translation) {
        targetChunks = new SingleLinkedList<Translation>(translation, targetChunks);
    }

    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ChunkTranslation other = (ChunkTranslation) obj;
        return (sourceChunks == other.sourceChunks || sourceChunks != null && sourceChunks.equals(other.sourceChunks)) && (targetChunks == other.targetChunks || targetChunks != null && targetChunks.equals(other.targetChunks));
    }

    public final int getCoverage() {
        int result = 0;
        for (Translation translation : sourceChunks) result += translation.getCoverage();
        return result;
    }

    public final Iterator<Phrase> getReverseSourcePhraseIterator() {
        return new SourcePhraseIterator(getReverseSourceChunksIterator(true), true);
    }

    public final Iterator<Phrase> getReverseTargetPhraseIterator() {
        return new TargetPhraseIterator(getReverseTargetChunksIterator(true), true);
    }

    public final Iterator<Translation> getSourceChunksIterator(boolean doRecursion) {
        final SingleLinkedList<Translation> reversedSourceChunks = sourceChunks.reverse();
        return doRecursion ? new RecursiveSourceChunksIterator(reversedSourceChunks, false) : reversedSourceChunks.iterator();
    }

    public final Iterator<Translation> getReverseSourceChunksIterator(boolean doRecursion) {
        return doRecursion ? new RecursiveSourceChunksIterator(sourceChunks, true) : sourceChunks.iterator();
    }

    public final Phrase getSourcePhrase() {
        return Phrase.concatenate(new Iterable<Phrase>() {

            public Iterator<Phrase> iterator() {
                return ChunkTranslation.this.getSourcePhraseIterator();
            }
        });
    }

    public final Iterator<Phrase> getSourcePhraseIterator() {
        return new SourcePhraseIterator(getSourceChunksIterator(true), false);
    }

    public final TypeSequence getSourceSequence(SequenceType type) {
        return Phrase.concatenate(new Iterable<Phrase>() {

            public Iterator<Phrase> iterator() {
                return ChunkTranslation.this.getSourcePhraseIterator();
            }
        }, type);
    }

    public final Iterator<Translation> getTargetChunksIterator(boolean doRecursion) {
        final SingleLinkedList<Translation> reversedTargetChunks = targetChunks.reverse();
        return doRecursion ? new RecursiveTargetChunksIterator(reversedTargetChunks, false) : reversedTargetChunks.iterator();
    }

    public final Iterator<Translation> getReverseTargetChunksIterator(boolean doRecursion) {
        return doRecursion ? new RecursiveTargetChunksIterator(targetChunks, true) : targetChunks.iterator();
    }

    public final Phrase getTargetPhrase() {
        return Phrase.concatenate(new Iterable<Phrase>() {

            public Iterator<Phrase> iterator() {
                return ChunkTranslation.this.getTargetPhraseIterator();
            }
        });
    }

    public final Iterator<Phrase> getTargetPhraseIterator() {
        return new TargetPhraseIterator(getTargetChunksIterator(true), false);
    }

    public final TypeSequence getTargetSequence(SequenceType type) {
        return Phrase.concatenate(new Iterable<Phrase>() {

            public Iterator<Phrase> iterator() {
                return ChunkTranslation.this.getTargetPhraseIterator();
            }
        }, type);
    }

    public final int hashCode() {
        int result = sourceChunks.hashCode();
        result = 65599 * result + targetChunks.hashCode();
        return result ^ result >> 16;
    }

    public final boolean isAligned() {
        return true;
    }

    public final boolean isComplete() {
        for (Translation targetChunk : targetChunks) if (!targetChunk.isComplete()) return false;
        return true;
    }

    public final boolean isChunked() {
        return true;
    }
}
