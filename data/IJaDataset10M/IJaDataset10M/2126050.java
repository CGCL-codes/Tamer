package cop;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import engine.Actor;
import engine.Control;
import engine.EventProcessor_IF;
import engine.Game;
import swing.GameFrame;
import swing.GamePanel;
import utils.MyException;

public class Main {

    private static void addProcessors(Control c) {
        c.addProcessor(new EventProcessor_IF() {

            @Override
            public synchronized void handleEvent(Object o, Game game) {
                if (o instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) o;
                    int x = e.getX(), y = e.getY();
                    if (game.getCurrentRoom().getName().equals("GameMenu")) {
                        ArrayList<Actor> actors = game.getCurrentRoom().getActors();
                        for (int i = 0; i < actors.size(); i++) {
                            Actor a = actors.get(i);
                            if (a.getBounds().contains(x, y)) {
                                if (e.getID() == MouseEvent.MOUSE_PRESSED || e.getID() == MouseEvent.MOUSE_DRAGGED) a.setSprIndex(0); else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                                    a.setSprIndex(1);
                                    try {
                                        if ((((Button) a).getString()).equals("Two players")) game.goToRoom("GameTwo"); else if ((((Button) a).getString()).equals("Exit")) System.exit(0);
                                    } catch (MyException e1) {
                                    }
                                }
                            } else a.setSprIndex(1);
                        }
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        COPGame g = new COPGame("CircusOfPlates");
        try {
            g.goToRoom("GameMenu");
        } catch (MyException e) {
        }
        Control c = new Control(g);
        GameFrame f = new GameFrame();
        GamePanel p = new GamePanel(800, 600, c);
        f.add(p);
        f.pack();
        addProcessors(c);
        c.addView(p);
        c.addView(f);
        c.run();
    }
}
