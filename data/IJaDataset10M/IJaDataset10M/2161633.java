package net.sf.openforge.report.throughput;

import java.io.*;

/**
 * ThroughputLimit is an interface used by classes which track any
 * limitation on how often new data can be applied to a given task.
 *
 * <p>Created: Thu Jan 30 09:46:19 2003
 *
 * @author imiller, last modified by $Author: imiller $
 * @version $Id: ThroughputLimit.java 2 2005-06-09 20:00:48Z imiller $
 */
public interface ThroughputLimit {

    /**
     * Retrieves the minimum clocks that you must wait between
     * consecutive assertions of the GO because of a limitation
     * imposed by an instance of this class.
     */
    public int getLimit();

    /**
     * Writes reporting information to the given stream based on the
     * throughput limitation tracked here.
     */
    public void writeReport(PrintStream ps, int tabDepth);

    public String toString();
}
