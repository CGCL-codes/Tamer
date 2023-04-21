package org.vikamine.swing.subgroup.visualization.roc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.vikamine.app.DMManager;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.Population;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGStatisticsBinary;
import org.vikamine.kernel.subgroup.target.BooleanTarget;
import org.vikamine.kernel.subgroup.target.SGTarget;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;

/**
 * @author Tobias Vogele
 */
public class ROCCurveCalculator implements IPoint2dForSGCalculator {

    static class ClassifiedInstance implements Comparable {

        private boolean positive;

        private double probability;

        @Override
        public int hashCode() {
            int hash = positive ? 1 : 0;
            hash = 17 * hash + (int) probability * 97;
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            } else if (o == this) {
                return true;
            } else if (o.getClass() != getClass()) {
                return false;
            } else {
                ROCCurveCalculator.ClassifiedInstance c = (ROCCurveCalculator.ClassifiedInstance) o;
                return positive == c.positive && probability == c.probability;
            }
        }

        public int compareTo(Object o) {
            ClassifiedInstance c = (ClassifiedInstance) o;
            if (probability < c.probability) {
                return 1;
            } else if (probability > c.probability) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public ROCCurveCalculator() {
        super();
    }

    public List<Point2D> getPoints(List subgroups) {
        if (subgroups.isEmpty()) {
            return new LinkedList();
        }
        List classifications = classifyInstances(subgroups);
        List points = new LinkedList();
        int tp = 0;
        int fp = 0;
        double pos = countPositives(classifications);
        double neg = classifications.size() - pos;
        double previous = Double.NaN;
        for (Iterator iter = classifications.iterator(); iter.hasNext(); ) {
            ClassifiedInstance inst = (ClassifiedInstance) iter.next();
            if (previous != inst.probability) {
                points.add(new Point2D.Double(fp / neg, tp / pos));
                previous = inst.probability;
            }
            if (inst.positive) {
                tp++;
            } else {
                fp++;
            }
        }
        points.add(new Point2D.Double(fp / neg, tp / pos));
        return points;
    }

    private double countPositives(List classifications) {
        int count = 0;
        for (Iterator iter = classifications.iterator(); iter.hasNext(); ) {
            ClassifiedInstance c = (ClassifiedInstance) iter.next();
            if (c.positive) {
                count++;
            }
        }
        return count;
    }

    private List classifyInstances(List subgroups) {
        Population popu = DMManager.getInstance().getOntology().getPopulation();
        List classifications = new ArrayList(popu.size());
        SGTarget sgtarget = AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup().getTarget();
        BooleanTarget target = null;
        if (sgtarget instanceof BooleanTarget) {
            target = (BooleanTarget) sgtarget;
        }
        for (Iterator<DataRecord> iter = popu.instanceIterator(); iter.hasNext(); ) {
            DataRecord inst = iter.next();
            ClassifiedInstance clas = new ClassifiedInstance();
            clas.probability = getProbability(inst, subgroups);
            clas.positive = (target != null && target.isPositive(inst));
            classifications.add(clas);
        }
        Collections.sort(classifications);
        return classifications;
    }

    private double getProbability(DataRecord inst, List<SG> subgroups) {
        int sgsCount = 0;
        double pSum = 0;
        for (Iterator iter = subgroups.iterator(); iter.hasNext(); ) {
            SG sg = (SG) iter.next();
            if (!sg.getTarget().isBoolean()) {
                throw new IllegalArgumentException();
            }
            if (sg.getSGDescription().isMatching(inst)) {
                sgsCount++;
                pSum += ((SGStatisticsBinary) sg.getStatistics()).getP();
            }
        }
        if (sgsCount == 0) {
            return 0;
        }
        return pSum / sgsCount;
    }
}
