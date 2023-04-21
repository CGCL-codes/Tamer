package com.iver.cit.gvsig.geoprocess.impl.dissolve.fmap;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Its a SingleFieldCriteria that adds adjacency condition:
 * once a feature has passed the single field value dissolve
 * criteria (it has the same specified field value) it must be
 * adjacent to the seed feature to dissolve them.
 * @author azabala
 *
 */
public class SingleFieldAdjacencyDissolveCriteria extends SingleFieldDissolveCriteria {

    public SingleFieldAdjacencyDissolveCriteria(String dissolveField, FLyrVect layer) throws DriverException {
        super(dissolveField, layer);
    }

    public boolean verifyIfDissolve(int featureIndex1, int featureIndex2) {
        if (super.verifyIfDissolve(featureIndex1, featureIndex2)) {
            Geometry g1;
            Geometry g2;
            try {
                g1 = layer.getSource().getShape(featureIndex1).toJTSGeometry();
                g2 = layer.getSource().getShape(featureIndex2).toJTSGeometry();
            } catch (ReadDriverException e) {
                return false;
            }
            return g1.intersects(g2);
        }
        return false;
    }

    public void clear() {
        super.clear();
    }
}
