package personal.mhc.pardus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import personal.mhc.pardus.model.PriceGuide;
import personal.mhc.pardus.model.commodities.AnimalEmbryos;
import personal.mhc.pardus.model.commodities.BattleweaponsParts;
import personal.mhc.pardus.model.commodities.BioWaste;
import personal.mhc.pardus.model.commodities.BlueSapphireJewels;
import personal.mhc.pardus.model.commodities.ChemicalSupplies;
import personal.mhc.pardus.model.commodities.CommodityImpl;
import personal.mhc.pardus.model.commodities.CyberneticX993Parts;
import personal.mhc.pardus.model.commodities.DroidModules;
import personal.mhc.pardus.model.commodities.Drugs;
import personal.mhc.pardus.model.commodities.Electronics;
import personal.mhc.pardus.model.commodities.Energy;
import personal.mhc.pardus.model.commodities.ExoticCrystal;
import personal.mhc.pardus.model.commodities.ExoticMatter;
import personal.mhc.pardus.model.commodities.Explosives;
import personal.mhc.pardus.model.commodities.FactionPackage;
import personal.mhc.pardus.model.commodities.Food;
import personal.mhc.pardus.model.commodities.GemStones;
import personal.mhc.pardus.model.commodities.GoldenBerylJewels;
import personal.mhc.pardus.model.commodities.HandWeapons;
import personal.mhc.pardus.model.commodities.HeavyPlastics;
import personal.mhc.pardus.model.commodities.HumanIntestines;
import personal.mhc.pardus.model.commodities.HydrogenFuel;
import personal.mhc.pardus.model.commodities.KeldonBrains;
import personal.mhc.pardus.model.commodities.LeechBaby;
import personal.mhc.pardus.model.commodities.Liquor;
import personal.mhc.pardus.model.commodities.Medicines;
import personal.mhc.pardus.model.commodities.Metal;
import personal.mhc.pardus.model.commodities.MilitaryExplosives;
import personal.mhc.pardus.model.commodities.NebulaGas;
import personal.mhc.pardus.model.commodities.NeuralStimulator;
import personal.mhc.pardus.model.commodities.NeuralTissue;
import personal.mhc.pardus.model.commodities.NutrientClods;
import personal.mhc.pardus.model.commodities.OpticalComponents;
import personal.mhc.pardus.model.commodities.Ore;
import personal.mhc.pardus.model.commodities.Packages;
import personal.mhc.pardus.model.commodities.RadioactiveCells;
import personal.mhc.pardus.model.commodities.RashkirBones;
import personal.mhc.pardus.model.commodities.Robots;
import personal.mhc.pardus.model.commodities.RubyJewels;
import personal.mhc.pardus.model.commodities.SkaariLimbs;
import personal.mhc.pardus.model.commodities.Slaves;
import personal.mhc.pardus.model.commodities.StimChip;
import personal.mhc.pardus.model.commodities.VIP;
import personal.mhc.pardus.model.commodities.Water;
import personal.mhc.pardus.model.commodities.X993RepairDrone;
import personal.mhc.pardus.service.PriceGuideService;

/**
 * Controller for Pardus Tools.
 *
 * @author Mark Camp
 */
@Controller
public class PardusToolController {

    /** Price guide service used to create and maintain priceGuides. */
    PriceGuideService pgs = new PriceGuideService();

    /**
     * Mapping for updateCommodityPrices.
     *
     * It gets all commodities and updates the priceGuide.  This priceGuide is then stored.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return commodityPrices View to be displayed.
     */
    @RequestMapping("updateCommodityPrices")
    public String updateCommPrices(HttpServletRequest request, HttpServletResponse response) {
        PriceGuide priceGuide = pgs.newPriceGuide();
        CommodityImpl comm;
        for (int i = 1; i <= 44; i++) {
            switch(i) {
                case 1:
                    comm = new Food();
                    break;
                case 2:
                    comm = new Energy();
                    break;
                case 3:
                    comm = new Water();
                    break;
                case 4:
                    comm = new AnimalEmbryos();
                    break;
                case 5:
                    comm = new Ore();
                    break;
                case 6:
                    comm = new Metal();
                    break;
                case 7:
                    comm = new Electronics();
                    break;
                case 8:
                    comm = new Robots();
                    break;
                case 9:
                    comm = new HeavyPlastics();
                    break;
                case 10:
                    comm = new HandWeapons();
                    break;
                case 11:
                    comm = new BattleweaponsParts();
                    break;
                case 12:
                    comm = new DroidModules();
                    break;
                case 13:
                    comm = new RadioactiveCells();
                    break;
                case 14:
                    comm = new Medicines();
                    break;
                case 15:
                    comm = new NebulaGas();
                    break;
                case 16:
                    comm = new ChemicalSupplies();
                    break;
                case 17:
                    comm = new GemStones();
                    break;
                case 18:
                    comm = new OpticalComponents();
                    break;
                case 19:
                    comm = new Liquor();
                    break;
                case 20:
                    comm = new HydrogenFuel();
                    break;
                case 21:
                    comm = new ExoticMatter();
                    break;
                case 22:
                    comm = new BioWaste();
                    break;
                case 23:
                    comm = new Slaves();
                    break;
                case 24:
                    comm = new Drugs();
                    break;
                case 25:
                    comm = new NutrientClods();
                    break;
                case 26:
                    comm = new CyberneticX993Parts();
                    break;
                case 27:
                    comm = new ExoticCrystal();
                    break;
                case 28:
                    comm = new BlueSapphireJewels();
                    break;
                case 29:
                    comm = new RubyJewels();
                    break;
                case 30:
                    comm = new GoldenBerylJewels();
                    break;
                case 31:
                    comm = new NeuralTissue();
                    break;
                case 32:
                    comm = new StimChip();
                    break;
                case 33:
                    comm = new LeechBaby();
                    break;
                case 34:
                    comm = new MilitaryExplosives();
                    break;
                case 35:
                    comm = new HumanIntestines();
                    break;
                case 36:
                    comm = new SkaariLimbs();
                    break;
                case 37:
                    comm = new KeldonBrains();
                    break;
                case 38:
                    comm = new RashkirBones();
                    break;
                case 39:
                    comm = new Packages();
                    break;
                case 40:
                    comm = new FactionPackage();
                    break;
                case 41:
                    comm = new Explosives();
                    break;
                case 42:
                    comm = new VIP();
                    break;
                case 43:
                    comm = new NeuralStimulator();
                    break;
                case 44:
                    comm = new X993RepairDrone();
                    break;
                default:
                    comm = new Food();
                    break;
            }
            comm.setAmount(new Integer(request.getParameter(comm.getName() + "Amount")));
            comm.setMax(new Integer(request.getParameter(comm.getName() + "Max")));
            priceGuide = pgs.updatePriceGuide(priceGuide, comm);
        }
        request.setAttribute("priceGuide", priceGuide);
        return "commodityPrices";
    }

    /**
     * Mapping for commodityPrices.
     *
     * It gets and stores a new PriceGuide.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return commodityPrices View to be displayed.
     */
    @RequestMapping("commodityPrices")
    public String commPrices(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("priceGuide", pgs.newPriceGuide());
        return "commodityPrices";
    }

    /**
     * Mapping for index.
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return index View to be displayed.
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        return "index";
    }
}
