package org.fudaa.ctulu.gis;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.fudaa.ctulu.ProgressionInterface;

/**
 * @author Fred Deniger
 * @version $Id: GISDataModelMultiAdapter.java,v 1.7 2006-12-05 10:12:51 deniger Exp $
 */
public class GISDataModelMultiAdapter implements GISDataModel {

    private final GISDataModel[] src_;

    private final int[] nbGeom_;

    private final int totalGeom_;

    /**
   * Il est suppos� que toutes les sources contiennent les memes attributs dans le meme ordre.
   * 
   * @param _src les sources initiales taille >0
   */
    public GISDataModelMultiAdapter(final GISDataModel[] _src) {
        super();
        src_ = _src;
        if (_src == null || _src.length == 0) {
            throw new IllegalArgumentException("src must not be empty");
        }
        nbGeom_ = new int[_src.length];
        int total = 0;
        for (int i = _src.length - 1; i >= 0; i--) {
            nbGeom_[i] = _src[i].getNumGeometries();
            total += nbGeom_[i];
        }
        totalGeom_ = total;
    }

    @Override
    public GISDataModel createTranslate(GISPoint xyToAdd) {
        if (xyToAdd == null) {
            return this;
        }
        final GISDataModel[] srcTranslated = new GISDataModel[src_.length];
        for (int i = 0; i < srcTranslated.length; i++) {
            srcTranslated[i] = src_[i].createTranslate(xyToAdd);
        }
        return new GISDataModelMultiAdapter(srcTranslated);
    }

    public void preload(final GISAttributeInterface[] _att, final ProgressionInterface _prog) {
        for (int i = src_.length - 1; i >= 0; i--) {
            src_[i].preload(_att, null);
        }
    }

    public Envelope getEnvelopeInternal() {
        return GISLib.computeEnveloppe(src_, null);
    }

    private int[] find(final int _globalIdx) {
        if (_globalIdx >= totalGeom_) {
            return null;
        }
        final int[] r = new int[2];
        final int nb = nbGeom_.length;
        int tmp = _globalIdx;
        for (int i = 0; i < nb; i++) {
            if (tmp < nbGeom_[i]) {
                r[0] = i;
                r[1] = tmp;
                return r;
            }
            tmp -= nbGeom_[i];
        }
        return null;
    }

    public GISAttributeInterface getAttribute(final int _idxAtt) {
        return src_[0].getAttribute(_idxAtt);
    }

    public Geometry getGeometry(final int _idxGeom) {
        final int[] pos = find(_idxGeom);
        return src_[pos[0]].getGeometry(pos[1]);
    }

    public int getIndiceOf(final GISAttributeInterface _att) {
        return src_[0].getIndiceOf(_att);
    }

    public int getNbAttributes() {
        return src_[0].getNbAttributes();
    }

    public int getNumGeometries() {
        return totalGeom_;
    }

    public Object getValue(final int _idxAtt, final int _idxGeom) {
        final int[] pos = find(_idxGeom);
        return src_[pos[0]].getValue(_idxAtt, pos[1]);
    }

    public double getDoubleValue(final int _idxAtt, final int _idxGeom) {
        final int[] pos = find(_idxGeom);
        return src_[pos[0]].getDoubleValue(_idxAtt, pos[1]);
    }
}
