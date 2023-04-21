package org.fudaa.dodico.ef.operation;

import com.vividsolutions.jts.algorithm.SIRtreePointInRing;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;
import org.fudaa.ctulu.CtuluListSelection;
import org.fudaa.ctulu.CtuluListSelectionInterface;
import org.fudaa.dodico.ef.EfElement;
import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.ef.EfIndexVisitor;

/**
 * @author fred deniger
 * @version $Id: EfIndexVisitorHashSet.java,v 1.2 2007-06-11 13:04:06 deniger Exp $
 */
public class EfIndexVisitorEltInRing implements EfIndexVisitor {

    CtuluListSelection eltSelection = new CtuluListSelection();

    final SIRtreePointInRing tester;

    final EfGridInterface grid;

    final Coordinate coord = new Coordinate();

    /**
   * @param grid le maillage a parcourir
   * @param ring la ligne ferm�e dans laquelles les elements doivent etre
   * @param strict true si tous les noeuds doivent compris dans la ligne fermee
   * @return la selection d'elements contenus dans le polyligne fermee.
   */
    public static CtuluListSelectionInterface findContainedElt(EfGridInterface grid, LinearRing ring, boolean strict) {
        EfIndexVisitorEltInRing visitor = new EfIndexVisitorEltInRing(grid, ring);
        visitor.strict = strict;
        grid.getIndex().query(ring.getEnvelopeInternal(), visitor);
        return visitor.getResult();
    }

    public EfIndexVisitorEltInRing(EfGridInterface grid, LinearRing _poly) {
        tester = new SIRtreePointInRing(_poly);
        this.grid = grid;
    }

    boolean strict = true;

    public void visitItem(final int _item) {
        EfElement elt = grid.getElement(_item);
        boolean add = strict ? isAllNodeSelected(elt) : isAtLeastOneNodeSelected(elt);
        if (add) eltSelection.add(_item);
    }

    private boolean isAllNodeSelected(EfElement elt) {
        for (int i = 0; i < elt.getPtNb(); i++) {
            if (!isNodeSelected(elt.getPtIndex(i))) return false;
        }
        return true;
    }

    private boolean isAtLeastOneNodeSelected(EfElement elt) {
        for (int i = 0; i < elt.getPtNb(); i++) {
            if (isNodeSelected(elt.getPtIndex(i))) return true;
        }
        return false;
    }

    private boolean isNodeSelected(int ndxId) {
        coord.x = grid.getPtX(ndxId);
        coord.y = grid.getPtY(ndxId);
        return tester.isInside(coord);
    }

    public CtuluListSelectionInterface getResult() {
        return eltSelection;
    }
}
