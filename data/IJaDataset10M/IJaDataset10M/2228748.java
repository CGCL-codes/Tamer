package com.foursoft.fourever.xmlfileio.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import com.foursoft.fourever.objectmodel.ComplexType;
import com.foursoft.fourever.objectmodel.CompositeBinding;
import com.foursoft.fourever.objectmodel.DateType;
import com.foursoft.fourever.objectmodel.HTMLType;
import com.foursoft.fourever.objectmodel.IDType;
import com.foursoft.fourever.objectmodel.IntegerType;
import com.foursoft.fourever.objectmodel.ObjectModel;
import com.foursoft.fourever.objectmodel.ReferenceBinding;
import com.foursoft.fourever.objectmodel.SimpleInstance;
import com.foursoft.fourever.objectmodel.SimpleType;
import com.foursoft.fourever.objectmodel.StringEnumerationType;
import com.foursoft.fourever.objectmodel.StringType;
import com.foursoft.fourever.objectmodel.Type;
import com.foursoft.fourever.objectmodel.exception.IllegalValueException;
import com.foursoft.fourever.objectmodel.exception.InstanceEvolutionNotSupportedException;
import com.foursoft.fourever.objectmodel.exception.NamingException;
import com.foursoft.fourever.objectmodel.exception.TargetsAlreadySetException;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;
import com.foursoft.fourever.xmlfileio.exception.SchemaProcessingException;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath.LocationPath;
import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;
import com.sun.org.apache.xerces.internal.impl.xs.identity.UniqueOrKey;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSConstants;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSIDCDefinition;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import com.sun.org.apache.xerces.internal.xs.XSModelGroup;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSParticle;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTerm;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

/**
 * A class which constructs the type and binding mesh from an post schema
 * validation infoset (PSVI)
 */
public class XMLSchemaProcessor {

    /**
	 * the object model
	 */
    private final ObjectModel objmodel;

    /**
	 * the schema
	 */
    private final XSModel schema;

    /**
	 * the set of encountered keys. If a type has been created for the element
	 * referred by the key it is stored as the value (it is a
	 * HashMap<XSIDCDefinition,Object>)
	 */
    private final HashMap<XSIDCDefinition, Set<ComplexType>> keys;

    /**
	 * the set of outstanding references, i.e. those key references which refer
	 * to an element which has not been encountered yet (it is a
	 * LinkedList<XSIDCDefinition>)
	 */
    private final LinkedList<XSIDCDefinition> keyrefs;

    /**
	 * Once a key reference is encountered which references a key for which no
	 * type is yet avaiable it is put in this set for later processing (with the
	 * referenced key definition for lookup). (it is a
	 * HashMap<XSIDCDefinition,LinkedList<DeferredBinding>>)
	 */
    private final HashMap<XSIDCDefinition, LinkedList<DeferredBinding>> missingkeys;

    /**
	 * A mapping of complex type definitions of the schema model to those of the
	 * object model to prevent creating the same type multiple times (it is a
	 * HashMap<XSComplexTypeDefinition,ComplexType>)
	 */
    private final HashMap<XSComplexTypeDefinition, ComplexType> complextypes;

    /**
	 * A mapping of string enum type definitions of the schema model to those of
	 * the object model to prevent creating the same type multiple times (it is
	 * a HashMap<XSSimpleTypeDefinition,StringEnumerationType>)
	 */
    private final HashMap<XSSimpleTypeDefinition, StringEnumerationType> enumtypes;

    /**
	 * When saving it is necessary to be able to distinguish those simple
	 * bindings which have been an element from thos who have been an attribute.
	 * This set contains those, which have been an element. (it is a
	 * HashSet<DeferredBinding>)
	 */
    private final HashSet<DeferredBinding> waselement;

    /**
	 * To retain the correct sequence of bindings, all bindings except attribute
	 * and simple content element bindings are postponed to a final processing
	 * step. This set contains the postponed bindings. (it is a
	 * LinkedList<DeferredBinding>)
	 */
    private final List<DeferredBinding> bindings;

    /**
	 * Set of objects already sent to the "validateKeys" method in order to stop
	 * out of control recursion. Should be cleared after each reading of a
	 * Document.
	 * 
	 */
    private final HashSet<XSTypeDefinition> alreadyValidated;

    /**
	 * true if processing has already taken place
	 */
    private boolean processed;

    /**
	 * a helper for processing annotations
	 */
    private final SimpleAnnotationParser annoparser;

