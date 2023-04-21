package de.rowbuddy.entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import de.rowbuddy.exceptions.RowBuddyException;

/**
 * Entity implementation class for Entity: Boat
 * 
 */
@Entity
public class Boat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;

    private String name = "";

    private int numberOfSeats = 0;

    private boolean coxed = false;

    @OneToMany
    private List<BoatDamage> boatDamages = new LinkedList<BoatDamage>();

    @OneToMany
    private List<BoatReservation> boatReservations = new LinkedList<BoatReservation>();

    private boolean locked = false;

    private boolean deleted = false;

    private static final long serialVersionUID = 1L;

    public Boat() {
        super();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) throws RowBuddyException {
        if (name == null) {
            throw new NullPointerException("Name darf nicht null sein");
        }
        if (name.isEmpty()) {
            throw new RowBuddyException("Name darf nicht leer sind");
        }
        this.name = name;
    }

    public int getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) throws RowBuddyException {
        if (numberOfSeats <= 0) {
            throw new RowBuddyException("Ein Boot muss mindestens einen Platz haben");
        }
        this.numberOfSeats = numberOfSeats;
    }

    public boolean isCoxed() {
        return this.coxed;
    }

    public void setCoxed(boolean coxed) {
        this.coxed = coxed;
    }

    public List<BoatDamage> getBoatDamages() {
        return boatDamages;
    }

    public void setBoatDamages(List<BoatDamage> boatDamages) {
        if (boatDamages == null) {
            throw new NullPointerException("Bootschaden darf nicht null sein");
        }
        this.boatDamages = boatDamages;
    }

    public void addBoatDamage(BoatDamage boatDamage) throws RowBuddyException {
        if (boatDamage == null) {
            throw new NullPointerException("Bootsschaden darf nicht null sein");
        }
        if (boatDamages.contains(boatDamage)) {
            throw new RowBuddyException("Der Schaden ist bereits registriert");
        }
        boatDamages.add(boatDamage);
        boatDamage.setBoat(this);
    }

    public void removeBoatDamage(BoatDamage boatDamage) throws RowBuddyException {
        if (boatDamage == null) {
            throw new NullPointerException("Bootsschaden darf nicht null sein");
        }
        if (!boatDamages.contains(boatDamage)) {
            throw new RowBuddyException("Bootsschaden gehoert nicht zu diesem Boot");
        }
        boatDamages.remove(boatDamage);
        boatDamage.removeBoat();
    }

    public List<BoatReservation> getBoatReservations() {
        return boatReservations;
    }

    public void addBoatReservation(BoatReservation boatReservation) {
        if (boatReservation == null) {
            throw new NullPointerException("Reservierung darf nicht null sein");
        }
        boatReservations.add(boatReservation);
    }

    public void setBoatReservations(List<BoatReservation> boatReservations) throws RowBuddyException {
        if (boatReservations == null) {
            throw new NullPointerException("Reservierung darf nicht null sein");
        }
        this.boatReservations = boatReservations;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void validate() throws RowBuddyException {
        if (name.isEmpty()) {
            throw new RowBuddyException("Der Name muss gesetzt sein");
        }
        if (numberOfSeats <= 0) {
            throw new RowBuddyException("Das Boot muss mindestens einen Sitzplatz haben");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Boat other = (Boat) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
