package org.fudaa.ebli.calque;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.fudaa.ctulu.CtuluCommandContainer;
import org.fudaa.ctulu.CtuluLib;
import org.fudaa.ctulu.CtuluLibArray;
import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.ctulu.gis.GISZoneListener;
import org.fudaa.ctulu.gis.GISAttributeInteger;
import org.fudaa.ctulu.gis.GISAttributeInterface;
import org.fudaa.ctulu.gis.GISAttributeModelIntegerInterface;
import org.fudaa.ctulu.gis.GISPoint;
import org.fudaa.ctulu.gis.GISZoneCollectionPoint;
import org.fudaa.ebli.calque.edition.ZModelePointEditable;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.commun.EbliTableInfoPanel;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrPoint;
import org.fudaa.ebli.palette.BPaletteInfo.InfoData;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuTable;
import com.memoire.fu.Fu;
import com.memoire.fu.FuLog;
import com.vividsolutions.jts.geom.Envelope;
import org.fudaa.ctulu.CtuluImageContainer;
import org.fudaa.ctulu.CtuluLibImage;
import org.fudaa.ctulu.RasterData;
import org.fudaa.ctulu.gis.CtuluLibGeometrie;
import org.fudaa.ctulu.table.CtuluTable;

/**
 * Un modele simple pour le modele Image. Les points et la boite retournes sont des objets r�initialises. Il est
 * possible d'auto-appliquer les morphismes sur ces objets.
 * 
 * @version $Id: ZModeleStatiqueImageRaster.java,v 1.20 2007-05-04 13:49:43 deniger Exp $
 * @author Fred Deniger
 */
public class ZModeleStatiqueImageRaster extends ZModelePointEditable implements ZModeleImageRaster, GISZoneListener {

    public boolean isImageLoaded() {
        return this.image_ != null && image_.isImageLoaded();
    }

    protected class ImgTableModel extends AbstractTableModel {

        public Class getColumnClass(final int _columnIndex) {
            return _columnIndex < 4 ? Integer.class : Double.class;
        }

        public int getColumnCount() {
            return 7;
        }

        public String getColumnName(final int _columnIndex) {
            if (_columnIndex == 0) {
                return EbliLib.getS("Indice");
            }
            if (_columnIndex == 1) {
                return EbliLib.getS("X image (pixel)");
            }
            if (_columnIndex == 2) {
                return EbliLib.getS("Y image");
            }
            if (_columnIndex == 3) {
                return EbliLib.getS("Y image (du haut)");
            }
            if (_columnIndex == 4) {
                return EbliLib.getS("X r�el");
            }
            if (_columnIndex == 5) {
                return EbliLib.getS("Y r�el");
            }
            return EbliLib.getS("Erreurs");
        }

        public int getRowCount() {
            return getNombre();
        }

        public Object getValueAt(final int _rowIndex, final int _columnIndex) {
            if (_columnIndex == 0) {
                return new Integer(_rowIndex + 1);
            }
            if (_columnIndex == 1) {
                return new Integer(getXImgCalage(_rowIndex));
            }
            if (_columnIndex == 2) {
                return new Integer((getHImg() - getYImgCalageFromTop(_rowIndex)));
            }
            if (_columnIndex == 3) {
                return new Integer(getYImgCalageFromTop(_rowIndex));
            }
            if (_columnIndex == 4) {
                return CtuluLib.getDouble(getX(_rowIndex));
            }
            if (_columnIndex == 5) {
                return CtuluLib.getDouble(getY(_rowIndex));
            }
            return CtuluLib.getDouble(getErreur(_rowIndex));
        }
    }

