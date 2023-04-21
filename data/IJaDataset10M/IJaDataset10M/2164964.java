package swingPlacement;

import javax.swing.JLabel;

/**
 * @author Administrator
 *
 * TODO Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre - Pr�f�rences - Java - Style de code - Mod�les de code
 */
public class PJLabel extends JLabel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6563234004832299223L;

    public PJLabel(int alignementX, int alignementY, String titre) {
        super(titre);
        setLocation(alignementX, alignementY);
    }
}
