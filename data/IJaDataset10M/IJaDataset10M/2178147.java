package at.ac.ait.enviro.tsapi.util.geo;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Helper class for Geometry handling
 * @author BonitzA
 */
public final class GeometryHelper {

    private static final DecimalFormat longFormat;

    private static final DecimalFormat latFormat;

    static {
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setNaN("NaN");
        sym.setDecimalSeparator('.');
        longFormat = new DecimalFormat();
        setupFormatter(longFormat, sym, "E", "W");
        latFormat = new DecimalFormat();
        setupFormatter(latFormat, sym, "N", "S");
    }

    private static final void setupFormatter(final DecimalFormat df, final DecimalFormatSymbols sym, final String psuf, final String nsuf) {
        df.setDecimalFormatSymbols(sym);
        df.setNegativePrefix("");
        df.setDecimalSeparatorAlwaysShown(true);
        df.setMaximumFractionDigits(5);
        df.setMinimumFractionDigits(1);
        df.setPositiveSuffix(psuf);
        df.setNegativeSuffix(nsuf);
    }

    /**
     * Creates a String from a String with coordinates for the TimeSeries API
     * @param geometry
     *      Any kind of geometry
     * @return a formatted String, can be {@code "WGS84 ggg.ggN ggg.ggW"} (point),
     *      {@code "WGS84 ggg.ggN ggg.ggW..ggg.ggS ggg.ggE"} (rectangle)
     */
    public static final String parseToString(final Geometry geometry) {
        StringBuilder content = new StringBuilder();
        Envelope env = geometry.getEnvelopeInternal();
        content.append(latFormat.format(env.getMinX()));
        content.append(" ");
        content.append(longFormat.format(env.getMinY()));
        if (env.getMinY() != env.getMaxY() && env.getMinX() != env.getMaxX()) {
            content.append("..");
            content.append(latFormat.format(env.getMaxX()));
            content.append(" ");
            content.append(longFormat.format(env.getMaxY()));
        }
        return content.toString();
    }
}
