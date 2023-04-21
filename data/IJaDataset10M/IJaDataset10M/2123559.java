package de.searchworkorange.lib.statusserverclient;

import java.util.ArrayList;

/**
 *
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class CommandServerReport {

    private int open;

    private long processed;

    private String mode;

    private ArrayList<SingleCommandReport> commandReportList = null;

    public CommandServerReport() {
        commandReportList = new ArrayList<SingleCommandReport>();
    }

    public void clearCommandReportList() {
        commandReportList = new ArrayList<SingleCommandReport>();
    }

    /**
     * 
     * @param report
     */
    public void addCommandReport(SingleCommandReport report) {
        if (report == null) {
            throw new IllegalArgumentException();
        } else {
            commandReportList.add(report);
        }
    }

    /**
     *
     * @return ArrayList<SingleCommandReport>
     */
    public ArrayList<SingleCommandReport> getCommandReportList() {
        return commandReportList;
    }

    /**
     * 
     * @return int
     */
    public int getOpen() {
        return open;
    }

    /**
     *
     * @return long
     */
    public long getProcessed() {
        return processed;
    }

    /**
     *
     * @return String
     */
    public String getMode() {
        return mode;
    }

    /**
     *
     * @param open
     */
    public void setOpen(int open) {
        if (open < 0) {
            this.open = 0;
        } else {
            this.open = open;
        }
    }

    /**
     * 
     * @param processed
     */
    public void setProcessed(long processed) {
        if (processed < 0) {
            this.processed = 0;
        } else {
            this.processed = processed;
        }
    }

    /**
     * 
     * @param mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
}
