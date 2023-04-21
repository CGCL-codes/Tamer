package kursach2.strategy;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import kursach2.barriers.AArchiBot;
import kursach2.barriers.IBarier;
import kursach2.IHuman;
import kursach2.TPremises;

/**
 * @author Vsevolod
 *
 */
public class HerdStrategy extends AStrategy {

    private String Name = "HerdStrategy";

    private java.util.Random random = new java.util.Random((new java.util.Date()).getTime());

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public double[] getProbably(AArchiBot LostHum) {
        int count[] = new int[4], i;
        List<AArchiBot> Hums = LostHum.getMyPrem().getHumans();
        for (IBarier mb : Hums) {
            if (mb instanceof IHuman && ((IHuman) mb).getNumberTarget() != -1) {
                count[((IHuman) mb).getNumberTarget()]++;
            }
        }
        int max = Math.max(count[0], count[1]);
        max = Math.max(max, count[2]);
        max = Math.max(max, count[3]);
        double[] r = new double[] { 0.0, 0.0, 0.0, 0.0 };
        if (max == 0 || random.nextDouble() < 0.0) {
            i = random.nextInt(4);
            while (LostHum.getMyPrem().getPassage(i) == null) i = (i + 1) % 4;
            r[i] = 1.0;
        } else {
            i = 0;
            while (count[i] != max) i++;
            r[i] = 1.0;
        }
        return r;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public void changePremises(AArchiBot ag, TPremises later, TPremises now) {
    }

    @Override
    public void init() {
        random = new java.util.Random((new java.util.Date()).getTime());
    }
}
