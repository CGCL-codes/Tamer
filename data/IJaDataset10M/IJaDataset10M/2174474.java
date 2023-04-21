package eu.pisolutions.ocelot.document.font.truetype;

import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.font.AbstractSimpleFont;
import eu.pisolutions.ocelot.document.font.FontDescriptor;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.object.PdfNameObject;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * TrueType font.
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 5.5.2</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 */
public final class TrueTypeFont extends AbstractSimpleFont {

    public enum Factory implements DocumentNodeFactory<TrueTypeFont> {

        INSTANCE;

        public TrueTypeFont createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new TrueTypeFont(indirect);
        }
    }

    public TrueTypeFont(String baseFont, FontDescriptor descriptor) {
        super(baseFont, descriptor);
    }

    private TrueTypeFont(boolean indirect) {
        super(indirect);
    }

    @Override
    protected PdfNameObject getFontType() {
        return PdfNameConstants.TRUE_TYPE;
    }
}
