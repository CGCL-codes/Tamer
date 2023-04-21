package personal.mhc.pardus.model;

import personal.mhc.pardus.model.commodities.X993RepairDrone;
import personal.mhc.pardus.model.commodities.VIP;
import personal.mhc.pardus.model.commodities.StimChip;
import personal.mhc.pardus.model.commodities.NeuralTissue;
import personal.mhc.pardus.model.commodities.NeuralStimulator;
import personal.mhc.pardus.model.commodities.SkaariLimbs;
import personal.mhc.pardus.model.commodities.Packages;
import personal.mhc.pardus.model.commodities.MilitaryExplosives;
import personal.mhc.pardus.model.commodities.HumanIntestines;
import personal.mhc.pardus.model.commodities.LeechBaby;
import personal.mhc.pardus.model.commodities.FactionPackage;
import personal.mhc.pardus.model.commodities.Explosives;
import personal.mhc.pardus.model.commodities.ExoticCrystal;
import personal.mhc.pardus.model.commodities.Ore;
import personal.mhc.pardus.model.commodities.RadioactiveCells;
import personal.mhc.pardus.model.commodities.Robots;
import personal.mhc.pardus.model.commodities.RubyJewels;
import personal.mhc.pardus.model.commodities.Slaves;
import personal.mhc.pardus.model.commodities.Water;
import personal.mhc.pardus.model.commodities.OpticalComponents;
import personal.mhc.pardus.model.commodities.NutrientClods;
import personal.mhc.pardus.model.commodities.NebulaGas;
import personal.mhc.pardus.model.commodities.Metal;
import personal.mhc.pardus.model.commodities.Medicines;
import personal.mhc.pardus.model.commodities.Liquor;
import personal.mhc.pardus.model.commodities.HydrogenFuel;
import personal.mhc.pardus.model.commodities.HeavyPlastics;
import personal.mhc.pardus.model.commodities.HandWeapons;
import personal.mhc.pardus.model.commodities.GoldenBerylJewels;
import personal.mhc.pardus.model.commodities.GemStones;
import personal.mhc.pardus.model.commodities.Food;
import personal.mhc.pardus.model.commodities.ExoticMatter;
import personal.mhc.pardus.model.commodities.Energy;
import personal.mhc.pardus.model.commodities.Electronics;
import personal.mhc.pardus.model.commodities.Drugs;
import personal.mhc.pardus.model.commodities.DroidModules;
import personal.mhc.pardus.model.commodities.CyberneticX993Parts;
import personal.mhc.pardus.model.commodities.ChemicalSupplies;
import personal.mhc.pardus.model.commodities.BlueSapphireJewels;
import personal.mhc.pardus.model.commodities.BioWaste;
import personal.mhc.pardus.model.commodities.BattleweaponsParts;
import personal.mhc.pardus.model.commodities.AnimalEmbryos;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit Test class for PriceGuide.
 *
 * @author Mark Camp
 */
public class PriceGuideTest {

    /**
     * Default constructor.
     */
    public PriceGuideTest() {
    }

    /**
     * Test PriceGuide contains all necessary Commodities.
     */
    @Test
    public void testPriceGuide() {
        PriceGuide pg = new PriceGuide();
        assertTrue(pg.getAnimalEmbryos().getClass().equals(AnimalEmbryos.class));
        assertTrue(pg.getBattleweaponsParts().getClass().equals(BattleweaponsParts.class));
        assertTrue(pg.getBioWaste().getClass().equals(BioWaste.class));
        assertTrue(pg.getBlueSapphireJewels().getClass().equals(BlueSapphireJewels.class));
        assertTrue(pg.getChemicalSupplies().getClass().equals(ChemicalSupplies.class));
        assertTrue(pg.getCyberneticX993Parts().getClass().equals(CyberneticX993Parts.class));
        assertTrue(pg.getDroidModules().getClass().equals(DroidModules.class));
        assertTrue(pg.getDrugs().getClass().equals(Drugs.class));
        assertTrue(pg.getElectronics().getClass().equals(Electronics.class));
        assertTrue(pg.getEnergy().getClass().equals(Energy.class));
        assertTrue(pg.getExoticCrystal().getClass().equals(ExoticCrystal.class));
        assertTrue(pg.getExoticMatter().getClass().equals(ExoticMatter.class));
        assertTrue(pg.getExplosives().getClass().equals(Explosives.class));
        assertTrue(pg.getFactionPackage().getClass().equals(FactionPackage.class));
        assertTrue(pg.getFood().getClass().equals(Food.class));
        assertTrue(pg.getGemStones().getClass().equals(GemStones.class));
        assertTrue(pg.getGoldenBerylJewels().getClass().equals(GoldenBerylJewels.class));
        assertTrue(pg.getHandWeapons().getClass().equals(HandWeapons.class));
        assertTrue(pg.getHeavyPlastics().getClass().equals(HeavyPlastics.class));
        assertTrue(pg.getHumanIntestines().getClass().equals(HumanIntestines.class));
        assertTrue(pg.getHydrogenFuel().getClass().equals(HydrogenFuel.class));
        assertTrue(pg.getLeechBaby().getClass().equals(LeechBaby.class));
        assertTrue(pg.getLiquor().getClass().equals(Liquor.class));
        assertTrue(pg.getMedicines().getClass().equals(Medicines.class));
        assertTrue(pg.getMetal().getClass().equals(Metal.class));
        assertTrue(pg.getMilitaryExplosives().getClass().equals(MilitaryExplosives.class));
        assertTrue(pg.getNebulaGas().getClass().equals(NebulaGas.class));
        assertTrue(pg.getNeuralStimulator().getClass().equals(NeuralStimulator.class));
        assertTrue(pg.getNeuralTissue().getClass().equals(NeuralTissue.class));
        assertTrue(pg.getNutrientClods().getClass().equals(NutrientClods.class));
        assertTrue(pg.getOpticalComponents().getClass().equals(OpticalComponents.class));
        assertTrue(pg.getOre().getClass().equals(Ore.class));
        assertTrue(pg.getPackages().getClass().equals(Packages.class));
        assertTrue(pg.getRadioactiveCells().getClass().equals(RadioactiveCells.class));
        assertTrue(pg.getRobots().getClass().equals(Robots.class));
        assertTrue(pg.getRubyJewels().getClass().equals(RubyJewels.class));
        assertTrue(pg.getSkaariLimbs().getClass().equals(SkaariLimbs.class));
        assertTrue(pg.getSlaves().getClass().equals(Slaves.class));
        assertTrue(pg.getStimChip().getClass().equals(StimChip.class));
        assertTrue(pg.getVIP().getClass().equals(VIP.class));
        assertTrue(pg.getWater().getClass().equals(Water.class));
        assertTrue(pg.getX993RepairDrone().getClass().equals(X993RepairDrone.class));
    }
}
