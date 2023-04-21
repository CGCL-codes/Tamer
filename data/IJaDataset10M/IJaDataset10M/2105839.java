package org.cleartk.classifier.mallet.factory;

import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MCMaxEnt;
import cc.mallet.classify.MCMaxEntTrainer;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philip Ogren
 */
public class MCMaxEntTrainerFactory implements ClassifierTrainerFactory<MCMaxEnt> {

    public static final String NAME = "MCMaxEnt";

    public ClassifierTrainer<MCMaxEnt> createTrainer(String... args) {
        MCMaxEntTrainer trainer = new MCMaxEntTrainer();
        if (args != null) {
            if (args.length % 2 != 0) {
                throw new IllegalArgumentException("each argument must be supplied with a value:  " + getUsageMessage());
            }
            for (int i = 0; i < args.length; i += 2) {
                String optionName = args[i];
                String optionValue = args[i + 1];
                if (optionName.equals("--useHyperbolicPrior")) trainer.setUseHyperbolicPrior(Boolean.parseBoolean(optionValue)); else if (optionName.equals("--gaussianPriorVariance")) trainer.setGaussianPriorVariance(Double.parseDouble(optionValue)); else if (optionName.equals("--hyperbolicPriorSlope")) trainer.setHyperbolicPriorSlope(Double.parseDouble(optionValue)); else if (optionName.equals("--hyperbolicPriorSharpness")) trainer.setHyperbolicPriorSharpness(Double.parseDouble(optionValue)); else if (optionName.equals("--numIterations")) trainer.setNumIterations(Integer.parseInt(optionValue)); else throw new IllegalArgumentException(String.format("the argument %1$s is invalid.  ", optionName) + getUsageMessage());
            }
        }
        return trainer;
    }

    public String getUsageMessage() {
        return "The arguments for MCMaxEntTrainerFactory.createTrainer(String...args) should be either empty or include any of the following:" + "\n--useHyperbolicPrior boolean" + "\n--gaussianPriorVariance double" + "\n--hyperbolicPriorSlope double" + "\n--hyperbolicPriorSharpness double" + "\n--numIterations int";
    }
}
