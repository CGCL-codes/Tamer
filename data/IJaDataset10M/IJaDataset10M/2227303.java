package eu.pisolutions.ocelot.document.io;

import java.util.HashMap;
import java.util.Map;
import eu.pisolutions.lang.Validations;
import eu.pisolutions.ocelot.document.DocumentNode;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * {@link DocumentNodeFactory} that delegates the creation of the document nodes to other factories.
 *
 * @author Laurent Pireyn
 */
public abstract class DelegatingDocumentNodeFactory<T extends DocumentNode> implements DocumentNodeFactory<T> {

    private final Map<Object, DocumentNodeFactory<? extends T>> factories = new HashMap<Object, DocumentNodeFactory<? extends T>>();

    protected DelegatingDocumentNodeFactory() {
        super();
    }

    public final T createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
        final Object key = this.getFactoryKey(context, indirect, object);
        DocumentNodeFactory<? extends T> factory = this.factories.get(key);
        if (factory == null) {
            factory = this.getDefaultFactory();
            if (factory == null) {
                throw new DocumentReadingException("Illegal factory key: " + key);
            }
        }
        return factory.createNode(context, indirect, object);
    }

    protected final void addFactory(Object key, DocumentNodeFactory<? extends T> factory) {
        Validations.notNull(factory, "factory");
        this.factories.put(key, factory);
    }

    protected DocumentNodeFactory<? extends T> getDefaultFactory() {
        return null;
    }

    protected abstract Object getFactoryKey(DocumentReadingContext context, boolean indirect, PdfObject object);
}
