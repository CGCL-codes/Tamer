package eu.pisolutions.ocelot.document.content;

import eu.pisolutions.ocelot.content.RenderingIntent;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.value.EnumWithNameNode;
import eu.pisolutions.ocelot.object.PdfObject;

public final class RenderingIntentNode extends EnumWithNameNode<RenderingIntent> {

    public enum Factory implements DocumentNodeFactory<RenderingIntentNode> {

        INSTANCE;

        public RenderingIntentNode createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new RenderingIntentNode(indirect);
        }
    }

    public static RenderingIntentNode valueOf(RenderingIntent value) {
        return value != null ? new RenderingIntentNode(value) : null;
    }

    public RenderingIntentNode(RenderingIntent value) {
        super(value);
    }

    private RenderingIntentNode(boolean indirect) {
        super(indirect);
    }

    @Override
    protected Class<RenderingIntent> getEnumClass() {
        return RenderingIntent.class;
    }
}
