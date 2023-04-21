package musicseeder.node;

import java.util.*;
import musicseeder.analysis.*;

@SuppressWarnings("nls")
public final class APart extends PPart {

    private final NodeList<PChord> _chordProgression_ = new NodeList<PChord>(this);

    public APart() {
    }

    public APart(@SuppressWarnings("hiding") List<PChord> _chordProgression_) {
        setChordProgression(_chordProgression_);
    }

    @Override
    public Object clone() {
        return new APart(cloneList(this._chordProgression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPart(this);
    }

    public LinkedList<PChord> getChordProgression() {
        return this._chordProgression_;
    }

    public void setChordProgression(List<PChord> list) {
        if (list == this._chordProgression_) {
            return;
        }
        this._chordProgression_.clear();
        this._chordProgression_.addAll(list);
    }

    @Override
    public String toString() {
        return "" + toString(this._chordProgression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._chordProgression_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PChord> i = this._chordProgression_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PChord) newChild);
                    return;
                }
                i.remove();
                return;
            }
        }
        throw new RuntimeException("Not a child.");
    }
}
