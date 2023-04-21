package genericAlgorithm.gp.operators.bean;

import genericAlgorithm.framework.dataTypes.SolutionAVo;
import genericAlgorithm.framework.operators.Crossover;
import genericAlgorithm.gp.utility.SolutionHelper;
import java.util.Random;

/**
 *
 * @author Oz
 */
public class OnePointCrossover implements Crossover {

    private static Random randg = new Random();

    @Override
    public SolutionAVo[] reproduction(SolutionAVo f, SolutionAVo m) {
        return new SolutionAVo[] { reproduceSolution(f, m), reproduceSolution(m, f) };
    }

    private SolutionAVo reproduceSolution(SolutionAVo f, SolutionAVo m) {
        int xpoint = 1 + randg.nextInt(f.getLeftTour().length - 1);
        int[] l, r;
        l = new int[m.getLeftTour().length];
        r = new int[m.getLeftTour().length];
        for (int i = 0; i < xpoint; i++) {
            r[i] = m.getRightTour()[i];
            l[i] = f.getLeftTour()[i];
        }
        for (int i = xpoint; i < r.length; i++) {
            if (f.getRightTour()[i] < m.getLeftTour()[i]) r[i] = f.getRightTour()[i]; else r[i] = m.getRightTour()[i];
            if (f.getRightTour()[i] < m.getLeftTour()[i]) l[i] = f.getLeftTour()[i]; else l[i] = m.getLeftTour()[i];
        }
        return SolutionHelper.createSolution(f.getRelatedPopulation(), l, r);
    }
}
