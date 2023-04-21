package org.fudaa.ebli.calque;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JComponent;
import com.memoire.fu.FuLog;
import org.fudaa.ebli.geometrie.GrBoite;
import org.fudaa.ebli.geometrie.GrMaillage;
import org.fudaa.ebli.geometrie.GrMorphisme;
import org.fudaa.ebli.geometrie.VecteurGrContour;
import org.fudaa.ebli.palette.BPaletteCouleur;
import org.fudaa.ebli.palette.BPaletteCouleurSimple;
import org.fudaa.ebli.trace.BParametresGouraud;
import org.fudaa.ebli.trace.Gouraud;
import org.fudaa.ebli.trace.TraceIsoLignes;
import org.fudaa.ebli.trace.TraceIsoSurfaces;

/**
 * Un calque d'affichage de cartes.
 * 
 * @version $Id: BCalqueCarte.java,v 1.15 2006-11-14 09:06:24 deniger Exp $
 * @author Guillaume Desnoix , Axel von Arnim
 */
public class BCalqueCarte extends BCalqueAffichage {

    protected GrMaillage maillageEcran_;

    private GrBoite boite_;

    double vmin_, vmax_;

    public BCalqueCarte() {
        super();
        maillage_ = null;
        paletteCouleur_ = null;
        maillageEcran_ = null;
        boite_ = null;
        vmax_ = 255.;
        vmin_ = 0.;
        isosurfaces_ = true;
        isolignes_ = true;
        ecart_ = 0.10;
        paramGouraud_ = new BParametresGouraud();
        setBackground(Color.white);
        setForeground(Color.black);
    }

    public void reinitialise() {
        maillage_ = null;
        maillageEcran_ = null;
        boite_ = null;
        vmax_ = 255.;
        vmin_ = 0.;
    }

    protected void construitLegende() {
        final BCalqueLegende cqLg = getLegende();
        if (cqLg == null) {
            return;
        }
        cqLg.enleve(this);
        final BPaletteCouleurSimple paletteLeg = new BPaletteCouleurSimple();
        final BPaletteCouleurSimple privPal = (BPaletteCouleurSimple) getPaletteCouleur();
        paletteLeg.setEspace(privPal.getEspace());
        paletteLeg.setCouleurMin(privPal.getCouleurMin());
        paletteLeg.setCouleurMax(privPal.getCouleurMax());
        paletteLeg.setPaliers(privPal.getPaliers());
        paletteLeg.setCycles(privPal.getCycles());
        paletteLeg.setOrientation(BPaletteCouleurSimple.VERTICAL);
        final JComponent p = new JComponent() {

            public void paint(Graphics _g) {
                super.paint(_g);
                _g.setColor(BCalqueCarte.this.getForeground());
                _g.setFont(BCalqueCarte.this.getFont());
                NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                nf.setMaximumFractionDigits(2);
                double iv = vmin_;
                double av = vmax_;
                if (!Double.isNaN(minVal_)) {
                    iv = minVal_;
                }
                if (!Double.isNaN(maxVal_)) {
                    av = maxVal_;
                }
                _g.drawString(nf.format(av) + getM(), 0, _g.getFontMetrics().getHeight());
                _g.drawString(nf.format((iv + av) / 2) + getM(), 0, (this.getHeight() + _g.getFontMetrics().getHeight()) / 2);
                _g.drawString(nf.format(iv) + getM(), 0, this.getHeight());
            }

            public Dimension getPreferredSize() {
                if (this.isPreferredSizeSet()) {
                    return super.getPreferredSize();
                }
                NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
                nf.setMaximumFractionDigits(2);
                Graphics gcq = BCalqueCarte.this.getGraphics();
                FontMetrics fm = gcq.getFontMetrics();
                double iv = vmin_;
                double av = vmax_;
                if (!Double.isNaN(minVal_)) {
                    iv = minVal_;
                }
                if (!Double.isNaN(maxVal_)) {
                    av = maxVal_;
                }
                Rectangle2D r1 = fm.getStringBounds(nf.format(iv) + getM(), gcq);
                Rectangle2D r2 = fm.getStringBounds(nf.format(av) + getM(), gcq);
                Rectangle2D r3 = fm.getStringBounds(nf.format((iv + av) / 2) + getM(), gcq);
                double w = Math.max(Math.max(r1.getWidth(), r2.getWidth()), r3.getWidth());
                double h = r1.getHeight() + r2.getHeight() + r3.getHeight();
                return new Dimension((int) w, (int) h);
            }
        };
        p.setOpaque(false);
        cqLg.ajoute(this, paletteLeg, p);
    }

