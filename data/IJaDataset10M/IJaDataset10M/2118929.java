package ontorama.webkbtools.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Description: Query consist of query term and an iterator of relation links that we
 * are interested in (for example: subtype, memberOf).
 * This list holds Integers for the relation links defined in OntoramaConfig
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 * @see ontorama.OntoramaConfig
 */
public class Query {

    /**
     * name of ontology type we are quering for
     */
    private String _typeName;

    /**
     * list of relation links for the ontology type typeName.

     */
    private List _relationLinks = new LinkedList();

    /**
     * depth of recursive query
     */
    private int _depth = -1;

    /**
     * Constructor. Initialise query type name
     */
    public Query(String typeName) {
        _typeName = typeName;
        System.out.println("created new query: " + this.toString());
    }

    /**
     * Convinience Constructor.
     * Initialise query type name and relation links
     */
    public Query(String typeName, List relationLinks) {
        _typeName = typeName;
        _relationLinks = relationLinks;
        System.out.println("created new query: " + this.toString());
    }

    /**
     * Get query type name
     * @return typeName
     */
    public String getQueryTypeName() {
        return _typeName;
    }

    /**
     * Set Relation types iterator.
     * @param   List relationLinks
     */
    public void setRelationLinks(List relationLinks) {
        _relationLinks = relationLinks;
    }

    /**
     * Get Relation types iterator
     * @return relationLinks Iterator
     */
    public Iterator getRelationLinksIterator() {
        return _relationLinks.iterator();
    }

    /**
     * Get Relation types list
     * @return relationLinks List
     */
    public List getRelationLinksList() {
        return _relationLinks;
    }

    /**
     *
     */
    public Collection getRelationLinksCollection() {
        return (Collection) _relationLinks;
    }

    /**
     * set depth for recursive query
     */
    public void setDepth(int depth) {
        _depth = depth;
        System.out.println("created new query: " + this.toString());
    }

    /**
     * get depth of recursive query
     */
    public int getDepth() {
        return _depth;
    }

    /**
     *
     */
    public String toString() {
        String str = "Query: ";
        str = str + "termName = " + _typeName + ", depth = " + _depth;
        str = str + ", relation links: " + _relationLinks;
        return str;
    }
}
