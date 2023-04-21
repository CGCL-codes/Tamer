package com.wikiup.romulan.gom.imp.defence;

import java.util.ArrayList;
import java.util.List;
import com.wikiup.core.inf.DocumentInf;
import com.wikiup.core.inf.DocumentInitializationAware;
import com.wikiup.romulan.gom.imp.CombatUnit;
import com.wikiup.romulan.gom.inf.DamageInf;
import com.wikiup.romulan.gom.inf.DefenceUnitInf;
import com.wikiup.romulan.gom.inf.NamedModelInf;
import com.wikiup.util.InterfaceUtil;

public class Defence implements DefenceUnitInf, DocumentInitializationAware, NamedModelInf {

    private List<DefenceUnitInf> defences = new ArrayList<DefenceUnitInf>();

    public int getHitPoint() {
        return defences.get(defences.size() - 1).getHitPoint();
    }

    public void initialize(DocumentInf desc) {
        ShieldDefence shield = new ShieldDefence();
        ArmourDefence armour = new ArmourDefence();
        InterfaceUtil.initialize(shield, desc);
        InterfaceUtil.initialize(armour, desc);
        defences.add(shield);
        defences.add(armour);
    }

    public void hittedBy(CombatUnit hitPoint, DamageInf damage) {
        for (DefenceUnitInf defence : defences) defence.hittedBy(hitPoint, damage);
    }

    public String getName() {
        return "defence";
    }
}
