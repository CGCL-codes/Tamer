package cz.cuni.mff.ksi.jinfer.basicxsd.elementsexporters;

import cz.cuni.mff.ksi.jinfer.basicxsd.preprocessing.PreprocessingResult;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Attribute;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.IGGUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicxsd.Indentator;
import cz.cuni.mff.ksi.jinfer.basicxsd.InterruptChecker;
import cz.cuni.mff.ksi.jinfer.basicxsd.properties.XSDExportPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.RegexpTypeUtils;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.TypeCategory;
import cz.cuni.mff.ksi.jinfer.basicxsd.utils.TypeUtils;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Basic implementation of elements exporter. Specific exporters should be derived
 * from this class and can use already implemented logic or implement it on their own.
 * @author rio
 */
public abstract class AbstractElementsExporter {

    protected static final Logger LOG = Logger.getLogger(AbstractElementsExporter.class);

    protected final PreprocessingResult preprocessingResult;

    protected final Indentator indentator;

    protected final String typenamePrefix;

    protected final String typenamePostfix;

    private int keyNumber = 1;

    private int foreignKeyNumber = 1;

    private final Map<Key, String> keyNames = new HashMap<Key, String>();

    private static final String XQUERY_PROCESSOR_METADATA_TYPE = "xquery_processor_type";

    private static final String XQUERY_PROCESSOR_METADATA_KEYS = "xquery_processor_keys";

    private static final String XQUERY_PROCESSOR_METADATA_FOREIGN_KEYS = "xquery_processor_foreign_keys";

    /**
   * Constructor.
   * @param preprocessingResult Result of preprocessing of input grammar.
   * @param indentator Instance of {@see Indentator} to be used to indent output.
   */
    public AbstractElementsExporter(final PreprocessingResult preprocessingResult, final Indentator indentator) {
        this.preprocessingResult = preprocessingResult;
        this.indentator = indentator;
        final Properties properties = RunningProject.getActiveProjectProps(XSDExportPropertiesPanel.NAME);
        typenamePrefix = properties.getProperty(XSDExportPropertiesPanel.TYPENAME_PREFIX_PROP, XSDExportPropertiesPanel.TYPENAME_PREFIX_DEFAULT);
        typenamePostfix = properties.getProperty(XSDExportPropertiesPanel.TYPENAME_POSTFIX_PROP, XSDExportPropertiesPanel.TYPENAME_POSTFIX_DEFAULT);
    }

    /**
   * Performs export. Has to be implemented in derived exporters.
   * @throws InterruptedException
   */
    public abstract void run() throws InterruptedException;

    /**
   * Processes element. If its type is defined globally simply uses it. Otherwise
   * defines it inline.
   *
   * @param element
   * @param interval
   * @throws InterruptedException
   */
    protected void processElement(final Element element, final RegexpInterval interval) throws InterruptedException {
        InterruptChecker.checkInterrupt();
        indentator.indent("<xs:element name=\"");
        indentator.append(element.getName());
        indentator.append("\"");
        if (TypeUtils.isOfBuiltinType(element)) {
            final String type = TypeUtils.getBuiltinType(element);
            final XSDBuiltinAtomicType xqueryType = (XSDBuiltinAtomicType) element.getMetadata().get(XQUERY_PROCESSOR_METADATA_TYPE);
            if (xqueryType == null) {
                indentator.append(" type=\"" + type + '"');
            } else {
                LOG.info("Built-in type of element \"" + element.getName() + "\" inferred by the simplifier: " + type);
                LOG.info("Built-in type of element \"" + element.getName() + "\" inferred by the xquery analyzer: xs:" + xqueryType);
                indentator.append(" type=\"xs:" + xqueryType + '"');
            }
            indentator.append(OccurencesProcessor.processOccurrences(interval));
            indentator.append("/>\n");
            return;
        }
        if (preprocessingResult.isElementGlobal(element.getName())) {
            indentator.append(" type=\"");
            indentator.append(typenamePrefix);
            indentator.append(element.getName());
            indentator.append(typenamePostfix);
            indentator.append("\"");
            indentator.append(OccurencesProcessor.processOccurrences(interval));
            if (hasElementKeys(element)) {
                indentator.append(">\n");
                indentator.increaseIndentation();
                processElementKeys(element);
                processElementForeignKeys(element);
                indentator.decreaseIndentation();
                indentator.indent("</xs:element>\n");
            } else {
                indentator.append("/>\n");
            }
            return;
        }
        indentator.append(OccurencesProcessor.processOccurrences(interval));
        indentator.append(">\n");
        indentator.increaseIndentation();
        final TypeCategory typeCategory = TypeUtils.getTypeCategory(element);
        switch(typeCategory) {
            case SIMPLE:
                indentator.indent("<xs:simpleType>\n");
                break;
            case COMPLEX:
                indentator.indent("<xs:complexType");
                if (TypeUtils.hasMixedContent(element)) {
                    indentator.append(" mixed=\"true\"");
                }
                indentator.append(">\n");
                break;
            default:
                throw new IllegalStateException("Unknown or illegal enum member.");
        }
        indentator.increaseIndentation();
        processElementContent(element);
        indentator.decreaseIndentation();
        switch(typeCategory) {
            case SIMPLE:
                indentator.indent("</xs:simpleType>\n");
                break;
            case COMPLEX:
                indentator.indent("</xs:complexType>\n");
                break;
            default:
                throw new IllegalStateException("Unknown or illegal enum member.");
        }
        processElementKeys(element);
        processElementForeignKeys(element);
        indentator.decreaseIndentation();
        indentator.indent("</xs:element>\n");
    }

