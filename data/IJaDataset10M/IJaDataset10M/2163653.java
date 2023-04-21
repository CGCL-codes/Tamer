package mars4stars;

import java.util.*;

/**
 * This class implements the Shot object. When a weapon fires it should generate
 * a Shot object and query it rather than attempting to directly access 
 * StackBattleStub. This class also allows the individual shots in a battle to be 
 * saved and analysed.  
 *  
 * <br><br> Copyright (C) 2004 - Licenced under the GNU GPL 
 * @author James McGuigan
 */
public class Shot {

    private SlotBattleStub from;

    private StackBattleStub target;

    private int hits;

    private int misses;

    private int wholeShipKills;

    private int armourDamage;

    private int shieldDamage;

    /**
	 * @param from
	 * @param target
	 * @param hits
	 * @param misses
	 * @param wholeShipKills
	 * @param armourDamage
	 * @param shieldDamage
	 */
    public Shot(SlotBattleStub from, StackBattleStub target, int hits, int misses, int wholeShipKills, int armourDamage, int shieldDamage) {
        super();
        this.from = from;
        this.target = target;
        this.hits = hits;
        this.misses = misses;
        this.wholeShipKills = wholeShipKills;
        this.armourDamage = armourDamage;
        this.shieldDamage = shieldDamage;
    }
}
