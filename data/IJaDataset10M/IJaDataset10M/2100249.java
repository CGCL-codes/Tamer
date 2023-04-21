package uk.ac.ncl.neresc.dynasoar.utils;

import uk.ac.besc.crisp.machine.MachineInfo;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: davient
 * Date: 05-Dec-2008
 * Time: 12:56:38
 * To change this template use File | Settings | File Templates.
 */
public class MachineInfoMemoryComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        MachineInfo mA = (MachineInfo) o1;
        MachineInfo mB = (MachineInfo) o2;
        if (mA.getMainMemoryRAMAvailable() < mB.getMainMemoryRAMAvailable()) {
            return 1;
        } else if (mA.getMainMemoryRAMAvailable() > mB.getMainMemoryRAMAvailable()) {
            return -1;
        }
        return 0;
    }
}
