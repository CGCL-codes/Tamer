package net.sf.irunninglog.jdbc;

import java.math.BigDecimal;
import java.util.Date;
import net.sf.irunninglog.canonical.Shoe;

/**
 * <code>IMapper</code> implementation for Shoe business objects.
 *
 * @author <a href="mailto:allan_e_lewis@yahoo.com">Allan Lewis</a>
 * @version $Revision: 1.2 $ $Date: 2005/10/01 13:23:23 $
 * @since iRunningLog 1.1
 */
public final class ShoeMapper implements IMapper {

    /**
     * Get an ordered listing of the business object's property names.  The
     * array returned from this method should provide a listing of property
     * names for a business object class.  This array's ordering should be
     * consistent with the ordering of the class names returned from the
     * <code>getOrdererdPropertyClasses</code> method.
     *
     * @return An ordered listing of the business object's property names
     * @see #getOrderedPropertyClasses()
     */
    public String[] getOrderedPropertyNames() {
        return new String[] { Shoe.FIELD_ID, Shoe.FIELD_RUNNER_ID, Shoe.FIELD_DESCRIPTION, Shoe.FIELD_DEFAULT, Shoe.FIELD_RETIRED, Shoe.FIELD_START_DATE, Shoe.FIELD_START_MILEAGE };
    }

    /**
     * Get an ordered listing of the business object's property classes.  The
     * array returned from this method should provide a listing of property
     * classes for a business object class.  This array's ordering should be
     * consistent with the ordering of the property names returned from the
     * <code>getOrdererdPropertyNames</code> method.
     *
     * @return An ordered listing of the business object's property classes
     * @see #getOrderedPropertyNames()
     */
    public Class[] getOrderedPropertyClasses() {
        return new Class[] { String.class, String.class, String.class, Boolean.class, Boolean.class, Date.class, BigDecimal.class };
    }
}
