package megamek.common.weapons;

import megamek.common.TechConstants;

/**
 * @author Jason Tighe
 */
public class CLBPod extends BPodWeapon {

    /**
     * 
     */
    private static final long serialVersionUID = -950371259420885833L;

    /**
     * 
     */
    public CLBPod() {
        super();
        this.techLevel = TechConstants.T_CLAN_TW;
        this.name = "B-Pod";
        this.setInternalName("CLBPod");
        this.addLookupName("CLB-Pod");
    }
}
