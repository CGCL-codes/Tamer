package spaceopera.universe.ai;

/**
 * Each species has a preferred range values for the different planet attributes like temperature, air composition...
 * all temperatures in degrees Celsius
 * default values are for humans and for aliens with preferred planet type=earthlike
 * 
 * used in 
 * - Planet.getValue
 * - Planet.getNeededColonyModuleName
 * - Planet.improveXXX
 * - Planet.getMaxPopulationCount
 * - CPlayer.setPreferredPlanetValues
 * - Colony.buildingsWithSpecialEffects
 */
public class PreferredPlanetValues {

    float lowerPoorSeasons = 0.2f;

    float lowerAcceptableSeasons = 0.5f;

    float lowerIdealSeasons = 0.8f;

    float upperIdealSeasons = 1.2f;

    float upperAcceptableSeasons = 1.5f;

    float upperPoorSeasons = 1.8f;

    float lowerExtremeRadiation = 0;

    float lowerAcceptableRadiation = 100;

    float lowerIdealRadiation = 200;

    float upperIdealRadiation = 400;

    float upperAcceptableRadiation = 600;

    float upperExtremeRadiation = 800;

    float lowerExtremeAtmosphericComposition = 0.4f;

    float lowerAcceptableAtmosphericComposition = 0.6f;

    float lowerIdealAtmosphericComposition = 0.8f;

    float upperIdealAtmosphericComposition = 1.3f;

    float upperAcceptableAtmosphericComposition = 1.6f;

    float upperExtremeAtmosphericComposition = 1.8f;

    float lowerExtremeAtmosphericPressure = 0.4f;

    float lowerAcceptableAtmosphericPressure = 0.6f;

    float lowerIdealAtmosphericPressure = 0.8f;

    float upperIdealAtmosphericPressure = 1.3f;

    float upperAcceptableAtmosphericPressure = 1.8f;

    float upperExtremeAtmosphericPressure = 2.5f;

    int lowerExtremeTemperature = -10;

    int lowerAcceptableTemperature = 0;

    int lowerGoodTemperature = 10;

    int lowerIdealTemperature = 16;

    int upperIdealTemperature = 24;

    int upperGoodTemperature = 30;

    int upperAcceptableTemperature = 40;

    int upperExtremeTemperature = 50;

    float lowerExtremeGravitation = 0.2f;

    float lowerAcceptableGravitation = 0.5f;

    float lowerIdealGravitation = 0.8f;

    float upperIdealGravitation = 1.2f;

    float upperAcceptableGravitation = 1.5f;

    float upperExtremeGravitation = 2.5f;

    float lowerExtremeWaterLandRatio = 0.2f;

    float lowerAcceptableWaterLandRatio = 0.4f;

    float lowerIdealWaterLandRatio = 0.7f;

    float upperIdealWaterLandRatio = 1.3f;

    float upperAcceptableWaterLandRatio = 1.6f;

    float upperExtremeWaterLandRatio = 1.8f;

    /**windspeed */
    float lowerPoorWindSpeed = 2;

    float lowerAcceptableWindSpeed = 5;

    float lowerIdealWindSpeed = 10;

    float upperIdealWindSpeed = 20;

    float upperAcceptableWindSpeed = 30;

    float upperPoorWindSpeed = 50;

    public boolean lessThanIdealTemperature(double avgTemp) {
        return (avgTemp < lowerIdealTemperature);
    }

    public boolean moreThanIdealTemperature(double avgTemp) {
        return (avgTemp >= upperIdealTemperature);
    }

    public boolean isTemperatureIdeal(double avgTemp) {
        return (avgTemp >= lowerIdealTemperature && avgTemp < upperIdealTemperature);
    }

    public boolean isTemperatureGood(double avgTemp) {
        return (avgTemp >= lowerGoodTemperature && avgTemp < lowerIdealTemperature) || (avgTemp >= upperIdealTemperature && avgTemp < upperGoodTemperature);
    }

    public boolean isTemperatureAcceptable(double avgTemp) {
        return (avgTemp >= lowerAcceptableTemperature && avgTemp < lowerGoodTemperature) || (avgTemp >= upperGoodTemperature && avgTemp < upperAcceptableTemperature);
    }

    public boolean isTemperatureExtreme(double avgTemp) {
        return (avgTemp >= lowerExtremeTemperature && avgTemp < lowerAcceptableTemperature) || (avgTemp >= upperAcceptableTemperature && avgTemp < upperExtremeTemperature);
    }

