package org.fudaa.fudaa.sig;

import java.io.File;
import java.io.IOException;
import org.geotools.data.FileDataStoreFactorySpi;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.memoire.bu.BuFileFilter;
import com.memoire.fu.FuLog;
import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.ProgressionInterface;
import org.fudaa.ctulu.gis.GISGeometry;
import org.fudaa.ctulu.gis.GISVisitorDefault;

/**
 * @author Fred Deniger
 * @version $Id: FSigFileLoaderGIS.java,v 1.7 2006-09-19 15:10:21 deniger Exp $
 */
public class FSigFileLoaderGIS implements FSigFileLoaderI {

    final BuFileFilter ft_;

    final FileDataStoreFactorySpi factory_;

    transient FSigDataModelStoreAdapter src_;

    transient int nbPoint_;

    transient int nbPointTotal_;

    transient int nbPolygone_;

    transient int nbPolyligne_;

    public FSigFileLoaderGIS(final BuFileFilter _ft, final FileDataStoreFactorySpi _factory) {
        ft_ = _ft;
        factory_ = _factory;
    }

    public FSigFileLoaderI createNew() {
        return new FSigFileLoaderGIS(ft_, factory_);
    }

    public BuFileFilter getFileFilter() {
        return ft_;
    }

    private class GiSCount extends GISVisitorDefault {

        int idx_;

        final CtuluAnalyze analyze_;

        boolean isPolygoneWarn_;

        public GiSCount(final CtuluAnalyze _analyze) {
            super();
            analyze_ = _analyze;
        }

        public boolean visitPoint(final Point _p) {
            nbPoint_++;
            return true;
        }

        public boolean visitPolygone(final LinearRing _p) {
            nbPolygone_++;
            nbPointTotal_ += _p.getNumPoints();
            return true;
        }

        public boolean visitPolygoneWithHole(final Polygon _p) {
            nbPolygone_++;
            nbPointTotal_ += _p.getExteriorRing().getNumPoints();
            src_.replacePolygonWithLine(idx_);
            if (!isPolygoneWarn_ && analyze_ != null && _p.getNumInteriorRing() > 0) {
                analyze_.addWarn(FSigLib.getS("Des lignes int�rieures � des polygones ont �t� ignor�es"), idx_);
                isPolygoneWarn_ = true;
            }
            return true;
        }

        public boolean visitPolyligne(final LineString _p) {
            nbPolyligne_++;
            nbPointTotal_ += _p.getNumPoints();
            return true;
        }
    }

    public void setInResult(final FSigFileLoadResult _r, final File _f, final ProgressionInterface _prog, final CtuluAnalyze _analyze) {
        if (src_ == null) {
            try {
                src_ = new FSigDataModelStoreAdapter(factory_, _f, _prog);
                final GiSCount chooser = new GiSCount(_analyze);
                if (src_.getNbGeometries() > 0) {
                    for (int i = src_.getNbGeometries() - 1; i >= 0; i--) {
                        final Geometry geom = src_.getGeometry(i);
                        chooser.idx_ = i;
                        ((GISGeometry) geom).accept(chooser);
                    }
                }
            } catch (final IOException _e) {
                if (_analyze != null) {
                    _analyze.addFatalError(_e.getMessage());
                }
                FuLog.warning(_e);
            }
        }
        if (src_ != null && src_.getNbGeometries() > 0) {
            src_.atts_ = null;
            src_.buildAttributes(_r);
            _r.addUsedAttributes(src_.atts_);
            if (nbPoint_ > 0) {
                _r.pointModel_.add(src_);
            }
            if (nbPolygone_ > 0) {
                _r.polygoneModel_.add(src_);
            }
            if (nbPolyligne_ > 0) {
                _r.ligneModel_.add(src_);
            }
            _r.nbPoint_ += nbPoint_;
            _r.nbPointTotal_ += nbPointTotal_;
            _r.nbPolygones_ += nbPolygone_;
            _r.nbPolylignes_ += nbPolyligne_;
        }
    }
}
