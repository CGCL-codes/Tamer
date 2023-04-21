package dbexel.model.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "WORKSHEET")
public class WorkSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "WS_ID")
    private Long ws_Id = new Long(0);

    @Column(name = "WS_NAME")
    private String ws_Name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "WORKSHEET_ATTRIBUTES")
    private List<WorkSheet_Attribute> attributes;

    @OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinTable(name = "WORKSHEET_ENTRIES")
    @JoinColumn(name = "entry_id")
    private List<WorkSheet_Entry> entries;

    public WorkSheet() {
        attributes = new ArrayList<WorkSheet_Attribute>();
        entries = new ArrayList<WorkSheet_Entry>();
    }

    public WorkSheet(final String in_wsName) {
        ws_Name = in_wsName;
        attributes = new ArrayList<WorkSheet_Attribute>();
        entries = new ArrayList<WorkSheet_Entry>();
    }

    public Long getWs_Id() {
        return ws_Id;
    }

    public void setWs_Id(Long in_wsId) {
        ws_Id = in_wsId;
    }

    public String getWs_Name() {
        return ws_Name;
    }

    public void setWs_Name(String in_wsName) {
        ws_Name = in_wsName;
    }

    /**
	 * @param attributes
	 *            the attributes to set
	 */
    public void setAttributes(ArrayList<WorkSheet_Attribute> in_attributes) {
        this.attributes = in_attributes;
    }

    /**
	 * @return the attributes
	 */
    public List<WorkSheet_Attribute> getAttributes() {
        return attributes;
    }

    /**
	 * This method add an attribute to the Worksheet
	 * 
	 * @param in_myAttribute
	 */
    public void attachAttribute(WorkSheet_Attribute in_myAttribute) {
        getAttributes().add(in_myAttribute);
    }

    /**
	 * @param entries
	 *            the entries to set
	 */
    public void setEntries(List<WorkSheet_Entry> entries) {
        this.entries = entries;
    }

    /**
	 * @return the entries
	 */
    public List<WorkSheet_Entry> getEntries() {
        return entries;
    }

    /**
	 * This method adds an entry to the current worksheet
	 * 
	 * @param in_WorkSheetEntry
	 */
    public void addEntry(WorkSheet_Entry in_WorkSheetEntry) {
        getEntries().add(in_WorkSheetEntry);
        in_WorkSheetEntry.setWorkSheet(this);
    }

    /**
	 * This method given an attribute and an index, moves the attribute to that
	 * index
	 * 
	 * @param in_wsAttribute
	 * @param to_index
	 */
    public void moveWorkSheetAttribute(WorkSheet_Attribute in_wsAttribute, int to_index) {
        int from_index = findWorkSheetAttribute(in_wsAttribute);
        if ((to_index > getAttributes().size()) || (to_index < 0)) {
            throw new IndexOutOfBoundsException("The given index " + to_index + " is not within the domain of the attached attributes.");
        }
        if (from_index < 0) {
            throw new IndexOutOfBoundsException("The given attribute " + in_wsAttribute.getAttrName() + " is not attached to the worksheet yet.");
        }
        if (from_index > to_index) {
            for (int i = from_index; i > to_index; i--) {
                getAttributes().set(i, getAttributes().get(i - 1));
            }
            getAttributes().set(to_index, in_wsAttribute);
        }
        if (from_index < to_index) {
            for (int i = from_index; i < to_index; i++) {
                getAttributes().set(i, getAttributes().get(i + 1));
            }
            getAttributes().set(to_index, in_wsAttribute);
        }
    }

    /**
	 * This method will find the index of the given attribute
	 * 
	 * @param in_wsAttribute
	 * @return the index of the attribute
	 */
    private int findWorkSheetAttribute(WorkSheet_Attribute in_wsAttribute) {
        for (int i = 0; i < getAttributes().size(); i++) {
            if (getAttributes().get(i) == in_wsAttribute) {
                return i;
            }
        }
        return -1;
    }

    /**
	 * This method searches the list of attached attributes for a given AttributeId
	 * @param in_WorkSheetAttributeId
	 * @return the corresponding WorkSheet_Attribute. Otherwise null
	 */
    public WorkSheet_Attribute findAttributeById(Long in_WorkSheetAttributeId) {
        for (WorkSheet_Attribute lt_WorkSheetAttribute : getAttributes()) {
            if (lt_WorkSheetAttribute.getAttrId().equals(in_WorkSheetAttributeId)) return lt_WorkSheetAttribute;
        }
        return null;
    }
}
