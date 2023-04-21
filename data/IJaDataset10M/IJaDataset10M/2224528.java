package gestion;

import carte.Menu;
import carte.MenuPossible;
import carte.Plat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 * La classe réservation permet au client de faire une réservation en ligne ou à
 * l’employé d’enregistrer une commande.
 * @author Aurélien
 */
public class Reservation {

    /**Identifiant de la réservation*/
    private long idReservation;

    /**Nombre de personnes pour la réservation*/
    private int nbPersonne;

    /**Date à laquelle la réservation est établie*/
    private Date dateReservation;

    /**Montant total de la réservation*/
    private double montantTotal;

    /**Le type de la réservation (Permet surtout de différentier si elle provient
     * la plateforme ou non)*/
    private String typeReservation;

    /**Horaire à laquelle la réservation est prévue*/
    private String horaireVoulu;

    /**Statut de la réservation*/
    private String statutReservation;

    private CompteClient compteClient;

    private Set<LigneReservation> setLignesReservation = new HashSet<LigneReservation>();

    private Paiement paiement;

    public Reservation() {
    }

    public Reservation(int nbPersonne, Date dateReservation, String horaireVoulu, CompteClient compteClient, Paiement paiement) {
        this.nbPersonne = nbPersonne;
        this.dateReservation = dateReservation;
        this.horaireVoulu = horaireVoulu;
        this.compteClient = compteClient;
        this.paiement = paiement;
    }

    public Reservation(int nbPersonne, Date dateReservation, String horaireVoulu, CompteClient compteClient) {
        this.nbPersonne = nbPersonne;
        this.dateReservation = dateReservation;
        this.horaireVoulu = horaireVoulu;
        this.compteClient = compteClient;
    }

    /**
     * Méthode qui cherche à remonter des reservations en fonctions de mots clefs
     * donnés.
     * Cette méthode va s'appliquer sur toutes les reservations existantes en bdd.
     * @param tabKeywords
     * @return la liste des reservation satisfaisant à un des mots clefs.
     */
    public static List<Reservation> findReservations(String[] tabKeywords) {
        if (tabKeywords.length == 0) {
            return null;
        } else {
            List<Reservation> reservationsToFind = new ArrayList<Reservation>();
            Session session = HibernateUtil.currentSession();
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("from Reservation");
            List<Reservation> results = query.list();
            for (Reservation reservation : results) {
                session.persist(reservation);
                if (reservation.isSearched(tabKeywords)) {
                    reservationsToFind.add(reservation);
                } else if (reservation.getCompteClient().isSearched(tabKeywords)) {
                    reservationsToFind.add(reservation);
                } else {
                    for (LigneReservation ligneReservation : reservation.getSetLignesReservation()) {
                        System.out.println("l = " + ligneReservation.toString());
                        if (ligneReservation.isSearched(tabKeywords)) {
                            reservationsToFind.add(reservation);
                            break;
                        }
                    }
                }
            }
            tx.commit();
            HibernateUtil.getSessionFactory().close();
            return reservationsToFind;
        }
    }

    /**
     * Méthode qui doit aller chercher l'existence des mots clefs dans les attributs
     * compteClient, dateReservation, horaireVoulu, ligneReservation..
     * @param tabKeyWords
     * @return
     */
    private boolean isSearched(String[] tabKeyWords) {
        for (String keyword : tabKeyWords) {
            if (this.horaireVoulu.equalsIgnoreCase(keyword)) {
                return true;
            } else if (this.dateReservation.toString().equals(keyword)) {
                return true;
            }
        }
        if (this.compteClient.isSearched(tabKeyWords)) {
            return true;
        }
        return false;
    }

    /**
     * Méthode qui créé et ajoute une reservation selon les paramétres donnés.
     * @param date
     * @param heureChoisie
     * @param nbPersonne
     * @param client
     * @return la reservation crée.
     */
    public static Reservation ajouterReservation(Date date, String heureChoisie, int nbPersonne, CompteClient client) {
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        Reservation reservationToInsert = new Reservation(nbPersonne, date, heureChoisie, client);
        session.save(reservationToInsert);
        tx.commit();
        HibernateUtil.getSessionFactory().close();
        return reservationToInsert;
    }

    /**
     * Méthode qui ajoute les lignes de reservation passés en paramètre à la
     * réservation elle aussi passée en paramètre. Attention la reservation
     * existe déjà en base de donnée, il ne s'agit donc pas de recréer une nouvelle
     * réservation. Si ce n'est pas le cas la méthode renvoi false.
     * Ajout des lignes doit mettre à jour le montant total
     * L'objet res passé en paramètre doit être modifié car il doit pouvoir être
     * réutilisé après l'appel à cette fonction.
     * @param res Réservation existante.
     * @param listLR Liste des Lignes de Réservation.
     * @return true si l'ajout se passe bien. false si la reservation res existe déjà.
     */
    public static boolean ajouterLignesReservation(Reservation res, List<LigneReservation> listLR) {
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        session.persist(res);
        Query query = session.createQuery("from Reservation where idReservation=" + res.getIdReservation());
        List<Reservation> result = query.list();
        if (result.isEmpty()) {
            tx.commit();
            HibernateUtil.getSessionFactory().close();
            return false;
        } else {
            Reservation reservation = result.get(0);
            System.out.println(reservation);
            System.out.println(listLR);
            for (LigneReservation LR : listLR) {
                session.persist(LR);
                if (LR.getMenuPossible() != null) {
                    reservation.setLignesReservation.add(LR);
                } else if (LR.getPlat() != null) {
                    reservation.setLignesReservation.add(LR);
                }
            }
            tx.commit();
            HibernateUtil.getSessionFactory().close();
        }
        return true;
    }

