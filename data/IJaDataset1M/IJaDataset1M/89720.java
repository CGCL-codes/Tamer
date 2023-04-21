package net.sf.mzmine.modules.peaklistmethods.identification.dbsearch;

import java.net.URL;
import net.sf.mzmine.data.IsotopePattern;
import net.sf.mzmine.data.impl.SimplePeakIdentity;

public class DBCompound extends SimplePeakIdentity {

    private final URL compoundUrl;

    private final URL structure2dUrl;

    private final URL structure3dUrl;

    private Double isotopePatternScore;

    private IsotopePattern isotopePattern;

    private final OnlineDatabase database;

    /**
     * @param db      the database the compound is from.
     * @param id      the compound's ID in the database.
     * @param name    the compound's formula.
     * @param formula the compound's name.
     * @param urlDb   the URL of the compound in the database.
     * @param url2d   the URL of the compound's 2D structure.
     * @param url3d   the URL of the compound's 3D structure.
     */
    public DBCompound(final OnlineDatabase db, final String id, final String name, final String formula, final URL urlDb, final URL url2d, final URL url3d) {
        super(name, formula, db + " search", id, urlDb.toString());
        database = db;
        compoundUrl = urlDb;
        structure2dUrl = url2d;
        structure3dUrl = url3d;
        isotopePatternScore = null;
        isotopePattern = null;
    }

    /**
     * @return Returns the 2D structure URL.
     */
    public URL get2DStructureURL() {
        return structure2dUrl;
    }

    /**
     * @return Returns the 3D structure URL.
     */
    public URL get3DStructureURL() {
        return structure3dUrl;
    }

    /**
     * Returns the isotope pattern score or null if the score was not calculated.
     *
     * @return isotope pattern score.
     */
    public Double getIsotopePatternScore() {
        return isotopePatternScore;
    }

    /**
     * Set the isotope pattern score. of this compound.
     *
     * @param score the score.
     */
    public void setIsotopePatternScore(final double score) {
        isotopePatternScore = score;
    }

    /**
     * Returns the isotope pattern (predicted) of this compound.
     *
     * @return the isotope pattern
     */
    public IsotopePattern getIsotopePattern() {
        return isotopePattern;
    }

    /**
     * Sets the isotope pattern of this compound.
     *
     * @param pattern the isotope pattern.
     */
    public void setIsotopePattern(final IsotopePattern pattern) {
        isotopePattern = pattern;
    }

    @Override
    public synchronized Object clone() {
        final DBCompound dbCompound = new DBCompound(database, getPropertyValue(PROPERTY_ID), getName(), getPropertyValue(PROPERTY_FORMULA), compoundUrl, structure2dUrl, structure3dUrl);
        dbCompound.setIsotopePattern(isotopePattern);
        dbCompound.setIsotopePatternScore(isotopePatternScore);
        return dbCompound;
    }
}
