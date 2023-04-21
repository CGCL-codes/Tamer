package de.fhb.defenderTouch.units.amunition;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;
import de.fhb.defenderTouch.audio.FormatProblemException;
import de.fhb.defenderTouch.audio.SampleThread;
import de.fhb.defenderTouch.gamelogic.DefenderControl;
import de.fhb.defenderTouch.gamelogic.Player;
import de.fhb.defenderTouch.graphics.Animations;
import de.fhb.defenderTouch.graphics.GraphicTools;
import de.fhb.defenderTouch.units.root.Unit;
import org.newdawn.slick.Graphics;

public class ShootWithRange extends Shoot {

    protected int maxRange = 400;

    protected Vector2f startPostion;

    protected Animation smallExplosion;

    protected Player player;

    protected Animations gl;

    protected Graphics graphics;

    protected boolean shouldBeDelete = false;

    protected int nochXFramesZeichnen = 10;

    public ShootWithRange(int x, int y, int mode, Player player, Unit destinationUnit, int damage, DefenderControl gamelogic) {
        super(x, y, mode, player, destinationUnit, damage, gamelogic);
        this.startPostion = new Vector2f(x, y);
        this.player = player;
        this.gl = new Animations("small explosion", 17);
        this.smallExplosion = gl.getAni();
    }

    public void update() {
        super.update();
        if (this.startPostion.distance(this.position) > maxRange) {
            this.delete();
        }
    }

    public void delete() {
        this.shouldBeDelete = true;
    }

    public void drawFigure(Graphics graphics) {
        if (!shouldBeDelete) {
            super.drawFigure(graphics);
        }
        if (shouldBeDelete && nochXFramesZeichnen > 0) {
            smallExplosion.draw((-smallExplosion.getHeight() / 2) * player.getActualZoom(), (-smallExplosion.getWidth() / 2) * player.getActualZoom(), smallExplosion.getHeight() * player.getActualZoom(), smallExplosion.getWidth() * player.getActualZoom());
            graphics.resetTransform();
            this.nochXFramesZeichnen--;
        }
        if (shouldBeDelete && nochXFramesZeichnen == 0) {
            this.deleteWirklich();
        }
    }

    public void deleteWirklich() {
        this.playExplosionSound();
        super.delete();
    }

    protected void drawTail(Player player, Graphics graphics) {
        if (!shouldBeDelete) {
            super.drawTail(player, graphics);
        }
    }
}
