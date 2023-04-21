package fr.ird.database.sample.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import javax.media.jai.util.Range;
import org.geotools.units.Unit;
import org.geotools.cs.CoordinateSystem;
import org.geotools.cs.GeographicCoordinateSystem;
import fr.ird.database.sample.CruiseEntry;

/**
 * Un �chantillon pris sur une ligne (par exemple une capture � la palangre).
 *
 * @version $Id: LinearSampleEntry.java,v 1.2 2003/07/07 09:30:23 desruisseaux Exp $
 * @author Martin Desruisseaux
 */
final class LinearSampleEntry extends SampleEntry {

    /**
     * Num�ro de s�rie pour compatibilit� entre diff�rentes versions.
     */
    private static final long serialVersionUID = -856538436667543534L;

    /**
     * Date et heure de l'�chantillon, en nombre de
     * millisecondes �coul�es depuis le 1 janvier 1970.
     */
    private final long date;

    /**
     * Longitude et latitude du d�but de l'�chantillon, en degr�s.
     */
    private final float x1, y1;

    /**
     * Longitude et latitude de la fin de l'�chantillon, en degr�s.
     */
    private final float x2, y2;

    /**
     * Construit un enregistrement repr�sentant un �chantillon mesur�e sur une ligne.
     *
     * @param  table  Table d'o� proviennent les donn�es.
     * @param  result R�sultat de la requ�te SQL.
     * @throws SQLException si l'interrogation de la base de donn�es a �chou�.
     */
    public LinearSampleEntry(final LinearSampleTable table, final ResultSet result) throws SQLException {
        super(result.getInt(LinearSampleTable.ID), null, table.species.species);
        final float efu;
        date = table.getTimestamp(LinearSampleTable.DATE, result).getTime();
        x1 = getFloat(result, LinearSampleTable.START_LONGITUDE);
        y1 = getFloat(result, LinearSampleTable.START_LATITUDE);
        x2 = getFloat(result, LinearSampleTable.END_LONGITUDE);
        y2 = getFloat(result, LinearSampleTable.END_LATITUDE);
        efu = getFloat(result, LinearSampleTable.EFFORT_UNIT) / 1000;
        for (int i = 0; i < amount.length; i++) {
            amount[i] = getFloat(result, LinearSampleTable.SAMPLE_VALUE + i) / efu;
        }
    }

    /**
     * Retourne le nombre r�el de la colonne sp�cifi�, ou
     * <code>NaN</code> si ce nombre r�el n'est pas sp�cifi�.
     */
    private static float getFloat(final ResultSet result, final int column) throws SQLException {
        final float value = result.getFloat(column);
        return result.wasNull() ? Float.NaN : value;
    }

    /**
     * Retourne la moyenne des deux nombres sp�cifi�s. Si un des deux nombres
     * est NaN, l'autre sera retourn�. Si les deux nombres sont NaN, alors NaN
     * sera retourn�.
     */
    private static float mean(final float x1, final float x2) {
        if (Float.isNaN(x1)) return x2;
        if (Float.isNaN(x2)) return x1;
        return (x1 + x2) * 0.5f;
    }

    /**
     * {@inheritDoc}
     */
    public Point2D getCoordinate() {
        return new Point2D.Float(mean(x1, x2), mean(y1, y2));
    }

    /**
     * Retourne une forme repr�sentant la ligne. Si les informations
     * disponibles ne permettent pas de conna�tre la ligne, retourne
     * <code>null</code>.
     */
    public Shape getShape() {
        if (Float.isNaN(x1) || Float.isNaN(x2) || Float.isNaN(y1) || Float.isNaN(y2) || (x1 == x2 && y1 == y2)) {
            return null;
        }
        return new Line2D.Float(x1, y1, x2, y2);
    }

    /**
     * Verifie si la ligne intercepte le rectangle sp�cifi�. Cette m�thode suppose
     * que la trajectoire de l'�chantillonage ne fait pas de crochets.
     */
    public boolean intersects(final Rectangle2D rect) {
        if (Float.isNaN(x1) || Float.isNaN(y1)) return rect.contains(x2, y2);
        if (Float.isNaN(x2) || Float.isNaN(y2)) return rect.contains(x1, y1);
        return rect.intersectsLine(x1, y1, x2, y2);
    }

    /**
     * {@inheritDoc}
     */
    public Date getTime() {
        return new Date(date);
    }

    /**
     * {@inheritDoc}
     */
    public Range getTimeRange() {
        final Date date = new Date(this.date);
        return new Range(Date.class, date, date);
    }

    /**
     * {@inheritDoc}
     */
    public Unit getUnit() {
        return null;
    }

    /**
     * V�rifie si cet �chantillon est identique � l'objet sp�cifi�.
     */
    public boolean equals(final Object other) {
        if (super.equals(other)) {
            final LinearSampleEntry that = (LinearSampleEntry) other;
            return this.date == that.date && Float.floatToIntBits(this.x1) == Float.floatToIntBits(that.x1) && Float.floatToIntBits(this.y1) == Float.floatToIntBits(that.y1) && Float.floatToIntBits(this.x2) == Float.floatToIntBits(that.x2) && Float.floatToIntBits(this.y2) == Float.floatToIntBits(that.y2);
        }
        return false;
    }
}
