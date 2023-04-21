package eu.planets_project.pp.plato.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import org.hibernate.annotations.MapKey;
import eu.planets_project.pp.plato.model.measurement.Measurement;
import eu.planets_project.pp.plato.model.measurement.Measurements;
import eu.planets_project.pp.plato.model.values.INumericValue;
import eu.planets_project.pp.plato.services.action.MigrationResult;
import flanagan.analysis.Regression;

/**
 * Holds measured values of a tool.
 * 
 * @author Christoph Becker
 *
 */
@Entity
public class ToolExperience implements Serializable {

    private static final long serialVersionUID = -2154004085898549126L;

    @Id
    @GeneratedValue
    private int id;

    /**
     * Maps property names to lists of measurements of this property 
     */
    @MapKey(columns = { @Column(name = "key_name") })
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "toolexp_measurements")
    private Map<String, Measurements> measurements = new HashMap<String, Measurements>();

    /**
     * Adds a Measurement to the list of measurements
     * uses its property name as key for this hashmap
     *   
     * @param m
     */
    public void addMeasurement(Measurement m) {
        Measurements ms = measurements.get(m.getProperty().getName());
        if (ms == null) {
            ms = new Measurements();
            measurements.put(m.getProperty().getName(), ms);
        }
        ms.addMeasurement(m);
    }

    /**
     * Calculates the average of all measurements for the given property
     * 
     * @param propertyName
     * @return
     */
    public Measurement getAverage(String propertyName) {
        Measurements m = measurements.get(propertyName);
        return (m == null ? null : m.getAverage());
    }

    /**
     * Returns the number of measurements of the given property
     * 
     * @param propertyName
     * @return
     */
    public int getSize(String propertyName) {
        Measurements m = measurements.get(propertyName);
        return (m == null ? 0 : m.getSize());
    }

    /**
     * Uses linear regression to calculate the tool's startup time based on the elapsed 
     * time and size of the file that has been migrated. 
     * 
     * @return startup time of the tool
     */
    public double getStartupTime() {
        Measurements elapsedTimeMeasurements = measurements.get(MigrationResult.MIGRES_USED_TIME);
        Measurements resultFileSizes = measurements.get(MigrationResult.MIGRES_RESULT_FILESIZE);
        Measurements relativeFileSizes = measurements.get(MigrationResult.MIGRES_RELATIVE_FILESIZE);
        if (elapsedTimeMeasurements == null || resultFileSizes == null || relativeFileSizes == null) {
            return 0.0;
        }
        if (!((elapsedTimeMeasurements.getSize() == resultFileSizes.getSize()) && (resultFileSizes.getSize() == relativeFileSizes.getSize()))) {
            return 0.0;
        }
        if (elapsedTimeMeasurements.getSize() <= 2) {
            return 0.0;
        }
        if (elapsedTimeMeasurements.getSize() <= 0) {
            return 0.0;
        }
        double[] xArray = new double[elapsedTimeMeasurements.getSize()];
        double[] yArray = new double[elapsedTimeMeasurements.getSize()];
        int i = 0;
        for (i = 0; i < resultFileSizes.getSize(); i++) {
            double resultFileSize = ((INumericValue) resultFileSizes.getList().get(i).getValue()).value();
            double factor = ((INumericValue) relativeFileSizes.getList().get(i).getValue()).value();
            xArray[i] = (resultFileSize / factor) * 100.0;
            yArray[i] = ((INumericValue) elapsedTimeMeasurements.getList().get(i).getValue()).value();
        }
        Regression reg = new Regression(xArray, yArray);
        reg.linear();
        double[] bestEstimates = reg.getBestEstimates();
        double startUpTime = bestEstimates[0];
        return startUpTime;
    }

    public Map<String, Measurements> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Map<String, Measurements> measurements) {
        this.measurements = measurements;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
