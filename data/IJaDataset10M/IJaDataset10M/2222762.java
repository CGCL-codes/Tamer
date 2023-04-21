package dsb.tks.barkassa.server.persistence.model.verkoopgegevens;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Embeddable
public class VerkoopgegevensPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datum;

    private Integer rekening;

    public Date getDatum() {
        return datum;
    }

    public Integer getRekening() {
        return rekening;
    }

    @Override
    public String toString() {
        final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        builder.append("datum", this.getDatum());
        builder.append("rekening", this.getRekening());
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final VerkoopgegevensPK that = (VerkoopgegevensPK) obj;
        return this.equals(that);
    }

    private boolean equals(VerkoopgegevensPK that) {
        final EqualsBuilder builder = new EqualsBuilder();
        builder.appendSuper(super.equals(that));
        builder.append(this.getDatum(), that.getDatum());
        builder.append(this.getRekening(), that.getRekening());
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        final HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getDatum());
        builder.append(this.getRekening());
        return builder.toHashCode();
    }
}
