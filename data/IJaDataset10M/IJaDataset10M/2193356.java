package twjcalc.model.calculate;

import twjcalc.model.whistle.Embouchure;
import twjcalc.model.whistle.Hole;
import twjcalc.model.whistle.Whistle;

public class TWCalc extends WhistleCalculator {

    protected static final double Chimney = 0.6133F;

    private static final double EmbCorrection = 0.25F;

    public TWCalc() {
        super();
    }

    @Override
    protected double closedCorrection(final Hole hole) {
        final double bore = hole.whistle.bore;
        final double holeSize = hole.size;
        final double p = (holeSize / bore) * (holeSize / bore);
        final double cls = (hole.whistle.wallThickness * p) / 4;
        return cls;
    }

    @Override
    protected double effectiveThickness(final Hole hole) {
        return hole.whistle.wallThickness + (Chimney * hole.size);
    }

    @Override
    protected double embouchureCorrection(final Whistle whistle) {
        final Embouchure emb = whistle.embouchure;
        final double embArea = (emb.width * emb.length * 2 * Math.PI);
        final double embRadius = Math.sqrt(embArea / Math.PI);
        final double bore = whistle.bore;
        double embDist = ((Math.PI * bore * bore * EmbCorrection) / embArea);
        final double ccc = ((whistle.wallThickness + 1.5 * embRadius));
        embDist = embDist * ccc;
        return embDist;
    }

    @Override
    protected double firstHoleDistance(final Hole hole) {
        final double bore = hole.whistle.bore;
        final double holeSize = hole.size;
        final double pow = (holeSize / bore) * (holeSize / bore);
        final double holeSpacing = holeSpacing(hole);
        final double chimney = effectiveThickness(hole);
        final double q = chimney * (1 / holeSpacing);
        final double r = pow + q;
        final double openCorrection = chimney / r;
        return openCorrection;
    }

    protected double holeSpacing(final Hole hole) {
        double holeSpacing = 0;
        Hole previousHole = hole.whistle.hole[hole.holeNumber - 1];
        if (previousHole.isThumbHole) {
            previousHole = hole.whistle.hole[hole.holeNumber - 2];
        }
        if (null != previousHole) {
            holeSpacing = previousHole.position - hole.position;
        }
        return holeSpacing;
    }

    @Override
    protected double subsequentHoleDistance(final Hole hole) {
        final double bore = hole.whistle.bore;
        final double holeSize = hole.size;
        final double a = bore / holeSize;
        final double b = a * a;
        final double holeSpacing = holeSpacing(hole);
        final double c = 4.0F * effectiveThickness(hole) / holeSpacing * b;
        final double d = Math.sqrt(1.0F + c);
        final double openCorrection = holeSpacing * 0.5F * (d - 1.0F);
        return openCorrection;
    }

    @Override
    public String getName() {
        if (isIterative()) {
            return "TWCalc (Iterative)";
        }
        return "TWCalc";
    }
}
