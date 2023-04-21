package entity;

import java.util.List;
import javax.persistence.*;

@Entity
public class Module {

    private String nom;

    private String sigle;

    private Option option;

    private int semestre;

    private int promotion;

    private Domaine domaine;

    private String syllabusPF;

    private String syllabusGF;

    private String syllabusPA;

    private String syllabusGA;

    private int creditsECTS;

    private boolean estAffecte;

    private int id;

    private List<Theorie> theories;

    private List<Projet> projets;

    private boolean isLocked = false;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    @OneToMany
    public List<Projet> getProjets() {
        return projets;
    }

    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }

    @OneToMany
    public List<Theorie> getTheories() {
        return theories;
    }

    public void setTheories(List<Theorie> theories) {
        this.theories = theories;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne
    public Domaine getDomaine() {
        return domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @OneToOne
    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getSyllabusGA() {
        return syllabusGA;
    }

    public void setSyllabusGA(String syllabusGA) {
        this.syllabusGA = syllabusGA;
    }

    public String getSyllabusGF() {
        return syllabusGF;
    }

    public void setSyllabusGF(String syllabusGF) {
        this.syllabusGF = syllabusGF;
    }

    public String getSyllabusPA() {
        return syllabusPA;
    }

    public void setSyllabusPA(String syllabusPA) {
        this.syllabusPA = syllabusPA;
    }

    public String getSyllabusPF() {
        return syllabusPF;
    }

    public void setSyllabusPF(String syllabusPF) {
        this.syllabusPF = syllabusPF;
    }

    public Module() {
        super();
    }

    public int getCreditsECTS() {
        return creditsECTS;
    }

    public void setCreditsECTS(int creditsECTS) {
        this.creditsECTS = creditsECTS;
    }

    public boolean isEstAffecte() {
        return estAffecte;
    }

    public void setEstAffecte(boolean estAffecte) {
        this.estAffecte = estAffecte;
    }
}
