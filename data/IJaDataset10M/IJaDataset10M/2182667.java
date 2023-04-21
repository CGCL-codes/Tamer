package de.ibis.permoto.model.definitions;

import de.ibis.permoto.model.basic.predictionbusinesscase.DescriptionSection;
import de.ibis.permoto.model.basic.predictionbusinesscase.PredictionBusinessCase;
import de.ibis.permoto.model.definitions.impl.PerMoToBusinessCase;

/**
 * @author Slavko Segota
 *
 */
public interface IPredictionBusinessCase {

    /**
	 * Returns the checksum for the ppm file.
	 */
    public String getPpmFileCRC();

    /**
	 * Sets the checksum for the ppm file.
	 * @param ppmFileCRC The checksum to be set
	 */
    public void setPpmFileCRC(String ppmFileCRC);

    /** 
     * Returns the name of the prediction scenario. Default: name of XML file.   
     * @return String the name of the prediction scenario contained 
     * in this prediction business case  
     */
    public String getPredictionScenarioName();

    /** 
     * Sets the name of the prediction scenario.  
     * @param value String the name of the prediction scenario contained in 
     * this prediction business case     
     */
    public void setPredictionScenarioName(String value);

    /** 
     * Returns the name of the prediction scenario including the path..   
     * @return String the name of the prediction scenario contained in this 
     * prediction business case  
     */
    public String getFullPredictionScenarioName();

    /** 
     * Sets the name of the prediction scenario including the path.  
     * @param value String the name of the prediction scenario contained in this 
     * prediction business case     
     */
    public void setFullScenarioName(String value);

    /** 
     * Returns the PredictionClassSection part of the PerMoToBusinessCase.  
     * @return IPredictionClassSection the PredictionClassSection part of the PerMoToPredictionBusinessCase, 
     * null if not defined
     */
    public IPredictionClassSection getPredictionClassSection();

    /** 
	 * Returns the PredictionStationSection part of the PerMoToBusinessCase.  
 	 * @return IPredictionStationSection the PredictionStationSection part of the PerMoToPredictionBusinessCase,
 	 * null if not defined
 	 */
    public IPredictionStationSection getPredictionStationSection();

    /**
     * Sets the new PredictionBusinessCase model.
     * @param pbcNew PredictionBusinessCase
     */
    public void setDataModel(PredictionBusinessCase pbcNew);

    /**
	 * check, if all parameters are set and the model can be saved
	 * @return String info about parameters not set in model
	 */
    public String checkForSave();

    /**
	 * Returns the PredictionSolverSection of this prediction business case.
	 * @return IPredictionSolverSection the PredictionSolverSection of the PerMoToPredictionBusinessCase,
	 * null if not defined
	 */
    public IPredictionSolverSection getPredictionSolverSection();

    /**
	 * Returns the basic business case which the prediction is based on.
	 * @return IBusinessCase the basic business case which the prediction is based on.
	 */
    public IBusinessCase getBasicBusinessCase();

    /**
	 * Returns this prediction scenario's description section.
	 * @return {@link DescriptionSection} this prediction scenario's description section
	 */
    public IPredictionDescriptionSection getDescriptionSection();

    /**
	 * Removes the prediction class section from the model, i.e. all changes made to 
	 * classes are lost. 
	 * @return boolean true if after execution there is no prediction class section within the scenario,
	 * false otherwise. 
	 */
    public boolean removePredictionClassSection();

    /**
	 * Removes the prediction station section from the model, i.e. all changes made to 
	 * stations are lost. 
	 * @return boolean true if after execution there is no prediction station section within the scenario,
	 * false otherwise. 
	 */
    public boolean removePredictionStationSection();

    /**
	 * Generates the predicted business case from the predictions made. Be aware
	 * the prediction business case is modified by this 
	 * method and is no longer consistent with loaded prediction business case. If the original
	 * prediction business case is needed, it has to be reloaded from file! 
	 * @return PerMoToBusinessCase the generated predicted business case.
	 */
    public PerMoToBusinessCase generatePredictedBusinessCase();
}
