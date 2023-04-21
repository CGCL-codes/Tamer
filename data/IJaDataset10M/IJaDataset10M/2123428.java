package jalview;

import java.util.*;
import java.awt.*;

public class HelixColourScheme extends ScoreColourScheme {

    public HelixColourScheme() {
        super(ResidueProperties.helix, ResidueProperties.helixmin, ResidueProperties.helixmax);
    }

    public Color makeColour(float c) {
        return new Color(c, (float) 1.0 - c, c);
    }
}
