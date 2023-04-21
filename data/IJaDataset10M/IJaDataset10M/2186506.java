package com.incendiaryblue.cmslite.search;

import com.incendiaryblue.cmslite.*;

/**
 * A bean for encapsulating the criteria for a CMS content search. Objects of
 * this type are used as the key to the content search caches.
 */
public class ContentSearchCriteria {

    private String nodeDescriptor;

    private String where, orderBy;

    private boolean recurse;

    private String stringRep;

    private Integer structureId, handleTypeId;

    /**
		 * Create a new ContentSearchCriteria object for the given search parameters
		 */
    public ContentSearchCriteria(Node n, Structure s, String where, String orderBy, boolean recurse) {
        this(n, where, orderBy, recurse);
        if (this.structureId == null && s != null) {
            this.structureId = (Integer) s.getPrimaryKey();
        }
        setStringRep();
    }

    public ContentSearchCriteria(Node n, HandleType ht, String where, String orderBy, boolean recurse) {
        this(n, where, orderBy, recurse);
        if (this.handleTypeId == null && ht != null) {
            this.handleTypeId = (Integer) ht.getPrimaryKey();
        }
        setStringRep();
    }

    public ContentSearchCriteria(Node n, String where, String orderBy, boolean recurse) {
        CategoryStub stub = n.getCategoryStub();
        if (stub.getNodeType() == Node.CONCRETE_CATEGORY && !((Category) stub).isRoot()) {
            this.structureId = (Integer) ((Category) stub).getStructure().getPrimaryKey();
        }
        this.nodeDescriptor = n.getNodeDescriptor();
        this.where = where;
        this.orderBy = orderBy;
        this.recurse = recurse;
        setStringRep();
    }

    private void setStringRep() {
        this.stringRep = "(" + nodeDescriptor + ")(" + structureId + ")(" + handleTypeId + ")(" + where + ")(" + orderBy + ")(" + recurse + ")";
    }

    /**
		 * Get the descriptor of the Node this object is searching
		 */
    public String getNodeDescriptor() {
        return this.nodeDescriptor;
    }

    public Node getNode() {
        return NodeHelper.getNode(nodeDescriptor);
    }

    public Integer getCategoryId() {
        return (Integer) getCategoryStub().getPrimaryKey();
    }

    public Structure getStructure() {
        return Structure.getStructure(structureId);
    }

    public HandleType getHandleType() {
        return HandleType.getHandleType(handleTypeId);
    }

    public CategoryStub getCategoryStub() {
        Node n = NodeHelper.getNode(nodeDescriptor);
        return n.getCategoryStub();
    }

    /**
		 * Get the where clause
		 */
    public String getWhere() {
        return this.where;
    }

    /**
		 * Get the order by clause
		 */
    public String getOrderBy() {
        return this.orderBy;
    }

    /**
		 * Return whether or not the search is recursive, i.e. the category and all
		 * its subcategories, or just the category specified
		 */
    public boolean getRecursive() {
        return this.recurse;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public String toString() {
        return stringRep;
    }

    public boolean equals(Object o) {
        if (!(o instanceof ContentSearchCriteria)) {
            return false;
        }
        return ((ContentSearchCriteria) o).stringRep.equalsIgnoreCase(this.stringRep);
    }
}
