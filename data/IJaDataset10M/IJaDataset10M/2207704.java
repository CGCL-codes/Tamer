package org.poli.brazilianipt.bd;

import com.google.appengine.api.datastore.Key;
import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class DarwinCoreBasic {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key occurrenceID;

    @Persistent
    private String institutionCode;

    @Persistent
    private String collectionCode;

    @Persistent
    private String recordedBy;

    @Persistent
    private Date eventDate;

    @Persistent
    private Date year;

    @Persistent
    private String typeStatus;

    @Persistent
    private String kingdom;

    @Persistent
    private String family;

    @Persistent
    private String locality;

    @Persistent
    private Double decimalLongitude;

    @Persistent
    private Double decimalLatitude;

    @Persistent
    private Double coordinateUncertaintyInMeters;

    public void setOccurrenceID(Key occurrenceID) {
        this.occurrenceID = occurrenceID;
    }

    public Key getOccurrenceID() {
        return occurrenceID;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setCollectionCode(String collectionCode) {
        this.collectionCode = collectionCode;
    }

    public String getCollectionCode() {
        return collectionCode;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public Date getYear() {
        return year;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFamily() {
        return family;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLocality() {
        return locality;
    }

    public void setDecimalLongitude(Double decimalLongitude) {
        this.decimalLongitude = decimalLongitude;
    }

    public Double getDecimalLongitude() {
        return decimalLongitude;
    }

    public void setDecimalLatitude(Double decimalLatitude) {
        this.decimalLatitude = decimalLatitude;
    }

    public Double getDecimalLatitude() {
        return decimalLatitude;
    }

    public void setCoordinateUncertaintyInMeters(Double coordinateUncertaintyInMeters) {
        this.coordinateUncertaintyInMeters = coordinateUncertaintyInMeters;
    }

    public Double getCoordinateUncertaintyInMeters() {
        return coordinateUncertaintyInMeters;
    }
}
