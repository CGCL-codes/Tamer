package org.gwanted.gwt.core.client.data;

/**
 * @author Juan Carlos Paz
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface HasAutoRefreshStatus {

    /** DOCUMENT ME! */
    public static final AutoRefreshStatus STARTED = new AutoRefreshStatus("STARTED");

    /** DOCUMENT ME! */
    public static final AutoRefreshStatus PAUSED = new AutoRefreshStatus("PAUSED");

    /** DOCUMENT ME! */
    public static final AutoRefreshStatus STOPPED = new AutoRefreshStatus("STOPPED");

    /**
   * DOCUMENT ME!
   *
   * @param status DOCUMENT ME!
   */
    void setAutoRefreshStatus(final AutoRefreshStatus status);

    /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME! $returnType$
   */
    AutoRefreshStatus getAutoRefreshStatus();

    /**
   * DOCUMENT ME!
   *
   * @author $author$
   * @version $Revision: 1.3 $
   */
    public static class AutoRefreshStatus {

        private String status = null;

        /**
     * Creates a new AutoRefreshStatus object.
     *
     * @param status DOCUMENT ME!
     */
        public AutoRefreshStatus(final String status) {
            this.status = status;
        }

        /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME! $returnType$
     */
        public String toString() {
            return this.status;
        }

        /**
     * DOCUMENT ME!
     *
     * @param anObject DOCUMENT ME!
     *
     * @return DOCUMENT ME! $returnType$
     */
        public boolean equals(final Object anObject) {
            if (this == anObject) {
                return true;
            }
            if ((anObject instanceof HasAutoRefreshStatus.AutoRefreshStatus) && (this.status == ((HasAutoRefreshStatus.AutoRefreshStatus) anObject).status)) {
                return true;
            }
            return false;
        }
    }
}
