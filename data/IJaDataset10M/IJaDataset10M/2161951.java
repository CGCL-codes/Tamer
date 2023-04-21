package jalview.gui.schemes;

import jalview.datamodel.*;
import jalview.gui.*;
import java.util.*;
import java.awt.*;

public class ScoreColourScheme extends ResidueColourScheme {

    public double min;

    public double max;

    public double[] scores;

    public ScoreColourScheme(double[] scores, double min, double max) {
        super();
        this.scores = scores;
        this.min = min;
        this.max = max;
    }

    public Color getColour(AlignSequenceI seq, int j, Vector aa) {
        Color c = Color.white;
        String s = String.valueOf(seq.getBaseAt(j));
        if (threshold > 0) {
            if (aboveThreshold(aa, seq, j, threshold)) {
                c = findColour(seq, s, j, aa);
            }
        } else if (!s.equals("-") && !s.equals(".") && !s.equals(" ")) {
            c = findColour(seq, s, j, aa);
        } else {
            c = Color.white;
        }
        return c;
    }

    public Color findColour(AlignSequenceI seq, String s, int j, Vector aa) {
        float red = (float) (scores[((Integer) ResidueProperties.aaHash.get(s)).intValue()] - (float) min) / (float) (max - min);
        if (red > (float) 1.0) {
            red = (float) 1.0;
        }
        if (red < (float) 0.0) {
            red = (float) 0.0;
        }
        return makeColour(red);
    }

    public Color makeColour(float c) {
        return new Color(c, (float) 0.0, (float) 1.0 - c);
    }
}
