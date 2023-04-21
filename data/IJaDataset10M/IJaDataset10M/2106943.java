package com.customwars.client.io.loading;

import com.customwars.client.model.gameobject.City;
import com.customwars.client.model.gameobject.CityFactory;
import com.customwars.client.model.gameobject.Unit;
import com.customwars.client.model.gameobject.UnitFactory;
import com.customwars.client.model.gameobject.UnitFight;
import com.customwars.client.model.gameobject.UnitVsCityFight;
import com.customwars.client.tools.IOUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Load Damage values from an XML file (Idea from MiniWars)
 * The following format is used:
 * <damages>
 * <unit id="LTANK">
 * <enemies>
 * <enemy id="xxx" basedamage="1" altdamage="2" submerged="3"/>
 * </enemies>
 * </unit>
 * </damages>
 * <p/>
 * The enemy id can be a unit or a base city but not a terrain.
 * The enemy parameters base damage, alt damage and submerged damage
 * default to 0 and are optional.
 */
public class XMLDamageParser implements CWResourceLoader {

    private static final XStream xStream = new XStream(new DomDriver());

    private static final int NO_DAMAGE = 0;

    private final InputStream in;

    private final int[][] unitBaseDmgTables, unitAltDmgTables;

    private final int[][] cityBaseDmgTables, cityAltDmgTables;

    private final int[][] unitSubmergedDmgTable;

    private final int dmgChartSize;

    public XMLDamageParser(InputStream in) {
        this.in = in;
        int unitCount = UnitFactory.countUnits();
        int cityCount = CityFactory.countCities();
        dmgChartSize = unitCount + cityCount;
        unitBaseDmgTables = new int[dmgChartSize][dmgChartSize];
        unitAltDmgTables = new int[dmgChartSize][dmgChartSize];
        cityBaseDmgTables = new int[dmgChartSize][dmgChartSize];
        cityAltDmgTables = new int[dmgChartSize][dmgChartSize];
        unitSubmergedDmgTable = new int[dmgChartSize][dmgChartSize];
    }

    public void load() throws IOException {
        xStream.alias("damages", List.class);
        xStream.alias("unit", UnitDamage.class);
        xStream.useAttributeFor(UnitDamage.class, "id");
        xStream.alias("enemy", Enemy.class);
        xStream.useAttributeFor(Enemy.class, "id");
        xStream.useAttributeFor(Enemy.class, "basedamage");
        xStream.useAttributeFor(Enemy.class, "altdamage");
        xStream.useAttributeFor(Enemy.class, "submerged");
        try {
            @SuppressWarnings("unchecked") Collection<UnitDamage> damages = (Collection<UnitDamage>) xStream.fromXML(in);
            parseDamageList(damages);
            UnitFight.setBaseDMG(unitBaseDmgTables);
            UnitFight.setAltDMG(unitAltDmgTables);
            UnitFight.setSubmergedDMG(unitSubmergedDmgTable);
            UnitVsCityFight.setBaseDMG(cityBaseDmgTables);
            UnitVsCityFight.setAltDMG(cityAltDmgTables);
        } finally {
            IOUtil.closeStream(in);
        }
    }

    private void parseDamageList(Collection<UnitDamage> unitDamages) {
        for (UnitDamage unitDamage : unitDamages) {
            String unitName = unitDamage.id;
            int attackerID = UnitFactory.getUnit(unitName).getStats().getID();
            int enemyCount = unitDamage.enemies.size();
            if (enemyCount > dmgChartSize) {
                throw new RuntimeException(String.format("There are more enemies(%s) for %s(%s) then units+cities(%s)", enemyCount, unitName, attackerID, dmgChartSize));
            }
            for (int i = 0; i < unitDamage.enemies.size(); i++) {
                Enemy enemy = unitDamage.enemies.get(i);
                boolean isEnemyUnit = UnitFactory.hasUnitForName(enemy.id);
                boolean isEnemyCity = CityFactory.hasBaseCityForName(enemy.id);
                if (isEnemyUnit) {
                    readUnitDmgTable(attackerID, enemy);
                } else if (isEnemyCity) {
                    readCityDamageTable(attackerID, enemy);
                } else {
                    throw new IllegalArgumentException("the ID " + enemy.id + " is not a valid base city or unit name");
                }
            }
        }
    }

    private void readUnitDmgTable(int attackerID, Enemy enemy) {
        Unit unit = UnitFactory.getUnit(enemy.id);
        int defenderID = unit.getStats().getID();
        int baseDamage = enemy.basedamage;
        int altDamage = enemy.altdamage;
        int submergedDamage = enemy.submerged;
        if (baseDamage != NO_DAMAGE) {
            unitBaseDmgTables[attackerID][defenderID] = baseDamage;
        }
        if (altDamage != NO_DAMAGE) {
            unitAltDmgTables[attackerID][defenderID] = altDamage;
        }
        if (submergedDamage != NO_DAMAGE) {
            unitSubmergedDmgTable[attackerID][defenderID] = submergedDamage;
        }
    }

    private void readCityDamageTable(int attackerID, Enemy enemy) {
        City city = CityFactory.getBaseCity(enemy.id);
        int defenderID = city.getID();
        int baseDamage = enemy.basedamage;
        int altDamage = enemy.altdamage;
        if (baseDamage != NO_DAMAGE) {
            cityBaseDmgTables[attackerID][defenderID] = baseDamage;
        }
        if (altDamage != NO_DAMAGE) {
            cityAltDmgTables[attackerID][defenderID] = altDamage;
        }
    }

    /**
   * The damage a unit can do against a bunch of enemies
   */
    private static class UnitDamage {

        String id;

        List<Enemy> enemies = new ArrayList<Enemy>();
    }

    /**
   * An enemy of a unit, the id can represent a unit or a base city
   */
    private static class Enemy {

        String id;

        int basedamage;

        int altdamage;

        int submerged;
    }
}
