package de.mguennewig.pobjects.metadata;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import de.mguennewig.pobjects.Container;
import de.mguennewig.pobjects.PObjSyntaxException;

/** Floating point type with double precision.
 *
 * @author Michael G�nnewig
 */
public class DoubleType extends ScalarType {

    /** Creates a new floating point type. */
    public DoubleType() {
        super();
    }

    /** {@inheritDoc} */
    public final Class<Double> getJavaClass() {
        return Double.class;
    }

    /** {@inheritDoc} */
    @Override
    public final int getMaxFieldSize() {
        if (getMap() != null) return super.getMaxFieldSize();
        return Math.max(Double.toString(Double.MAX_VALUE).length(), Double.toString(Double.MIN_VALUE).length()) + 1;
    }

    /** {@inheritDoc}
   *
   * @return a {@link Double}.
   */
    public Double parseNative(final String value) throws PObjSyntaxException {
        if (value == null || value.length() == 0) return null;
        try {
            return Double.valueOf(value);
        } catch (NumberFormatException e) {
            throw new PObjSyntaxException(INVALID_VALUE, value);
        }
    }

    /** {@inheritDoc}
   *
   * @param db The database container that created the statement (Unused).
   */
    public void setParam(final Container db, final PreparedStatement statm, final int pos, final Object value) throws SQLException {
        if (value == null) statm.setNull(pos, Types.DOUBLE); else statm.setDouble(pos, ((Double) value).doubleValue());
    }
}
