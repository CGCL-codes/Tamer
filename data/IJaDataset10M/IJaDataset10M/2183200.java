package org.fudaa.dodico.ef.serviceDunes;

import org.fudaa.dodico.corba.dunes.ICalculDunes;
import org.fudaa.dodico.corba.ef.IElement;
import org.fudaa.dodico.corba.ef.IMaillage;
import org.fudaa.dodico.corba.ef.INoeud;
import org.fudaa.dodico.corba.ef.IParametresCalcul;
import org.fudaa.dodico.corba.ef.IRegion;
import org.fudaa.dodico.corba.ef.ITrou;
import org.fudaa.dodico.corba.geometrie.*;
import org.fudaa.dodico.corba.usine.IUsine;
import org.fudaa.dodico.objet.CDodico;

/**
 * Classe statique de conversion: les methodes statiques convertissent les interfaces en structures et inversement.
 * Toutes les fonctions sont simples. Si les parametres d'entrees sont invalides, un tableau vide est renvoye.
 * 
 * @version $Id: Convertisseur.java,v 1.17 2006-10-19 14:12:34 deniger Exp $
 * @author deniger
 */
public final class Convertisseur {

    /**
   * Conversion d'un IPoint en SPoint.
   * 
   * @param _point
   * @return le SPoint correspondant a _point
   */
    public static SPoint transformIPoint2S(final IPoint _point) {
        if (_point == null) {
            return null;
        }
        final SPoint r = new SPoint();
        final double[] p = _point.coordonnees();
        r.x = p[0];
        r.y = p[1];
        r.z = p[2];
        return r;
    }

    /**
   * Conversion de _points en un tableau de IPoint[].
   * 
   * @param _points
   * @return le tableau convertit ou un tableau vide si _points est nul.
   */
    public static SPoint[] transformIPoints2S(final IPoint[] _points) {
        if (_points == null) {
            return new SPoint[0];
        }
        final int longueur = _points.length;
        final SPoint[] spoints = new SPoint[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            spoints[i] = transformIPoint2S(_points[i]);
        }
        return spoints;
    }

    /**
   * Conversion de l'interface _poly en Structure.
   * 
   * @param _poly
   */
    public static SPolyligne transformIPolyligne2S(final IPolyligne _poly) {
        if (_poly == null) {
            return null;
        }
        final SPolyligne r = new SPolyligne();
        final int longueur = _poly.points().taille();
        final IPoint[] tempo = new IPoint[longueur];
        for (int i = 0; i < longueur; i++) {
            tempo[i] = IPointHelper.narrow(_poly.points().element(i));
        }
        r.points = transformIPoints2S(tempo);
        return r;
    }

    /**
   * Conversion du tableau de IPolyligne _polys en Structure.
   * 
   * @param _polys
   */
    public static SPolyligne[] transformIPolylignes2S(final IPolyligne[] _polys) {
        if (_polys == null) {
            return null;
        }
        final int longueur = _polys.length;
        final SPolyligne[] r = new SPolyligne[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            r[i] = transformIPolyligne2S(_polys[i]);
        }
        return r;
    }

    /**
   * Transforme l'interface _region en structure equivalente.
   * 
   * @param _region
   */
    public static SRegion transformIRegion2S(final IRegion _region) {
        if (_region == null) {
            return null;
        }
        final SRegion r = new SRegion();
        r.position = transformIPoint2S(_region.position());
        r.aireMaxElement = _region.aireMaxElement();
        return r;
    }

    /**
   * Transforme le tableau d'interfaces _regions en tableau de structures equivalentes.
   * 
   * @param _regions
   */
    public static SRegion[] transformIRegions2S(final IRegion[] _regions) {
        if (_regions == null) {
            return new SRegion[0];
        }
        final int longueur = _regions.length;
        final SRegion[] sregion = new SRegion[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            sregion[i] = transformIRegion2S(_regions[i]);
        }
        return sregion;
    }

    /**
   * Transforme l'interface _trou en structure equivalente.
   * 
   * @param _trou
   */
    public static STrou transformITrou2S(final ITrou _trou) {
        if (_trou == null) {
            return null;
        }
        final STrou r = new STrou();
        r.position = transformIPoint2S(_trou.position());
        return r;
    }

    /**
   * Transforme le tableau d'interfaces _trous en tableau de structures equivalentes.
   * 
   * @param _trous
   */
    public static STrou[] transformITrous2S(final ITrou[] _trous) {
        if (_trous == null) {
            return null;
        }
        final int longueur = _trous.length;
        final STrou[] strou = new STrou[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            strou[i] = transformITrou2S(_trous[i]);
        }
        return strou;
    }

