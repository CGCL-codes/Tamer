package ru.beta2.testyard.engine.points;

import ru.beta2.testyard.engine.ScriptPoint;

/**
 * User: Inc
 * Date: 19.06.2008
 * Time: 0:11:40
 */
public class LoginPoint extends ScriptPoint {

    private final int player;

    public LoginPoint(int player) {
        this.player = player;
    }

    public void exec() {
        expectations().expectEvent(new PlayerEvent(PlayerEvent.LOGGED_IN, player), this);
        link().login(player);
    }
}
