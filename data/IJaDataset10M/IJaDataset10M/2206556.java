package logic.items;

import logic.nodes.nodeSettings.upgrades.UpgradableSettings;

public class RepairItemProperties extends ItemProperties {

    private static final String REPAIR = "repairAmount";

    public RepairItemProperties(UpgradableSettings settings, ItemType type) {
        super(settings, type);
    }

    public int getRepairAmount() {
        return Integer.valueOf(getValueOf(REPAIR)).intValue();
    }
}