    /**
	 * Constructor for a xml schema processor
	 * 
	 * @param s
	 *            the xs model
	 * @param om
	 *            the object model
	 */
    public XMLSchemaProcessor(XSModel s, ObjectModel om) {
        objmodel = om;
        schema = s;
        keys = new HashMap<XSIDCDefinition, Set<ComplexType>>();
        keyrefs = new LinkedList<XSIDCDefinition>();
        missingkeys = new HashMap<XSIDCDefinition, LinkedList<DeferredBinding>>();
        alreadyValidated = new HashSet<XSTypeDefinition>();
        complextypes = new HashMap<XSComplexTypeDefinition, ComplexType>();
        enumtypes = new HashMap<XSSimpleTypeDefinition, StringEnumerationType>();
        waselement = new HashSet<DeferredBinding>();
        processed = false;
        bindings = new LinkedList<DeferredBinding>();
        annoparser = new SimpleAnnotationParser();
    }

    /**
	 * Construct the types from the schema. This is done in a "recursive
	 * descent" kind to break down complexity.
	 * 
	 * @return a HashSet of SimpleBindings which were represented as elements in
	 *         the xml file
	 * @throws SchemaProcessingException
	 *             if anything goes wrong, like unsupported xml schema features,
	 *             exceptions from the object model etc.
	 */
    public Set<CompositeBinding> processSchema() throws SchemaProcessingException {
        int i;
        Set<CompositeBinding> result;
        CompositeBinding cb;
        alreadyValidated.clear();
        if (processed) {
            throw new SchemaProcessingException("processSchema() may only be called once.");
        } else {
            processed = true;
        }
        XSNamedMap map = schema.getComponents(XSConstants.ELEMENT_DECLARATION);
        for (i = 0; i < map.getLength(); i++) {
            processElementDeclaration((XSElementDeclaration) map.item(i), 0, 1, null);
        }
        if (missingkeys.size() > 0) {
            String message = "Fatal: unresolved references - i.e. missing key \"" + missingkeys.keySet().iterator().next().getName() + "\".";
            XMLFileIOManagerImpl.log.error(message);
            throw new SchemaProcessingException(message);
        }
        result = new HashSet<CompositeBinding>();
        Map<ReferenceBinding, DeferredBinding> collectedDeferredReferencesMap = new LinkedHashMap<ReferenceBinding, DeferredBinding>();
        for (DeferredBinding b : bindings) {
            try {
                if (b.target instanceof SimpleType) {
                    cb = objmodel.createCompositeBinding(b.name, b.min, b.max, b.source, b.descr, null, b.target);
                    if (waselement.contains(b)) {
                        result.add(cb);
                    }
                } else {
                    if (b.reference) {
                        try {
                            assert (b instanceof DeferredReferenceBinding);
                            ReferenceBinding inCompleteBinding = objmodel.createReferenceBinding(b.name, b.min, b.max, b.source, b.descr, null, objmodel);
                            collectedDeferredReferencesMap.put(inCompleteBinding, b);
                        } catch (InstanceEvolutionNotSupportedException e) {
                            throw new SchemaProcessingException("error in creating bindings", e);
                        }
                    } else {
                        objmodel.createCompositeBinding(b.name, b.min, b.max, b.source, b.descr, null, b.target);
                    }
                }
            } catch (InstanceEvolutionNotSupportedException ex) {
                throw new SchemaProcessingException("Exception while creating bindings", ex);
            } catch (TypeMismatchException ex) {
                throw new SchemaProcessingException("Exception while creating bindings", ex);
            }
        }
        try {
            for (ReferenceBinding binding : collectedDeferredReferencesMap.keySet()) {
                DeferredBinding b = collectedDeferredReferencesMap.get(binding);
                assert (b instanceof DeferredReferenceBinding);
                Set<ComplexType> refTypes = ((DeferredReferenceBinding) b).referenceTargetTypes;
                assert (refTypes != null);
                Set<CompositeBinding> referenceBindings = getReferredBindingsForTargetTypes(refTypes);
                objmodel.assignTargetsToReferenceBinding(binding, referenceBindings);
            }
        } catch (TargetsAlreadySetException ex) {
            XMLFileIOManagerImpl.log.error("Error while creating bindings", ex);
            throw new SchemaProcessingException("Error while creating bindings", ex);
        }
        alreadyValidated.clear();
        return result;
    }

