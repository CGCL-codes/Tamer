package mars4stars;

import java.util.ArrayList;
import java.util.List;

/**
 * StackBattleStub is one of the main actors in the battle simulator. They are 
 * moved around on the Grid, shoot at each other, take damage and get killed etc. 
 *  
 * <br><br>Copyright (C) 2004 - Licenced under the GNU GPL
 * @author James McGuigan
 */
public class StackBattleStub {

    private int shieldDP;

    private int armour512ths = 512;

    private int shipCount;

    private GridSquare gridSquare;

    private List vValidTargets;

    private DesignBattleStub design;

    private BattleOrders battleOrders;

    private boolean canRegenShields;

    private boolean takenArmourDamage = false;

    private RaceInterface ownerRace;

    public StackBattleStub(int armour512ths, int shipCount, boolean canRegenShields, RaceInterface ownerRace, DesignBattleStub design, BattleOrders battleOrders) {
        this.armour512ths = armour512ths;
        this.shipCount = shipCount;
        this.canRegenShields = canRegenShields;
        this.ownerRace = ownerRace;
        this.design = design;
        this.battleOrders = battleOrders;
        this.shieldDP = getShieldMaxDP();
    }

    /**
	 * 
	 *
	 */
    public void InitalizeValidTarget() {
    }

    /**
	 * 
	 *
	 */
    public void regenShields() {
        if (this.canRegenShields && this.takenArmourDamage == false) {
            this.shieldDP = Math.max(this.getShieldMaxDP(), this.getShieldDP() + (this.getShieldMaxDP() / 10));
        }
    }

    /**
	 * 
	 * @param shot
	 * @return
	 */
    public boolean wouldKill(Shot shot) {
        return false;
    }

    /**
	 * 
	 * @param shot
	 */
    public void applyDamage(Shot shot) {
    }

    public void move(GridSquare to) {
        if (to == null) {
            Log.error(this, "StackBattleStub.move() moving to null square");
            return;
        }
        if (this.gridSquare == null) this.gridSquare = to;
        if (to.getFleets().contains(this) == true) {
            this.gridSquare = to;
        } else {
            this.gridSquare.moveStack(this, to);
        }
    }

    /**
		 * @return current stack armour (design armour * ship count * armour 512ths)
		 */
    public int getArmourDP() {
        return design.getArmour() * this.shipCount * this.armour512ths / 512;
    }

    /**
		 * @return the maximum undamaged ShieldDP.
		 */
    public int getArmourMaxDP() {
        return design.getArmour() * this.shipCount;
    }

    /**
		 * @return the current ShieldDP.
		 */
    public int getShieldDP() {
        return shieldDP;
    }

    /**
		 * @return the maximum undamaged ShieldDP.
		 */
    public int getShieldMaxDP() {
        return design.getShields() * this.shipCount;
    }

    /**
		 * @return true if Stack has taken ArmourDamage. 
		 */
    public boolean hasTakenArmourDamage() {
        return takenArmourDamage;
    }

    /**
		 * @return Returns the ownerRace.
		 */
    public RaceInterface getOwnerRace() {
        return ownerRace;
    }

    /**
		 * @return Returns the shipCount.
		 */
    public int getShipCount() {
        return shipCount;
    }

    /**
		 * @return Returns the vValidTarget.
		 */
    public List getValidTargets() {
        return vValidTargets;
    }

    /**
	     * @return Returns the gridSquare.
	     */
    public GridSquare getGridSquare() {
        return this.gridSquare;
    }

    /**
	     * @return Returns the battleOrders.
	     */
    public BattleOrders getBattleOrders() {
        return this.battleOrders;
    }

    /**
	     * @return Returns the canRegenShields.
	     */
    public boolean canCanRegenShields() {
        return this.canRegenShields;
    }

    /**
	     * @return Returns the design.
	     */
    public DesignBattleStub getDesign() {
        return this.design;
    }
}
