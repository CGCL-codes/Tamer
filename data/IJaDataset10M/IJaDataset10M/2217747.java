package pf.alaudes.mastermahjong.ihm.util.local;

import java.util.List;
import pf.alaudes.mastermahjong.metier.impl.Combinaison;
import pf.alaudes.mastermahjong.metier.impl.Joueur;
import pf.alaudes.mastermahjong.metier.impl.Score;
import pf.alaudes.mastermahjong.metier.impl.Tour;

public class LocalizedTour extends AbstractLocalizedObject {

    private Tour tour;

    @Override
    protected void setObject(Object object) {
        this.tour = (Tour) object;
    }

    public void ajouter(Combinaison combinaison) {
        tour.ajouter(combinaison);
    }

    public void calculerPoints(List<Joueur> joueurs) {
        tour.calculerPoints(joueurs);
    }

    public boolean equals(Object object) {
        return tour.equals(object);
    }

    public List<Combinaison> getCombinaisons() {
        return tour.getCombinaisons();
    }

    public Joueur getDonneur() {
        return tour.getDonneur();
    }

    public Joueur getGagnant() {
        return tour.getGagnant();
    }

    public Long getIdentifiantObjet() {
        return tour.getIdentifiantObjet();
    }

    public String getIdGagnant() {
        return tour.getIdGagnant();
    }

    public String getIdDonneur() {
        return tour.getIdDonneur();
    }

    public int getPoint(Joueur joueur) {
        return tour.getPoint(joueur);
    }

    public int getPointGagnant(Joueur joueur) {
        return tour.getPointGagnant(joueur);
    }

    public int getPointPerdant(Joueur joueur) {
        return tour.getPointPerdant(joueur);
    }

    public int getPointSansFleur() {
        return tour.getPointSansFleur();
    }

    public int getPointFleur() {
        return tour.getPointFleur();
    }

    public int getPointCombinaison() {
        return tour.getPointCombinaison();
    }

    public String getCombinaisonGagnante() {
        return getLibelleCombinaisonLocale(tour.getCombinaisonGagnante());
    }

    public List<Score> getScores() {
        return tour.getScores();
    }

    public Long getVersion() {
        return tour.getVersion();
    }

    public int hashCode() {
        return tour.hashCode();
    }

    public void setCombinaisons(List<Combinaison> combinaisons) {
        tour.setCombinaisons(combinaisons);
    }

    public void setDonneur(Joueur donneur) {
        tour.setDonneur(donneur);
    }

    public void setGagnant(Joueur gagnant) {
        tour.setGagnant(gagnant);
    }

    public void setIdentifiantObjet(Long identifiantObjet) {
        tour.setIdentifiantObjet(identifiantObjet);
    }

    public void setScores(List<Score> scores) {
        tour.setScores(scores);
    }

    public void setVersion(Long version) {
        tour.setVersion(version);
    }

    public void supprimer(Combinaison combinaison) {
        tour.supprimer(combinaison);
    }

    public String toString() {
        return tour.toString();
    }
}
