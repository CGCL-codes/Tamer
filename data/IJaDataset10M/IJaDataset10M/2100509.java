package eu.pisolutions.ocelot.document.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import eu.pisolutions.lang.Objects;
import eu.pisolutions.lang.Validations;
import eu.pisolutions.ocelot.document.DocumentNode;
import eu.pisolutions.ocelot.object.ContainerPdfObjectElement;
import eu.pisolutions.ocelot.object.PdfArrayObject;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfNameObject;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.object.PdfObjectIdentifier;
import eu.pisolutions.ocelot.object.PdfObjectReference;

/**
 * {@link DocumentReadingContext} helper.
 *
 * @author Laurent Pireyn
 */
public final class DocumentReadingContextHelper extends Object {

    public static void requireIndirect(boolean indirect) {
        if (!indirect) {
            throw new DocumentReadingException("Illegal direct PDF object");
        }
    }

    public static void expectElement(ContainerPdfObjectElement actualElement, ContainerPdfObjectElement expectedElement) {
        if (!Objects.equals(actualElement, expectedElement)) {
            throw new DocumentReadingException("Expected element " + expectedElement + " instead of " + actualElement);
        }
    }

    public static <T extends PdfObject> T asPdfObjectOfType(Class<? extends T> expectedClass, PdfObject pdfObject) {
        validateExpectedClass(expectedClass);
        if (pdfObject == null) {
            return null;
        }
        final Class<? extends PdfObject> pdfObjectClass = pdfObject.getClass();
        if (!expectedClass.isAssignableFrom(pdfObjectClass)) {
            throw new DocumentReadingException("Unexpected PDF object type " + pdfObjectClass.getName() + " instead of " + expectedClass.getName());
        }
        return expectedClass.cast(pdfObject);
    }

    public static <T extends ContainerPdfObjectElement> T asElementOfType(Class<? extends T> expectedClass, ContainerPdfObjectElement element) {
        validateExpectedClass(expectedClass);
        if (element == null) {
            return null;
        }
        final Class<? extends ContainerPdfObjectElement> elementClass = element.getClass();
        if (!expectedClass.isAssignableFrom(elementClass)) {
            throw new DocumentReadingException("Unexpected element type " + elementClass.getSimpleName() + " instead of " + expectedClass.getSimpleName());
        }
        return expectedClass.cast(element);
    }

    public static PdfNameObject getRequiredPdfDictionaryObjectType(PdfDictionaryObject dictionary) {
        final PdfNameObject value = dictionary.getTypeValue();
        if (value == null) {
            throw new DocumentReadingException("Missing PDF dictionary object type");
        }
        return value;
    }

    public static PdfNameObject getRequiredPdfDictionaryObjectSubtype(PdfDictionaryObject dictionary) {
        final PdfNameObject value = dictionary.getSubtypeValue();
        if (value == null) {
            throw new DocumentReadingException("Missing PDF dictionary object subtype");
        }
        return value;
    }

    public static ContainerPdfObjectElement getRequiredPdfDictionaryObjectValue(PdfDictionaryObject dictionary, PdfNameObject key) {
        final ContainerPdfObjectElement value = dictionary.get(key);
        if (value == null) {
            throw new DocumentReadingException("Missing PDF dictionary object entry: " + key.getString());
        }
        return value;
    }

    public static void expectPdfDictionaryObjectType(PdfNameObject actualValue, PdfNameObject expectedValue) {
        if (actualValue != null && !actualValue.equals(expectedValue)) {
            throw new DocumentReadingException("Unexpected PDF dictionary object type: " + actualValue.getString());
        }
    }

    public static void expectPdfDictionaryObjectSubtype(PdfNameObject actualValue, PdfNameObject expectedValue) {
        if (actualValue != null && !actualValue.equals(expectedValue)) {
            throw new DocumentReadingException("Unexpected PDF dictionary object subtype: " + actualValue.getString());
        }
    }

    private static void validateExpectedClass(Class<?> expectedClass) {
        Validations.notNull(expectedClass, "expected class");
    }

    private static <T extends DocumentNode> void validateFactory(DocumentNodeFactory<T> factory) {
        Validations.notNull(factory, "factory");
    }

    private final DocumentReadingContext context;

    public DocumentReadingContextHelper(DocumentReadingContext context) {
        super();
        Validations.notNull(context, "context");
        this.context = context;
    }

    public <T extends DocumentNode> T getIndirectNode(PdfObjectIdentifier identifier, DocumentNodeFactory<T> factory) {
        DocumentReadingContextHelper.validateFactory(factory);
        if (identifier == null) {
            return null;
        }
        return this.context.getNode(identifier, factory);
    }

    public <T extends DocumentNode> T getNode(ContainerPdfObjectElement element, DocumentNodeFactory<? extends T> factory) {
        DocumentReadingContextHelper.validateFactory(factory);
        if (element == null) {
            return null;
        }
        if (element instanceof PdfObjectReference) {
            final PdfObjectIdentifier identifier = ((PdfObjectReference) element).getIdentifier();
            try {
                return this.context.getNode(identifier, factory);
            } catch (DocumentReadingException exception) {
                throw new DocumentReadingException("Cannot read indirect document node with identifier " + identifier, exception);
            }
        }
        assert element instanceof PdfObject;
        final PdfObject pdfObject = (PdfObject) element;
        try {
            final T node = new ValidatingDocumentNodeFactory<T>(factory).createNode(this.context, false, pdfObject);
            node.readFromPdfObject(this.context, pdfObject);
            return node;
        } catch (DocumentReadingException exception) {
            throw new DocumentReadingException("Cannot read direct document node for PDF object " + pdfObject, exception);
        }
    }

    public <T extends DocumentNode> List<T> readSimplifiableNodeList(ContainerPdfObjectElement element, DocumentNodeFactory<? extends T> factory) {
        DocumentReadingContextHelper.validateFactory(factory);
        if (element == null) {
            return Collections.emptyList();
        }
        final List<T> list = new ArrayList<T>();
        if (element instanceof PdfArrayObject) {
            final PdfArrayObject array = (PdfArrayObject) element;
            for (final ContainerPdfObjectElement arrayElement : array) {
                final T node = this.getNode(arrayElement, factory);
                if (node != null) {
                    list.add(node);
                }
            }
        } else {
            final T node = this.getNode(element, factory);
            if (node != null) {
                list.add(node);
            }
        }
        return list;
    }
}
