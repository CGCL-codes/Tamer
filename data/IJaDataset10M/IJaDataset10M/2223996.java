package entity;

import java.util.Collection;
import util.DateHelper;

public class Reservation {

    private long id;

    private DateHelper dateReservation;

    private Client client;

    private Collection<Voyageur> listeVoyageur;

    private Collection<Produit> listeProduit;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateHelper getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(DateHelper dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Collection<Voyageur> getListeVoyageur() {
        return listeVoyageur;
    }

    public void setListeVoyageur(Collection<Voyageur> listeVoyageur) {
        this.listeVoyageur = listeVoyageur;
    }

    public Collection<Produit> getListeProduit() {
        return listeProduit;
    }

    public void setListeProduit(Collection<Produit> listeProduit) {
        this.listeProduit = listeProduit;
    }

    public Reservation(Client client, DateHelper dateReservation) {
        super();
        this.client = client;
        this.dateReservation = dateReservation;
    }

    public Reservation() {
        super();
    }
}
