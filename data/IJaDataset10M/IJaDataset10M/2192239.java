package eu.pisolutions.ocelot.document.action;

import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.destination.Destination;
import eu.pisolutions.ocelot.document.destination.RegisterableDestination;
import eu.pisolutions.ocelot.document.file.specification.FileSpecification;
import eu.pisolutions.ocelot.document.io.DocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistry;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistryHelper;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.value.BooleanNode;
import eu.pisolutions.ocelot.document.value.ValueNode;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfNameObject;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.version.PdfVersion;

/**
 * Remote go-to action.
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 8.5.3</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 */
public final class RemoteGoToAction extends Action implements RegisterableDestination {

    public enum Factory implements DocumentNodeFactory<RemoteGoToAction> {

        INSTANCE;

        public RemoteGoToAction createNode(DocumentReadingContext context, boolean indirect, PdfObject object) {
            return new RemoteGoToAction(indirect);
        }
    }

    private FileSpecification fileSpecification;

    private Destination destination;

    private BooleanNode newWindowNode;

    public RemoteGoToAction() {
        super();
    }

    private RemoteGoToAction(boolean indirect) {
        super(indirect);
    }

    public FileSpecification getFileSpecification() {
        return this.fileSpecification;
    }

    public void setFileSpecification(FileSpecification fileSpecification) {
        this.fileSpecification = this.setProperty(this.fileSpecification, fileSpecification);
    }

    public Destination getDestination() {
        return this.destination;
    }

    public void setDestination(Destination destination) {
        this.destination = this.setProperty(this.destination, destination);
    }

    public BooleanNode getNewWindowNode() {
        return this.newWindowNode;
    }

    public void setNewWindowNode(BooleanNode newWindowNode) {
        this.newWindowNode = this.setProperty(this.newWindowNode, newWindowNode);
    }

    public Boolean getNewWindow() {
        return ValueNode.getValue(this.newWindowNode);
    }

    public void setNewWindow(Boolean newWindow) {
        this.setNewWindowNode(BooleanNode.valueOf(newWindow));
    }

    @Override
    public void registerNodes(DocumentNodeRegistry registry) {
        super.registerNodes(registry);
        new DocumentNodeRegistryHelper(registry).registerNode(this.fileSpecification).registerNode(this.destination).registerNode(this.newWindowNode, PdfVersion.VERSION_1_2);
    }

    @Override
    protected PdfNameObject getActionType() {
        return PdfNameConstants.GO_TO_R;
    }

    @Override
    protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
        final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
        this.fileSpecification = helper.getNode(DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, PdfNameConstants.F), FileSpecification.Factory.INSTANCE);
        this.destination = helper.getNode(DocumentReadingContextHelper.getRequiredPdfDictionaryObjectValue(dictionary, PdfNameConstants.D), Destination.Factory.INSTANCE);
        this.newWindowNode = helper.getNode(dictionary.get(PdfNameConstants.NEW_WINDOW), BooleanNode.Factory.INSTANCE);
    }

    @Override
    protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        dictionary.put(PdfNameConstants.F, helper.getElement(this.fileSpecification, true));
        dictionary.put(PdfNameConstants.D, helper.getElement(this.destination, true));
        if (this.newWindowNode != null && context.supportsPdfVersion(PdfVersion.VERSION_1_2)) {
            dictionary.put(PdfNameConstants.NEW_WINDOW, helper.getElement(this.newWindowNode));
        }
    }
}
