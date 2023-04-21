package org.dtdmanipulator.decls;

import java.util.*;

/**
 * <p>
 * An <tt>ElementDefinition</tt> aggregates one {@link ElementDecl} and all
 * related {@link AttlistDecl} objects.
 * </p>
 * 
 * <p>
 * For example, assuming the following DTD declarations: <blockquote>
 * 
 * <pre>
 * &lt;!ELEMENT TITLE (#PCDATA)>
 * &lt;!ATTLIST TITLE RATING CDATA #IMPLIED>
 * &lt;!ATTLIST TITLE LANGUAGE CDATA #IMPLIED>
 * </pre>
 * 
 * </blockquote>
 * 
 * the corresponding <tt>EntityDefinition</tt> consists of one
 * {@link ElementDecl} for element <code>TITLE</code> and two
 * {@link AttlistDecl} instances, one for the <tt>RATING</tt> attribute and one
 * for the <tt>LANGUAGE</tt> attribute.
 * </p>
 * 
 * @see ElementDecl
 * @see AttlistDecl
 */
public class ElementDefinition {

    /**
     * The name of the defined XML element.
     */
    public final String Name;

    /**
     * The DTD model of the defined XML element as defined in
     * {@link ElementDecl#Model}.
     */
    public final String Model;

    /**
     * All attribute declarations for the defined XML element. The map key is
     * the attribute name.
     */
    public final Map<String, AttlistDecl> Attlists;

    /**
     * Creates a new element definition.
     * 
     * @param name
     *            the name of the defined XML element
     * @param model
     *            the DTD model of the defined XML element
     * @param attlists
     *            all attribute declarations corresponding to this XML element
     */
    public ElementDefinition(String name, String model, Collection<AttlistDecl> attlists) {
        Name = name;
        Model = model;
        Attlists = new HashMap<String, AttlistDecl>();
        for (AttlistDecl attlist : attlists) {
            if (false == Attlists.containsKey(attlist.Name)) {
                Attlists.put(attlist.Name, attlist);
            }
        }
    }
}
