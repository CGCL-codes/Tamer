package com.vi.data;

/**
 * Needed for the failuresList on allTestsOverview, so Struts2 can iterate through it
 * 
 * @author Johannes Sauer
 *
 */
public class FailureInformation {

    private String fid;

    private String pieceLocation;

    private String componentLocation;

    private String failureCode;

    private String failureDefinition;

    private String failureDescription;

    private String time;

    private String operatorID;

    private String confirmation;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getPieceLocation() {
        return pieceLocation;
    }

    public void setPieceLocation(String pieceLocation) {
        this.pieceLocation = pieceLocation;
    }

    public String getComponentLocation() {
        return componentLocation;
    }

    public void setComponentLocation(String componentLocation) {
        this.componentLocation = componentLocation;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureDefinition() {
        return failureDefinition;
    }

    public void setFailureDefinition(String failureDefinition) {
        this.failureDefinition = failureDefinition;
    }

    public String getFailureDescription() {
        return failureDescription;
    }

    public void setFailureDescription(String failureDescription) {
        this.failureDescription = failureDescription;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
