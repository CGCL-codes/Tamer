package eu.pisolutions.ocelot.document.function;

import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.version.PdfVersion;

/**
 * PostScript calculator function.
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 3.9.4</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 */
public final class PostScriptCalculatorFunction extends StreamFunction {

    public enum Factory implements DocumentNodeFactory<PostScriptCalculatorFunction> {

        INSTANCE;

        public PostScriptCalculatorFunction createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            DocumentReadingContextHelper.requireIndirect(indirect);
            return new PostScriptCalculatorFunction();
        }
    }

    public static final int TYPE = 4;

    public PostScriptCalculatorFunction() {
        super();
    }

    public int getType() {
        return PostScriptCalculatorFunction.TYPE;
    }

    @Override
    public PdfVersion getRequiredPdfVersion() {
        return PdfVersion.VERSION_1_3;
    }
}
