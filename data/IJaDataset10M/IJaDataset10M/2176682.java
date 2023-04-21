package xbrlcore.instance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import xbrlcore.constants.ExceptionConstants;
import xbrlcore.constants.GeneralConstants;
import xbrlcore.constants.NamespaceConstants;
import xbrlcore.dimensions.MultipleDimensionType;
import xbrlcore.dimensions.SingleDimensionType;
import xbrlcore.exception.InstanceException;
import xbrlcore.exception.XBRLException;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.TaxonomySchema;

/**
 * This class is responsible for creating an Instance object of an instance
 * file. <br/><br/>
 * 
 * @author Daniel Hamm
 * 
 */
public class InstanceFactory {

    private static InstanceFactory xbrlInstanceFactory;

    private Instance instance;

    private Document xmlInstance;

    private Map<String, InstanceContext> contextMap;

    private Map<String, InstanceUnit> unitMap;

    private String PATH;

    private List<Namespace> schemaRefNamespaces;

    private FileLoader fileLoader = new FileLoader();

    /**
	 * Constructor, private.
	 * 
	 */
    private InstanceFactory() {
        contextMap = new HashMap<String, InstanceContext>();
        unitMap = new HashMap<String, InstanceUnit>();
        schemaRefNamespaces = new ArrayList<Namespace>();
    }

    /**
	 * 
     * @return the only Instance of InstanceFactory object (singleton).
	 */
    public static synchronized InstanceFactory get() {
        if (xbrlInstanceFactory == null) {
            xbrlInstanceFactory = new InstanceFactory();
        }
        return xbrlInstanceFactory;
    }

    /**
	 * Creates an xbrlcore.instance.Instance object.
	 * 
	 * @param instanceFile
	 *            Instance file.
	 * @return An object of xbrlcore.instance.Instance.
	 * @throws JDOMException
	 * @throws IOException
	 * @throws XBRLException
	 */
    public Instance createInstance(File instanceFile) throws JDOMException, IOException, InstanceException, XBRLException, CloneNotSupportedException, SAXException, ParserConfigurationException {
        PATH = instanceFile.getAbsolutePath().substring(0, instanceFile.getAbsolutePath().lastIndexOf(File.separator) + 1);
        SAXBuilder builder = new SAXBuilder();
        xmlInstance = builder.build(PATH + instanceFile.getName());
        Set<String> taxonomyNameSet = getReferencedSchemaNames(xmlInstance);
        xbrlcore.taxonomy.sax.SAXBuilder xbrlBuilder = new xbrlcore.taxonomy.sax.SAXBuilder();
        Iterator<String> taxonomyNameSetIterator = taxonomyNameSet.iterator();
        Set<DiscoverableTaxonomySet> dtsSet = new HashSet<DiscoverableTaxonomySet>();
        while (taxonomyNameSetIterator.hasNext()) {
            String currTaxonomyName = taxonomyNameSetIterator.next();
            String path = PATH + currTaxonomyName;
            String source = path.replaceAll("\\\\", "/");
            DiscoverableTaxonomySet currTaxonomy = xbrlBuilder.build(new InputSource(source));
            dtsSet.add(currTaxonomy);
        }
        return getInstance(dtsSet, instanceFile.getName());
    }

    /**
	 * Builds an instance.
	 * 
	 * @param dtsSet
	 *            Set of discoverable taxonomy sets this instance refers to.
	 * @return An object of xbrlcore.instance.Instance.
	 * @throws InstanceException
	 */
    private Instance getInstance(Set<DiscoverableTaxonomySet> dtsSet, String fileName) throws InstanceException, CloneNotSupportedException {
        instance = new Instance(dtsSet);
        instance.setFileName(fileName);
        setInstanceNamespace();
        setAdditionalNamespaces(xmlInstance.getRootElement().getAdditionalNamespaces());
        setAdditionalNamespaces(schemaRefNamespaces);
        setSchemaLocation();
        setContextElements();
        setUnitElements();
        setFactsAndTuples();
        return instance;
    }

