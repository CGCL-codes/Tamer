package fr.fg.client.data;

public class WeaponData {

    public static final int AUTOCANON = 0, LASER = 1, PULSE_LASER = 2, TACTICAL_ROCKET = 3, TWIN_AUTOCANON = 4, INFERNO = 5, GAUSS = 6, SONIC_TORPEDO = 7, PPC = 8, BLASTER = 9;

    public static final WeaponData[] WEAPONS;

    static {
        WEAPONS = new WeaponData[20];
        WEAPONS[AUTOCANON] = new WeaponData(2, 3);
        WEAPONS[LASER] = new WeaponData(1, 5);
        WEAPONS[PULSE_LASER] = new WeaponData(3, 6);
        WEAPONS[TACTICAL_ROCKET] = new WeaponData(3, 8);
        WEAPONS[TWIN_AUTOCANON] = new WeaponData(4, 6);
        WEAPONS[INFERNO] = new WeaponData(2, 38);
        WEAPONS[GAUSS] = new WeaponData(16, 24);
        WEAPONS[SONIC_TORPEDO] = new WeaponData(5, 10);
        WEAPONS[PPC] = new WeaponData(0, 0);
        WEAPONS[BLASTER] = new WeaponData(0, 6);
    }

    private int damageMin;

    private int damageMax;

    public WeaponData(int damageMin, int damageMax) {
        this.damageMin = damageMin;
        this.damageMax = damageMax;
    }

    public int getDamageMin() {
        return damageMin;
    }

    public int getDamageMax() {
        return damageMax;
    }
}
