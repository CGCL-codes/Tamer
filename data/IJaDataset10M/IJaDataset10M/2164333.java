package view;

import view.forms.CreateGameForm;
import view.forms.MainMenuForm;
import view.forms.NewPlayerForm;
import view.forms.GamePlayersForm;
import view.forms.JoinGameForm;
import view.forms.MainForm;
import view.forms.NewGameOptionsForm;

/**
 *
 * @author JPEXS
 */
public class Main {

    public static MainForm mainForm;

    public static MainMenuForm mainMenuForm;

    public static NewPlayerForm newPlayerForm;

    public static GamePlayersForm gamePlayersForm;

    public static JoinGameForm joinGameForm;

    public static CreateGameForm createGameForm;

    public static NewGameOptionsForm newGameOptionsForm;

    public static eve.fx.Font getFontByHeight(String name, int fontHeight) {
        eve.fx.Graphics g = new eve.fx.Graphics(new eve.fx.Image(1, 1));
        eve.fx.Font f = null;
        for (int i = 1; i < 200; i++) {
            f = new eve.fx.Font(name, 0, i);
            if (g.getFontMetrics(f).getHeight() >= fontHeight) {
                return f;
            }
        }
        return f;
    }

    public static void mainMenu() {
        if (mainForm != null) {
            mainForm.close(0);
            mainForm = null;
        }
        if (gamePlayersForm != null) {
            gamePlayersForm.close(0);
            gamePlayersForm = null;
        }
        mainMenuForm = new MainMenuForm();
        mainMenuForm.show();
    }

    public static void newGame() {
        if (newGameOptionsForm != null) {
            newGameOptionsForm.close(0);
            newGameOptionsForm = null;
        }
        if (gamePlayersForm != null) {
            gamePlayersForm.close(0);
            gamePlayersForm = null;
        }
        mainForm = new MainForm();
        mainForm.show();
    }

    public static void selectGamePlayers() {
        if (mainMenuForm != null) {
            mainMenuForm.close(0);
            mainMenuForm = null;
        }
        if (newPlayerForm != null) {
            newPlayerForm.close(0);
            newPlayerForm = null;
        }
        if (createGameForm != null) {
            createGameForm.close(0);
            createGameForm = null;
        }
        if (joinGameForm != null) {
            joinGameForm.close(0);
            joinGameForm = null;
        }
        if (newGameOptionsForm != null) {
            newGameOptionsForm.close(0);
            newGameOptionsForm = null;
        }
        gamePlayersForm = new GamePlayersForm();
        gamePlayersForm.show();
    }

    public static void newPlayer() {
        if (gamePlayersForm != null) {
            gamePlayersForm.close(0);
            gamePlayersForm = null;
        }
        newPlayerForm = new NewPlayerForm();
        newPlayerForm.show();
    }

    public static void joinGame() {
        if (gamePlayersForm != null) {
            gamePlayersForm.close(0);
            gamePlayersForm = null;
        }
        joinGameForm = new JoinGameForm();
        joinGameForm.show();
    }

    public static void createGame() {
        if (newGameOptionsForm != null) {
            newGameOptionsForm.close(0);
            newGameOptionsForm = null;
        }
        if (gamePlayersForm != null) {
            gamePlayersForm.close(0);
            gamePlayersForm = null;
        }
        createGameForm = new CreateGameForm();
        createGameForm.show();
    }

    public static void newGameOptions() {
        if (gamePlayersForm != null) {
            gamePlayersForm.close(0);
            gamePlayersForm = null;
        }
        if (createGameForm != null) {
            createGameForm.close(0);
            createGameForm = null;
        }
        newGameOptionsForm = new NewGameOptionsForm();
        newGameOptionsForm.show();
    }
}
