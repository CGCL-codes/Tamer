package org.zkoss.ganttz.util;

/**
 * @author Óscar González Fernández <ogfernandez@gmail.com>
 */
public class ReentranceGuard {

    public interface IReentranceCases {

        public void ifNewEntrance();

        public void ifAlreadyInside();
    }

    private final ThreadLocal<Boolean> inside = new ThreadLocal<Boolean>() {

        protected Boolean initialValue() {
            return false;
        }

        ;
    };

    public void entranceRequested(IReentranceCases reentranceCases) {
        if (inside.get()) {
            reentranceCases.ifAlreadyInside();
            return;
        }
        inside.set(true);
        try {
            reentranceCases.ifNewEntrance();
        } finally {
            inside.set(false);
        }
    }
}
