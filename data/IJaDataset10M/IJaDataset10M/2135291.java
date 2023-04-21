package com.keggview.application.xml.datatypes;

import java.util.ArrayList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * The entry element contains information about a node of the pathway. The
 * attributes of this element are as follows.
 * 
 * @author Pablo
 * 
 */
@XStreamAlias("entry")
public class Entry extends Base {

    public static final String T_ORTHOLOG = "ortholog";

    public static final String T_ENZYME = "enzyme";

    public static final String T_GENE = "gene";

    public static final String T_GROUP = "group";

    public static final String T_COMPOUND = "compound";

    public static final String T_MAP = "map";

    /**
	 * (required) The id attribute specifies the identification number of this
	 * entry. Each entry element in the pathway element is uniquely specified
	 * according to this id attribute value.<br>
	 * 
	 * the identification number of this entry
	 */
    @XStreamAsAttribute
    private Integer id;

    /**
	 * (required) The name attribute contains the KEGG identifier of this entry,
	 * which is generally in the form of db:accession where db is the database
	 * name and accession is the accession number.<br>
	 * 
	 * attribute value:<br> - path:(accession) - pathway map ex)
	 * name="path:map00040"<br> - ko:(accession) - KO (ortholog group) ex)
	 * name="ko:E3.1.4.11"<br> - ec:(accession) - enzyme ex) name="ec:1.1.3.5"<br> -
	 * cpd:(accession) - chemical compound ex) name="cpd:C01243"<br> -
	 * gl:(accession) - glycan ex) name="gl:G00166"<br> - [org
	 * prefix]:(accession) - gene product of a given organism ex)
	 * name="eco:b1207"<br> - group:(accession) - complex of KOs If accession
	 * is undefined, "undefined" is specified. ex) name="group:ORC"<br>
	 * 
	 * 
	 */
    @XStreamAsAttribute
    private String name;

    /**
	 * (required) The type attribute specifies the type of this entry. Note that
	 * when the pathway map is linked to another map, the linked pathway map is
	 * treated as a node, a clickable graphics object (round rectangle) in the
	 * KEGG Web service. 
	 * 
	 * types:<br> - ortholog : the node is a KO (ortholog
	 * group)<br> - enzyme : the node is an enzyme<br> - gene : the node is a
	 * gene product (mostly a protein)<br> - group : the node is a complex of
	 * gene products (mostly a protein complex)<br> - compound : the node is a
	 * chemical compound (including a glycan)<br> - map : the node is a linked
	 * pathway map<br>
	 */
    @XStreamAsAttribute
    private String type;

    /**
	 * (implied) The link attribute specifies the resource location of the
	 * information about this entry in the KEGG Web service. In the
	 * organism-specific pathways, this attribute is not defined if the organism
	 * does not have the entry (gene).
	 */
    @XStreamAsAttribute
    private String link;

    /**
	 * (implied) The reaction attribute specifies the KEGGID of the
	 * corresponding chemical reaction(s) in the KEGG LIGAND database. 
	 * 
	 * example:<br>
	 * rn:(accession - ex)reaction="rn:R02749"
	 */
    @XStreamAsAttribute
    private String reaction;

    /**
	 * (implied) When the current pathway map is linked to another pathway map,
	 * it is represented by an entry element of "map" type. Some nodes in the
	 * linked pathway maps are stored as partners of binary relations in the
	 * KGML file, although they are not shown in the actual KEGG pathway map.
	 * The map attribute specifies the ID of the linked map entry.<br>
	 * 
	 * format:<br>
	 * positive interger - ex)map="1"
	 * 
	 */
    @XStreamAsAttribute
    private Integer map;

    /**
	 * [0,n)
	 */
    @XStreamImplicit
    private ArrayList<Component> components;

    private Graphics graphics;

    @XStreamOmitField
    private Reaction reactionObj;

    private Entry() {
        Registry.register(this);
    }

    /**
	 * @param id
	 * @param name
	 * @param type
	 * @param link
	 * @param reaction
	 * @param map
	 */
    public Entry(String id, String name, String type, String link, String reaction, Integer map) {
        this();
        this.components = new ArrayList<Component>(0);
        this.graphics = null;
        this.id = Integer.valueOf(id);
        this.name = name;
        this.type = type;
        if (link == null) {
            this.link = new String("");
        } else {
            this.link = link;
        }
        if (reaction == null) {
            this.reaction = new String("");
        } else {
            this.reaction = reaction;
        }
        if (map == null) {
            this.map = new Integer(-1);
        } else {
            this.map = map;
        }
    }

    /**
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type
	 *            the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the link
	 */
    public String getLink() {
        return link;
    }

    /**
	 * @param link
	 *            the link to set
	 */
    public void setLink(String link) {
        this.link = link;
    }

    /**
	 * @return the reaction
	 */
    public String getReaction() {
        return reaction;
    }

    public Reaction getReactionObject() {
        if (this.reactionObj == null) {
            this.reactionObj = Registry.nReactions.get(this.reaction);
        }
        return this.reactionObj;
    }

    /**
	 * @param reaction
	 *            the reaction to set
	 */
    public void setReaction(String reaction) {
        this.reaction = reaction;
        this.reactionObj = Registry.nReactions.get(this.reaction);
    }

    /**
	 * @return the map
	 */
    public Integer getMap() {
        return map;
    }

    /**
	 * @param map
	 *            the map to set
	 */
    public void setMap(String map) {
        this.map = Integer.valueOf(map);
    }

    /**
	 * @param map
	 *            the map to set
	 */
    public void setMap(Integer map) {
        this.map = map;
    }

    /**
	 * @return the components
	 */
    public ArrayList<Component> getComponents() {
        return components;
    }

    /**
	 * @param components the components to set
	 */
    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    /**
	 * @return the graph
	 */
    public Graphics getGraph() {
        return graphics;
    }

    /**
	 * @param graph the graph to set
	 */
    public void setGraph(Graphics graph) {
        this.graphics = graph;
    }
}
