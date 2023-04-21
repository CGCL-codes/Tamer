package de.ibis.permoto.model.definitions.impl;

import java.math.BigInteger;
import java.util.List;
import de.ibis.permoto.model.basic.predictionbusinesscase.GranularityType;
import de.ibis.permoto.model.basic.predictionbusinesscase.MeasureTypeType;
import de.ibis.permoto.model.basic.predictionbusinesscase.ObjectFactory;
import de.ibis.permoto.model.basic.predictionbusinesscase.PredictionSolverSection;
import de.ibis.permoto.model.basic.predictionbusinesscase.StationPerformanceIndex;
import de.ibis.permoto.model.basic.scenario.Class;
import de.ibis.permoto.model.basic.scenario.Station;
import de.ibis.permoto.model.definitions.IPredictionSolverSection;

/**
 * @author Slavko Segota
 */
public class PerMoToPredictionSolverSection extends PredictionSolverSection implements IPredictionSolverSection {

    PerMoToPredictionSolverSection() {
        super();
        this.analyticSolverParameters = null;
        this.simulationSolverParameters = null;
    }

    PerMoToPredictionSolverSection(PredictionSolverSection pss) {
        super();
        this.analyticSolverParameters = pss.getAnalyticSolverParameters();
        this.simulationSolverParameters = pss.getSimulationSolverParameters();
    }

    public boolean areAnalyticSolverParametersSet() {
        if (this.analyticSolverParameters != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean areSimulationSolverParametersSet() {
        if (this.simulationSolverParameters != null) {
            return true;
        } else {
            return false;
        }
    }

    public Double getEpsilon() {
        if (this.analyticSolverParameters != null) {
            return this.analyticSolverParameters.getEpsilon();
        } else {
            return null;
        }
    }

    public GranularityType getGranularity() {
        if (this.analyticSolverParameters != null) {
            return this.analyticSolverParameters.getGranularity();
        } else {
            return null;
        }
    }

    public Boolean isInfiniteTime() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.isInfiniteTime();
        } else {
            return null;
        }
    }

