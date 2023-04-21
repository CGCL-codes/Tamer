package twjcalc.model.calculate;

import twjcalc.gui.ChangeListener;
import twjcalc.model.whistle.Hole;
import twjcalc.model.whistle.Whistle;

/**
 */
public class WhistleCalculator implements ChangeListener {

    private static double speedOfSound = 345000;

    private static double temperature = 273;

    protected static final String SPEED_OF_SOUND = "SpeedOfSound";

    private static final double MAX_DELTA = 0.0001;

    private static final int MAX_LOOP = 12;

    public static final double getSpeedOfSound() {
        return speedOfSound;
    }

    public static final double getTemperature() {
        return temperature;
    }

    public static final void setSpeedOfSound(final double speedOfSound) {
        WhistleCalculator.speedOfSound = speedOfSound;
    }

    public static final void setTemperature(final double temperature) {
        WhistleCalculator.temperature = temperature;
    }

    private boolean iterative = false;

    private final void calculateIterative(final Whistle whistle) {
        double xEnd = whistle.speedOfSound / (2 * whistle.hole[0].frequency);
        xEnd -= endCorrection(whistle);
        for (int i = 1; i < whistle.hole.length; i++) {
            xEnd -= closedCorrection(whistle.hole[i]);
        }
        whistle.hole[0].position = xEnd;
        double nominalPosition = whistle.speedOfSound / (2 * whistle.hole[1].frequency);
        whistle.hole[1].position = 0;
        double delta = 10;
        for (int i = 0; (delta > MAX_DELTA) && (i < MAX_LOOP); i++) {
            final double oldPosition = whistle.hole[1].position;
            whistle.hole[1].position = nominalPosition - firstHoleDistance(whistle.hole[1]);
            for (int h = 2; h < whistle.hole.length; h++) {
                whistle.hole[1].position -= closedCorrection(whistle.hole[h]);
            }
            delta = Math.abs(whistle.hole[1].position - oldPosition);
        }
        for (int holeNum = 2; holeNum < whistle.hole.length; holeNum++) {
            final Hole hole = whistle.hole[holeNum];
            nominalPosition = whistle.speedOfSound / (2 * hole.frequency);
            hole.position = 0;
            delta = 10;
            for (int i = 0; (delta > MAX_DELTA) && (i < MAX_LOOP); i++) {
                final double oldPosition = hole.position;
                hole.position = nominalPosition - subsequentHoleDistance(hole);
                for (int h = holeNum + 1; h < whistle.hole.length; h++) {
                    hole.position -= closedCorrection(whistle.hole[h]);
                }
                delta = Math.abs(hole.position - oldPosition);
            }
        }
        whistle.embouchure.correction = embouchureCorrection(whistle);
        for (int holeNum = 1; holeNum < whistle.hole.length; holeNum++) {
            final Hole hole = whistle.hole[holeNum];
            final double cutoff = cutoffFrequency(hole);
            hole.relativeCutoff = cutoff / hole.frequency;
        }
    }

    private final void calculateQuadratic(final Whistle whistle) {
        double xEnd = whistle.speedOfSound / (2 * whistle.hole[0].frequency);
        xEnd -= endCorrection(whistle);
        for (int i = 1; i < whistle.hole.length; i++) {
            xEnd -= closedCorrection(whistle.hole[i]);
        }
        whistle.hole[0].position = xEnd;
        double length = whistle.speedOfSound / (2 * whistle.hole[1].frequency);
        for (int i = 2; i < whistle.hole.length; i++) {
            length -= closedCorrection(whistle.hole[i]);
        }
        double a = whistle.hole[1].size / whistle.bore;
        a = a * a;
        double b = -(xEnd + length) * a;
        double c = (xEnd * length) * a;
        c += effectiveThickness(whistle.hole[1]) * (length - xEnd);
        whistle.hole[1].position = (-b - Math.sqrt((b * b) - (4 * a * c))) / ((2 * a));
        for (int holeNum = 2; holeNum < whistle.hole.length; holeNum++) {
            final Hole hole = whistle.hole[holeNum];
            length = whistle.speedOfSound / (2 * hole.frequency);
            for (int i = holeNum + 1; i < whistle.hole.length; i++) {
                length -= closedCorrection(whistle.hole[i]);
            }
            a = 2;
            double ratio = whistle.bore / hole.size;
            ratio = ratio * ratio * effectiveThickness(hole);
            Hole prevHole = whistle.hole[holeNum - 1];
            if (prevHole.isThumbHole) {
                prevHole = whistle.hole[holeNum - 2];
            }
            b = -prevHole.position - (3 * length);
            b += ratio;
            c = length - ratio;
            c *= prevHole.position;
            c += length * length;
            hole.position = (-b - Math.sqrt((b * b) - (4 * a * c))) / ((2 * a));
        }
        whistle.embouchure.correction = embouchureCorrection(whistle);
        for (int holeNum = 1; holeNum < whistle.hole.length; holeNum++) {
            final Hole hole = whistle.hole[holeNum];
            final double cutoff = cutoffFrequency(hole);
            hole.relativeCutoff = cutoff / hole.frequency;
        }
    }

