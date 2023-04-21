package swingPlacement;

import javax.swing.JCheckBox;

/**
 * @author Administrator
 *
 * TODO Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre - Pr�f�rences - Java - Style de code - Mod�les de code
 */
public class PJCheckBox extends JCheckBox {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8827751253892140227L;

    public PJCheckBox(int height, int width, int alignementX, int alignementY, String titre, boolean bool) {
        super(titre, bool);
        setOpaque(false);
        setBounds(0, 0, height, width);
        setLocation(alignementX, alignementY);
    }
}
