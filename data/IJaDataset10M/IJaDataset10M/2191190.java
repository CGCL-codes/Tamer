package net.sf.linuxorg.pcal;

/**
 * This class represents the complete day information 
 * from the PCal engine viewpoint.
 * @author Mar'yan Rachynskyy
 */
public class PCalDayInfo {

    /**-1 if the date state is undefined
     *0 for the first day of the period
     */
    public int day_num = -1;

    /** 
	 * true if day info is just an estimate
     * false if it is the documented fact
     */
    public boolean estimate;

    public boolean fertile;

    public boolean pregnancy;

    public boolean birth;

    public boolean pregnancy_interruption;

    public boolean ovulation;

    public boolean badFeel;

    public String notes;
}

;
