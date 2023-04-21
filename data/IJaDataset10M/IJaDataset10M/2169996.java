package fungusEater.ekosystem;

import fungusEater.Config;
import fungusEater.FungusEaterHelper;
import fungusEater.datatypes.*;
import net.ekology.ekosystem.*;
import net.ekology.ekosystem.datatypes.*;
import net.ekology.core.datatypes.*;
import net.ekology.graphics.EKPainter;
import java.awt.geom.*;
import java.awt.*;

/**
 * @author Aarón Tavío - aaron.tavio at gmail.com
 * @version 1.0.0 - 20081019-1700
 */
public class FungusEater extends BioticAgent {

    private EKPainter oPainter;

    private Taros oTaros;

    private Color oBodyDrawColor;

    private Color oBodyFillColor;

    private String sFungusFoundID;

    private String sOreFoundID;

    private boolean bFactoryFound;

    private double dEnergy;

    private double dCurrentLoadWeight;

    private double dMaxCapacity;

    private OreType oCollectingOre;

    private FungusType oFoodType;

    public FungusEater() {
        oPainter = EKPainter.getInstance();
        oTaros = null;
        sFungusFoundID = null;
        sOreFoundID = null;
        bFactoryFound = false;
    }

    public FungusEater(String sID, Taros oBiotope, Genome oGenome) {
        super(sID, oBiotope, oGenome);
        oPainter = EKPainter.getInstance();
        oTaros = oBiotope;
        sFungusFoundID = null;
        sOreFoundID = null;
        bFactoryFound = false;
        dEnergy = FungusEaterHelper.getRandomValue(Config.FE_ENERGY_INIT_MAX, Config.FE_ENERGY_INIT_MIN);
        dCurrentLoadWeight = 0;
    }

    @Override
    public void initialize() {
        if (getID() == null) {
            System.out.printf("FungusEater.java->initialize();\n");
            oTaros = (Taros) super.oBiotope;
            setID(FungusEaterHelper.generateID());
            dEnergy = FungusEaterHelper.getRandomValue(Config.FE_ENERGY_INIT_MAX, Config.FE_ENERGY_INIT_MIN);
            dCurrentLoadWeight = 0;
            setPosition(new EKVector(0, 0));
            setOrientation(new EKVector(0, 1));
        }
    }

    protected void initializeFeatures() {
        Genome oGenome = this.getGenome();
        dMaxCapacity = Double.parseDouble(oGenome.getFeature("dMaxCapacity"));
        oCollectingOre = OreType.valueOf(oGenome.getFeature("oCollectingOre"));
        oFoodType = FungusType.valueOf(oGenome.getFeature("oFoodType"));
        oBodyFillColor = Color.RED;
        oBodyDrawColor = getDrawColor(oFoodType);
    }

    private Color getDrawColor(FungusType oType) {
        if (oType == FungusType.A_TYPE) return Config.A_TYPE_FUNGUS_COLOR; else if (oType == FungusType.B_TYPE) return Config.B_TYPE_FUNGUS_COLOR; else return Config.C_TYPE_FUNGUS_COLOR;
    }

    @Override
    public int _DO(String sCurrentStateName, int iEKTimeSlice) {
        int iEKTimeUsed;
        System.out.printf("FungusEater.java->_DO(\"%s\");\n", sCurrentStateName);
        iEKTimeUsed = (Integer) super._DO(sCurrentStateName, iEKTimeSlice);
        printState();
        return iEKTimeUsed;
    }

    @Override
    public int _EXIT(String sCurrentStateName, EKEvent oFiringEvent, int iEKTimeSlice) {
        int iEKTimeUsed;
        System.out.printf("FungusEater.java->_EXIT(\"%s\");\n", sCurrentStateName);
        iEKTimeUsed = (Integer) super._EXIT(sCurrentStateName, oFiringEvent, iEKTimeSlice);
        printState();
        return iEKTimeUsed;
    }

    public int collectingOre_EXIT(EKEvent oFiringEvent, int iEKTimeSlice) {
        sOreFoundID = null;
        return 0;
    }

    public int seekingFungus_DO(int iEKTimeSlice) {
        return seek(oFoodType, iEKTimeSlice);
    }

    public int eating_DO(int iEKTimeSlice) {
        double dFood;
        dFood = oTaros.eat(getID(), sFungusFoundID);
        if (dFood == 0) sFungusFoundID = null; else dEnergy = dEnergy + dFood;
        return 1;
    }

    public int seekingOre_DO(int iEKTimeSlice) {
        return seek(oCollectingOre, iEKTimeSlice);
    }

    public int collectingOre_DO(int iEKTimeSlice) {
        double dAmount, dLoadedAmount;
        dAmount = dMaxCapacity - dCurrentLoadWeight;
        dLoadedAmount = oTaros.loadOre(getID(), sOreFoundID, dAmount);
        if (dLoadedAmount == 0) sOreFoundID = null; else dCurrentLoadWeight += dLoadedAmount;
        return 1;
    }

    public int seekingFactory_DO(int iEKTimeSlice) {
        EKVector oFactoryPosition;
        int iEKTimeUsed;
        oFactoryPosition = locateRadioBeacon();
        if (oFactoryPosition.length() < Config.PROXIMITY_THRESHOLD) {
            bFactoryFound = true;
            iEKTimeUsed = 0;
        } else {
            iEKTimeUsed = oTaros.goTo(getID(), oFactoryPosition, iEKTimeSlice);
            dEnergy = dEnergy - getEnergyLost(iEKTimeUsed);
            bFactoryFound = false;
        }
        return iEKTimeUsed;
    }

