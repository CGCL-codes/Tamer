package org.fudaa.ctulu.interpolation.profile;

import org.fudaa.ctulu.CtuluAnalyze;
import org.fudaa.ctulu.gis.CtuluLibGeometrie;
import org.fudaa.ctulu.gis.GISGeometryFactory;
import org.fudaa.ctulu.gis.GISLib;
import org.fudaa.ctulu.gis.GISPoint;
import com.memoire.fu.FuLog;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Calculateur de profil en travers � partir d'un nuage de points.
 * @author J.B. Faure
 * @author Bertrand Marchand
 * @version $Id: ProfileCalculator.java 6184 2011-03-30 08:57:28Z bmarchan $
 */
public class ProfileCalculator {

    /** Le nuage de points */
    PointCloudI cloud_;

    /** La trace. Peut �tre d�termin�e automatiquement, ou donn�e. */
    GISPoint[] ptsTrace_;

    /** La fenetre de selection */
    ProfileCalculatorWindow window_;

    /**
   * Constructeur, vide.
   */
    public ProfileCalculator() {
    }

    /**
   * D�finit la trace a utiliser.
   * @param _pts Les points dans l'ordre dans lequel ils sont reli�s.
   */
    public void setTrace(GISPoint[] _pts) {
        ptsTrace_ = _pts;
    }

    /**
   * Retourne la trace, celle d�finie ou calcul�e.
   * @return La trace.
   */
    public GISPoint[] getTrace() {
        return ptsTrace_;
    }

    /**
   * Definit la fenetre de selection. En principe, les points du nuage a prendre en compte sont dans
   * cette fenetre.
   * @param _w La fenetre de selection.
   */
    public void setWindow(ProfileCalculatorWindow _w) {
        window_ = _w;
    }

    /**
   * @return La fenetre de selection.
   */
    public ProfileCalculatorWindow getWindow() {
        return window_;
    }

    /**
   * D�finit le nuage de points.
	 * @param _cloud Le nuage de points.
	 */
    public void setCloud(PointCloudI _cloud) {
        cloud_ = _cloud;
    }

    /**
   * @return Le nuage de points.
   */
    public PointCloudI getCloud() {
        return cloud_;
    }

    /**
   * Calcule la trace a partir des points du nuage et du rectangle de selection des points du nuage.
   * TODO : A r�implementer.
   */
    public void computeDefaultTrace() {
        if (window_ == null) throw new IllegalStateException("Window points aren't defined");
        ptsTrace_ = new GISPoint[2];
        Coordinate c1 = GISLib.computeMiddle(window_.getCorner(0).getCoordinate(), window_.getCorner(1).getCoordinate());
        Coordinate c2 = GISLib.computeMiddle(window_.getCorner(2).getCoordinate(), window_.getCorner(3).getCoordinate());
        ptsTrace_[0] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(c1);
        ptsTrace_[1] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(c2);
    }

    /**
	 * calcul de la pente moyenne du nuage de points obtenu par getCloud() � l'int�rieur 
	 * de la fen�tre de s�lection
   * TODO : A r�implementer.
	 */
    private double penteMoyenne(ProfileCalculatorWindow _win) {
        PointCloudI cloudRestreint = _win.select(cloud_);
        double x1 = ptsTrace_[0].getX();
        double y1 = ptsTrace_[0].getY();
        double z1 = ptsTrace_[0].getZ();
        double x2 = ptsTrace_[ptsTrace_.length - 1].getX();
        double y2 = ptsTrace_[ptsTrace_.length - 1].getY();
        double z2 = ptsTrace_[ptsTrace_.length - 1].getZ();
        double dist = CtuluLibGeometrie.getDistance3D(x1, y1, z1, x2, y2, z2);
        double pente = 10 / dist;
        return pente;
    }

    /**
   * Calcul du profil � partir du nuage de points.
   * NB : on impose de fournir la fen�tre pour �tre s�r d'avoir un rectangle
   * @param _algo : flag de la m�thode � utiliser
   * @param _ana La trace d'ex�cution (pamaretre de sortie).
   * @return Les points, ou null s'il y a eu une erreur lors de l'ex�cution
   * 
   * TODO : A r�implementer.
	 */
    public GISPoint[] extractProfile(int _algo, CtuluAnalyze _ana) {
        FuLog.debug("CTU:Cloud Nb points=" + cloud_.getNbPoints());
        FuLog.debug("CTU:Fenetre selection Nb points=" + window_.select(cloud_).getNbPoints());
        if (_algo == 0) return new InterpolationProfilTest(ptsTrace_, cloud_).process(); else if (_algo == 1) {
            return new InterpolationProfilRefineAndProj(ptsTrace_, cloud_).process(_ana);
        } else if (_algo == 2) {
            double x;
            double y;
            double[] zh = new double[3];
            for (int i = 0; i < 3; i++) {
                GISPoint[] pts = new GISPoint[4];
                x = window_.getCorner(0).getX() + (window_.getCorner(3).getX() - window_.getCorner(0).getX()) * i / 3.;
                y = window_.getCorner(0).getY() + (window_.getCorner(3).getY() - window_.getCorner(0).getY()) * i / 3.;
                pts[0] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(x, y, 0);
                x = window_.getCorner(1).getX() + (window_.getCorner(2).getX() - window_.getCorner(1).getX()) * i / 3.;
                y = window_.getCorner(1).getY() + (window_.getCorner(2).getY() - window_.getCorner(1).getY()) * i / 3.;
                pts[1] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(x, y, 0);
                x = window_.getCorner(1).getX() + (window_.getCorner(2).getX() - window_.getCorner(1).getX()) * (i + 1) / 3.;
                y = window_.getCorner(1).getY() + (window_.getCorner(2).getY() - window_.getCorner(1).getY()) * (i + 1) / 3.;
                pts[2] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(x, y, 0);
                x = window_.getCorner(0).getX() + (window_.getCorner(3).getX() - window_.getCorner(0).getX()) * (i + 1) / 3.;
                y = window_.getCorner(0).getY() + (window_.getCorner(3).getY() - window_.getCorner(0).getY()) * (i + 1) / 3.;
                pts[3] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(x, y, 0);
                ProfileCalculatorWindow win = new ProfileCalculatorWindow(pts);
                PointCloudI cloudwin = win.select(cloud_);
                zh[i] = 0;
                for (int j = 0; j < cloudwin.getNbPoints(); j++) {
                    zh[i] += cloudwin.getZ(j);
                }
                zh[i] /= cloudwin.getNbPoints();
                FuLog.debug("CTU:Sous fenetre " + (i + 1) + " Nb points=" + cloudwin.getNbPoints() + ", Zh=" + zh[i]);
            }
            GISPoint[] prof = new GISPoint[ptsTrace_.length];
            for (int i = 0; i < ptsTrace_.length; i++) {
                double z = zh[(i * 3) / ptsTrace_.length];
                prof[i] = (GISPoint) GISGeometryFactory.INSTANCE.createPoint(ptsTrace_[i].getX(), ptsTrace_[i].getY(), z);
            }
            return prof;
        } else return null;
    }
}
