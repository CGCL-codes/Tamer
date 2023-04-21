package javax.print.attribute.standard;

import javax.print.attribute.IntegerSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/**
 * The <code>JobMediaSheets</code> printing attribute specifies
 * the total number of media sheets needed by a job.
 * <p>
 * In contrary to the other job size attributes this attribute must include
 * the multiplication factor from the number of copies if a Copies attribute
 * was specified for the job.
 * </p>
 * <p>
 * This attribute belongs to a group of job size attributes which are 
 * describing the size of a job to be printed. The values supplied by
 * these attributes are intended to be used for routing and scheduling
 * of jobs on the print service. A client may specify these attributes.
 * If a clients supplies these attributes a print service may change
 * the values if its be able to compute a more accurate value at the
 * time of the job submission or also later.
 * </p>
 * <p>
 * <b>IPP Compatibility:</b> JobMediaSheets is an IPP 1.1 attribute.
 * </p>
 * @see javax.print.attribute.standard.JobKOctets
 * @see javax.print.attribute.standard.JobImpressions
 * 
 * @author Michael Koch
 */
public class JobMediaSheets extends IntegerSyntax implements PrintJobAttribute, PrintRequestAttribute {

    private static final long serialVersionUID = 408871131531979741L;

    /**
   * Creates a <code>JobMediaSheets</code> object.
   *
   * @param value the number of media sheets for a print job
   *
   * @exception IllegalArgumentException if value &lt; 0
   */
    public JobMediaSheets(int value) {
        super(value);
        if (value < 0) throw new IllegalArgumentException("value may not be less than 0");
    }

    /**
   * Tests if the given object is equal to this object.
   *
   * @param obj the object to test
   *
   * @return <code>true</code> if both objects are equal, 
   * <code>false</code> otherwise.
   */
    public boolean equals(Object obj) {
        if (!(obj instanceof JobMediaSheets)) return false;
        return super.equals(obj);
    }

    /**
   * Returns category of this class.
   *
   * @return The class <code>JobMediaSheets</code> itself.
   */
    public Class getCategory() {
        return JobMediaSheets.class;
    }

    /**
   * Returns the name of this attribute.
   *
   * @return The name "job-media-sheets".
   */
    public String getName() {
        return "job-media-sheets";
    }
}