    public boolean isTemperatureOutOfRange(double avgTemp) {
        return (avgTemp < lowerExtremeTemperature || avgTemp >= upperExtremeTemperature);
    }

    public boolean airPressureOutOfRange(double airPressure) {
        return (airPressure < lowerExtremeAtmosphericPressure) || (airPressure >= upperExtremeAtmosphericPressure);
    }

    public boolean airPressureIsExtreme(double airPressure) {
        return (airPressure >= lowerExtremeAtmosphericPressure && airPressure < lowerAcceptableAtmosphericPressure) || (airPressure >= upperAcceptableAtmosphericPressure && airPressure < upperExtremeAtmosphericPressure);
    }

    public boolean airPressureIsAcceptable(double airPressure) {
        return (airPressure >= lowerAcceptableAtmosphericPressure && airPressure < lowerIdealAtmosphericPressure) || (airPressure >= upperIdealAtmosphericPressure && airPressure < upperAcceptableAtmosphericPressure);
    }

    public boolean airPressureIsIdeal(double airPressure) {
        return (airPressure >= lowerIdealAtmosphericPressure && airPressure < upperIdealAtmosphericPressure);
    }

    public boolean moreThanIdealAtmosphericPressure(double airPressure) {
        return airPressure >= upperIdealAtmosphericPressure;
    }

    public boolean lessThanIdealAtmosphericPressure(double airPressure) {
        return airPressure < lowerIdealAtmosphericPressure;
    }

    public boolean atmosphericCompositionIsOutOfRange(double atmosphericComposition) {
        return (atmosphericComposition < lowerExtremeAtmosphericComposition || atmosphericComposition >= upperExtremeAtmosphericComposition);
    }

    public boolean atmosphericCompositionIsExtreme(double atmosphericComposition) {
        return (atmosphericComposition >= lowerExtremeAtmosphericComposition && atmosphericComposition < lowerAcceptableAtmosphericComposition) || (atmosphericComposition >= upperAcceptableAtmosphericComposition && atmosphericComposition < upperExtremeAtmosphericComposition);
    }

    public boolean atmosphericCompositionIsAcceptable(double atmosphericComposition) {
        return (atmosphericComposition >= lowerAcceptableAtmosphericComposition && atmosphericComposition < lowerIdealAtmosphericComposition) || (atmosphericComposition >= upperIdealAtmosphericComposition && atmosphericComposition < upperAcceptableAtmosphericComposition);
    }

    public boolean atmosphericCompositionIsIdeal(double atmosphericComposition) {
        return (atmosphericComposition >= lowerIdealAtmosphericComposition && atmosphericComposition < upperIdealAtmosphericComposition);
    }

    public boolean lessThanIdealAtmosphericComposition(double atmosphericComposition) {
        return (atmosphericComposition < lowerIdealAtmosphericComposition);
    }

    public boolean moreThanIdealAtmosphericComposition(double atmosphericComposition) {
        return (atmosphericComposition >= upperIdealAtmosphericComposition);
    }

    public boolean gravitationIsIdeal(double gravitation) {
        return (gravitation >= lowerIdealGravitation) && (gravitation < upperIdealGravitation);
    }

    public boolean gravitationIsAcceptable(double gravitation) {
        return (gravitation >= lowerAcceptableGravitation) && (gravitation < lowerIdealGravitation) || (gravitation >= upperIdealGravitation) && (gravitation < upperAcceptableGravitation);
    }

    public boolean gravitationIsExtreme(double gravitation) {
        return (gravitation >= lowerExtremeGravitation) && (gravitation < lowerAcceptableGravitation) || (gravitation >= upperAcceptableGravitation) && (gravitation < upperExtremeGravitation);
    }

    public boolean gravitationIsOutOfRange(double gravitation) {
        return (gravitation < lowerExtremeGravitation || gravitation >= upperExtremeGravitation);
    }

    public boolean moreThanIdealGravitation(double gravitation) {
        return (gravitation >= upperIdealGravitation);
    }

    public boolean lessThanIdealGravitation(double gravitation) {
        return (gravitation < lowerIdealGravitation);
    }

    public boolean lessThanIdealRadiation(double avgTemp) {
        return (avgTemp < lowerIdealRadiation);
    }

    public boolean moreThanIdealRadiation(double avgTemp) {
        return (avgTemp >= upperIdealRadiation);
    }