    /**
	 * Takes a list of Types and returns the parent binding which are not root
	 * bindings. May need to enforce the key conditions in the future.
	 * 
	 * @param referenceTargetTypes
	 *            the referenced target types
	 * @return the referred bindings
	 */
    Set<CompositeBinding> getReferredBindingsForTargetTypes(Set<ComplexType> referenceTargetTypes) {
        assert (referenceTargetTypes != null);
        Set<CompositeBinding> referenceBindings = new LinkedHashSet<CompositeBinding>();
        for (ComplexType type : referenceTargetTypes) {
            for (Iterator<CompositeBinding> parentBindings = type.getParentBindings(); parentBindings.hasNext(); ) {
                CompositeBinding bind = parentBindings.next();
                if (bind.getContext() != null) {
                    referenceBindings.add(bind);
                } else {
                    continue;
                }
            }
        }
        return referenceBindings;
    }

    /**
	 * process a TypeDefinition
	 * 
	 * @param td
	 *            the type definition
	 * @param attrkey
	 *            the attribute key
	 * @return the new type
	 * @throws SchemaProcessingException
	 *             on error
	 */
    private Type processTypeDefinition(XSTypeDefinition td, XSIDCDefinition attrkey) throws SchemaProcessingException {
        if (td.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            return processSimpleTypeDefinition((XSSimpleTypeDefinition) td, "");
        } else {
            return processComplexTypeDefinition((XSComplexTypeDefinition) td, attrkey);
        }
    }

    /**
	 * process a SimpleTypeDefinition
	 * 
	 * @param td
	 *            the definition to process
	 * @param attributeName
	 *            the name of the attribute to be output whenever an error
	 *            occurs
	 * @return the new type
	 * @throws SchemaProcessingException
	 */
    private SimpleType processSimpleTypeDefinition(XSSimpleTypeDefinition td, String attributeName) throws SchemaProcessingException {
        switch(td.getVariety()) {
            case XSSimpleTypeDefinition.VARIETY_ATOMIC:
                {
                    switch(td.getBuiltInKind()) {
                        case XSConstants.STRING_DT:
                            {
                                if ((td.getName() != null) && td.getName().equals("html")) {
                                    return (SimpleType) objmodel.getTypeByName(HTMLType.TYPE_NAME);
                                }
                                if (td.isDefinedFacet(XSSimpleTypeDefinition.FACET_ENUMERATION)) {
                                    LinkedList<String> values;
                                    int i;
                                    StringList sl;
                                    StringEnumerationType set;
                                    String setname;
                                    String annotation = "";
                                    if (enumtypes.containsKey(td)) {
                                        return enumtypes.get(td);
                                    } else {
                                        values = new LinkedList<String>();
                                        sl = td.getLexicalEnumeration();
                                        for (i = 0; i < sl.getLength(); i++) {
                                            values.add(sl.item(i));
                                        }
                                        if (td.getAnonymous()) {
                                            setname = null;
                                        } else {
                                            setname = td.getName();
                                        }
                                        if (td.getAnnotations() != null) {
                                            XSObjectList annoList = td.getAnnotations();
                                            for (int annoIndex = 0; annoIndex < annoList.getLength(); annoIndex++) {
                                                XSAnnotation anno = (XSAnnotation) annoList.item(annoIndex);
                                                if (anno != null) {
                                                    annotation += processAnnotation(anno);
                                                }
                                            }
                                        }
                                        try {
                                            set = objmodel.createStringEnumerationType(setname, annotation, values.iterator());
                                        } catch (NamingException e) {
                                            throw new SchemaProcessingException("Exception creating string enumeration type" + ((setname == null) ? " (anonymous)" : ((" with name " + setname) + " for attribute " + attributeName)), e);
                                        }
                                        enumtypes.put(td, set);
                                        return set;
                                    }
                                }
                                return (SimpleType) objmodel.getTypeByName(StringType.TYPE_NAME);
                            }
                        case XSConstants.INTEGER_DT:
                            return (SimpleType) objmodel.getTypeByName(IntegerType.TYPE_NAME);
                        case XSConstants.DATE_DT:
                            return (SimpleType) objmodel.getTypeByName(DateType.TYPE_NAME);
                        default:
                            throw new SchemaProcessingException("Unsupported built-in type : " + td.getName());
                    }
                }
            default:
                {
                    String message = "Unsupported variety in built-in type (other than atomic) : \"" + td.getName() + "\" in the definition of attribute \"" + attributeName + "\"";
                    if (td.getName() == "anySimpleType") {
                        message += "\nMaybe no type is assigned to the attribute";
                    }
                    throw new SchemaProcessingException(message);
                }
        }
    }

