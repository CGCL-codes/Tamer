package eu.pisolutions.ocelot.document.annotation.border.style;

import eu.pisolutions.ocelot.document.FlexibleDocumentNode;
import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.io.DelegatingDocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingException;
import eu.pisolutions.ocelot.document.value.FloatNode;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfNameObject;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * Annotation border style.
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 8.4.3</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 */
public abstract class AnnotationBorderStyle extends FlexibleDocumentNode {

    public static final class Factory extends DelegatingDocumentNodeFactory<AnnotationBorderStyle> {

        public static final Factory INSTANCE = new Factory();

        private Factory() {
            super();
            this.addFactory(null, SolidAnnotationBorderStyle.Factory.INSTANCE);
            this.addFactory(PdfNameConstants.S, SolidAnnotationBorderStyle.Factory.INSTANCE);
            this.addFactory(PdfNameConstants.D, DashedAnnotationBorderStyle.Factory.INSTANCE);
            this.addFactory(PdfNameConstants.B, BeveledAnnotationBorderStyle.Factory.INSTANCE);
            this.addFactory(PdfNameConstants.I, InsetAnnotationBorderStyle.Factory.INSTANCE);
            this.addFactory(PdfNameConstants.U, UnderlineAnnotationBorderStyle.Factory.INSTANCE);
        }

        @Override
        protected Object getFactoryKey(DocumentReadingContext context, boolean indirect, PdfObject object) {
            final PdfDictionaryObject dictionary = DocumentReadingContextHelper.asPdfObjectOfType(PdfDictionaryObject.class, object);
            return dictionary.get(PdfNameConstants.S);
        }
    }

    private FloatNode widthNode;

    AnnotationBorderStyle() {
        super();
    }

    AnnotationBorderStyle(boolean indirect) {
        super(indirect);
    }

    public final FloatNode getWidthNode() {
        return this.widthNode;
    }

    public final void setWidthNode(FloatNode widthNode) {
        this.widthNode = this.setProperty(this.widthNode, widthNode);
    }

    public final void readFromPdfObject(DocumentReadingContext context, PdfObject object) {
        final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
        final PdfDictionaryObject dictionary = DocumentReadingContextHelper.asPdfObjectOfType(PdfDictionaryObject.class, object);
        DocumentReadingContextHelper.expectPdfDictionaryObjectType(dictionary.getTypeValue(), PdfNameConstants.BORDER);
        final PdfNameObject type = DocumentReadingContextHelper.asElementOfType(PdfNameObject.class, dictionary.get(PdfNameConstants.S));
        if (type != null && !type.equals(this.getStyleType())) {
            throw new DocumentReadingException("Illegal annotation border style type: " + type);
        }
        this.widthNode = helper.getNode(dictionary.get(PdfNameConstants.W), FloatNode.Factory.INSTANCE);
        this.readSpecificEntriesFrom(context, dictionary);
    }

    public final PdfDictionaryObject createPdfObject(DocumentPdfObjectCreationContext context) {
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        final PdfDictionaryObject dictionary = new PdfDictionaryObject();
        dictionary.setTypeValue(PdfNameConstants.BORDER);
        dictionary.put(PdfNameConstants.S, this.getStyleType());
        dictionary.put(PdfNameConstants.W, helper.getElement(this.widthNode));
        this.addSpecificEntriesTo(context, dictionary);
        return dictionary;
    }

    protected abstract PdfNameObject getStyleType();

    protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
    }

    protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
    }
}
