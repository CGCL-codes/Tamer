package eu.pisolutions.ocelot.document.content.resources;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.xobject.XObject;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * XObject registry.
 *
 * @author Laurent Pireyn
 * @see XObject
 */
public final class XObjectRegistry extends ContentResourceRegistry<XObject> {

    public enum Factory implements DocumentNodeFactory<XObjectRegistry> {

        INSTANCE;

        public XObjectRegistry createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new XObjectRegistry(indirect);
        }
    }

    public XObjectRegistry() {
        super();
    }

    private XObjectRegistry(boolean indirect) {
        super(indirect);
    }

    @Override
    protected DocumentNodeFactory<? extends XObject> getValueFactory() {
        return XObject.Factory.INSTANCE;
    }

    @Override
    protected String getGeneratedNamePrefix() {
        return "X";
    }
}
