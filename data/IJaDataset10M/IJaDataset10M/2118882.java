package org.jrobin.inspector;

import org.jrobin.core.*;
import java.io.File;
import java.io.IOException;

class RrdNode {

    private int dsIndex = -1, arcIndex = -1;

    private String label;

    RrdNode(RrdDb rrd) {
        String path = rrd.getRrdBackend().getPath();
        label = new File(path).getName();
    }

    RrdNode(RrdDb rrd, int dsIndex) throws IOException, RrdException {
        this.dsIndex = dsIndex;
        RrdDef def = rrd.getRrdDef();
        DsDef[] dsDefs = def.getDsDefs();
        label = dsDefs[dsIndex].dump();
    }

    RrdNode(RrdDb rrd, int dsIndex, int arcIndex) throws IOException, RrdException {
        this.dsIndex = dsIndex;
        this.arcIndex = arcIndex;
        ArcDef[] arcDefs = rrd.getRrdDef().getArcDefs();
        label = arcDefs[arcIndex].dump();
    }

    int getDsIndex() {
        return dsIndex;
    }

    int getArcIndex() {
        return arcIndex;
    }

    public String toString() {
        return label;
    }
}
