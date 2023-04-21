package net.sourceforge.mandelbroccoli.input;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Mathias Johansson
 */
public class EventHub {

    public static interface EffectListener {

        public void effectTriggered(EffectEvent event);
    }

    public static interface GamepadEventListener {

        public void gamepadTriggered(GamepadEvent event);
    }

    protected static List<EffectListener> effectListeners = new LinkedList<EffectListener>();

    protected static List<GamepadEventListener> gamepadEventListeners = new LinkedList<GamepadEventListener>();

    public static void registerEffectListener(EffectListener listener) {
        effectListeners.add(listener);
    }

    public static void registerGamepadEventListener(GamepadEventListener listener) {
        gamepadEventListeners.add(listener);
    }

    public static void unregisterEffectListener(EffectListener listener) {
        effectListeners.remove(listener);
    }

    public static void unregisterGamepadEventListener(GamepadEventListener listener) {
        gamepadEventListeners.remove(listener);
    }

    public static void triggerEffect(EffectEvent event) {
        for (EffectListener listener : effectListeners) listener.effectTriggered(event);
    }

    public static void sendGamepadEvent(GamepadEvent event) {
        for (GamepadEventListener listener : gamepadEventListeners) listener.gamepadTriggered(event);
    }
}
