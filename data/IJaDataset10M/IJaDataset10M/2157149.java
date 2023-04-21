package openwar;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import openwar.DB.Unit;
import openwar.battle.BattleAppState;
import openwar.world.Tile;

/**
 *
 * @author kehl
 */
public class MenuAppState extends AbstractAppState implements ScreenController {

    public enum Status {

        None, Main, NewGame, Options, Quit
    }

    Main game;

    AppStateManager manager;

    Status status;

    Nifty nifty;

    Screen screen;

    public MenuAppState() {
        status = Status.None;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        this.initialize(stateManager, (Main) app);
    }

    public void initialize(AppStateManager stateManager, Main main) {
        game = main;
        manager = stateManager;
        initialized = true;
    }

    public void startGame() {
        game.worldMapState.logic.playerFaction = game.worldMapState.logic.currentTurn = "romans";
        game.gameLoaderState.loadWorldMap();
    }

    public void quickBattle() {
        Unit m = new Unit("militia");
        m.count = 45;
        m.att = 3;
        m.def = 2;
        m.exp = 0;
        Unit l = new Unit("bowmen");
        l.count = 15;
        l.att = 1;
        l.def = 1;
        l.exp = 0;
        ArrayList<Unit> a1 = new ArrayList<Unit>();
        ArrayList<Unit> a2 = new ArrayList<Unit>();
        a1.add(m);
        a1.add(m);
        a1.add(l);
        a2.add(m);
        a2.add(l);
        BattleAppState b = new BattleAppState(a1, a2);
        game.gameLoaderState.loadBattle(b);
    }

    public void quitGame() {
        game.wishToQuit = true;
    }

    public void enterMainMenu() {
        game.nifty.fromXml("ui/menu/ui.xml", "start", this);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void bind(Nifty n, Screen s) {
        nifty = n;
        screen = s;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }
}
