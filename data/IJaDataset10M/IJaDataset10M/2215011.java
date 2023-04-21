package infothello.gui.event;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButtonMenuItem;

/**
 * La classe des boutons dans "Mode de Jeu" dans le menu
 */
public class ModeJeuAction extends AbstractAction {

    public ModeJeuAction(String titre) {
        super(titre);
    }

    public void actionPerformed(ActionEvent e) {
        String nomBouton = ((JRadioButtonMenuItem) e.getSource()).getText();
        String action = ((JRadioButtonMenuItem) e.getSource()).getActionCommand();
        System.out.println("Vous avez cliqué sur un bouton dans \"Mode de jeu\" => " + action + " : " + nomBouton);
    }
}