    /**
   * Met a jour les parametres de ICalculDunes a partir des IParametresCalcul issus du modele metier. L'operation est
   * effectuee si les 2 parametres sont non nuls (envoie exceptions sinon). Verifie egalement le typeElement qui doit
   * etre egale a T3 ou T6 pour le code de calcul Dunes (exception sinon).
   * 
   * @param _calCode le calcul
   * @param _paramMetier les parametres
   * @return true si deroulement complet et correct et false sion
   */
    public static boolean majICalculDunes(final ICalculDunes _calCode, final IParametresCalcul _paramMetier) {
        if (_paramMetier == null) {
            CDodico.exception(Convertisseur.class, "Les parametres metiers sont nuls.");
            return false;
        }
        if (_calCode == null) {
            CDodico.exception(Convertisseur.class, "L'objet ICalculDunes est nul.");
            return false;
        }
        _calCode.optionP(true);
        _calCode.optionQ(_paramMetier.controleAngles());
        _calCode.optionA(_paramMetier.controleAires());
        _calCode.optionS(!_paramMetier.ajoutPoints());
        _calCode.optionO(_paramMetier.maillageParLongueurOnde());
        _calCode.optionC(_paramMetier.ajoutEnveloppeConvexe());
        _calCode.optionY(!_paramMetier.ajoutNoeudsContour());
        _calCode.aireMax(_paramMetier.aireMax());
        _calCode.angleMin(_paramMetier.angleMin());
        _calCode.hauteurMer(_paramMetier.hauteurMer());
        _calCode.periodeHoule(_paramMetier.periodeHoule());
        _calCode.nombrePointsLongueurOnde(_paramMetier.nombrePoints());
        if ((_paramMetier.typeElement() == LTypeElement.T6) || (_paramMetier.typeElement() == LTypeElement.T3)) {
            _calCode.typeElementDunes(_paramMetier.typeElement());
            return true;
        }
        CDodico.exception(Convertisseur.class, "Dunes g�re des �l�ments de type T3 ou T6 uniquement");
        return false;
    }

    /**
   * Transforme le tableau d'interfaces _elements en tableau de structures equivalentes.
   * 
   * @param _nds les noeuds a transformer
   * @param _elements les elements a transformer
   * @param _u l'usine a utiliser pour creer les interfaces corba
   * @return les ielements
   */
    public static IElement[] transformSElement2I(final SNoeud[] _nds, final SElement[] _elements, final IUsine _u) {
        if ((_elements == null) || (_u == null)) {
            return new IElement[0];
        }
        final int longueur = _elements.length;
        final IElement[] ielem = new IElement[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            ielem[i] = transformSElement2I(_nds, _elements[i], _u);
        }
        return ielem;
    }

    /**
   * Transforme l'interface _element en structure equivalente.
   * 
   * @param _element
   * @param _u
   * @param _pts les noeuds du maillage
   */
    public static IElement transformSElement2I(final SNoeud[] _pts, final SElement _element, final IUsine _u) {
        if ((_element == null) || (_u == null)) {
            return null;
        }
        final IElement r = _u.creeEfElement();
        final SNoeud[] snds = new SNoeud[_element.noeudsIdx.length];
        for (int j = _element.noeudsIdx.length - 1; j >= 0; j--) {
            snds[j] = _pts[_element.noeudsIdx[j]];
        }
        r.noeuds(transformSNoeud2I(snds, _u));
        if ((_element.type == LTypeElement.T3) || (_element.type == LTypeElement.T6)) {
            r.type(_element.type);
        } else {
            CDodico.exception(Convertisseur.class, "Dunes g�re des �l�ments de type T3 ou T6 uniquement");
            return null;
        }
        return r;
    }

    /**
   * @param _smaillage
   * @param _u
   */
    public static IMaillage transformSMaillage2I(final SMaillage _smaillage, final IUsine _u) {
        if ((_smaillage == null) || (_u == null)) {
            return null;
        }
        final IMaillage r = _u.creeEfMaillage();
        r.elements(transformSElement2I(_smaillage.noeuds, _smaillage.elements, _u));
        return r;
    }

    /**
   * Transforme l'interface _noeud en structure equivalente.
   * 
   * @param _noeud
   * @param _u
   */
    public static INoeud transformSNoeud2I(final SNoeud _noeud, final IUsine _u) {
        if ((_noeud == null) || (_u == null)) {
            return null;
        }
        final INoeud r = _u.creeEfNoeud();
        r.point(transformSPoint2I(_noeud.point, _u));
        return r;
    }

    /**
   * Transforme le tableau d'interfaces _noeuds en tableau de structures equivalentes.
   * 
   * @param _noeuds
   * @param _u
   */
    public static INoeud[] transformSNoeud2I(final SNoeud[] _noeuds, final IUsine _u) {
        if ((_noeuds == null) || (_u == null)) {
            return new INoeud[0];
        }
        final int longueur = _noeuds.length;
        final INoeud[] inoeud = new INoeud[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            inoeud[i] = transformSNoeud2I(_noeuds[i], _u);
        }
        return inoeud;
    }

    public static IPoint transformSPoint2I(final SPoint _point, final IUsine _u) {
        if ((_point == null) || (_u == null)) {
            return null;
        }
        final IPoint r = _u.creeGeometriePoint();
        r.coordonnees(new double[] { _point.x, _point.y, _point.z });
        return r;
    }

    public static IPoint[] transformSPoint2I(final SPoint[] _points, final IUsine _u) {
        if ((_points == null) || (_u == null)) {
            return null;
        }
        final int longueur = _points.length;
        final IPoint[] r = new IPoint[longueur];
        for (int i = longueur - 1; i >= 0; i--) {
            r[i] = transformSPoint2I(_points[i], _u);
        }
        return r;
    }

    /**
   * Singleton. Constructeur prive.
   */
    private Convertisseur() {
    }
}
