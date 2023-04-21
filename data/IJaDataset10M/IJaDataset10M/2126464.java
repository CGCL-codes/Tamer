package org.jikesrvm.adaptive.measurements;

/**
 * Interface for all reportable objects that are managed by the runtime
 * measurements.
 */
public interface VM_Reportable {

    /**
   * generate a report
   */
    void report();

    /**
   * reset (clear) data set being gathered
   */
    void reset();
}