    public boolean radiationIsIdeal(double avgTemp) {
        return (avgTemp >= lowerIdealRadiation && avgTemp < upperIdealRadiation);
    }

    public boolean radiationIsAcceptable(double avgTemp) {
        return (avgTemp >= lowerAcceptableRadiation && avgTemp < lowerIdealRadiation) || (avgTemp >= upperIdealRadiation && avgTemp < upperAcceptableRadiation);
    }

    public boolean radiationIsExtreme(double avgTemp) {
        return (avgTemp >= lowerExtremeRadiation && avgTemp < lowerAcceptableRadiation) || (avgTemp >= upperAcceptableRadiation && avgTemp < upperExtremeRadiation);
    }

    public boolean radiationIsOutOfRange(double avgTemp) {
        return (avgTemp < lowerExtremeRadiation || avgTemp >= upperExtremeRadiation);
    }

    public boolean seasonsAreIdeal(double seasons) {
        return (seasons >= lowerIdealSeasons) && (seasons < upperIdealSeasons);
    }

    public boolean seasonsAreAcceptable(double seasons) {
        return (seasons >= lowerAcceptableSeasons) && (seasons < lowerIdealSeasons) || (seasons >= upperIdealSeasons) && (seasons < upperAcceptableSeasons);
    }

    public boolean seasonsArePoor(double seasons) {
        return (seasons >= lowerPoorSeasons) && (seasons < lowerAcceptableSeasons) || (seasons >= upperAcceptableSeasons) && (seasons < upperPoorSeasons);
    }

    public boolean seasonsAreExtreme(double seasons) {
        return (seasons >= upperPoorSeasons) || (seasons < lowerPoorSeasons);
    }

    public boolean moreThanIdealSeasons(double seasons) {
        return (seasons >= upperIdealSeasons);
    }

    public boolean lessThanIdealSeasons(double seasons) {
        return (seasons < lowerIdealSeasons);
    }

    public boolean waterLandRatioIsExtreme(double waterLandRatio) {
        return (waterLandRatio >= upperExtremeWaterLandRatio) || (waterLandRatio < lowerExtremeWaterLandRatio);
    }

    public boolean waterLandRatioIsPoor(double waterLandRatio) {
        return (waterLandRatio >= lowerExtremeWaterLandRatio) && (waterLandRatio < lowerAcceptableWaterLandRatio) || (waterLandRatio >= upperAcceptableWaterLandRatio) && (waterLandRatio < upperExtremeWaterLandRatio);
    }

    public boolean waterLandRatioIsAverage(double waterLandRatio) {
        return (waterLandRatio >= lowerAcceptableWaterLandRatio) && (waterLandRatio < lowerIdealWaterLandRatio) || (waterLandRatio >= upperIdealWaterLandRatio) && (waterLandRatio < upperAcceptableWaterLandRatio);
    }

    public boolean waterLandRatioIsIdeal(double waterLandRatio) {
        return (waterLandRatio >= lowerIdealWaterLandRatio) && (waterLandRatio < upperIdealWaterLandRatio);
    }

    public boolean moreThanIdealWaterLandRatio(double waterLandRatio) {
        return (waterLandRatio >= upperIdealWaterLandRatio);
    }

    public boolean lessThanIdealWaterLandRatio(double waterLandRatio) {
        return (waterLandRatio < lowerIdealWaterLandRatio);
    }

    public boolean windSpeedIsIdeal(double windSpeed) {
        return (windSpeed >= lowerIdealWindSpeed) && (windSpeed < upperIdealWindSpeed);
    }

    public boolean windSpeedIsAcceptable(double windSpeed) {
        return (windSpeed >= lowerAcceptableWindSpeed) && (windSpeed < lowerIdealWindSpeed) || (windSpeed >= upperIdealWindSpeed) && (windSpeed < upperAcceptableWindSpeed);
    }

    public boolean windSpeedIsPoor(double windSpeed) {
        return (windSpeed >= lowerPoorWindSpeed) && (windSpeed < lowerAcceptableWindSpeed) || (windSpeed >= upperAcceptableWindSpeed) && (windSpeed < upperPoorWindSpeed);
    }

    public boolean windSpeedIsExtreme(double windSpeed) {
        return (windSpeed < lowerPoorWindSpeed || windSpeed >= upperPoorWindSpeed);
    }

