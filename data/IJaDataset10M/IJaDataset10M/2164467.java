package JiBoot;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class bt_Check extends AbstractAction {

    private main_fen fenetre;

    public bt_Check(main_fen fenetre, String texte) {
        super(texte);
        this.fenetre = fenetre;
    }

    public void actionPerformed(ActionEvent e) {
        JFrame window_check = new win_check(fenetre);
    }
}
