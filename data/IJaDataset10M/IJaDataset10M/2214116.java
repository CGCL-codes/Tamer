package net.sf.csutils.core.registry.jaxmas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.JAXRException;
import javax.xml.registry.infomodel.Association;
import javax.xml.registry.infomodel.Classification;
import javax.xml.registry.infomodel.ClassificationScheme;
import javax.xml.registry.infomodel.Concept;
import javax.xml.registry.infomodel.InternationalString;
import javax.xml.registry.infomodel.Key;
import javax.xml.registry.infomodel.RegistryObject;
import javax.xml.registry.infomodel.Slot;
import net.sf.csutils.core.model.QName;
import net.sf.csutils.core.model.ROAttribute;
import net.sf.csutils.core.model.ROClassification;
import net.sf.csutils.core.model.ROFile;
import net.sf.csutils.core.model.RORelation;
import net.sf.csutils.core.model.ROSlot;
import net.sf.csutils.core.model.ROType;
import net.sf.csutils.core.model.impl.StaticROAttribute;
import net.sf.csutils.core.model.impl.StaticROClassification;
import net.sf.csutils.core.model.impl.StaticROFile;
import net.sf.csutils.core.model.impl.StaticRORelation;
import net.sf.csutils.core.model.impl.StaticROSlot;
import net.sf.csutils.core.model.impl.StaticROType;
import net.sf.csutils.core.registry.RegistryFacade;
import net.sf.csutils.core.registry.impl.MetaModelAccessor;
import net.sf.csutils.core.utils.Generics;
import org.apache.labs.jaxmas.registry.infomodel.BusinessQueryManagerImpl;

/**
 * CentraSite specific implementation of {@link MetaModelAccessor}.
 */
public class JaxMasMetaModelAccessor extends MetaModelAccessor {

    private static final String SLOT_NAME = "{http://namespaces.apache.org/labs/jaxmas/metaModel/1.0.0}name";

    private static final String SLOT_MAXOCCURS = "{http://namespaces.apache.org/labs/jaxmas/metaModel/1.0.0}maxOccurs";

    private static final String SLOT_MINOCCURS = "{http://namespaces.apache.org/labs/jaxmas/metaModel/1.0.0}minOccurs";

    private static final String SLOT_TAXONOMY = "{http://namespaces.apache.org/labs/jaxmas/metaModel/1.0.0}taxonomy";

    private static final String SLOT_ASSOCIATION_TYPE = "{http://namespaces.apache.org/labs/jaxmas/metaModel/1.0.0}associationType";

    private static final String SLOT_TARGET_TYPE = "{http://namespaces.apache.org/labs/jaxmas/metaModel/1.0.0}targetType";

    private static class JaxMasMetaModel extends MetaModel {

        String attributeTypesKey;

        JaxMasMetaModel(RegistryFacade pFacade) {
            super(pFacade);
        }
    }

    private static final JaxMasMetaModelAccessor theInstance = new JaxMasMetaModelAccessor();

    /**
     * Private constructor, to ensure singleton pattern.
     */
    private JaxMasMetaModelAccessor() {
    }

    /**
     * Returns the singleton instance.
     * @return The singleton instance.
     */
    public static JaxMasMetaModelAccessor getInstance() {
        return theInstance;
    }

    @Override
    public MetaModel newMetaModel(RegistryFacade pFacade) {
        return new JaxMasMetaModel(pFacade);
    }

    private Concept getConceptByTaxonomyAndValue(ClassificationScheme pTaxonomy, String pValue) throws JAXRException {
        final Collection<Concept> types = Generics.cast(pTaxonomy.getChildrenConcepts());
        for (Concept type : types) {
            if (pValue.equals(type.getValue())) {
                return type;
            }
        }
        return null;
    }

    private Concept getObjectTypeConcept(MetaModel pModel, QName pQName) throws JAXRException {
        return getConceptByTaxonomyAndValue(pModel.getFacade().getObjectTypeClassificationScheme(), pQName.toString());
    }

    private Concept getAttributeTypeConcept(MetaModel pModel, ROAttribute.Type pType) throws JAXRException {
        final ClassificationScheme metaModelTaxonomy = pModel.getFacade().findClassificationScheme("MetaModel");
        if (metaModelTaxonomy == null) {
            throw new IllegalStateException("No such taxonomy: MetaModel");
        }
        final RegistryFacade facade = pModel.getFacade();
        final Concept concept = facade.findDescendant(metaModelTaxonomy, "AttributeTypes");
        if (concept == null) {
            throw new IllegalStateException("No such concept: MetaModel/AttributeTypes");
        }
        final Concept attrConcept = facade.findDescendant(concept, pType.name());
        if (attrConcept == null) {
            throw new IllegalStateException("No such concept: MetaModel/AttributeTypes/" + pType.name());
        }
        return attrConcept;
    }