    /**
     * Méthode qui ajoute un paiement passé en paramètre à la réservation elle
     * aussi passée en paramètre. Attention la reservation existe déjà en base
     * de données, il ne s'agit donc pas de recréer une nouvelle réservation.
     * Si la reservation n'existe pas la méthode renvoi null;
     * Sinon si tout ce passe bien elle renvoi le Paiement.
     * L'ajout du paiement doit changer le statut de la réservation.
     * @param res - Reservation à laquelle on veut joindre le paiement.
     * @param datePaiement - Date du paiement.
     * @param montantPaiement - Montant du paiement.
     * @param moyenPaiement - Moyen de paiement utilisé.
     * @return null si la réservation 'nexiste pas déjç en bdd sinon on renvoi
     * le paiement effectivement créé.
     */
    public static Paiement ajouterPaiement(Reservation res, Date datePaiement, double montantPaiement, double montantTotal, String moyenPaiement) {
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from Reservation where idReservation=" + res.getIdReservation());
        List<Reservation> result = query.list();
        session.persist(res);
        if (!result.isEmpty()) {
            double solde = montantTotal - montantPaiement;
            Paiement paiementToInsert = new Paiement(datePaiement, montantPaiement, solde, moyenPaiement, res);
            session.save(paiementToInsert);
            res.setPaiement(paiementToInsert);
            session.saveOrUpdate(res);
            tx.commit();
            HibernateUtil.getSessionFactory().close();
            return paiementToInsert;
        } else {
            return null;
        }
    }

    /**
     * Permet de supprimer une liste de  reservations d'un client
     * @param lres: liste de reservations à supprimer
     */
    public static boolean supprimerReservations(List<Reservation> lres) {
        Session session = HibernateUtil.currentSession();
        Transaction tx = session.beginTransaction();
        if (!lres.isEmpty()) {
            for (Reservation reservation : lres) {
                session.persist(reservation);
                Query query2 = session.createQuery("from LigneReservation where Reservation_idReservation=" + reservation.getIdReservation());
                List<LigneReservation> ligneReservationToRemove = query2.list();
                Query query3 = session.createQuery("select p from Paiement p, Reservation r where p.idPaiement = " + reservation.getPaiement().getIdPaiement() + " and r.idReservation=" + reservation.getIdReservation());
                List<Paiement> result = query3.list();
                Paiement paiementToRemove = result.get(0);
                if (!ligneReservationToRemove.isEmpty()) {
                    LigneReservation.supprimerLignesReservation(ligneReservationToRemove);
                }
                session.persist(reservation);
                session.delete(reservation);
                if (paiementToRemove != null) {
                    Paiement.supprimerPaiement(paiementToRemove);
                }
            }
            HibernateUtil.getSessionFactory().close();
            return true;
        } else {
            tx.commit();
            HibernateUtil.getSessionFactory().close();
            return false;
        }
    }

    /**
     * Méthode qui test si l'objet a été paramétré en testant l'attribut horaire
     * voulu.
     * @return
     */
    public boolean isSetted() {
        if (this.horaireVoulu == null) {
            return false;
        } else {
            return true;
        }
    }

    public long getIdReservation() {
        return idReservation;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public String getHoraireVoulu() {
        return horaireVoulu;
    }

    public int getNbPersonne() {
        return nbPersonne;
    }

    public CompteClient getCompteClient() {
        return compteClient;
    }

    public void setCompteClient(CompteClient compteClient) {
        this.compteClient = compteClient;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setHoraireVoulu(String horaireVoulu) {
        this.horaireVoulu = horaireVoulu;
    }

    public void setIdReservation(long idReservation) {
        this.idReservation = idReservation;
    }

    public void setNbPersonne(int nbPersonne) {
        this.nbPersonne = nbPersonne;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Set<LigneReservation> getSetLignesReservation() {
        return setLignesReservation;
    }

    public void setSetLignesReservation(Set<LigneReservation> setLignesReservation) {
        this.setLignesReservation = setLignesReservation;
    }

    public String getStatutReservation() {
        return statutReservation;
    }

    public void setStatutReservation(String statutReservation) {
        this.statutReservation = statutReservation;
    }

    public String getTypeReservation() {
        return typeReservation;
    }

    public void setTypeReservation(String typeReservation) {
        this.typeReservation = typeReservation;
    }

    /**
     * Override toString pour l'affichage des Attributs d'un menu. Peut être amener à être modifier si nécessaire.
     *
     */
    @Override
    public String toString() {
        return ("idReservation: " + idReservation + "\n" + "nbPersonnes: " + nbPersonne + "\n" + "dateReservation: " + dateReservation + "\n" + "montantTotal: " + montantTotal + "€\n" + "typeReservation: " + typeReservation + "\n" + "horaireVoulu: " + horaireVoulu + "\n" + "statutReservation: " + statutReservation + "\n" + "idClient: " + compteClient.getIdClient() + "\n" + "LignesReservation: \n" + setLignesReservation.toString() + "\n");
    }
}
