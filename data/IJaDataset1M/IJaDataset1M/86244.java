package org.fudaa.fudaa.reflux.calcul;

import org.fudaa.dodico.corba.calcul.ICalcul;
import org.fudaa.dodico.corba.reflux3d.ICalculReflux3dHelper;
import org.fudaa.dodico.objet.CDodico;
import org.fudaa.dodico.objet.UsineLib;

public class PRCalculReflux3d {

    /**
   * Serveur de calcul.
   */
    private static ICalcul serveur_ = null;

    /**
   * Recherche ou non d'un serveur distant.
   */
    public static boolean distant = false;

    /**
   * Initialisation du serveur de calcul.
   * @param _distant
   *  <i>true</i>Recherche un serveur de calcul distant, et cr�e un serveur
   *             local en cas d'�chec.
   *  <i>false</i>Cr�e un serveur local sans recherche d'un distant.
   */
    public static void initialiser(boolean _distant) {
        if (_distant) {
            System.out.println("Recherche d'un serveur Reflux 3D distant...");
            serveur_ = ICalculReflux3dHelper.narrow(CDodico.findServerByInterface("::reflux3d::ICalculReflux3d", 4000));
        }
        if (serveur_ == null) {
            System.out.println("Cr�ation d'un serveur Reflux 3D local...");
            serveur_ = UsineLib.findUsine().creeReflux3dCalculReflux3d();
        }
    }

    /**
   * Retourne le serveur associ� ou null s'il n'a pas �t� initialis�.
   * @return Le serveur de calcul.
   */
    public static ICalcul getServeur() {
        if (serveur_ == null) initialiser(distant);
        return serveur_;
    }
}
