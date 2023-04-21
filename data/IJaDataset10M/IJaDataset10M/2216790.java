package org.opencube.oms.meta;

import java.util.ArrayList;
import java.util.Date;
import org.opencube.oms.OMSNode;
import org.opencube.oms.util.OMSUtil;
import org.opencube.util.Util;

/**
 * @author <a href="mailto:maciek@fingo.pl">FINGO - Maciej Mroczko</a>
 * TODO: comment
 */
public class Scheme extends OMSNode {

    private ArrayList complexAttributes = new ArrayList();

    private ArrayList simpleAttributes = new ArrayList();

    private Namespace namespace = null;

    private Scheme parentScheme = null;

    private boolean scmAbstract = false;

    private ArrayList childSchemes = new ArrayList();

    private ArrayList referencingAttributes = new ArrayList();

    /**
	 * Full constructor.
	 * 
	 * @param namespace
	 * @param id
	 * @param name
	 * @param scmAbstract
	 * @param creatingDate
	 * @param creater
	 * @param modifyingDate
	 * @param modifier
	 * 
	 * @throws OMSMetaDataException
	 */
    Scheme(Namespace namespace, Scheme parentScheme, String id, String name, boolean scmAbstract, Date creatingDate, String creater, Date modifyingDate, String modifier) throws OMSMetaDataException {
        super(id, name, creatingDate, creater, modifyingDate, modifier);
        this.namespace = namespace;
        namespace.getMetaData().addScheme(this);
        setParentScheme(parentScheme);
        this.scmAbstract = scmAbstract;
        if (namespace == null) {
            throw new OMSMetaDataException("The namespace for the scheme cannot be null: name = '" + name + "', id = '" + id + "'");
        }
    }

    /**
	 * Creates simple attribute
	 * 
	 * @param datatype
	 * @param defaultValue
	 * @param id
	 * @param name
	 * @param creatingDate
	 * @param creater
	 * @param modifyingDate
	 * @param modifier
	 * 
	 * @return SimpleAttribute
	 * 
	 * @throws OMSMetaDataException
	 */
    public SimpleAttribute createSimpleAttribute(Datatype datatype, String defaultValue, char use, String id, String name, Date creatingDate, String creater, Date modifyingDate, String modifier) throws OMSMetaDataException {
        SimpleAttribute att = new SimpleAttribute(this, datatype, defaultValue, use, id, name, creatingDate, creater, modifyingDate, modifier);
        simpleAttributes.add(att);
        return att;
    }

    /**
	 * Creates complex attribute
	 * 
	 * @param targetScheme
	 * @param minMultiplicity
	 * @param maxMultiplicity
	 * @param type
	 * @param id
	 * @param name
	 * @param creatingDate
	 * @param creater
	 * @param modifyingDate
	 * @param modifier
	 * 
	 * @return ComplexAttribute
	 * 
	 * @throws OMSMetaDataException
	 */
    public ComplexAttribute createComplexAttribute(Scheme targetScheme, int minMultiplicity, int maxMultiplicity, char type, String id, String name, Date creatingDate, String creater, Date modifyingDate, String modifier) throws OMSMetaDataException {
        ComplexAttribute att = new ComplexAttribute(this, targetScheme, minMultiplicity, maxMultiplicity, type, id, name, creatingDate, creater, modifyingDate, modifier);
        complexAttributes.add(att);
        att.getTargetScheme().referencingAttributes.add(att);
        return att;
    }

    /**
	 * Returns the scheme namespace
	 * 
	 * @return Namespace - the namespace of this scheme
	 */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
	 * Returns all complex attributes of this scheme
	 * 
	 * @return ComplexAttribute[] - all complex attributes of this scheme
	 */
    public ComplexAttribute[] getComplexAttributes() {
        return (ComplexAttribute[]) complexAttributes.toArray(new ComplexAttribute[complexAttributes.size()]);
    }

    /**
	 * Returns all simple attributes of this scheme
	 * 
	 * @return SimpleAttribute[] - all simple attributes of this scheme
	 */
    public SimpleAttribute[] getSimpleAttributes() {
        return (SimpleAttribute[]) simpleAttributes.toArray(new SimpleAttribute[simpleAttributes.size()]);
    }

    /**
	 * Returns the complex attribute with the given id
	 * 
	 * @param id - the id of the complex attribute to find
	 * 
	 * @return ComplexAttribute - the attribute with the given id or null if not found
	 */
    public ComplexAttribute getComplexAttributeById(String id) {
        return (ComplexAttribute) OMSUtil.getNodeById(complexAttributes, id);
    }

    /**
	 * Returns the simple attribute with the given id
	 * 
	 * @param id - the id of the simple attribute to find
	 * 
	 * @return SimpleAttribute - the attribute with the given id or null if not found
	 */
    public SimpleAttribute getSimpleAttributeById(String id) {
        return (SimpleAttribute) OMSUtil.getNodeById(simpleAttributes, id);
    }

