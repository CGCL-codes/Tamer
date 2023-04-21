package test.engine;

import agonism.ce.*;
import test.TActor;
import com.objectspace.jgl.Array;
import com.objectspace.jgl.UnaryFunction;
import java.util.Enumeration;

public class TCommand {

    public static void main(String[] args) throws InterruptedException {
        TActor actor = new TActor(new WrapperID("actor1"), new WrapperID("team1"));
        Engine engine = new RealtimeEngine(new AbstractGameState() {

            private double t = 0;

            public double getTime() {
                return t;
            }

            public void advance(double t) {
                this.t = t;
            }
        }, new Controller[0], null);
        Thread engineThread = new Thread(engine);
        engineThread.setDaemon(true);
        engineThread.start();
        for (int i = 0; i < 50; i++) {
            Action action = new MoveAction(actor);
            engine.addAction(action);
            Thread.currentThread().sleep(100);
        }
        java.lang.System.exit(0);
    }

    static class MoveAction extends AbstractAction {

        public MoveAction(Actor actor) {
            super(actor);
        }

        public void execute(GameState state) {
        }
    }
}
