package net.comensus.gh.core.entity.ext;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author faraya
 */
@Entity
public class SysDate implements Serializable {

    private Date date;

    @Id
    @Column(name = "DATE_VALUE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     * the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
