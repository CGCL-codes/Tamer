package org.fudaa.dodico.corba;

import org.fudaa.dodico.corba.geometrie.IPoint;
import org.fudaa.dodico.corba.geometrie.IPolygone;
import org.fudaa.dodico.corba.geometrie.IPolygoneHelper;
import org.fudaa.dodico.objet.UsineLib;

/**
 * Une classe de test de CPolygone.
 * 
 * @version $Revision: 1.3 $ $Date: 2006-09-19 14:45:46 $ by $Author: deniger $
 * @author Axel von Arnim
 */
public final class TestCPolygone {

    private TestCPolygone() {
    }

    public static void affiche(String _name, IPolygone _o) {
        System.out.println("Nom       : " + _name);
        System.out.println("Classe    : " + _o.getClass().getName());
        System.out.println("Module    : " + _o.moduleCorba());
        String[] io = _o.interfacesCorba();
        for (int i = 0; i < io.length; i++) {
            System.out.println("Interface : " + io[i]);
        }
        System.out.println("Texte     : " + _o);
        System.out.println("Dimension : " + _o.dimension());
        System.out.println("Boite     : " + _o.boite());
        System.out.println("Centre apparent: " + _o.centreApparent());
        System.out.println("Barycentre: " + _o.barycentre());
        System.out.println("Aire      : " + _o.aire());
        System.out.println("perimetre : " + _o.perimetre());
        System.out.println("AireXY    : " + _o.aireXY());
        System.out.println("perimetreXY: " + _o.perimetreXY());
        System.out.println("ContientXY (0,0): " + _o.contientPointXY(UsineLib.findUsine().creeGeometriePoint()));
        System.out.println("");
    }

    public static void main(String[] _args) {
        double[][] h4in = { { 2., 2., 0., 1. }, { 2., 0., 0., 1. }, { 1., -1., 0., 1. } };
        IPolygone p1 = UsineLib.findUsine().creeGeometriePolygone();
        affiche("p1", p1);
        IPoint point1 = UsineLib.findUsine().creeGeometriePoint();
        point1.coordonnees(new double[] { 1, 2, 0 });
        IPoint point2 = UsineLib.findUsine().creeGeometriePoint();
        point2.coordonnees(new double[] { 0, 0, 0 });
        IPoint point3 = UsineLib.findUsine().creeGeometriePoint();
        point3.coordonnees(new double[] { -2, 0, 0 });
        affiche("p1", p1);
        double[][] h4out = p1.h4();
        System.out.println("p1 H4:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + "," + j + ": " + h4out[i][j]);
            }
            System.out.println("");
        }
        p1.h4(h4in);
        affiche("p1", p1);
        h4out = p1.h4();
        System.out.println("p1 H4:");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.println(i + "," + j + ": " + h4out[i][j]);
            }
            System.out.println("");
        }
        IPolygone p2 = UsineLib.findUsine().creeGeometriePolygone();
        affiche("p2", p2);
        IPolygone p3 = IPolygoneHelper.narrow(p2.creeClone());
        affiche("p3", p3);
        point1.coordonnees(new double[] { 1, 3, 0 });
        affiche("p2", p2);
        affiche("p3", p3);
        IPolygone p4 = UsineLib.findUsine().creeGeometriePolygone();
        p4.initialise(p2);
        affiche("p4", p4);
    }
}