    /**
	 * process a ComplexTypeDefinition
	 * 
	 * @param td
	 *            the type definition
	 * @param attrkey
	 *            the attribute key
	 * @return the new type
	 * @throws SchemaProcessingException
	 *             on error
	 */
    private ComplexType processComplexTypeDefinition(XSComplexTypeDefinition td, XSIDCDefinition attrkey) throws SchemaProcessingException {
        XSObjectList attr;
        int i;
        ComplexType t;
        if (complextypes.containsKey(td)) {
            t = complextypes.get(td);
            if (attrkey != null) {
                if (hasIDAttribute(td)) {
                    Set<ComplexType> targetTypeSet = keys.get(attrkey);
                    if (targetTypeSet == null) {
                        targetTypeSet = new HashSet<ComplexType>();
                        keys.put(attrkey, targetTypeSet);
                    }
                    targetTypeSet.add(t);
                    if (missingkeys.containsKey(attrkey)) {
                        completeDeferredBindings(attrkey, targetTypeSet);
                    }
                }
            }
            validateKeys(td);
            return t;
        } else {
            String name = null;
            if (!td.getAnonymous()) {
                name = td.getName();
            }
            try {
                t = objmodel.createEntityType(null, "");
            } catch (NamingException e) {
                throw new SchemaProcessingException("Failed to create entity type " + ((name == null) ? "(anonymous)" : name), e);
            }
            complextypes.put(td, t);
        }
        attr = td.getAttributeUses();
        for (i = 0; i < attr.getLength(); i++) {
            processAttributeUse((XSAttributeUse) attr.item(i), attrkey, t);
        }
        switch(td.getContentType()) {
            case XSComplexTypeDefinition.CONTENTTYPE_EMPTY:
                break;
            case XSComplexTypeDefinition.CONTENTTYPE_ELEMENT:
                processParticle(td.getParticle(), t);
                break;
            case XSComplexTypeDefinition.CONTENTTYPE_SIMPLE:
                {
                    SimpleType st;
                    st = processSimpleTypeDefinition(td.getSimpleType(), "");
                    try {
                        objmodel.createSimpleCompositeBinding("", false, t, "", null, st);
                    } catch (InstanceEvolutionNotSupportedException ex) {
                        XMLFileIOManagerImpl.log.error("Error occurred during creation of simple binding", ex);
                        throw new SchemaProcessingException("Error occurred during creation of simple binding", ex);
                    } catch (TypeMismatchException ex) {
                        XMLFileIOManagerImpl.log.error("Error occurred during creation of simple binding", ex);
                        throw new SchemaProcessingException("Error occurred during creation of simple binding", ex);
                    }
                }
                break;
            default:
                throw new SchemaProcessingException("Unsupported Content Type");
        }
        return t;
    }

    /**
	 * process AttributeUse
	 * 
	 * @param au
	 * @param attrkey
	 * @param parent
	 * @throws SchemaProcessingException
	 */
    private void processAttributeUse(XSAttributeUse au, XSIDCDefinition attrkey, ComplexType parent) throws SchemaProcessingException {
        switch(au.getConstraintType()) {
            case XSConstants.VC_NONE:
                processAttributeDeclaration(au.getAttrDeclaration(), au.getRequired(), null, attrkey, parent);
                break;
            case XSConstants.VC_DEFAULT:
                processAttributeDeclaration(au.getAttrDeclaration(), au.getRequired(), au.getConstraintValue(), attrkey, parent);
                break;
            default:
                throw new SchemaProcessingException("Unsupported constraint type");
        }
    }

    /**
	 * process AttributeDeclaration
	 * 
	 * @param ad
	 * @param required
	 * @param def
	 * @param attrkey
	 * @param parent
	 * @throws SchemaProcessingException
	 */
    private void processAttributeDeclaration(XSAttributeDeclaration ad, boolean required, String def, XSIDCDefinition attrkey, ComplexType parent) throws SchemaProcessingException {
        SimpleType st;
        SimpleInstance si;
        if (ad.getName().equals("id")) {
            st = (SimpleType) objmodel.getTypeByName(IDType.TYPE_NAME);
            if (attrkey != null) {
                Set<ComplexType> targetTypeSet = keys.get(attrkey);
                if (targetTypeSet == null) {
                    targetTypeSet = new LinkedHashSet<ComplexType>();
                }
                targetTypeSet.add(parent);
                keys.put(attrkey, targetTypeSet);
                if (missingkeys.containsKey(attrkey)) {
                    completeDeferredBindings(attrkey, targetTypeSet);
                }
            }
        } else if (ad.getName().equals("base")) {
            XMLFileIOManagerImpl.log.debug("xml:base in objectmodel attribute ignored.");
            return;
        } else {
            st = processSimpleTypeDefinition(ad.getTypeDefinition(), ad.getName());
        }
        si = null;
        if (def != null) {
            si = (SimpleInstance) st.createInstance(false);
            try {
                si.setValueFromString(def);
            } catch (IllegalValueException e) {
                throw new SchemaProcessingException("Exception while setting value for simple instance", e);
            }
        }
        try {
            objmodel.createSimpleCompositeBinding(ad.getName(), required, parent, processAnnotation(ad.getAnnotation()), si, st);
        } catch (InstanceEvolutionNotSupportedException ex) {
            XMLFileIOManagerImpl.log.error("Error occurred during creation of simple binding", ex);
            throw new SchemaProcessingException("Error occurred during creation of simple binding", ex);
        } catch (TypeMismatchException ex) {
            XMLFileIOManagerImpl.log.error("Error occurred during creation of simple binding", ex);
            throw new SchemaProcessingException("Error occurred during creation of simple binding", ex);
        }
    }

