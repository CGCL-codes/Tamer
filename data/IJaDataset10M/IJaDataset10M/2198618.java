package org.jcrpg.world.climate.conditions.light;

import org.jcrpg.world.climate.Condition;

public class Dark extends Condition {

    public static String DARK_ID = Dawn.class.getCanonicalName();

    public Dark(int weightPercentage) throws Exception {
        super(weightPercentage);
        ID = DARK_ID;
    }
}