    /**
	 * Determines which taxonomy an instance refers to.
	 * 
	 * @param currDocument
	 *            Structure of the instance file.
	 * @return Set of names of the taxonomy the instance refers to.
	 */
    private Set<String> getReferencedSchemaNames(Document currDocument) {
        Set<String> referencedSchemaNameSet = new HashSet<String>();
        List<Element> elementList = currDocument.getRootElement().getChildren();
        Iterator<Element> elementListIterator = elementList.iterator();
        while (elementListIterator.hasNext()) {
            Element currElement = elementListIterator.next();
            if (currElement.getName().equals("schemaRef")) {
                referencedSchemaNameSet.add(currElement.getAttributeValue("href", NamespaceConstants.XLINK_NAMESPACE));
                schemaRefNamespaces = currElement.getAdditionalNamespaces();
            }
        }
        return referencedSchemaNameSet;
    }

    /**
	 * Sets the namespace of the instance.
	 * 
	 */
    private void setInstanceNamespace() {
        Namespace instanceNamespace = xmlInstance.getRootElement().getNamespace();
        instance.setInstanceNamespace(instanceNamespace);
    }

    /**
	 * Sets additional namespaces needed in this instance.
	 * 
	 */
    private void setAdditionalNamespaces(List<Namespace> additionalNamespaces) {
        Iterator<Namespace> additionalNamespacesIterator = additionalNamespaces.iterator();
        while (additionalNamespacesIterator.hasNext()) {
            Namespace currentNamespace = additionalNamespacesIterator.next();
            if (instance.getNamespace(currentNamespace.getURI()) == null) {
                instance.addNamespace(currentNamespace);
            }
        }
    }

    /**
	 * Sets schema location information defined in this instance.
	 * 
	 * 
	 */
    private void setSchemaLocation() {
        if (xmlInstance.getRootElement().getAttributes().size() > 0 && xmlInstance.getRootElement().getAttribute("schemaLocation", instance.getNamespace(NamespaceConstants.XSI_NAMESPACE.getURI())) != null) {
            String schemaLocationValue = xmlInstance.getRootElement().getAttributeValue("schemaLocation", instance.getNamespace(NamespaceConstants.XSI_NAMESPACE.getURI()));
            while (schemaLocationValue.indexOf(" ") > 0) {
                String schemaLocationURI = schemaLocationValue.substring(0, schemaLocationValue.indexOf(" "));
                schemaLocationValue = schemaLocationValue.substring(schemaLocationValue.indexOf(" "), schemaLocationValue.length());
                schemaLocationValue = schemaLocationValue.trim();
                String schemaLocationPrefix = null;
                if (schemaLocationValue.indexOf(" ") > 0) {
                    schemaLocationPrefix = schemaLocationValue.substring(0, schemaLocationValue.indexOf(" "));
                    schemaLocationValue = schemaLocationValue.substring(schemaLocationValue.indexOf(" "), schemaLocationValue.length());
                    schemaLocationValue = schemaLocationValue.trim();
                } else {
                    schemaLocationPrefix = schemaLocationValue;
                }
                instance.addSchemaLocation(schemaLocationURI, schemaLocationPrefix);
            }
        }
    }

    public String getLocalValue(String value) {
        if (value == null) {
            return value;
        }
        return value.substring(value.indexOf(":") + 1, value.length());
    }

    public String getValueNamespace(String value) {
        if (value == null) {
            return value;
        }
        return value.substring(0, value.indexOf(":"));
    }

