package domain.parameter_objects;

import java.io.Serializable;
import domain.parameter_objects.model_parameter_objects.CanopyParameters;
import domain.parameter_objects.model_parameter_objects.EnvironmentalParameters;
import domain.parameter_objects.model_parameter_objects.LocationParameters;
import domain.parameter_objects.model_parameter_objects.SchedulingParameters;

/**
 *
 * @author Uwe Grueters,	email: uwe.grueters@bot2.bio.uni-giessen.de
 * @author Markus W�tzel,   email: markus@woetzel.net
 */
public class ModelParameters implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static ModelParameters instance;

    private LocationParameters locationParams;

    private SchedulingParameters schedulingParams;

    private EnvironmentalParameters enviornmentParams;

    private CanopyParameters canopyParams;

    private boolean useMeasuredData = false;

    private String ExcelFilePath = "c:/Linden.xls";

    private int ExcelSheetNumber = 1;

    /**
	 * @return the useMeasuredData
	 */
    public boolean isUseMeasuredData() {
        return useMeasuredData;
    }

    /**
	 * @param useMeasuredData the useMeasuredData to set
	 */
    public void setUseMeasuredData(boolean useMeasuredData) {
        this.useMeasuredData = useMeasuredData;
    }

    private ModelParameters() {
        this.locationParams = LocationParameters.getInstance();
        this.schedulingParams = SchedulingParameters.getInstance();
        this.enviornmentParams = EnvironmentalParameters.getInstance();
        this.canopyParams = CanopyParameters.getInstance();
    }

    public static ModelParameters getInstance() {
        if (instance == null) {
            instance = new ModelParameters();
        }
        return instance;
    }

    /**
	 * @return the enviornmentParams
	 */
    public EnvironmentalParameters getEnviornmentParams() {
        return enviornmentParams;
    }

    /**
	 * @return the locationParams
	 */
    public LocationParameters getLocationParams() {
        return locationParams;
    }

    /**
	 * @return the schedulingParams
	 */
    public SchedulingParameters getSchedulingParams() {
        return schedulingParams;
    }

    /**
	 * @return the canopyParams
	 */
    public CanopyParameters getCanopyParams() {
        return canopyParams;
    }

    /**
	 * @return the excelFilePath
	 */
    public String getExcelFilePath() {
        return ExcelFilePath;
    }

    /**
	 * @param excelFilePath the excelFilePath to set
	 */
    public void setExcelFilePath(String excelFilePath) {
        ExcelFilePath = excelFilePath;
    }

    /**
	 * @return the excelSheetNumber
	 */
    public int getExcelSheetNumber() {
        return ExcelSheetNumber;
    }

    /**
	 * @param excelSheetNumber the excelSheetNumber to set
	 */
    public void setExcelSheetNumber(int excelSheetNumber) {
        ExcelSheetNumber = excelSheetNumber;
    }
}
