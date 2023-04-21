package org.vikamine.app.persistence;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import javax.swing.tree.TreePath;
import org.vikamine.app.DMManager;
import org.vikamine.app.event.OntologyModifiedEvent;
import org.vikamine.kernel.data.Attribute;
import org.vikamine.kernel.data.AttributeProvider;
import org.vikamine.kernel.data.DerivedAttribute;
import org.vikamine.kernel.data.DerivedAttributeBuilder;
import org.vikamine.kernel.data.DerivedNominalAttribute;
import org.vikamine.kernel.data.DerivedNominalValue;
import org.vikamine.kernel.data.Ontology;
import org.vikamine.kernel.data.RDFStatement;
import org.vikamine.kernel.data.RDFTripleStore;
import org.vikamine.kernel.data.Value;
import org.vikamine.kernel.formula.FormulaBooleanElement;
import org.vikamine.kernel.formula.FormulaElement;
import org.vikamine.kernel.formula.FormulaNumberElement;
import org.vikamine.kernel.persistence.DomUtils;
import org.vikamine.kernel.persistence.ValuesMarshaller;
import org.vikamine.kernel.persistence.XMLPersistenceReader;
import org.vikamine.kernel.persistence.formula.FormulaUnmarshaller;
import org.vikamine.kernel.subgroup.SConstraints;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.quality.SGQualityFunction;
import org.vikamine.kernel.subgroup.selectors.SGNominalSelector;
import org.vikamine.swing.VikamineFileOpenHandler;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;
import org.vikamine.swing.subgroup.MetaInformationController;
import org.vikamine.swing.subgroup.editors.zoomtable.AttributeNode;
import org.vikamine.swing.subgroup.editors.zoomtable.CommonData;
import org.vikamine.swing.subgroup.editors.zoomtable.CommonZoomTablesController;
import org.vikamine.swing.subgroup.editors.zoomtable.CommonZoomTreesModel;
import org.vikamine.swing.subgroup.editors.zoomtable.ZoomSession;
import org.vikamine.swing.subgroup.editors.zoomtable.update.AttributeValuesComputer;
import org.vikamine.swing.util.filters.AttributeFilter;
import org.vikamine.swing.util.filters.Filterable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Tobias Vogele, atzmueller
 */
public class VikamineStateLoader extends XMLPersistenceReader {

    public static class CustomAttributeProvider implements AttributeProvider {

        Map customFeatures;

        public CustomAttributeProvider(Map customFeatures) {
            this.customFeatures = customFeatures;
        }

        public DerivedAttribute getBasicAttribute(String name) {
            AttributeNode att = (AttributeNode) customFeatures.get(name);
            if (att != null) {
                return (DerivedAttribute) att.getAttribute();
            } else {
                return (DerivedAttribute) DMManager.getInstance().getOntology().getAttribute(name);
            }
        }

        public Attribute getAttribute(String name) {
            AttributeNode att = (AttributeNode) customFeatures.get(name);
            if (att != null) {
                return att.getAttribute();
            } else {
                return DMManager.getInstance().getOntology().getAttribute(name);
            }
        }

        public int getNumAttributes() {
            return customFeatures.size();
        }
    }

    static interface Uncomplete {

        void complete(Map customFeatures);
    }

    static class UncompleteAttributeComputer implements Uncomplete {

        private final AttributeNode attnode;

        private final CommonData data;

        public UncompleteAttributeComputer(AttributeNode attnode, CommonData data) {
            this.attnode = attnode;
            this.data = data;
        }

        public void complete(Map customFeatures) {
            attnode.setValuesComputer(new AttributeValuesComputer(data));
        }
    }

    static class UncompleteNumericFormula implements Uncomplete {

        DerivedAttribute att;

        Element formula;

        public UncompleteNumericFormula(DerivedAttribute att, Element formula) {
            this.att = att;
            this.formula = formula;
        }

        public void complete(Map customFeatures) {
            FormulaElement form = new FormulaUnmarshaller(DMManager.getInstance().getOntology()).unmarshal(formula, new CustomAttributeProvider(customFeatures));
            att.setFormula((FormulaNumberElement) form);
        }
    }

    static class UncompleteNominalAlternative implements Uncomplete {

        DerivedNominalValue alternative;

        Element formula;

        public UncompleteNominalAlternative(DerivedNominalValue alternative, Element formula) {
            this.alternative = alternative;
            this.formula = formula;
        }

