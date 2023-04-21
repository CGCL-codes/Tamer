package desmoj.extensions.db.dist;

import desmoj.core.dist.Distribution;
import desmoj.core.simulator.Model;

public abstract class DBDataDistribution extends Distribution {

    public DBDataDistribution(Model owner, String name, boolean showInReport, boolean showInTrace) {
        super(owner, name, showInReport, showInTrace);
    }
}