    public void paintIcon(final Component _c, final Graphics _g, final int _x, final int _y) {
        super.paintIcon(_c, _g, _x, _y);
        _g.translate(_x, _y);
        final boolean attenue = isAttenue();
        final int w = getIconWidth();
        final int h = getIconHeight();
        Color fg = getForeground();
        Color bg = getBackground();
        if (attenue) {
            fg = attenueCouleur(fg);
        }
        if (attenue) {
            bg = attenueCouleur(bg);
        }
        if (isosurfaces_) {
            paintIconIsoSurface(_g, attenue, w, h);
        }
        for (int i = 2; i < w - 5; i += 4) {
            for (int j = 2; j < h - 5; j += 4) {
                final int[] vx = new int[] { i, i + 4, i + 4, i };
                final int[] vy = new int[] { j, j, j + 4, j + 4 };
                final double v = (double) j / (double) h;
                Color c = paletteCouleur_.couleur(v);
                if (attenue) {
                    c = attenueCouleur(c);
                }
                if (surface_ && !isosurfaces_) {
                    _g.setColor(c);
                    _g.fillPolygon(vx, vy, 4);
                }
                if (contour_) {
                    if (surface_ || isolignes_ || isosurfaces_) {
                        _g.setColor(bg);
                    } else {
                        _g.setColor(c);
                    }
                    _g.drawPolygon(vx, vy, 4);
                }
            }
        }
        if (isolignes_) {
            paintIconIso(_g, attenue, w, h, fg);
        }
        _g.translate(-_x, -_y);
    }

    private void paintIconIsoSurface(final Graphics _g, final boolean _attenue, final int _w, final int _h) {
        Color c;
        c = paletteCouleur_.couleur(0.0);
        if (_attenue) {
            c = attenueCouleur(c);
        }
        _g.setColor(c);
        _g.fillRect(1, 1, _w - 1, _h - 1);
        c = paletteCouleur_.couleur(0.5);
        if (_attenue) {
            c = attenueCouleur(c);
        }
        _g.setColor(c);
        _g.fillOval(3, 3, _w - 5, _h - 5);
        c = paletteCouleur_.couleur(1.0);
        if (_attenue) {
            c = attenueCouleur(c);
        }
        _g.setColor(c);
        _g.fillOval(7, 7, _w - 14, _h - 14);
    }

    private void paintIconIso(final Graphics _g, final boolean _attenue, final int _w, final int _h, final Color _fg) {
        Color c;
        if (surface_ || isosurfaces_) {
            _g.setColor(_fg);
        } else {
            c = paletteCouleur_.couleur(0.5);
            if (_attenue) {
                c = attenueCouleur(c);
            }
            _g.setColor(c);
        }
        _g.drawOval(3, 3, _w - 5, _h - 5);
        if (surface_ || isosurfaces_) {
            _g.setColor(_fg);
        } else {
            c = paletteCouleur_.couleur(1.0);
            if (_attenue) {
                c = attenueCouleur(c);
            }
            _g.setColor(c);
        }
        _g.drawOval(7, 7, _w - 14, _h - 14);
    }

