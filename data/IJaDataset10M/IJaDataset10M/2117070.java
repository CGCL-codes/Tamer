package com.gencom.fun.ogame.model.defense;

import com.gencom.fun.ogame.model.Resources;
import com.gencom.fun.ogame.model.buildings.BuildingType;
import com.gencom.fun.ogame.model.requirements.IRequirement;
import com.gencom.fun.ogame.model.requirements.impl.BuildingRequirement;
import com.gencom.fun.ogame.model.requirements.impl.ResearchRequirement;
import com.gencom.fun.ogame.model.research.ResearchType;

public class LargeShieldDome extends BaseDefense {

    public static final IRequirement[] requirements = new IRequirement[] { new BuildingRequirement(BuildingType.SHIPYARD, 6), new ResearchRequirement(ResearchType.SHIELDING_TECHNOLOGY, 6) };

    public LargeShieldDome(int quantity) {
        super("Du", Resources.getResourcesValues(50000, 50000, 0), 10000, 1, quantity, requirements);
    }
}
