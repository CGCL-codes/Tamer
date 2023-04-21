package tauwars;

import ui.*;

public class Game {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            UI gui = new ServerUI(new UIDrivenEngine());
            gui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
