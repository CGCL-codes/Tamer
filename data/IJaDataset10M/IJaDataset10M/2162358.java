package delphorm.entite.questionnaire;

public class FiltreTexte implements Filtre {

    private Integer tailleMinimum;

    private Integer tailleMaximum;

    private String expressionReguliere;

    private String messageAide;

    private Boolean facultative;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFacultative() {
        return facultative;
    }

    public void setFacultative(Boolean facultative) {
        this.facultative = facultative;
    }

    public void setMessageAide(String messageAide) {
        this.messageAide = messageAide;
    }

    public String getExpressionReguliere() {
        return expressionReguliere;
    }

    public void setExpressionReguliere(String expressionReguliere) {
        this.expressionReguliere = expressionReguliere;
    }

    public Integer getTailleMaximum() {
        return tailleMaximum;
    }

    public void setTailleMaximum(Integer tailleMaximum) {
        this.tailleMaximum = tailleMaximum;
    }

    public Integer getTailleMinimum() {
        return tailleMinimum;
    }

    public void setTailleMinimum(Integer tailleMinimum) {
        this.tailleMinimum = tailleMinimum;
    }

    public boolean Check(Valeur valeur) {
        ValeurTexte valeurTexte = (ValeurTexte) valeur;
        if ((valeurTexte.getValeur() == null || valeurTexte.getValeur().trim().equals(new String(""))) && facultative.booleanValue() == true) return true;
        if ((valeurTexte.getValeur() == null || valeurTexte.getValeur().trim().equals(new String(""))) && tailleMaximum.intValue() != 0 && facultative.booleanValue() == false) return false;
        if (tailleMaximum != null && tailleMaximum.intValue() < valeurTexte.getValeur().length()) return false;
        if (tailleMinimum != null && tailleMinimum.intValue() > valeurTexte.getValeur().length()) return false;
        if (!valeurTexte.getValeur().matches(expressionReguliere)) return false;
        return true;
    }

    public String getMessageAide() {
        return messageAide;
    }
}