    public void paintComponent(final Graphics _g) {
        final boolean attenue = isAttenue();
        final boolean rapide = isRapide();
        if (boite_ == null) {
            boite_ = maillage_.boite();
        }
        final GrMorphisme versEcran = getVersEcran();
        final Polygon pecr = boite_.enPolygoneXY().applique(versEcran).polygon();
        Rectangle clip = _g.getClipBounds();
        if (clip == null) {
            clip = new Rectangle(0, 0, getWidth(), getHeight());
        }
        if (clip.intersects(pecr.getBounds())) {
            if (rapide) {
                paintRapide(_g, attenue, pecr);
            } else {
                paintNormal(_g, attenue, versEcran, clip);
            }
        }
        super.paintComponent(_g);
    }

    private void paintNormal(final Graphics _g, final boolean _attenue, final GrMorphisme _versEcran, final Rectangle _clip) {
        double iv = vmin_;
        double av = vmax_;
        if (!Double.isNaN(minVal_)) {
            iv = minVal_;
        }
        if (!Double.isNaN(maxVal_)) {
            av = maxVal_;
        }
        if (maillageEcran_ == null) {
            maillageEcran_ = maillage_.applique(_versEcran);
        }
        Color fg = getForeground();
        Color bg = getBackground();
        if (_attenue) {
            fg = attenueCouleur(fg);
        }
        if (_attenue) {
            bg = attenueCouleur(bg);
        }
        BPaletteCouleur piso = paletteCouleur_;
        if (surface_ || isosurfaces_) {
            final BPaletteCouleurSimple ps = new BPaletteCouleurSimple();
            ps.setCouleurMin(fg);
            ps.setCouleurMax(fg);
            piso = ps;
        }
        final TraceIsoLignes isol = new TraceIsoLignes(ecart_, piso);
        final TraceIsoSurfaces isos = new TraceIsoSurfaces(ecart_, paletteCouleur_);
        if (isosurfaces_) {
            final int n = maillageEcran_.nombre();
            for (int i = 0; i < n; i++) {
                final Polygon p = maillageEcran_.polygon(i);
                if (_clip.intersects(p.getBounds())) {
                    final int[] noeuds = (int[]) maillageEcran_.connectivites_.get(i);
                    final int m = noeuds.length;
                    final double[] v = new double[m];
                    for (int j = 0; j < m; j++) {
                        v[j] = Math.max(0., Math.min(1., (valeurs_[noeuds[j]] - iv) / (av - iv)));
                    }
                    isos.draw(_g, p, v);
                }
            }
        }
        if (contour_ || surface_) {
            paintSurfaces(_g, _attenue, _clip, iv, av, bg);
        }
        if (isolignes_) {
            paintIso(_g, _clip, iv, av, isol);
        }
    }

    private void paintSurfaces(final Graphics _g, final boolean _attenue, final Rectangle _clip, final double _iv, final double _av, final Color _bg) {
        Gouraud grd = null;
        if (_g instanceof PrintGraphics) {
            if (FuLog.isDebug()) {
                FuLog.debug("EBL: Gouraud imprimante");
            }
            grd = new Gouraud(_g, 2, 2);
        } else {
            if (FuLog.isDebug()) {
                FuLog.debug("EBL: Gouraud �cran");
            }
            grd = new Gouraud(_g, paramGouraud_.getNiveau(), paramGouraud_.getTaille());
        }
        final int n = maillageEcran_.nombre();
        for (int i = 0; i < n; i++) {
            final Polygon p = maillageEcran_.polygon(i);
            if (_clip.intersects(p.getBounds())) {
                final int[] noeuds = (int[]) maillageEcran_.connectivites_.get(i);
                final int m = noeuds.length;
                if (surface_ && !isosurfaces_) {
                    final int[] rc = new int[m];
                    final int[] vc = new int[m];
                    final int[] bc = new int[m];
                    for (int j = 0; j < m; j++) {
                        final double v = (valeurs_[noeuds[j]] - _iv) / (_av - _iv);
                        Color c = paletteCouleur_.couleur(v);
                        if (_attenue) {
                            c = attenueCouleur(c);
                        }
                        rc[j] = c.getRed();
                        vc[j] = c.getGreen();
                        bc[j] = c.getBlue();
                    }
                    grd.fillRectangle(p.xpoints, p.ypoints, rc, vc, bc);
                }
                if (contour_) {
                    double v = 0.;
                    for (int j = 0; j < m; j++) {
                        v += (valeurs_[noeuds[j]] - _iv) / (_av - _iv);
                    }
                    v /= m;
                    Color c = paletteCouleur_.couleur(v);
                    if (_attenue) {
                        c = attenueCouleur(c);
                    }
                    if (surface_ || isolignes_ || isosurfaces_) {
                        _g.setColor(_bg);
                    } else {
                        _g.setColor(c);
                    }
                    _g.drawPolygon(p);
                }
            }
        }
    }

