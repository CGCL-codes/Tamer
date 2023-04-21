package esdomaci.ai;

import java.awt.Point;
import java.util.ArrayList;
import esdomaci.mediator.IMapaVidljivoAI;

/**
 * Algoritam pretrage za putem je 
 * baziran na kombinaciji FLOYD i PATH algoritama 
 * iz knjige "Strukture podataka" od Mila Tomasevica
 * 
 * Matrica W je opisana ovako:
 * 1) w[i,j]=0 ako je i=j
 * 2) w[i,j]=w(i,j) ako (i,j) validna veza u grafu   (ovde je uvek w(i,j)==1)
 * 3) w[i,j]=infinity ako (i,j) nije validna veza u grafu   (ovde je uvek 0xFFFF)
 * 
 * Matrica T je opisana ovako:
 * 1) t[i,j]=0 ako je (i == j) || (w[i,j] == infinity)
 * 2) t[i,j]=i ako je (i != j) && (w[i,j] != infinity)
 * 
 * FLOYD(W)  ->>> ovde ce biti W = 1
 * D = W
 * for k=1 to n do
 *   for i = 1 to n do
 *     for j = 1 to n do
 *       if (d[i,j] > d[i,k] +d[k,j]) then
 *          t[i,j] = t[k,j];
 *          d[i,j] = d[i,k] + d[k,j];
 *       end_if
 *     end_for
 *   end_for
 * end_for
 * 
 * PATH(i,j)
 * if (i=j) then
 *   print(i)
 *   return
 * else
 *   if (t[i,j]=0) then
 *     print(nema puta izmedju i i j!!!)
 *   else
 *     PATH(i, t[i,j])
 *     print(j)
 *   end_if
 * end_if
 * 
 * @author Milan Aleksic, milanaleksic@gmail.com
 */
public class NalazenjePuta {

    private int[][] FloydMatricaW = new int[17][17];

    private int[][] FloydMatricaT = new int[17][17];

    private IMapaVidljivoAI originalnaMatrica;

    private boolean dozvoljeneRizicnePutanje;

    private static final Point[] konverzionaTabelaIntToPoint = { null, new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1), new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(0, 3), new Point(1, 3), new Point(2, 3), new Point(3, 3) };

    private static final int[][] konverzionaTabelaPointToInt = { { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 }, { 4, 8, 12, 16 } };

    public NalazenjePuta(IMapaVidljivoAI matricaIgre) {
        this(matricaIgre, false);
    }

    public NalazenjePuta(IMapaVidljivoAI matricaIgre, boolean dozvoljeneRizicnePutanje) {
        originalnaMatrica = matricaIgre;
        this.dozvoljeneRizicnePutanje = dozvoljeneRizicnePutanje;
        for (int i = 1; i <= 16; i++) for (int j = 1; j <= 16; j++) FloydMatricaW[i][j] = proracunajWPolje(i, j);
        for (int i = 1; i <= 16; i++) for (int j = 1; j <= 16; j++) FloydMatricaT[i][j] = proracunajTPolje(i, j);
    }

    /**
     * 01 02 03 04
     * 05 06 07 08
     * 09 10 11 12
     * 13 14 15 16
     * 
     * Matrica W je opisana ovako:
     * 1) w[i,j]=0 ako je i=j
     * 2) w[i,j]=w(i,j) ako (i,j) validna veza u grafu   (ovde je uvek w(i,j)==1)
     * 3) w[i,j]=infinity ako (i,j) nije validna veza u grafu   (ovde je uvek 0xFFFF)
     */
    private int proracunajWPolje(int x, int y) {
        if (Math.abs(x - y) == 1) {
            int veci = (x > y ? x : y);
            if (veci % 4 == 1) return 0xFFFF; else return obradiSpojenost(x, y);
        } else if (Math.abs(x - y) == 4) return obradiSpojenost(x, y); else if (x == y) return 0; else return 0xFFFF;
    }

    /**
     * Ocekuje prijem indeksa u notaciji od 1 a ne od nule
     */
    private int obradiSpojenost(int x, int y) {
        Point a = konverzionaTabelaIntToPoint[x];
        Point b = konverzionaTabelaIntToPoint[y];
        if (dozvoljeneRizicnePutanje) if (originalnaMatrica.vrati(a).isBezbedno() && originalnaMatrica.vrati(b).isBezbedno()) return 1; else return 14;
        if (originalnaMatrica.vrati(a).isBezbedno() && originalnaMatrica.vrati(b).isBezbedno()) return 1; else return 0xFFFF;
    }

    /**
     * Matrica T je opisana ovako:
     * 1) t[i,j]=0 ako je (i == j) || (w[i,j] == infinity)
     * 2) t[i,j]=i ako je (i != j) && (w[i,j] != infinity)
     */
    private int proracunajTPolje(int x, int y) {
        if ((x == y) || (FloydMatricaW[x][y] == 0xFFFF)) return 0; else return x;
    }

    public void Floyd() {
        for (int k = 1; k <= 16; k++) for (int i = 1; i <= 16; i++) for (int j = 1; j <= 16; j++) if (FloydMatricaW[i][j] > FloydMatricaW[i][k] + FloydMatricaW[k][j]) {
            FloydMatricaT[i][j] = FloydMatricaT[k][j];
            FloydMatricaW[i][j] = FloydMatricaW[i][k] + FloydMatricaW[k][j];
        }
    }

    public ArrayList<Point> Path(Point source, Point dest) {
        int x = konverzionaTabelaPointToInt[source.x][source.y];
        int y = konverzionaTabelaPointToInt[dest.x][dest.y];
        ArrayList<Point> put = new ArrayList<Point>();
        calculatePath(put, x, y);
        if (put.size() > 0) put.remove(0);
        return put;
    }

    private void calculatePath(ArrayList<Point> output, int x, int y) {
        if (x == y) output.add(konverzionaTabelaIntToPoint[x]); else {
            if (FloydMatricaT[x][y] == 0) {
                if (dozvoljeneRizicnePutanje) System.err.println("Ne postoji bezbedan put izmedju " + x + " i " + y); else System.err.println("Ne postoji put izmedju " + x + " i " + y);
            } else {
                calculatePath(output, x, FloydMatricaT[x][y]);
                output.add(konverzionaTabelaIntToPoint[y]);
            }
        }
    }
}