    /**
	 * process Particle
	 * 
	 * @param p
	 * @param parent
	 * @throws SchemaProcessingException
	 */
    private void processParticle(XSParticle p, ComplexType parent) throws SchemaProcessingException {
        processTerm(p.getTerm(), p.getMinOccurs(), p.getMaxOccursUnbounded() ? 1048576 : p.getMaxOccurs(), parent);
    }

    /**
	 * process Term
	 * 
	 * @param t
	 * @param min
	 * @param max
	 * @param parent
	 * @throws SchemaProcessingException
	 */
    private void processTerm(XSTerm t, int min, int max, ComplexType parent) throws SchemaProcessingException {
        if (t instanceof XSElementDeclaration) {
            processElementDeclaration((XSElementDeclaration) t, min, max, parent);
        } else if (t instanceof XSModelGroup) {
            processModelGroup((XSModelGroup) t, parent);
        } else {
            throw new SchemaProcessingException("Unsupported Term");
        }
    }

    /**
	 * process ElementDeclaration
	 * 
	 * @param ed
	 * @param min
	 * @param max
	 * @param parent
	 * @throws SchemaProcessingException
	 */
    private void processElementDeclaration(XSElementDeclaration ed, int min, int max, ComplexType parent) throws SchemaProcessingException {
        HashSet<XSIDCDefinition> attrkeyRefSet = new HashSet<XSIDCDefinition>();
        HashSet<XSIDCDefinition> attrkeySet = new HashSet<XSIDCDefinition>();
        for (XSIDCDefinition idc : keys.keySet()) {
            if (idc.getFieldStrs().getLength() != 1) {
                throw new SchemaProcessingException("Only IDCs with one field supported.");
            } else {
                if (keySelectorMatchesElementName(idc, ed)) {
                    attrkeySet.add(idc);
                }
            }
        }
        for (XSIDCDefinition idc : keyrefs) {
            if (idc.getSelectorStr().equals(".//" + ed.getName())) {
                if (idc.getFieldStrs().getLength() != 1) {
                    throw new SchemaProcessingException("Only IDCs with one field supported.");
                } else {
                    attrkeyRefSet.add(idc);
                }
            }
        }
        XSNamedMap nameMap = ed.getIdentityConstraints();
        for (int i = 0; i < nameMap.getLength(); i++) {
            processIDCDefinition((XSIDCDefinition) nameMap.item(i));
        }
        boolean isRef = false;
        if (attrkeyRefSet.size() > 0) {
            for (XSIDCDefinition attrkeyref : attrkeyRefSet) {
                isRef = isRef || impliesProcessReference(ed, attrkeyref, min, max, parent);
            }
            assert (isRef);
        }
        if (!isRef) {
            Type t = null;
            if (attrkeySet.size() > 0) {
                Set<Type> locallyProcessedTypeDefinitions = new HashSet<Type>();
                for (XSIDCDefinition attrkey : attrkeySet) {
                    t = processTypeDefinition(ed.getTypeDefinition(), attrkey);
                    if (locallyProcessedTypeDefinitions.contains(t)) {
                        continue;
                    }
                    DeferredBinding b = new DeferredBinding(ed.getName(), parent, t, min, max, processAnnotation(ed.getAnnotation()));
                    locallyProcessedTypeDefinitions.add(t);
                    bindings.add(b);
                    if (t instanceof SimpleType) {
                        waselement.add(b);
                    }
                }
            } else {
                t = processTypeDefinition(ed.getTypeDefinition(), null);
                DeferredBinding b = new DeferredBinding(ed.getName(), parent, t, min, max, processAnnotation(ed.getAnnotation()));
                bindings.add(b);
                if (t instanceof SimpleType) {
                    waselement.add(b);
                }
            }
        }
    }

