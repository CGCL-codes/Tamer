package eu.pisolutions.ocelot.document.content.resources;

import eu.pisolutions.ocelot.document.font.Font;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * Font registry.
 *
 * @author Laurent Pireyn
 * @see Font
 */
public final class FontRegistry extends ContentResourceRegistry<Font> {

    public enum Factory implements DocumentNodeFactory<FontRegistry> {

        INSTANCE;

        public FontRegistry createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new FontRegistry(indirect);
        }
    }

    public FontRegistry() {
        super();
    }

    private FontRegistry(boolean indirect) {
        super(indirect);
    }

    @Override
    protected DocumentNodeFactory<? extends Font> getValueFactory() {
        return Font.Factory.INSTANCE;
    }

    @Override
    protected String getGeneratedNamePrefix() {
        return "F";
    }
}
