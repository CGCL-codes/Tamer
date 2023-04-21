package org.systemsbiology.analysis;

import org.systemsbiology.chem.*;
import org.systemsbiology.chem.app.*;

/**
 * I can put a constructor and controls on parameters in this class.
 * */
public class SensitivityAnalysisOATDensityBasedParameters {

    /**
	 * used to add results
	 * */
    public MainApp mainApp;

    /**
	 * used to interact with the GUI
	 * */
    public SensitivityAnalysisOATPanel analysisPanel;

    public Model model;

    public ISimulator simulator;

    /**
	 * nominal value of each parameter requested.
	 * */
    public double[] parameterNominalValues;

    /**
	 * list of names of parameters
	 * */
    public String[] parameterNames;

    public SimulatorParameters simulatorParameters;

    public double startTime;

    public double endTime;

    public int numTimePoints;

    public String[] mRequestedSymbolNames;

    public int numOfHistogramColumns;

    public double perturbation;

    public boolean isLoadFromFile;

    public String method;

    public SimulationResults[] simulationResults;

    public double[] parameterDeltas;
}
