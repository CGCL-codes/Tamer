package fr.ign.cogit.geoxygene.contrib.geometrie;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPosition;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IDirectPositionList;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.ILineString;
import fr.ign.cogit.geoxygene.api.spatial.coordgeom.IPolygon;
import fr.ign.cogit.geoxygene.api.spatial.geomaggr.IMultiSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomcomp.ICompositeSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IOrientableSurface;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IPoint;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.IRing;
import fr.ign.cogit.geoxygene.api.spatial.geomprim.ISurface;
import fr.ign.cogit.geoxygene.api.spatial.geomroot.IGeometry;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_LineString;
import fr.ign.cogit.geoxygene.spatial.coordgeom.GM_Polygon;
import fr.ign.cogit.geoxygene.spatial.geomaggr.GM_MultiSurface;

/**
 * Méthodes statiques de calcul de distance.
 * <p>
 * English: Computation of distances (static methods)
 * 
 * @author Mustière/Bonin
 * @version 1.0
 */
public abstract class Distances {

    @Deprecated
    public static double distance(IDirectPosition dp1, IDirectPosition dp2) {
        if (!Double.isNaN(dp1.getZ()) && !Double.isNaN(dp2.getZ())) {
            return Math.sqrt(Math.pow(dp1.getX() - dp2.getX(), 2) + Math.pow(dp1.getY() - dp2.getY(), 2) + Math.pow(dp1.getZ() - dp2.getZ(), 2));
        }
        return Math.sqrt(Math.pow(dp1.getX() - dp2.getX(), 2) + Math.pow(dp1.getY() - dp2.getY(), 2));
    }

    /**
   * Distance euclidienne calculée en 2 dimensions XY, même sur des objets 3D.
   * @deprecated La méthode .distance de DirectPosition fait la même chose
   * @return the 2d distance between 2 points
   * */
    @Deprecated
    public static double distance2D(IDirectPosition dp1, IDirectPosition dp2) {
        return Math.sqrt(Math.pow(dp1.getX() - dp2.getX(), 2) + Math.pow(dp1.getY() - dp2.getY(), 2));
    }

    /**
   * Est-ce que les deux points sont distants de moins du seuil passé en
   * paramètre ? Méthode optimisée pour accélérer les requêtes spatiales.
   */
    public static boolean proche(IDirectPosition dp1, IDirectPosition dp2, double distance) {
        if (Math.abs(dp1.getX() - dp2.getX()) > distance) {
            return false;
        }
        if (Math.abs(dp1.getY() - dp2.getY()) > distance) {
            return false;
        }
        if (Distances.distance(dp1, dp2) > distance) {
            return false;
        }
        return true;
    }

    /** Distance euclidienne du point M au segment [A,B] */
    public static double distancePointSegment(IDirectPosition M, IDirectPosition A, IDirectPosition B) {
        return Distances.distance(M, Operateurs.projection(M, A, B));
    }

    /**
   * Distance euclidienne d'un point P à une ligne.
   * @param point point
   * @param line ligne
   * @return Distance euclidienne d'un point P à une ligne
   */
    public static double distance(IDirectPosition point, ILineString line) {
        return Distances.distance(point, line.coord());
    }

    /**
   * Distance euclidienne d'un point P à un anneau.
   * @param point point
   * @param ring un anneau
   * @return distance euclidienne
   */
    public static double distance(IDirectPosition point, IRing ring) {
        return Distances.distance(point, ring.coord());
    }

    /**
   * @param point
   * @param surface
   * @return the distance between a point and a surface
   */
    public static double distance(IDirectPosition point, IOrientableSurface surface) {
        return Distances.distance(point, surface.coord());
    }

    /**
   * Distance euclidienne d'un point P à une liste de points.
   * @param point point
   * @param pointList une liste de points
   * @return distance euclidienne
   */
    public static double distance(IDirectPosition point, IDirectPositionList pointList) {
        double distmin = Distances.distance(pointList.get(0), point);
        for (int i = 0; i < pointList.size() - 1; i++) {
            double dist = Distances.distancePointSegment(point, pointList.get(i), pointList.get(i + 1));
            if (dist < distmin) {
                distmin = dist;
            }
        }
        return distmin;
    }

    /**
   * Approximation de la première composante de Hausdorff d'une ligne vers une
   * autre. Elle est calculee comme le maximum des distances des points
   * intermédiaires de la première ligne L1 à l'autre ligne L2.
   */
    public static double premiereComposanteHausdorff(ILineString l1, ILineString l2) {
        IDirectPositionList listePoints = l1.coord();
        double dist, distmax = 0;
        for (int i = 0; i < listePoints.size(); i++) {
            dist = Distances.distance(listePoints.get(i), l2);
            if (dist > distmax) {
                distmax = dist;
            }
        }
        return distmax;
    }