    /**
	 * Returns the complex attribute with the given name
	 * 
	 * @param name - the name of the complex attribute to find
	 * 
	 * @return ComplexAttribute - the attribute with the given name or null if not found
	 */
    public ComplexAttribute getComplexAttributeByName(String name) {
        Scheme parent = this;
        while (parent != null) {
            ComplexAttribute result = (ComplexAttribute) OMSUtil.getNodeByName(parent.complexAttributes, name);
            if (result != null) {
                return result;
            }
            parent = parent.getParentScheme();
        }
        return null;
    }

    /**
	 * Returns the simple attribute with the given name
	 * 
	 * @param name - the name of the simple attribute to find
	 * 
	 * @return SimpleAttribute - the attribute with the given name or null if not found
	 */
    public SimpleAttribute getSimpleAttributeByName(String name) {
        Scheme parent = this;
        while (parent != null) {
            SimpleAttribute result = (SimpleAttribute) OMSUtil.getNodeByName(parent.simpleAttributes, name);
            if (result != null) {
                return result;
            }
            parent = parent.getParentScheme();
        }
        return null;
    }

    public String toString() {
        String result = "<scheme name=\"" + name + "\" id=\"" + id + "\" parent_scm=\"" + (parentScheme == null ? "" : parentScheme.getName()) + "\"";
        if (simpleAttributes.size() > 0 || complexAttributes.size() > 0) {
            result += ">";
            for (int i = 0; i < simpleAttributes.size(); i++) {
                result += Util.indent(simpleAttributes.get(i).toString());
            }
            for (int i = 0; i < complexAttributes.size(); i++) {
                result += Util.indent(complexAttributes.get(i).toString());
            }
            result += "\n</scheme>";
        } else {
            result += "/>";
        }
        return result;
    }

    /**
	 * Returns the parent scheme 
	 * 
	 * @return Scheme - parent scheme
	 */
    public Scheme getParentScheme() {
        return parentScheme;
    }

    /**
	 * Checks if this scheme is instance of the scheme represented 
	 * by the given one.
	 * 
	 * @param scheme - the schema to test
	 * 
	 * @return boolean - <code>true</code> if this scheme 
	 * is instance of the scheme represented by the given one
	 */
    public boolean isSubclassOf(Scheme scheme) {
        Scheme s = this;
        do {
            if (scheme.equals(s)) {
                return true;
            }
        } while ((s = s.getParentScheme()) != null);
        return false;
    }

    /**
	 * Checks if the scheme is abstract
	 * 
	 * @return <code>true</code> if the scheme is abstract, <code>false</code> otherwise
	 */
    public boolean isAbstract() {
        return scmAbstract;
    }

    public void setParentScheme(Scheme parentScheme) {
        if (parentScheme != null) {
            parentScheme.appendChildScheme(this);
        }
        this.parentScheme = parentScheme;
    }

    public void appendChildScheme(Scheme scheme) {
        if (!childSchemes.contains(scheme)) {
            childSchemes.add(scheme);
        }
    }

    public Scheme[] getChildSchemes() {
        return (Scheme[]) childSchemes.toArray(new Scheme[childSchemes.size()]);
    }

    public ComplexAttribute[] getReferencingAttributes() {
        return (ComplexAttribute[]) referencingAttributes.toArray(new ComplexAttribute[referencingAttributes.size()]);
    }

    /**
	 * Lifert alle geerbten SimpleAttributes
	 * @return Attributes
	 */
    public SimpleAttribute[] getSimpleAttributesHierarchy() {
        ArrayList list = new ArrayList();
        Scheme parentScheme = this;
        while (parentScheme != null) {
            SimpleAttribute[] simpl = parentScheme.getSimpleAttributes();
            for (int i = 0; i < simpl.length; i++) list.add(simpl[i]);
            parentScheme = parentScheme.getParentScheme();
        }
        SimpleAttribute[] simpleAttributes = new SimpleAttribute[list.size()];
        for (int i = 0; i < list.size(); i++) simpleAttributes[i] = (SimpleAttribute) list.get(i);
        return simpleAttributes;
    }

    /**
		 * Lifert alle geerbten ComplexAttribute
		 * @return Attributes
		 */
    public ComplexAttribute[] getComplexAttributesHierarchy() {
        ArrayList list = new ArrayList();
        Scheme parentScheme = this;
        ComplexAttribute[] complexAttributes = null;
        while (parentScheme != null) {
            complexAttributes = parentScheme.getComplexAttributes();
            for (int i = 0; i < complexAttributes.length; i++) list.add(complexAttributes[i]);
            parentScheme = parentScheme.getParentScheme();
        }
        ComplexAttribute[] complexAttributes2 = new ComplexAttribute[list.size()];
        for (int i = 0; i < list.size(); i++) {
            complexAttributes2[i] = (ComplexAttribute) list.get(i);
        }
        return complexAttributes;
    }
}
