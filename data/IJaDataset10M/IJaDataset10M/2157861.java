package mclib.pawn;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import mclib.slick.component.Skin;
import mclib.slick.component.interfaces.Pawn;
import mclib.slick.component.map.MapObjective;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class Bullet extends Pawn {

    private final int DAMAGE;

    private Dimension size;

    private final float speed;

    private final int MAX_LIFE;

    private int life;

    private ParticleSystem system;

    private ConfigurableEmitter emitter;

    private Pawn shooter;

    private float weight;

    private float initialDirection;

    public Bullet(Pawn shooter, String name, Point location, float direction) {
        this(shooter, name, location, direction, 3000, 600, 5, 0.001f);
    }

    public Bullet(Pawn shooter, String name, Point location, float direction, float speed, int life, int damage, float weight) {
        super(name);
        if (shooter.getSide() == Side.Enemy) {
            side = Side.EnemyBullet;
        } else {
            side = Side.PlayerBullet;
        }
        this.weight = weight;
        this.shooter = shooter;
        initialDirection = direction;
        setLocation(location);
        DAMAGE = damage;
        this.speed = speed;
        MAX_LIFE = life;
        this.life = MAX_LIFE;
        size = new Dimension(10, 10);
        initBody();
        buildEmitter();
    }

    @Override
    public void initBody() {
        setBody(new Body(new Box(size.width, size.height), weight));
        getBody().setPosition(getLocation().x, getLocation().y);
        getBody().setDamping(0);
        getBody().setUserData(this);
        getBody().setBitmask(Pawn.WEAPON_BITS);
        getBody().setRotation(initialDirection);
        Vector2f velocity = new Vector2f(-(float) sin(getBody().getRotation()), (float) cos(getBody().getRotation()));
        velocity.scale(speed);
        getBody().adjustVelocity(velocity);
    }

    private void buildEmitter() {
        system = new ParticleSystem("org/newdawn/slick/data/particle.tga", 2000);
        try {
            emitter = ParticleIO.loadEmitter(this.getClass().getResourceAsStream("/mclib/pawn/bullet.xml"));
            system.addEmitter(emitter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void collideWith(Pawn other) {
        if (other != null) {
            other.damage(DAMAGE);
            if (other.getHealth() <= 0) {
                if (other instanceof MapObjective) {
                    shooter.scoreObjective();
                } else {
                    shooter.scoreKill();
                }
            }
        }
        this.setRemove(true);
    }

    @Override
    public void damage(int amt) {
    }

    @Override
    public int getSkinIndex() {
        return 0;
    }

    @Override
    public void setSkin(Skin s, int skinIndex) {
    }

    @Override
    public void setSkin(int skinIndex) {
    }

    @Override
    public void draw(Graphics g, Point offset) {
        system.setPosition(offset.x, offset.y);
        emitter.setPosition(getLocation().x, getLocation().y);
        system.render();
    }

    @Override
    public void update(final int delta) {
        setLocation(new Point((int) getBody().getPosition().getX(), (int) getBody().getPosition().getY()));
        life -= delta;
        if (life < 0) {
            setRemove(true);
        }
        system.update(delta);
    }

    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public String getBindingString() {
        return null;
    }

    @Override
    public void keyPressed(int keyCode, String keyVal) {
    }

    @Override
    public void keyReleased(int keyCode, String keyVal) {
    }

    @Override
    public void destroy() {
        getBody().setUserData(null);
        setBody(null);
        emitter = null;
        system.removeAllEmitters();
        system = null;
    }

    @Override
    public int getHealth() {
        return 1;
    }

    @Override
    public void setRespawn() {
    }

    @Override
    public boolean doesBlockPawn(Pawn p) {
        if (shooter.getSide() == Pawn.Side.Enemy) {
            return p.getSide() == Pawn.Side.Player;
        } else if (shooter.getSide() == Pawn.Side.Player) {
            return p.getSide() == Pawn.Side.Enemy || p.getSide() == Pawn.Side.Objective;
        } else {
            return true;
        }
    }
}
