package wood.controller.controllers;

import wood.controller.GameFlow;
import wood.controller.keys.KeyBindings;
import wood.controller.listeners.*;
import wood.view.viewport.IntroViewport;

public class IntroViewportController extends Controller {

    private final IntroViewport viewport;

    private final GameFlow flow;

    private IntroViewportController(KeyBindings bindings, IntroViewport introViewport, GameFlow flow) {
        super(bindings, introViewport, flow.getGameManager());
        this.viewport = introViewport;
        this.flow = flow;
    }

    public static IntroViewportController makeIntroViewportController(KeyBindings bindings, IntroViewport introViewport, GameFlow flow) {
        IntroViewportController ret;
        ret = new IntroViewportController(bindings, introViewport, flow);
        ret.hookup();
        return ret;
    }

    @Override
    protected void setInitialHandlers() {
        addCustomMouseListener(new MouseClickListener(viewport.getNewButton().getDrawableID(), new NewGameHandler()));
        addCustomMouseListener(new MouseClickListener(viewport.getQuitButton().getDrawableID(), new QuitHandler()));
        addCustomMouseListener(new MenuItemMouseListener(viewport.getNewButton()));
        addCustomMouseListener(new MenuItemMouseListener(viewport.getQuitButton()));
        addCustomMouseListener(new MenuItemMouseListener(viewport.getLoadButton()));
        addCustomMouseListener(new MenuItemMouseListener(viewport.getOptionsButton()));
    }

    private class NewGameHandler implements Runnable {

        public void run() {
            flow.getGameManager().getDrawEventManager().pop();
            flow.gotoAvatarSel();
        }
    }

    private class QuitHandler implements Runnable {

        public void run() {
            flow.quit();
        }
    }
}
