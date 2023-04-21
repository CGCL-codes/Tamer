package de.schlund.pfixxml.exceptionhandler;

/**
 * Class representing the general configuration for exceptionhandler.
 * <br/>
 * @author <a href="mailto: haecker@schlund.de">Joerg Haecker</a>
 */
class GeneralConfig {

    private int cleanupschedule_ = 0;

    private String cleanupscheduledim_ = null;

    private int reportschedule_ = 0;

    private String reportscheduledim_ = null;

    private int straceobsolete_ = 0;

    private String straceobsoletedim_ = null;

    private boolean useme_ = false;

    GeneralConfig(boolean useme, int clsched, String clscheddim, int repsched, String repscheddim, int stobsolete, String stobsoletedim) {
        this.useme_ = useme;
        this.cleanupschedule_ = clsched;
        this.cleanupscheduledim_ = clscheddim;
        this.reportschedule_ = repsched;
        this.reportscheduledim_ = repscheddim;
        this.straceobsolete_ = stobsolete;
        this.straceobsoletedim_ = stobsoletedim;
    }

    /**
     * Returns the cleanupschedule.
     * @return int
     */
    int getCleanupSchedule() {
        return cleanupschedule_;
    }

    /**
     * Returns the cleanupscheduledim.
     * @return String
     */
    String getCleanupScheduledim() {
        return cleanupscheduledim_;
    }

    int getReportSchedule() {
        return reportschedule_;
    }

    /**
     * Returns the reportscheduledim.
     * @return String
     */
    String getReportScheduledim() {
        return reportscheduledim_;
    }

    /**
     * Returns the straceobsolete.
     * @return int
     */
    int getStraceObsolete() {
        return straceobsolete_;
    }

    /**
     * Returns the straceobsoletedim.
     * @return String
     */
    String getStraceObsoleteDim() {
        return straceobsoletedim_;
    }

    /**
     * Returns the useme.
     * @return boolean
     */
    boolean isUseme() {
        return useme_;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(128);
        sb.append("Use me         =" + useme_).append("\n");
        sb.append("Cleanup        = all " + cleanupschedule_ + " " + cleanupscheduledim_).append("\n");
        sb.append("Strace obsolete=" + straceobsolete_ + " " + straceobsoletedim_).append("\n");
        sb.append("Report         = all " + reportschedule_ + " " + reportscheduledim_).append("\n");
        return sb.toString();
    }
}
