package dw2;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Brian
 */
public class npcBlowie extends NPC {

    int cycle_count = 0;

    int health = 3;

    boolean collided_w_worm = false;

    int current_state;

    int flee_duration;

    int detonate_duration;

    protected static ArrayList<Image> image = new ArrayList<Image>();

    public static void initImage(Image images[]) {
        image.addAll(Arrays.asList(images));
    }

    public npcBlowie(int x, int y, int direction_to_face, int points) {
        super(x, y - image.get(0).getHeight(null), direction_to_face, points);
        digging_state();
        graphics.add(new DWGraphics(image.get(0)));
    }

    @Override
    public void action(Main mainparent, Worm player) {
        move(mainparent, player.getHeadPosition());
        cycle_count++;
        if (current_state == 0) {
        } else if (current_state == 1) {
            flee_state_move(mainparent, player);
            if (player.hitHead(graphics.get(graphics_index).getBounds()) == true) {
                collided_w_worm = true;
                new AePlayWave("Sounds\\bit1.wav").start();
                player.add_points(points);
                death_state();
            } else if (cycle_count >= flee_duration) {
                suicidal_state();
            }
        } else if (current_state == 2) {
            suicidal_state_move(mainparent, player);
            if (player.hitWorm(graphics.get(graphics_index).getBounds()) == true) {
                player.hurtWorm(10);
                death_state();
            } else if (cycle_count >= detonate_duration) {
                death_state();
            }
        } else if (current_state == 3) {
            if (player.hitWorm(graphics.get(graphics_index).getBounds()) == true && collided_w_worm == false) {
                player.hurtWorm(ATTACK_POWER);
            }
            if (1 == 1) {
                mainparent.removeNPC(this);
            }
        }
    }

    private void flee_state_move(Main mainparent, Worm player) {
    }

    private void suicidal_state_move(Main mainparent, Worm player) {
    }

    private void digging_state() {
        graphics_index = 0;
        current_state = 0;
        cycle_count = 0;
        ATTACK_POWER = 0;
        WALK_SPEED = 0;
    }

    private void death_state() {
        graphics_index = 3;
        WALK_SPEED = 0;
        current_state = 3;
        cycle_count = 0;
        ATTACK_POWER = 10;
    }

    private void flee_state() {
        graphics_index = 1;
        WALK_SPEED = 6;
        cycle_count = 0;
    }

    private void suicidal_state() {
        graphics_index = 2;
        WALK_SPEED = 8;
        current_state = 2;
        cycle_count = 0;
        ATTACK_POWER = 10;
    }
}
