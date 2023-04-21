package weapons;

import java.util.Random;
import javax.swing.Timer;
import game.World;
import particles.SimpleShot;
import game.AePlayWave;
import game.Player;

public class Shotgun extends Weapon {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public Shotgun() {
        this.name = "Shutgun";
        this.reloadtime = 1500;
        this.damage = 10;
    }

    public float[] shoot(short playerx, short playery, float aimx, float aimy, boolean dir, short seed, Player owner) {
        float[] recoil = new float[2];
        if (this.reloading) {
            recoil[0] = 0;
            recoil[1] = 0;
            return recoil;
        }
        Random random = new Random(seed);
        new AePlayWave("/shotgun2.wav").start();
        World.getInstance().addBullet(new SimpleShot(playerx, playery, aimx + (float) ((random.nextFloat() - 0.5) * 2), aimy + (float) ((random.nextFloat() - 0.5) * 2), this.damage, owner));
        World.getInstance().addBullet(new SimpleShot(playerx, playery, aimx + (float) ((random.nextFloat() - 0.5) * 2), aimy + (float) ((random.nextFloat() - 0.5) * 2), this.damage, owner));
        World.getInstance().addBullet(new SimpleShot(playerx, playery, aimx + (float) ((random.nextFloat() - 0.5) * 2), aimy + (float) ((random.nextFloat() - 0.5) * 2), this.damage, owner));
        World.getInstance().addBullet(new SimpleShot(playerx, playery, aimx + (float) ((random.nextFloat() - 0.5) * 2), aimy + (float) ((random.nextFloat() - 0.5) * 2), this.damage, owner));
        World.getInstance().addBullet(new SimpleShot(playerx, playery, aimx + (float) ((random.nextFloat() - 0.5) * 2), aimy + (float) ((random.nextFloat() - 0.5) * 2), this.damage, owner));
        this.reloading = true;
        Timer timer = new Timer(this.reloadtime, reload);
        timer.setRepeats(false);
        timer.start();
        if (dir == true) {
            recoil[0] = -aimx;
            recoil[1] = -aimy;
            return recoil;
        } else {
            recoil[0] = -1 * aimx;
            recoil[1] = -1 * aimy;
        }
        return recoil;
    }
}