    protected void processElementContent(final Element element) throws InterruptedException {
        if (element.getSubnodes().isToken() && element.getSubnodes().getContent().isElement()) {
            indentator.indent("<xs:sequence>\n");
            indentator.increaseIndentation();
            processSubElements(element.getSubnodes());
            indentator.decreaseIndentation();
            indentator.indent("</xs:sequence>\n");
        } else {
            processSubElements(element.getSubnodes());
        }
        processElementAttributes(element);
    }

    protected boolean hasElementKeys(final Element element) {
        final List<Key> keys = (List<Key>) element.getMetadata().get(XQUERY_PROCESSOR_METADATA_KEYS);
        if (BaseUtils.isEmpty(keys)) {
            return false;
        } else {
            return true;
        }
    }

    protected void processElementKeys(final Element element) {
        final List<Key> keys = (List<Key>) element.getMetadata().get(XQUERY_PROCESSOR_METADATA_KEYS);
        if (keys == null) {
            return;
        }
        for (final Key key : keys) {
            final NormalizedPathType targetPath = key.getTargetPath();
            final NormalizedPathType keyPath = key.getKeyPath();
            keyNames.put(key, "key" + keyNumber);
            indentator.indent("<xs:key name=\"key" + keyNumber + "\">\n");
            ++keyNumber;
            indentator.increaseIndentation();
            indentator.indent("<xs:selector xpath=\"" + targetPath.toString() + "\"/>\n");
            indentator.indent("<xs:field xpath=\"" + keyPath.toString() + "\"/>\n");
            indentator.decreaseIndentation();
            indentator.indent("</xs:key>\n");
        }
    }

    protected void processElementForeignKeys(final Element element) {
        final Set<ForeignKey> fKeys = (Set<ForeignKey>) element.getMetadata().get(XQUERY_PROCESSOR_METADATA_FOREIGN_KEYS);
        if (fKeys == null) {
            return;
        }
        for (final ForeignKey fKey : fKeys) {
            final NormalizedPathType targetPath = fKey.getForeignTargetPath();
            final NormalizedPathType keyPath = fKey.getForeignKeyPath();
            final String keyName = keyNames.get(fKey.getKey());
            assert (keyName != null);
            indentator.indent("<xs:keyref name=\"" + "keyRef" + foreignKeyNumber + '_' + keyName + "\" refer=\"" + keyName + "\">\n");
            ++foreignKeyNumber;
            indentator.increaseIndentation();
            indentator.indent("<xs:selector xpath=\"" + targetPath.toString() + "\"/>\n");
            indentator.indent("<xs:field xpath=\"" + keyPath.toString() + "\"/>\n");
            indentator.decreaseIndentation();
            indentator.indent("</xs:keyref>\n");
        }
    }

    private void processElementAttributes(final Element element) throws InterruptedException {
        final List<Attribute> attributes = element.getAttributes();
        if (!attributes.isEmpty()) {
            assert TypeUtils.isOfComplexType(element);
            for (Attribute attribute : attributes) {
                InterruptChecker.checkInterrupt();
                indentator.indent("<xs:attribute name=\"");
                indentator.append(attribute.getName());
                final String type = TypeUtils.getBuiltinAttributeType(attribute);
                final XSDBuiltinAtomicType xqueryType = (XSDBuiltinAtomicType) attribute.getMetadata().get(XQUERY_PROCESSOR_METADATA_TYPE);
                if (xqueryType == null) {
                    indentator.append("\" type=\"" + type + '"');
                } else {
                    LOG.info("Built-in type of attribute \"" + attribute.getName() + "\" inferred by the simplifier: " + type);
                    LOG.info("Built-in type of attribute \"" + attribute.getName() + "\" inferred by the xquery analyzer: xs:" + xqueryType);
                    indentator.append("\" type=\"xs:" + xqueryType + '"');
                }
                if (attribute.getMetadata().containsKey(IGGUtils.REQUIRED)) {
                    indentator.append(" use=\"required\"");
                }
                indentator.append("/>\n");
            }
        }
    }

