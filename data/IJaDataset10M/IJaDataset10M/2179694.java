package dolf.objects.weapons.DriveByRocket;

import javax.swing.ImageIcon;
import dolf.game.*;
import dolf.objects.weapons.*;
import dolf.objects.*;
import dolf.window.SystemMessagesHandler;

public class DriveByRocket implements Weapon, java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static final int Price = 150;

    public static final String Name = SystemMessagesHandler.getInstance().getMessages("dolf.objects.weapons.DriveByRocket.lang.lang").getString("name");

    ;

    public static final String WeaponType = SystemMessagesHandler.getInstance().getMessages("dolf.objects.weapons.DriveByRocket.lang.lang").getString("type");

    ;

    public static final String Description = SystemMessagesHandler.getInstance().getMessages("dolf.objects.weapons.DriveByRocket.lang.lang").getString("description");

    ;

    public static final javax.swing.ImageIcon Icon = ImageStore.getInstance().getIcon("dolf/objects/weapons/DriveByRocket/gfx/icon.png");

    private boolean launched = false;

    private Player pl;

    private boolean aimed;

    private boolean inititalized = false;

    private Point aim;

    private int timer;

    private AimObject aimObj;

    private boolean tricker;

    private Rocket rock;

    public DriveByRocket() {
        rock = new Rocket(null, null, null, null);
    }

    public void initialize(Player _pl) {
        if (this.inititalized) {
            return;
        }
        this.inititalized = true;
        this.launched = false;
        this.aimed = false;
        this.pl = _pl;
        this.pl.setWeaponState();
        this.timer = 10;
        this.tricker = false;
    }

    public void deInitialize() {
        this.inititalized = false;
        this.pl = null;
    }

    public void execute() {
        if (this.timer > 0) {
            timer--;
        }
        if (this.aimed && !Level.getInstance().mousePressed()) {
            this.tricker = true;
        }
        if (Level.getInstance().mousePressed() && pl.getBall().isMoving() && !this.aimed && timer == 0) {
            this.aim = Level.getInstance().getMouse();
            this.aimed = true;
            Level.getInstance().registerObjectOnTop(aimObj = new AimObject(aim));
            this.tricker = false;
        }
        if (Level.getInstance().mousePressed() && pl.getBall().isMoving() && this.aimed && this.tricker) {
            if (!this.launched) {
                this.rock.setPosition(pl.getBall().getPosition().sub(pl.getBall().getPosition().sub(Level.getInstance().getMouse()).setLength(pl.getBall().getRadius() + pl.getBall().getMotion().getLength() + 5)));
                this.rock.setMotion(Level.getInstance().getMouse().sub(pl.getBall().getPosition()));
                this.rock.setAim(this.aimObj);
                this.rock.setPlayer(this.pl);
                Level.getInstance().registerObjectOnTop(this.rock);
                this.launched = true;
                this.pl.setWeaponState();
                return;
            } else {
                System.err.println("out of Bullets!");
            }
        }
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

    public Player getPlayer() {
        return this.pl;
    }

    public void remove() {
        pl.removeWeapon(this);
        if (!this.launched) {
            Level.getInstance().removeObject(this.aimObj);
        }
        this.pl.setWeaponState();
        this.pl.setAktWeapon(null);
    }

    public double getState() {
        if (this.launched) {
            return 0;
        }
        return 1;
    }
}