    public boolean moreThanIdealWindSpeed(double windSpeed) {
        return (windSpeed >= upperIdealWindSpeed);
    }

    public boolean lessThanIdealWindSpeed(double windSpeed) {
        return (windSpeed < lowerIdealWindSpeed);
    }

    public void setLowerExtremeTemperature(int lowerExtremeTemperature) {
        this.lowerExtremeTemperature = lowerExtremeTemperature;
    }

    public void setLowerAcceptableTemperature(int lowerAcceptableTemperature) {
        this.lowerAcceptableTemperature = lowerAcceptableTemperature;
    }

    public void setLowerGoodTemperature(int lowerGoodTemperature) {
        this.lowerGoodTemperature = lowerGoodTemperature;
    }

    public void setLowerIdealTemperature(int lowerIdealTemperature) {
        this.lowerIdealTemperature = lowerIdealTemperature;
    }

    public void setUpperIdealTemperature(int upperIdealTemperature) {
        this.upperIdealTemperature = upperIdealTemperature;
    }

    public void setUpperGoodTemperature(int upperGoodTemperature) {
        this.upperGoodTemperature = upperGoodTemperature;
    }

    public void setUpperAcceptableTemperature(int upperAcceptableTemperature) {
        this.upperAcceptableTemperature = upperAcceptableTemperature;
    }

    public void setUpperExtremeTemperature(int upperExtremeTemperature) {
        this.upperExtremeTemperature = upperExtremeTemperature;
    }

    public void setLowerAcceptableAtmosphericPressure(float lowerAcceptableAtmosphericPressure) {
        this.lowerAcceptableAtmosphericPressure = lowerAcceptableAtmosphericPressure;
    }

    public void setLowerExtremeAtmosphericPressure(float lowerExtremeAtmosphericPressure) {
        this.lowerExtremeAtmosphericPressure = lowerExtremeAtmosphericPressure;
    }

    public void setLowerIdealAtmosphericPressure(float lowerIdealAtmosphericPressure) {
        this.lowerIdealAtmosphericPressure = lowerIdealAtmosphericPressure;
    }

    public void setUpperAcceptableAtmosphericPressure(float upperAcceptableAtmosphericPressure) {
        this.upperAcceptableAtmosphericPressure = upperAcceptableAtmosphericPressure;
    }

    public void setUpperExtremeAtmosphericPressure(float upperExtremeAtmosphericPressure) {
        this.upperExtremeAtmosphericPressure = upperExtremeAtmosphericPressure;
    }

    public void setUpperIdealAtmosphericPressure(float upperIdealAtmosphericPressure) {
        this.upperIdealAtmosphericPressure = upperIdealAtmosphericPressure;
    }

    public void setLowerAcceptableRadiation(float lowerAcceptableRadiation) {
        this.lowerAcceptableRadiation = lowerAcceptableRadiation;
    }

    public void setLowerExtremeRadiation(float lowerExtremeRadiation) {
        this.lowerExtremeRadiation = lowerExtremeRadiation;
    }

    public void setLowerIdealRadiation(float lowerIdealRadiation) {
        this.lowerIdealRadiation = lowerIdealRadiation;
    }

    public void setUpperAcceptableRadiation(float upperAcceptableRadiation) {
        this.upperAcceptableRadiation = upperAcceptableRadiation;
    }

    public void setUpperExtremeRadiation(float upperExtremeRadiation) {
        this.upperExtremeRadiation = upperExtremeRadiation;
    }

    public void setUpperIdealRadiation(float upperIdealRadiation) {
        this.upperIdealRadiation = upperIdealRadiation;
    }

    public void setLowerAcceptableAtmosphericComposition(float lowerAcceptableAtmosphericComposition) {
        this.lowerAcceptableAtmosphericComposition = lowerAcceptableAtmosphericComposition;
    }

    public void setLowerExtremeAtmosphericComposition(float lowerExtremeAtmosphericComposition) {
        this.lowerExtremeAtmosphericComposition = lowerExtremeAtmosphericComposition;
    }

    public void setLowerIdealAtmosphericComposition(float lowerIdealAtmosphericComposition) {
        this.lowerIdealAtmosphericComposition = lowerIdealAtmosphericComposition;
    }

    public void setUpperAcceptableAtmosphericComposition(float upperAcceptableAtmosphericComposition) {
        this.upperAcceptableAtmosphericComposition = upperAcceptableAtmosphericComposition;
    }

