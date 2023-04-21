package server.campaign.pilot.skills;

import megamek.common.AmmoType;
import megamek.common.Entity;
import megamek.common.Mounted;
import server.campaign.CampaignMain;
import server.campaign.SHouse;
import server.campaign.pilot.SPilot;
import common.MegaMekPilotOption;
import common.Unit;
import common.campaign.pilot.Pilot;
import common.campaign.pilot.skills.PilotSkill;

/**
 * NOTE: This is a unofficial rule. A pilot with this skill receives only 1 pilot hit from ammunition explosions.
 * @@author Torren (Jason Tighe)
 */
public class IronManSkill extends SPilotSkill {

    public IronManSkill(int id) {
        super(id, "Iron Man", "IM");
        this.setDescription("NOTE: This is a unofficial rule. A pilot with this skill receives only 1 pilot hit from ammunition explosions.");
    }

    public IronManSkill() {
    }

    @Override
    public int getChance(int unitType, Pilot pilot) {
        if (pilot.getSkills().has(this)) return 0;
        if (unitType != Unit.MEK) return 0;
        String chance = "chancefor" + this.getAbbreviation() + "for" + Unit.getTypeClassDesc(unitType);
        SHouse house = CampaignMain.cm.getHouseFromPartialString(pilot.getCurrentFaction());
        if (house == null) return CampaignMain.cm.getIntegerConfig(chance);
        return house.getIntegerConfig(chance);
    }

    @Override
    public void modifyPilot(Pilot pilot) {
        pilot.addMegamekOption(new MegaMekPilotOption("iron_man", true));
    }

    @Override
    public int getBVMod(Entity unit) {
        return 0;
    }

    @Override
    public int getBVMod(Entity unit, SPilot pilot) {
        int amountOfAmmo = 0;
        int IronManBVBaseMod = CampaignMain.cm.getIntegerConfig("IronManBaseBVMod");
        if (pilot.getSkills().has(PilotSkill.PainResistanceSkillID)) return 0;
        for (Mounted ammoType : unit.getAmmo()) {
            if (ammoType.getShotsLeft() <= 0) continue;
            AmmoType ammo = (AmmoType) ammoType.getType();
            if (ammo.getAmmoType() == AmmoType.T_GAUSS || ammo.getAmmoType() == AmmoType.T_GAUSS_HEAVY || ammo.getAmmoType() == AmmoType.T_GAUSS_LIGHT) continue;
            amountOfAmmo++;
        }
        for (Mounted weapon : unit.getWeaponList()) {
            if (weapon.getName().indexOf("Gauss Rifle") != -1) amountOfAmmo++;
        }
        return amountOfAmmo * IronManBVBaseMod;
    }
}
