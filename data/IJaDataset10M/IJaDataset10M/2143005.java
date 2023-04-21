package net.sf.doolin.app.sc.game.dist;

import net.sf.doolin.app.sc.game.type.Position;

public class CircleSpatialDistribution extends AcceptRejectSpatialDistribution {

    private static final long serialVersionUID = 1L;

    public CircleSpatialDistribution(double range) {
        super(range);
    }

    @Override
    protected boolean accept(Position p) {
        double rc = getRange() * (1 - getMargin());
        double rc2 = rc * rc;
        double rp2 = p.x * p.x + p.y * p.y;
        return (rp2 <= rc2);
    }
}
