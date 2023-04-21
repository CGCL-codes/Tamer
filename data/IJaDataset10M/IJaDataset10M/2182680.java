package ch.zarzu.champions.builder;

import javax.swing.UIManager;
import ch.zarzu.util.CustomThread;

public class RootLauncher {

    public static void main(String[] args) {
        String build = "";
        for (String s : args) {
            build = s;
        }
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "ChampionBuilder");
            try {
            } catch (Exception e) {
            }
        }
        new CustomThread<String>(build) {

            public void run() {
                Main main = Main.getInstance();
                main.createAndShowGUI(this.getData());
            }
        }.start();
    }
}
