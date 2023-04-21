package nomads.gui;

import javax.swing.JFrame;

/**
 * //TODO: O scurta descriere a clasei
 * 
 * @author Victor
 * 
 */
public class NomadsStandalone extends JFrame {

    private GUI gui;

    private NomadsStandalone() {
        gui = new GUI();
        gui.path = "small_easy.ppm";
        gui.init();
        getContentPane().add(gui);
    }

    public void hide() {
        super.hide();
        System.out.println("hide");
        System.exit(0);
    }

    public static void main(String argv[]) {
        NomadsStandalone a = new NomadsStandalone();
        a.pack();
        a.show();
        a.setExtendedState(a.MAXIMIZED_BOTH);
        System.out.println("shown");
    }
}
