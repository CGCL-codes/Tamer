package mswing.buttons;

/**
 * Bouton 'Annuler'.
 *
 * @author Emeric Vernat
 */
public class MCancelButton extends mswing.MButton {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructeur.
	 */
    public MCancelButton() {
        super(MCancelButton.class);
        setActionCommand("cancel");
    }
}
