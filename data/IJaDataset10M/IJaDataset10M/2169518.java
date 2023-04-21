package ordas.model.orgs;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 * When you update data in LegalEntityInfo you create a copy of the old data,
 * store it as a LegalEntityInfoHistory and then modify the data.
 * Be careful when adding new member, you must update LegalEntityInfoHistory constructor
 */
@Entity
@Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "FLAGS", length = 1, discriminatorType = javax.persistence.DiscriminatorType.STRING)
public class LegalEntityInfo implements Serializable, Cloneable {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar data_updated_date;

    private String legal_full_name;

    private String legal_id_code;

    private String legal_industrial_code;

    private String legal_economical_code;

    @ManyToOne(fetch = FetchType.LAZY)
    private AddressData legal_address;

    private String manager_name_legal;

    private String manager_email_legal;

    private String comments;

    @OneToMany(mappedBy = "owning")
    private List<LegalEntityInfoHistory> history;

    @OneToMany(mappedBy = "legal_entity_info")
    private List<OrgGeneric> orgs_for_this_legal;

    @Transient
    private LegalEntityInfo backup_copy;

    public LegalEntityInfo() {
        data_updated_date = Calendar.getInstance();
    }

    /**
 * Used for History storage, data_updated_date is not accesible
 * @param data_updated_date
 */
    public LegalEntityInfo(Calendar data_updated_date) {
        this.data_updated_date = data_updated_date;
    }

    @PostLoad
    private void makeBackupCopy() {
        new LegalEntityInfoHistory(backup_copy);
    }

    @PreUpdate
    private void saveHistoricalData() {
        LegalEntityInfoHistory leih = new LegalEntityInfoHistory(backup_copy);
        this.getHistory().add(leih);
    }

    public Long getId() {
        return id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Calendar getData_updated_date() {
        return data_updated_date;
    }

    public AddressData getLegal_address() {
        return legal_address;
    }

    public void setLegal_address(AddressData legal_address) {
        this.legal_address = legal_address;
    }

    public String getLegal_economical_code() {
        return legal_economical_code;
    }

    public void setLegal_economical_code(String legal_economical_code) {
        this.legal_economical_code = legal_economical_code;
    }

    public String getLegal_full_name() {
        return legal_full_name;
    }

    public void setLegal_full_name(String legal_full_name) {
        this.legal_full_name = legal_full_name;
    }

    public String getLegal_id_code() {
        return legal_id_code;
    }

    public void setLegal_id_code(String legal_id_code) {
        this.legal_id_code = legal_id_code;
    }

    public String getLegal_industrial_code() {
        return legal_industrial_code;
    }

    public void setLegal_industrial_code(String legal_industrial_code) {
        this.legal_industrial_code = legal_industrial_code;
    }

    public String getManager_email_legal() {
        return manager_email_legal;
    }

    public void setManager_email_legal(String manager_email_legal) {
        this.manager_email_legal = manager_email_legal;
    }

    public String getManager_name_legal() {
        return manager_name_legal;
    }

    public void setManager_name_legal(String manager_name_legal) {
        this.manager_name_legal = manager_name_legal;
    }

    public List<LegalEntityInfoHistory> getHistory() {
        return history;
    }

    /**
     * @return the orgs_for_this_legal
     */
    public List<OrgGeneric> getOrgs_for_this_legal() {
        return orgs_for_this_legal;
    }
}
