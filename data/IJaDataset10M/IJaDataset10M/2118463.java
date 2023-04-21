package PathFind;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Th3rulXT
 * 
 *         Classe permettant de trouver le chemin le plus court (A*)
 */
public class AoT_PathFinder {

    private ArrayList<AoT_Case> ferme = new ArrayList<AoT_Case>();

    private ArrayList<AoT_Case> ouverte = new ArrayList<AoT_Case>();

    ArrayList<AoT_Case> path = new ArrayList<AoT_Case>();

    private boolean go = false;

    private static int[][] pos;

    private AoT_Case debut;

    private AoT_Case fin;

    public AoT_PathFinder(AoT_Case d, AoT_Case f, int[][] p) {
        if (p[d.getX()][d.getY()] == 0 && p[f.getX()][f.getY()] == 0) {
            setPos(p);
            this.debut = d;
            this.fin = f;
            ajoutAdjacentAOuverte(debut);
        } else System.out.println("Vous étes sur un mur !!!");
    }

    public int[][] getPos() {
        return pos;
    }

    public void setPos(int[][] p) {
        this.pos = p;
    }

    public ArrayList<AoT_Case> getPath() {
        while (!go) {
            getPath();
        }
        return path;
    }

    public int getPathSize() {
        while (!go) {
            getPath();
        }
        return path.size();
    }

    public void setPath(ArrayList<AoT_Case> path) {
        this.path = path;
    }

    void getParentPath() {
        AoT_Case curr = this.fin;
        while (!curr.equals(debut)) {
            path.add(curr);
            curr = curr.getParent();
        }
        path.add(debut);
        this.go = true;
    }

    AoT_Case getMinF() {
        AoT_Case min = null;
        min = ouverte.get(0);
        Iterator<AoT_Case> fIt = ouverte.iterator();
        while (fIt.hasNext()) {
            min = compareF(min, fIt.next());
        }
        return min;
    }

    public void ajoutAdjacentAOuverte(AoT_Case debut) {
        int xc, yc;
        AoT_Case courante = debut;
        AoT_Case memoire = null;
        while (!isInList(fin, ferme)) {
            if (!isInList(courante, ferme)) {
                ferme.add(courante);
                xc = courante.getX();
                yc = courante.getY();
                if ((yc >= 1) && (pos[xc][yc - 1] == 0)) ajoutOuverte(courante, (new AoT_Case(xc, yc - 1)));
                if ((yc < pos.length - 1) && (pos[xc][yc + 1] == 0)) ajoutOuverte(courante, (new AoT_Case(xc, yc + 1)));
                if ((xc < pos.length - 1) && (pos[xc + 1][yc] == 0)) ajoutOuverte(courante, (new AoT_Case(xc + 1, yc)));
                if ((xc >= 1) && (pos[xc - 1][yc] == 0)) ajoutOuverte(courante, (new AoT_Case(xc - 1, yc)));
                memoire = courante;
            }
            ouverte.remove(courante);
            if (ouverte.isEmpty()) {
                if (Math.abs(memoire.getX() - fin.getX()) >= 1 && Math.abs(memoire.getY() - fin.getY()) >= 1) {
                    System.out.println("Il n'y a pas de chemin entre ces deux point !!!");
                    break;
                } else break;
            }
            courante = getMinF();
        }
        fin.setParent(memoire);
        getParentPath();
    }

    public void afficheList(ArrayList<AoT_Case> list) {
        Iterator<AoT_Case> ito = list.iterator();
        AoT_Case test;
        while (ito.hasNext()) {
            test = ito.next();
            System.out.println(test.toString() + "-->:" + " X= " + test.getX() + " Y= " + test.getY() + " F= " + test.getF());
        }
    }

    public boolean isInList(AoT_Case courante, ArrayList<AoT_Case> list) {
        Iterator ito = list.iterator();
        while (ito.hasNext()) {
            if (ito.next().equals(courante)) return true;
        }
        return false;
    }

    public void ajoutOuverte(AoT_Case courante, AoT_Case adjacente) {
        int g = courante.getG() + ((adjacente.getX() == courante.getX() || adjacente.getY() == courante.getY()) ? 10 : 15);
        int h = (Math.abs(adjacente.getX() - fin.getX()) + Math.abs(adjacente.getY() - fin.getY()));
        int f = g + h;
        if (isInList(adjacente, ouverte)) {
            if (adjacente.getF() > f) {
                adjacente.setG(g);
                adjacente.setF(f);
                adjacente.setParent(courante);
            }
        } else if (!isInList(adjacente, ferme)) {
            adjacente.setG(g);
            adjacente.setH(h);
            adjacente.setF(f);
            adjacente.setParent(courante);
            ouverte.add(adjacente);
        }
    }

    private AoT_Case compareF(AoT_Case cF1, AoT_Case cF2) {
        if (cF1.getF() < cF2.getF()) return cF1;
        return cF2;
    }

    public void dessine() {
        for (int pl = 0; pl < pos.length; pl++) {
            for (int c = 0; c < pos[pl].length; c++) {
                System.out.print(pos[pl][c] + " ");
            }
            System.out.println();
        }
    }

    public void dessineResult() {
        Iterator<AoT_Case> itFerme = path.iterator();
        AoT_Case fil = null;
        while (itFerme.hasNext()) {
            fil = itFerme.next();
            pos[fil.getX()][fil.getY()] = 8;
        }
        for (int pl = 0; pl < pos[0].length; pl++) {
            for (int c = 0; c < pos.length; c++) {
                System.out.print(pos[c][pl] + " ");
            }
            System.out.println();
        }
    }
}
