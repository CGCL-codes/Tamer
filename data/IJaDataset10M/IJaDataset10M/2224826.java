package org.encuestame.utils.web;

import java.io.Serializable;

/**
 * Unit Tree Node.
 * @author Picado, Juan juanATencuestame.org
 * @since May 20, 2010 9:33:58 PM
 * @version $Id:$
 */
public class UtilTreeNode implements Serializable {

    /** **/
    private static final long serialVersionUID = 4114757339131583646L;

    private Long id;

    private TypeTreeNode node;

    private String name;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the node
     */
    public TypeTreeNode getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(TypeTreeNode node) {
        this.node = node;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
