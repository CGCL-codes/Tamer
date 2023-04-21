package photobook.view;

import javax.swing.JOptionPane;
import photobook.data.model.Group;
import photobook.data.model.User;

/**
 * <b>SupprimerAmiDialBox est la dialogue box qui permet de supprimer un contact</b>
 * <p>
 * Cette dialogue box permet � un utilisateur de supprimer un contact
 * </p>
 * @author Rova Ramarson 
 * @version 1.0
 * 
 */
public class SupprimerAmiDialBox {

    private JOptionPane supression = null;

    private String suppNameAmi = null;

    /**
	 * R�cup�re le r�sultat de la suppression d'un ami ou d'une photo
	 * @arg Ami � supprimer
	 * @return /
	 */
    public SupprimerAmiDialBox(String _Name) {
        supression = new JOptionPane();
        suppNameAmi = _Name;
    }

    /**
	 * R�cup�re le r�sultat de la suppression d'un ami ou d'une photo
	 * @return result
	 * result = 1 -> suppression de l'ami ou de la photo effectu�e
	 * result = 0 -> annulation de la suppression
	 * result = -1 -> erreur
	 */
    public int answerUserDeleteAmi() {
        int answer = -1;
        int result = -1;
        answer = supression.showConfirmDialog(null, "Etes-vous sur de vouloir supprimer " + suppNameAmi + " ?", "Suppression", JOptionPane.YES_NO_OPTION);
        if (answer == supression.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Suppression de " + suppNameAmi + " effectuee.");
            result = 1;
        }
        if (answer == supression.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Suppression de  " + suppNameAmi + " annulee.");
            result = 0;
        }
        return result;
    }
}
