package photobook.view;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import photobook.data.model.Album;
import photobook.data.model.Group;

/**
 * Dialogue box permettant d'ajouter un groupe
 * @param userLogin
 * @return le nouveau groupe newGroup
 * @author Rova Ramarson
 * @version 0.1
 */
public class AjouterGroupDialBox extends JDialog {

    private JPanel textFieldPanel = null;

    private JTextField groupName = null;

    private JOptionPane optionPane = null;

    public AjouterGroupDialBox() {
        textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new MigLayout());
        groupName = new JTextField("");
        textFieldPanel.add(new JLabel("Nom :"));
        textFieldPanel.add(groupName, "wrap, growx");
    }

    /**
	 * Attention
	 */
    public String answerUserAjoutGroupe() {
        String result = null;
        int answer = -1;
        answer = optionPane.showOptionDialog(null, textFieldPanel, "Ajouter un Album", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (answer == JOptionPane.OK_OPTION) {
            result = groupName.getText();
            if (result.equals("")) {
                JOptionPane.showMessageDialog(null, "Le nom du groupe ne doit pas �tre vide, creation annulee");
                result = null;
            } else {
                JOptionPane.showMessageDialog(null, "Votre groupe " + result + " a ete cree.");
            }
        } else if (answer == JOptionPane.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "Creation d'un nouveau groupe annulee.");
        }
        return result;
    }
}
