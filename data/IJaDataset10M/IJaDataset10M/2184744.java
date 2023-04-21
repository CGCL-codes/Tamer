package job;

import java.util.Stack;

/**
* Cette classe repr�sente notre terrain de jeu, c'est � dire l'endroit o� les pi�ces s'accumulent
* @author Flav
*/
public class PlayGround {

    /** Nous représentons notre terrain de jeu par une pile de ligne, chaque ligne pleine est d�pil�e, puis une ligne vide est ajout�e en bas de la pile **/
    protected Stack<Row> rows;

    /** Hauteur de notre terrain de jeu, cela repr�sente donc le nombre de lignes contenues par le terrain **/
    private int height;

    /** Largeur de notre terrain, c'est donc le nombre de blocks contenus par chaque ligne de notre terrain **/
    private int width;

    /**
	* Constructeur
	*/
    public PlayGround(int w, int h) {
        this.rows = new Stack<Row>();
        this.height = h;
        this.width = w;
    }

    /** Ajoute un ligne */
    public void addRow(Row r) {
        rows.add(r);
    }

    /** supprime la ieme ligne
	 * @param v la position verticale de la ligne
	 **/
    public void removeRow(int v) {
        rows.remove(v);
    }

    /** Test la rotation a gauche de la piece */
    public boolean testRotateLeft(FallingPiece fp) {
        RotPiece np = fp.getCurRot().getNextRot();
        for (int xPiece = 0; xPiece < 4; xPiece++) {
            for (int yPiece = 0; yPiece < 4; yPiece++) {
                if (!np.isEmpty(xPiece, yPiece)) if (fp.getHPos() + xPiece > getWidth() - 1 || fp.getHPos() + xPiece < 0) return false;
                if (rows.size() - 1 < fp.getVPos() + yPiece) continue;
                if (!np.isEmpty(xPiece, yPiece)) {
                    if (!rows.get(fp.getVPos() + yPiece).isEmpty(fp.getHPos() + xPiece)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Test si la piece peut aller à droite */
    public boolean testRight(FallingPiece fp) {
        for (int xPiece = 0; xPiece < 4; xPiece++) {
            for (int yPiece = 0; yPiece < 4; yPiece++) {
                if (!fp.isEmpty(xPiece, yPiece)) if (fp.getHPos() + xPiece >= getWidth() - 1) return false;
                if (rows.size() - 1 < fp.getVPos() + yPiece) continue;
                if (!fp.isEmpty(xPiece, yPiece)) {
                    if (!rows.get(fp.getVPos() + yPiece).isEmpty(fp.getHPos() + xPiece + 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Test si la piece peut aller à gauche */
    public boolean testLeft(FallingPiece fp) {
        for (int xPiece = 0; xPiece < 4; xPiece++) {
            for (int yPiece = 0; yPiece < 4; yPiece++) {
                if (!fp.isEmpty(xPiece, yPiece)) if (fp.getHPos() + xPiece <= 0) return false;
                if (rows.size() - 1 < fp.getVPos() + yPiece) continue;
                if (!fp.isEmpty(xPiece, yPiece)) {
                    if (!rows.get(fp.getVPos() + yPiece).isEmpty(fp.getHPos() + xPiece - 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Test si la piece peut aller en bas */
    public boolean testDown(FallingPiece fp) {
        if (fp.getVPos() == 0) {
            makeRowsWithPiece(fp);
            return false;
        }
        if (rows.size() < fp.getVPos()) return true;
        for (int xPiece = 0; xPiece < 4; xPiece++) {
            for (int yPiece = 0; yPiece < 4; yPiece++) {
                if (rows.size() < fp.getVPos() + yPiece) break;
                if (fp.isEmpty(xPiece, yPiece) == false) {
                    if (rows.get(fp.getVPos() + yPiece - 1).isEmpty(fp.getHPos() + xPiece) == false) {
                        makeRowsWithPiece(fp);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** cette methode permet de creer des lignes ou ajouter des  
	 * blocks dans une ligne en fonction de la position 
	 * d'une piece passé en argument (la piece qui tombe) */
    private void makeRowsWithPiece(FallingPiece fp) {
        for (int yPiece = 0; yPiece < 4; yPiece++) {
            for (int xPiece = 0; xPiece < 4; xPiece++) {
                if (fp.isEmpty(xPiece, yPiece) == false) {
                    if (rows.size() > yPiece + fp.getVPos()) rows.get(yPiece + fp.getVPos()).addBlock(xPiece + fp.getHPos(), fp.getBlock(xPiece, yPiece).getColor()); else {
                        rows.add(new Row());
                        rows.get(yPiece + fp.getVPos()).addBlock(xPiece + fp.getHPos(), fp.getBlock(xPiece, yPiece).getColor());
                    }
                }
            }
        }
    }

    /** Supprime les lignes pleines */
    public int deleteCompleteRows() {
        int nbLines = 0;
        for (int r = 0; r < rows.size(); r++) {
            boolean isComplete = true;
            for (int x = 0; x < getWidth(); x++) {
                if (rows.get(r).isEmpty(x)) {
                    isComplete = false;
                }
            }
            if (isComplete) {
                this.removeRow(r);
                r--;
                nbLines++;
            }
        }
        return nbLines;
    }

    /**
	* Retourne la valeur de hight
	* @return the hight
	*/
    public int getHeight() {
        return this.height;
    }

    /**
	* Retourne la valeur de width
	* @return the width
	*/
    public int getWidth() {
        return this.width;
    }

    /** Y a t'il un dépassement de la pile de ligne sur le terrain? */
    public boolean overflow() {
        if (rows.size() >= height) return true;
        return false;
    }
}
