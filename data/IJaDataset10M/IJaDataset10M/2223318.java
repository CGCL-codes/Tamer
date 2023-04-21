package org.bookshare.validator.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.benetech.beans.error.ValidationError.Severity;
import org.bookshare.validator.error.DuplicateElementValidationError;
import org.bookshare.validator.error.MissingElementValidationError;
import org.bookshare.validator.report.ValidationMetadata;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * Utilities related to validating XML documents.
 * @author Reuben Firmin
 */
public final class XMLValidationUtils {

    /**
     * Non-constructor.
     */
    private XMLValidationUtils() {
    }

    /**
     * Return a child element from the supplied parent in the given
     * namespace. If it doesn't exist, add a validation violation at the given
     * severity.
     * @param parent The parent element that contains the child
     * @param elementName The name of the child to find
     * @param namespace The namespace the child must be in
     * @param reportMetadata The reporting object
     * @param severity The severity of the validation, if not found
     * @param doc The document that's being processed
     * @return null if none exists
     */
    public static Element assertExistsAsExpected(final Element parent, final String elementName, final Namespace namespace, final ValidationMetadata reportMetadata, final Severity severity, final Document doc) {
        final Element element = parent.getChild(elementName, namespace);
        if (element == null) {
            reportMetadata.getDocumentViolations().put(doc, new MissingElementValidationError(elementName, parent.getName(), doc.getBaseURI(), severity));
        }
        return element;
    }

    /**
     * Try to get a child element from the supplied parent in the given
     * set of children. If it doesn't exist, add a validation violation at the
     * given severity.  If the element is not repeatable, add a violation if
     * duplicates are found (at error severity). Return all matches.
     * @param parentName The name of the parent element that contains the child
     * @param elements The collection of children of the parent
     * @param elementName The name of the child to find
     * @param reportMetadata The reporting object
     * @param severity The severity of the validation, if not found
     * @param doc The document that's being processed
     * @param repeatable Whether the element is repeatable within the parent
     * @return null if none exists
     */
    public static List<Element> assertExistsAsExpected(final String parentName, final Map<String, List<Element>> elements, final String elementName, final ValidationMetadata reportMetadata, final Severity severity, final Document doc, final boolean repeatable) {
        final List<Element> siblings = elements.get(elementName);
        if (siblings == null || siblings.size() == 0) {
            reportMetadata.getDocumentViolations().put(doc, new MissingElementValidationError(elementName, parentName, doc.getBaseURI(), severity));
            return null;
        } else if (!repeatable && siblings.size() > 1) {
            for (Element sibling : siblings) {
                reportMetadata.getElementViolations().put(sibling, new DuplicateElementValidationError(elementName, parentName, doc.getBaseURI(), Severity.WARNING));
            }
        }
        return siblings;
    }

    /**
     * Return a child element from the supplied parent in the given
     * namespace with the given attribute set to the specified value. If it
     * doesn't exist, add a validation violation at the given severity. If
     * the element is not repeatable, add a violation if duplicates are found
     * (at error severity). Return the list of elements that match.
     * @param parentName The name of the parent element that contains the child
     * @param elements Sibling elements
     * @param elementName The name of the child to find
     * @param attributeName The name of the attribute that must be set
     * @param attributeValue The value that the attribute should be set to
     * @param reportMetadata The reporting object
     * @param severity The severity of the validation, if not found
     * @param doc The document that's being processed
     * @param repeatable Whether the element is repeatable
     * @return null if none exists
     */
    public static List<Element> assertExistsAsExpected(final String parentName, final Map<String, List<Element>> elements, final String elementName, final String attributeName, final String attributeValue, final ValidationMetadata reportMetadata, final Severity severity, final Document doc, final boolean repeatable) {
        final List<Element> siblings = elements.get(elementName);
        if (siblings == null || siblings.size() == 0) {
            reportMetadata.getDocumentViolations().put(doc, new MissingElementValidationError(elementName, parentName, doc.getBaseURI(), severity));
            return null;
        }
        final List<Element> siblingsWithAttribute = new ArrayList<Element>();
        for (Element element : siblings) {
            final Attribute attribute = element.getAttribute(attributeName);
            if (attribute != null && attributeValue.equals(attribute.getValue())) {
                siblingsWithAttribute.add(element);
            }
        }
        if (siblingsWithAttribute.size() == 0) {
            reportMetadata.getDocumentViolations().put(doc, new MissingElementValidationError(elementName, attributeName, attributeValue, parentName, doc.getBaseURI(), severity));
            return null;
        } else if (!repeatable && siblingsWithAttribute.size() > 1) {
            for (Element sibling : siblingsWithAttribute) {
                reportMetadata.getElementViolations().put(sibling, new DuplicateElementValidationError(elementName, attributeName, attributeValue, parentName, doc.getBaseURI(), Severity.ERROR));
            }
        }
        return siblingsWithAttribute;
    }
}
