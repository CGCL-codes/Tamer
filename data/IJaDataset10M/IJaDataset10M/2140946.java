package org.jcrpg.world.ai.abs.attribute;

public class FantasyResRatios extends AttributeRatios {

    public static String[] attributeName = null;

    static {
        attributeName = FantasyResistances.resistanceName;
    }

    public FantasyResRatios() {
        for (String a : attributeName) {
            attributeRatios.put(a, 1f);
        }
    }
}
