package edu.umich.sph.epidkardia.RiskIndex;

import java.util.*;
import edu.umich.sph.epidkardia.RiskIndex.RiskIndexData.VARTYPES;

public class RiskIndexModel {

    private EnumMap<VARTYPES, ArrayList<String>> modelvariables = new EnumMap<VARTYPES, ArrayList<String>>(VARTYPES.class);

    private EnumMap<VARTYPES, Double> modelcutpoints = new EnumMap<VARTYPES, Double>(VARTYPES.class);

    private EnumMap<VARTYPES, Double> modelpercentilecutpoints = new EnumMap<VARTYPES, Double>(VARTYPES.class);

    private HashMap<String, HashMap<String, Double>> coefficientMap;

    private EnumMap<VARTYPES, ArrayList<String>> untrimmedModel = new EnumMap<VARTYPES, ArrayList<String>>(VARTYPES.class);

    private EnumMap<VARTYPES, double[]> brierScores = new EnumMap<VARTYPES, double[]>(VARTYPES.class);

    private EnumMap<VARTYPES, double[]> optSetRisks = new EnumMap<VARTYPES, double[]>(VARTYPES.class);

    /**
   * A method to get the coefficients for the variables used in the risk index model
   * 
   * @return a HashMap<String, HashMap<String, Double>> containing coefficients for each 
   * variable in the risk index model for each VARTYPE
   */
    public HashMap<String, HashMap<String, Double>> getCoefficients() {
        return (new HashMap<String, HashMap<String, Double>>(coefficientMap));
    }

    /**
   * A method to get the coefficients for the variables used in the risk index model
   * 
   * @param cMap a HashMap<String, HashMap<String, Double>> containing coefficients for each 
   * variable in the risk index model for each VARTYPE
   */
    public void setCoefficients(HashMap<String, HashMap<String, Double>> cMap) {
        coefficientMap = new HashMap<String, HashMap<String, Double>>(cMap);
    }

    /**
   * A method to get the cutpoints for each of the VARTYPES
   * 
   * @return EnumMap<VARTYPES, Double> containing the cutpoints 
   * for each of the VARTYPES
   */
    public EnumMap<VARTYPES, Double> getCutpoints() {
        return (new EnumMap<VARTYPES, Double>(modelcutpoints));
    }

    /**
   * A method to set the cutpoints for each of the VARTYPES
   * 
   * @param cps an EnumMap<VARTYPES, Double> containing the cutpoints for each of the VARTYPES
   */
    public void setCutpoints(EnumMap<VARTYPES, Double> cps) {
        modelcutpoints = new EnumMap<VARTYPES, Double>(cps);
    }

    /**
   * A method to get the percentile cutpoints for each of the VARTYPES
   * 
   * @return EnumMap<VARTYPES, Double> containing the percentile cutpoints 
   * for each of the VARTYPES
   */
    public EnumMap<VARTYPES, Double> getPercentileCutpoints() {
        return (new EnumMap<VARTYPES, Double>(modelpercentilecutpoints));
    }

    /**
   * A method to set the percentile cutpoints for each of the VARTYPES
   * 
   * @param cps an EnumMap<VARTYPES, Double> containing the percentile cutpoints for each of the VARTYPES
   */
    public void setPercentileCutpoints(EnumMap<VARTYPES, Double> cps) {
        modelpercentilecutpoints = new EnumMap<VARTYPES, Double>(cps);
    }

    /**
   * A method to get the trimmed variables selected into the risk index models for each VARTYPE
   * 
   * @return an EnumMap<VARTYPES,  ArrayList<String>> with the trimmed set of variables selected
   * into the risk index model for each VARTYPE
   */
    public EnumMap<VARTYPES, ArrayList<String>> getModelVariables() {
        return (new EnumMap<VARTYPES, ArrayList<String>>(modelvariables));
    }

    /**
   * A method to get the trimmed variables selected into the risk index models for each VARTYPE
   * 
   * @param umvs an EnumMap<VARTYPES,  ArrayList<String>> with the trimmed set of variables selected
   * into the risk index model for each VARTYPE
   */
    public void setModelVariables(EnumMap<VARTYPES, ArrayList<String>> mvs) {
        modelvariables = new EnumMap<VARTYPES, ArrayList<String>>(mvs);
    }

    /**
   * A method to get the Brier scores for each VARTYPE
   * 
   * @return an EnumMap<VARTYPES, double[]> with the Brier score for each VARTYPE
   */
    public EnumMap<VARTYPES, double[]> getBrierScores() {
        return (new EnumMap<VARTYPES, double[]>(brierScores));
    }

    /**
   * A method to set the Brier scores for each VARTYPE
   * 
   * @param bs an EnumMap<VARTYPES, double[]> with the Brier score for each VARTYPE
   */
    public void setBrierScores(EnumMap<VARTYPES, double[]> bs) {
        brierScores = new EnumMap<VARTYPES, double[]>(bs);
    }

    /**
   * A method to get the risk index values for each individuals in the Optimization Set 
   * for each VARTYPE
   * 
   * @return an EnumMap<VARTYPES, double[]> containing the risk index values for each individual 
   * in the Optimization Set for each VARTYPE 
   */
    public EnumMap<VARTYPES, double[]> getOptSetRisks() {
        return (new EnumMap<VARTYPES, double[]>(optSetRisks));
    }

    /**
   * A method to get the all of the variables selected into the risk index models for each VARTYPE
   * 
   * @return an EnumMap<VARTYPES,  ArrayList<String>> with the full set of variables selected
   * into the risk index model for each VARTYPE
   */
    public void setOptSetRisks(EnumMap<VARTYPES, double[]> osr) {
        optSetRisks = new EnumMap<VARTYPES, double[]>(osr);
    }

    public EnumMap<VARTYPES, ArrayList<String>> getUntrimmedModelVariables() {
        return (new EnumMap<VARTYPES, ArrayList<String>>(untrimmedModel));
    }

    /**
   * A method to get all of the variables selected into the risk index models for each VARTYPE
   * 
   * @param umvs an EnumMap<VARTYPES,  ArrayList<String>> with the full set of variables selected
   * into the risk index model for each VARTYPE
   */
    public void setUntrimmedModelVariables(EnumMap<VARTYPES, ArrayList<String>> umvs) {
        untrimmedModel = new EnumMap<VARTYPES, ArrayList<String>>(umvs);
    }
}