    public int unloadingOre_DO(int iEKTimeSlice) {
        oTaros.unloadOre(getID(), oCollectingOre, dCurrentLoadWeight);
        dCurrentLoadWeight = 0;
        return 1;
    }

    public int unloadingOre_EXIT(EKEvent oFiringEvent, int iEKTimeSlice) {
        bFactoryFound = false;
        return 0;
    }

    @Override
    public String[] getAttributeNames() {
        String[] aResult = { "dEnergy", "dCurrentLoadWeight", "dMaxCapacity", "oCollectingOre", "oFoodType" };
        return aResult;
    }

    @Override
    public Attribute getAttribute(String sName) {
        Attribute oResult = null;
        if (sName.equals("dEnergy")) oResult = new Attribute<Double>("dEnergy", dEnergy); else if (sName.equals("dCurrentLoadWeight")) oResult = new Attribute<Double>("dCurrentLoadWeight", dCurrentLoadWeight); else if (sName.equals("dMaxCapacity")) oResult = new Attribute<Double>("dMaxCapacity", dMaxCapacity); else if (sName.equals("oCollectingOre")) oResult = new Attribute<OreType>("oCollectingOre", oCollectingOre); else if (sName.equals("oFoodType")) oResult = new Attribute<FungusType>("oFoodType", oFoodType);
        return oResult;
    }

    @Override
    public void setAttribute(String sName, String sValue) {
        if (sName.equals("dEnergy")) dEnergy = Double.parseDouble(sValue); else if (sName.equals("dCurrentLoadWeight")) dCurrentLoadWeight = Double.parseDouble(sValue); else if (sName.equals("dMaxCapacity")) dMaxCapacity = Double.parseDouble(sValue); else if (sName.equals("oCollectingOre")) oCollectingOre = OreType.valueOf(sValue.toUpperCase()); else if (sName.equals("oFoodType")) {
            oFoodType = FungusType.valueOf(sValue.toUpperCase());
            oBodyDrawColor = getDrawColor(oFoodType);
        }
    }

    public void draw(Graphics2D g2) {
        oPainter.drawDummy(g2, getPosition(), getOrientation(), Config.FE_SIZE, oBodyDrawColor, oBodyFillColor);
    }

    /**
     * llama a oBiotope.sniff y cuando se haya encontrado (el módulo del vector es 0) genera un evento 'fungusFound' para sí mismo 
     * @param oFungusType
     * @param iEKTimeSlice
     * @return 
     */
    public int seek(FungusType oFungusType, int iEKTimeSlice) {
        String sFungusID;
        EKVector oFungusPosition;
        int iEKTimeUsed;
        sFungusID = oTaros.sniff(getID(), oFungusType);
        if (sFungusID != null) {
            oFungusPosition = oTaros.getAgentRelativePosition(sFungusID, getPosition());
            if (oFungusPosition.length() < Config.PROXIMITY_THRESHOLD) {
                sFungusFoundID = sFungusID;
                iEKTimeUsed = 0;
            } else {
                iEKTimeUsed = oTaros.goTo(getID(), oFungusPosition, iEKTimeSlice);
                dEnergy = dEnergy - getEnergyLost(iEKTimeUsed);
            }
        } else {
            iEKTimeUsed = iEKTimeSlice;
            dEnergy = dEnergy - (iEKTimeUsed * Config.FE_WAITING_ENERGY_FACTOR);
        }
        return iEKTimeUsed;
    }

    public int seek(OreType oOreType, int iEKTimeSlice) {
        String sOreID;
        EKVector oOrePosition;
        int iEKTimeUsed;
        sOreID = oTaros.scanForOre(getID(), oOreType);
        if (sOreID != null) {
            oOrePosition = oTaros.getAgentRelativePosition(sOreID, getPosition());
            if (oOrePosition.length() < Config.PROXIMITY_THRESHOLD) {
                sOreFoundID = sOreID;
                iEKTimeUsed = 0;
            } else {
                iEKTimeUsed = oTaros.goTo(getID(), oOrePosition, iEKTimeSlice);
                dEnergy = dEnergy - getEnergyLost(iEKTimeUsed);
            }
        } else {
            iEKTimeUsed = iEKTimeSlice;
            dEnergy = dEnergy - (iEKTimeUsed * Config.FE_WAITING_ENERGY_FACTOR);
        }
        return iEKTimeUsed;
    }

    private double getEnergyLost(int iEKTimeUsed) {
        return ((iEKTimeUsed * Config.FE_MOVEMENT_ENERGY_FACTOR) + (iEKTimeUsed * dCurrentLoadWeight * Config.FE_MOVEMENT_WEIGHT_ENERGY_FACTOR));
    }

    private EKVector locateRadioBeacon() {
        return oTaros.radioBeacon(getID());
    }

    public void setCollectingOre(OreType oCollectingOre) {
        this.oCollectingOre = oCollectingOre;
    }

    public void kill() {
        if (Config.DEBUG_LEVEL > 0) System.out.printf("FungusEater.java->kill():%s; \n", getID());
    }

    public double getEnergy() {
        return dEnergy;
    }

    public boolean fungusFound() {
        return (sFungusFoundID != null);
    }

    public boolean oreFound() {
        return (sOreFoundID != null);
    }

    public boolean fullLoaded() {
        return (dCurrentLoadWeight >= dMaxCapacity);
    }

    public boolean empty() {
        return (dCurrentLoadWeight == 0);
    }

    public boolean factoryFound() {
        return bFactoryFound;
    }

    public void printState() {
        System.out.printf("FungusEater=[id: '%s', dEnergy: '%.3f', dCurrentLoadWeight: '%.3f' , oFoodType:%s; , oCollectingOre:%s;]\n", getID(), dEnergy, dCurrentLoadWeight, oFoodType, oCollectingOre);
    }
}
