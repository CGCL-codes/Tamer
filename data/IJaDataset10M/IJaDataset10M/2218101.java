package br.ufmg.lcc.pcollecta.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import br.ufmg.lcc.arangi.commons.StringHelper;

/**
 *Represents one import transfer that can be scheduled to be 
 *executed for one or more entities. One collecting uses the 
 *Import Process structure but it should be used with Import 
 *Process which the ETLs uses pCollecta WebService repository 
 *type as data source. In other case, doesn't make sense use a 
 *Collecting. Each Collecting Item has one entity which has 
 *one webserviceEndPoint attribute that will be replaced in
 *ETL repository data source during the import process.
 */
@Entity
@Table(name = "PC_HARVEST")
public class Harvest extends PCollectaDTO {

    private static final long serialVersionUID = 1L;

    /**
	 *Description of the Collecting
	 */
    private String description;

    /**
	 *Date and time of request
	 */
    private Date requestDate;

    /**
	 *Database which will be target of SQL sentences
	 */
    private String database;

    private ImportProcess importProcess;

    private Boolean preApproved = Boolean.FALSE;

    private String preApprovedAux = convertBooleanToString(Boolean.FALSE);

    private Date termStartDate;

    private Date termFinishDate;

    private List<SourceOrganization> sourceOrganizations;

    private String requesterName;

    public Harvest() {
    }

    public Harvest(Long id, String name, String description, String database, Date requestDate) {
        setId(id);
        setName(name);
        setDescription(description);
        setRequestDate(requestDate);
        setDatabase(database);
    }

    public Harvest(Long id, String name, String description, Date requestDate, String database, Schedule schedule) {
        setId(id);
        setName(name);
        setDescription(description);
        setRequestDate(requestDate);
        setDatabase(database);
    }

    public Harvest(Long id, String name, Long importProcessId, String importProcessName) {
        setId(id);
        setName(name);
        setImportProcess(new ImportProcess());
        getImportProcess().setId(importProcessId);
        getImportProcess().setName(importProcessName);
    }

    /** Setters and getters */
    @Id
    @Column(name = "ID_HARVEST")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "SE_HARVEST")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQUENCE")
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "REQUEST_DATE")
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Column(name = "DATABASE_NAME")
    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_IMPORT_PROCESS")
    public ImportProcess getImportProcess() {
        if (importProcess == null) importProcess = new ImportProcess();
        return importProcess;
    }

    public void setImportProcess(ImportProcess importProcess) {
        this.importProcess = importProcess;
    }

    @OneToMany(mappedBy = "master", cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    public List<SourceOrganization> getSourceOrganizations() {
        if (sourceOrganizations == null) sourceOrganizations = new ArrayList<SourceOrganization>();
        return sourceOrganizations;
    }

    public void setSourceOrganizations(List<SourceOrganization> sourceOrganizations) {
        this.sourceOrganizations = sourceOrganizations;
    }

    @Override
    @Column(name = "NAME")
    public String getName() {
        return super.getName();
    }

    @Transient
    public Boolean getPreApproved() {
        return preApproved;
    }

    public void setPreApproved(Boolean preApproved) {
        this.preApproved = preApproved;
        this.preApprovedAux = this.convertBooleanToString(preApproved);
    }

    @Column(name = "TERM_START_DATE")
    public Date getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(Date termStartDate) {
        this.termStartDate = termStartDate;
    }

    @Column(name = "TERM_FINISH_DATE")
    public Date getTermFinishDate() {
        return termFinishDate;
    }

    public void setTermFinishDate(Date termFinishDate) {
        this.termFinishDate = termFinishDate;
    }

    @Column(name = "PRE_APPROVED")
    public String getPreApprovedAux() {
        return preApprovedAux;
    }

    public void setPreApprovedAux(String preApprovedAux) {
        if (preApprovedAux == null) preApprovedAux = "N";
        this.preApprovedAux = preApprovedAux;
        this.preApproved = this.convertStringToBoolean(preApprovedAux);
    }

    @Transient
    public String getImportEtls() {
        StringBuffer etls = new StringBuffer();
        Collections.sort(getImportProcess().getProcessItems());
        for (ProcessItem item : getImportProcess().getProcessItems()) {
            etls.append(StringHelper.SPACE + item.getSortOrder() + " . " + item.getEtl().getName() + StringHelper.BREAK_LINE);
        }
        return etls.toString();
    }

    @Column(name = "REQUESTER_NAME")
    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }
}
