package controlers;

import graphics.ProfileFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import metier.BADA;

/**
 * Listener pour l'affichage de la frame tracant le profil 
 * de vol
 * 
 * @author Pierre
 *
 */
public class ProfileListener implements ActionListener {

    private BADA bada;

    private ProfileFrame frame;

    /**
	 * Constructeur
	 * @param bada la base BADA
	 * @param frame la fen�tre graphique
	 */
    public ProfileListener(BADA bada, ProfileFrame frame) {
        this.bada = bada;
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (bada.getCurrentModele().estTracable()) {
            frame.toFront();
            frame.setVisible(true);
        } else {
            System.out.println("Profile non tracable");
            JOptionPane.showMessageDialog(null, "Some data missing to draw graphics", "Caution", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
