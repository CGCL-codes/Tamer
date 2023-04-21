package eu.pisolutions.ocelot.document.value;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.object.PdfBooleanObject;
import eu.pisolutions.ocelot.object.PdfObject;

public final class BooleanNode extends ValueNode<Boolean> {

    public enum Factory implements DocumentNodeFactory<BooleanNode> {

        INSTANCE;

        public BooleanNode createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new BooleanNode(indirect, null);
        }
    }

    public static BooleanNode valueOf(Boolean value) {
        return value != null ? new BooleanNode(value) : null;
    }

    public BooleanNode(boolean value) {
        super(Boolean.valueOf(value));
    }

    private BooleanNode(boolean indirect, Object dummy) {
        super(indirect);
    }

    public void readFromPdfObject(DocumentReadingContext context, PdfObject object) {
        this.value = DocumentReadingContextHelper.asPdfObjectOfType(PdfBooleanObject.class, object).getValue();
    }

    public PdfBooleanObject createPdfObject(DocumentPdfObjectCreationContext context) {
        return PdfBooleanObject.valueOf(this.value.booleanValue());
    }
}
