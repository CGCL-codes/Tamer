package javax.print.attribute.standard;

import javax.print.attribute.DocAttribute;
import javax.print.attribute.EnumSyntax;
import javax.print.attribute.PrintJobAttribute;
import javax.print.attribute.PrintRequestAttribute;

/**
 * The <code>Chromaticity</code> printing attribute specifies if print data
 * should be printed in monochrome or color.
 * <p>
 * The attribute interacts with the document to be printed. If the document
 * to be printed is a monochrome document it will be printed monochrome 
 * regardless of the value of this attribute category. However if it is a
 * color document supplying the attribute value <code>MONOCHROME</code>
 * will prepare the document to be printed in monochrome instead of color.
 * </p>
 * <p>
 * This printing attribute has nothing to do with the capabilities of the
 * printer device. To check if a specific printer service supports printing
 * in color you have to use the attribute
 * {@link javax.print.attribute.standard.ColorSupported}
 * </p>
 * <p>
 * <b>IPP Compatibility:</b> Chromaticity is not an IPP 1.1 attribute.
 * </p>
 *  
 * @author Michael Koch (konqueror@gmx.de)
 */
public final class Chromaticity extends EnumSyntax implements DocAttribute, PrintRequestAttribute, PrintJobAttribute {

    private static final long serialVersionUID = 4660543931355214012L;

    /** Specifies monochrome printing. */
    public static final Chromaticity MONOCHROME = new Chromaticity(0);

    /** Specifies color printing. */
    public static final Chromaticity COLOR = new Chromaticity(1);

    private static final String[] stringTable = { "monochrome", "color" };

    private static final Chromaticity[] enumValueTable = { MONOCHROME, COLOR };

    /**
   * Creates a <code>Chromaticity</code> object.
   *
   * @param value the enum value
   */
    protected Chromaticity(int value) {
        super(value);
    }

    /**
   * Returns category of this class.
   *
   * @return The class <code>Chromaticity</code> itself.
   */
    public Class getCategory() {
        return Chromaticity.class;
    }

    /**
   * Returns the name of this attribute.
   *
   * @return The name "chromaticity".
   */
    public String getName() {
        return "chromaticity";
    }

    /**
   * Returns a table with the enumeration values represented as strings
   * for this object.
   *
   * @return The enumeration values as strings.
   */
    protected String[] getStringTable() {
        return stringTable;
    }

    /**
   * Returns a table with the enumeration values for this object.
   *
   * @return The enumeration values.
   */
    protected EnumSyntax[] getEnumValueTable() {
        return enumValueTable;
    }
}
