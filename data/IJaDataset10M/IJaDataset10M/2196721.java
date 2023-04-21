package net.slashie.expedition.domain;

import net.slashie.expedition.item.StorageType;
import net.slashie.utils.roll.Roll;

public class Armor extends ExpeditionItem {

    private static final long serialVersionUID = 1L;

    public enum ArmorType {

        LIGHT, METAL_HEAVY;

        public String getAppearanceId() {
            switch(this) {
                case LIGHT:
                    return "STUDDED_VEST";
                case METAL_HEAVY:
                    return "BREASTPLATE";
            }
            return null;
        }
    }

    private int burden;

    private Roll defense;

    private String shortDescription;

    private ArmorType armorType;

    public int getBurden() {
        return burden;
    }

    public Roll getDefense() {
        return defense;
    }

    public ArmorType getArmorType() {
        return armorType;
    }

    public Armor(ArmorType armorType, String classifierId, String description, String pluralDescription, String longDescription, int weight, int burden, Roll defense, String shortDescription, int palosStoreValue, int baseTradingValue) {
        super(classifierId, description, pluralDescription, longDescription, classifierId, weight, GoodType.ARMORY, palosStoreValue, baseTradingValue, StorageType.WAREHOUSE);
        this.armorType = armorType;
        this.burden = burden;
        this.defense = defense;
        this.shortDescription = shortDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
