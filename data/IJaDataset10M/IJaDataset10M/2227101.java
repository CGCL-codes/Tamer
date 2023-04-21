package Cliente;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel;
import GUI.Login;

public class mainCliente {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new SubstanceRavenGraphiteGlassLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        Login log = new Login();
    }
}
