package zimoch.mordheim.unused;

public class Weapon {

    String name = null;

    String type = null;

    boolean singleHanded = false;

    boolean doubleHanded = false;

    boolean strikeFirst = false;

    boolean strikeLast = false;

    boolean canParry = false;

    boolean hasCriticalHit = false;

    boolean isHeavy = false;

    boolean isDifficultToUse = false;

    int wsMod = 0;

    int strMod = 0;

    int iniMod = 0;

    int criticalMod = 0;

    int enemyArmSv = 0;

    int stunnedMod = 0;

    int knockedDownMod = 0;

    int range = 0;

    int str = 0;

    int fireTwiceToHitMod = 0;

    boolean isMoveOrFire = false;

    boolean isFireTwiceAtHalfRange = false;

    boolean isThrownWeapon = false;

    boolean canFireTwice = false;

    boolean canShootInCloseCombat = false;

    boolean isStrAsUser = false;

    boolean isReloded = false;

    int toHitMod = 0;

    void setFist() {
        this.enemyArmSv = -1;
    }

    void setDagger() {
        this.enemyArmSv = -1;
        this.hasCriticalHit = false;
    }
}
