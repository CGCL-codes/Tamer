package uk.ac.aber.Blockmation.Tests;

import uk.ac.aber.Blockmation.MainFrame;
import uk.ac.aber.Blockmation.OurFileChooser;

public class OurFileChooserTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        OurFileChooser chooser = new OurFileChooser(mainFrame);
    }
}
