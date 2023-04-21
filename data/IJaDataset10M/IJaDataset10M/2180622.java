package net.sourceforge.spacebutcher2.game.main.objects.bonus;

import net.sourceforge.spacebutcher2.game.main.objects.ProxyPlayerShip;
import net.sourceforge.spacebutcher2.game.main.objects.weapons.WeaponSystem;
import com.golden.gamedev.GameObject;

/**
 * Weapon bonus. It may only be set.
 *
 * @author M.Olszewski
 */
class WeaponBonus extends Bonus {

    private static final long serialVersionUID = -3404442020409456603L;

    private final WeaponSystem _weaponSystem;

    /**
   * Constructs {@link Bonus} object with specified values of
   * speed and damage in specified location.
   * 
   * @param bonusName bonus's name. 
   * @param bonusParent - parent of this {@link Bonus} object.
   * @param x - x location of projectile.
   * @param y - y location of projectile.
   * @param speed - speed of the bonus.
   * @param weaponSystem weapon system.
   */
    WeaponBonus(String bonusName, GameObject bonusParent, double x, double y, double speed, WeaponSystem weaponSystem) {
        super(bonusName, bonusParent, x, y, speed);
        _weaponSystem = weaponSystem;
    }

    /** 
   * @see net.sourceforge.spacebutcher2.game.main.objects.bonus.Bonus#updatePlayerShip(net.sourceforge.spacebutcher2.game.main.objects.ProxyPlayerShip)
   */
    @Override
    public void updatePlayerShip(ProxyPlayerShip player) {
        player.setWeapon(_weaponSystem);
    }
}