    @Override
    public ROType getROType(MetaModel pModel, QName pQName) throws JAXRException {
        synchronized (pModel) {
            final Map<QName, ROType> types = pModel.getTypes();
            final ROType type = types.get(pQName);
            if (type != null) {
                return type;
            }
            final Concept objectTypeConcept = getObjectTypeConcept(pModel, pQName);
            if (objectTypeConcept == null) {
                return null;
            }
            final StaticROType roType = new StaticROType();
            roType.setQName(pQName);
            types.put(pQName, roType);
            roType.setAttributes(getAttributes(pModel, objectTypeConcept));
            return roType;
        }
    }

    private String getAttributeTypesKey(MetaModel pModel) throws JAXRException {
        final JaxMasMetaModel model = (JaxMasMetaModel) pModel;
        if (model.attributeTypesKey == null) {
            final ClassificationScheme modelTaxonomy = pModel.getFacade().findClassificationScheme("MetaModel");
            if (modelTaxonomy == null) {
                throw new IllegalStateException("No such taxonomy: MetaModel");
            }
            final Concept concept = pModel.getFacade().findDescendant(modelTaxonomy, "AttributeTypes");
            if (concept == null) {
                throw new IllegalStateException("No such concept: MetaModel/AttributeTypes");
            }
            model.attributeTypesKey = concept.getKey().getId();
        }
        return model.attributeTypesKey;
    }

    private String getFirstSlotValue(Classification pCl, String pSlotName) throws JAXRException {
        final Slot slot = pCl.getSlot(pSlotName);
        if (slot == null) {
            throw new IllegalStateException("No such slot: " + pSlotName);
        }
        final Iterator<String> values = Generics.cast(slot.getValues().iterator());
        if (!values.hasNext()) {
            throw new IllegalStateException("No value for slot: " + pSlotName);
        }
        return values.next();
    }

    private ROAttribute getAttribute(MetaModel pModel, Classification pCl) throws JAXRException {
        final String typeName = pCl.getConcept().getValue();
        final ROAttribute.Type type;
        try {
            type = ROAttribute.Type.valueOf(typeName);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid attribute type: " + typeName);
        }
        switch(type) {
            case relation:
                return getRelationAttribute(pModel, pCl);
            case classification:
                return getClassificationAttribute(pCl);
            case file:
                return getFileAttribute(pCl);
            default:
                return getSlotAttribute(pCl, type);
        }
    }

    private Map<String, ROAttribute> getAttributes(MetaModel pModel, Concept pType) throws JAXRException {
        final Map<String, ROAttribute> attributes = new HashMap<String, ROAttribute>();
        final Collection<Classification> classifications = Generics.cast(pType.getClassifications());
        final String attributeTypesKey = getAttributeTypesKey(pModel);
        for (Classification cl : classifications) {
            final Concept clConcept = cl.getConcept();
            final RegistryObject parent = clConcept.getParent();
            if (parent != null && parent.getKey().getId().equals(attributeTypesKey)) {
                final ROAttribute attr = getAttribute(pModel, cl);
                if (attr != null) {
                    attributes.put(attr.getName(), attr);
                }
            }
        }
        return attributes;
    }

    private void map(Classification pCl, StaticROAttribute pTo) throws JAXRException {
        pTo.setName(getFirstSlotValue(pCl, SLOT_NAME));
        final String minOccursStr = getFirstSlotValue(pCl, SLOT_MINOCCURS);
        final int minOccurs;
        try {
            minOccurs = Integer.parseInt(minOccursStr);
        } catch (Exception e) {
            throw new IllegalStateException("Invalid minOccurs value: " + minOccursStr);
        }
        pTo.setMinOccurs(minOccurs);
        final String maxOccursStr = getFirstSlotValue(pCl, SLOT_MAXOCCURS);
        final int maxOccurs;
        if ("unbounded".equals(maxOccursStr)) {
            maxOccurs = -1;
        } else {
            try {
                maxOccurs = Integer.parseInt(maxOccursStr);
            } catch (Exception e) {
                throw new IllegalStateException("Invalid maxOccurs value: " + maxOccursStr);
            }
        }
        pTo.setMaxOccurs(maxOccurs);
    }

    private ROSlot getSlotAttribute(Classification pCl, ROAttribute.Type pType) throws JAXRException {
        final StaticROSlot attr = new StaticROSlot();
        attr.setType(pType);
        map(pCl, attr);
        return attr;
    }

    private ROFile getFileAttribute(Classification pCl) throws JAXRException {
        final StaticROFile file = new StaticROFile();
        map(pCl, file);
        file.setAttributeKey(pCl.getKey().getId());
        return file;
    }

    private ROClassification getClassificationAttribute(Classification pCl) throws JAXRException {
        final StaticROClassification classification = new StaticROClassification();
        map(pCl, classification);
        classification.setAttributeKey(pCl.getKey().getId());
        classification.setTaxonomy(getFirstSlotValue(pCl, SLOT_TAXONOMY));
        return classification;
    }