        public void complete(Map customFeatures) {
            FormulaElement form = new FormulaUnmarshaller(DMManager.getInstance().getOntology()).unmarshal(this.formula, new CustomAttributeProvider(customFeatures));
            alternative.setCondition((FormulaBooleanElement) form);
        }
    }

    static class UncompleteCache implements Uncomplete {

        DerivedAttribute att;

        public UncompleteCache(DerivedAttribute att) {
            this.att = att;
        }

        public void complete(Map customFeatures) {
            att.createCache(DMManager.getInstance().getOntology().getPopulation().instanceIterator());
        }
    }

    static class UncompleteWekaAttribute implements Uncomplete {

        DerivedAttribute att;

        String wekaAttribute;

        public UncompleteWekaAttribute(DerivedAttribute att, String wekaAttribute) {
            this.att = att;
            this.wekaAttribute = wekaAttribute;
        }

        public void complete(Map customFeatures) {
            Attribute wekaAtt = new CustomAttributeProvider(customFeatures).getAttribute(wekaAttribute);
        }
    }

    public VikamineStateLoader() {
        super(DMManager.getInstance().getOntology());
    }

    @Override
    public void processDocument(Document doc) throws IOException {
        parseDom(doc);
    }

    private void parseDom(Document doc) throws IOException {
        Map customAttributes = new HashMap();
        Element root = doc.getDocumentElement();
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element) {
                String name = node.getNodeName();
                if ("dataSets".equals(name)) {
                    parseDataSets(node);
                } else if ("metaInformation".equals(name)) {
                    parseMetaInformation(node);
                } else if ("customFeatures".equals(name)) {
                    parseCustomFeatures(node, customAttributes);
                } else if ("population".equals(name)) {
                    parsePopulation(node, customAttributes);
                } else if ("constraints".equals(name)) {
                    parseConstraints(node);
                } else if ("globalSettings".equals(name)) {
                    parseGlobalSettings(node);
                } else if ("interestingSGs".equals(name)) {
                    parseInterestingSubgroups(node, customAttributes);
                } else if ("backgroundKnowledgeSGs".equals(name)) {
                    parseBackgroundKnowledgeSubgroups(node, customAttributes);
                } else if ("currentSG".equals(name)) {
                    parseCurrentSG(node, customAttributes);
                } else if ("zoomAttributes".equals(name)) {
                    AllSubgroupPluginController.getInstance().getZoomController().clearZoomTreeModels();
                    parseZoomAttributes(node, customAttributes);
                } else if ("zoomFilters".equals(name)) {
                    parseZoomFilters(node);
                } else if ("zoomSessions".equals(name)) {
                    parseZoomSessions(node);
                }
            }
        }
    }

    private void parseDataSets(Node node) {
        Node child = node.getFirstChild();
        if (child != null) {
            String content = child.getTextContent();
            File source = new File(content);
            new VikamineFileOpenHandler().handleOpenFile(source);
            setOntology(DMManager.getInstance().getOntology());
            RDFTripleStore store = DMManager.getInstance().getOntology().getTripleStore();
            RDFStatement statement = new RDFStatement(DMManager.getInstance().getOntology(), "hasFileSource", source);
            store.addStatement(statement);
        }
    }

    private void parseMetaInformation(Node node) {
        MetaInformationController metaController = AllSubgroupPluginController.getInstance().getMetaInformationController();
        metaController.reset();
        if (node.getFirstChild() != null) {
            String metaInfo = node.getFirstChild().getTextContent();
            if (metaInfo != null) {
                metaController.setMetaInfo(metaInfo);
            }
        }
    }

    private void parseZoomSessions(Node node) {
        NodeList children = node.getChildNodes();
        AllSubgroupPluginController.getInstance().getZoomController().getStoredSessions().clear();
        for (int i = 0; i < children.getLength(); i++) {
            Node element = children.item(i);
            if (element.getNodeName().equals("singleZoomSession") && element instanceof Element) {
                ZoomSession session = parseSingleZoomSession(element);
                AllSubgroupPluginController.getInstance().getZoomController().getStoredSessions().add(session);
            } else {
                System.out.println("Unknown node: " + element.getNodeName());
            }
        }
    }

    private ZoomSession parseSingleZoomSession(Node zoomSessionNode) {
        String sessionName = ((Element) zoomSessionNode).getAttribute("name");
        List sessionTreeNodes = new LinkedList();
        List confoundingFactors = new LinkedList();
        NodeList zoomSessionNodeChildren = zoomSessionNode.getChildNodes();
        for (int i = 0; i < zoomSessionNodeChildren.getLength(); i++) {
            Node child = zoomSessionNodeChildren.item(i);
            if (child.getNodeName().equals("treeNodes")) {
                NodeList treeNodes = child.getChildNodes();
                for (int j = 0; j < treeNodes.getLength(); j++) {
                    Node treeNode = treeNodes.item(j);
                    if (treeNode.getNodeName().equals("singleTreeNode") && treeNode instanceof Element) {
                        Element treeNodeElement = (Element) treeNode;
                        String id = treeNodeElement.getAttribute("id");
                        String expandedOverview = treeNodeElement.getAttribute("expandedOverview");
                        String expandedSorted = treeNodeElement.getAttribute("expandedSorted");
                        sessionTreeNodes.add(new ZoomSession.StoredTreeNode(id, expandedOverview.equals("1"), expandedSorted.equals("1")));
                    } else {
                        System.out.println("Unknown node: " + treeNode.getNodeName());
                    }
                }
            } else if (child.getNodeName().equals("confoundingAttributes")) {
                NodeList confoundingAttributeList = child.getChildNodes();
                for (int j = 0; j < confoundingAttributeList.getLength(); j++) {
                    Node cfAttrNode = confoundingAttributeList.item(j);
                    if (cfAttrNode.getNodeName().equals("singleCFattribute") && cfAttrNode instanceof Element) {
                        String name = ((Element) cfAttrNode).getAttribute("name");
                        Attribute attr = DMManager.getInstance().getOntology().getAttribute(name);
                        confoundingFactors.add(attr);
                    } else {
                        System.out.println("Unknown node: " + cfAttrNode.getNodeName());
                    }
                }
            } else {
                System.out.println("Unknown node: " + child.getNodeName());
            }
        }
        return new ZoomSession(sessionName, sessionTreeNodes, confoundingFactors);
    }

    private void parseCurrentSG(Node node, Map customAttributes) throws IOException {
        NodeList children = node.getChildNodes();
        List sgNodes = new LinkedList();
        for (int i = 0; i < children.getLength(); i++) {
            Node element = children.item(i);
            if (element.getNodeName().equals("sg")) {
                sgNodes.add(element);
            }
        }
        if (sgNodes.size() > 1) {
            throw new IllegalStateException("More than one currentSG saved!");
        } else {
            if (sgNodes.size() == 1) {
                SG sg = parseSGNode((Element) sgNodes.get(0), new CustomAttributeProvider(customAttributes));
                AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().setSubgroup(sg);
            }
        }
    }

    private void parseInterestingSubgroups(Node parent, Map customAttributes) throws IOException {
        List subgroups = parseSubgroups(parent, new CustomAttributeProvider(customAttributes));
        for (Iterator iter = subgroups.iterator(); iter.hasNext(); ) {
            SG sg = (SG) iter.next();
            DMManager.getInstance().addSubgroup(sg);
        }
    }

    private void parseBackgroundKnowledgeSubgroups(Node parent, Map customAttributes) throws IOException {
        List subgroups = parseSubgroups(parent, new CustomAttributeProvider(customAttributes));
        DMManager.getInstance().setBackgroundKnowledgeSubgroup(subgroups);
    }

    /**
	 * @param node
	 */
    private void parseGlobalSettings(Node node) {
        if (node instanceof Element) {
            Element globalSetting = (Element) node;
            String type = globalSetting.getAttribute("type");
            if (type.equals("sgMinQualityLimit")) {
                String value = globalSetting.getAttribute("value");
                AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setSGMinQualityLimit(Double.parseDouble(value));
            } else if (type.equals("useSGMinQualityLimit")) {
                String value = globalSetting.getAttribute("value");
                AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setUseSGMinQualityLimit(Boolean.valueOf(value).booleanValue());
            } else if (type.equals("minSupport")) {
                String value = globalSetting.getAttribute("value");
                AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setMinSupport(Integer.parseInt(value));
            } else if (type.equals("maxSGCount")) {
                String value = globalSetting.getAttribute("value");
                AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setMaxSGCount(Integer.parseInt(value));
            } else if (type.equals("maxSGDSizeLimit")) {
                String value = globalSetting.getAttribute("value");
                AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setMaxSGDSizeLimit(Integer.parseInt(value));
            } else if (type.equals("useMaxSGDSizeLimit")) {
                String value = globalSetting.getAttribute("value");
                AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setUseMaxSGDSizeLimit(Boolean.valueOf(value).booleanValue());
            }
        }
    }

    private void parseCustomFeatures(Node parent, Map customAttributes) throws IOException {
        List uncomplete = scanCustomFeatures(parent, customAttributes);
        fillCustomFeatures(uncomplete, customAttributes);
    }

    private List scanCustomFeatures(Node parent, Map customAttributes) throws IOException {
        List uncomplete = new LinkedList();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                if (!"attribute".equals(child.getNodeName())) {
                    throw new IOException("Wrong element: " + child.getNodeName() + " (expected: 'attribute')");
                }
                String type = child.getAttributes().getNamedItem("type").getNodeValue();
                if (!"customFeature".equals(type)) {
                    throw new IOException("Wrong type: " + type + " (expected: 'customFeature')");
                }
                AttributeNode att = parseCustomFeature(child, uncomplete);
                assert (att != null);
                customAttributes.put(att.getAttribute().getId(), att);
            }
        }
        return uncomplete;
    }

    private AttributeNode parseCustomFeature(Node node, List uncomplete) {
        Element elem = (Element) node;
        String id = elem.getAttributes().getNamedItem("id").getNodeValue();
        String attributeDescription = elem.getAttributes().getNamedItem("description").getNodeValue();
        if (attributeDescription == null) attributeDescription = id;
        Node first = elem.getFirstChild();
        DerivedAttribute att = null;
        DerivedAttributeBuilder attBuilder = new DerivedAttributeBuilder();
        if (first != null && first instanceof Element && "formula".equals(first.getNodeName())) {
            Element formulaNode = (Element) first;
            attBuilder.buildNumericCustomAttribute(id);
            att = attBuilder.getAttribute();
            uncomplete.add(new UncompleteNumericFormula(att, formulaNode));
        } else {
            List uncompleteNominalValues = new LinkedList();
            List values = new ArrayList();
            NodeList valueElements = elem.getChildNodes();
            if (valueElements.getLength() > 0) {
                for (int i = 0; i < valueElements.getLength(); i++) {
                    if (valueElements.item(i) instanceof Element) {
                        Element child = (Element) valueElements.item(i);
                        try {
                            Value value = new ValuesMarshaller().parseValueNode(getOntology(), getOntology(), child, uncompleteNominalValues);
                            values.add(value);
                        } catch (ParseException ex) {
                            Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "parseCustomFeature", ex);
                            throw new IllegalStateException("Problem in parsing the values ...");
                        }
                    }
                }
                for (Iterator iter = uncompleteNominalValues.iterator(); iter.hasNext(); ) {
                    Value.UncompleteNominalValue uncompleteNominalValue = (Value.UncompleteNominalValue) iter.next();
                    uncomplete.add(new UncompleteNominalAlternative(uncompleteNominalValue.getValue(), uncompleteNominalValue.getCondition()));
                }
                attBuilder.buildNominalCustomAttribute(id, values);
                att = attBuilder.getAttribute();
                for (Iterator iter = values.iterator(); iter.hasNext(); ) {
                    Value val = (Value) iter.next();
                    if (val instanceof Value.CustomDiscretizedValue) {
                        ((Value.CustomDiscretizedValue) val).setAttribute(att);
                    } else if (val instanceof DerivedNominalValue) {
                        ((DerivedNominalValue) val).setAttribute(att);
                    }
                }
            } else {
                IllegalStateException ex = new IllegalStateException("No value elements in element " + elem);
                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "parseCustomFeature", ex);
                throw ex;
            }
        }
        uncomplete.add(new UncompleteWekaAttribute(att, null));
        AttributeNode attnode = new AttributeNode(id, att);
        CommonZoomTreesModel tree = AllSubgroupPluginController.getInstance().getZoomController().getActiveZoomTreeModel();
        uncomplete.add(new UncompleteCache(att));
        if (attnode.getValuesComputer() == null) {
            uncomplete.add(new UncompleteAttributeComputer(attnode, tree.getData()));
        }
        Ontology onto = getOntology();
        onto.addUserDefinedAttribute(att);
        DMManager.getInstance().fireOntologyModifiedEvent(new OntologyModifiedEvent(this, onto));
        return attnode;
    }

    private void fillCustomFeatures(List uncomplete, Map customFeatures) {
        for (Iterator iter = uncomplete.iterator(); iter.hasNext(); ) {
            Uncomplete unc = (Uncomplete) iter.next();
            if (!(unc instanceof UncompleteCache) && !(unc instanceof UncompleteAttributeComputer)) {
                unc.complete(customFeatures);
            }
        }
        for (Iterator iter = uncomplete.iterator(); iter.hasNext(); ) {
            Uncomplete unc = (Uncomplete) iter.next();
            if (unc instanceof UncompleteAttributeComputer) {
                unc.complete(customFeatures);
            }
        }
        for (Iterator iter = uncomplete.iterator(); iter.hasNext(); ) {
            Uncomplete unc = (Uncomplete) iter.next();
            if (unc instanceof UncompleteCache) {
                unc.complete(customFeatures);
            }
        }
    }

    @Override
    protected SConstraints parseConstraints(Node node) throws IOException {
        SConstraints sConstraints = super.parseConstraints(node);
        AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().setSConstraints(sConstraints);
        return sConstraints;
    }

    private void parsePopulation(Node node, Map customAttributes) {
        List selectors = parseSelectorList(node, new CustomAttributeProvider(customAttributes));
        List old = new LinkedList(DMManager.getInstance().getPopulationRangesSelectors());
        for (Iterator iter = old.iterator(); iter.hasNext(); ) {
            SGNominalSelector oldSelect = (SGNominalSelector) iter.next();
            DMManager.getInstance().getPopulationRangesSelectors().remove(oldSelect);
            DMManager.getInstance().refineInstancesPopulation();
        }
        for (Iterator iter = selectors.iterator(); iter.hasNext(); ) {
            SGNominalSelector newSelect = (SGNominalSelector) iter.next();
            DMManager.getInstance().getPopulationRangesSelectors().add(newSelect);
            DMManager.getInstance().refineInstancesPopulation();
        }
    }

    private void parseZoomFilters(Node node) throws IOException {
        Filterable overviewFilterable = AllSubgroupPluginController.getInstance().getZoomController().getOverviewZoomTreeModel();
        Filterable sortedFilterable = AllSubgroupPluginController.getInstance().getZoomController().getSortedZoomTreeModel();
        List overviewFilters = AllSubgroupPluginController.getInstance().getZoomController().getOverviewZoomController().getFilterAction().getFilters();
        List sortedFilters = AllSubgroupPluginController.getInstance().getZoomController().getSortedZoomController().getFilterAction().getFilters();
        for (Iterator iter = overviewFilters.iterator(); iter.hasNext(); ) {
            AttributeFilter phil = (AttributeFilter) iter.next();
            overviewFilterable.removeFilter(phil);
        }
        for (Iterator iter = sortedFilters.iterator(); iter.hasNext(); ) {
            AttributeFilter phil = (AttributeFilter) iter.next();
            sortedFilterable.removeFilter(phil);
        }
        List filterClassNames = parseFilterNodes(node);
        for (Iterator iter = filterClassNames.iterator(); iter.hasNext(); ) {
            String className = (String) iter.next();
            AttributeFilter filter = findFilter(className, overviewFilters);
            overviewFilterable.addFilter(filter);
            filter = findFilter(className, sortedFilters);
            sortedFilterable.addFilter(filter);
        }
    }

    private AttributeFilter findFilter(String className, List filters) {
        for (Iterator iterator = filters.iterator(); iterator.hasNext(); ) {
            AttributeFilter phil = (AttributeFilter) iterator.next();
            if (className.equals(phil.getClass().getName())) {
                return phil;
            }
        }
        Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "findFilter", new NoSuchElementException("No filter with classname '" + className + "'"));
        return null;
    }

    private List parseFilterNodes(Node node) throws IOException {
        List filters = new LinkedList();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                if (!"filter".equals(child.getNodeName())) {
                    throw new IOException("Wrong element: " + child.getNodeName() + " (expected: 'filter')");
                }
                Node value = ((Element) child).getFirstChild();
                if (value == null) {
                    throw new IOException("Missing child-node for filter-node");
                }
                filters.add(DomUtils.getNormalizedNodeValue(value));
            }
        }
        return filters;
    }

    public void parseZoomAttributes(Node parent, Map customAttributes) throws IOException {
        List attributeIds = parseZoomAttributesInternal(parent, customAttributes);
        List customZoomAtts = new LinkedList();
        List nonCustomAttributes = new LinkedList();
        for (Iterator iter = attributeIds.iterator(); iter.hasNext(); ) {
            String id = (String) iter.next();
            if (customAttributes.containsKey(id)) {
                customZoomAtts.add(id);
                iter.remove();
            } else {
                Attribute attrib = DMManager.getInstance().getOntology().getAttribute(id);
                if (attrib == null) {
                    Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "parseZoomAttributes", new IllegalStateException("DMAttribute for ID " + id + " does not exist!"));
                    iter.remove();
                } else nonCustomAttributes.add(attrib);
            }
        }
        if (!attributeIds.isEmpty()) {
            CommonZoomTablesController ctrl = AllSubgroupPluginController.getInstance().getZoomController();
            ctrl.setSorting(CommonZoomTreesModel.SORT_MANUAL);
            List uncompleteAdd = addNonCustomAttributes(nonCustomAttributes, ctrl);
            addZoomattribute(customAttributes, customZoomAtts, ctrl);
            addNonCustomAttributes(uncompleteAdd, ctrl);
            ctrl.refilter();
            AllSubgroupPluginController.getInstance().getZoomController().updateZoomTables();
        }
    }

    /**
	 * @param customAttributes
	 * @param customZoomAtts
	 * @param tree
	 */
    private void addZoomattribute(Map customAttributes, List customZoomAtts, CommonZoomTablesController ctrl) {
        for (Iterator iter = customZoomAtts.iterator(); iter.hasNext(); ) {
            String id = (String) iter.next();
            TreePath parentPath = new TreePath(ctrl.getActiveZoomTreeModel().getRoot());
            ctrl.add((AttributeNode) customAttributes.get(id), parentPath, 0, false);
        }
    }

    private List addNonCustomAttributes(List attributeList, CommonZoomTablesController ctrl) {
        List uncompleteAdd = new LinkedList();
        for (Iterator iter = attributeList.iterator(); iter.hasNext(); ) {
            Attribute att = (Attribute) iter.next();
            ctrl.add(att.getId(), false);
        }
        return uncompleteAdd;
    }

    public List parseZoomAttributesInternal(Node parent, Map customAttributes) throws IOException {
        List attributeIds = new LinkedList();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                if (!"attribute".equals(child.getNodeName())) {
                    throw new IOException("Wrong element: " + child.getNodeName() + " (expected: 'attribute')");
                }
                Node value = ((Element) child).getFirstChild();
                if (value == null) {
                    throw new IOException("Missing child-node for attribute-node");
                }
                String id = DomUtils.getNormalizedNodeValue(value);
                attributeIds.add(id);
                attributeIds.addAll(parseZoomAttributesInternal(child, customAttributes));
            }
        }
        return attributeIds;
    }

    @Override
    protected SG parseSGNode(Element parent, AttributeProvider attributeProvider) throws IOException {
        SG sg = super.parseSGNode(parent, attributeProvider);
        sg.createStatistics(AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getSConstraints());
        SGQualityFunction func = AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().createQualityFunctionForTarget(sg.getTarget());
        sg.updateQuality(func);
        return sg;
    }

    @SuppressWarnings("unused")
    private void parseUserDefinedAttributes(Node parent, List<AttributeNode> userDefAtts) throws IOException {
        List uncomplete = new LinkedList();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                if (!"Attribute".equals(child.getNodeName())) {
                    throw new IOException("Wrong element: " + child.getNodeName() + " (expected: 'Attribute')");
                }
                String type = child.getAttributes().getNamedItem("type").getNodeValue();
                if (!"userDef".equals(type)) {
                    throw new IOException("Wrong type: " + type + " (expected: 'userDef')");
                }
                AttributeNode att = parseCustomFeature(child, uncomplete);
                Attribute attrib = att.getAttribute();
                if (attrib instanceof DerivedNominalAttribute) {
                    Ontology ontology = DMManager.getInstance().getOntology();
                    DerivedNominalAttribute attribute = (DerivedNominalAttribute) attrib;
                    attribute.createCache(ontology.getPopulation().instanceIterator());
                    ontology.addUserDefinedAttribute(attribute);
                    DMManager.getInstance().fireOntologyModifiedEvent(new OntologyModifiedEvent(this, ontology));
                } else {
                    throw new IOException("Expected an instance of DerivedNominalAttribute!");
                }
                userDefAtts.add(att);
            }
        }
    }
}
