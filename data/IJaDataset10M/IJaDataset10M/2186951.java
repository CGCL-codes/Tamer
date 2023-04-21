package de.gstpl.algo;

import de.gstpl.algo.genetic.GeneticAlgorithmA;
import de.gstpl.algo.hybrid.StepwiseOptimization;
import de.gstpl.algo.localsearch.NoCollisionPrincipleA;
import de.gstpl.algo.localsearch.sa.SimulatedAnnealingA;
import de.peathal.resource.L;
import de.peathal.util.GLog;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class AlgorithmFactory {

    public static <T extends IAlgorithm> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception exc) {
            GLog.error(L.tr("Can't_instantiate:") + " " + clazz.getName(), exc);
            return null;
        }
    }

    public static List<Class<? extends IAlgorithm>> getAllAvailableClasses() {
        List<Class<? extends IAlgorithm>> list = new ArrayList<Class<? extends IAlgorithm>>(3);
        list.add(NoCollisionPrincipleA.class);
        list.add(GeneticAlgorithmA.class);
        list.add(SimulatedAnnealingA.class);
        list.add(StepwiseOptimization.class);
        return list;
    }
}
