package de.mse.mogwai.utils.uieditor;

/**
 * UI - Editor
 *
 * @author  Mirko Sertic.
 */
public class UIEditor {

    public static void main(String[] args) throws Exception {
        javax.swing.UIManager.setLookAndFeel(new com.incors.plaf.kunststoff.KunststoffLookAndFeel());
        EditorMainFrame main = new EditorMainFrame();
        main.show();
    }
}
