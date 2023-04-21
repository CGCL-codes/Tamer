package net.sourceforge.mpango.dto;

public class WeaponDTO extends BaseDTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6959755504908661439L;

    private Float attackBonus;

    public Float getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(Float attackBonus) {
        this.attackBonus = attackBonus;
    }
}
