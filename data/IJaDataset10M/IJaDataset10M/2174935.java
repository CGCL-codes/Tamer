package org.pojosoft.lms.content.scorm2004;

import java.io.Serializable;

/**
 * SCORM activity objective's RTE state. 
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public class ActivityObjectiveRteState implements Serializable {

    private String objectiveId;

    private Double scaledScore;

    private String successStatus;

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public Double getScaledScore() {
        return scaledScore;
    }

    public void setScaledScore(Double scaledScore) {
        this.scaledScore = scaledScore;
    }

    public String getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(String successStatus) {
        this.successStatus = successStatus;
    }
}