    /**
   * Approximation (très proche) de la distance de Hausdorff entre deux lignes.
   * Elle est calculee comme le maximum des distances d'un point intermediaire
   * d'une des lignes a l'autre ligne. Dans certains cas cette definition
   * diffère de la définition theorique pure car la distance de Hausdorff ne se
   * realise pas necessairement sur un point intermediaire. Mais cela est rare
   * sur des données réelles. Cette implementation est un bon compromis entre
   * simplicité et précision.
   */
    public static double hausdorff(ILineString L1, ILineString L2) {
        return Math.max(Distances.premiereComposanteHausdorff(L1, L2), Distances.premiereComposanteHausdorff(L2, L1));
    }

    /**
   * Distance de Hausdorff entre un point P et une ligne L. C'est-à-dire
   * distance au point P du point intermédiaire de la ligne L le plus éloigné du
   * point P.
   */
    public static double hausdorff(ILineString l, IPoint p) {
        Iterator<IDirectPosition> itPts = l.coord().getList().iterator();
        IDirectPosition point;
        double distmax = 0, dist;
        while (itPts.hasNext()) {
            point = itPts.next();
            dist = Distances.distance(point, p.getPosition());
            if (dist > distmax) {
                distmax = dist;
            }
        }
        return distmax;
    }

    /**
   * Distance moyenne entre deux polylignes, définie comme le rapport de l'aire
   * séparant deux polylignes sur la moyenne de leurs longueurs.
   * 
   * IMPORTANT: la méthode suppose que les lignes sont orientées globalement
   * dans le même sens.
   */
    public static double distanceMoyenne(ILineString L1, ILineString L2) {
        Iterator<IDirectPosition> itPts;
        GM_LineString perimetre = new GM_LineString();
        itPts = L1.coord().getList().iterator();
        while (itPts.hasNext()) {
            IDirectPosition pt = itPts.next();
            perimetre.addControlPoint(0, pt);
        }
        itPts = L2.coord().getList().iterator();
        while (itPts.hasNext()) {
            IDirectPosition pt = itPts.next();
            perimetre.addControlPoint(0, pt);
        }
        perimetre.addControlPoint(L1.endPoint());
        GM_Polygon poly = new GM_Polygon(perimetre);
        return 2 * poly.area() / (L1.length() + L2.length());
    }

    /**
   * Mesure d'écart entre deux polylignes, défini comme une approximation de la
   * surface séparant les polylignes. Plus précisément, cet écart est égal à la
   * somme, pour chaque point P de L1, de (distance de P à L2) * (moyenne des
   * longueurs des segments autour de P)
   * 
   * NB: Ce n'est pas une distance au sens mathématique du terme, et en
   * particulier cet écart n'est pas symétrique: ecart(L1,L2) != ecart(L2,L1)
   */
    public static double ecartSurface(ILineString L1, ILineString L2) {
        double ecartTotal = 0, distPt, long1, long2;
        IDirectPositionList pts = L1.coord();
        for (int i = 0; i < pts.size(); i++) {
            distPt = Distances.distance(pts.get(i), L2);
            if (i == 0) {
                long1 = 0;
            } else {
                long1 = Distances.distance(pts.get(i), pts.get(i - 1));
            }
            if (i == pts.size() - 1) {
                long2 = 0;
            } else {
                long2 = Distances.distance(pts.get(i), pts.get(i + 1));
            }
            ecartTotal = ecartTotal + distPt * (long1 + long2) / 2;
        }
        return ecartTotal;
    }

    @SuppressWarnings("unchecked")
    private static IMultiSurface<IOrientableSurface> toMultiSurface(IGeometry geom) {
        if (geom instanceof IMultiSurface<?>) {
            return (IMultiSurface<IOrientableSurface>) geom;
        }
        List<IOrientableSurface> list = new ArrayList<IOrientableSurface>();
        list.add((IOrientableSurface) geom);
        IMultiSurface<IOrientableSurface> multiSurface = new GM_MultiSurface<IOrientableSurface>((ICompositeSurface) list);
        return multiSurface;
    }

    /**
   * Distance surfacique entre deux IGeometry.
   */
    public static double distanceSurfacique(IGeometry geom, IGeometry geom2) {
        if (geom instanceof IMultiSurface<?> || geom2 instanceof IMultiSurface<?>) {
            return Distances.distanceSurfacique(Distances.toMultiSurface(geom), Distances.toMultiSurface(geom2));
        }
        return Distances.distanceSurfacique((IPolygon) geom, (IPolygon) geom2);
    }

