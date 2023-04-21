package com.gochromium.nes.client.ui;

import java.util.List;
import com.gochromium.nes.client.model.Controller;
import com.gochromium.nes.client.model.Game;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface GameListView extends IsWidget {

    void setPresenter(Presenter listener);

    void renderGameList(List<Game> gameList);

    void displayGettingStarted(boolean display);

    void loadGameCartridge(byte[] game);

    void loadSettings(Controller controller);

    public interface Presenter {

        void goTo(Place place);

        void onGameClicked(String gameId);
    }
}