    /**
	 * Calculate the effect of a closed hole on the length of the vibrating air column. The length
	 * of the vibrating air column is effectively increased by each closed tone hole which exists
	 * above the first open tone hole.
	 * 
	 * @param hole
	 * @return
	 */
    protected double closedCorrection(final Hole hole) {
        double ratio = hole.size / hole.whistle.bore;
        ratio = ratio * ratio;
        return 0.25 * hole.whistle.wallThickness * ratio;
    }

    /**
	 * Calculates the cutoff frequency above which the open hole correction is not valid. Instrument
	 * should be designed so that all second register notes are well below this frequency.
	 * 
	 */
    protected double cutoffFrequency(final Hole hole) {
        final Whistle whistle = hole.whistle;
        final int holeNum = hole.holeNumber;
        Hole prevHole = whistle.hole[holeNum - 1];
        if (prevHole.isThumbHole) {
            prevHole = whistle.hole[holeNum - 2];
        }
        final double dist = prevHole.position - whistle.hole[holeNum].position;
        final double sqrtTerm = Math.sqrt(effectiveThickness(whistle.hole[holeNum]) * dist);
        double ratio = whistle.speedOfSound / (2 * Math.PI);
        ratio *= (whistle.hole[holeNum].size / whistle.bore);
        ratio /= sqrtTerm;
        return ratio;
    }

    /**
	 * Effective wall thickness, i.e. height of air column at open finger holes; air column extends
	 * out past end of hole 3/4 of the hole diameter
	 * 
	 * @param i
	 * @return
	 */
    protected double effectiveThickness(final Hole hole) {
        return hole.whistle.wallThickness + (0.75 * hole.size);
    }

    protected double embouchureCorrection(final Whistle whistle) {
        final double Demb = (whistle.embouchure.width + whistle.embouchure.length) / 2;
        double r = (whistle.bore / Demb) * (whistle.bore / Demb);
        r *= 10.84 * whistle.wallThickness * Demb;
        r /= whistle.bore + (2 * whistle.wallThickness);
        return r;
    }

    /**
	 * Calculates the distance from physical open end of flute to effective end of vibrating air
	 * column. The vibrating air column ends beyond the end of the flute and C_end is always
	 * positive.
	 * 
	 * @param whistle
	 * @return end correction distance
	 */
    protected double endCorrection(final Whistle whistle) {
        return 0.6133 * whistle.bore / 2;
    }

    /**
	 * Calculates the effective distance from the first ("single") tone hole to the end of the
	 * vibrating air column when only that hole is open.
	 * 
	 * @param hole
	 * @return
	 */
    protected double firstHoleDistance(final Hole hole) {
        final double first = effectiveThickness(hole);
        double second = hole.size / hole.whistle.bore;
        second *= second;
        final double third = first / (hole.whistle.hole[0].position - hole.position);
        return first / (second + third);
    }

    /**
	 * Calculates the effective distance from the second and subsequent tone holes to the end of the
	 * vibrating air column when all holes below are open. NOTE: closed hole corrections must be
	 * added to this value! NOTE: the value of this correction is invalid if the frequency of the
	 * note played is above the cutoff frequency f_c.
	 * 
	 * @param hole
	 * @return
	 */
    protected double subsequentHoleDistance(final Hole hole) {
        Hole prevHole = hole.whistle.hole[hole.holeNumber - 1];
        if (prevHole.isThumbHole) {
            prevHole = hole.whistle.hole[hole.holeNumber - 2];
        }
        final double delta = prevHole.position - hole.position;
        final double first = delta / 2;
        double second = effectiveThickness(hole) / delta;
        second *= (hole.whistle.bore / hole.size) * (hole.whistle.bore / hole.size);
        second *= 4;
        second += 1;
        second = Math.sqrt(second) - 1;
        return first * second;
    }

    public final void calculate(final Whistle whistle) {
        if (iterative) {
            calculateIterative(whistle);
        } else {
            calculateQuadratic(whistle);
        }
    }

    @Override
    public void changed(final Object sender) {
    }

    public String getName() {
        if (isIterative()) {
            return "Flutomat (Iterative)";
        }
        return "Flutomat";
    }

    public final boolean isIterative() {
        return iterative;
    }

    public final void setIterative(final boolean iterative) {
        this.iterative = iterative;
    }
}
