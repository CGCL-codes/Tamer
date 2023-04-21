package uk.ac.ebi.interpro.scan.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;
import java.util.Set;

/**
 * HMMER2 match.
 *
 * @author Antony Quinn
 * @version $Id: Hmmer2Match.java 952 2011-02-03 16:43:25Z philip.j.r.jones $
 * @since 1.0
 */
@Entity
@Table(name = "hmmer2_match")
@XmlType(name = "Hmmer2MatchType")
public class Hmmer2Match extends HmmerMatch<Hmmer2Match.Hmmer2Location> {

    protected Hmmer2Match() {
    }

    public Hmmer2Match(Signature signature, double score, double evalue, Set<Hmmer2Match.Hmmer2Location> locations) {
        super(signature, score, evalue, locations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hmmer2Match)) return false;
        final Hmmer2Match m = (Hmmer2Match) o;
        return new EqualsBuilder().appendSuper(super.equals(o)).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(39, 49).appendSuper(super.hashCode()).toHashCode();
    }

    /**
     * Location(s) of match on protein sequence
     *
     * @author Antony Quinn
     */
    @Entity
    @Table(name = "hmmer2_location")
    @XmlType(name = "Hmmer2LocationType")
    public static class Hmmer2Location extends HmmerLocation {

        /**
         * protected no-arg constructor required by JPA - DO NOT USE DIRECTLY.
         */
        protected Hmmer2Location() {
        }

        public Hmmer2Location(int start, int end, double score, double evalue, int hmmStart, int hmmEnd, HmmBounds hmmBounds) {
            super(start, end, score, evalue, hmmStart, hmmEnd, hmmBounds);
        }

        public Hmmer2Location(int start, int end, double score, double evalue, int hmmStart, int hmmEnd, int hmmLength) {
            super(start, end, score, evalue, hmmStart, hmmEnd, hmmLength);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof HmmerLocation)) return false;
            final HmmerLocation h = (HmmerLocation) o;
            return new EqualsBuilder().appendSuper(super.equals(o)).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(39, 53).appendSuper(super.hashCode()).toHashCode();
        }
    }
}
