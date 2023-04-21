package Gum;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ItemStunner extends ItemBase {

    public ItemStunner(String rootName) {
        super(rootName);
    }

    public void save(String rootName) {
        super.save(rootName);
        FileHandler itemFile = new FileHandler();
        String fileName = rootName + "." + this.getItemName();
        itemFile.write_setting(fileName, "class", "itemstunner");
        itemFile.close_file();
    }

    public void attack(Player player, Player enemy) {
        if (enemy.getSetting("stun") <= 0 && this.getSetting("uses") > 0) {
            this.setSetting("uses", this.getSetting("uses") - 1);
            int dex = player.getSetting("dex");
            int ref = enemy.getSetting("ref");
            if (dex < 1) {
                dex = 1;
            }
            if (ref < 1) {
                ref = 1;
            }
            int attack_roll = random.nextInt(dex);
            int defend_roll = random.nextInt(ref);
            System.out.println("attack roll:" + String.valueOf(attack_roll));
            System.out.println("defend roll:" + String.valueOf(defend_roll));
            if (attack_roll > defend_roll) {
                enemy.setSetting("stun", 1);
                player.broadcast("Your " + this.getHitString() + "\r\n");
                enemy.broadcast("Your enemy's " + this.getHitString() + "\r\n");
            } else {
                player.broadcast("Your " + this.getMissString() + "\r\n");
                enemy.broadcast("Your enemy's " + this.getMissString() + "\r\n");
            }
        } else {
            if (this.getSetting("uses") <= 0) {
                player.broadcast(this.getItemName() + " is depleted!");
            }
        }
    }
}
