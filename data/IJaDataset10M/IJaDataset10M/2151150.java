package ui;

import javax.swing.JFrame;

public class FlughafenInfo {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        MainMenu mainWindow = MainMenu.getInstance();
        mainWindow.setSize(600, 600);
        mainWindow.setResizable(true);
        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
