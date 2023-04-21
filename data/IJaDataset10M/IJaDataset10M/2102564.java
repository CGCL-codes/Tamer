package com.spotimage.eosps;

import org.vast.cdm.common.DataComponent;
import org.vast.data.DataValue;
import org.vast.ows.sps.TaskingRequest;

/**
 * <p><b>Title:</b><br/>
 * EO Optical Param Helper
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This class is a helper to get and set tasking parameters
 * that are specific to optical earth observation instruments.
 * It must be constructed from an existing GetFeasibility
 * or Submit request (for instance as obtained using the
 * generic SPS deserializers).
 * </p>
 *
 * <p>Copyright (c) 2008, Spot Image</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date July, 10th 2008
 * @since 1.0
 */
public class EOOpticalParamHelper extends EOParamHelper implements EOOpticalConstants {

    public EOOpticalParamHelper(TaskingRequest request) {
        super(request);
    }

    public EOOpticalParamHelper(TaskingRequest request, DataComponent taskingParams) {
        super(request, taskingParams);
    }

    /**
	 * Finds min luminosity field in request parameters structure
	 * @return
	 */
    public DataValue findMinLuminosity() {
        DataComponent param = findParameterByDefinition(MIN_LUMINOSITY_URI);
        return (DataValue) param;
    }

    public double getMinLuminosity() {
        DataValue param = findMinLuminosity();
        if (param == null) return 0.0;
        return param.getData().getDoubleValue();
    }

    public void setMinLuminosity(double lum) {
        DataValue param = findMinLuminosity();
        if (param != null) param.getData().setDoubleValue(lum);
    }

    /**
	 * Finds max cloud cover field in request parameters structure
	 * @return
	 */
    public DataValue findMaxCloudCover() {
        DataComponent param = findParameterByDefinition(MAX_CLOUD_COVER_URI);
        return (DataValue) param;
    }

    public double getMaxCloudCover() {
        DataComponent param = findMaxCloudCover();
        if (param == null) return 100.0;
        return param.getData().getDoubleValue();
    }

    public void setMaxCloudCover(double maxCloud) {
        DataComponent param = findMaxCloudCover();
        if (param != null) param.getData().setDoubleValue(maxCloud);
    }

    /**
	 * Finds max snow cover field in request parameters structure
	 * @return
	 */
    public DataValue findMaxSnowCover() {
        DataComponent param = findParameterByDefinition(MAX_SNOW_COVER_URI);
        return (DataValue) param;
    }

    public double getMaxSnowCover() {
        DataComponent param = findMaxSnowCover();
        if (param == null) return 100.0;
        return param.getData().getDoubleValue();
    }

    public void setMaxSnowCover(double maxSnow) {
        DataComponent param = findMaxSnowCover();
        if (param != null) param.getData().setDoubleValue(maxSnow);
    }

    /**
	 * Finds haze accepted field in request parameters structure
	 * @return
	 */
    public DataValue findHazeAccepted() {
        DataComponent param = findParameterByDefinition(HAZE_ACCEPTED_URI);
        return (DataValue) param;
    }

    public boolean isHazeAccepted() {
        DataComponent param = findHazeAccepted();
        if (param != null) return param.getData().getBooleanValue(); else return true;
    }

    public void setHazeAccepted(boolean hazeOk) {
        DataComponent param = findHazeAccepted();
        if (param != null) param.getData().setBooleanValue(hazeOk);
    }

    /**
	 * Finds sand wind accepted field in request parameters structure
	 * @return
	 */
    public DataValue findSandWindAccepted() {
        DataComponent param = findParameterByDefinition(SAND_ACCEPTED_URI);
        return (DataValue) param;
    }

    public boolean isSandWindAccepted() {
        DataComponent param = findSandWindAccepted();
        if (param != null) return param.getData().getBooleanValue(); else return true;
    }

    public void setSandWindAccepted(boolean sandOk) {
        DataComponent param = findSandWindAccepted();
        if (param != null) param.getData().setBooleanValue(sandOk);
    }
}
