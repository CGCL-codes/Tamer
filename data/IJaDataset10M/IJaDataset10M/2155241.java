package JiBoot;

import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class JiBoot {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                main_fen fenetre = new main_fen();
                fenetre.setVisible(true);
            }
        });
    }
}