    private void processLambdaNodeAlternation(final Regexp<AbstractStructuralNode> simpleAlternation) throws InterruptedException {
        if (simpleAlternation.getChild(1).isToken()) {
            processSubElements(simpleAlternation);
        } else if (simpleAlternation.getChild(1).isConcatenation()) {
            indentator.indent("<xs:sequence minOccurs=\"0\">\n");
            indentator.increaseIndentation();
            for (final Regexp<AbstractStructuralNode> subReg : simpleAlternation.getChild(1).getChildren()) {
                processSubElements(subReg);
            }
            indentator.decreaseIndentation();
            indentator.indent("</xs:sequence>\n");
        } else {
            indentator.indent("<xs:sequence minOccurs=\"0\">\n");
            indentator.increaseIndentation();
            processSubElements(simpleAlternation.getChild(1));
            indentator.decreaseIndentation();
            indentator.indent("</xs:sequence>\n");
        }
    }

    private void processConcatenation(final Regexp<AbstractStructuralNode> concatenation) throws InterruptedException {
        indentator.indent("<xs:sequence");
        indentator.append(OccurencesProcessor.processOccurrences(concatenation.getInterval()));
        indentator.append(">\n");
        indentator.increaseIndentation();
        for (final Regexp<AbstractStructuralNode> subRegexp : concatenation.getChildren()) {
            if (RegexpTypeUtils.isLambdaNodeAlternation(subRegexp)) {
                processLambdaNodeAlternation(subRegexp);
            } else {
                processSubElements(subRegexp);
            }
        }
        indentator.decreaseIndentation();
        indentator.indent("</xs:sequence>\n");
    }

    private void processAlternation(final Regexp<AbstractStructuralNode> alternation) throws InterruptedException {
        if (RegexpTypeUtils.isLambdaTokenAlternation(alternation)) {
            processToken(alternation.getChild(1).getContent(), RegexpInterval.getBounded(0, 1));
        } else if (RegexpTypeUtils.isSimpleDataTokenAlternation(alternation)) {
            indentator.indent("<xs:sequence>\n");
            indentator.increaseIndentation();
            processToken(alternation.getChild(1).getContent(), RegexpInterval.getBounded(0, 1));
            indentator.decreaseIndentation();
            indentator.indent("</xs:sequence>\n");
        } else {
            indentator.indent("<xs:choice");
            indentator.append(OccurencesProcessor.processOccurrences(alternation.getInterval()));
            indentator.append(">\n");
            indentator.increaseIndentation();
            for (Regexp<AbstractStructuralNode> subRegexp : alternation.getChildren()) {
                if ((subRegexp != null) && (!subRegexp.isLambda())) {
                    processSubElements(subRegexp);
                }
            }
            indentator.decreaseIndentation();
            indentator.indent("</xs:choice>\n");
        }
    }

    /**
   * Given one node from result of getSubnodes method call. We are interested
   * only in elements, so if the node does not contain any elements, function
   * terminates.
   * This filtering also prevents generation of empty xs elements, which may
   * occurs for example if all TOKENs are SIMPLE_DATA.
   *
   * @param regexp Node to process its sub elements.
   * @throws InterruptedException When inference was interrupted.
   */
    private void processSubElements(final Regexp<AbstractStructuralNode> regexp) throws InterruptedException {
        InterruptChecker.checkInterrupt();
        if (regexp.isLambda()) {
            return;
        }
        if (BaseUtils.filter(regexp.getTokens(), new BaseUtils.Predicate<AbstractStructuralNode>() {

            @Override
            public boolean apply(final AbstractStructuralNode argument) {
                return argument.isElement();
            }
        }).isEmpty()) {
            return;
        }
        switch(regexp.getType()) {
            case TOKEN:
                processToken(regexp.getContent(), regexp.getInterval());
                break;
            case CONCATENATION:
                processConcatenation(regexp);
                break;
            case ALTERNATION:
                processAlternation(regexp);
                break;
            default:
                throw new IllegalArgumentException("Unknown enum member.");
        }
    }

    private void processToken(final AbstractStructuralNode node, final RegexpInterval interval) throws InterruptedException {
        assert !node.isSimpleData();
        assert node.isElement();
        final Element element = preprocessingResult.getElementByName(node.getName());
        if (element == null) {
            LOG.warn("XSD Exporter: Referenced element(" + node.getName() + ") not found in IG element list, probably error in code");
            return;
        }
        processElement(element, interval);
    }
}
