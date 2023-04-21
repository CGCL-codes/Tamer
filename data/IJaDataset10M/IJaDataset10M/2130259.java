package eu.pisolutions.ocelot.document.xobject.image;

import eu.pisolutions.lang.ToStringBuilder;
import eu.pisolutions.lang.Validations;
import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.color.ColorSpace;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistry;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistryHelper;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.value.IntegerNode;
import eu.pisolutions.ocelot.document.xobject.XObject;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfObject;

/**
 * Image XObject.
 *
 * @author Laurent Pireyn
 */
public final class ImageXObject extends XObject {

    public enum Factory implements DocumentNodeFactory<ImageXObject> {

        INSTANCE;

        public ImageXObject createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            DocumentReadingContextHelper.requireIndirect(indirect);
            return new ImageXObject();
        }
    }

    private static void validateWidth(int width) {
        Validations.greaterThanOrEqualTo(width, 0, "width");
    }

    private static void validateHeight(int height) {
        Validations.greaterThanOrEqualTo(height, 0, "height");
    }

    private static void validateBitsPerComponent(int bitsPerComponent) {
        Validations.greaterThan(bitsPerComponent, 0, "bits per component");
    }

    private IntegerNode widthNode;

    private IntegerNode heightNode;

    private ColorSpace colorSpace;

    private IntegerNode bitsPerComponentNode;

    public ImageXObject() {
        super();
    }

    public IntegerNode getWidthNode() {
        return this.widthNode;
    }

    public void setWidthNode(IntegerNode widthNode) {
        Validations.notNull(widthNode, "width node");
        ImageXObject.validateWidth(widthNode.getValue());
        this.widthNode = this.setProperty(this.widthNode, widthNode);
    }

    public int getWidth() {
        return this.widthNode.getValue();
    }

    public void setWidth(int width) {
        this.setWidthNode(new IntegerNode(width));
    }

    public IntegerNode getHeightNode() {
        return this.heightNode;
    }

    public void setHeightNode(IntegerNode heightNode) {
        Validations.notNull(heightNode, "height node");
        ImageXObject.validateHeight(heightNode.getValue());
        this.heightNode = this.setProperty(this.heightNode, heightNode);
    }

    public int getHeight() {
        return this.heightNode.getValue();
    }

    public void setHeight(int height) {
        this.setHeightNode(new IntegerNode(height));
    }

    public ColorSpace getColorSpace() {
        return this.colorSpace;
    }

    public void setColorSpace(ColorSpace colorSpace) {
        this.colorSpace = this.setProperty(this.colorSpace, colorSpace);
    }

    public IntegerNode getBitsPerComponentNode() {
        return this.bitsPerComponentNode;
    }

    public void setBitsPerComponentNode(IntegerNode bitsPerComponentNode) {
        Validations.notNull(bitsPerComponentNode, "bits per component node");
        ImageXObject.validateBitsPerComponent(bitsPerComponentNode.getValue());
        this.bitsPerComponentNode = this.setProperty(this.bitsPerComponentNode, bitsPerComponentNode);
    }

    public int getBitsPerComponent() {
        return this.bitsPerComponentNode.getValue();
    }

    public void setBitsPerComponent(int bitsPerComponent) {
        this.setBitsPerComponentNode(new IntegerNode(bitsPerComponent));
    }

    @Override
    public void registerNodes(DocumentNodeRegistry registry) {
        super.registerNodes(registry);
        new DocumentNodeRegistryHelper(registry).registerNode(this.colorSpace);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("widthNode", this.widthNode).append("heightNode", this.heightNode).append("colorSpace", this.colorSpace).append("bitsPerComponentNode", this.bitsPerComponentNode).toString();
    }

    @Override
    protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
        super.readSpecificEntriesFrom(context, dictionary);
        final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
        DocumentReadingContextHelper.expectPdfDictionaryObjectSubtype(dictionary.getSubtypeValue(), PdfNameConstants.IMAGE);
        this.widthNode = helper.getNode(DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, PdfNameConstants.WIDTH), IntegerNode.Factory.INSTANCE);
        this.heightNode = helper.getNode(DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, PdfNameConstants.HEIGHT), IntegerNode.Factory.INSTANCE);
        this.colorSpace = helper.getNode(dictionary.get(PdfNameConstants.COLOR_SPACE), ColorSpace.Factory.INSTANCE);
        this.bitsPerComponentNode = helper.getNode(DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, PdfNameConstants.BITS_PER_COMPONENT), IntegerNode.Factory.INSTANCE);
    }

    @Override
    protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
        super.addSpecificEntriesTo(context, dictionary);
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        dictionary.setSubtypeValue(PdfNameConstants.IMAGE);
        dictionary.put(PdfNameConstants.WIDTH, helper.getElement(this.widthNode));
        dictionary.put(PdfNameConstants.HEIGHT, helper.getElement(this.heightNode));
        dictionary.put(PdfNameConstants.COLOR_SPACE, helper.getElement(this.colorSpace));
        dictionary.put(PdfNameConstants.BITS_PER_COMPONENT, helper.getElement(this.bitsPerComponentNode));
    }
}
