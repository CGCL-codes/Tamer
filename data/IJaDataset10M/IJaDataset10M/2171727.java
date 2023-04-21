package models.maillage;

import java.util.ArrayList;

public class GraphePondere {

    private ArrayList<Noeud> noeuds;

    private ArrayList<Arc> arcs;

    public GraphePondere() {
        noeuds = new ArrayList<Noeud>();
        arcs = new ArrayList<Arc>();
    }

    public void ajouterNoeud(Noeud n) {
        noeuds.add(n);
    }

    public void ajouterArc(Arc a) {
        arcs.add(a);
    }

    public int getNbNoeuds() {
        return noeuds.size();
    }

    public int getNbArcs() {
        return arcs.size();
    }

    public Noeud[] getNoeuds() {
        Noeud noeudArray[] = new Noeud[noeuds.size()];
        return noeuds.toArray(noeudArray);
    }

    public Arc[] getArcs() {
        Arc arcsArray[] = new Arc[arcs.size()];
        return arcs.toArray(arcsArray);
    }

    public Noeud[] getVoisins(Noeud n) {
        ArrayList<Noeud> voisins = new ArrayList<Noeud>();
        for (Arc a : arcs) if (a.getDepart() == n) voisins.add(a.getArrivee());
        Noeud noeudArray[] = new Noeud[voisins.size()];
        return voisins.toArray(noeudArray);
    }

    public void print() {
        for (Arc a : arcs) System.out.println(a);
    }

    public Noeud getNoeud(int i) {
        return noeuds.get(i);
    }
}