    private RORelation getRelationAttribute(MetaModel pModel, Classification pCl) throws JAXRException {
        final StaticRORelation relation = new StaticRORelation();
        map(pCl, relation);
        relation.setAttributeKey(pCl.getKey().getId());
        relation.setAssociationType(getFirstSlotValue(pCl, SLOT_ASSOCIATION_TYPE));
        final Slot targetTypeSlot = pCl.getSlot(SLOT_TARGET_TYPE);
        final Collection<String> targetTypeNames = Generics.cast(targetTypeSlot.getValues());
        final List<ROType> targetTypes = new ArrayList<ROType>();
        for (String targetTypeName : targetTypeNames) {
            final QName qName = QName.valueOf(targetTypeName);
            final ROType targetROType = getROType(pModel, qName);
            if (targetROType != null) {
                targetTypes.add(targetROType);
            }
        }
        relation.setTargetTypes(targetTypes);
        return relation;
    }

    @Override
    protected void remove(MetaModel pModel, ROType pType) throws JAXRException {
        final QName qName = pType.getQName();
        final BusinessQueryManagerImpl bqm = (BusinessQueryManagerImpl) pModel.getFacade().getBusinessQueryManager();
        final Collection<?> instances = bqm.findObjects(null, Collections.singleton(qName.toString()), null, null, null, null, null).getCollection();
        final BusinessLifeCycleManager blcm = pModel.getFacade().getBusinessLifeCycleManager();
        final List<Key> instanceKeys = new ArrayList<Key>();
        final Map<Key, RegistryObject> dirtyObjects = new HashMap<Key, RegistryObject>();
        for (Object instance : instances) {
            final RegistryObject ro = (RegistryObject) instance;
            final Key key = ro.getKey();
            instanceKeys.add(key);
            final Collection<?> assocs = bqm.findAssociations(null, null, key.getId(), null).getCollection();
            for (Object assocObject : assocs) {
                final Association assoc = (Association) assocObject;
                final RegistryObject source = assoc.getSourceObject();
                source.removeAssociation(assoc);
                dirtyObjects.put(source.getKey(), source);
            }
        }
        blcm.saveObjects(dirtyObjects.values());
        blcm.deleteObjects(instanceKeys);
        final Concept concept = getObjectTypeConcept(pModel, qName);
        blcm.deleteObjects(Collections.singleton(concept.getKey()));
        final ClassificationScheme scheme = pModel.getFacade().getObjectTypeClassificationScheme();
        scheme.removeChildConcept(concept);
    }

    private void add(MetaModel pModel, Concept pConcept, ROAttribute pAttr) throws JAXRException {
        final Concept attrTypeConcept = getAttributeTypeConcept(pModel, pAttr.getType());
        final BusinessLifeCycleManager blcm = pModel.getFacade().getBusinessLifeCycleManager();
        final Classification cl = blcm.createClassification(attrTypeConcept);
        pConcept.addClassification(cl);
        cl.addSlot(blcm.createSlot(SLOT_NAME, pAttr.getName(), "xs:string"));
        cl.addSlot(blcm.createSlot(SLOT_MINOCCURS, String.valueOf(pAttr.getMinOccurs()), "xs:int"));
        final int maxOccurs = pAttr.getMaxOccurs();
        cl.addSlot(blcm.createSlot(SLOT_MAXOCCURS, maxOccurs == -1 ? "unbounded" : String.valueOf(maxOccurs), "xs:string"));
        switch(pAttr.getType()) {
            case relation:
                final RORelation relation = (RORelation) pAttr;
                String assocType = relation.getAssociationType();
                if (assocType == null) {
                    assocType = relation.getName();
                }
                cl.addSlot(blcm.createSlot(SLOT_ASSOCIATION_TYPE, assocType, "xs:string"));
                final List<String> targetTypes = new ArrayList<String>();
                for (ROType roType : relation.getTargetTypes()) {
                    targetTypes.add(roType.getQName().toString());
                }
                cl.addSlot(blcm.createSlot(SLOT_TARGET_TYPE, targetTypes, "xs:string"));
                break;
            case classification:
                final ROClassification classification = (ROClassification) pAttr;
                cl.addSlot(blcm.createSlot(SLOT_TAXONOMY, classification.getTaxonomy(), "xs:string"));
                break;
            case file:
                break;
            default:
                break;
        }
    }

    @Override
    protected ROType add(MetaModel pModel, ROType pType) throws JAXRException {
        final QName qName = pType.getQName();
        final BusinessLifeCycleManager blcm = pModel.getFacade().getBusinessLifeCycleManager();
        final InternationalString is = blcm.createInternationalString(Locale.US, qName.getLocalPart());
        final Concept concept = blcm.createConcept(pModel.getFacade().getObjectTypeClassificationScheme(), is, qName.toString());
        for (ROAttribute attr : pType.getAttributes().values()) {
            add(pModel, concept, attr);
        }
        blcm.saveObjects(Collections.singleton(concept));
        return this.getROType(pModel, pType.getQName());
    }
}
