package cantro.threads;

import java.util.ArrayList;
import cantro.*;
import cantro.entity.*;
import cantro.util.StringUtil;

public class BulletCheckerThread extends Thread {

    Cantro owner;

    public BulletCheckerThread(Cantro c) {
        owner = c;
    }

    public void run() {
        while (true) {
            ArrayList<Bullet> remove = new ArrayList<Bullet>();
            synchronized (owner.p.bullets) {
                for (Bullet b : owner.p.bullets) {
                    b.travel();
                    if (!b.isStillOnScreen()) remove.add(b);
                    if (isHitting(b)) {
                        remove.add(b);
                    }
                }
            }
            synchronized (owner.p.bullets) {
                for (Bullet b : remove) {
                    owner.p.bullets.remove(b);
                }
            }
            try {
                Thread.sleep(15);
            } catch (Exception e) {
            }
        }
    }

    public boolean isHitting(Bullet b) {
        synchronized (Zombie.zombies) {
            for (Zombie p : Zombie.zombies) {
                if (b.x > p.x && b.x < p.x + p.width && b.y > p.y && b.y < p.y + p.height) {
                    p.damage(5);
                    return true;
                }
            }
        }
        return false;
    }
}
