package pcgen.persistence.lst;

import pcgen.core.character.EquipSlot;

/**
 * <code>EquipSlotLstToken</code>
 *
 * @author  Devon Jones <soulcatcher@evilsoft.org>
 */
public interface EquipSlotLstToken extends LstToken {

    /**
	 * @param eqSlot
	 * @param value
	 * @return true if parse OK
	 */
    public abstract boolean parse(EquipSlot eqSlot, String value);
}
