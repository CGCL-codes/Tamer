package net.maizegenetics.analysis;

import net.maizegenetics.analysis.PositionType;
import net.maizegenetics.reports.*;
import pal.misc.TableReport;
import pal.alignment.*;
import pal.distance.*;
import pal.datatype.*;
import java.io.Serializable;
import java.util.*;

/**
 *This class provides the distribution of polymorphisms
 *
 * @author Ed Buckler
 * @version 1.0
 */
public class PolymorphismDistribution implements TableReport, Serializable {

    Vector polyDistResultsVector = new Vector();

    int maxSeqCount = -1;

    public PolymorphismDistribution() {
    }

    public void addDistribution(String label, SitePattern theSP, boolean poolMinor) {
        int numStates = theSP.getDataType().getNumStates();
        int[] pdist = new int[theSP.getSequenceCount()];
        if (maxSeqCount < theSP.getSequenceCount()) maxSeqCount = theSP.getSequenceCount();
        int[] stateCount;
        int majorityState = -1, maxCount, dist;
        for (int i = 0; i < theSP.numPatterns; i++) {
            stateCount = new int[numStates + 1];
            for (int j = 0; j < theSP.getSequenceCount(); j++) {
                stateCount[theSP.pattern[j][i]]++;
            }
            maxCount = -1;
            for (int j = 0; j < numStates; j++) {
                if (stateCount[j] > maxCount) {
                    maxCount = stateCount[j];
                    majorityState = j;
                }
            }
            if (poolMinor) {
                dist = theSP.getSequenceCount() - stateCount[majorityState] - stateCount[numStates];
                pdist[dist] += theSP.weight[i];
            } else {
                for (int j = 0; j < numStates; j++) {
                    if ((j != majorityState) && (stateCount[j] != 0)) {
                        pdist[stateCount[j]] += theSP.weight[i];
                    }
                }
            }
        }
        PolymorphismDistributionResults pdr = new PolymorphismDistributionResults(label, pdist, poolMinor);
        polyDistResultsVector.add(pdr);
    }

    public Object[] getTableColumnNames() {
        String[] basicLabels = new String[1 + polyDistResultsVector.size()];
        basicLabels[0] = "Site_Freq";
        PolymorphismDistributionResults pdr;
        for (int i = 0; i < polyDistResultsVector.size(); i++) {
            pdr = (PolymorphismDistributionResults) polyDistResultsVector.get(i);
            basicLabels[i + 1] = pdr.label;
        }
        return basicLabels;
    }

    public Object[][] getTableData() {
        Object[][] data;
        int basicCols = 1, labelOffset;
        PolymorphismDistributionResults pdr;
        data = new String[maxSeqCount + 1][basicCols + polyDistResultsVector.size()];
        data[0][0] = "N";
        for (int i = 0; i < maxSeqCount; i++) {
            data[i + 1][0] = "" + i;
        }
        for (int i = 0; i < polyDistResultsVector.size(); i++) {
            pdr = (PolymorphismDistributionResults) polyDistResultsVector.get(i);
            data[0][i + 1] = "" + pdr.polyDist.length;
            for (int j = 0; j < pdr.polyDist.length; j++) {
                data[j + 1][i + 1] = "" + pdr.polyDist[j];
            }
        }
        return data;
    }

    public String getTableTitle() {
        return "Polymorphism Distribution";
    }

    public String toString() {
        if (polyDistResultsVector.size() == 0) {
            return "Needs to be run";
        }
        StringBuffer cs = new StringBuffer();
        Object[] header = this.getTableColumnNames();
        for (int i = 0; i < header.length; i++) {
            cs.append(RightJustifiedFormat.monoString(header[i]));
        }
        cs.append("\n");
        Object[][] data = this.getTableData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                cs.append(RightJustifiedFormat.monoString(data[i][j]));
            }
            cs.append("\n");
        }
        return cs.toString();
    }
}

class PolymorphismDistributionResults implements Serializable {

    protected int[] polyDist;

    protected String label;

    protected boolean poolMinor;

    private int index;

    public PolymorphismDistributionResults(String label, int[] dist, boolean poolMinor) {
        this.label = label;
        this.poolMinor = poolMinor;
        this.polyDist = dist;
    }

    public boolean equals(Object anObject) {
        PolymorphismDistributionResults x = (PolymorphismDistributionResults) anObject;
        if (x.index == this.index) {
            return true;
        } else {
            return false;
        }
    }

    public void setIndex(int theIndex) {
        this.index = theIndex;
    }

    public String toString() {
        String result = "Polymorphism Distribution for " + label + "\n";
        result += "Pool Minor =" + poolMinor + "\n";
        result += "Dist =" + polyDist.toString() + "\n";
        return result;
    }
}
