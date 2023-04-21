package jalview;

import java.util.*;
import java.awt.*;

public class SequencePoint {

    SequenceI sequence;

    float[] coord;

    public SequencePoint(SequenceI sequence, float[] coord) {
        this.sequence = sequence;
        this.coord = coord;
    }
}
