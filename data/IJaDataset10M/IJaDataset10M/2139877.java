package eu.pisolutions.ocelot.document.font.program;

import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistry;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistryHelper;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.metadata.MetadataStream;
import eu.pisolutions.ocelot.document.stream.StreamNode;
import eu.pisolutions.ocelot.document.value.IntegerNode;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfNameObject;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.object.PdfObjectReference;
import eu.pisolutions.ocelot.version.PdfVersion;
import eu.pisolutions.ocelot.version.RequiredPdfVersionHelper;

/**
 * Embedded font.
 *
 * @author Laurent Pireyn
 */
public final class EmbeddedFont extends StreamNode {

    public enum Factory implements DocumentNodeFactory<EmbeddedFont> {

        INSTANCE;

        public EmbeddedFont createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            DocumentReadingContextHelper.requireIndirect(indirect);
            return new EmbeddedFont();
        }
    }

    public static final String FORMAT_TYPE1_COMPACT = "Type1C";

    public static final String FORMAT_TYPE0_COMPACT = "CIDFontType0C";

    public static final String FORMAT_OPEN_TYPE = "OpenType";

    private IntegerNode clearTextPortionLengthNode;

    private IntegerNode encryptedPortionLengthNode;

    private IntegerNode fixedContentPortionLengthNode;

    private String format;

    private MetadataStream metadata;

    public EmbeddedFont() {
        super();
    }

    public IntegerNode getClearTextPortionLengthNode() {
        return this.clearTextPortionLengthNode;
    }

    public void setClearTextPortionLengthNode(IntegerNode clearTextPortionLengthNode) {
        this.clearTextPortionLengthNode = this.setProperty(this.clearTextPortionLengthNode, clearTextPortionLengthNode);
    }

    public IntegerNode getEncryptedPortionLengthNode() {
        return this.encryptedPortionLengthNode;
    }

    public void setEncryptedPortionLengthNode(IntegerNode encryptedPortionLengthNode) {
        this.encryptedPortionLengthNode = this.setProperty(this.encryptedPortionLengthNode, encryptedPortionLengthNode);
    }

    public IntegerNode getFixedContentPortionLengthNode() {
        return this.fixedContentPortionLengthNode;
    }

    public void setFixedContentPortionLengthNode(IntegerNode fixedContentPortionLengthNode) {
        this.fixedContentPortionLengthNode = this.setProperty(this.fixedContentPortionLengthNode, fixedContentPortionLengthNode);
    }

    public MetadataStream getMetadata() {
        return this.metadata;
    }

    public void setMetadata(MetadataStream metadata) {
        this.metadata = this.setProperty(this.metadata, metadata);
    }

    @Override
    public PdfVersion getRequiredPdfVersion() {
        return new RequiredPdfVersionHelper(super.getRequiredPdfVersion()).requirePdfVersionIfNotNull(PdfVersion.VERSION_1_2, this.format).requirePdfVersionIfNotNull(PdfVersion.VERSION_1_4, this.metadata).getRequiredPdfVersion();
    }

    @Override
    public void registerNodes(DocumentNodeRegistry registry) {
        super.registerNodes(registry);
        new DocumentNodeRegistryHelper(registry).registerNode(this.clearTextPortionLengthNode).registerNode(this.encryptedPortionLengthNode).registerNode(this.fixedContentPortionLengthNode).registerNode(this.metadata, PdfVersion.VERSION_1_4);
    }

    @Override
    protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
        final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
        this.clearTextPortionLengthNode = helper.getNode(dictionary.get(PdfNameConstants.LENGTH1), IntegerNode.Factory.INSTANCE);
        this.encryptedPortionLengthNode = helper.getNode(dictionary.get(PdfNameConstants.LENGTH2), IntegerNode.Factory.INSTANCE);
        this.fixedContentPortionLengthNode = helper.getNode(dictionary.get(PdfNameConstants.LENGTH3), IntegerNode.Factory.INSTANCE);
        this.format = PdfNameObject.getString(dictionary.getSubtypeValue());
        this.metadata = helper.getIndirectNode(PdfObjectReference.getIdentifier(DocumentReadingContextHelper.asElementOfType(PdfObjectReference.class, dictionary.get(PdfNameConstants.METADATA))), MetadataStream.Factory.INSTANCE);
    }

    @Override
    protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        dictionary.put(PdfNameConstants.LENGTH1, helper.getElement(this.clearTextPortionLengthNode));
        dictionary.put(PdfNameConstants.LENGTH2, helper.getElement(this.encryptedPortionLengthNode));
        dictionary.put(PdfNameConstants.LENGTH3, helper.getElement(this.fixedContentPortionLengthNode));
        dictionary.setSubtypeValue(PdfNameObject.valueOf(this.format));
        dictionary.put(PdfNameConstants.METADATA, PdfObjectReference.valueOf(context.getIdentifier(this.metadata)));
    }
}
