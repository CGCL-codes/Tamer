package org.opencube.oms.meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.opencube.oms.util.OMSUtil;
import org.opencube.util.Constants;

/**
 * @author <a href="mailto:maciek@fingo.pl">FINGO - Maciej Mroczko</a>
 * TODO: comment
 */
public class OMSMetaData {

    private HashMap schemes = new HashMap();

    private HashMap complexAttributes = new HashMap();

    private HashMap simpleAttributes = new HashMap();

    private HashMap datatypes = new HashMap();

    private HashMap datatypeEnumerations = new HashMap();

    private ArrayList namespaces = new ArrayList();

    private HashMap children = new HashMap();

    /**
	 * Creates new namespace and appends it to the list
	 * 
	 * @param parentId
	 * @param id
	 * @param name
	 * @param creatingDate
	 * @param creater
	 * @param modifyingDate
	 * @param modifier
	 * 
	 * @return The newly created namespace
	 */
    public Namespace createNamespace(String parentId, String id, String name, Date creatingDate, String creater, Date modifyingDate, String modifier) {
        Namespace namespace = new Namespace(this, parentId, id, name, creatingDate, creater, modifyingDate, modifier);
        if (parentId != null) {
            ArrayList list = (ArrayList) children.get(parentId);
            if (list == null) {
                list = new ArrayList();
                children.put(parentId, list);
            }
            list.add(namespace);
        }
        namespaces.add(namespace);
        return namespace;
    }

    /**
	 * Returns all namesapces.
	 * 
	 * @return Namesapce[] - all namesapces
	 */
    public Namespace[] getAllNamespaces() {
        return (Namespace[]) namespaces.toArray(new Namespace[namespaces.size()]);
    }

    /**
	 * Returns the namespace with the given name.
	 * 
	 * @param name - the name of the namespace to find
	 * 
	 * @return Namespace - namespace with the given name, null if not exists
	 */
    public Namespace getNamespaceByPath(String path) {
        StringTokenizer st = new StringTokenizer(path, Namespace.PATH_SEPARATOR);
        String name = st.nextToken();
        Namespace result = (Namespace) OMSUtil.getNodeByName(Arrays.asList(getRootNamespaces()), name);
        while (st.hasMoreTokens() && result != null) {
            name = st.nextToken();
            result = result.getChildNamespaceByName(name);
        }
        return result;
    }

    /**
	 * Returns the namespace with the given id.
	 * 
	 * @param id - the id of the namespace to find
	 * 
	 * @return Namespace - namespace with the given id, null if not exists
	 */
    public Namespace getNamespaceById(String id) {
        return (Namespace) OMSUtil.getNodeById(this.namespaces, id);
    }

    /**
	 * Returns scheme with given id
	 *  
	 * @param id the id of the scheme to find
	 * 
	 * @return Scheme - scheme with the given id, null if not exists
	 */
    public Scheme getSchemeById(String id) {
        return (Scheme) schemes.get(id);
    }

    /**
	 * Returns datatype with given id
	 *  
	 * @param id - the id of the datatype to find
	 * 
	 * @return Datatype - datatype with the given id, null if not exists
	 */
    public Datatype getDatatypeById(String id) {
        return (Datatype) datatypes.get(id);
    }

    /**
	 * Returns datatype enumeration with given id
	 *  
	 * @param id - the id of the datatype enumeration to find
	 * 
	 * @return DatatypeEnumeration - datatype enumeration with the given id, null if not exists
	 */
    public DatatypeEnumeration getDatatypeEnumerationById(String id) {
        return (DatatypeEnumeration) datatypeEnumerations.get(id);
    }

    /**
	 * Returns simple attribute with given id
	 *  
	 * @param id - the id of the simple attribute to find
	 * 
	 * @return SimpleAttribute - simple attribute with the given id, null if not exists
	 */
    public SimpleAttribute getSimpleAttributeById(String id) {
        return (SimpleAttribute) simpleAttributes.get(id);
    }

    /**
	 * Returns complex attribute with given id
	 *  
	 * @param id - the id of the complex attribute to find
	 * 
	 * @return ComplexAttribute - complex attribute with the given id, null if not exists
	 */
    public ComplexAttribute getComplexAttributeById(String id) {
        return (ComplexAttribute) complexAttributes.get(id);
    }

    /**
	 * Returns all the top level namespaces.
	 * 
	 * @return All the top level namespaces
	 */
    public Namespace[] getRootNamespaces() {
        ArrayList result = new ArrayList();
        for (int i = 0; namespaces != null && i < namespaces.size(); i++) {
            if (((Namespace) namespaces.get(i)).getParentNamespace() == null) {
                result.add(namespaces.get(i));
            }
        }
        return (Namespace[]) result.toArray(new Namespace[result.size()]);
    }

    /**
	 * Returns the children of the namespace with the given id
	 * 
	 * @param id the id of the namespace to get the children
	 * 
	 * @return The children of the namespace with the given id
	 */
    protected Namespace[] getNamespaceChildren(String id) {
        ArrayList childrenList = (ArrayList) children.get(id);
        if (childrenList != null) {
            return (Namespace[]) childrenList.toArray(new Namespace[childrenList.size()]);
        } else {
            return null;
        }
    }

    /**
	 * Returns the namespace child with the given name.
	 * 
	 * @param id the id of the parent namespace
	 * @param name name of the child namespace to find
	 * 
	 * @return The namespace child with the given name
	 */
    protected Namespace getChildNamespaceByName(String id, String name) {
        ArrayList childrenList = (ArrayList) children.get(id);
        if (childrenList != null) {
            return (Namespace) OMSUtil.getNodeByName(childrenList, name);
        } else {
            return null;
        }
    }

    public Scheme getScheme(String namespacePath, String schemeName) {
        Namespace n = getNamespaceByPath(namespacePath);
        if (n != null) {
            return n.getSchemeByName(schemeName);
        } else {
            return null;
        }
    }

    public SimpleAttribute getSimpleAttributeFromHierarchy(Scheme scheme, String attribute) throws OMSMetaDataException {
        Scheme s = scheme;
        do {
            SimpleAttribute att = null;
            if ((att = s.getSimpleAttributeByName(attribute)) != null) {
                return att;
            }
        } while ((s = s.getParentScheme()) != null);
        throw new OMSMetaDataException("The simple attribute '" + attribute + "' not found in the scheme '" + scheme.getNamespace().getPath() + Constants.STR_SLASH + scheme.getName() + "' hierarchy");
    }

    public ComplexAttribute getComplexAttributeFromHierarchy(Scheme scheme, String attribute) throws OMSMetaDataException {
        Scheme s = scheme;
        if (s == null) {
            return null;
        }
        do {
            ComplexAttribute att = null;
            if ((att = s.getComplexAttributeByName(attribute)) != null) {
                return att;
            }
        } while ((s = s.getParentScheme()) != null);
        throw new OMSMetaDataException("The complex attribute '" + attribute + "' not found in the scheme '" + scheme.getNamespace().getPath() + Constants.STR_SLASH + scheme.getName() + "' hierarchy");
    }

    void addScheme(Scheme scm) {
        schemes.put(scm.getId(), scm);
    }

    void addComplexAttribute(ComplexAttribute cat) {
        complexAttributes.put(cat.getId(), cat);
    }

    void addSimpleAttribute(SimpleAttribute sat) {
        simpleAttributes.put(sat.getId(), sat);
    }

    void addDatatype(Datatype dat) {
        datatypes.put(dat.getId(), dat);
    }

    void addDatatypeEnumeration(DatatypeEnumeration den) {
        datatypeEnumerations.put(den.getId(), den);
    }
}
