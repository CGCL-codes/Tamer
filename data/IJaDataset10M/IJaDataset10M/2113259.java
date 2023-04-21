package fretboard.editor.music;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to dynamically calculate the circle of fifths. It can be
 * used for exemple to display the 12 diatonic scales.
 * 
 * @author ccordenier
 * 
 */
public class CircleOfFifths {

    private static final ScaleNote C_RELATIVE_MINOR = new ScaleNote(TemperedNote.A);

    private static final ScaleNote C = new ScaleNote(TemperedNote.C);

    private List<ScaleNote> majorSharpKeys = new ArrayList<ScaleNote>();

    private List<ScaleNote> majorFlatKeys = new ArrayList<ScaleNote>();

    private List<ScaleNote> minorSharpKeys = new ArrayList<ScaleNote>();

    private List<ScaleNote> minorFlatKeys = new ArrayList<ScaleNote>();

    /**
	 * Constructor of circle of fifths.
	 * 
	 */
    public CircleOfFifths() {
        ScaleNote tonic = new ScaleNote(TemperedNote.C);
        for (int i = 0; i < 7; i++) {
            tonic = Interval.findScaleNote(tonic, Interval.PERFECT_FIFTH, true);
            majorSharpKeys.add(tonic);
            minorSharpKeys.add(Interval.findScaleNote(tonic, Interval.MINOR_THIRD, false));
        }
        tonic = new ScaleNote(TemperedNote.C);
        for (int i = 0; i < 8; i++) {
            tonic = Interval.findScaleNote(tonic, Interval.PERFECT_FOURTH, true);
            majorFlatKeys.add(tonic);
            minorFlatKeys.add(Interval.findScaleNote(tonic, Interval.MINOR_THIRD, false));
        }
    }

    /**
	 * This method can be used to obtain a major scale note key in function of
	 * an inflection and the number of inflection in the circle.
	 * 
	 * @param inflection
	 *            if Natural then nbInflection is not read
	 * @param nbInflections
	 *            the number must be between 0 and 7
	 * @return
	 */
    public ScaleNote getMajorKey(Inflection inflection, int nbInflections) {
        if (nbInflections > 7 || nbInflections < 0) {
            throw new IllegalArgumentException("Number of inflections cannot exceed 7");
        }
        if (nbInflections == 0) {
            return C;
        }
        if (Inflection.SHARP.equals(inflection)) {
            return this.majorSharpKeys.get(nbInflections - 1);
        } else {
            if (Inflection.FLAT.equals(inflection)) {
                return this.majorFlatKeys.get(nbInflections - 1);
            } else {
                return C;
            }
        }
    }

    /**
	 * This method can be used to obtain a minor scale note key in function of
	 * an inflection and the number of inflection in the circle.
	 * 
	 * @param inflection
	 *            if Natural then nbInflection is not read
	 * @param nbInflections
	 *            the number must be between 0 and 7
	 * @return
	 */
    public ScaleNote getMinorKey(Inflection inflection, int nbInflections) {
        if (nbInflections > 7 || nbInflections < 0) {
            throw new IllegalArgumentException("Number of inflections cannot exceed 7");
        }
        if (nbInflections == 0) {
            return C_RELATIVE_MINOR;
        }
        if (Inflection.SHARP.equals(inflection)) {
            return this.minorSharpKeys.get(nbInflections - 1);
        } else {
            if (Inflection.FLAT.equals(inflection)) {
                return this.minorFlatKeys.get(nbInflections - 1);
            } else {
                return C_RELATIVE_MINOR;
            }
        }
    }
}
