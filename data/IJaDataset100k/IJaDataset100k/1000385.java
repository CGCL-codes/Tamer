package net.sf.openv4j;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class CycleTimes {

    private CycleTimeEntry[] entries;

    /**
     * Creates a new CycleTimes object.
     *
     * @param numberOfCycles DOCUMENT ME!
     */
    public CycleTimes(int numberOfCycles) {
        entries = new CycleTimeEntry[numberOfCycles];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CycleTimeEntry getEntry(int i) {
        return entries[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param entry DOCUMENT ME!
     */
    public void setEntry(int i, CycleTimeEntry entry) {
        entries[i] = entry;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CycleTimeEntry ct : entries) {
            if (ct == null) {
                sb.append("ON --:-- OFF --:-- | ");
            } else {
                sb.append(String.format("ON %02d:%02d OFF %02d:%02d | ", ct.getStartHour(), ct.getStartMin(), ct.getEndHour(), ct.getEndMin()));
            }
        }
        sb.delete(sb.lastIndexOf(" | "), sb.length());
        return sb.toString();
    }
}
