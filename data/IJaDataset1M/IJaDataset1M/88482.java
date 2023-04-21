package org.fudaa.dodico.ef.operation;

import gnu.trove.TDoubleObjectHashMap;
import java.util.ArrayList;
import java.util.List;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.gis.GISAttributeInterface;
import org.fudaa.ctulu.gis.GISDataModel;
import org.fudaa.ctulu.gis.GISGeometry;
import org.fudaa.ctulu.gis.GISPoint;

/**
 * @author fred deniger
 * @version $Id: EfIsoResultDefault.java,v 1.3 2007-06-28 09:25:08 deniger Exp $
 */
public class EfIsoResultDefault implements EfIsoResultInterface, GISDataModel {

    private final List<GISGeometry> res_ = new ArrayList<GISGeometry>();

    private List values_ = new ArrayList();

    TDoubleObjectHashMap pool_ = new TDoubleObjectHashMap();

    private Double createDouble(double _d) {
        Double val = (Double) pool_.get(_d);
        if (val == null) {
            val = CtuluLib.getDouble(_d);
            pool_.put(_d, val);
        }
        return val;
    }

    @Override
    public GISDataModel createTranslate(GISPoint xyToAdd) {
        if (xyToAdd == null) {
            return this;
        }
        EfIsoResultDefault res = new EfIsoResultDefault();
        res.values_ = values_;
        res.pool_ = pool_;
        for (GISGeometry geom : res_) {
            res.res_.add(geom.createTranslateGeometry(xyToAdd));
        }
        return res;
    }

    public void geometryFound(GISGeometry _g, double _value) {
        res_.add(_g);
        values_.add(createDouble(_value));
    }

    GISAttributeInterface att_;

    public GISAttributeInterface getAttribute(int _idxAtt) {
        return att_;
    }

    public double getDoubleValue(int _idxAtt, int _idxGeom) {
        return ((Double) values_.get(_idxGeom)).doubleValue();
    }

    public Envelope getEnvelopeInternal() {
        return null;
    }

    public Geometry getGeometry(int _idxGeom) {
        return (Geometry) res_.get(_idxGeom);
    }

    public int getIndiceOf(GISAttributeInterface _att) {
        return 0;
    }

    public int getNbAttributes() {
        return 1;
    }

    public int getNumGeometries() {
        return res_.size();
    }

    public Object getValue(int _idxAtt, int _idxGeom) {
        return values_.get(_idxGeom);
    }

    public void preload(GISAttributeInterface[] _att, ProgressionInterface _prog) {
    }

    public GISAttributeInterface getAtt() {
        return att_;
    }

    public void setAtt(GISAttributeInterface _att) {
        att_ = _att;
    }
}
