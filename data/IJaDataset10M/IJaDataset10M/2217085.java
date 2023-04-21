package net.sf.doolin.app.sc.game.service;

public interface GameParameters {

    double getExtractionLevelPower();

    double getExtractionRatio();

    int getInitialPopulation();

    long getInitialResources();

    long getInitialStock();

    int getPopulationMax();

    double getQualityGFactor();

    double getQualityRatio();

    int getTechBaseInvestment();

    double getTechInvestmentWasteFactor();

    double getTerraformingCost();

    double getTerraformingGRatio();

    double getTerraformingQualityTolerance();
}
