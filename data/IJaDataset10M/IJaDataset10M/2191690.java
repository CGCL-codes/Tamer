package playground.scnadine.gpsCompareWithFlammData.stopPointStagesAlgorithms.stopPointStageAlgorithm;

import org.matsim.core.config.Config;
import playground.scnadine.fuzzy.EvaluationException;
import playground.scnadine.fuzzy.FuzzyBlockOfRules;
import playground.scnadine.fuzzy.FuzzyEngine;
import playground.scnadine.fuzzy.LinguisticVariable;
import playground.scnadine.fuzzy.NoRulesFiredException;
import playground.scnadine.fuzzy.RulesParsingException;
import playground.scnadine.gpsCompareWithFlammData.GPSStopPointStage;
import playground.scnadine.gpsCompareWithFlammData.GPSStopPointStages;

public class GPSStopPointStageModeDetection extends GPSStopPointStageAlgorithm {

    private Config config;

    private String CONFIG_MODULE;

    private double medSpeedWalkStart, medSpeedWalkTopLeft, medSpeedWalkTopRight, medSpeedWalkEnd;

    private double medSpeedLowStart, medSpeedLowTopLeft, medSpeedLowTopRight, medSpeedLowEnd;

    private double medSpeedMediumStart, medSpeedMediumTopLeft, medSpeedMediumTopRight, medSpeedMediumEnd;

    private double medSpeedHighStart, medSpeedHighTopLeft, medSpeedHighTopRight, medSpeedHighEnd;

    private double ninetyFiveAccLowStart, ninetyFiveAccLowTopLeft, ninetyFiveAccLowTopRight, ninetyFiveAccLowEnd;

    private double ninetyFiveAccMediumStart, ninetyFiveAccMediumTopLeft, ninetyFiveAccMediumTopRight, ninetyFiveAccMediumEnd;

    private double ninetyFiveAccHighStart, ninetyFiveAccHighTopLeft, ninetyFiveAccHighTopRight, ninetyFiveAccHighEnd;

    private double ninetyFiveSpeedLowStart, ninetyFiveSpeedLowTopLeft, ninetyFiveSpeedLowTopRight, ninetyFiveSpeedLowEnd;

    private double ninetyFiveSpeedMediumStart, ninetyFiveSpeedMediumTopLeft, ninetyFiveSpeedMediumTopRight, ninetyFiveSpeedMediumEnd;

    private double ninetyFiveSpeedHighStart, ninetyFiveSpeedHighTopLeft, ninetyFiveSpeedHighTopRight, ninetyFiveSpeedHighEnd;

    private LinguisticVariable ninetyFiveSpeed = new LinguisticVariable("ninetyFiveSpeed");

    private LinguisticVariable medSpeed = new LinguisticVariable("medSpeed");

    private LinguisticVariable ninetyFiveAcc = new LinguisticVariable("ninetyFiveAcc");

    private String[] ruleStrings;

    public GPSStopPointStageModeDetection(Config config, String CONFIG_MODULE) {
        super();
        this.config = config;
        this.CONFIG_MODULE = CONFIG_MODULE;
        this.readModeDetectionParameters();
        this.setLinguisticVariables();
    }

    @Override
    public void run(GPSStopPointStages stages) {
        for (GPSStopPointStage stage : stages.getStages()) {
            FuzzyEngine fuzzyEngine;
            fuzzyEngine = new FuzzyEngine();
            LinguisticVariable mode = new LinguisticVariable("mode");
            mode.add("walk", 1, 1, 1, 1);
            mode.add("bike", 2, 2, 2, 2);
            mode.add("car", 3, 3, 3, 3);
            mode.add("urbanPuT", 4, 4, 4, 4);
            mode.add("rail", 5, 5, 5, 5);
            FuzzyBlockOfRules rules = this.setRules();
            fuzzyEngine.register(this.ninetyFiveSpeed);
            fuzzyEngine.register(this.medSpeed);
            fuzzyEngine.register(this.ninetyFiveAcc);
            fuzzyEngine.register(mode);
            fuzzyEngine.register(rules);
            this.medSpeed.setInputValue(stage.getMedianSpeed());
            this.ninetyFiveAcc.setInputValue(stage.getNinetyFivePercAcceleration());
            this.ninetyFiveSpeed.setInputValue(stage.getNinetyFivePercSpeed());
            try {
                rules.parseBlock();
                rules.evaluateBlock();
            } catch (RulesParsingException e) {
                e.printStackTrace();
            } catch (EvaluationException e) {
                e.printStackTrace();
            }
            try {
                stage.setFuzzyModes(mode.defuzzifyGPS());
                if (stage.getFuzzyModeProbability("walk") > 0.5) stage.setMostProbableMode("walk"); else if (stage.getFuzzyModeProbability("bike") > 0.5) stage.setMostProbableMode("bike"); else if (stage.getFuzzyModeProbability("car") > 0.5) stage.setMostProbableMode("car"); else if (stage.getFuzzyModeProbability("urbanPuT") > 0.5) stage.setMostProbableMode("urbanPuT"); else if (stage.getFuzzyModeProbability("rail") > 0.5) stage.setMostProbableMode("rail"); else stage.setMostProbableMode("other");
            } catch (NoRulesFiredException e) {
                e.printStackTrace();
            }
        }
    }