    public static Point2D.Double[] copy(final Point2D.Double[] _ini) {
        if (_ini == null) {
            return null;
        }
        final Point2D.Double[] res = new Point2D.Double[_ini.length];
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = (Point2D.Double) _ini[i].clone();
        }
        return res;
    }

    public static double[] getErreurs(final Point2D.Double[] _img, final Point2D.Double[] _reel, final AffineTransform _trans) {
        return getErreurs(_img, _reel, _trans, null);
    }

    public static double[] getErreurs(final Point2D.Double[] _img, final Point2D.Double[] _reel, final AffineTransform _trans, final double[] _error) {
        if (CtuluLibArray.isEmpty(_img) || CtuluLibArray.isEmpty(_reel) || _trans == null) {
            return null;
        }
        final double[] res = (_error != null && _error.length == _img.length) ? _error : new double[_img.length];
        final Point2D.Double tmp = new Point2D.Double();
        for (int i = res.length - 1; i >= 0; i--) {
            _trans.transform(_img[i], tmp);
            res[i] = tmp.distance(_reel[i]);
        }
        return res;
    }

    CtuluImageContainer image_;

    double[] erreurs_;

    File path_;

    AffineTransform raster_;

    public ZModeleStatiqueImageRaster() {
        super(false);
        pts_ = createZone();
    }

    public ZModeleStatiqueImageRaster(final CtuluImageContainer _img) {
        super(false);
        pts_ = createZone();
        setImage(_img);
    }

    private void ajustePt(final GrBoite _boite, final Point2D.Double _init, final Point2D.Double _dest) {
        raster_.transform(_init, _dest);
        _boite.ajuste(_dest);
    }

    private GISZoneCollectionPoint createZone() {
        final GISAttributeInteger xImg = new GISAttributeInteger("X img", true);
        final GISAttributeInteger yImg = new GISAttributeInteger("Y img", true);
        final GISZoneCollectionPoint res = new GISZoneCollectionPoint(this);
        res.setAttributes(new GISAttributeInteger[] { xImg, yImg }, null);
        return res;
    }

    /**
   * On doit mettre l'image dans le sens de ebli avant de commencer.
   */
    private void initTransform() {
        raster_ = new AffineTransform();
        raster_.concatenate(AffineTransform.getTranslateInstance(0, image_.getImageHeight()));
        raster_.concatenate(AffineTransform.getScaleInstance(1, -1));
    }

    public BufferedImage getImage(final GrBoite _clipReel, final int _widthEcran, final int _heightEcran, final AffineTransform _toReel, final boolean _quick) {
        _toReel.setToIdentity();
        BufferedImage res;
        int xOffset = 0;
        int yOffset = 0;
        float ratioX = 1;
        float ratioY = 1;
        if (_quick) {
            res = image_.getAvailableSnapshot();
            ratioX = CtuluLibImage.getRatio(res.getWidth(), getWImg());
            ratioY = CtuluLibImage.getRatio(res.getHeight(), getHImg());
        } else {
            AffineTransform inverse = null;
            final GrBoite b = getDomaine();
            b.intersect(_clipReel);
            try {
                inverse = raster_.createInverse();
            } catch (final NoninvertibleTransformException _evt) {
                FuLog.error(_evt);
            }
            if (inverse == null) {
                return null;
            }
            final Envelope env = new Envelope();
            final Point2D.Double init = b.e_.getPoint2D();
            final Point2D.Double dest = new Point2D.Double();
            inverse.transform(init, dest);
            env.expandToInclude(dest.x, dest.y);
            init.x = b.o_.x_;
            init.y = b.o_.y_;
            inverse.transform(init, dest);
            env.expandToInclude(dest.x, dest.y);
            init.x = b.e_.x_;
            init.y = b.o_.y_;
            inverse.transform(init, dest);
            env.expandToInclude(dest.x, dest.y);
            init.x = b.o_.x_;
            init.y = b.e_.y_;
            inverse.transform(init, dest);
            env.expandToInclude(dest.x, dest.y);
            xOffset = (int) Math.floor(env.getMinX());
            yOffset = (int) Math.floor(env.getMinY());
            int h = (int) Math.ceil(env.getHeight());
            int w = (int) Math.ceil(env.getWidth());
            if (w < 0) {
                w = 0;
            }
            if (xOffset < 0) {
                w += xOffset;
                xOffset = 0;
            }
            if (yOffset < 0) {
                h += xOffset;
                yOffset = 0;
            }
            h = Math.min(h, getHImg() - yOffset);
            if (h <= 0) {
                h = 1;
            }
            w = Math.min(w, getWImg() - xOffset);
            if (w <= 0) {
                w = 1;
            }
            ratioX = CtuluLibImage.getRatio(_widthEcran, w);
            ratioY = CtuluLibImage.getRatio(_heightEcran, h);
            final Rectangle r = new Rectangle(xOffset, yOffset, w, h);
            res = getImage(r, ratioX, ratioY);
            ratioX = CtuluLibImage.getRatio(res.getWidth(), r.width);
            ratioY = CtuluLibImage.getRatio(res.getHeight(), r.height);
            xOffset = r.x;
            yOffset = r.y;
        }
        _toReel.concatenate(raster_);
        _toReel.translate(xOffset, yOffset);
        _toReel.scale(1 / ratioX, 1 / ratioY);
        return res;
    }

    Rectangle lastRec_;

    float lastRatioX_;

    float lastRatioY_;

    /** Image cache */
    BufferedImage lastImage_;

    /**
   * Retourne une partie de l'image. Methode optimis�e qui retourne un cache
   * si les dimensions et ratio ne sont pas modifi�s depuis le dernier appel.
   * @param _r Le rectangle de l'image.
   * @param _ratioX Le ratio suivant X
   * @param _ratioY Le ratio suivant Y
   * @return L'image partielle
   */
    private BufferedImage getImage(final Rectangle _r, final float _ratioX, final float _ratioY) {
        if (lastRec_ != null && lastImage_ != null && lastRec_.contains(_r) && _ratioX <= lastRatioX_ && _ratioY <= lastRatioY_) {
            if (Fu.DEBUG && FuLog.isDebug()) {
                FuLog.debug("ECA: on recupere l'ancienne image");
            }
            _r.setBounds(lastRec_);
            return lastImage_;
        }
        if (lastRec_ == null) {
            lastRec_ = new Rectangle(_r);
        } else {
            lastRec_.setBounds(_r);
        }
        lastRatioY_ = _ratioY;
        lastRatioX_ = _ratioX;
        lastImage_ = image_.getImage(_r, _ratioX, _ratioY);
        return lastImage_;
    }

    public final void clear() {
        pts_.removeAll(null);
        lastImage_ = null;
        if (image_ != null) initTransform();
    }

    public BuTable createValuesTable(final ZCalqueAffichageDonneesInterface _layer) {
        final BuTable r = new CtuluTable(new ImgTableModel());
        EbliTableInfoPanel.setTitle(r, _layer.getTitle());
        EbliTableInfoPanel.setComponent(r, new BuLabel("<html>" + EbliLib.getS("Les coordonn�es de l'image sont donn�es � partir d'en bas � droite.") + "<br>" + EbliLib.getS("La colonne '{0}' du tableau permet de conna�tre l'ordonn�e du point depuis le haut de l'image", r.getModel().getColumnName(3))));
        return r;
    }

    protected void autoUpdate() {
        if (autoUpdate_) {
            setProj(getPtImg(), getPtReel());
        }
    }

    public GrBoite domaine() {
        final GrBoite r = new GrBoite();
        if (domaine(r)) {
            return r;
        }
        return null;
    }

    public int getWImg() {
        return image_ == null ? 0 : image_.getImageWidth();
    }

    public int getHImg() {
        return image_ == null ? 0 : image_.getImageHeight();
    }

    public boolean domaine(final GrBoite _boite) {
        if (raster_ == null) {
            _boite.ajuste(0, 0, 0);
            _boite.ajuste(getWImg(), getHImg(), 0);
            return true;
        }
        final Point2D.Double init = new Point2D.Double();
        final Point2D.Double dest = new Point2D.Double();
        ajustePt(_boite, init, dest);
        init.x = getWImg();
        ajustePt(_boite, init, dest);
        init.y = getHImg();
        ajustePt(_boite, init, dest);
        init.x = 0;
        ajustePt(_boite, init, dest);
        final Envelope e = pts_.getEnvelopeInternal();
        _boite.ajuste(e.getMinX(), e.getMinY(), 0);
        _boite.ajuste(e.getMaxX(), e.getMaxY(), 0);
        return true;
    }

    public void fillWithInfo(final InfoData _d, final ZCalqueAffichageDonneesInterface _layer) {
        _d.setTitle(_layer.getTitle());
        _d.put(EbliLib.getS("Nombre de points"), CtuluLibString.getString(getNombre()));
        final int nb = _layer.isSelectionEmpty() || _layer.getLayerSelection() == null ? 0 : _layer.getLayerSelection().getNbSelectedIndex();
        _d.put(EbliLib.getS("Nombre de points s�lectionn�s"), CtuluLibString.getString(nb));
        if (nb == 2) {
            final int i = _layer.getLayerSelection().getMaxIndex();
            final int i2 = _layer.getLayerSelection().getMinIndex();
            _d.put(EbliLib.getS("Distance entre les 2 points"), CtuluLib.DEFAULT_NUMBER_FORMAT.format(CtuluLibGeometrie.getDistance(getX(i), getY(i), getX(i2), getY(i2))));
            return;
        }
        if (nb != 1) {
            return;
        }
        final int idxNode = _layer.getLayerSelection().getMaxIndex();
        _d.setTitle(EbliLib.getS("Point {0}", CtuluLibString.getString(idxNode + 1)));
        _d.put(EbliLib.getS("Pixels "), (getXImgCalage(idxNode)) + CtuluLibString.VIR + (getHImg() - getYImgCalageFromTop(idxNode)));
        _d.put(EbliLib.getS("Pixels (depuis haut,gauche)"), getXImgCalage(idxNode) + CtuluLibString.VIR + getYImgCalageFromTop(idxNode));
        _d.put(EbliLib.getS("Reel"), CtuluLib.DEFAULT_NUMBER_FORMAT.format(getX(idxNode)) + CtuluLibString.VIR + CtuluLib.DEFAULT_NUMBER_FORMAT.format(getY(idxNode)));
    }

    public boolean removePoint(final int[] _idx, final CtuluCommandContainer _cmd) {
        if (CtuluLibArray.isEmpty(_idx)) {
            return false;
        }
        if (getNombre() - _idx.length >= 3) {
            return pts_.remove(_idx, _cmd);
        }
        return false;
    }

    public RasterData getData() {
        final RasterData r = new RasterData();
        r.setImg(image_);
        for (int i = 0; i < getNombre(); i++) {
            r.addPt(getXImgCalage(i), getYImgCalageFromTop(i), getX(i), getY(i));
        }
        return r;
    }

    public GrBoite getDomaine() {
        final GrBoite grBoite = new GrBoite();
        domaine(grBoite);
        return grBoite;
    }

    public Point2D.Double[] getPtReel() {
        final Point2D.Double[] res = new Point2D.Double[getNombre()];
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = new Point2D.Double(getX(i), getY(i));
        }
        return res;
    }

    public Point2D.Double[] getPtImg() {
        final Point2D.Double[] res = new Point2D.Double[getNombre()];
        final GISAttributeModelIntegerInterface x = (GISAttributeModelIntegerInterface) pts_.getModel(0);
        final GISAttributeModelIntegerInterface y = (GISAttributeModelIntegerInterface) pts_.getModel(1);
        for (int i = res.length - 1; i >= 0; i--) {
            res[i] = new Point2D.Double(x.getValue(i), y.getValue(i));
        }
        return res;
    }

    public double getErreur(final int _i) {
        if (erreurs_ == null) {
            erreurs_ = getErreurs(getPtImg(), getPtReel(), raster_);
        }
        return erreurs_ == null ? 0 : erreurs_[_i];
    }

    public BufferedImage getImage() {
        if (image_ != null) return image_.getSnapshot();
        return null;
    }

    public Object getObject(final int _ind) {
        return null;
    }

    public File getPath() {
        return path_;
    }

    public AffineTransform getRasterTransform() {
        return raster_;
    }

    public int getXImgCalage(final int _i) {
        return ((GISAttributeModelIntegerInterface) pts_.getModel(0)).getValue(_i);
    }

    public int getYImgCalageFromTop(final int _i) {
        return ((GISAttributeModelIntegerInterface) pts_.getModel(1)).getValue(_i);
    }

    public boolean isValuesTableAvailable() {
        return true;
    }

    public boolean point(final GrPoint _p, final int _i, final boolean _force) {
        _p.setCoordonnees(getX(_i), getY(_i), 0);
        return true;
    }

    public final void setImage(final CtuluImageContainer _img) {
        image_ = _img;
        clear();
    }

    public void setPath(final File _path) {
        path_ = _path;
    }

    boolean autoUpdate_ = true;

    protected void setValues(final Point2D.Double[] _img, final Point2D.Double[] _reel) {
        autoUpdate_ = false;
        pts_.removeAll(null);
        final GISPoint[] pts = new GISPoint[_img.length];
        final int nb = pts.length;
        final int[] x = new int[nb];
        final int[] y = new int[nb];
        for (int i = 0; i < nb; i++) {
            pts[i] = new GISPoint(_reel[i].x, _reel[i].y, 0);
            x[i] = (int) _img[i].x;
            y[i] = (int) _img[i].y;
        }
        final List data = new ArrayList(2);
        data.add(x);
        data.add(y);
        pts_.addAll(pts, data, null);
        autoUpdate_ = true;
    }

    /**
   * Attention: les pixels des images doivent partir d'en haut a gauche.
   */
    public void setProj(final Point2D.Double[] _img, final Point2D.Double[] _reel) {
        if (CtuluLibArray.isEmpty(_img) || CtuluLibArray.isEmpty(_reel)) {
            clear();
            return;
        }
        erreurs_ = null;
        try {
            setValues(_img, _reel);
            raster_ = CtuluLibGeometrie.projection(_img, _reel);
            if (raster_ == null) {
                clear();
            }
        } catch (final Exception _e) {
            FuLog.warning(_e);
            clear();
        }
    }

    /**
   * Attention: les pixels des images doivent partir d'en haut a gauche.
   */
    public void setProj(final Point2D.Double[] _img, final Point2D.Double[] _reel, final AffineTransform _trans, final double[] _err) {
        if (CtuluLibArray.isEmpty(_img) || CtuluLibArray.isEmpty(_reel)) {
            clear();
            return;
        }
        erreurs_ = CtuluLibArray.copy(_err);
        raster_ = new AffineTransform(_trans);
        setValues(_img, _reel);
    }

    public void setProjeMaxTaille(final int _maxSizePixel) {
        clear();
        final double maxSize = Math.max(getWImg(), getHImg());
        if (maxSize > _maxSizePixel) {
            final double ratio = _maxSizePixel / maxSize;
            final double newH = getHImg() * ratio;
            final double newW = getWImg() * ratio;
            final Point2D.Double[] img = new Point2D.Double[4];
            final Point2D.Double[] dest = new Point2D.Double[4];
            img[0] = new Point2D.Double(0, 0);
            dest[0] = new Point2D.Double(0, 0);
            img[1] = new Point2D.Double(0, getWImg());
            dest[1] = new Point2D.Double(0, newW);
            img[2] = new Point2D.Double(getHImg(), getWImg());
            dest[2] = new Point2D.Double(newH, newW);
            img[3] = new Point2D.Double(getHImg(), 0);
            dest[3] = new Point2D.Double(newH, 0);
            setProj(img, dest);
        } else {
            clear();
        }
    }

    public void attributeAction(Object _source, int att, GISAttributeInterface _att, int _action) {
    }

    public void attributeValueChangeAction(Object _source, int att, GISAttributeInterface _att, int geom, Object value) {
        autoUpdate();
    }

    public void objectAction(Object _source, int _indexObj, Object _obj, int _action) {
        autoUpdate();
    }
}