    private void paintIso(final Graphics _g, final Rectangle _clip, final double _iv, final double _av, final TraceIsoLignes _isol) {
        final int n = maillageEcran_.nombre();
        for (int i = 0; i < n; i++) {
            final Polygon p = maillageEcran_.polygon(i);
            if (_clip.intersects(p.getBounds())) {
                final int[] noeuds = (int[]) maillageEcran_.connectivites_.get(i);
                final int m = noeuds.length;
                final double[] v = new double[m];
                for (int j = 0; j < m; j++) {
                    v[j] = Math.max(0., Math.min(1., (valeurs_[noeuds[j]] - _iv) / (_av - _iv)));
                }
                _isol.draw(_g, p, v);
            }
        }
    }

    private void paintRapide(final Graphics _g, final boolean _attenue, final Polygon _pecr) {
        Color c;
        if (surface_) {
            c = paletteCouleur_.couleur(0.5);
            if (_attenue) {
                c = attenueCouleur(c);
            }
            _g.setColor(c);
            _g.fillPolygon(_pecr);
        }
        c = paletteCouleur_.couleur(0.7);
        if (_attenue) {
            c = attenueCouleur(c);
        }
        _g.setColor(c);
        _g.drawPolygon(_pecr);
    }

    public GrBoite getDomaine() {
        GrBoite r = super.getDomaine();
        if (maillage_ != null) {
            final GrBoite b = maillage_.boite();
            if (r == null) {
                r = b;
            } else {
                r = r.union(b);
            }
        }
        return r;
    }

    /**
   * Renvoi de la liste des elements selectionnables.
   */
    public VecteurGrContour contours() {
        return new VecteurGrContour();
    }

    public void setVersEcran(final GrMorphisme _v) {
        maillageEcran_ = null;
        super.setVersEcran(_v);
    }

    private GrMaillage maillage_;

    public GrMaillage getMaillage() {
        return maillage_;
    }

    public void setMaillage(final GrMaillage _maillage) {
        if (maillage_ != _maillage) {
            final GrMaillage vp = maillage_;
            maillage_ = _maillage;
            boite_ = null;
            maillageEcran_ = null;
            firePropertyChange("maillage", vp, maillage_);
            repaint();
        }
    }

    private double[] valeurs_;

    public double[] getValeurs() {
        return valeurs_;
    }

    public void setValeurs(final double[] _valeurs) {
        if (valeurs_ != _valeurs) {
            final double[] vp = valeurs_;
            valeurs_ = _valeurs;
            firePropertyChange("valeurs", vp, valeurs_);
            vmax_ = Double.MIN_VALUE;
            vmin_ = Double.MAX_VALUE;
            for (int i = 0; i < valeurs_.length; i++) {
                final double v = valeurs_[i];
                if (vmin_ > v) {
                    vmin_ = v;
                }
                if (vmax_ < v) {
                    vmax_ = v;
                }
            }
            construitLegende();
            repaint();
        }
    }

    private BPaletteCouleur paletteCouleur_;

    public BPaletteCouleur getPaletteCouleur() {
        return paletteCouleur_;
    }

