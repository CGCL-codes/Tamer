package eu.pisolutions.ocelot.document.page;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.value.EnumWithCodeNode;
import eu.pisolutions.ocelot.object.PdfObject;

public final class PageRotationNode extends EnumWithCodeNode<PageRotation> {

    public enum Factory implements DocumentNodeFactory<PageRotationNode> {

        INSTANCE;

        public PageRotationNode createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new PageRotationNode(indirect);
        }
    }

    public static PageRotationNode valueOf(PageRotation value) {
        return value != null ? new PageRotationNode(value) : null;
    }

    public PageRotationNode(PageRotation value) {
        super(value);
    }

    private PageRotationNode(boolean indirect) {
        super(indirect);
    }

    @Override
    protected Class<PageRotation> getEnumClass() {
        return PageRotation.class;
    }
}
