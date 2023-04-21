package org.apache.hadoop.util;

/**
 * A facility for reporting progress.
 * 
 * <p>Clients and/or applications can use the provided <code>Progressable</code>
 * to explicitly report progress to the Hadoop framework. This is especially
 * important for operations which take an insignificant amount of time since,
 * in-lieu of the reported progress, the framework has to assume that an error
 * has occured and time-out the operation.</p>
 */
public interface Progressable {

    /**
   * Report progress to the Hadoop framework.
   */
    public void progress();
}
