package musite.taxonomy;

import java.util.ArrayList;

/**
 *
 * @author LucasYao
 */
public class TaxonomyNode implements NetworkNode<TaxonomyNode> {

    private String Identifier;

    private String Type;

    private String Rank;

    private String ScientificName;

    private ArrayList<String> OtherNames;

    private boolean partOfLineage;

    private ArrayList<TaxonomyNode> parents;

    private ArrayList<TaxonomyNode> children;

    public TaxonomyNode() {
        this.OtherNames = new ArrayList<String>();
        this.children = new ArrayList<TaxonomyNode>();
        this.parents = new ArrayList<TaxonomyNode>();
        this.clearMembers();
    }

    public void clearMembers() {
        this.Identifier = null;
        this.Type = null;
        this.Rank = null;
        this.ScientificName = null;
        if (this.OtherNames != null) this.OtherNames.clear();
        this.partOfLineage = false;
        this.clearChildren();
        this.clearParents();
    }

    public void copyMembersTo(TaxonomyNode node) {
        node.setIdentifier(this.Identifier);
        node.setRank(this.Rank);
        node.setScientificName(this.ScientificName);
        node.setType(this.Type);
        node.setOtherNames(this.OtherNames);
        node.setPartOfLineage(this.partOfLineage);
        node.copyChildrenFrom(this);
        node.copyParentsFrom(this);
    }

    public void setIdentifier(String id) {
        this.Identifier = id;
    }

    public String getIdentifier() {
        return this.Identifier;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getType() {
        return this.Type;
    }

    public void setRank(String rank) {
        this.Rank = rank;
    }

    public String getRank() {
        return this.Rank;
    }

    public void setScientificName(String name) {
        this.ScientificName = name;
    }

    public String getScientificName() {
        return this.ScientificName;
    }

    public void setPartOfLineage(boolean lineage) {
        this.partOfLineage = lineage;
    }

    public boolean getPartOfLineage() {
        return this.partOfLineage;
    }

    public void setOtherNames(ArrayList<String> others) {
        this.OtherNames = others;
    }

    public ArrayList<String> getOtherNames() {
        return this.OtherNames;
    }

    public void addOthernames(String othername) {
        this.OtherNames.add(othername);
    }

    public void addParentOnly(TaxonomyNode parent) {
        this.getParents().add(parent);
    }

    public void addParent(TaxonomyNode parent) {
        this.getParents().add(parent);
        parent.getChildren().add(this);
    }

    public void addChild(TaxonomyNode child) {
        this.getChildren().add(child);
        child.getParents().add(this);
    }

    public ArrayList<TaxonomyNode> getChildren() {
        return this.children;
    }

    public ArrayList<TaxonomyNode> getParents() {
        return this.parents;
    }

    public void clearParents() {
        if (this.parents != null) this.parents.clear();
    }

    public void clearChildren() {
        if (this.children != null) this.children.clear();
    }

    public void copyParentsFrom(TaxonomyNode node) {
        this.parents.addAll(node.getParents());
    }

    public void copyChildrenFrom(TaxonomyNode node) {
        this.children.addAll(node.getChildren());
    }
}
