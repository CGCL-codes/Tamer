package name.jelen.reqtop.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "T61_View_list")
public class RequirementListView extends View {

    /***************************************************************************
	 * Properties
	 **************************************************************************/
    private static final long serialVersionUID = -5202783969254146186L;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "T62_List_view_attrs", joinColumns = { @JoinColumn(name = "T62T61VIEW_ID") }, inverseJoinColumns = { @JoinColumn(name = "T62T03ATTRTYPE_ID") })
    @IndexColumn(name = "T62POSITION")
    private List<AttributeType> attributeTypes;

    @Column(name = "T61QUERY")
    private String query;

    @JoinColumn(name = "T61T02REQTYPE")
    @ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private RequirementType requirementType;

    /***************************************************************************
	 * Constructors
	 **************************************************************************/
    public RequirementListView() {
        attributeTypes = new ArrayList<AttributeType>();
    }

    /***************************************************************************
	 * "Interesting" methods
	 **************************************************************************/
    @Override
    protected Versionable copy(Versionable original) {
        RequirementListView result = new RequirementListView();
        result.kind = ((RequirementListView) original).kind;
        result.query = ((RequirementListView) original).query;
        return result;
    }

    /***************************************************************************
	 * Generated getters/setters
	 **************************************************************************/
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        attributeChanged(this.query, query, "Query");
        this.query = query;
    }

    public List<AttributeType> getAttributeTypes() {
        return attributeTypes;
    }

    public void setAttributeTypes(List<AttributeType> attributeTypes) {
        this.attributeTypes = attributeTypes;
    }

    public RequirementType getRequirementType() {
        return requirementType;
    }

    public void setRequirementType(RequirementType requirementType) {
        this.requirementType = requirementType;
    }

    public void initFromTemplate(RequirementListView template, Map<Versionable, Versionable> copyMap, User author) {
        copyMap.put(template, this);
        this.setAuthor(author);
        this.setName(template.getName());
        this.setDescription(template.getDescription());
        this.setFolder((Folder) copyMap.get(template.getFolder()));
        this.setKind(template.getKind());
        this.setProject((Project) copyMap.get(template.getProject()));
        this.setQuery(template.getQuery());
        this.setRequirementType((RequirementType) copyMap.get(template.getRequirementType()));
        List<AttributeType> attributeTypes = new ArrayList<AttributeType>();
        for (AttributeType at : template.getAttributeTypes()) {
            attributeTypes.add((AttributeType) copyMap.get(at));
        }
        this.setAttributeTypes(attributeTypes);
    }

    @Override
    public void claimRecursively(User newUser) {
        this.setAuthor(newUser);
    }
}
