package net.sf.doolin.app.sc.common.gen;

import net.sf.doolin.app.sc.common.type.Position;

public abstract class AcceptRejectSpatialDistribution extends StandardSpatialDistribution {

    private static final long serialVersionUID = 1L;

    public AcceptRejectSpatialDistribution(double range) {
        super(range);
    }

    @Override
    public Position generate() {
        Position p = super.generate();
        while (!accept(p)) {
            p = super.generate();
        }
        return p;
    }

    protected abstract boolean accept(Position p);
}
