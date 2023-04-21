package de.mediumster.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import de.mediumster.model.hibernate.ExemplarPK;

/**
 * Diese Klasse definiert ein physikalisches Exemplare eines Mediums. Jedes Exemplar ist mit einem
 * {@link Medium} verkn�pft.
 * 
 * @see Medium
 * @author Hannes Rempel
 * @author Sebastian Gerken
 */
@SuppressWarnings("serial")
@Entity(name = "exemplars")
@IdClass(ExemplarPK.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Mediumster")
public class Exemplar implements Serializable, Comparable<Exemplar> {

    /**
	 * Exemplarnummer, die f�r jedes Medium eindeutig ist
	 */
    @Id
    private int exemplarId;

    @Id
    private Medium medium;

    /**
	 * Entleihdatum, wenn das Exemplar ausgeliehen ist; ansonsten {@code null}
	 */
    private Date rentalDate;

    /**
	 * Entleiher, wenn das Exemplar ausgeliehen ist; ansonsten {@code null}
	 */
    @ManyToOne
    @PrimaryKeyJoinColumn
    private User user;

    /**
	 * Der Konstruktor liefert ein Exemplar zur�ck.
	 */
    @SuppressWarnings("unused")
    private Exemplar() {
    }

    /**
	 * Der Konstruktor liefert ein Exemplar zur�ck.
	 */
    Exemplar(Medium medium, int exemplarId) {
        this.medium = medium;
        this.exemplarId = exemplarId;
    }

    /**
	 * Diese Methode vergleicht dieses Exemplar mit dem �bergebenen Exemplar um sie sortieren zu
	 * k�nnen. Die Exemplare werden erst nach der Mediumnummer und dann nach der Exemplarnummer
	 * sortiert.
	 * 
	 * @param exemplar
	 *            Vergleichsexemplar
	 * @return ein negativer Wert, wenn dieses Exemplar kleiner ist als das �bergebene Exemplar ist;
	 *         der Wert 0, wenn beide Exemplare gleich sind; ein positiver Wert, wenn dieses
	 *         Exemplar gr��er ist
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(final Exemplar exemplar) {
        if (getMedium().getMediumId() == exemplar.getMedium().getMediumId()) {
            return getExemplarId() - exemplar.getExemplarId();
        } else {
            return getMedium().getMediumId() - exemplar.getMedium().getMediumId();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Exemplar other = (Exemplar) obj;
        if (exemplarId != other.exemplarId) {
            return false;
        }
        if (medium == null) {
            if (other.medium != null) {
                return false;
            }
        } else if (!medium.equals(other.medium)) {
            return false;
        }
        return true;
    }

    /**
	 * Diese Methode gibt die Exemplarnummer zur�ck.
	 * 
	 * @return Exemplarnummer
	 */
    public int getExemplarId() {
        return exemplarId;
    }

    /**
	 * Diese Methode gibt das verkn�pfte Medium zur�ck.
	 * 
	 * @return Medium
	 */
    public Medium getMedium() {
        return medium;
    }

    /**
	 * Diese Methode gibt das Ausleihdatum zur�ck.
	 * 
	 * @return Entleihdatum
	 */
    public Date getRentalDate() {
        return rentalDate;
    }

    /**
	 * Diese Methode gibt das R�ckgabedatum zur�ck.
	 * 
	 * @return R�ckgabedatum
	 */
    public Date getReturnDate() {
        final Calendar c = Calendar.getInstance();
        c.setTime(rentalDate);
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 14);
        return c.getTime();
    }

    /**
	 * Diese Methode gibt den Entleiher zur�ck.
	 * 
	 * @return Entleiher
	 */
    public User getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + exemplarId;
        result = prime * result + (medium == null ? 0 : medium.hashCode());
        return result;
    }

    /**
	 * Diese Methode �berpr�ft, ob das Exemplar verliehen werden kann.
	 * 
	 * @return {@code}, wenn das Exemplar verf�gbar ist
	 */
    public boolean isAvailable() {
        return user == null;
    }

    /**
	 * Diese Mehtode verleiht das Exemplar an einen Benutzer.
	 * 
	 * @param user
	 *            Entleiher
	 * @return {@code}, wenn das Exemplar an den Benutzer verliehen wurde
	 */
    public boolean startLoan(User user) {
        if (user != null && isAvailable()) {
            user.getExemplars().add(this);
            this.user = user;
            rentalDate = new Date();
            return true;
        }
        return false;
    }

    /**
	 * Diese Methode beendet die Ausleihe.
	 */
    public void stopLoan() {
        if (user != null) {
            user.getExemplars().remove(this);
            user = null;
            rentalDate = null;
        }
    }

    @Override
    public String toString() {
        return medium + " (Exemplar " + exemplarId + ")";
    }
}
