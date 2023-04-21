package pck_tap.beerxml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import pck_tap.alg.Util;
import pck_tap.alg.VectorElement;
import pck_tap.beerxml.recipe.Fermentable;
import pck_tap.beerxml.recipe.Fermentation;
import pck_tap.beerxml.recipe.Fermentation_Step;
import pck_tap.beerxml.recipe.Hop;
import pck_tap.beerxml.recipe.Mash;
import pck_tap.beerxml.recipe.Mash_Step;
import pck_tap.beerxml.recipe.Misc;
import pck_tap.beerxml.recipe.Water;
import pck_tap.beerxml.recipe.Yeast;

public class XmlLoaderStax {

    private Boolean debug = false;

    private BeerXml beerXml;

    private Vector vectorPath = new Vector();

    private int vHopIndex;

    private Vector vectorHops = new Vector();

    private int vFermentableIndex;

    private Vector vectorFermentables = new Vector();

    private int vMiscIndex;

    private Vector vectorMiscs = new Vector();

    private int vYeastIndex;

    private Vector vectorYeasts = new Vector();

    private int vWaterIndex;

    private Vector vectorWaters = new Vector();

    private int vMashIndex;

    private Vector vectorMashs = new Vector();

    private int vMashStepsIndex;

    private Vector vectorMashSteps = new Vector();

    private int vFermentationIndex;

    private int vFermentationStepsIndex;

    private Vector vectorFermentations = new Vector();

    private Vector vectorFermentationSteps = new Vector();

    private String Message = "";

    public XmlLoaderStax() {
    }

    private void initVars() {
        vectorPath = new Vector();
        vHopIndex = 0;
        vectorHops = new Vector();
        vFermentableIndex = 0;
        vectorFermentables = new Vector();
        vMiscIndex = 0;
        vectorMiscs = new Vector();
        vYeastIndex = 0;
        vectorYeasts = new Vector();
        vWaterIndex = 0;
        vectorWaters = new Vector();
        vMashIndex = 0;
        vectorMashs = new Vector();
        vMashStepsIndex = 0;
        vectorMashSteps = new Vector();
        vFermentationIndex = 0;
        vFermentationStepsIndex = 0;
        vectorFermentationSteps = new Vector();
    }

    public boolean LoadXml(String pFilename, int pRecipeNo, BeerXml beerXml) {
        try {
            initVars();
            this.beerXml = beerXml;
            stax(pFilename, pRecipeNo);
            beerXml.getRecipe().setTapFileName(pFilename);
        } catch (XMLStreamException e) {
            e.printStackTrace();
            this.Message = e.getMessage();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.Message = e.getMessage();
            return false;
        }
        return true;
    }

