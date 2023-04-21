package com.deimos_space.sps_cfi.jni;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import com.deimos.dataBase.ScenarioResponse;

/**
 * Contains a set of values as a result of a Feasibility Study
 * 
 * @author Manuel Casado Martin
 */
public class FeasibilityStudy {

    private String DATE_FORMAT_CFI = "yyyy-MM-dd'_'HH:mm:ss.SSSSSS";

    private SimpleDateFormat sdf_cfi = new SimpleDateFormat(DATE_FORMAT_CFI);

    private Vector<double[][]> swaths = new Vector<double[][]>();

    private Vector<String[]> segments = new Vector<String[]>();

    private String instrument;

    private String platform;

    private String platformId;

    private Feasibility result;

    private String lastUpdateTime;

    private String description;

    private double weatherChance;

    private ScenarioResponse acquisitionSuccess;

    public ScenarioResponse getAcquisitionSuccess() {
        return acquisitionSuccess;
    }

    public void setAcquisitionSuccess(ScenarioResponse acquisitionSuccess) {
        this.acquisitionSuccess = acquisitionSuccess;
    }

    /**
     * @return the swaths
     */
    public Vector<double[][]> getSwaths() {
        return swaths;
    }

    /**
     * @param swaths
     *            the swaths to set
     */
    public void addSwath(double[][] swathPoints) {
        this.swaths.add(swathPoints);
    }

    public void clearSwaths() {
        this.swaths.clear();
    }

    /**
     * @param result
     *            the result of the Feasibility study
     */
    public void setResult(Feasibility result) {
        this.result = result;
    }

    /**
     * @return the result of the Feasibility study
     */
    public Feasibility getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the instrument
     */
    public String getInstrument() {
        return instrument;
    }

    /**
     * @param instrument
     *            the instrument to set
     */
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    /**
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * @param platform
     *            the platform to set
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *            the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the number of swaths
     */
    public int getNbSwaths() {
        return this.swaths.size();
    }

    /**
     * @return the segments
     */
    public Vector<String[]> getSegments() {
        return segments;
    }

    /**
     * @param segments
     *            the segments to set
     */
    public void addSegment(String[] segment) {
        this.segments.add(segment);
    }

    public void clearSegments() {
        this.segments.clear();
    }

    public boolean equals(Object object) {
        boolean result = true;
        FeasibilityStudy study = null;
        if (object instanceof FeasibilityStudy) {
            study = (FeasibilityStudy) object;
            try {
                result &= study.getInstrument().equals(this.instrument);
                result &= study.getLastUpdateTime().equals(this.lastUpdateTime);
                result &= study.getNbSwaths() == this.swaths.size();
                result &= study.getPlatform().equals(this.platform);
                result &= study.getResult() == this.result;
                for (int i = 0; i < study.getSegments().size(); i++) {
                    String[] segment = study.getSegments().get(i);
                    String[] thisSegment = this.segments.get(i);
                    result &= segment[0].equals(thisSegment[0]);
                    result &= segment[1].equals(thisSegment[1]);
                    result &= segment[2].equals(thisSegment[2]);
                }
                for (int i = 0; i < study.getSwaths().size(); i++) {
                    double[][] swath = study.getSwaths().get(i);
                    double[][] thisSwath = this.swaths.get(i);
                    result &= swath[0][0] == thisSwath[0][0];
                    result &= swath[0][1] == thisSwath[0][1];
                    result &= swath[1][0] == thisSwath[1][0];
                    result &= swath[1][1] == thisSwath[1][1];
                    result &= swath[2][0] == thisSwath[2][0];
                    result &= swath[2][1] == thisSwath[2][1];
                    result &= swath[3][0] == thisSwath[3][0];
                    result &= swath[3][1] == thisSwath[3][1];
                }
            } catch (Exception e) {
                return false;
            }
            return result;
        }
        return false;
    }

    /**
     * @return the weatherChance
     */
    public double getWeatherChance() {
        return weatherChance;
    }

    /**
     * @param weatherChance
     *            the weatherChance to set
     */
    public void setWeatherChance(double weatherChance) {
        this.weatherChance = weatherChance;
    }

    /**
     * @return the platformId
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * @param platformId
     *            the platformId to set
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public void sort() {
        try {
            sortSegments(this.segments, this.swaths, 0, segments.size() - 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void sortSegments(Vector<String[]> segments, Vector<double[][]> swaths, int primero, int ultimo) throws ParseException {
        int i = primero, j = ultimo;
        Date pivote = sdf_cfi.parse(segments.get((primero + ultimo) / 2)[0]);
        String[] auxiliar;
        double[][] auxSwaths;
        do {
            while (sdf_cfi.parse(segments.get(i)[0]).getTime() < pivote.getTime()) i++;
            while (sdf_cfi.parse(segments.get(j)[0]).getTime() > pivote.getTime()) j--;
            if (i <= j) {
                auxiliar = segments.get(j);
                this.getSegments().set(j, segments.get(i));
                this.getSegments().set(i, auxiliar);
                auxSwaths = swaths.get(j);
                swaths.set(j, swaths.get(i));
                swaths.set(i, auxSwaths);
                i++;
                j--;
            }
        } while (i <= j);
        if (primero < j) sortSegments(segments, swaths, primero, j);
        if (ultimo > i) sortSegments(segments, swaths, i, ultimo);
    }
}
