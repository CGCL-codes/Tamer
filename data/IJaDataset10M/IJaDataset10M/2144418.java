package org.systemsbiology.analysis;

import org.systemsbiology.chem.*;
import org.systemsbiology.chem.app.*;

/**
 * I can put a constructor and controls on parameters in this class.
 * */
public class SensitivityAnalysisMorrisDistanceBasedParameters {

    public MainApp mainApp;

    public Model model;

    public SensitivityAnalysisMorrisPanel analysisPanel;

    public ISimulator simulator;

    /**
	 * expected p times 2. Lowest and highest value of each parameter.
	 * */
    public double[][] parameterLimits;

    /**
	 * list of names of parameters
	 * */
    public String[] parameterNames;

    public SimulatorParameters simulatorParameters;

    public double startTime;

    public double endTime;

    public int numTimePoints;

    public String[] mRequestedSymbolNames;

    public int gridLevel;

    public int numOfRandomPoints;
}
