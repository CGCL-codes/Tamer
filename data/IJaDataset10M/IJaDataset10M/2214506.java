package outils.ihm;

import java.awt.Cursor;
import javax.swing.JComponent;

public class OutilsCurseur {

    public static enum TypeCurseur {

        WAIT
    }

    private JComponent composant;

    private Cursor cursor_precedent;

    private Cursor cursor_suivant;

    public OutilsCurseur(JComponent composant, TypeCurseur typeCurseur) {
        this.composant = composant;
        this.cursor_precedent = composant.getCursor();
        if (typeCurseur.equals(TypeCurseur.WAIT)) this.cursor_suivant = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR); else this.cursor_suivant = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        metNouveauCurseur();
    }

    public void metNouveauCurseur() {
        composant.setCursor(cursor_suivant);
    }

    public void metAncienCurseur() {
        composant.setCursor(cursor_precedent);
    }
}
