package no.ugland.utransprod.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

public class County extends BaseObject {

    private String countyNr;

    private String countyName;

    public County(final String aCountyNr, final String aCountyName) {
        super();
        this.countyNr = aCountyNr;
        this.countyName = aCountyName;
    }

    public County() {
        super();
    }

    public final String getCountyName() {
        return countyName;
    }

    public final void setCountyName(final String aCountyName) {
        this.countyName = aCountyName;
    }

    public final String getCountyNr() {
        return countyNr;
    }

    public final void setCountyNr(final String aCountyNr) {
        this.countyNr = aCountyNr;
    }

    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof County)) {
            return false;
        }
        County castOther = (County) other;
        return new EqualsBuilder().append(countyNr, castOther.countyNr).isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder().append(countyNr).toHashCode();
    }

    @Override
    public final String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("countyName", countyName).toString();
    }
}
