package org.thymeleaf.processor.tag;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.thymeleaf.Arguments;
import org.thymeleaf.ParsedTemplate;
import org.thymeleaf.exceptions.AttrProcessorException;
import org.thymeleaf.templateresolver.TemplateResolution;
import org.thymeleaf.util.FragmentUtils;
import org.thymeleaf.util.Validate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public abstract class AbstractFragmentInclusionTagProcessor extends AbstractMarkupSubstitutionTagProcessor {

    public AbstractFragmentInclusionTagProcessor() {
        super();
    }

    @Override
    protected List<Node> getMarkupSubstitutes(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element) {
        final AbstractFragmentSpec fragmentSpec = getFragmentSpec(arguments, templateResolution, document, element);
        if (fragmentSpec == null) {
            throw new AttrProcessorException("Null value fragment specification (tag \"" + element.getTagName() + "\") not allowed");
        }
        final Node fragmentNode = getFragment(arguments, element, fragmentSpec);
        if (fragmentNode == null) {
            throw new AttrProcessorException("An error happened during parsing of include: \"" + element.getTagName() + "\": fragment node is null");
        }
        try {
            final Node node = document.importNode(fragmentNode, true);
            final List<Node> children = new ArrayList<Node>();
            final NodeList nodeList = node.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                children.add(nodeList.item(i));
            }
            return children;
        } catch (final Exception e) {
            throw new AttrProcessorException("An error happened during parsing of include: \"" + element.getTagName() + "\"", e);
        }
    }

    private static Node getFragment(final Arguments arguments, final Element element, final AbstractFragmentSpec fragmentSpec) {
        final String fragmentTemplateName = fragmentSpec.getFragmentTemplateName();
        if (arguments.getTemplateName().equals(fragmentTemplateName)) {
            throw new AttrProcessorException("Template \"" + arguments.getTemplateName() + " references itself from a " + "\"" + element.getTagName() + "\" tag, which is forbidden.");
        }
        try {
            final Arguments fragmentArguments = new Arguments(arguments.getConfiguration(), arguments.getTemplateParser(), fragmentTemplateName, arguments.getContext(), arguments.getLocalVariables(), arguments.getSelectionTarget(), arguments.getIdCounts(), arguments.getTextInliner());
            final ParsedTemplate parsedTemplate = arguments.getTemplateParser().parseDocument(fragmentArguments);
            Node fragmentNode = null;
            if (fragmentSpec instanceof NamedFragmentSpec) {
                final NamedFragmentSpec namedFragmentSpec = (NamedFragmentSpec) fragmentSpec;
                final String fragmentTagName = namedFragmentSpec.getFragmentTagName();
                final String fragmentAttributeName = namedFragmentSpec.getFragmentAttributeName();
                final String fragmentAttributeValue = namedFragmentSpec.getFragmentAttributeValue();
                fragmentNode = FragmentUtils.extractFragmentByAttributevalue(parsedTemplate.getDocument(), fragmentTagName, fragmentAttributeName, fragmentAttributeValue);
                if (fragmentNode == null) {
                    throw new AttrProcessorException("Fragment \"" + fragmentAttributeValue + "\" in template \"" + fragmentTemplateName + "\" could not be found");
                }
            } else if (fragmentSpec instanceof CompleteTemplateFragmentSpec) {
                fragmentNode = parsedTemplate.getDocument().getDocumentElement();
                if (fragmentNode == null) {
                    throw new AttrProcessorException("Root node in template \"" + fragmentTemplateName + "\" could not be found");
                }
            } else if (fragmentSpec instanceof XPathFragmentSpec) {
                final XPathFragmentSpec xpathFragmentSpec = (XPathFragmentSpec) fragmentSpec;
                final XPath xpath = XPathFactory.newInstance().newXPath();
                final XPathExpression xpathExpr = xpath.compile(xpathFragmentSpec.getXPathExpression());
                final NodeList exprResult = (NodeList) xpathExpr.evaluate(parsedTemplate.getDocument(), XPathConstants.NODESET);
                fragmentNode = exprResult.item(0);
                if (fragmentNode == null) {
                    throw new AttrProcessorException("No result for XPath expression \"" + xpathFragmentSpec.getXPathExpression() + "\" in template \"" + fragmentTemplateName + "\" could be found");
                }
            }
            return fragmentNode;
        } catch (final AttrProcessorException e) {
            throw e;
        } catch (final Exception e) {
            throw new AttrProcessorException("An error happened during parsing of template: \"" + fragmentTemplateName + "\"", e);
        }
    }

    protected abstract AbstractFragmentSpec getFragmentSpec(final Arguments arguments, final TemplateResolution templateResolution, final Document document, final Element element);

    /**
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    protected abstract static class AbstractFragmentSpec {

        private final String fragmentTemplateName;

        public AbstractFragmentSpec(final String fragmentTemplateName) {
            super();
            Validate.notEmpty(fragmentTemplateName, "Fragment template name cannot be null or empty");
            this.fragmentTemplateName = fragmentTemplateName;
        }

        public String getFragmentTemplateName() {
            return this.fragmentTemplateName;
        }
    }

    /**
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    protected static final class NamedFragmentSpec extends AbstractFragmentSpec {

        private final String fragmentTagName;

        private final String fragmentAttributeName;

        private final String fragmentAttributeValue;

        public NamedFragmentSpec(final String fragmentTemplateName, final String fragmentAttributeName, final String fragmentAttributeValue) {
            this(fragmentTemplateName, null, fragmentAttributeName, fragmentAttributeValue);
        }

        public NamedFragmentSpec(final String fragmentTemplateName, final String fragmentTagName, final String fragmentAttributeName, final String fragmentAttributeValue) {
            super(fragmentTemplateName);
            Validate.notEmpty(fragmentAttributeName, "Fragment attribute name cannot be null or empty");
            Validate.notEmpty(fragmentAttributeValue, "Fragment attribute value cannot be null or empty");
            this.fragmentTagName = fragmentTagName;
            this.fragmentAttributeName = fragmentAttributeName;
            this.fragmentAttributeValue = fragmentAttributeValue;
        }

        public String getFragmentTagName() {
            return this.fragmentTagName;
        }

        public String getFragmentAttributeName() {
            return this.fragmentAttributeName;
        }

        public String getFragmentAttributeValue() {
            return this.fragmentAttributeValue;
        }
    }

    /**
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    protected static final class CompleteTemplateFragmentSpec extends AbstractFragmentSpec {

        public CompleteTemplateFragmentSpec(final String fragmentTemplateName) {
            super(fragmentTemplateName);
        }
    }

    /**
     * 
     * @author Daniel Fern&aacute;ndez
     * 
     * @since 1.0
     *
     */
    protected static final class XPathFragmentSpec extends AbstractFragmentSpec {

        private final String xpathExpression;

        public XPathFragmentSpec(final String fragmentTemplateName, final String xpathExpression) {
            super(fragmentTemplateName);
            Validate.notEmpty(xpathExpression, "XPath expression cannot be null or empty");
            this.xpathExpression = xpathExpression;
        }

        public String getXPathExpression() {
            return this.xpathExpression;
        }
    }
}
