package jaytter;

import javax.swing.UIManager;
import ui.core.InitWindow;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        InitWindow.main(args);
    }
}