    /**
	 * @param keyDef
	 * @param ed
	 * @return true if the key matches
	 * 
	 */
    private boolean keySelectorMatchesElementName(XSIDCDefinition keyDef, XSElementDeclaration ed) {
        boolean matches = false;
        String selectorString = keyDef.getSelectorStr();
        String[] tokens = selectorString.split("\\|");
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].equals(".//" + ed.getName())) {
                matches = true;
                break;
            }
        }
        return matches;
    }

    /**
	 * checkImpliesProcessReference
	 * 
	 * @param ed
	 * @param attrkeyref
	 * @param min
	 * @param max
	 * @param parent
	 * @return true if the element implies a process reference
	 * @throws SchemaProcessingException
	 */
    private boolean impliesProcessReference(XSElementDeclaration ed, XSIDCDefinition attrkeyref, int min, int max, ComplexType parent) throws SchemaProcessingException {
        XSTypeDefinition td = ed.getTypeDefinition();
        if (td.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            return false;
        } else {
            XSComplexTypeDefinition ct = (XSComplexTypeDefinition) td;
            if (ct.getContentType() != XSComplexTypeDefinition.CONTENTTYPE_EMPTY) {
                return false;
            }
            XSObjectList au = ct.getAttributeUses();
            if (au.getLength() != 1) {
                return false;
            }
            XSAttributeDeclaration ad = ((XSAttributeUse) au.item(0)).getAttrDeclaration();
            if (!ad.getName().equals("link")) {
                return false;
            }
            DeferredReferenceBinding b = new DeferredReferenceBinding(ed.getName(), parent, min, max, processAnnotation(ed.getAnnotation()));
            bindings.add(b);
            if (keys.get(attrkeyref.getRefKey()) == null) {
                if (!missingkeys.containsKey(attrkeyref.getRefKey())) {
                    missingkeys.put(attrkeyref.getRefKey(), new LinkedList<DeferredBinding>());
                }
                (missingkeys.get(attrkeyref.getRefKey())).add(b);
            } else {
                b.referenceTargetTypes = keys.get(attrkeyref.getRefKey());
            }
            return true;
        }
    }

    /**
	 * process Annotation
	 * 
	 * @param a
	 * @return the string
	 * @throws SchemaProcessingException
	 */
    private String processAnnotation(XSAnnotation a) throws SchemaProcessingException {
        if (a == null) {
            return "";
        } else {
            return annoparser.parseAnnotation(a.getAnnotationString());
        }
    }

    /**
	 * process ModelGroup
	 * 
	 * @param mg
	 * @param parent
	 * @throws SchemaProcessingException
	 */
    private void processModelGroup(XSModelGroup mg, ComplexType parent) throws SchemaProcessingException {
        switch(mg.getCompositor()) {
            case XSModelGroup.COMPOSITOR_SEQUENCE:
                {
                    XSObjectList l;
                    int i;
                    l = mg.getParticles();
                    for (i = 0; i < l.getLength(); i++) {
                        processParticle((XSParticle) l.item(i), parent);
                    }
                }
                break;
            case XSModelGroup.COMPOSITOR_CHOICE:
                throw new SchemaProcessingException("Compositor <xs:choice> is not supported by XMLSchemaProcessor");
            case XSModelGroup.COMPOSITOR_ALL:
                throw new SchemaProcessingException("Compositor <xs:all> is not supported by XMLSchemaProcessor");
            default:
                throw new SchemaProcessingException("Unknown Compositor is not supported by XMLSchemaProcessor");
        }
    }

    /**
	 * process IDCDefinition
	 * 
	 * @param idc
	 * @throws SchemaProcessingException
	 */
    private void processIDCDefinition(XSIDCDefinition idc) throws SchemaProcessingException {
        switch(idc.getCategory()) {
            case XSIDCDefinition.IC_KEY:
            case XSIDCDefinition.IC_UNIQUE:
                if (idc.getFieldStrs().getLength() != 1) {
                    throw new SchemaProcessingException("IDCs with other than one field are not supported.");
                }
                if (idc.getFieldStrs().item(0).equals("./@id")) {
                    keys.put(idc, null);
                }
                break;
            case XSIDCDefinition.IC_KEYREF:
                if (idc.getFieldStrs().item(0).equals("./@link")) {
                    keyrefs.add(idc);
                }
                break;
        }
    }

    /**
	 * complete the deferred bindings: complete the references which refered to
	 * the now encountered and created key
	 * 
	 * @param attrkey
	 * @param targetTypeSet
	 * 
	 */
    private void completeDeferredBindings(XSIDCDefinition attrkey, Set<ComplexType> targetTypeSet) {
        if (missingkeys.containsKey(attrkey)) {
            for (DeferredBinding dr : missingkeys.get(attrkey)) {
                if (dr instanceof DeferredReferenceBinding) {
                    ((DeferredReferenceBinding) dr).referenceTargetTypes = targetTypeSet;
                } else {
                    assert false;
                }
            }
            missingkeys.remove(attrkey);
        }
    }

    /**
	 * validate keys: a nasty situtation is if a type has already been created
	 * but is later referenced by an element which as a key constraint on this
	 * type, e.g.
	 * 
	 * <xs:element name="wolf"> ... </xs:element>
	 * 
	 * <xs:element name="rudel"> ... <xs:element ref="wolf"/> ... <xs:key
	 * name="wolfkey"> <xs:selector xpath=".//wolf"/> ... </xs:key>
	 * </xs:element>
	 * 
	 * In this case wolf is created and much later a key constraint for wolf is
	 * encountered. Thus these possible keys need to be searched for and put
	 * into the keys table.
	 * 
	 * @param td
	 *            the type definition
	 * @throws SchemaProcessingException
	 */
    private void validateKeys(XSTypeDefinition td) throws SchemaProcessingException {
        XSComplexTypeDefinition ctd;
        if (td.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            return;
        }
        if (alreadyValidated.contains(td)) {
            return;
        } else {
            alreadyValidated.add(td);
        }
        ctd = (XSComplexTypeDefinition) td;
        switch(ctd.getContentType()) {
            case XSComplexTypeDefinition.CONTENTTYPE_EMPTY:
            case XSComplexTypeDefinition.CONTENTTYPE_SIMPLE:
                return;
            case XSComplexTypeDefinition.CONTENTTYPE_ELEMENT:
            case XSComplexTypeDefinition.CONTENTTYPE_MIXED:
                {
                    XSParticle p;
                    XSTerm t;
                    XSModelGroup mg;
                    XSObjectList ol;
                    int i;
                    p = ctd.getParticle();
                    t = p.getTerm();
                    assert (t instanceof XSModelGroup);
                    mg = (XSModelGroup) t;
                    ol = mg.getParticles();
                    for (i = 0; i < ol.getLength(); i++) {
                        assert (ol.item(i) instanceof XSParticle);
                        p = (XSParticle) ol.item(i);
                        t = p.getTerm();
                        assert (t instanceof XSElementDeclaration);
                        validateKeys((XSElementDeclaration) t);
                    }
                }
        }
    }

    /**
	 * see above
	 * 
	 * @param ed
	 * @throws SchemaProcessingException
	 */
    private void validateKeys(XSElementDeclaration ed) throws SchemaProcessingException {
        Set<XSIDCDefinition> keysToComplete = new LinkedHashSet<XSIDCDefinition>();
        for (XSIDCDefinition idc : keys.keySet()) {
            if (idc instanceof UniqueOrKey) {
                Selector selector = ((UniqueOrKey) idc).getSelector();
                LocationPath[] paths = selector.getXPath().getLocationPaths();
                for (int i = 0; i < paths.length; i++) {
                    String field = paths[i].toString();
                    if (field.equals(".//" + ed.getName())) {
                        keysToComplete.add(idc);
                    }
                }
            }
            if ((keys.get(idc) == null) && idc.getSelectorStr().equals(".//" + ed.getName())) {
                if (idc.getFieldStrs().getLength() != 1) {
                    throw new SchemaProcessingException("IDCs with other than one field are not supported.");
                }
                keysToComplete.add(idc);
            }
        }
        boolean canProcessKeys = true;
        for (XSIDCDefinition attrkey : keysToComplete) {
            if ((attrkey != null) && hasIDAttribute(ed.getTypeDefinition())) {
                ComplexType ct;
                ct = complextypes.get(ed.getTypeDefinition());
                if (ct == null) {
                    String error = "No complex type " + ed.getName();
                    XMLFileIOManagerImpl.log.error(error);
                    canProcessKeys = false;
                }
                if (canProcessKeys) {
                    Set<ComplexType> targetTypeSet = keys.get(attrkey);
                    if (targetTypeSet == null) {
                        targetTypeSet = new LinkedHashSet<ComplexType>();
                    }
                    targetTypeSet.add(ct);
                    keys.put(attrkey, targetTypeSet);
                    completeDeferredBindings(attrkey, targetTypeSet);
                }
            }
        }
        validateKeys(ed.getTypeDefinition());
    }

    /**
	 * @param td
	 * @return true iff td has an attribute named "id"
	 */
    private boolean hasIDAttribute(XSTypeDefinition td) {
        if (td.getTypeCategory() == XSTypeDefinition.SIMPLE_TYPE) {
            return false;
        } else {
            return hasIDAttribute((XSComplexTypeDefinition) td);
        }
    }

    private boolean hasIDAttribute(XSComplexTypeDefinition td) {
        XSObjectList aul;
        XSAttributeDeclaration ad;
        XSAttributeUse au;
        int i;
        aul = td.getAttributeUses();
        for (i = 0; i < aul.getLength(); i++) {
            au = (XSAttributeUse) aul.item(i);
            ad = au.getAttrDeclaration();
            if (ad.getName().equals("id")) {
                return true;
            }
        }
        return false;
    }

    /**
	 * A simple class representing a deferred binding
	 */
    private static class DeferredBinding {

        /** <code>name</code>: the name of the binding */
        public String name;

        /** <code>source</code>: the source */
        public ComplexType source;

        /** <code>target</code>: the target */
        public Type target;

        /** <code>reference</code>: the reference */
        public final boolean reference;

        /** <code>min</code>: the minimum */
        public int min;

        /** <code>max</code>: the maximum */
        public int max;

        /** <code>descr</code>: the description */
        public String descr;

        /**
		 * Create a new deferred binding
		 * 
		 * @param n
		 *            the name
		 * @param s
		 *            the source
		 * @param i
		 *            the minimum
		 * @param j
		 *            the maximum
		 * @param d
		 *            the description
		 */
        protected DeferredBinding(String n, ComplexType s, int i, int j, String d) {
            name = n;
            source = s;
            target = null;
            reference = true;
            min = i;
            max = j;
            descr = d;
        }

        public DeferredBinding(String n, ComplexType s, Type t, int i, int j, String d) {
            name = n;
            source = s;
            target = t;
            reference = false;
            min = i;
            max = j;
            descr = d;
        }
    }

    private static class DeferredReferenceBinding extends DeferredBinding {

        /** the set of possible targets for a reference * */
        public Set<ComplexType> referenceTargetTypes;

        /**
		 * Create a new deferred binding
		 * 
		 * @param n
		 *            the name
		 * @param s
		 *            the source
		 * @param i
		 *            the minimum
		 * @param j
		 *            the maximum
		 * @param d
		 *            the description
		 */
        public DeferredReferenceBinding(String n, ComplexType s, int i, int j, String d) {
            super(n, s, i, j, d);
        }
    }

    /**
	 * helper class for processing annotations: since annotations are passed
	 * literally on the PSVI interface (ie.
	 * "<annotation><documentation>Text...." ) we need to process them. This
	 * solution, based on an SAX parser, just extracts the text between the
	 * "documentation" tags.
	 */
    private static class SimpleAnnotationParser extends DefaultHandler {

        private boolean doccontext;

        private String result;

        private XMLReader reader;

        /**
		 * Default constructor
		 */
        public SimpleAnnotationParser() {
            super();
            doccontext = false;
            result = "";
            reader = new SAXParser();
            reader.setContentHandler(this);
            reader.setErrorHandler(this);
        }

        /**
		 * Parses an annotation
		 * 
		 * @param annotation
		 *            the annotation
		 * @return the parsed annotation
		 * @throws SchemaProcessingException
		 *             if an error occurred
		 */
        public String parseAnnotation(String annotation) throws SchemaProcessingException {
            StringReader sr;
            resetResult();
            sr = new StringReader(annotation);
            try {
                reader.parse(new InputSource(sr));
            } catch (SAXException ex) {
                XMLFileIOManagerImpl.log.error("Error occurred while processing Annotation", ex);
                throw new SchemaProcessingException("Error occurred while processing Annotation", ex);
            } catch (IOException ex) {
                XMLFileIOManagerImpl.log.error("Error occurred while processing Annotation", ex);
                throw new SchemaProcessingException("Error occurred while processing Annotation", ex);
            }
            return result;
        }

        /**
		 * just set the result to an empty string
		 */
        private void resetResult() {
            result = "";
        }

        /**
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
		 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
        @Override
        public void startElement(String uri, String name, String qname, Attributes a) {
            if (name.equals("documentation")) {
                doccontext = true;
            }
        }

        /**
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
		 *      java.lang.String, java.lang.String)
		 */
        @Override
        public void endElement(String uri, String name, String qname) {
            if (name.equals("documentation")) {
                doccontext = false;
            }
        }

        /**
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            int i;
            if (doccontext) {
                for (i = start; i < (start + length); i++) {
                    result += ch[i];
                }
            }
        }
    }
}
