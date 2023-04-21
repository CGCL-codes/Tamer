package org.openofficesearch.data;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Holds information about the version of the OpenOffice Search database<br />
 * Created: September 25, 2006, 12:48 PM
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.0.4
 */
public class DatabaseVersion implements Comparable<DatabaseVersion> {

    private int a;

    private int b;

    private int c;

    /**
   * Creates a new instance of DatabaseVersion
   * @param a The main version number
   * @param b The sub-version number
   * @param c The patch level number
   */
    public DatabaseVersion(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
   * Creates a database version from a period separated version number
   * @param version The version number
   */
    public DatabaseVersion(String version) {
        if (StringUtils.isBlank(version)) {
            throw new IllegalArgumentException("Version number can't be null or " + "blank");
        }
        String[] versionTokens = StringUtils.split(version, '.');
        if (versionTokens.length != 3) {
            throw new IllegalArgumentException("Version number must have three " + "parts");
        }
        this.a = Integer.parseInt(versionTokens[0]);
        this.b = Integer.parseInt(versionTokens[1]);
        this.c = Integer.parseInt(versionTokens[2]);
    }

    /**
   * Gets the main version number
   * @return The main version number
   */
    public int getA() {
        return this.a;
    }

    /**
   * Gets the sub-version number
   * @return The sub-version number
   */
    public int getB() {
        return this.b;
    }

    /**
   * Gets the patch level number
   * @return The patch level number
   */
    public int getC() {
        return this.c;
    }

    /**
   * Compares one version to another
   * @param that The object to which this will be compared
   * @return 1 if the version on which this method was called is greater, 0 if
   *         they are the same or -1 if this version is less than the version in
   *         the parameter.
   */
    @Override
    public int compareTo(DatabaseVersion that) {
        if (that == null) {
            throw new IllegalArgumentException("Can't compare to a null database " + "version");
        }
        if (this.getA() > that.getA()) {
            return 1;
        } else if (this.getA() < that.getA()) {
            return -1;
        }
        if (this.getB() > that.getB()) {
            return 1;
        } else if (this.getB() < that.getB()) {
            return -1;
        }
        if (this.getC() > that.getC()) {
            return 1;
        } else if (this.getC() < that.getC()) {
            return -1;
        }
        return 0;
    }

    /**
   * Determines whether two database versions are equal
   * @param o The object to which this will be compared
   * @return True if the versions are equal, false otherwise
   */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        DatabaseVersion that = (DatabaseVersion) o;
        return new EqualsBuilder().append(this.getC(), that.getC()).append(this.getB(), that.getB()).append(this.getA(), that.getA()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getA()).append(this.getB()).append(this.getC()).toHashCode();
    }
}