    public void setPaletteCouleur(final BPaletteCouleur _palette) {
        if ((_palette != null) && (paletteCouleur_ != _palette)) {
            final BPaletteCouleur vp = paletteCouleur_;
            paletteCouleur_ = _palette;
            construitLegende();
            firePropertyChange("paletteCouleur", vp, paletteCouleur_);
            quickRepaint();
        }
    }

    public Color getCouleur() {
        return getForeground();
    }

    private BParametresGouraud paramGouraud_;

    public BParametresGouraud getParametresGouraud() {
        return paramGouraud_;
    }

    public void setParametresGouraud(final BParametresGouraud _paramGouraud) {
        final BParametresGouraud vp = paramGouraud_;
        paramGouraud_ = _paramGouraud;
        firePropertyChange("parametresGouraud", vp, paramGouraud_);
        quickRepaint();
    }

    private boolean contour_;

    public boolean getContour() {
        return contour_;
    }

    public void setContour(final boolean _v) {
        if (_v != contour_) {
            final boolean vp = contour_;
            contour_ = _v;
            firePropertyChange("contour", vp, contour_);
            repaint();
        }
    }

    private boolean surface_;

    public boolean getSurface() {
        return surface_;
    }

    public void setSurface(final boolean _v) {
        if (_v != surface_) {
            final boolean vp = surface_;
            surface_ = _v;
            firePropertyChange("surface", vp, surface_);
            repaint();
        }
    }

    private boolean isolignes_;

    public boolean getIsolignes() {
        return isolignes_;
    }

    public void setIsolignes(final boolean _v) {
        if (_v != isolignes_) {
            final boolean vp = isolignes_;
            isolignes_ = _v;
            firePropertyChange("isolignes", vp, isolignes_);
            repaint();
        }
    }

    private boolean isosurfaces_;

    public boolean getIsosurfaces() {
        return isosurfaces_;
    }

    public void setIsosurfaces(final boolean _v) {
        if (_v != isosurfaces_) {
            final boolean vp = isosurfaces_;
            isosurfaces_ = _v;
            firePropertyChange("isosurfaces", vp, isosurfaces_);
            repaint();
        }
    }

    private double ecart_;

    public double getEcart() {
        return ecart_;
    }

    public void setEcart(final double _v) {
        double v = _v;
        double iv = vmin_;
        double av = vmax_;
        if (!Double.isNaN(minVal_)) {
            iv = minVal_;
        }
        if (!Double.isNaN(maxVal_)) {
            av = maxVal_;
        }
        v = Math.round((100. * v / (av - iv))) / 100.;
        if (v < 0.01) {
            v = 0.01;
        }
        if (v > 0.50) {
            v = 0.50;
        }
        if (v != ecart_) {
            final double vp = ecart_;
            ecart_ = v;
            firePropertyChange("ecart", vp, ecart_);
            quickRepaint();
        }
    }

    double minVal_ = Double.NaN;

    public double getMinValeur() {
        return minVal_;
    }

    public void setMinValeur(final double _v) {
        if (_v != minVal_) {
            final double vp = minVal_;
            minVal_ = _v;
            firePropertyChange("minValeur", vp, minVal_);
            quickRepaint();
        }
    }

    double maxVal_ = Double.NaN;

    public double getMaxValeur() {
        return maxVal_;
    }

    public void setMaxValeur(final double _v) {
        if (_v != maxVal_) {
            final double vp = maxVal_;
            maxVal_ = _v;
            firePropertyChange("maxValeur", vp, maxVal_);
            quickRepaint();
        }
    }

    private boolean paletteLocale_;

    public boolean getPaletteLocale() {
        return paletteLocale_;
    }

    public void setPaletteLocale(final boolean _v) {
        if (_v != paletteLocale_) {
            final boolean vp = paletteLocale_;
            paletteLocale_ = _v;
            firePropertyChange("paletteLocale", vp, paletteLocale_);
            quickRepaint();
        }
    }

    public static String getM() {
        return "m";
    }
}
