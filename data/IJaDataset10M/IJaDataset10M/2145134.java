package net.sourceforge.traffiscope.entity.jpa;

import net.sourceforge.traffiscope.entity.DayEntity;
import net.sourceforge.traffiscope.model.DayTO;
import net.sourceforge.traffiscope.model.ProfileType;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamedQueries({ @NamedQuery(name = Day.QUERY_LIST, query = "SELECT o FROM Day o ORDER BY o.date") })
@Entity
public class Day extends AbstractEntity<DayTO> implements DayEntity {

    static final String ENTITY_NAME = "Day";

    static final String QUERY_LIST = AbstractIdEntity.PREFIX_LIST + ENTITY_NAME;

    private static final long serialVersionUID = -5690730360616487318L;

    private Date _date;

    private ProfileType _profile;

    public Day() {
        _profile = ProfileType.UNKNOWN;
    }

    @Id
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return _date;
    }

    public void setDate(Date value) {
        _date = value;
    }

    @Enumerated(EnumType.STRING)
    public ProfileType getProfile() {
        return _profile;
    }

    public void setProfile(ProfileType value) {
        _profile = value;
    }

    @Override
    public DayTO buildTO() {
        DayTO to = new DayTO();
        writeTO(to);
        return to;
    }

    @Override
    public void readTO(DayTO to) {
        setProfile(to.getProfile());
    }

    @Override
    public void writeTO(DayTO to) {
        to.setProfile(getProfile());
        to.setDate(getDate());
    }
}
