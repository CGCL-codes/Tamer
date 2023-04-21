package com.radroid.rts.types;

import java.io.File;
import org.kxml2.kdom.Element;
import com.radroid.rts.type_instances.Lang;

public class UpgradeSkillType extends SkillType {

    public UpgradeSkillType() {
        skillClass = SkillClass.upgrade;
    }

    public UpgradeSkillType(Element sn, File dir, TechTree techTree, FactionType factionType) {
        this();
        load(sn, dir, techTree, factionType);
    }

    public String toString() {
        return Lang.getInstance().get("Upgrade");
    }

    public int getTotalSpeed(TotalUpgrade totalUpgrade) {
        return speed + totalUpgrade.getProdSpeed();
    }
}
