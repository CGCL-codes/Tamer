package net.sourceforge.harness;

/**
 * Signals an error during the actual test run.
 * 
 * <p><b>Usage:</b> <code>trivial</code>
 *
 * <p><b>History:</b>
 * <ul>
 *   <li>20011019, Initial file</li>
 * </ul>
 *
 * <p><b>CVS Information:</b><br>
 * <i>
 * $Date: 2002/10/09 13:11:31 $<br>
 * $Revision: 1.3 $<br>
 * $Author: mgl $<br>
 * </i>
 *
 * @author Marcel Schepers (mgl@dds.nl)
 */
public class RunException extends HarnessException {

    /**
   * Creates a new <code>RunException</code> instance.
   *
   * @param exception an <code>Exception</code> value
   * @param source an <code>Object</code> value
   * @param descripion a <code>String</code> value
   */
    public RunException(Exception exception, Object source, String description) {
        super(exception, source, description);
    }

    /**
   * Creates a new <code>RunException</code> instance, without a
   * description.
   *
   * @param exception an <code>Exception</code> value
   * @param source an <code>Object</code> value
   */
    public RunException(Exception exception, Object source) {
        super(exception, source);
    }

    /**
   * Creates a new <code>RunException</code> instance, without a
   * description or source indicator
   *
   * @param exception an <code>Exception</code> value
   */
    public RunException(Exception exception) {
        super(exception);
    }
}
