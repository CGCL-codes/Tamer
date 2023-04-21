package schedsim.helper;

/**
 * <br>
 * Beschreibung:<br>
 * This data structure contains the cpu number and the
 * time slice for each cpu. This class is needed for the
 * combo box in the cmd list panel class.
 * <br>
 * 
 * <br>
 * <pre>
 * Datum:           08.12.2003
 * Class:			CPUTimeSlice.java
 * 
 * ************************************************
 * History
 * ************************************************
 * Date			Author			Description
 * 
 * ************************************************
 * </pre>
 * 
 * @author         Martin Migasiewicz
 * @version		1.0
 */
public class CPUTimeSlice {

    private int cpu;

    private int timeslice;

    public CPUTimeSlice(int cpu, int timeslice) {
        this.cpu = cpu;
        this.timeslice = timeslice;
    }

    public void setCPU(int cpu) {
        this.cpu = cpu;
    }

    public int getCPU() {
        return this.cpu;
    }

    public void setTimeslice(int timeslice) {
        this.timeslice = timeslice;
    }

    public int getTimeslice() {
        return this.timeslice;
    }

    public String toString() {
        String result = null;
        result = "CPU" + this.cpu + "   Q=" + this.timeslice;
        return result;
    }
}