    private void readModeDetectionParameters() {
        this.medSpeedWalkStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedWalkStart"));
        this.medSpeedWalkTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedWalkTopLeft"));
        this.medSpeedWalkTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedWalkTopRight"));
        this.medSpeedWalkEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedWalkEnd"));
        this.medSpeedLowStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedLowStart"));
        this.medSpeedLowTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedLowTopLeft"));
        this.medSpeedLowTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedLowTopRight"));
        this.medSpeedLowEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedLowEnd"));
        this.medSpeedMediumStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedMediumStart"));
        this.medSpeedMediumTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedMediumTopLeft"));
        this.medSpeedMediumTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedMediumTopRight"));
        this.medSpeedMediumEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedMediumEnd"));
        this.medSpeedHighStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedHighStart"));
        this.medSpeedHighTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedHighTopLeft"));
        this.medSpeedHighTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedHighTopRight"));
        this.medSpeedHighEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "medSpeedHighEnd"));
        this.ninetyFiveAccLowStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccLowStart"));
        this.ninetyFiveAccLowTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccLowTopLeft"));
        this.ninetyFiveAccLowTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccLowTopRight"));
        this.ninetyFiveAccLowEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccLowEnd"));
        this.ninetyFiveAccMediumStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccMediumStart"));
        this.ninetyFiveAccMediumTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccMediumTopLeft"));
        this.ninetyFiveAccMediumTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccMediumTopRight"));
        this.ninetyFiveAccMediumEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccMediumEnd"));
        this.ninetyFiveAccHighStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccHighStart"));
        this.ninetyFiveAccHighTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccHighTopLeft"));
        this.ninetyFiveAccHighTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccHighTopRight"));
        this.ninetyFiveAccHighEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveAccHighEnd"));
        this.ninetyFiveSpeedLowStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedLowStart"));
        this.ninetyFiveSpeedLowTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedLowTopLeft"));
        this.ninetyFiveSpeedLowTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedLowTopRight"));
        this.ninetyFiveSpeedLowEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedLowEnd"));
        this.ninetyFiveSpeedMediumStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedMediumStart"));
        this.ninetyFiveSpeedMediumTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedMediumTopLeft"));
        this.ninetyFiveSpeedMediumTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedMediumTopRight"));
        this.ninetyFiveSpeedMediumEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedMediumEnd"));
        this.ninetyFiveSpeedHighStart = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedHighStart"));
        this.ninetyFiveSpeedHighTopLeft = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedHighTopLeft"));
        this.ninetyFiveSpeedHighTopRight = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedHighTopRight"));
        this.ninetyFiveSpeedHighEnd = Double.parseDouble(config.findParam(CONFIG_MODULE, "ninetyFiveSpeedHighEnd"));
    }

    private void setLinguisticVariables() {
        this.medSpeed.add("walk", this.medSpeedWalkStart, this.medSpeedWalkTopLeft, this.medSpeedWalkTopRight, this.medSpeedWalkEnd);
        this.medSpeed.add("low", this.medSpeedLowStart, this.medSpeedLowTopLeft, this.medSpeedLowTopRight, this.medSpeedLowEnd);
        this.medSpeed.add("medium", this.medSpeedMediumStart, this.medSpeedMediumTopLeft, this.medSpeedMediumTopRight, this.medSpeedMediumEnd);
        this.medSpeed.add("high", this.medSpeedHighStart, this.medSpeedHighTopLeft, this.medSpeedHighTopRight, this.medSpeedHighEnd);
        this.ninetyFiveAcc.add("low", this.ninetyFiveAccLowStart, this.ninetyFiveAccLowTopLeft, this.ninetyFiveAccLowTopRight, this.ninetyFiveAccLowEnd);
        this.ninetyFiveAcc.add("medium", this.ninetyFiveAccMediumStart, this.ninetyFiveAccMediumTopLeft, this.ninetyFiveAccMediumTopRight, this.ninetyFiveAccMediumEnd);
        this.ninetyFiveAcc.add("high", this.ninetyFiveAccHighStart, this.ninetyFiveAccHighTopLeft, this.ninetyFiveAccHighTopRight, this.ninetyFiveAccHighEnd);
        this.ninetyFiveSpeed.add("low", this.ninetyFiveSpeedLowStart, this.ninetyFiveSpeedLowTopLeft, this.ninetyFiveSpeedLowTopRight, this.ninetyFiveSpeedLowEnd);
        this.ninetyFiveSpeed.add("medium", this.ninetyFiveSpeedMediumStart, this.ninetyFiveSpeedMediumTopLeft, this.ninetyFiveSpeedMediumTopRight, this.ninetyFiveSpeedMediumEnd);
        this.ninetyFiveSpeed.add("high", this.ninetyFiveSpeedHighStart, this.ninetyFiveSpeedHighTopLeft, this.ninetyFiveSpeedHighTopRight, this.ninetyFiveSpeedHighEnd);
    }

    /**Generate the block of rules*/
    private FuzzyBlockOfRules setRules() {
        ruleStrings = new String[Integer.parseInt(config.findParam(CONFIG_MODULE, "numberOfFuzzyRules"))];
        for (int i = 0; i < ruleStrings.length; i++) {
            ruleStrings[i] = config.findParam(CONFIG_MODULE, "rule" + i);
        }
        return new FuzzyBlockOfRules(this.ruleStrings);
    }
}
