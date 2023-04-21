package org.ras.bin.checklister.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jz
 */
@Entity
@Table(name = "cl_check_list_list")
public class CheckList implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String ENTITY_SIGNATURE = "CL";

    @Id
    @Column(name = "cl_check_list_id")
    private String checkListId;

    @Column(name = "cl_check_list_title")
    private String checkListTitle;

    @Column(name = "cl_check_list_note")
    private String checkListNote;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checkList")
    private Collection<Family> familyCollection;

    public String getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(String checkListId) {
        this.checkListId = checkListId;
    }

    public String getCheckListNote() {
        return checkListNote;
    }

    public void setCheckListNote(String checkListNote) {
        this.checkListNote = checkListNote;
    }

    public String getCheckListTitle() {
        return checkListTitle;
    }

    public void setCheckListTitle(String checkListTitle) {
        this.checkListTitle = checkListTitle;
    }

    public Collection<Family> getFamilyCollection() {
        return familyCollection;
    }

    public void setFamilyCollection(Collection<Family> familyCollection) {
        this.familyCollection = familyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (checkListId != null ? checkListId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CheckList)) {
            return false;
        }
        CheckList other = (CheckList) object;
        if ((this.checkListId == null && other.checkListId != null) || (this.checkListId != null && !this.checkListId.equals(other.checkListId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.ras.bin.checklister.domain.CheckList[id=" + checkListId + "] " + checkListTitle;
    }

    public String getCheckListPresentation() {
        return "<html>" + "<b>" + checkListTitle;
    }
}
