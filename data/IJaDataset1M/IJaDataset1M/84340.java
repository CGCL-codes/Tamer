package islandev.testing;

import islandev.*;
import java.awt.Point;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author  mmg20
 */
public class MigrationPatterns {

    protected ClientGrid grid = new ClientGrid();

    /** Creates a new instance of MigrationPatterns */
    public MigrationPatterns() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MigrationPatterns mp = new MigrationPatterns();
        mp.createSingleCandidateGrid();
        mp.single();
    }

    protected void single() {
        DistantRouletteMigrationPattern drmp = new DistantRouletteMigrationPattern(3);
        Object pin = drmp.getImmigrantSource(grid, grid.getClient(p(0, 0)));
        System.out.println("" + pin);
    }

    protected void stats(Point centre, int maxDistance, int tests) {
        ArrayList[] migrationCandidates = DistantRouletteMigrationPattern.getAllMigrationCandidates(grid, grid.getClient(centre), maxDistance);
        int[] count = new int[maxDistance];
        DistantRouletteMigrationPattern drmp = new DistantRouletteMigrationPattern(maxDistance);
        for (int i = 0; i < tests; i++) {
            Object pin = drmp.getImmigrantSource(grid, grid.getClient(centre));
            count[belongsTo(pin, migrationCandidates)]++;
        }
        printAll(count);
    }

    protected int belongsTo(Object pin, ArrayList[] hay) {
        for (int hl = 0; hl < hay.length; hl++) {
            if (hay[hl].contains(pin)) {
                return hl;
            }
        }
        return -1;
    }

    protected void roul(Point centre, int maxDistance) {
        ArrayList[] migrationCandidates = DistantRouletteMigrationPattern.getAllMigrationCandidates(grid, grid.getClient(centre), maxDistance);
        int[] roulette = DistantRouletteMigrationPattern.generateRouletteWheel(migrationCandidates);
        ArrayList allCandidates = DistantRouletteMigrationPattern.flatten(migrationCandidates);
        printAll(roulette);
        System.out.println("" + allCandidates);
    }

    protected void filterNeiRec(Point centre, int distance) {
        ArrayList cr = DistantRouletteMigrationPattern.getMigrationCandidates(grid, centre, distance);
        System.out.println("" + cr);
    }

    protected void neiRec(Point centre, int distance) {
        ClientRecord[] cr = DistantRouletteMigrationPattern.getRecords(grid, DistantRouletteMigrationPattern.generateNeighbourPos(centre, distance));
        printAll(cr);
    }

    protected void neiPos(Point centre, int distance) {
        Point[] pos = DistantRouletteMigrationPattern.generateNeighbourPos(centre, distance);
        System.out.println("Relative Neighbour Positions from " + centre + " at distance " + distance);
        printAll(pos);
    }

    protected void relNeiPos(int distance) {
        Point[] pos = DistantRouletteMigrationPattern.generateRelativeNeighbourPos(distance);
        System.out.println("Relative Neighbour Positions from 0,0 at distance " + distance);
        printAll(pos);
    }

    protected void createSingleCandidateGrid() {
        grid.addClient("00");
        grid.addClient("01");
        grid.getClient(p(0, 0)).setEmigrants(new Vector());
    }

    protected void createUnevenClientGrid() {
        for (int rl = 0; rl < 46; rl++) {
            grid.addClient("" + rl);
        }
        Vector dummyEmigrants = new Vector();
        Point[] coordsToFill = { p(-1, 0), p(1, 0), p(2, -1), p(-1, 2), p(2, 3), p(-3, 0), p(-3, -1), p(-2, 2), p(-2, -2), p(-1, 1), p(-1, -1), p(0, 0), p(0, -3), p(1, 1), p(1, -2), p(1, -3), p(2, 0), p(2, -2) };
        for (int pl = 0; pl < coordsToFill.length; pl++) {
            System.out.println("Filling " + coordsToFill[pl]);
            grid.getClient(coordsToFill[pl]).setEmigrants(dummyEmigrants);
        }
    }

    protected void createEvenClientGrid() {
        for (int rl = 0; rl < 46; rl++) {
            grid.addClient("" + rl);
        }
        Vector dummyEmigrants = new Vector();
        Point[] coordsToFill = { p(-2, -1), p(-1, 2), p(-1, 1), p(-1, 0), p(0, 1), p(0, -2), p(0, -3), p(1, 2), p(1, -1), p(2, 1), p(2, 0), p(3, 0) };
        for (int pl = 0; pl < coordsToFill.length; pl++) {
            System.out.println("Filling " + coordsToFill[pl]);
            grid.getClient(coordsToFill[pl]).setEmigrants(dummyEmigrants);
        }
    }

    protected static Point p(int x, int y) {
        return new Point(x, y);
    }

    protected void printAll(Object[] os) {
        for (int ol = 0; ol < os.length; ol++) {
            System.out.println("" + os[ol]);
        }
    }

    protected void printAll(int[] os) {
        for (int ol = 0; ol < os.length; ol++) {
            System.out.println("" + os[ol]);
        }
    }
}
