package org.pcorp.space.metier.domaine;

import java.util.List;

public class Cmd {

    private String nom;

    private String rang;

    private Integer level;

    private String reputation;

    private String race;

    private int id;

    private String dla;

    private int xp;

    private int nbrmort;

    private int xpused;

    private List<Caracteristique> listeCarac;

    private Station resurect;

    private int ordre;

    private int maxOrdre;

    private int tresor;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getRang() {
        return rang;
    }

    public void setRang(String rang) {
        this.rang = rang;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDla() {
        return dla;
    }

    public void setDla(String dla) {
        this.dla = dla;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getNbrmort() {
        return nbrmort;
    }

    public void setNbrmort(int nbrmort) {
        this.nbrmort = nbrmort;
    }

    public int getXpused() {
        return xpused;
    }

    public void setXpused(int xpused) {
        this.xpused = xpused;
    }

    public List<Caracteristique> getListeCarac() {
        return listeCarac;
    }

    public void setListeCarac(List<Caracteristique> listeCarac) {
        this.listeCarac = listeCarac;
    }

    public Station getResurect() {
        return resurect;
    }

    public void setResurect(Station resurect) {
        this.resurect = resurect;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public int getMaxOrdre() {
        return maxOrdre;
    }

    public void setMaxOrdre(int maxOrdre) {
        this.maxOrdre = maxOrdre;
    }

    public int getTresor() {
        return tresor;
    }

    public void setTresor(int tresor) {
        this.tresor = tresor;
    }
}
