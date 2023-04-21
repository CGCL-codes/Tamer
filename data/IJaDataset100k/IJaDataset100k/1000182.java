package eu.planets_project.tb.api.model;

public interface ExperimentResources {

    public static final int INTENSITY_LOW = 0;

    public static final int INTENSITY_MEDIUM = 1;

    public static final int INTENSITY_HIGH = 2;

    public void setNumberOfOutputFiles(int iNr);

    public int getNumberOfOutputFiles();

    public void setIntensity(int iIntensity);

    public int getIntensity();
}
