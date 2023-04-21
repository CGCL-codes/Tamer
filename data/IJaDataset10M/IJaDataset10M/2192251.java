package org.vardb.resources.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Hibernate;
import org.vardb.resources.CResourceType;
import org.vardb.util.CStringHelper;

@Entity
@Table(name = "hierarchyitems")
public class CHierarchyItem {

    protected String id;

    protected String name;

    protected String hierarchy_id;

    protected String parent_id;

    protected String resourceid;

    protected Integer left = 0;

    protected Integer right = 0;

    protected CHierarchy hierarchy;

    protected CHierarchyItem parent;

    protected Set<CHierarchyItem> items = new LinkedHashSet<CHierarchyItem>();

    protected List<CHierarchyItem> path;

    public CHierarchyItem() {
    }

    public CHierarchyItem(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(insertable = false, updatable = false)
    public String getHierarchy_id() {
        return this.hierarchy_id;
    }

    public void setHierarchy_id(String hierarchy_id) {
        this.hierarchy_id = hierarchy_id;
    }

    @Column(insertable = false, updatable = false)
    public String getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getResourceid() {
        return this.resourceid;
    }

    public void setResourceid(String resourceid) {
        this.resourceid = resourceid;
    }

    @Column(name = "lft")
    public Integer getLeft() {
        return this.left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    @Column(name = "rght")
    public Integer getRight() {
        return this.right;
    }

    public void setRight(Integer right) {
        this.right = right;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hierarchy_id")
    public CHierarchy getHierarchy() {
        return this.hierarchy;
    }

    public void setHierarchy(CHierarchy hierarchy) {
        this.hierarchy = hierarchy;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CHierarchyItem getParent() {
        return this.parent;
    }

    public void setParent(CHierarchyItem parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent_id", cascade = CascadeType.ALL)
    @OrderBy("name")
    public Set<CHierarchyItem> getItems() {
        return this.items;
    }

    public void setItems(Set<CHierarchyItem> items) {
        this.items = items;
    }

    @Transient
    public CResourceType getResourceType() {
        return CResourceType.HIERARCHY_ITEM;
    }

    public void initialize() {
        if (this.parent_id != null) Hibernate.initialize(this.parent);
        Hibernate.initialize(this.items);
    }

    public void add(CHierarchyItem item) {
        item.setParent(this);
        this.items.add(item);
    }

    public void numberNodes(String prefix) {
        this.id = prefix;
        int counter = 1;
        for (CHierarchyItem item : this.items) {
            item.numberNodes(prefix + "." + counter);
            counter++;
        }
    }

    @Transient
    public boolean getHaschildren() {
        return !getIsleaf();
    }

    @Transient
    public boolean getIsleaf() {
        return (this.right - this.left == 1);
    }

    public void getXml(StringBuilder buffer) {
        buffer.append("<hierarchyitem");
        buffer.append(" id=\"" + this.id + "\"");
        if (this.parent != null) buffer.append(" parent=\"" + this.parent.getId() + "\"");
        buffer.append(">\n");
        buffer.append("<name>" + this.name + "</name>\n");
        if (this.resourceid != null) buffer.append("<resourceid>" + this.resourceid + "</resourceid>\n");
        buffer.append("</hierarchyitem>\n");
    }

    public void createSql(StringBuilder buffer) {
        List<String> names = new ArrayList<String>();
        names.add("id");
        names.add("name");
        names.add("hierarchy_id");
        names.add("parent_id");
        names.add("resourceid");
        names.add("lft");
        names.add("rght");
        List<String> values = new ArrayList<String>();
        values.add(quote(this.id));
        values.add(quote(this.name));
        values.add(quote(this.hierarchy.getId()));
        if (this.parent != null) values.add(quote(this.parent.getId())); else values.add(quote(null));
        values.add(quote(this.resourceid));
        values.add(this.left.toString());
        values.add(this.right.toString());
        buffer.append("insert into hierarchyitems(" + CStringHelper.join(names, ",") + ")\n");
        buffer.append("values(" + CStringHelper.join(values, ",") + ");\n");
    }

    private String quote(String value) {
        if (value == null) return "null";
        return "'" + escapeSql(value) + "'";
    }

    private String escapeSql(String sql) {
        if (sql == null) return "";
        return CStringHelper.replace(sql, "'", "''");
    }

    @Transient
    public List<CHierarchyItem> getPath() {
        return this.path;
    }

    public void setPath(List<CHierarchyItem> path) {
        this.path = path;
    }
}
