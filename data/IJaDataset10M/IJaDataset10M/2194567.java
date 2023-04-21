package dw2;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Brian
 */
public class npcMonster extends NPC {

    protected int health = 10;

    protected static ArrayList<Image> image = new ArrayList<Image>();

    protected int cycle_count = 0;

    public static void initImage(Image images[]) {
        image.addAll(Arrays.asList(images));
    }

    public npcMonster(int x, int y, int direction_to_face, int points) {
        super(x, y - image.get(0).getHeight(null), direction_to_face, points);
        WALK_SPEED = 2;
        ATTACK_POWER = 4;
        FIRING_RATE = 250;
        graphics.add(new DWGraphics_Animation(image.get(0), 5, 4));
        graphics.add(new DWGraphics_Animation(image.get(1), 5, 3));
        graphics.add(new DWGraphics_Animation(image.get(2), 1, 8));
    }

    public void action(Main mainparent, Worm player) {
        cycle_count++;
        this.player = player;
        move(mainparent, player.getHeadPosition());
        if (state == 0) {
            notFire();
        }
        if (state == 1) {
            Fire();
            cycle_count++;
        }
        if (state == 2) {
            Hurt();
            cycle_count++;
        }
        if (fire_gun(mainparent, player.getHeadPosition())) {
            state = 1;
        }
        if (player.hitHead(graphics.get(graphics_index).getBounds()) == true) {
            health--;
            state = 2;
            cycle_count = 0;
            if (health == 0) {
                new AePlayWave("Sounds\\bit1.wav").start();
                player.add_points(points);
                deathParticles(mainparent, player);
                mainparent.removeNPC(this);
                return;
            }
        }
        if (cycle_count > 50) {
            state = 0;
            cycle_count = 0;
        }
    }

    public void notFire() {
        graphics_index = 0;
    }

    public void Fire() {
        graphics_index = 1;
    }

    public void Hurt() {
        graphics_index = 2;
    }

    public void deathParticles(Main mainparent, Worm player) {
        int center_x = x + graphics.get(graphics_index).getWidth() / 2;
        int center_y = y + graphics.get(graphics_index).getHeight();
        for (int w = 0; w < death_particle_count; w++) {
            mainparent.addParticle(new Particle(center_x, center_y, Color.GREEN, 2, 5));
        }
    }

    public boolean fire_gun(Main mainparent, Point head_pos) {
        bulletY = y + 25;
        bulletX = x + 5;
        if (head_pos.y < mainparent.getHeight() / 2) {
            fireCount++;
            if (fireCount == FIRING_RATE) {
                new AePlayWave("Sounds\\sndGun.wav").start();
                Bullet bullet_to_fire = new Bullet(mainparent, x, bulletY, Color.yellow, bullet_size, ATTACK_POWER);
                double bullet_direction = Worm.pointDirection(x, y, head_pos.x, head_pos.y);
                double speed = ATTACK_BULLET_SPEED;
                bullet_to_fire.setXYspeed(Worm.getXspeed(bullet_direction, speed), Worm.getYspeed(bullet_direction, speed));
                mainparent.addBullet(bullet_to_fire);
                fireCount = 0;
                return true;
            }
        }
        if (head_pos.y < mainparent.getHeight() / 2) {
            fireCount++;
            if (fireCount == FIRING_RATE) {
                new AePlayWave("Sounds\\sndGun.wav").start();
                Bullet bullet_to_fire = new Bullet(mainparent, x, bulletY, Color.yellow, bullet_size, ATTACK_POWER);
                double bullet_direction = Worm.pointDirection(x, y, head_pos.x, head_pos.y);
                double speed = ATTACK_BULLET_SPEED;
                bullet_to_fire.setXYspeed(Worm.getXspeed(bullet_direction, speed), Worm.getYspeed(bullet_direction, speed));
                mainparent.addBullet(bullet_to_fire);
                fireCount = 0;
                return true;
            }
        }
        return false;
    }
}