    private void stax(String pFilename, int pRecipeNo) throws XMLStreamException, FileNotFoundException {
        String filename = pFilename;
        XMLInputFactory vXMLInputFactory = XMLInputFactory.newInstance();
        XMLStreamReader reader = vXMLInputFactory.createXMLStreamReader(filename, new FileInputStream(filename));
        int vRecipeIndex = 0;
        vHopIndex = 0;
        vFermentableIndex = 0;
        vMiscIndex = 0;
        vYeastIndex = 0;
        vWaterIndex = 0;
        vMashIndex = 0;
        vFermentationIndex = 0;
        int vDepth = 0;
        String vElementName = "";
        String vElementValueBuffer = "";
        boolean RecipeFragment = false;
        while (reader.hasNext()) {
            int eventCode = reader.next();
            switch(eventCode) {
                case XMLStreamConstants.START_ELEMENT:
                    vDepth++;
                    vectorPath.addElement(reader.getLocalName());
                    if (isRecipe(reader.getLocalName())) {
                        vRecipeIndex++;
                        RecipeFragment = true;
                        break;
                    } else if (isHop(reader.getLocalName())) {
                        vHopIndex++;
                    } else if (isFermentable(reader.getLocalName())) {
                        vFermentableIndex++;
                    } else if (isMisc(reader.getLocalName())) {
                        vMiscIndex++;
                    } else if (isYeast(reader.getLocalName())) {
                        vYeastIndex++;
                    } else if (isWater(reader.getLocalName())) {
                        vWaterIndex++;
                    } else if (isMash(reader.getLocalName())) {
                        vMashIndex++;
                    } else if (isMashStep(reader.getLocalName())) {
                        vMashStepsIndex++;
                    } else if (isFermentation(reader.getLocalName())) {
                        vFermentationIndex++;
                    } else if (isFermentationStep(reader.getLocalName())) {
                        vFermentationStepsIndex++;
                    }
                    if (RecipeFragment) {
                        vElementName = reader.getLocalName();
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (vElementName.equalsIgnoreCase("NOTES")) {
                        vElementValueBuffer = vElementValueBuffer + reader.getText();
                    } else {
                        if (!Util.isNull(Util.nin(Util.ltrim(reader.getText()).trim()))) {
                            vElementValueBuffer = vElementValueBuffer + Util.ltrim(reader.getText()).trim();
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    doPrintELement(vElementName, vElementValueBuffer, vRecipeIndex, vDepth, pRecipeNo);
                    vElementValueBuffer = "";
                    vDepth--;
                    if (isRecipe(reader.getLocalName())) {
                        RecipeFragment = false;
                    }
                    vectorPath.removeElementAt(vectorPath.size() - 1);
                    break;
            }
        }
        this.doHopPostActions();
        this.doFermentablePostActions();
        this.doMiscPostActions();
        this.doYeastPostActions();
        this.doWaterPostActions();
        this.doMashPostActions();
        this.doMashStepsPostActions();
        this.doFermentationPostActions();
        this.doFermentationStepsPostActions();
        reader.close();
    }

    private void doPrintELement(String vElementName, String pElementValue, int inRecipeNo, int vDepth, int intRecipeNoSelected) {
        if (pElementValue.trim().length() > 0 && inRecipeNo == intRecipeNoSelected && vDepth == 3) {
            String vStringElementValue = pElementValue;
            doRecipeSpecifics(vElementName, vStringElementValue);
        } else if (pElementValue.trim().length() > 0 && inRecipeNo == intRecipeNoSelected) {
            String vStringPath = "";
            for (int i = 0; i < this.vectorPath.size() - 1; i++) {
                vStringPath = vStringPath + (String) vectorPath.get(i) + "/";
            }
            String vStringElementValue = pElementValue.replace("\n", "").trim();
            if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/STYLE/")) {
                doStyle(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/EQUIPMENT/")) {
                doEquipment(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/HOPS/HOP/")) {
                doHop(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/FERMENTABLES/FERMENTABLE/")) {
                doFermentable(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/MISCS/MISC/")) {
                doMisc(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/YEASTS/YEAST/")) {
                doYeast(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/WATERS/WATER/")) {
                doWater(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/MASH/")) {
                doMash(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/MASH/MASH_STEPS/MASH_STEP/")) {
                doMashSteps(vElementName, vStringElementValue, vMashIndex);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/FERMENTATION/")) {
                doFermentation(vElementName, vStringElementValue);
            } else if (vStringPath.equalsIgnoreCase("RECIPES/RECIPE/FERMENTATION/FERMENTATION_STEPS/FERMENTATION_STEP/")) {
                doFermentationSteps(vElementName, vStringElementValue, vMashIndex);
            } else {
            }
        }
    }

    private static boolean isRecipe(String name) {
        if (name.equalsIgnoreCase("RECIPE")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isHop(String name) {
        if (name.equalsIgnoreCase("HOP")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isMisc(String name) {
        if (name.equalsIgnoreCase("MISC")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isFermentable(String name) {
        if (name.equalsIgnoreCase("FERMENTABLE")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isYeast(String name) {
        if (name.equalsIgnoreCase("YEAST")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isWater(String name) {
        if (name.equalsIgnoreCase("WATER")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isMash(String name) {
        if (name.equalsIgnoreCase("MASH")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isMashStep(String name) {
        if (name.equalsIgnoreCase("MASH_STEP")) {
            return true;
        } else {
            return false;
        }
    }

    private void doRecipeSpecifics(String vElementName, String vStringElementValue) {
        if (vElementName.equalsIgnoreCase("NAME")) {
            this.beerXml.getRecipe().setName(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("VERSION")) {
            this.beerXml.getRecipe().setVersion(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TAP_NUMBER")) {
            this.beerXml.getRecipe().setTapNumber(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TYPE")) {
            this.beerXml.getRecipe().setType(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BREWER")) {
            this.beerXml.getRecipe().setBrewer(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("ASST_BREWER")) {
            this.beerXml.getRecipe().setAsst_Brewer(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BATCH_SIZE")) {
            this.beerXml.getRecipe().setBatch_Size(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BOIL_SIZE")) {
            this.beerXml.getRecipe().setBoil_Size(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BOIL_TIME")) {
            this.beerXml.getRecipe().setBoil_Time(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EFFICIENCY")) {
            this.beerXml.getRecipe().setEfficiency(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("NOTES")) {
            this.beerXml.getRecipe().setNotes(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TASTE_NOTES")) {
            this.beerXml.getRecipe().setTaste_Notes(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TASTE_RATING")) {
            this.beerXml.getRecipe().setTaste_Rating(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("OG")) {
            this.beerXml.getRecipe().setOg(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("FG")) {
            this.beerXml.getRecipe().setFg(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("FERMENTATION_STAGES")) {
            this.beerXml.getRecipe().setFermentation_Stages(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("PRIMARY_AGE")) {
            this.beerXml.getRecipe().setPrimary_Age(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("PRIMARY_TEMP")) {
            this.beerXml.getRecipe().setPrimary_Temp(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("SECONDARY_AGE")) {
            this.beerXml.getRecipe().setSecondary_Age(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("SECONDARY_TEMP")) {
            this.beerXml.getRecipe().setSecondary_Temp(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TERTIARY_AGE")) {
            this.beerXml.getRecipe().setTertiary_Age(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TERTIARY_TEMP")) {
            this.beerXml.getRecipe().setTertiary_Temp(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("AGE")) {
            this.beerXml.getRecipe().setAge(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("AGE_TEMP")) {
            this.beerXml.getRecipe().setAge_Temp(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DATE")) {
            this.beerXml.getRecipe().setDate(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CARBONATION")) {
            this.beerXml.getRecipe().setCarbonation(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("FORCED_CARBONATION")) {
            this.beerXml.getRecipe().setForced_Carbonation(Util.str2bln(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("PRIMING_SUGAR_NAME")) {
            this.beerXml.getRecipe().setPriming_Sugar_Name(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CARBONATION_TEMP")) {
            this.beerXml.getRecipe().setCarbonation_Temp(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("PRIMING_SUGAR_EQUIV")) {
            this.beerXml.getRecipe().setPriming_Sugar_Equiv(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("KEG_PRIMING_FACTOR")) {
            this.beerXml.getRecipe().setKeg_Priming_Factor(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EST_OG")) {
            this.beerXml.getRecipe().setEst_Og(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EST_FG")) {
            this.beerXml.getRecipe().setEst_Fg(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EST_COLOR")) {
            this.beerXml.getRecipe().setEst_Color(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("IBU")) {
            this.beerXml.getRecipe().setIbu(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("IBU_METHOD")) {
            this.beerXml.getRecipe().setIbu_Method(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EST_ABV")) {
            this.beerXml.getRecipe().setEst_Abv(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("ABV")) {
            this.beerXml.getRecipe().setAbv(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("ACTUAL_EFFICIENCY")) {
            this.beerXml.getRecipe().setActual_Efficiency(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CALORIES")) {
            this.beerXml.getRecipe().setCalories(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_BATCH_SIZE")) {
            this.beerXml.getRecipe().setDisplay_Batch_Size(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_BOIL_SIZE")) {
            this.beerXml.getRecipe().setDisplay_Boil_Size(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_OG")) {
            this.beerXml.getRecipe().setDisplay_Og(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_FG")) {
            this.beerXml.getRecipe().setDisplay_Fg(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_PRIMARY_TEMP")) {
            this.beerXml.getRecipe().setDisplay_Primary_Temp(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_SECONDARY_TEMP")) {
            this.beerXml.getRecipe().setDisplay_Secondary_Temp(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_TERTIARY_TEMP")) {
            this.beerXml.getRecipe().setDisplay_Tertiary_Temp(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_AGE_TEMP")) {
            this.beerXml.getRecipe().setDisplay_Age_Temp(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CARBONATION_USED")) {
            this.beerXml.getRecipe().setCarbonation_Used(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("DISPLAY_CARB_TEMP")) {
            this.beerXml.getRecipe().setDisplay_Carb_Temp(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("PLANNED_OG")) {
            this.beerXml.getRecipe().setOg(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CALCULATED_IBU")) {
            this.beerXml.getRecipe().setIbu(Util.str2dbl(vStringElementValue));
        }
    }

    private void doStyle(String vElementName, String vStringElementValue) {
        if (vElementName.equalsIgnoreCase("NAME")) {
            this.beerXml.getRecipe().getStyle().setName(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CATEGORY")) {
            this.beerXml.getRecipe().getStyle().setCategory(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("VERSION")) {
            this.beerXml.getRecipe().getStyle().setVersion(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CATEGORY_NUMBER")) {
            this.beerXml.getRecipe().getStyle().setCategory_Number(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("STYLE_LETTER")) {
            this.beerXml.getRecipe().getStyle().setStyle_Letter(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("STYLE_GUIDE")) {
            this.beerXml.getRecipe().getStyle().setStyle_Guide(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TYPE")) {
            String Domain = "LagerAleMeadWheatMixedCider";
            int i = Domain.indexOf(vStringElementValue);
            if (i < 0) {
                this.beerXml.getRecipe().getStyle().setType("Ale");
            } else {
                this.beerXml.getRecipe().getStyle().setType(Util.str2str(vStringElementValue));
            }
        } else if (vElementName.equalsIgnoreCase("OG_MIN")) {
            this.beerXml.getRecipe().getStyle().setOg_Min(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("OG_MAX")) {
            this.beerXml.getRecipe().getStyle().setOg_Max(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("FG_MIN")) {
            this.beerXml.getRecipe().getStyle().setFg_Min(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("FG_MAX")) {
            this.beerXml.getRecipe().getStyle().setFg_Max(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("IBU_MIN")) {
            this.beerXml.getRecipe().getStyle().setIbu_Min(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("IBU_MAX")) {
            this.beerXml.getRecipe().getStyle().setIbu_Max(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("COLOR_MIN")) {
            this.beerXml.getRecipe().getStyle().setColor_Min(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("COLOR_MAX")) {
            this.beerXml.getRecipe().getStyle().setColor_Max(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CARB_MIN")) {
            this.beerXml.getRecipe().getStyle().setCarb_Min(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CARB_MAX")) {
            this.beerXml.getRecipe().getStyle().setCarb_Max(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("ABV_MIN")) {
            this.beerXml.getRecipe().getStyle().setAbv_Min(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("ABV_MAX")) {
            this.beerXml.getRecipe().getStyle().setAbv_Max(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("NOTES")) {
            this.beerXml.getRecipe().getStyle().setNotes(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("PROFILE")) {
            this.beerXml.getRecipe().getStyle().setProfile(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("INGREDIENTS")) {
            this.beerXml.getRecipe().getStyle().setIngredients(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EXAMPLES")) {
            this.beerXml.getRecipe().getStyle().setExamples(Util.str2str(vStringElementValue));
        }
    }

    private void doEquipment(String vElementName, String vStringElementValue) {
        if (vElementName.equalsIgnoreCase("NAME")) {
            this.beerXml.getRecipe().getEquipment().setName(Util.str2str(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("VERSION")) {
            this.beerXml.getRecipe().getEquipment().setVersion(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BOIL_SIZE")) {
            this.beerXml.getRecipe().getEquipment().setBoil_Size(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BATCH_SIZE")) {
            this.beerXml.getRecipe().getEquipment().setBatch_Size(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TUN_VOLUME")) {
            this.beerXml.getRecipe().getEquipment().setTun_Volume(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TUN_WEIGHT")) {
            this.beerXml.getRecipe().getEquipment().setTun_Weight(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TUN_SPECIFIC_HEAT")) {
            this.beerXml.getRecipe().getEquipment().setTun_Specific_Heat(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TOP_UP_WATER")) {
            this.beerXml.getRecipe().getEquipment().setTop_Up_Water(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TRUB_CHILLER_LOSS")) {
            this.beerXml.getRecipe().getEquipment().setTrub_Chiller_Loss(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("EVAP_RATE")) {
            this.beerXml.getRecipe().getEquipment().setEvap_Rate(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("BOIL_TIME")) {
            this.beerXml.getRecipe().getEquipment().setBoil_Time(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("CALC_BOIL_VOLUME")) {
            this.beerXml.getRecipe().getEquipment().setCalc_Boil_Volume(Util.str2bln(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("LAUTER_DEADSPACE")) {
            this.beerXml.getRecipe().getEquipment().setLauter_Deadspace(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("TOP_UP_KETTLE")) {
            this.beerXml.getRecipe().getEquipment().setTop_Up_Kettle(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("HOP_UTILIZATION")) {
            this.beerXml.getRecipe().getEquipment().setHop_Utilization(Util.str2dbl(vStringElementValue));
        } else if (vElementName.equalsIgnoreCase("NOTES")) {
            this.beerXml.getRecipe().getEquipment().setNotes(Util.str2str(vStringElementValue));
        }
    }

    private void doHop(String vElementName, String vStringElementValue) {
        vectorHops.addElement(new VectorElement(vHopIndex, vStringElementValue, vElementName));
    }

    private void doHopPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Hop vHop = new Hop();
        ;
        Vector vHopVector = new Vector();
        for (int y = 0; y < vectorHops.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorHops.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vHopVector.addElement(vHop);
                }
                vHop = new Hop();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vHop.setName(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vHop.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("ALPHA")) {
                vHop.setAlpha(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT")) {
                vHop.setAmount(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("USE")) {
                vHop.setUse(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("TIME")) {
                vHop.setTime(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
                vHop.setNotes(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("TYPE")) {
                vHop.setType(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("FORM")) {
                vHop.setForm(Util.str2str(vValue.replace("Bloem", "Leaf")));
            } else if (vProperty.equalsIgnoreCase("BETA")) {
                vHop.setBeta(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("HSI")) {
                vHop.setHsi(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("ORIGIN")) {
                vHop.setOrigin(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("SUBSTITUTES")) {
                vHop.setSubstitutes(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("HUMULENE")) {
                vHop.setHumulene(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("CARYOPHYLLENE")) {
                vHop.setCaryophyllene(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("COHUMULONE")) {
                vHop.setCohumulone(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("MYRCENE")) {
                vHop.setMyrcene(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TAP_HOPBAG")) {
                vHop.setTap_HopBag(Util.str2bln(vValue));
            } else if (vProperty.equalsIgnoreCase("TAP_IBU")) {
                vHop.setTap_Ibu(Util.str2dbl(vValue));
            }
        }
        vHopVector.addElement(vHop);
        for (int i = 0; i < vHopVector.size(); i++) {
            beerXml.getRecipe().getHops().addElement(vHopVector.get(i));
        }
    }

    private void doFermentable(String vElementName, String vStringElementValue) {
        vectorFermentables.addElement(new VectorElement(vFermentableIndex, vStringElementValue, vElementName));
    }

    private void doFermentablePostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Fermentable vFermentable = new Fermentable();
        ;
        Vector vFermentableVector = new Vector();
        for (int y = 0; y < vectorFermentables.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorFermentables.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vFermentableVector.addElement(vFermentable);
                }
                vFermentable = new Fermentable();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                String s = Util.str2str(vValue);
                if (s.substring(0, 1).equalsIgnoreCase("_")) {
                    s = s.substring(1);
                }
                vFermentable.setName(s);
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vFermentable.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TYPE")) {
                vFermentable.setType(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT")) {
                vFermentable.setAmount(Util.str2dbl(vValue));
                vFermentable.setTap_Orginal_Amounts(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("YIELD")) {
                vFermentable.setYield(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("COLOR")) {
                vFermentable.setColor(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("ADD_AFTER_BOIL")) {
                vFermentable.setAdd_After_Boil(Util.str2bln(vValue));
            } else if (vProperty.equalsIgnoreCase("ORIGIN")) {
                vFermentable.setOrigin(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("SUPPLIER")) {
                vFermentable.setSupplier(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
                vFermentable.setNotes(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("COARSE_FINE_DIFF")) {
                vFermentable.setCoarse_Fine_Diff(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("MOISTURE")) {
                vFermentable.setMoisture(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("DIASTATIC_POWER")) {
                vFermentable.setDiastatic_Power(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("PROTEIN")) {
                vFermentable.setProtein(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("MAX_IN_BATCH")) {
                vFermentable.setMax_In_Batch(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("RECOMMEND_MASH")) {
                vFermentable.setRecommend_Mash(Util.str2bln(vValue));
            } else if (vProperty.equalsIgnoreCase("IBU_GAL_PER_LB")) {
                vFermentable.setIbu_Gal_Per_Lb(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TAP_PERCENT")) {
                vFermentable.setTap_Percent(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("POTENTIAL")) {
                vFermentable.setPotential(Util.nin(Util.str2dbl(vValue)));
            }
        }
        vFermentableVector.addElement(vFermentable);
        for (int i = 0; i < vFermentableVector.size(); i++) {
            beerXml.getRecipe().getFermentables().addElement(vFermentableVector.get(i));
        }
    }

    private void doMisc(String vElementName, String vStringElementValue) {
        vectorMiscs.addElement(new VectorElement(vMiscIndex, vStringElementValue, vElementName));
    }

    private void doMiscPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Misc vMisc = new Misc();
        ;
        Vector vMiscVector = new Vector();
        for (int y = 0; y < vectorMiscs.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorMiscs.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vMiscVector.addElement(vMisc);
                }
                vMisc = new Misc();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vMisc.setName(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vMisc.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TYPE")) {
                vMisc.setType(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("USE")) {
                vMisc.setUse(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("TIME")) {
                vMisc.setTime(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT")) {
                vMisc.setAmount(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT_IS_WEIGHT")) {
                vMisc.setAmount_Is_Weight(Util.str2bln(vValue));
            } else if (vProperty.equalsIgnoreCase("USE_FOR")) {
                vMisc.setUse_For(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
                vMisc.setNotes(Util.str2str(vValue));
            }
        }
        vMiscVector.addElement(vMisc);
        for (int i = 0; i < vMiscVector.size(); i++) {
            beerXml.getRecipe().getMiscs().addElement(vMiscVector.get(i));
        }
    }

    private void doYeast(String vElementName, String vStringElementValue) {
        vectorYeasts.addElement(new VectorElement(vYeastIndex, vStringElementValue, vElementName));
    }

    private void doYeastPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Yeast vYeast = new Yeast();
        ;
        Vector vYeastVector = new Vector();
        for (int y = 0; y < vectorYeasts.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorYeasts.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vYeastVector.addElement(vYeast);
                }
                vYeast = new Yeast();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vYeast.setName(Util.nin(Util.str2str(vValue)));
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vYeast.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TYPE")) {
                vYeast.setType(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("FORM")) {
                vYeast.setForm(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT")) {
                vYeast.setAmount(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT_IS_WEIGHT")) {
                vYeast.setAmount_Is_Weight(Util.str2bln(vValue));
            } else if (vProperty.equalsIgnoreCase("LABORATORY")) {
                vYeast.setLaboratory(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("PRODUCT_ID")) {
                vYeast.setProduct_Id(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("MIN_TEMPERATURE")) {
                vYeast.setMin_Temperature(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("MAX_TEMPERATURE")) {
                vYeast.setMax_Temperature(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("FLOCCULATION")) {
                vYeast.setFlocculation(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("ATTENUATION")) {
                vYeast.setAttenuation(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
                vYeast.setNotes(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("BEST_FOR")) {
                vYeast.setBest_For(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("TIMES_CULTURED")) {
                vYeast.setTimes_Cultured(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("MAX_REUSE")) {
                vYeast.setMax_Reuse(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("ADD_TO_SECONDARY")) {
                vYeast.setAdd_To_Secondary(Util.str2bln(vValue));
            } else if (vProperty.equalsIgnoreCase("TAP_USE")) {
                vYeast.setTap_Use(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("TAP_ATTENUATION_MAX")) {
                vYeast.setTaAttenuationMax(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TAP_ATTENUATION_MIN")) {
                vYeast.setTaAttenuationMin(Util.str2dbl(vValue));
            }
        }
        vYeastVector.addElement(vYeast);
        for (int i = 0; i < vYeastVector.size(); i++) {
            beerXml.getRecipe().getYeasts().addElement(vYeastVector.get(i));
        }
    }

    private void doWater(String vElementName, String vStringElementValue) {
        vectorWaters.addElement(new VectorElement(vWaterIndex, vStringElementValue, vElementName));
    }

    private void doWaterPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Water vWater = new Water();
        ;
        Vector vWaterVector = new Vector();
        for (int y = 0; y < vectorWaters.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorWaters.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vWaterVector.addElement(vWater);
                }
                vWater = new Water();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vWater.setName(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vWater.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("AMOUNT")) {
                vWater.setAmount(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("CALCIUM")) {
                vWater.setCalcium(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("BICARBONATE")) {
                vWater.setBicarbonate(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("SULFATE")) {
                vWater.setSulfate(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("CHLORIDE")) {
                vWater.setChloride(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("SODIUM")) {
                vWater.setSodium(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("MAGNESIUM")) {
                vWater.setMagnesium(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("PH")) {
                vWater.setPh(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
                vWater.setNotes(Util.str2str(vValue));
            }
        }
        vWaterVector.addElement(vWater);
        for (int i = 0; i < vWaterVector.size(); i++) {
            beerXml.getRecipe().getWaters().addElement(vWaterVector.get(i));
        }
    }

    private void doMash(String vElementName, String vStringElementValue) {
        vectorMashs.addElement(new VectorElement(vMashIndex, vStringElementValue, vElementName));
    }

    private void doMashPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Mash vMash = new Mash();
        ;
        Vector vMashVector = new Vector();
        for (int y = 0; y < vectorMashs.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorMashs.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vMashVector.addElement(vMash);
                }
                vMash = new Mash();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vMash.setName(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vMash.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("GRAIN_TEMP")) {
                vMash.setGrain_Temp(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
                vMash.setNotes(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("TUN_TEMP")) {
                vMash.setTun_Temp(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("SPARGE_TEMP")) {
                vMash.setSparge_Temp(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("PH")) {
                vMash.setPh(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TUN_WEIGHT")) {
                vMash.setTun_Weight(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TUN_SPECIFIC_HEAT")) {
                vMash.setTun_Specific_Heat(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("EQUIP_ADJUST")) {
                vMash.setEquip_Adjust(Util.str2bln(vValue));
            }
        }
        vMashVector.addElement(vMash);
        for (int i = 0; i < vMashVector.size(); i++) {
            beerXml.getRecipe().getMash().addElement(vMashVector.get(i));
        }
    }

    public void pl(String s) {
        if (debug) {
            System.out.println(this.getClass().getName() + "." + s);
        }
    }

    public void setBeerXml(BeerXml beerXml) {
        this.beerXml = beerXml;
    }

    public BeerXml getBeerXml() {
        return beerXml;
    }

    private void doMashSteps(String vElementName, String vStringElementValue, int vMashIndex) {
        vectorMashSteps.addElement(new VectorElement(vMashStepsIndex, vStringElementValue, vElementName, vMashIndex - 1));
    }

    private void doMashStepsPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Mash_Step vMash_Step = new Mash_Step();
        ;
        Vector vMash_StepVector = new Vector();
        for (int y = 0; y < vectorMashSteps.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorMashSteps.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vMash_StepVector.addElement(vMash_Step);
                }
                vMash_Step = new Mash_Step();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vMash_Step.setName(Util.str2str(vValue));
                vMash_Step.setTa_parentid(vVectorElement.getParent());
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vMash_Step.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TYPE")) {
                vMash_Step.setType(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("INFUSE_AMOUNT")) {
                vMash_Step.setInfuse_Amount(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("STEP_TEMP")) {
                vMash_Step.setStep_Temp(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("STEP_TIME")) {
                vMash_Step.setStep_Time(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("RAMP_TIME")) {
                vMash_Step.setRamp_Time(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("END_TEMP")) {
                vMash_Step.setEnd_Temp(Util.str2dbl(vValue));
            }
        }
        vMash_StepVector.addElement(vMash_Step);
        for (int i = 0; i < vMash_StepVector.size(); i++) {
            if (!Util.isNull(Util.nin(((Mash_Step) vMash_StepVector.get(i)).getStep_Temp())) || !Util.isNull(Util.nin(((Mash_Step) vMash_StepVector.get(i)).getStep_Time()))) {
                ((Mash) beerXml.getRecipe().getMash().get(((Mash_Step) vMash_StepVector.get(i)).getTa_parentid())).getMash_Steps().addElement(vMash_StepVector.get(i));
            } else {
            }
        }
    }

    private static boolean isFermentation(String name) {
        if (name.equalsIgnoreCase("FERMENTATION")) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isFermentationStep(String name) {
        if (name.equalsIgnoreCase("FERMENTATION_STEP")) {
            return true;
        } else {
            return false;
        }
    }

    private void doFermentation(String vElementName, String vStringElementValue) {
        vectorFermentations.addElement(new VectorElement(vFermentationIndex, vStringElementValue, vElementName));
    }

    private void doFermentationPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Fermentation vFermentation = new Fermentation();
        ;
        Vector vFermentationVector = new Vector();
        for (int y = 0; y < vectorFermentations.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorFermentations.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vFermentationVector.addElement(vFermentation);
                }
                vFermentation = new Fermentation();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vFermentation.setName(Util.str2str(vValue));
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vFermentation.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("GRAIN_TEMP")) {
            } else if (vProperty.equalsIgnoreCase("NOTES")) {
            } else if (vProperty.equalsIgnoreCase("TUN_TEMP")) {
            } else if (vProperty.equalsIgnoreCase("SPARGE_TEMP")) {
            } else if (vProperty.equalsIgnoreCase("PH")) {
            } else if (vProperty.equalsIgnoreCase("TUN_WEIGHT")) {
            } else if (vProperty.equalsIgnoreCase("TUN_SPECIFIC_HEAT")) {
            } else if (vProperty.equalsIgnoreCase("EQUIP_ADJUST")) {
            }
        }
        vFermentationVector.addElement(vFermentation);
        for (int i = 0; i < vFermentationVector.size(); i++) {
            beerXml.getRecipe().getFermentation().addElement(vFermentationVector.get(i));
        }
    }

    private void doFermentationSteps(String vElementName, String vStringElementValue, int vFermentationIndex) {
        vectorFermentationSteps.addElement(new VectorElement(vFermentationStepsIndex, vStringElementValue, vElementName, vFermentationIndex - 1));
    }

    private void doFermentationStepsPostActions() {
        int vGetNr = 0;
        int vTeller = -1;
        Fermentation_Step vFermentation_Step = new Fermentation_Step();
        ;
        Vector vFermentation_StepVector = new Vector();
        for (int y = 0; y < vectorFermentationSteps.size(); y++) {
            VectorElement vVectorElement = (VectorElement) vectorFermentationSteps.get(y);
            String vProperty = vVectorElement.getProperty();
            String vValue = vVectorElement.getValue();
            if (vVectorElement.getNr() != vGetNr) {
                if (vGetNr != 0) {
                    vFermentation_StepVector.addElement(vFermentation_Step);
                }
                vFermentation_Step = new Fermentation_Step();
                vTeller = vTeller + 1;
            }
            vGetNr = vVectorElement.getNr();
            if (vProperty.equalsIgnoreCase("NAME")) {
                vFermentation_Step.setName(Util.str2str(vValue));
                vFermentation_Step.setTa_parentid(vVectorElement.getParent());
            } else if (vProperty.equalsIgnoreCase("VERSION")) {
                vFermentation_Step.setVersion(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("TYPE")) {
            } else if (vProperty.equalsIgnoreCase("INFUSE_AMOUNT")) {
            } else if (vProperty.equalsIgnoreCase("STEP_TEMP")) {
                vFermentation_Step.setStep_Temp(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("STEP_TIME")) {
                vFermentation_Step.setStep_Time(Util.str2dbl(vValue));
            } else if (vProperty.equalsIgnoreCase("RAMP_TIME")) {
            } else if (vProperty.equalsIgnoreCase("END_TEMP")) {
            }
        }
        vFermentation_StepVector.addElement(vFermentation_Step);
        for (int i = 0; i < vFermentation_StepVector.size(); i++) {
            if (!Util.isNull(Util.nin(((Fermentation_Step) vFermentation_StepVector.get(i)).getStep_Temp())) || !Util.isNull(Util.nin(((Fermentation_Step) vFermentation_StepVector.get(i)).getStep_Time()))) {
                ((Fermentation) beerXml.getRecipe().getFermentation().get(((Fermentation_Step) vFermentation_StepVector.get(i)).getTa_parentid())).getFermentation_Steps().addElement(vFermentation_StepVector.get(i));
            } else {
            }
        }
    }

    public String getMessage() {
        return Message;
    }
}
