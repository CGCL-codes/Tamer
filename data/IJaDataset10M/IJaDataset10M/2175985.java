package scrabble.client.cController;

import java.util.ArrayList;
import scrabble.client.cModel.model.Player;
import scrabble.client.cView.PlayerGWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * Classe PLayerController : Gestion des joueurs.
 * @author Thibault MERLE et Jonathan BRUEIL
 *
 */
public class PlayerController implements ClickHandler {

    private PlayerGWT view;

    private Player[] player;

    /**
	 * Initialise les vues et modele liés aux joueurs.
	 * @param gameController
	 * @param numberOfPlayers
	 * @param listPlayers
	 */
    public PlayerController(Player[] player, PlayerGWT view) {
        this.player = player;
        this.view = view;
    }

    public void setName(ArrayList<Player> listPl) {
        for (int i = 0; i < listPl.size(); ++i) {
            player[i].setName(listPl.get(i).getName());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        ArrayList<String> listNames = view.getTextFieldNames();
        for (int i = 0; i < listNames.size(); ++i) player[i].setName(listNames.get(i));
    }
}
