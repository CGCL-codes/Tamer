package spaceopera.gui.objects.battle;

import java.awt.*;
import spaceopera.gui.objects.weapons.Torpedo;
import spaceopera.gui.objects.weapons.Weapon;

/**
 * this is the interface class that is used in FightDetail.checkDistanceAndFire
 * to connect to a ships torpedo-style weapons TODO: Probably rename this class
 * to something like 'torpedo-like-weapons-interface-class'?
 */
public class FightTorpedoWeapon extends FightObject {

    private int oldX = 0;

    private int oldY = 0;

    private int lifeCycles = 0;

    private Weapon weapon = null;

    private FightObject fightObject = null;

    public String playerName = "";

    public void useLifeCycle() {
        lifeCycles--;
    }

    public int getLifeCycles() {
        return (lifeCycles);
    }

    public Weapon getWeapon() {
        return (weapon);
    }

    public int getWeaponBattleSpeed() {
        return (weapon.getBattleSpeed());
    }

    public int getWeaponForce() {
        return (weapon.getForce());
    }

    public FightTorpedoWeapon(Object target, FightObject fo, Weapon w, int fx, int fy) {
        super();
        orderTarget = target;
        weapon = w;
        lifeCycles = weapon.getLifeCycles();
        fightObject = fo;
        x = fx;
        y = fy;
    }

    public void clear(Graphics g) {
        weapon.clear(g, x, y);
    }

    public void display(Graphics g) {
        ((Torpedo) weapon).display(g, x, y);
    }

    public void explode(Graphics g, int step) {
        weapon.explode(g, step, x, y);
    }

    public FightObject getFightObject() {
        return fightObject;
    }

    public int getTargetComputer() {
        return 0;
    }
}