    public void setUpperExtremeAtmosphericComposition(float upperExtremeAtmosphericComposition) {
        this.upperExtremeAtmosphericComposition = upperExtremeAtmosphericComposition;
    }

    public void setUpperIdealAtmosphericComposition(float upperIdealAtmosphericComposition) {
        this.upperIdealAtmosphericComposition = upperIdealAtmosphericComposition;
    }

    public void setLowerAcceptableGravitation(float lowerAcceptableGravitation) {
        this.lowerAcceptableGravitation = lowerAcceptableGravitation;
    }

    public void setLowerExtremeGravitation(float lowerExtremeGravitation) {
        this.lowerExtremeGravitation = lowerExtremeGravitation;
    }

    public void setLowerIdealGravitation(float lowerIdealGravitation) {
        this.lowerIdealGravitation = lowerIdealGravitation;
    }

    public void setUpperAcceptableGravitation(float upperAcceptableGravitation) {
        this.upperAcceptableGravitation = upperAcceptableGravitation;
    }

    public void setUpperExtremeGravitation(float upperExtremeGravitation) {
        this.upperExtremeGravitation = upperExtremeGravitation;
    }

    public void setUpperIdealGravitation(float upperIdealGravitation) {
        this.upperIdealGravitation = upperIdealGravitation;
    }

    public void setLowerAcceptableSeasons(float lowerAcceptableSeasons) {
        this.lowerAcceptableSeasons = lowerAcceptableSeasons;
    }

    public void setLowerIdealSeasons(float lowerIdealSeasons) {
        this.lowerIdealSeasons = lowerIdealSeasons;
    }

    public void setLowerPoorSeasons(float lowerPoorSeasons) {
        this.lowerPoorSeasons = lowerPoorSeasons;
    }

    public void setUpperAcceptableSeasons(float upperAcceptableSeasons) {
        this.upperAcceptableSeasons = upperAcceptableSeasons;
    }

    public void setUpperIdealSeasons(float upperIdealSeasons) {
        this.upperIdealSeasons = upperIdealSeasons;
    }

    public void setUpperPoorSeasons(float upperPoorSeasons) {
        this.upperPoorSeasons = upperPoorSeasons;
    }

    public void setLowerAcceptableWaterLandRatio(float lowerAcceptableWaterLandRatio) {
        this.lowerAcceptableWaterLandRatio = lowerAcceptableWaterLandRatio;
    }

    public void setLowerExtremeWaterLandRatio(float lowerExtremeWaterLandRatio) {
        this.lowerExtremeWaterLandRatio = lowerExtremeWaterLandRatio;
    }

    public void setLowerIdealWaterLandRatio(float lowerIdealWaterLandRatio) {
        this.lowerIdealWaterLandRatio = lowerIdealWaterLandRatio;
    }

    public void setUpperAcceptableWaterLandRatio(float upperAcceptableWaterLandRatio) {
        this.upperAcceptableWaterLandRatio = upperAcceptableWaterLandRatio;
    }

    public void setUpperExtremeWaterLandRatio(float upperExtremeWaterLandRatio) {
        this.upperExtremeWaterLandRatio = upperExtremeWaterLandRatio;
    }

    public void setUpperIdealWaterLandRatio(float upperIdealWaterLandRatio) {
        this.upperIdealWaterLandRatio = upperIdealWaterLandRatio;
    }

    public void setLowerAcceptableWindSpeed(float lowerAcceptableWindSpeed) {
        this.lowerAcceptableWindSpeed = lowerAcceptableWindSpeed;
    }

    public void setLowerIdealWindSpeed(float lowerIdealWindSpeed) {
        this.lowerIdealWindSpeed = lowerIdealWindSpeed;
    }

    public void setLowerPoorWindSpeed(float lowerPoorWindSpeed) {
        this.lowerPoorWindSpeed = lowerPoorWindSpeed;
    }

    public void setUpperAcceptableWindSpeed(float upperAcceptableWindSpeed) {
        this.upperAcceptableWindSpeed = upperAcceptableWindSpeed;
    }

    public void setUpperIdealWindSpeed(float upperIdealWindSpeed) {
        this.upperIdealWindSpeed = upperIdealWindSpeed;
    }

    public void setUpperPoorWindSpeed(float upperPoorWindSpeed) {
        this.upperPoorWindSpeed = upperPoorWindSpeed;
    }
}
