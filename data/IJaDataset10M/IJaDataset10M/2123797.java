package org.vizzini.math;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides a formatter for <code>Vector</code> s.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @see      org.vizzini.math.Vector
 * @since    v0.3
 */
public class VectorFormat {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(VectorFormat.class.getName());

    /** Formatter. */
    private DecimalFormat _formatter;

    /**
     * Construct this object.
     */
    public VectorFormat() {
        this(2);
    }

    /**
     * Construct this object with the given parameters.
     *
     * @param  places  Number of decimal places to show.
     */
    public VectorFormat(int places) {
        if (places < 0) {
            throw new IllegalArgumentException("places < 0");
        }
        _formatter = (DecimalFormat) NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder("0.");
        for (int i = 0; i < places; i++) {
            sb.append("0");
        }
        sb.append("E00");
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("sb.toString() = " + sb.toString());
        }
        _formatter.applyPattern("+" + sb.toString() + ";-" + sb.toString());
    }

    /**
     * @param   vector  Vector to format.
     *
     * @return  a formatted string version of the given <code>Vector</code>.
     *
     * @since   v0.3
     */
    public String format(Vector vector) {
        String x = _formatter.format(vector.getX());
        String y = _formatter.format(vector.getY());
        String z = _formatter.format(vector.getZ());
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(x).append(",");
        sb.append(y).append(",");
        sb.append(z).append(")");
        return sb.toString();
    }
}
