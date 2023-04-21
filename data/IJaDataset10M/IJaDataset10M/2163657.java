package game.source.ships;

import game.source.gui.ship.GuiEnemyBoss;
import game.source.shot.GuiShotInterface.Level;
import game.source.shot.GuiShotInterface.Shooter;
import java.awt.Point;

public class EnemyBoss extends EnemyShip {

    public EnemyBoss(Point start, GuiEnemyBoss gui, GameInterface game) {
        super(start, gui, game, 300, 300);
        this.superMove(new Point(0, 0));
        this.helthPoints = 1000;
    }

    @Override
    public void update() {
        this.game.createShot(this, Shooter.ENEMY, Level.MEDIUM);
    }

    @Override
    public void move(Point p) {
        if (this.getPosX() <= 0) {
            this.superMove(new Point(game.getGameWidth(), this.getHeight() / 2));
        } else if (getPosX() >= game.getGameWidth()) {
            this.superMove(new Point(0, this.getHeight() / 2));
        }
    }

    @Override
    public void endExplosion() {
        super.endExplosion();
        this.game.gameWin();
    }
}
