package mswing.buttons;

/**
 * Bouton 'Imprimer'.
 *
 * @author Emeric Vernat
 */
public class MPrintButton extends mswing.MButton {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructeur.
	 */
    public MPrintButton() {
        super(MPrintButton.class);
        setActionCommand("print");
    }
}
