package bltt.game;

public class Carte implements Constant {

    private int couleur;

    private int figure;

    public Carte() {
        this.couleur = 0;
        this.figure = 0;
    }

    public Carte(int couleur, int figure) {
        this.couleur = couleur;
        this.figure = figure;
    }

    public boolean isValid() {
        return ((couleur != 0) && (figure != 0));
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
    }

    public int getValeur(int couleuratout) {
        int result = 0;
        if (this.couleur == couleuratout) result += 0; else result += 0;
        return result;
    }

    public String toString() {
        String result = "Carte : ";
        result += figures[this.figure] + " " + couleurs[this.couleur];
        return result;
    }
}
