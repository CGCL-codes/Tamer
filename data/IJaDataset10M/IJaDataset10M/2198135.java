package com.launchcode.game.entities.ships;

import com.launchcode.game.system.Player;
import java.util.ArrayList;

/**
 * Creator: Kevin Hastie
 * Date: Feb 18, 2009
 * Time: 8:27:53 AM
 * Contents are property of Cappex.com LLC, and strictly confidential.
 */
public class ShipFighter extends Ship {

    public enum SubClass {

        Shrimp, Crab, Dolphin, Shark, Whale, Behemoth, Juggernaut, Leviathan
    }

    long hullStrength;

    long shieldStrength;

    Player owner;

    ArrayList<Weapon> guns = new ArrayList<Weapon>(2);

    ArrayList<Weapon> missiles = new ArrayList<Weapon>(2);

    /**
     *
     * @return currently hull + shields + gun & missile total dmg
     */
    public long getStrength() {
        long toReturn = hullStrength + shieldStrength;
        for (Weapon weapon : guns) {
            toReturn += weapon.subType.shieldDamage;
            toReturn += weapon.subType.hullDamage;
        }
        for (Weapon weapon : missiles) {
            toReturn += weapon.subType.shieldDamage;
            toReturn += weapon.subType.hullDamage;
        }
        return toReturn;
    }

    public long getHullStrength() {
        return hullStrength;
    }

    public void setHullStrength(long hullStrength) {
        this.hullStrength = hullStrength;
    }

    public long getShieldStrength() {
        return shieldStrength;
    }

    public void setShieldStrength(long shieldStrength) {
        this.shieldStrength = shieldStrength;
    }

    public ArrayList<Weapon> getGuns() {
        return guns;
    }

    public void setGuns(ArrayList<Weapon> guns) {
        this.guns = guns;
    }

    public ArrayList<Weapon> getMissiles() {
        return missiles;
    }

    public void setMissiles(ArrayList<Weapon> missiles) {
        this.missiles = missiles;
    }

    @Override
    public String toString() {
        return "ShipFighter{" + "hullStrength=" + hullStrength + ", shieldStrength=" + shieldStrength + ", guns=" + guns + ", missiles=" + missiles + "} " + super.toString();
    }
}
