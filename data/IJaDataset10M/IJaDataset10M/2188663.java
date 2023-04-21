package com.crimerank.data;

public class Incident {

    String IncidentID;

    String IncidentType;

    String DateUpdated;

    String DelaySeverity;

    String Description;

    String EmergencyText;

    String StartLocationFullName;

    String EndLocationFullName;

    String LinesAffected;

    String PassengerDelay;

    public String getIncidentID() {
        return IncidentID;
    }

    public void setIncidentID(String incidentID) {
        IncidentID = incidentID;
    }

    public String getIncidentType() {
        return IncidentType;
    }

    public void setIncidentType(String incidentType) {
        IncidentType = incidentType;
    }

    public String getDateUpdated() {
        return DateUpdated;
    }

    public void setDateUpdated(String dateUpdated) {
        DateUpdated = dateUpdated;
    }

    public String getDelaySeverity() {
        return DelaySeverity;
    }

    public void setDelaySeverity(String delaySeverity) {
        DelaySeverity = delaySeverity;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEmergencyText() {
        return EmergencyText;
    }

    public void setEmergencyText(String emergencyText) {
        EmergencyText = emergencyText;
    }

    public String getStartLocationFullName() {
        return StartLocationFullName;
    }

    public void setStartLocationFullName(String startLocationFullName) {
        StartLocationFullName = startLocationFullName;
    }

    public String getEndLocationFullName() {
        return EndLocationFullName;
    }

    public void setEndLocationFullName(String endLocationFullName) {
        EndLocationFullName = endLocationFullName;
    }

    public String getLinesAffected() {
        return LinesAffected;
    }

    public void setLinesAffected(String linesAffected) {
        LinesAffected = linesAffected;
    }

    public String getPassengerDelay() {
        return PassengerDelay;
    }

    public void setPassengerDelay(String passengerDelay) {
        PassengerDelay = passengerDelay;
    }
}
