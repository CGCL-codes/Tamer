package web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import dto.HebergementVO;
import dto.LieuVO;
import dto.TrajetVO;
import dto.VoyageVO;

public class IndexBean {

    private boolean volChecked = true;

    private boolean hotelChecked = false;

    private String depart;

    private String arrivee;

    private Date dateDepart;

    private Date dateArrivee;

    private List<VoyageVO> listeVoyages;

    private Collection<TrajetVO> listeTrajets;

    private Collection<HebergementVO> listeHebergements;

    private long idVoyage;

    private VoyageVO currentVoyage;

    private int nbVoyageurs = 1;

    private String message;

    private GestionVente gestionVente;

    private CoordAchat coordAchat;

    public String doRecupVoyagesAll() {
        String suite = null;
        this.idVoyage = 0;
        this.listeTrajets = null;
        this.listeHebergements = null;
        this.message = "";
        try {
            this.listeVoyages = gestionVente.getAllVoyages();
            this.listeTrajets = null;
            suite = "ok";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suite;
    }

    public String detailsVoyage() {
        String suite = null;
        this.message = "";
        System.out.println("idVoyage : " + idVoyage);
        if (idVoyage == 0) return suite;
        currentVoyage = gestionVente.getVoyageById(idVoyage);
        listeTrajets = currentVoyage.getListeTrajet();
        listeHebergements = currentVoyage.getListeHebergement();
        suite = "ok";
        return suite;
    }

    public String rechercheTrajets() {
        System.out.println("Recherche un vol FROM=" + depart + " TO=" + arrivee);
        this.message = "";
        this.idVoyage = 0;
        this.listeVoyages = null;
        this.listeHebergements = null;
        this.listeTrajets = gestionVente.getTrajets(depart, arrivee, dateDepart, dateArrivee);
        return "ok";
    }

    public String doReservation() {
        coordAchat.ajouterDansCaddy(idVoyage, nbVoyageurs);
        this.message = "Produit de ref [" + idVoyage + "]x" + nbVoyageurs + "ex. bien ajouté.";
        return "caddy";
    }

    public Collection<VoyageVO> getListeVoyages() {
        return listeVoyages;
    }

    public Collection<TrajetVO> getListeTrajets() {
        return listeTrajets;
    }

    public String getMessage() {
        return message;
    }

    public void setGestionVente(GestionVente gestionVente) {
        this.gestionVente = gestionVente;
    }

    public VoyageVO getCurrentVoyage() {
        return currentVoyage;
    }

    public void setCoordAchat(CoordAchat coordAchat) {
        this.coordAchat = coordAchat;
    }

    public Collection<SelectItem> getDepartList() {
        List<SelectItem> lieux = new ArrayList<SelectItem>();
        for (LieuVO lieu : gestionVente.getAllLieux()) lieux.add(new SelectItem(lieu.getLieu(), lieu.getLieu()));
        return lieux;
    }

    public Collection<SelectItem> getArriveeList() {
        List<SelectItem> lieux = new ArrayList<SelectItem>();
        for (LieuVO lieu : gestionVente.getAllLieux()) lieux.add(new SelectItem(lieu.getLieu(), lieu.getLieu()));
        return lieux;
    }

    public boolean isVolChecked() {
        return volChecked;
    }

    public void setVolChecked(boolean volChecked) {
        this.volChecked = volChecked;
    }

    public boolean isHotelChecked() {
        return hotelChecked;
    }

    public void setHotelChecked(boolean hotelChecked) {
        this.hotelChecked = hotelChecked;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public void setIdVoyage(long idVoyage) {
        System.out.println("(dans setIdVoyage)");
        this.idVoyage = idVoyage;
    }

    public long getIdVoyage() {
        System.out.println("(dans getIdVoyage)");
        return idVoyage;
    }

    public void setNbVoyageurs(int nbVoyageurs) {
        this.nbVoyageurs = nbVoyageurs;
    }

    public int getNbVoyageurs() {
        return nbVoyageurs;
    }

    public Collection<HebergementVO> getListeHebergements() {
        return listeHebergements;
    }
}
