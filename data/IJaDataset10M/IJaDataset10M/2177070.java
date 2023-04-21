package aplikasi.com.main;

import aplikasi.com.gui.formUtama;
import javax.swing.JFrame;

/**
 *
 * @author Henfryandie
 */
public class aplikasiRental {

    public static void main(String[] args) {
        formUtama utama = new formUtama();
        utama.setExtendedState(JFrame.MAXIMIZED_BOTH);
        utama.setVisible(true);
    }
}
