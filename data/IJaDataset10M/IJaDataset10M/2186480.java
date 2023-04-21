package cunei.alignment;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import cunei.bits.ScoreArray;
import cunei.bits.UnsignedArray;
import cunei.document.Phrase;
import cunei.util.Bounds;
import cunei.util.IntegerBoundIndex;

public class AlignmentIndex implements Serializable {

    private static final long serialVersionUID = 1L;

    private class IndexedWordAlignmentIterator implements Iterator<WordAlignment> {

        private class IndexedWordAlignment implements WordAlignment {

            private int loc;

            public IndexedWordAlignment(final int loc) {
                this.loc = loc;
            }

            public final float getSourceProbability() {
                return scores.get(loc);
            }

            public final float getTargetProbability() {
                return scores.get(loc + 1);
            }

            public final int getSourcePosition() {
                return (int) positions.get(loc);
            }

            public final int getTargetPosition() {
                return (int) positions.get(loc + 1);
            }
        }

        protected int loc;

        protected final int end;

        public IndexedWordAlignmentIterator(int loc, int end) {
            this.loc = loc;
            this.end = end;
        }

        public final boolean hasNext() {
            return loc < end;
        }

        public final WordAlignment next() {
            if (!hasNext()) return null;
            WordAlignment result = new IndexedWordAlignment(loc);
            loc += 2;
            return result;
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class IndexedWordAlignmentSet implements Iterable<WordAlignment> {

        protected final int loc;

        protected final int end;

        public IndexedWordAlignmentSet(int loc, int end) {
            this.loc = loc;
            this.end = end;
        }

        public Iterator<WordAlignment> iterator() {
            return new IndexedWordAlignmentIterator(loc, end);
        }
    }

    private IntegerBoundIndex offsets;

    private UnsignedArray positions;

    private ScoreArray scores;

    public AlignmentIndex(IntegerBoundIndex offsets, UnsignedArray positions, ScoreArray scores) {
        this.offsets = offsets;
        this.positions = positions;
        this.scores = scores;
    }

    public final Iterator<WordAlignment> getAlignmentIterator(int sentence) {
        Bounds offset = offsets.getBounds(sentence);
        return offset.getRange() == 0 ? null : new IndexedWordAlignmentIterator(offset.getLower(), offset.getUpper());
    }

    public final Iterable<WordAlignment> getAlignmentSet(int sentence) {
        Bounds offset = offsets.getBounds(sentence);
        return new IndexedWordAlignmentSet(offset.getLower(), offset.getUpper());
    }

    public final PhraseAlignment getPhraseAlignment(int sentenceId, Phrase source, Phrase target) {
        return new PhraseAlignment(source, target, getAlignmentIterator(sentenceId));
    }

    public final Collection<PhraseAlignment> getSubPhraseAlignments(int sentenceId, Phrase source, Phrase target, int sourceStartPosition, int sourceEndPosition) {
        final PhraseAlignment alignment = getPhraseAlignment(sentenceId, source, target);
        return alignment.getAlignments(sourceStartPosition, sourceEndPosition, false);
    }

    public final AlignmentIndex load(String path) {
        offsets.load(path);
        positions.load(path);
        if (scores != null) scores.load(path);
        return this;
    }

    public final void remove(String path) {
        offsets.remove(path);
        positions.remove(path);
        if (scores != null) scores.remove(path);
    }

    public final AlignmentIndex save(String path, String file) {
        offsets = offsets.save(path, file + "-offsets");
        positions = positions.save(path, file + "-positions");
        if (scores != null) scores = scores.save(path, file);
        return this;
    }

    public String toString() {
        return "{ALIGNINDEX " + offsets.size() + " offsets, " + positions.size() + " positions" + scores.toString() + "}";
    }
}
