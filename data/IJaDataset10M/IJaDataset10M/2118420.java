package org.pigeons.tivo;

import java.awt.Color;
import com.tivo.hme.bananas.BApplication;
import com.tivo.hme.bananas.BScreen;
import com.tivo.hme.bananas.BText;

public class PlaylistScreen extends BScreen {

    private PlaylistView pview;

    private BText title;

    public PlaylistScreen(BApplication app) {
        super(app);
        setResource(Color.black);
        title = new BText(getNormal(), SAFE_TITLE_H, SAFE_TITLE_V - 10, (getWidth() - (SAFE_TITLE_H * 2)), 54);
        title.setFlags(RSRC_TEXT_WRAP | RSRC_VALIGN_TOP);
        title.setFont("default-22.font");
        pview = new PlaylistView(getNormal(), SAFE_TITLE_H, SAFE_TITLE_V + 20, 500, 360, 30);
        pview.setVisible(true);
        setFocusDefault(pview);
        title.setValue("Current Playlist");
        title.setColor(Color.GREEN);
    }

    public PlaylistView getPlaylistView() {
        return pview;
    }

    public void setRandomized(boolean isRandomized) {
        if (isRandomized) {
            title.setValue("Current Playlist (shuffle)");
        } else {
            title.setValue("Current Playlist");
        }
    }
}