    public Double getMaxDuration() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.getMaxDuration();
        } else {
            return null;
        }
    }

    public Double getMaxLoadFactor() {
        if (this.analyticSolverParameters != null) {
            return this.analyticSolverParameters.getMaxLoadFactor();
        } else {
            return null;
        }
    }

    public BigInteger getMaxNrSamples() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.getMaxNrSamples();
        } else {
            return null;
        }
    }

    public MeasureTypeType getMeasureType() {
        if (this.analyticSolverParameters != null) {
            List<MeasureTypeType> theList = this.analyticSolverParameters.getMeasureType();
            if (theList.size() == 0) {
                theList.add(MeasureTypeType.CLASS_RESPONSE_TIME);
            }
            return this.analyticSolverParameters.getMeasureType().get(0);
        } else {
            return null;
        }
    }

    public BigInteger getNumberOfSteps() {
        if (this.analyticSolverParameters != null) {
            return this.analyticSolverParameters.getNumberOfSteps();
        } else {
            return null;
        }
    }

    public BigInteger getRandomSeed() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.getRandomSeed();
        } else {
            return null;
        }
    }

    public Boolean isRandomUsage() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.isRandomUsage();
        } else {
            return null;
        }
    }

    public Boolean isNoAutomaticStop() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.isNoAutomaticStop();
        } else {
            return null;
        }
    }

    public void setEpsilon(Double value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.setEpsilon(value);
        }
    }

    public void setGranularity(GranularityType value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.setGranularity(value);
        }
    }

    public void setInfiniteTime(Boolean value) {
        if (this.simulationSolverParameters != null) {
            this.simulationSolverParameters.setInfiniteTime(value);
        }
    }

    public void setIsNoAutomaticStop(Boolean value) {
        if (this.simulationSolverParameters != null) {
            this.simulationSolverParameters.setNoAutomaticStop(value);
        }
    }

    public void setMaxDuration(Double value) {
        if (this.simulationSolverParameters != null) {
            this.simulationSolverParameters.setMaxDuration(value);
        }
    }

    public void setMaxLoadFatcor(Double value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.setMaxLoadFactor(value);
        }
    }

    public void setMaxNrSamples(BigInteger value) {
        if (this.simulationSolverParameters != null) {
            this.simulationSolverParameters.setMaxNrSamples(value);
        }
    }

    public void setMeasureType(MeasureTypeType value) {
        if (this.analyticSolverParameters != null) {
            List<MeasureTypeType> measureTypeList = this.analyticSolverParameters.getMeasureType();
            measureTypeList.clear();
            measureTypeList.add(value);
        }
    }

    public void setNumberOfSteps(BigInteger value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.setNumberOfSteps(value);
        }
    }

    public void setRandomSeed(BigInteger value) {
        if (this.simulationSolverParameters != null) {
            this.simulationSolverParameters.setRandomSeed(value);
        }
    }

    public void setRandomUsage(Boolean value) {
        if (this.simulationSolverParameters != null) {
            this.simulationSolverParameters.setRandomUsage(value);
        }
    }

    public void setAnalyticSolverParameters(Boolean value) {
        if (value) {
            if (this.analyticSolverParameters == null) {
                this.analyticSolverParameters = new ObjectFactory().createAnalyticSolverParameters();
            }
        } else {
            this.analyticSolverParameters = null;
        }
    }

    public void setSimulationSolverParameters(Boolean value) {
        if (value) {
            if (this.simulationSolverParameters == null) {
                this.simulationSolverParameters = new ObjectFactory().createSimulationSolverParameters();
            }
        } else {
            this.simulationSolverParameters = null;
        }
    }

    public List<String> getAlgorithms() {
        if (this.analyticSolverParameters != null) {
            return this.analyticSolverParameters.getAlgorithm();
        } else {
            return null;
        }
    }

    public void addAlgorithm(String value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.getAlgorithm().add(value);
        }
    }

    public void setAlgorithms(List<String> value) {
        if (this.analyticSolverParameters != null) {
            List<String> algs = this.analyticSolverParameters.getAlgorithm();
            algs.clear();
            algs.addAll(value);
        }
    }

    public void addStationPerformanceIndex(Station station, Class clazz, MeasureTypeType type, Double conInt, Double maxRelError) {
        if (this.simulationSolverParameters != null) {
            StationPerformanceIndex spi = new ObjectFactory().createStationPerformanceIndex();
            spi.setClazz(clazz);
            spi.setStation(station);
            spi.setMeasure(type);
            spi.setConfInt(conInt);
            spi.setMaxRelError(maxRelError);
            this.simulationSolverParameters.getStationPerformanceIndex().add(spi);
        }
    }

    public List<StationPerformanceIndex> getPerfomanceIndices() {
        if (this.simulationSolverParameters != null) {
            return this.simulationSolverParameters.getStationPerformanceIndex();
        } else return null;
    }

    public void clearStationPerformanceIndices() {
        if (this.simulationSolverParameters != null) {
            if (this.simulationSolverParameters.getStationPerformanceIndex() != null) {
                this.simulationSolverParameters.getStationPerformanceIndex().clear();
            }
        }
    }

    public void removeStationPerformanceIndexAt(int index) {
        if (this.simulationSolverParameters != null) {
            if (this.simulationSolverParameters.getStationPerformanceIndex() != null) {
                if (index >= 0 && index < this.simulationSolverParameters.getStationPerformanceIndex().size()) {
                    this.simulationSolverParameters.getStationPerformanceIndex().remove(index);
                }
            }
        }
    }

    public StationPerformanceIndex getPerformanceIndexAt(int index) {
        if (this.simulationSolverParameters != null) {
            if (this.simulationSolverParameters.getStationPerformanceIndex() != null) {
                if (index >= 0 && index < this.simulationSolverParameters.getStationPerformanceIndex().size()) {
                    return this.simulationSolverParameters.getStationPerformanceIndex().get(index);
                }
            }
        }
        return null;
    }

    public boolean isWhatIfAnalysisSet() {
        return this.analyticSolverParameters.isWhatIfAnalyis();
    }

    public void setWhatIfAnalysis(boolean value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.setWhatIfAnalyis(value);
        }
    }

    public Integer getPopulation() {
        if (this.analyticSolverParameters != null) {
            return this.analyticSolverParameters.getPopulation();
        } else {
            return null;
        }
    }

    public void setPopulation(Integer value) {
        if (this.analyticSolverParameters != null) {
            this.analyticSolverParameters.setPopulation(value);
        }
    }
}
