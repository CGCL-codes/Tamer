package ispyb.client.results;

import ispyb.server.data.interfaces.ScreeningStrategyValue;
import ispyb.server.util.PathUtils;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ScreeningStrategyValueInfo extends ScreeningStrategyValue {

    private static final long serialVersionUID = 0;

    private Integer totNbImages = null;

    private Double totExposureTime = null;

    private boolean programLogFileExists = true;

    private String programLog = null;

    public ScreeningStrategyValueInfo(ScreeningStrategyValue screeningStrategyValue) {
        super(screeningStrategyValue);
        try {
            NumberFormat nf1 = NumberFormat.getIntegerInstance();
            DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            df1.applyPattern("#####0.0");
            DecimalFormat df2 = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            df2.applyPattern("#####0.00");
            Double nbImagesdb = null;
            Double rotation = this.getRotation();
            Double phiS = this.getPhiStart();
            Double phiE = this.getPhiEnd();
            Double exposureT = this.getExposureTime();
            if (phiE != null && phiS != null && rotation != null && rotation.compareTo(new Double(0)) != 0) {
                nbImagesdb = (phiE - phiS) / rotation;
                this.totNbImages = new Integer(nf1.format(nbImagesdb));
            }
            if (exposureT != null) this.totExposureTime = new Double(df1.format(nbImagesdb * exposureT));
            Double phiSformat = null;
            Double phiEformat = null;
            Double rotationf = null;
            Double exposureTf = null;
            Double resolutionf = null;
            Double completenessf = null;
            Double multiplicityf = null;
            if (phiS != null) phiSformat = new Double(df1.format(phiS));
            if (phiE != null) phiEformat = new Double(df1.format(phiE));
            if (rotation != null) rotationf = new Double(df2.format(rotation));
            if (exposureT != null) exposureTf = new Double(df1.format(exposureT));
            if (this.getResolution() != null) resolutionf = new Double(df2.format(this.getResolution()));
            if (this.getCompleteness() != null) completenessf = new Double(df1.format(this.getCompleteness().doubleValue() * 100));
            if (this.getMultiplicity() != null) multiplicityf = new Double(df1.format(this.getMultiplicity()));
            this.setPhiStart(phiSformat);
            this.setPhiEnd(phiEformat);
            this.setRotation(rotationf);
            this.setExposureTime(exposureTf);
            this.setResolution(resolutionf);
            this.setCompleteness(completenessf);
            this.setMultiplicity(multiplicityf);
        } catch (Exception e) {
        }
    }

    public boolean isProgramLogFileExists() {
        return programLogFileExists;
    }

    public void setProgramLogFileExists(boolean programLogFileExists) {
        this.programLogFileExists = programLogFileExists;
    }

    public Integer getTotNbImages() {
        return totNbImages;
    }

    public void setTotNbImages(Integer totNbImages) {
        this.totNbImages = totNbImages;
    }

    public Double getTotExposureTime() {
        return totExposureTime;
    }

    public void setTotExposureTime(Double totExposureTime) {
        this.totExposureTime = totExposureTime;
    }

    public String getProgramLog() {
        return programLog;
    }

    public void setProgramLog(String programLog) {
        this.programLog = programLog;
    }

    /**
	 * Set the program log file path.
	 * @param the datacollectionId
	 */
    public void setProgramLog(Integer dataCollectionId) {
        try {
            String programLogPath = PathUtils.GetFullLogPath(dataCollectionId);
            String programLogFilePath = programLogPath + "best.log";
            boolean logFilePresent = (new File(programLogFilePath)).exists();
            this.setProgramLogFileExists(logFilePresent);
            if (logFilePresent) this.programLog = programLogFilePath;
        } catch (Exception e) {
        }
    }
}
