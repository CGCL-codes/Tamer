package dolf.objects.weapons.HeavyBall;

import javax.swing.ImageIcon;
import dolf.objects.ImageStore;
import dolf.objects.weapons.*;
import dolf.window.SystemMessagesHandler;
import dolf.game.Player;

public class HeavyBall implements Weapon, java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final int Price = 50;

    public static final String Name = SystemMessagesHandler.getInstance().getMessages("dolf.objects.weapons.HeavyBall.lang.lang").getString("name");

    public static final String WeaponType = SystemMessagesHandler.getInstance().getMessages("dolf.objects.weapons.HeavyBall.lang.lang").getString("type");

    public static final String Description = SystemMessagesHandler.getInstance().getMessages("dolf.objects.weapons.HeavyBall.lang.lang").getString("description");

    public static final javax.swing.ImageIcon Icon = ImageStore.getInstance().getIcon("dolf/objects/weapons/HeavyBall/gfx/icon.png");

    private double preWeigth;

    private final double WEIGHT = 20;

    private Player pl;

    private boolean inititalized = false;

    public HeavyBall() {
    }

    public void initialize(Player _pl) {
        if (this.inititalized) {
            return;
        }
        this.inititalized = true;
        this.pl = _pl;
        this.preWeigth = pl.getBall().getMass();
        pl.getBall().setMass(WEIGHT);
        pl.setWeaponState();
    }

    public void deInitialize() {
        pl.getBall().setMass(this.preWeigth);
        this.inititalized = false;
        this.pl = null;
    }

    public void execute() {
    }

    public ImageIcon getIcon() {
        return Icon;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getPrice() {
        return Price;
    }

    public String getType() {
        return WeaponType;
    }

    public void remove() {
        pl.getBall().setMass(this.preWeigth);
        pl.removeWeapon(this);
        this.pl.setAktWeapon(null);
    }

    public Player getPlayer() {
        return this.pl;
    }

    public double getState() {
        return 0;
    }
}
