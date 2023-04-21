package eu.pisolutions.ocelot.document.content;

import eu.pisolutions.ocelot.content.LineCapStyle;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.value.EnumWithCodeNode;
import eu.pisolutions.ocelot.object.PdfObject;

public final class LineCapStyleNode extends EnumWithCodeNode<LineCapStyle> {

    public enum Factory implements DocumentNodeFactory<LineCapStyleNode> {

        INSTANCE;

        public LineCapStyleNode createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new LineCapStyleNode(indirect);
        }
    }

    public static LineCapStyleNode valueOf(LineCapStyle value) {
        return value != null ? new LineCapStyleNode(value) : null;
    }

    public LineCapStyleNode(LineCapStyle value) {
        super(value);
    }

    private LineCapStyleNode(boolean indirect) {
        super(indirect);
    }

    @Override
    protected Class<LineCapStyle> getEnumClass() {
        return LineCapStyle.class;
    }
}
