package ufpr.mestrado.ais.base.operator.selection;

import jmetal.base.operator.selection.Selection;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import ufpr.mestrado.ais.base.SolutionMNO;
import ufpr.mestrado.ais.util.AdaptiveGridArchiveMNO;

/**
 * This class implements a selection operator as the used in PESA-II algorithm
 */
public class PESA2SelectionMNO extends Selection {

    /**
	 * Performs the operation
	 * 
	 * @param object
	 *            Object representing a SolutionSet. This solution set must be
	 *            an instancen <code>AdaptiveGridArchive</code>
	 * @return the selected solution
	 * @throws JMException
	 */
    public Object execute(Object object) throws JMException {
        try {
            AdaptiveGridArchiveMNO archive = (AdaptiveGridArchiveMNO) object;
            int selected;
            int hypercube1 = archive.getGrid().randomOccupiedHypercube();
            int hypercube2 = archive.getGrid().randomOccupiedHypercube();
            if (hypercube1 != hypercube2) {
                if (archive.getGrid().getLocationDensity(hypercube1) < archive.getGrid().getLocationDensity(hypercube2)) {
                    selected = hypercube1;
                } else if (archive.getGrid().getLocationDensity(hypercube2) < archive.getGrid().getLocationDensity(hypercube1)) {
                    selected = hypercube2;
                } else {
                    if (PseudoRandom.randDouble() < 0.5) {
                        selected = hypercube2;
                    } else {
                        selected = hypercube1;
                    }
                }
            } else {
                selected = hypercube1;
            }
            int base = PseudoRandom.randInt(0, archive.size() - 1);
            int cnt = 0;
            while (cnt < archive.size()) {
                SolutionMNO individual = (SolutionMNO) archive.get((base + cnt) % archive.size());
                if (archive.getGrid().location(individual) != selected) {
                    cnt++;
                } else {
                    return individual;
                }
            }
            return (SolutionMNO) archive.get((base + cnt) % archive.size());
        } catch (ClassCastException e) {
            Configuration.logger_.severe("PESA2SelectionMNO.execute: ClassCastException. " + "Found" + object.getClass() + "Expected: AdaptativeGridArchive");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }
    }
}
