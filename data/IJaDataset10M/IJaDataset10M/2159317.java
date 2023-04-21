package no.eirikb.bomberman.shared.clientcommand;

import no.eirikb.bomberman.applet.game.Bomb;
import no.eirikb.bomberman.applet.game.Fire;
import no.eirikb.bomberman.applet.game.Way;
import no.eirikb.bomberman.shared.Game;
import no.eirikb.bomberman.shared.Map;
import no.eirikb.bomberman.shared.User;

/**
 *
 * @author eirikb
 */
public class BombExplodeEvent extends ClientEvent {

    public Bomb bomb;

    public BombExplodeEvent(Bomb bomb) {
        this.bomb = bomb;
    }

    public void execute(Game game) {
        game.removeBomb(bomb);
        User user = game.getUsers().get(bomb.getUser().getNick());
        user.setBombs(user.getBombs() + 1);
        addFire(game, bomb);
        chainReaction(game, bomb);
    }

    public void execute(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void addFire(Game game, Bomb bomb) {
        boolean l, u, r, d;
        l = u = r = d = true;
        int x = bomb.getX();
        int y = bomb.getY();
        game.addFire(new Fire(x, y, Way.NOWAI, true));
        for (int i = 1; i < bomb.getSize(); i++) {
            if (l) {
                l = addFire(game, new Fire(x - i, y, Way.LEFT, false));
            }
            if (u) {
                u = addFire(game, new Fire(x, y - i, Way.UP, false));
            }
            if (r) {
                r = addFire(game, new Fire(x + i, y, Way.RIGHT, false));
            }
            if (d) {
                d = addFire(game, new Fire(x, y + i, Way.DOWN, false));
            }
        }
        if (l) {
            addFire(game, new Fire(x - bomb.getSize(), y, Way.LEFT, true));
        }
        if (u) {
            addFire(game, new Fire(x, y - bomb.getSize(), Way.UP, true));
        }
        if (r) {
            addFire(game, new Fire(x + bomb.getSize(), y, Way.RIGHT, true));
        }
        if (d) {
            addFire(game, new Fire(x, y + bomb.getSize(), Way.DOWN, true));
        }
    }

    private void chainReaction(Game game, Bomb bomb) {
        Bomb[] bl = game.getBombs().toArray(new Bomb[0]);
        for (Bomb b : bl) {
            if (b != bomb) {
                if (bomb.compareTo(b) == 1) {
                    game.getBombs().remove(b);
                    User user = game.getUsers().get(bomb.getUser().getNick());
                    user.setBombs(user.getBombs() + 1);
                    addFire(game, b);
                    b.setTime(-1);
                    chainReaction(game, b);
                }
            }
        }
    }

    private boolean addFire(Game game, Fire fire) {
        if (Map.canWalk(game.getMap(fire.getX(), fire.getY()))) {
            game.addFire(fire);
            return true;
        } else {
            if (game.getMap(fire.getX(), fire.getY()) == Map.BRICK) {
                game.removeBrick(fire.getX(), fire.getY());
            }
            return false;
        }
    }
}