    /**
   * Distance surfacique entre deux GM_Polygon.
   * <p>
   * Définition : 1 - surface(intersection)/surface(union) Ref [Vauglin 97]
   * <p>
   * NB: renvoie 2 en cas de problème lors du calcul d'intersection avec JTS
   * (bug en particulier si les surfaces sont dégénérées ou trop complexes).
   */
    public static double distanceSurfacique(IPolygon A, IPolygon B) {
        IGeometry inter = A.intersection(B);
        if (inter == null) {
            return 2;
        }
        IGeometry union = A.union(B);
        if (union == null) {
            return 1;
        }
        return 1 - inter.area() / union.area();
    }

    /**
   * Distance surfacique entre deux IMultiSurface.
   * 
   * Définition : 1 - surface(intersection)/surface(union) Ref [Vauglin 97]
   * 
   * NB: renvoie 2 en cas de problème lors du calcul d'intersection avec JTS
   * (bug en particulier si les surfaces sont dégénérées ou trop complexes).
   */
    public static double distanceSurfacique(IMultiSurface<IOrientableSurface> A, IMultiSurface<IOrientableSurface> B) {
        IGeometry inter = A.intersection(B);
        if (inter == null) {
            return 2;
        }
        IGeometry union = A.union(B);
        if (union == null) {
            return 1;
        }
        return 1 - inter.area() / union.area();
    }

    /**
   * Distance surfacique "robuste" entre deux polygones.
   * <p>
   * Il s'agit ici d'une pure bidouille pour contourner certains bugs de JTS: Si
   * JTS plante au calcul d'intersection, on filtre les surfaces avec Douglas et
   * Peucker, progressivement avec 10 seuils entre min et max. Min et Max
   * doivent être fixer donc de l'ordre de grandeur de la précision des données
   * sinon le calcul risque d'être trop faussé.
   * <p>
   * Définition : 1 - surface(intersection)/surface(union) Ref [Vauglin 97]
   * <p>
   * NB: renvoie 2 en cas de problème lors du calcul d'intersection avec JTS
   * (bug en particulier si les surfaces sont dégénérées ou trop complexes).
   * */
    public static double distanceSurfaciqueRobuste(GM_Polygon A, GM_Polygon B, double min, double max) {
        IGeometry inter = Operateurs.intersectionRobuste(A, B, min, max);
        if (inter == null) {
            return 2;
        }
        IGeometry union = A.union(B);
        if (union == null) {
            return 1;
        }
        return 1 - inter.area() / union.area();
    }

    /**
   * Distance surfacique entre deux IMultiSurface.
   * 
   * Cette méthode contourne des bugs de JTS, qui sont trop nombreux sur les
   * agrégats. En contrepartie, cette méthode n'est valable que si les IPolygon
   * composant A [resp. B] ne s'intersectent pas entre elles.
   * 
   * Définition : 1 - surface(intersection)/surface(union) Ref [Vauglin 97]
   * 
   * NB: renvoie 2 en cas de problème résiduer lors du calcul d'intersection
   * avec JTS (bug en particulier si les surfaces sont dégénérées ou trop
   * complexes).
   */
    public static double distanceSurfaciqueRobuste(IMultiSurface<IOrientableSurface> A, IMultiSurface<IOrientableSurface> B) {
        double inter = Distances.surfaceIntersection(A, B);
        if (inter == -1) {
            System.out.println("Plantage JTS, renvoi 2 à la distance surfacique de deux multi_surfaces");
            return 2;
        }
        return 1 - inter / (A.area() + B.area() - inter);
    }

    /**
   * Surface de l'intersection.
   * 
   * Cette méthode contourne des bugs de JTS, qui sont trop nombreux sur les
   * agrégats. En contrepartie, cette méthode n'est valable que si les
   * GM_Polygon composant A [resp. B] ne s'intersectent pas entre elles.
   * 
   * NB: renvoie -1 en cas de problème résiduer lors du calcul d'intersection
   * avec JTS (bug en particulier si les surfaces sont dégénérées ou trop
   * complexes).
   */
    public static double surfaceIntersection(IMultiSurface<IOrientableSurface> A, IMultiSurface<IOrientableSurface> B) {
        Iterator<IOrientableSurface> itA = A.getList().iterator();
        Iterator<IOrientableSurface> itB;
        double inter = 0;
        while (itA.hasNext()) {
            ISurface surfA = (ISurface) itA.next();
            itB = B.getList().iterator();
            while (itB.hasNext()) {
                ISurface surfB = (ISurface) itB.next();
                if (surfB.intersection(surfA) == null) {
                    System.out.println("Plantage JTS, renvoi -1 à l'intersection de deux multi_surfaces");
                    return -1;
                }
                inter = inter + surfB.intersection(surfA).area();
            }
        }
        return inter;
    }

