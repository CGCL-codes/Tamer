package domain.attributes;

import infrastructure.species_parameters.IDatabase;
import infrastructure.species_parameters.ParamFile;

/**
 * This is a Factory class which creates structural attributes 
 * for leaves. 
 * It is implemented using a Multiton design pattern, 
 * i.e. a series of Singletons for each species 
 * residing in the virtual world.
 * 
 * @author Uwe Grueters
 */
class LeavesAttributesFactory {

    private final double allomCoeffLDCvsNLeaf, allomConstLDCvsNLeaf;

    private final double slopeTDLeafvsLDC, interceptTDLeafvsLDC;

    private final double allomCoeffLMAvsNLeaf, allomConstLMAvsNLeaf;

    private final double allomCoeffDMvsNLeaf, allomConstDMvsNLeaf;

    private final double allomCoeffLWRatiovsNLeaf, allomConstLWRatiovsNLeaf;

    private final double cInDryMass_g_per_g;

    private final double apicalDominance;

    /**
	 * Constructor
	 * @param specName
	 */
    LeavesAttributesFactory(String specName) {
        IDatabase paramDB = new ParamFile();
        allomCoeffLDCvsNLeaf = paramDB.getDouble(specName, "allomCoeffLDCvsNLeaf");
        allomConstLDCvsNLeaf = paramDB.getDouble(specName, "allomConstLDCvsNLeaf");
        slopeTDLeafvsLDC = paramDB.getDouble(specName, "slopeTDLeafvsLDC");
        interceptTDLeafvsLDC = paramDB.getDouble(specName, "interceptTDLeafvsLDC");
        allomCoeffLMAvsNLeaf = paramDB.getDouble(specName, "allomCoeffLMAvsNLeaf");
        allomConstLMAvsNLeaf = paramDB.getDouble(specName, "allomConstLMAvsNLeaf");
        allomCoeffDMvsNLeaf = paramDB.getDouble(specName, "allomCoeffDMvsNLeaf");
        allomConstDMvsNLeaf = paramDB.getDouble(specName, "allomConstDMvsNLeaf");
        allomCoeffLWRatiovsNLeaf = paramDB.getDouble(specName, "allomCoeffLWRatiovsNLeaf");
        allomConstLWRatiovsNLeaf = paramDB.getDouble(specName, "allomConstLWRatiovsNLeaf");
        cInDryMass_g_per_g = paramDB.getDouble(specName, "cInDryMass_g_per_g");
        apicalDominance = paramDB.getDouble(specName, "apicalDominance");
    }

    /**
	 * This method returns the resources paritioned to 
	 * the whole set of new leaves, when called with the 
	 * resources of the seedling/ramet.
	 * @param resources
	 * @return leavesResources
	 */
    Resources createLeavesResources(Resources resources) {
        double resourcesDryMass_g = 1 / cInDryMass_g_per_g * resources.carbon_g;
        double NConcentration_g_per_g = resources.nitrogen_g / resourcesDryMass_g;
        double percentNConcentration = percentNConcentration(NConcentration_g_per_g);
        Resources leavesResources = new Resources();
        leavesResources.carbon_g = this.cInDryMass_g_per_g * computeAllometry(allomCoeffDMvsNLeaf, allomConstDMvsNLeaf, percentNConcentration);
        leavesResources.nitrogen_g = NConcentration_g_per_g * leavesResources.carbon_g / cInDryMass_g_per_g;
        return leavesResources;
    }

    /**
	 * This method creates LeafStructuralAttributes 
	 * either for the set of leaves or for a single leaf. 
	 * @param resources
	 * @return leafAttributes
	 */
    LeafStructuralAttributes createLeafStructuralAttributes(Resources resources) {
        LeafStructuralAttributes leavesAttributes = adjustLeafAttributesForResourceSupply(resources);
        return leavesAttributes;
    }

    /**
	 * This method adjusts leaf anatomy to improve internal resources suppy
	 * and leaf morphology to improve external resources supply.
	 * @param resources
	 * @return leavesAttributes
	 */
    private LeafStructuralAttributes adjustLeafAttributesForResourceSupply(Resources resources) {
        double resourcesDryMass_g, NConentration_g_per_g, perentNConcentration, dryMatterContent_g_per_g, tissueDensity_g_per_cm3, leafMassPerArea_g_per_m2, lengthWidthRatio_cm_per_cm;
        resourcesDryMass_g = 1 / cInDryMass_g_per_g * resources.carbon_g;
        NConentration_g_per_g = resources.nitrogen_g / resourcesDryMass_g;
        perentNConcentration = percentNConcentration(NConentration_g_per_g);
        dryMatterContent_g_per_g = computeAllometry(allomCoeffLDCvsNLeaf, allomConstLDCvsNLeaf, perentNConcentration);
        tissueDensity_g_per_cm3 = slopeTDLeafvsLDC * dryMatterContent_g_per_g + interceptTDLeafvsLDC;
        leafMassPerArea_g_per_m2 = computeAllometry(allomCoeffLMAvsNLeaf, allomConstLMAvsNLeaf, perentNConcentration);
        lengthWidthRatio_cm_per_cm = computeAllometry(allomCoeffLWRatiovsNLeaf, allomConstLWRatiovsNLeaf, perentNConcentration);
        LeafStructuralAttributes leavesAttributes = new LeafStructuralAttributes(resources.nitrogen_g, resources.carbon_g, resourcesDryMass_g, dryMatterContent_g_per_g, tissueDensity_g_per_cm3, leafMassPerArea_g_per_m2, lengthWidthRatio_cm_per_cm);
        return leavesAttributes;
    }

    /**
	 * This auxilliary method returns the y-value of an 
	 * allometric relationship.
	 * @param allometricCoefficient
	 * @param allometricConstant
	 * @param x
	 * @return y
	 */
    private double computeAllometry(double allometricCoefficient, double allometricConstant, double x) {
        double log10x, log10y, y;
        log10x = Math.log10(x);
        log10y = allometricCoefficient * log10x + allometricConstant;
        y = Math.pow(10.0, log10y);
        return y;
    }

    /**
	 * This auxilliary method returns the percent N concentration.
	 * @param N_g_per_g
	 * @return percentN
	 */
    private double percentNConcentration(double N_g_per_g) {
        return 100 * N_g_per_g;
    }

    double getApicalDominance() {
        return apicalDominance;
    }
}
