package dev.cinema.ejb.entity.film;

import dev.cinema.ejb.entity.client.ReservationBean;
import dev.cinema.ejb.entity.hall.HallBean;
import dev.cinema.ejb.entity.hall.SeatStateBean;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author brushtyler
 */
@Entity
@Table(name = "Projection")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Projection.findAll", query = "SELECT p FROM ProjectionBean p ORDER BY p.date"), @NamedQuery(name = "Projection.findByRange", query = "SELECT p FROM ProjectionBean p WHERE p.date BETWEEN :firstDay AND :lastDay ORDER BY p.date"), @NamedQuery(name = "Projection.findProjectionDays", query = "SELECT p FROM ProjectionBean p WHERE p.idFilm.idFilm = :idFilm AND p.date BETWEEN :firstDay AND :lastDay ORDER BY p.date") })
public class ProjectionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProjection", nullable = false, insertable = false)
    private Integer idProjection;

    @Basic(optional = false)
    @NotNull
    @Column(name = "date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @JoinColumn(name = "idHall", referencedColumnName = "idHall", nullable = false)
    @ManyToOne(optional = false, targetEntity = HallBean.class)
    private HallBean idHall;

    @JoinColumn(name = "idFilm", referencedColumnName = "idFilm", nullable = false)
    @ManyToOne(optional = false, targetEntity = FilmBean.class)
    private FilmBean idFilm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProjection", targetEntity = ReservationBean.class)
    private Collection<ReservationBean> reservationCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProjection", targetEntity = SeatStateBean.class)
    private Collection<SeatStateBean> seatStateCollection;

    public ProjectionBean() {
    }

    public ProjectionBean(Integer idProjection) {
        this.idProjection = idProjection;
    }

    public ProjectionBean(Date date) {
        this.date = date;
    }

    public Integer getIdProjection() {
        return idProjection;
    }

    public void setIdProjection(Integer idProjection) {
        this.idProjection = idProjection;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HallBean getIdHall() {
        return idHall;
    }

    public void setIdHall(HallBean idHall) {
        this.idHall = idHall;
    }

    public void setHall(HallBean idHall) {
        if (this.idHall == idHall) {
            return;
        }
        if (this.idHall != null) {
            this.idHall.getProjectionCollection().remove(this);
        }
        this.idHall = idHall;
        if (this.idHall != null) {
            this.idHall.getProjectionCollection().add(this);
        }
    }

    public FilmBean getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(FilmBean idFilm) {
        this.idFilm = idFilm;
    }

    public void setFilm(FilmBean idFilm) {
        if (this.idFilm == idFilm) {
            return;
        }
        if (this.idFilm != null) {
            this.idFilm.getProjectionCollection().remove(this);
        }
        this.idFilm = idFilm;
        if (this.idFilm != null) {
            this.idFilm.getProjectionCollection().add(this);
        }
    }

    @XmlTransient
    public Collection<ReservationBean> getReservationCollection() {
        return reservationCollection;
    }

    public void setReservationCollection(Collection<ReservationBean> reservationCollection) {
        this.reservationCollection = reservationCollection;
    }

    public void addReservation(ReservationBean o) {
        if (!reservationCollection.contains(o)) {
            reservationCollection.add(o);
            o.setIdProjection(this);
        }
    }

    public void removeReservation(ReservationBean o) {
        reservationCollection.remove(o);
        if (o.getIdProjection() == this) {
            o.setIdProjection(null);
        }
    }

    public Collection<SeatStateBean> getSeatStateCollection() {
        return seatStateCollection;
    }

    public void setSeatStateCollection(Collection<SeatStateBean> seatStateCollection) {
        this.seatStateCollection = seatStateCollection;
    }

    public void addSeatState(SeatStateBean o) {
        if (!seatStateCollection.contains(o)) {
            seatStateCollection.add(o);
            o.setIdProjection(this);
        }
    }

    public void removeSeatState(SeatStateBean o) {
        seatStateCollection.remove(o);
        if (o.getIdProjection() == this) {
            o.setIdProjection(null);
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProjection != null ? idProjection.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProjectionBean)) {
            return false;
        }
        ProjectionBean other = (ProjectionBean) object;
        if ((this.idProjection == null && other.idProjection != null) || (this.idProjection != null && !this.idProjection.equals(other.idProjection))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "film.ProjectionBean[ idProjection=" + idProjection + " ]";
    }
}
