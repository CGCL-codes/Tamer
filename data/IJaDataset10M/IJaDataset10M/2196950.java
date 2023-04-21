package com.wwflgames.za.mob.attribute;

public class AllAttributes {

    public static final Attribute SLUGGER = new Attribute("Slugger", "Increase Head Smash 10%", "+10% Head Smash", null, Stat.BAT_HEAD_SMASH_CHANCE, 10);

    public static final Attribute SLUGGER2 = new Attribute("Slugger 2", "Increase Head Smash 10%", "+10% Head Smash", SLUGGER, Stat.BAT_HEAD_SMASH_CHANCE, 10);

    public static final Attribute SLUGGER3 = new Attribute("Slugger 3", "Increase Head Smash 10%", "+10% Head Smash", SLUGGER2, Stat.BAT_HEAD_SMASH_CHANCE, 10);

    public static final Attribute DUNDEE = new Attribute("Crocodile Dundee", "Increase Knife Damage by 2", "+2 Knife Dmg", null, Stat.KNIFE_SKILL, 2);

    public static final Attribute DUNDEE2 = new Attribute("Crocodile Dundee 2", "Increase Knife Damage by 1", "+1 Knife Dmg", DUNDEE, Stat.KNIFE_SKILL, 1);

    public static final Attribute MEDIC = new Attribute("Medic!", "Increase Bandage Healing by 4", "+4 Bandage Heal", null, Stat.BANDAGING_SKILL, 4);

    public static final Attribute MEDIC2 = new Attribute("Medic 2!", "Increase Bandage Healing by 4", "+4 Bandage Heal", MEDIC, Stat.BANDAGING_SKILL, 4);

    public static final Attribute SNIPER = new Attribute("Sniper", "Increase Rifle Headshot Chance by 10%", "+10% Rifle Headshot", null, Stat.RIFLE_HEADSHOT_CHANCE, 10);

    public static final Attribute MARKSMAN = new Attribute("Marksman", "Increase Pistol Headshot Change by 10%", "+10% Pistol Headhsot", null, Stat.PISTOL_HEADSHOT_CHANCE, 2);

    public static final Attribute FLEET_FOOTED = new Attribute("Fleet Footed", "Speed increased by 1", "+1 speed", null, Stat.SPEED, 1);

    public static final Attribute FLEET_FOOTED2 = new Attribute("Fleet Footed 2", "Speed increased by 1", "+1 speed", FLEET_FOOTED, Stat.SPEED, 1);

    public static final Attribute FLEET_FOOTED3 = new Attribute("Fleet Footed 3", "Speed increased by 1", "+1 speed", FLEET_FOOTED2, Stat.SPEED, 1);

    public static final Attribute HEALTHY_HORSE = new Attribute("Healthy as a Horse", "Max health increased by 5", "+5 Max Health", null, Stat.MAX_HEALTH, 5);

    public static final Attribute WOLVERINE = new Attribute("Wolverine", "Regen Health", "+1 Health Regen", null, Stat.HEALTH_REGEN, 1);

    public static final Attribute WOLVERINE2 = new Attribute("Wolverine 2", "Regen Health", "+1 Health Regen", WOLVERINE, Stat.HEALTH_REGEN, 1);

    public static final Attribute WOLVERINE3 = new Attribute("Wolverine 3", "Regen Health", "+1 Health Regen", WOLVERINE2, Stat.HEALTH_REGEN, 1);

    public static final Attribute[] ALL_ATTRIBUTES = { SLUGGER, SLUGGER2, SLUGGER3, DUNDEE, DUNDEE2, MEDIC, MEDIC2, SNIPER, MARKSMAN, FLEET_FOOTED, FLEET_FOOTED2, FLEET_FOOTED3, HEALTHY_HORSE, WOLVERINE, WOLVERINE2, WOLVERINE3 };
}
