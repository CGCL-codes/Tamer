package edu.unibi.agbi.biodwh.entity.epd;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity(name = "epd_dblinks")
@Table(appliesTo = "epd_dblinks", indexes = { @Index(name = "dblinksIdx", columnNames = { "dbname", "accession_number" }) })
public class EpdDbLinks implements java.io.Serializable {

    private static final long serialVersionUID = -681937661782686698L;

    private int id;

    private EpdIdentification epdIdentification;

    private String dbName;

    private String accessionNumber;

    private String secondaryIdentifier;

    private String positionDirectionType;

    public EpdDbLinks() {
    }

    public EpdDbLinks(int id, EpdIdentification epdIdentification, String dbName, String accessionNumber, String secondaryIdentifier, String positionDirectionType) {
        this.id = id;
        this.epdIdentification = epdIdentification;
        this.dbName = dbName;
        this.accessionNumber = accessionNumber;
        this.secondaryIdentifier = secondaryIdentifier;
        this.positionDirectionType = positionDirectionType;
    }

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_name", nullable = false)
    public EpdIdentification getEpdIdentification() {
        return this.epdIdentification;
    }

    public void setEpdIdentification(EpdIdentification epdIdentification) {
        this.epdIdentification = epdIdentification;
    }

    @Column(name = "dbname", nullable = false)
    public String getDbName() {
        return this.dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Column(name = "accession_number", nullable = false)
    public String getAccessionNumber() {
        return this.accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    @Column(name = "secondary_identifier", nullable = false)
    public String getSecondaryIdentifier() {
        return this.secondaryIdentifier;
    }

    public void setSecondaryIdentifier(String secondaryIdentifier) {
        this.secondaryIdentifier = secondaryIdentifier;
    }

    @Column(name = "position_direction_type", nullable = false)
    public String getPositionDirectionType() {
        return this.positionDirectionType;
    }

    public void setPositionDirectionType(String positionDirectionType) {
        this.positionDirectionType = positionDirectionType;
    }
}
