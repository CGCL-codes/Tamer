package ui;

import game.CivilWarAction;
import game.Game;
import java.util.List;
import java.util.Map;

public class PoliticActionMakeCivilWarMenu extends AbstractMenu {

    public PoliticActionMakeCivilWarMenu(Map<String, String> gameTexts, List<IMenu> menuList, Game game) {
        super(gameTexts, menuList);
        this.setGame(game);
        if (game == null) throw new IllegalArgumentException("'game' cannot be null");
    }

    public void execute() {
        getMenuOptionsList().add(getGameTexts().get("back"));
        getMenuOptionsList().add(getGameTexts().get("politicActionMakeCivilWarMenu_doCivilWar"));
        showMenuContents();
        setPlayerChoice(requestPlayerChoice());
        if (getPlayerChoice().equals("1")) {
            CivilWarAction civilWarAction = new CivilWarAction(getGame().getWhoHasTheTurn());
            getGame().getRound().getCurrentTurn().addGameAction(civilWarAction);
            showActionDoneMessage(getGame().getWhoHasTheTurn().getPlayerProxenus().getPosition().getName());
        }
    }

    public String getHeaderMessage() {
        return getGameTexts().get("politicActionMakeCivilWarMenu_headerMessage");
    }

    public IMenu getNextMenu() {
        IMenu next = null;
        switch(getPlayerChoice()) {
            case 0:
                next = getMenuList().get((getMenuList().size() - 1) - 1);
                break;
            default:
                next = new GameMainMenu(getGameTexts(), getMenuList(), getGame());
                next.setAutoExecutable(false);
                break;
        }
        return next;
    }

    public void showActionDoneMessage(String polisName) {
        System.out.println(" ");
        System.out.println(getGameTexts().get("politicActionCivilWarMenu_civilWarDone") + " " + polisName);
    }
}
