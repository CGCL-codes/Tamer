package javax.print.attribute.standard;

import javax.print.attribute.SetOfIntegerSyntax;
import javax.print.attribute.SupportedValuesAttribute;

/**
 * The <code>JobMediaSheetsSupported</code> printing attribute specifies the 
 * supported range of values for the 
 * {@link javax.print.attribute.standard.JobMediaSheets} attribute.
 * <p>
 * <b>IPP Compatibility:</b> JobMediaSheetsSupported is an IPP 1.1 attribute.
 * </p>
 * 
 * @author Michael Koch (konqueror@gmx.de)
 * @author Wolfgang Baer (WBaer@gmx.de)
 */
public final class JobMediaSheetsSupported extends SetOfIntegerSyntax implements SupportedValuesAttribute {

    private static final long serialVersionUID = 2953685470388672940L;

    /**
   * Constructs a <code>JobMediaSheetsSupported</code> object with the 
   * given range of supported job media sheets values.
   *
   * @param lowerBound the lower bound value
   * @param upperBound the upper bound value
   *
   * @exception IllegalArgumentException if lowerBound &lt;= upperbound
   * and lowerBound &lt; 1
   */
    public JobMediaSheetsSupported(int lowerBound, int upperBound) {
        super(lowerBound, upperBound);
        if (lowerBound < 1) throw new IllegalArgumentException("lowerBound may not be less than 1");
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
        if (!(obj instanceof JobMediaSheetsSupported)) return false;
        return super.equals(obj);
    }

    /**
   * Returns category of this class.
   *
   * @return The class <code>JobMediaSheetsSupported</code> itself.
   */
    public Class getCategory() {
        return JobMediaSheetsSupported.class;
    }

    /**
   * Returns the name of this attribute.
   *
   * @return The name "job-media-sheets-supported".
   */
    public String getName() {
        return "job-media-sheets-supported";
    }
}
