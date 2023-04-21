package org.fudaa.ebli.volume;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.fudaa.ebli.commun.EbliLib;
import org.fudaa.ebli.geometrie.GrBoite;

/**
 * Composant AWT de visualisation de la scene 3D.
 * 
 * @version $Revision: 1.9 $ $Date: 2007-05-22 14:19:03 $ by $Author: deniger $
 * @author Christophe Delhorbe
 */
public class BCanvas3D extends Canvas3D {

    public static GraphicsConfiguration getDefaultConf() {
        return com.sun.j3d.utils.universe.SimpleUniverse.getPreferredConfiguration();
    }

    /**
   * Constructeur de BUnivers. Cree un SimpleUniverse,recupere la matrice de transformation de la camera, et un Switch.
   * 
   * @param _gc a GraphicsConfiguration (=null)
   */
    public BCanvas3D(final GraphicsConfiguration _gc) {
        super(_gc == null ? getDefaultConf() : _gc);
        setStereoEnable(false);
        setDoubleBufferEnable(true);
    }

    /**
   * gele le ZBuffer de transparence.
   * 
   * @param _valeur booleen, gele si vrai
   */
    public void freeze(final boolean _valeur) {
        getView().setDepthBufferFreezeTransparent(_valeur);
    }

    BCanvas3DOffScreen off_;

    /**
   * Imprime le canvas dans une image.
   */
    public BufferedImage print() {
        if (off_ == null) {
            off_ = new BCanvas3DOffScreen(this);
        }
        return off_.produceImage(this);
    }

    /**
   * Fonction appel�e par le moteur de rendu apres l'echange des buffers ( derniere methode appel�e).
   */
    public void postSwap() {
        firePropertyChange("swap", Boolean.valueOf(false), Boolean.valueOf(true));
        super.postSwap();
    }

    public static BGroupeLumiere getGroupeLumiere(final BGroupeVolume _gv) {
        final BGroupeLumiere gl = new BGroupeLumiere();
        gl.setName(EbliLib.getS("Lumi�res"));
        final BLumiereDirectionnelle l1 = createLumiere(_gv);
        l1.setName(EbliLib.getS("Principale"));
        gl.add(l1);
        return gl;
    }

    public static BLumiereDirectionnelle createLumiere(final BGroupeVolume _gv) {
        GrBoite boite;
        boite = _gv.getBoite();
        final double dx = Math.abs(boite.getDeltaX());
        final double dz = Math.abs(boite.getDeltaZ());
        final double dy = Math.abs(boite.getDeltaY());
        final BLumiereDirectionnelle l1 = new BLumiereDirectionnelle(new Vector3f(0, 0, -1), Color.white);
        double dist = (dx * dx + dy * dy) / 4;
        dist = Math.sqrt(dz * dz + dist);
        final BoundingSphere sphere = new BoundingSphere(new Point3d(boite.o_.x_ + dx / 2, boite.o_.y_ + dy / 2, boite.e_.z_), dist);
        l1.setBounds(sphere);
        l1.setIntensite(0.3);
        l1.setDirection(new Vector3f(0, 0.1f, -1f));
        l1.setRapide(false);
        l1.setVisible(true);
        return l1;
    }
}
