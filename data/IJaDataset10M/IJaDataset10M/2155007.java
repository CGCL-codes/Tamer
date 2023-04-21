package modele.articles;

public abstract class Element {

    protected String valeur;

    protected int offsetDebut;

    protected int taille;

    private Element elementSuivant;

    private Element elementPrecedent;

    public static final String TYPE_MOT = "mot";

    public static final String TYPE_PONCTUATION = "ponctuation";

    public Element(String valeur, Element eltPrecedent, Element eltSuivant) {
        this.elementPrecedent = eltPrecedent;
        this.setElementSuivant(eltSuivant);
        this.setValeur(valeur);
        this.taille = valeur.length();
        if (this.elementPrecedent == null) this.offsetDebut = 0; else this.offsetDebut = this.elementPrecedent.getOffsetDebut() + this.elementPrecedent.getTaille();
    }

    public int getOffsetDebut() {
        return this.offsetDebut;
    }

    public void setOffsetDebut(int offs) {
        this.offsetDebut = offs;
    }

    public int getTaille() {
        return this.taille;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
        this.taille = valeur.length();
    }

    public String getValeur() {
        return valeur;
    }

    public abstract String getTypeElement();

    public void setElementSuivant(Element elementSuivant) {
        this.elementSuivant = elementSuivant;
    }

    public Element getElementSuivant() {
        return elementSuivant;
    }

    public void setElementPrecedent(Element elementPrecedent) {
        this.elementPrecedent = elementPrecedent;
    }

    @Override
    public String toString() {
        return String.valueOf(this.offsetDebut) + " : " + this.valeur;
    }

    public Element getElementPrecedent() {
        return elementPrecedent;
    }
}
