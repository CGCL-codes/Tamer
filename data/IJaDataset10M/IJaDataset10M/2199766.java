package com.wikiup.romulan.gom.imp.ws;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.wikiup.romulan.gom.imp.CombatUnit;
import com.wikiup.romulan.gom.inf.DamageInf;
import com.wikiup.romulan.gom.inf.WeaponSystemInf;

public class NoneWeaponSystem implements WeaponSystemInf {

    public int getAttackPoint() {
        return 0;
    }

    public DamageInf fire() {
        return null;
    }

    public Collection<CombatUnit> lock(List<CombatUnit> units) {
        return Collections.EMPTY_LIST;
    }
}
