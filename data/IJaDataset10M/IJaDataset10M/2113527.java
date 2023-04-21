package sia.tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import sia.domain.Universe;
import sia.ui.frames.MapWindowFrame;

public class Main {

    private static Universe universe;

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws Exception {
        universe = new Universe(1200, 1200);
        universe.initFromMAPFile(new FileInputStream("D:\\Gry\\Stars\\creche.MAP"));
        universe.processPFile(new FileInputStream("D:\\Gry\\Stars\\creche.P1"), 2425);
        universe.processPFile(new FileInputStream("D:\\Gry\\Stars\\creche.P4"), 2423);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    Method setLAF = Class.forName("javax.swing.UIManager").getMethod("setLookAndFeel", String.class);
                    setLAF.invoke(null, "org.jvnet.substance.skin.SubstanceMagmaLookAndFeel");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MapWindowFrame(universe);
            }
        });
    }
}
