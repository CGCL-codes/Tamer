package orimage.ori;

import java.util.Enumeration;
import java.util.HashMap;

/**
*
* @author   V. Osele
* @version  1.0
*/
public class Modele extends Object {

    private Couches _faces = new Couches();

    public Modele() {
    }

    public Face initialiser(Point[] pts) {
        _faces.initialiser();
        Face face = new Face(pts);
        _faces.ajouter(face);
        return face;
    }

    /** d�couper la ligne l des faces associ�es par p */
    public Lien[] decouper(Lien l, Point p) {
        Face[] fs = l.faces();
        Face f = fs[0];
        if (f == null) f = fs[1];
        if (f == null) return null;
        Lien[] ls = f.decouper(l, p);
        f.remplacer(l, ls);
        f = l.autre(f);
        if (f == null) return ls;
        f.remplacer(l, ls);
        return ls;
    }

    public Lien[] decouper(Face f, Lien l, Point p) {
        Lien[] ls = f.decouper(l, p);
        f.remplacer(l, ls);
        l.remplacer(f, null);
        f = l.autre(f);
        if (f == null) return ls;
        for (int i = 0; i < ls.length; ++i) {
            ls[i].remplacer(f, null);
        }
        return ls;
    }

    public void remplacer(Face f, Lien[] ls, Lien l) {
        f.remplacer(ls, l);
    }

    /**
	 * D�coupe la la face f par deux points lui appartenant
	 * en deux faces.
	 */
    public Face[] decouper(Face f, Point p1, Point p2) {
        Face[] fs = f.decouper(p1, p2);
        if (fs == null) return fs;
        _faces.remplacer(f, fs);
        for (int i = 0; i < fs.length; ++i) {
            Enumeration e = fs[i].lignes();
            while (e.hasMoreElements()) {
                Lien l = (Lien) e.nextElement();
                l.remplacer(f, fs[i]);
            }
        }
        return fs;
    }

    public void remplacer(Face[] fs, Face f) {
        _faces.remplacer(fs, f);
        Enumeration e = f.lignes();
        while (e.hasMoreElements()) {
            Lien l = (Lien) e.nextElement();
            if (l.estBord()) continue;
            int i;
            for (i = 0; i < fs.length; ++i) {
                if (l.appartient(fs[i])) break;
            }
            if (i >= fs.length) continue;
            l.remplacer(fs[i], f);
        }
    }

    public Face chercher(Point p) {
        return chercher(p.x(), p.y());
    }

    public Face chercher(int x, int y) {
        Enumeration e = _faces.faces(true);
        while (e.hasMoreElements()) {
            Face f = (Face) e.nextElement();
            if (f.contient(x, y)) return f;
        }
        return null;
    }

    public Lien chercherLien(Point a, Point b) {
        Enumeration e = _faces.faces(true);
        while (e.hasMoreElements()) {
            Face f = (Face) e.nextElement();
            Lien l = f.chercherLien(a, b);
            if (l != null) return l;
        }
        return null;
    }

    /**
	 * Cherche par ordre decroissant des couches du plus haut vers le plus bas
	 * une face qui contient les deux points.
	 * Les points ne sont pas necessairement cote � cote.
	 */
    public Face chercherFace(Point a, Point b) {
        Point[] pts = { a, b };
        return chercherFace(pts);
    }

    /**
	 * Cherche par ordre decroissant des couches du plus haut vers le plus bas
	 * une face qui contient les points.
	 * Les points ne sont pas necessairement cote � cote.
	 */
    public Face chercherFace(Point[] pts) {
        Enumeration e = _faces.faces(true);
        while (e.hasMoreElements()) {
            Face f = (Face) e.nextElement();
            boolean tous = true;
            for (int i = 0; i < pts.length; ++i) {
                if (!f.appartient(pts[i])) {
                    tous = false;
                    break;
                }
            }
            if (tous) return f;
        }
        return null;
    }

    public void deplacer(Face f, Point p, Point en) {
        p.deplacer(en.x(), en.y());
    }

    public void deplacer(Face f, Point p, int x, int y) {
        p.deplacer(x, y);
    }

    public boolean deplacerSur(Face face, int ref) {
        return _faces.deplacerSur(face, ref);
    }

    public boolean deplacerMeme(Face face, Face ref) {
        return _faces.deplacerMeme(face, ref);
    }

    public boolean deplacer(Face face, int ref) {
        return _faces.deplacer(face, ref);
    }

    public Enumeration faces() {
        return _faces.faces();
    }

    public Enumeration faces(boolean inverse) {
        return _faces.faces(inverse);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append(super.toString()).append(" : ");
        int size = 0;
        Enumeration e = _faces.faces();
        while (e.hasMoreElements()) {
            e.nextElement();
            ++size;
        }
        result.append(size).append(" [");
        e = _faces.faces();
        while (e.hasMoreElements()) {
            Face f = (Face) e.nextElement();
            result.append(f).append(" ");
        }
        result.append("]");
        return result.toString();
    }

    HashMap _noms = new HashMap();

    public void nommer(String nom, Object o) {
        _noms.put(nom, o);
    }

    public Object getObjet(String nom) {
        return _noms.get(nom);
    }
}
