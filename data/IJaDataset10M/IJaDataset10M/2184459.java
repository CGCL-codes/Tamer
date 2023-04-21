package guitar;

import music.IntervalSequence;
import music.Note;
import music.Scale;

/**
 * A compositor that composes the a scale pattern on the entire neck, from open
 * string to the last fret, and on all strings. Strings are iterated low to high
 * and so are the frets. There is no fingering intelligence, so all notes show
 * as played by the 1st finger.
 * @author martin
 */
public class WholeNeckCompositor extends Compositor {

    /**
   * When composing on the whole neck, there is only one position.
   * @param scale Ignored argument.
   * @return Always 1.
   */
    public int getNumberPositions(Scale scale) {
        return 1;
    }

    /**
   * Composes the scale on the entire neck.
   * @param neck
   * @param key
   * @param scale
   * @param position
   */
    protected void compose(Neck neck, Note key, Scale scale, int position) {
        IntervalSequence intervals = scale.getIntervalSequence();
        for (int string = neck.getNumberStrings(); string >= 1; string--) {
            int rollup = 0;
            int discrepancy = (neck.getCDistance(string) + 12 - key.cDistance) % 12;
            while (rollup < discrepancy) rollup += intervals.getNext();
            int fret = rollup - discrepancy;
            do {
                notifyFretPlayed(new FretboardEvent(string, fret, 1));
                fret += intervals.getNext();
            } while (fret <= neck.getNumberFrets());
            intervals.reset();
        }
    }

    public String getPositionTypeName() {
        return "whole neck";
    }
}
