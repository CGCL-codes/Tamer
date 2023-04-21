package text_adventure;

import javax.swing.JOptionPane;

public class WeaponRifle extends Weapon {

    public static final int RANGE = 8;

    public static final int DAMAGE = 4;

    public static final int CLIP_SIZE = 8;

    public WeaponRifle() {
        super("Rifle", 3, WeaponRifle.DAMAGE, WeaponRifle.CLIP_SIZE);
    }

    public void reload() {
        super.reload();
    }

    public void shoot(Direction d, World w) {
        int x, y;
        if (clip.getCurrentValue() != 0) {
            clip.decrementValue();
            x = w.getPlayers().getCurrentPlayer().getCurrentTile().getX();
            y = w.getPlayers().getCurrentPlayer().getCurrentTile().getY();
            Tile t, target;
            target = null;
            boolean blocked = false;
            if (d == Direction.NORTH) {
                for (int i = 1; (i <= WeaponRifle.RANGE) && (y - i >= 0) && (target == null) && (!blocked); i++) {
                    t = w.getTiles().get(y - i).get(x);
                    if (t.hasPlayer()) {
                        target = t;
                    } else if (t.isImpassable()) {
                        blocked = true;
                    }
                }
            } else if (d == Direction.EAST) {
                for (int i = 1; (i <= WeaponRifle.RANGE) && (x + i < World.WORLD_X_SIZE) && (target == null) && (!blocked); i++) {
                    t = w.getTiles().get(y).get(x + i);
                    if (t.hasPlayer()) {
                        target = t;
                    } else if (t.isImpassable()) {
                        blocked = true;
                    }
                }
            } else if (d == Direction.SOUTH) {
                for (int i = 1; (i <= WeaponRifle.RANGE) && (y + i < World.WORLD_Y_SIZE) && (target == null) && (!blocked); i++) {
                    t = w.getTiles().get(y + i).get(x);
                    if (t.hasPlayer()) {
                        target = t;
                    } else if (t.isImpassable()) {
                        blocked = true;
                    }
                }
            } else if (d == Direction.WEST) {
                for (int i = 1; (i <= WeaponRifle.RANGE) && (x - i >= 0) && (target == null) && (!blocked); i++) {
                    t = w.getTiles().get(y).get(x - i);
                    if (t.hasPlayer()) {
                        target = t;
                    } else if (t.isImpassable()) {
                        blocked = true;
                    }
                }
            }
            if (target != null) {
                target.getPlayer().takeDamage(this.getWeaponValue());
                if (target.getPlayer().isDead()) {
                    target.getPlayer().beDead();
                    w.getPlayers().getCurrentPlayer().addKill();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "You are out of ammo and need to reload");
        }
    }
}
