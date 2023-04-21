package org.gvsig.remoteClient.sld.symbolizers;

import java.util.ArrayList;
import org.gvsig.remoteClient.sld.AbstractSLDSymbolizer;
import com.iver.cit.gvsig.fmap.core.FShape;

/**
 * Implements a symbolizer which can contain more than one 
 * SLDPointSymbolizer at the same time
 * 
 * @see SLDPointSymbolizer
 * @see http://portal.opengeospatial.org/files/?artifact_id=1188
 * @author Pepe Vidal Salvador - jose.vidal.salvador@iver.es
 */
public abstract class SLDMultiPointSymbolizer extends AbstractSLDSymbolizer implements ISLDSymbolizer {

    protected ArrayList<SLDPointSymbolizer> points = new ArrayList<SLDPointSymbolizer>();

    public abstract String toXML();

    public int getShapeType() {
        return FShape.POINT;
    }

    public void addSldPoint(SLDPointSymbolizer point) {
        points.add(point);
    }
}
