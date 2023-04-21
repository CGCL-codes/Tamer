package dev.cinema.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author brushtyler
 */
public class Projection implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idProjection;

    private Date date;

    private int idHall;

    private String hallName;

    private int idFilm;

    private String title;

    public Projection() {
    }

    public Projection(Date date, int idHall, int idFilm) {
        this.date = date;
        this.idFilm = idFilm;
        this.idHall = idHall;
    }

    public Projection(Date date, int idHall, int idFilm, String hallName, String title) {
        this.date = date;
        this.idFilm = idFilm;
        this.idHall = idHall;
        this.hallName = hallName;
        this.title = title;
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

    public int getIdHall() {
        return idHall;
    }

    public void setIdHall(int idHall) {
        this.idHall = idHall;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        this.idFilm = idFilm;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    public String getDateToString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProjection != null ? idProjection.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Projection)) {
            return false;
        }
        Projection other = (Projection) object;
        if ((this.idProjection == null && other.idProjection != null) || (this.idProjection != null && !this.idProjection.equals(other.idProjection))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "model.Projection[ idProjection=" + idProjection + " ]";
    }
}
