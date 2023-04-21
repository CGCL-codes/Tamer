package game.source.shot;

import game.source.gui.shot.GuiShotBasic;
import game.source.ships.GameInterface;
import game.source.ships.Ship;
import game.source.shot.GuiShotInterface.Level;
import game.source.shot.GuiShotInterface.Shooter;
import java.awt.Point;

public class ShotBasic extends Shot {

    private static final int damage = 10;

    public ShotBasic(GuiShotBasic gui, Shooter shooter, GameInterface game, Point start) {
        super(gui, shooter, Level.BASIC, game, start, 1000);
    }

    @Override
    public void action(Ship hit) {
        hit.setHelthPoints(hit.getHelthPoints() - ShotBasic.damage);
        this.getGame().removeShot(this);
    }

    @Override
    public int getDamage() {
        return ShotBasic.damage;
    }
}