    /**
	 * Sets unit elements defined in this instance.
	 * 
	 * @throws InstanceException
	 */
    private void setUnitElements() throws InstanceException {
        List<Element> unitElementList = xmlInstance.getRootElement().getChildren("unit", instance.getInstanceNamespace());
        Iterator<Element> unitElementListIterator = unitElementList.iterator();
        while (unitElementListIterator.hasNext()) {
            Element currUnitElement = unitElementListIterator.next();
            String id = currUnitElement.getAttributeValue("id");
            if (id == null || id.length() == 0) {
                throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NOID_UNIT);
            }
            InstanceUnit currUnit = new InstanceUnit(id);
            List<Element> measureList = currUnitElement.getChildren("measure", instance.getInstanceNamespace());
            Iterator<Element> measureListIterator = measureList.iterator();
            while (measureListIterator.hasNext()) {
                Element currMeasureElement = measureListIterator.next();
                if (currUnit.getValue() == null) {
                    currUnit.setValue(getLocalValue(currMeasureElement.getValue()));
                } else {
                    currUnit.setValue(getLocalValue(currUnit.getValue()) + "*" + getLocalValue(currMeasureElement.getValue()));
                }
                currUnit.setNamespaceURI(instance.getNamespaceURI(getValueNamespace(currMeasureElement.getValue())));
            }
            Element divideElement = currUnitElement.getChild("divide", instance.getInstanceNamespace());
            if (divideElement != null) {
                Element unitNumeratorElement = divideElement.getChild("unitNumerator", instance.getInstanceNamespace());
                Element unitDenominatorElement = divideElement.getChild("unitDenominator", instance.getInstanceNamespace());
                Element unitNumeratorMeasureElement = unitNumeratorElement.getChild("measure", instance.getInstanceNamespace());
                Element unitDenominatorMeasureElement = unitDenominatorElement.getChild("measure", instance.getInstanceNamespace());
                currUnit.setValue(getLocalValue(unitNumeratorMeasureElement.getValue()) + "/" + getLocalValue(unitDenominatorMeasureElement.getValue()));
                currUnit.setNamespaceURI("");
            }
            unitMap.put(id, currUnit);
        }
    }

    /**
	 * @param name
	 * @return Returns a Concept object to a given name from all the
	 *         Discoverable Taxonomy Sets the instance refers to.
	 */
    private Concept getConceptByName(String name) {
        Set<DiscoverableTaxonomySet> dtsSet = instance.getDiscoverableTaxonomySet();
        Iterator<DiscoverableTaxonomySet> dtsSetIterator = dtsSet.iterator();
        while (dtsSetIterator.hasNext()) {
            DiscoverableTaxonomySet currDts = dtsSetIterator.next();
            Concept currConcept = currDts.getConceptByName(name);
            if (currConcept != null) {
                return currConcept;
            }
        }
        return null;
    }

    /**
	 * Sets context elements of this instance.
	 * 
	 * @throws InstanceException
	 */
    private void setContextElements() throws InstanceException, CloneNotSupportedException {
        List<Element> contextElementList = xmlInstance.getRootElement().getChildren("context", instance.getInstanceNamespace());
        Iterator<Element> contextElementListIterator = contextElementList.iterator();
        while (contextElementListIterator.hasNext()) {
            Element currContextElement = contextElementListIterator.next();
            String id = currContextElement.getAttributeValue("id");
            if (id == null || id.length() == 0) {
                throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NOID_CONTEXT);
            }
            InstanceContext currContext = new InstanceContext(id);
            Element identifierElement = currContextElement.getChild("entity", instance.getInstanceNamespace()).getChild("identifier", instance.getInstanceNamespace());
            currContext.setIdentifierScheme(identifierElement.getAttributeValue("scheme"));
            currContext.setIdentifier(identifierElement.getValue());
            Element periodElement = currContextElement.getChild("period", instance.getInstanceNamespace());
            if (periodElement != null) {
                if (periodElement.getChild("startDate", instance.getInstanceNamespace()) != null && periodElement.getChild("endDate", instance.getInstanceNamespace()) != null) {
                    currContext.setPeriodStartDate(periodElement.getChild("startDate", instance.getInstanceNamespace()).getText());
                    currContext.setPeriodEndDate(periodElement.getChild("endDate", instance.getInstanceNamespace()).getText());
                } else if (periodElement.getChild("instant", instance.getInstanceNamespace()) != null) {
                    if (periodElement.getChild("instant", instance.getInstanceNamespace()).getChild("forever", instance.getInstanceNamespace()) != null) {
                        currContext.setPeriodValue("forever");
                    } else {
                        currContext.setPeriodValue(periodElement.getChild("instant", instance.getInstanceNamespace()).getText());
                    }
                }
            }
            List<Element> scenSegElementList = new ArrayList<Element>();
            Element scenarioElement = currContextElement.getChild("scenario", instance.getInstanceNamespace());
            Element segmentElement = currContextElement.getChild("entity", instance.getInstanceNamespace()).getChild("segment", instance.getInstanceNamespace());
            if (scenarioElement != null) {
                scenSegElementList.add(scenSegElementList.size(), scenarioElement);
                Iterator<Element> itr = (scenarioElement.getChildren()).iterator();
                while (itr.hasNext()) {
                    currContext.addScenarioElement(itr.next());
                }
            }
            if (segmentElement != null) {
                scenSegElementList.add(scenSegElementList.size(), segmentElement);
                Iterator<Element> itr = (segmentElement.getChildren()).iterator();
                while (itr.hasNext()) {
                    currContext.addSegmentElement(itr.next());
                }
            }
            for (int i = 0; i < scenSegElementList.size(); i++) {
                Element currElement = scenSegElementList.get(i);
                List<Element> explicitMemberElementList = currElement.getChildren("explicitMember", instance.getNamespace(NamespaceConstants.XBRLDI_NAMESPACE.getURI()));
                List<Element> typedMemberElementList = currElement.getChildren("typedMember", instance.getNamespace(NamespaceConstants.XBRLDI_NAMESPACE.getURI()));
                Iterator<Element> explicitMemberElementListIterator = explicitMemberElementList.iterator();
                Iterator<Element> typedMemberElementListIterator = typedMemberElementList.iterator();
                MultipleDimensionType mdt = null;
                while (explicitMemberElementListIterator.hasNext()) {
                    Element currExplicitMemberElement = explicitMemberElementListIterator.next();
                    String dimensionAttribute = currExplicitMemberElement.getAttributeValue("dimension");
                    String prefix = dimensionAttribute.substring(0, dimensionAttribute.indexOf(":"));
                    String dimensionElementName = dimensionAttribute.substring(dimensionAttribute.indexOf(":") + 1, dimensionAttribute.length());
                    if (instance.getSchemaForURI(currExplicitMemberElement.getNamespace(prefix)) == null) {
                        throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NO_SCHEMA_PREFIX + prefix);
                    }
                    Concept dimensionElement = instance.getSchemaForURI(currExplicitMemberElement.getNamespace(prefix)).getConceptByName(dimensionElementName);
                    String value = currExplicitMemberElement.getValue();
                    String domainMemberElementName = value.substring(value.indexOf(":") + 1, value.length());
                    Concept domainMemberElement = getConceptByName(domainMemberElementName);
                    if (dimensionElement == null || domainMemberElement == null) {
                        throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_DIMENSIONS + id);
                    }
                    if (mdt == null) {
                        mdt = new MultipleDimensionType(dimensionElement, domainMemberElement);
                    } else {
                        mdt.addPredecessorDimensionDomain(new SingleDimensionType(dimensionElement, domainMemberElement));
                    }
                }
                while (typedMemberElementListIterator.hasNext()) {
                    Element currTypedMemberElement = typedMemberElementListIterator.next();
                    String dimensionAttribute = currTypedMemberElement.getAttributeValue("dimension");
                    String prefix = dimensionAttribute.substring(0, dimensionAttribute.indexOf(":"));
                    String dimensionElementName = dimensionAttribute.substring(dimensionAttribute.indexOf(":") + 1, dimensionAttribute.length());
                    Concept dimensionElement = instance.getSchemaForURI(currTypedMemberElement.getNamespace(prefix)).getConceptByName(dimensionElementName);
                    SingleDimensionType sdt = null;
                    if (currTypedMemberElement.getChildren().size() != 0) {
                        Element childElement = (Element) currTypedMemberElement.getChildren().get(0);
                        sdt = new SingleDimensionType(dimensionElement, childElement);
                    }
                    if (mdt == null) {
                        mdt = new MultipleDimensionType(sdt);
                    } else {
                        mdt.addPredecessorDimensionDomain(sdt);
                    }
                }
                if (mdt != null && currElement.getName().equals("scenario")) {
                    currContext.setDimensionalInformation(mdt, GeneralConstants.DIM_SCENARIO);
                } else if (mdt != null && currElement.getName().equals("segment")) {
                    currContext.setDimensionalInformation(mdt, GeneralConstants.DIM_SEGMENT);
                }
            }
            contextMap.put(id, currContext);
            instance.addContext(currContext);
        }
    }

    /**
	 * Sets facts of the instance.
	 * 
	 * @throws InstanceException
	 */
    private void setFactsAndTuples() throws InstanceException {
        List<Element> factElementList = xmlInstance.getRootElement().getChildren();
        Iterator<Element> factElementListIterator = factElementList.iterator();
        while (factElementListIterator.hasNext()) {
            Element currFactElement = factElementListIterator.next();
            if (!currFactElement.getName().equals("context") && !currFactElement.getName().equals("schemaRef") && !currFactElement.getName().equals("unit")) {
                TaxonomySchema schema = instance.getSchemaForURI(currFactElement.getNamespace());
                Concept currFactConceptElement = schema.getConceptByName(currFactElement.getName());
                if ("xbrli:tuple".contains(currFactConceptElement.getSubstitutionGroup())) {
                    Tuple newTuple = createTuple(currFactElement, currFactConceptElement);
                    instance.addTuple(newTuple);
                    continue;
                } else if ("xbrli:item".contains(currFactConceptElement.getSubstitutionGroup())) {
                    Fact newFact = createFact(currFactElement, currFactConceptElement);
                    instance.addFact(newFact);
                }
            }
        }
    }

    private Fact createFact(Element element, Concept concept) throws InstanceException {
        if (concept == null) {
            throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_FACT + element.getName());
        }
        Fact newFact = new Fact(concept);
        String contextID = element.getAttributeValue("contextRef");
        InstanceContext ctx = contextMap.get(contextID);
        if (ctx == null) {
            throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_NO_CONTEXT + element.getName());
        }
        newFact.setInstanceContext(ctx);
        if (element.getAttributeValue("id") != null) {
            newFact.setID(element.getAttributeValue("id"));
        }
        if (concept.isNumericItem()) {
            String unitID = element.getAttributeValue("unitRef");
            InstanceUnit unit = unitMap.get(unitID);
            newFact.setInstanceUnit(unit);
            if (element.getAttributeValue("decimals") != null) {
                newFact.setDecimals(element.getAttributeValue("decimals"));
            }
            if (element.getAttributeValue("precision") != null) {
                newFact.setPrecision(element.getAttributeValue("precision"));
            }
        }
        if (element.getContentSize() == 0) {
            newFact.setValue(null);
        } else {
            newFact.setValue(element.getValue());
        }
        return newFact;
    }

    private Tuple createTuple(Element element, Concept concept) throws InstanceException {
        if (concept == null) {
            throw new InstanceException(ExceptionConstants.EX_INSTANCE_CREATION_FACT + element.getName());
        }
        Tuple newTuple = new Tuple(concept);
        if (element.getAttributeValue("id") != null) {
            newTuple.setID(element.getAttributeValue("id"));
        }
        List<Element> childElementList = element.getChildren();
        Iterator<Element> childElementListIterator = childElementList.iterator();
        while (childElementListIterator.hasNext()) {
            Element currElement = childElementListIterator.next();
            TaxonomySchema schema = instance.getSchemaForURI(currElement.getNamespace());
            Concept currConcept = schema.getConceptByName(currElement.getName());
            if ("xbrli:tuple".contains(currConcept.getSubstitutionGroup())) {
                Tuple newChildTuple = createTuple(currElement, currConcept);
                newTuple.getTupleSet().add(newChildTuple);
            } else if ("xbrli:item".contains(currConcept.getSubstitutionGroup())) {
                Fact newFact = createFact(currElement, currConcept);
                newTuple.getFactSet().add(newFact);
            }
        }
        return newTuple;
    }

    public FileLoader getFileLoader() {
        return fileLoader;
    }

    public void setFileLoader(FileLoader fileLoader) {
        this.fileLoader = fileLoader;
    }
}
