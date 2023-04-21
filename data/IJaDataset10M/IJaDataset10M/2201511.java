package za.co.me23.methods;

import za.co.me23.canvas.Me23Canvas;
import za.co.me23.canvas.Methode;
import za.co.me23.chat.Retriever;
import za.co.me23.game.VillageScreen;

/**
 *
 * @author Jean-Pierre
 */
public class VillageOpenMethode extends Methode {

    public VillageOpenMethode(String iCaption, int iVid, int iCid) {
        super("Opening");
        caption = iCaption;
        vid = iVid;
        cid = iCid;
    }

    public void methode() {
        Me23Canvas.getMe23Canvas().switchScreen(VillageScreen.getNewVillageScreen(caption, vid, cid));
        setNextDisplay(null, Me23Canvas.getMe23Canvas());
        Retriever.switchGame();
    }

    private int vid, cid;

    private String caption;
}
