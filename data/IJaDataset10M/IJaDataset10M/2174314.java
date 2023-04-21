package org.opensourcephysics.stp.demon.demonlattice;

import org.opensourcephysics.display.*;
import org.opensourcephysics.numerics.*;
import java.awt.*;

public class DemonLattice implements Drawable {

    public int arraySize;

    public int s[][];

    public int realSpace[];

    public int L, Lp, pmax;

    public int steps = 0;

    public int e = 0;

    public int n = 0;

    public int E;

    public int N;

    public int p[][];

    public int ke[];

    public int accept, mcs;

    public double nsum, esum, nave, eave, Eave, Esum, Nave, Nsum;

    public int hc = 0, well = 0;

    public void initialize() {
        arraySize = N;
        Lp = 2 * pmax + 1;
        ke = new int[Lp];
        s = new int[L][Lp];
        realSpace = new int[L];
        for (int j = 0; j < Lp; j++) ke[j] = (j - pmax) * (j - pmax);
        reset();
        int availableE = E;
        E = 0;
        for (int k = 0; k < N; k++) {
            int i = (int) (Math.random() * L);
            int j = (int) (Math.random() * Lp);
            while ((ke[j] + pe(i, 1) > availableE) || (s[i][j] == 1)) {
                i = (int) (Math.random() * L);
                j = (int) (Math.random() * Lp);
            }
            int particleEnergy = ke[j] + pe(i, 0);
            availableE -= particleEnergy;
            E += particleEnergy;
            s[i][j] = 1;
            realSpace[i]++;
        }
    }

    public int pe(int i, int add) {
        int potentialEnergy = 0;
        if (add == 1) potentialEnergy = realSpace[i] * hc * 100000;
        int iLeft = PBC.position(i - 1, L);
        int iRight = PBC.position(i + 1, L);
        potentialEnergy -= (realSpace[iLeft] + realSpace[iRight]) * well;
        return potentialEnergy;
    }

    public void reset() {
        e = 0;
        n = 0;
        mcs = 0;
        steps = 0;
        nsum = 0;
        esum = 0;
        Esum = 0;
        Nsum = 0;
        accept = 0;
        p = new int[arraySize][arraySize];
    }

    public void averages() {
        Eave = Esum * 1.0 / steps;
        Nave = Nsum * 1.0 / steps;
        eave = esum / steps;
        nave = nsum / steps;
    }

    public void demonStep() {
        for (int k = 0; k < L * Lp; k++) {
            int i = (int) (Math.random() * L);
            int j = (int) (Math.random() * Lp);
            if (s[i][j] == 0) {
                int peAdd = pe(i, 1);
                if (n > 0 && (ke[j] + peAdd) <= e) {
                    s[i][j] = 1;
                    realSpace[i]++;
                    e -= ke[j] + peAdd;
                    n--;
                    E += ke[j] + peAdd;
                    N++;
                    accept++;
                }
            } else {
                int peSubstract = pe(i, 0);
                if (-peSubstract - ke[j] <= e) {
                    s[i][j] = 0;
                    realSpace[i]--;
                    e += ke[j] + peSubstract;
                    n++;
                    E -= ke[j] + peSubstract;
                    N--;
                    accept++;
                }
            }
            if (e < arraySize && n < arraySize && e >= 0) p[e][n]++;
            steps++;
            nsum += n;
            esum += e;
            Esum += E;
            Nsum += N;
        }
        mcs++;
    }

    public void draw(DrawingPanel myWorld, Graphics g) {
        if (s == null) return;
        int pxRadius = Math.abs(myWorld.xToPix(0.5) - myWorld.xToPix(0));
        int pyRadius = Math.abs(myWorld.yToPix(0.5) - myWorld.yToPix(0));
        g.setColor(Color.red);
        for (int i = 0; i < L; i++) for (int j = 0; j < Lp; j++) if (s[i][j] != 0) {
            int xpix = myWorld.xToPix(i * 1.0) - pxRadius;
            int ypix = myWorld.yToPix(j - pmax) - pyRadius;
            g.fillRect(xpix, ypix, 2 * pxRadius, 2 * pyRadius);
        }
    }
}