    /**
   * Surface de l'union.
   * 
   * Cette méthode contourne des bugs de JTS, qui sont trop nombreux sur les
   * agrégats. En contrepartie, cette méthode n'est valable que si les
   * GM_Polygon composant A [resp. B] ne s'intersectent pas entre elles.
   * 
   * NB: renvoie -1 en cas de problème résiduer lors du calcul d'intersection
   * avec JTS (bug en particulier si les surfaces sont dégénérées ou trop
   * complexes).
   */
    public static double surfaceUnion(IMultiSurface<IOrientableSurface> A, IMultiSurface<IOrientableSurface> B) {
        double inter = Distances.surfaceIntersection(A, B);
        if (inter == -1) {
            System.out.println("Plantage JTS, renvoi -1 à l'union de deux 2 multi_surfaces");
            return -1;
        }
        return A.area() + B.area() - inter;
    }

    /**
   * Mesure dite "Exactitude" entre 2 surfaces. Ref : [Bel Hadj Ali 2001]
   * <p>
   * Définition : Surface(A inter B) / Surface(A)
   */
    public static double exactitude(IPolygon A, IPolygon B) {
        IGeometry inter = A.intersection(B);
        if (inter == null) {
            return 0;
        }
        return inter.area() / A.area();
    }

    /**
   * Mesure dite "Complétude" entre 2 surfaces. Ref : [Bel Hadj Ali 2001]
   * <p>
   * Définition : Surface(A inter B) / Surface(B)
   */
    public static double completude(IPolygon A, IPolygon B) {
        return Distances.exactitude(B, A);
    }

    /**
   * Mesure dite "Exactitude" entre 2 IMultiSurface. Ref : [Bel Hadj Ali 2001].
   * <p>
   * Définition : Surface(A inter B) / Surface(A)
   */
    public static double exactitude(IMultiSurface<IOrientableSurface> A, IMultiSurface<IOrientableSurface> B) {
        IGeometry inter = A.intersection(B);
        if (inter == null) {
            return 0;
        }
        return inter.area() / A.area();
    }

    /**
   * Mesure dite "Complétude" entre 2 IMultiSurface.
   * <p>
   * Ref : [Bel Hadj Ali 2001] Définition : Surface(A inter B) / Surface(B)
   */
    public static double completude(IMultiSurface<IOrientableSurface> A, IMultiSurface<IOrientableSurface> B) {
        return Distances.exactitude(B, A);
    }

    /**
   * Mesure d'association entre deux surfaces (cf. [Bel Hadj Ali 2001]). <BR>
   * <STRONG> Definition : </STRONG> associationSurfaces(A,B) = vrai si
   * <UL>
   * <LI>Surface(intersection) > min (min etant la resolution minimum des deux
   * bases)</LI>
   * <LI>ET (Surface(intersection) > surface(A) * coeff</LI>
   * <LI>OU Surface(intersection) > surface(B) * coeff )</LI>
   * </UL>
   * <BR>
   * associationSurfaces(A,B) = faux sinon.
   * 
   */
    public static boolean associationSurfaces(IGeometry A, IGeometry B, double min, double coeff) {
        IGeometry inter = A.intersection(B);
        if (inter == null) {
            return false;
        }
        double interArea = inter.area();
        if (interArea < min) {
            return false;
        }
        if (interArea > A.area() * coeff) {
            return true;
        }
        if (interArea > B.area() * coeff) {
            return true;
        }
        return false;
    }

    /**
   * Test d'association "robuste" entre deux surfaces (cf. [Bel Hadj Ali 2001]).
   * 
   * Il s'agit ici d'une pure bidouille pour contourner certains bugs de JTS: Si
   * JTS plante au calcul , on filtre les surfaces avec Douglas et Peucker,
   * progressivement avec 10 seuils entre min et max. Min et Max doivent être
   * fixer donc de l'ordre de grandeur de la précision des données sinon le
   * calcul risque d'être trop faussé.
   * 
   * <BR>
   * <STRONG> Definition : </STRONG> associationSurfaces(A,B) = vrai si
   * <UL>
   * <LI>Surface(intersection) > min (min etant la resolution minimum des deux
   * bases)</LI>
   * <LI>ET (Surface(intersection) > surface(A) * coeff</LI>
   * <LI>OU Surface(intersection) > surface(B) * coeff )</LI>
   * </UL>
   * <BR>
   * associationSurfaces(A,B) = faux sinon.
   * 
   */
    public static boolean associationSurfacesRobuste(IGeometry A, IGeometry B, double min, double coeff, double minDouglas, double maxDouglas) {
        IGeometry inter = Operateurs.intersectionRobuste(A, B, minDouglas, maxDouglas);
        if (inter == null) {
            return false;
        }
        double interArea = inter.area();
        if (interArea < min) {
            return false;
        }
        if (interArea > A.area() * coeff) {
            return true;
        }
        if (interArea > B.area() * coeff) {
            return true;
        }
        return false;
    }
}
