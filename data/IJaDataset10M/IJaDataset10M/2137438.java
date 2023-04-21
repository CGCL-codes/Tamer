package org.fudaa.fudaa.meshviewer.layer;

import org.fudaa.dodico.ef.EfGridInterface;
import org.fudaa.dodico.h2d.resource.H2dResource;

/**
 * @author Fred Deniger
 * @version $Id: MvIsoPainterFond.java,v 1.6 2006-07-13 13:36:35 deniger Exp $
 */
public class MvIsoPainterFond extends MvIsoPainterDefault {

    /**
   * @param _g
   */
    public MvIsoPainterFond(final EfGridInterface _g) {
        super(_g);
    }

    public double getValue(final int _idxPoint) {
        return g_.getPtZ(_idxPoint);
    }

    public double getMax() {
        return g_.getMaxZ();
    }

    public double getMin() {
        return g_.getMinZ();
    }

    public String getNom() {
        return H2dResource.getS("Bathym�trie");
    }
}
