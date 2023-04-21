package eu.pisolutions.ocelot.document.outline;

import eu.pisolutions.lang.ToStringBuilder;
import eu.pisolutions.lang.Validations;
import eu.pisolutions.ocelot.content.Color;
import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.action.Action;
import eu.pisolutions.ocelot.document.color.ColorNode;
import eu.pisolutions.ocelot.document.color.device.DeviceRgbColorSpace;
import eu.pisolutions.ocelot.document.destination.Destination;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistry;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistryHelper;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingException;
import eu.pisolutions.ocelot.document.outline.visitor.OutlineTreeNodeVisitor;
import eu.pisolutions.ocelot.document.value.StringNode;
import eu.pisolutions.ocelot.document.value.ValueNode;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfIntegerNumberObject;
import eu.pisolutions.ocelot.object.PdfObjectReference;
import eu.pisolutions.ocelot.version.PdfVersion;
import eu.pisolutions.ocelot.version.RequiredPdfVersionHelper;
import eu.pisolutions.util.BitField;

/**
 * Item in an outline tree.
 *
 * <p><b>Note:</b></p>
 * <p>
 * According to the specification, an outline item which is open, has at least one item and has all its items closed should mention a zero <code>Count</code>.
 * However, Adobe Acrobat Reader seems to ignore that value and, as a result, considers the outline item closed.
 * Tested with version 8.
 * </p>
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 8.2.2</dd>
 * <dd>PDF 1.7, H.3, 74</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 * @see OutlineTree
 */
public final class OutlineItem extends OutlineTreeNode {

    private static final BitField BIT_FIELD_TITLE_ITALIC = BitField.createSingleBitField(0);

    private static final BitField BIT_FIELD_TITLE_BOLD = BitField.createSingleBitField(1);

    private static void validateTitleNode(StringNode titleNode) {
        Validations.notNull(titleNode, "title node");
    }

    private static void validateTitle(String title) {
        Validations.notNull(title, "title");
    }

    private OutlineTreeNode parent;

    private OutlineItem previousItem;

    private OutlineItem nextItem;

    private StringNode titleNode;

    private ColorNode titleColorNode;

    private boolean titleItalic;

    private boolean titleBold;

    private Destination destination;

    private Action action;

    private boolean open;

    /**
     * Creates an outline item with the specified title.
     *
     * @param titleNode the title node (must not be <code>null</code>)
     */
    public OutlineItem(StringNode titleNode) {
        super();
        OutlineItem.validateTitleNode(titleNode);
        this.titleNode = titleNode;
    }

    /**
     * Creates an outline item with the specified title.
     *
     * @param title the title (must not be <code>null</code>)
     */
    public OutlineItem(String title) {
        super();
        OutlineItem.validateTitle(title);
        this.titleNode = new StringNode(title);
    }

    OutlineItem() {
        super();
    }

    /**
     * Returns the parent outline tree node of this outline item.
     *
     * @return the parent outline tree node of this outline item
     */
    public OutlineTreeNode getParent() {
        return this.parent;
    }

    public OutlineItem getPreviousItem() {
        return this.previousItem;
    }

    public OutlineItem getNextItem() {
        return this.nextItem;
    }

    public StringNode getTitleNode() {
        return this.titleNode;
    }

    public void setTitleNode(StringNode titleNode) {
        OutlineItem.validateTitleNode(titleNode);
        this.titleNode = this.setProperty(this.titleNode, titleNode);
    }

    public String getTitle() {
        return ValueNode.getValue(this.titleNode);
    }

    public void setTitle(String title) {
        OutlineItem.validateTitle(title);
        this.setTitleNode(new StringNode(title));
    }

    public ColorNode getTitleColorNode() {
        return this.titleColorNode;
    }

    public void setTitleColorNode(ColorNode titleColorNode) {
        final Color color = ValueNode.getValue(titleColorNode);
        Validations.isTrue(color == null || color.getComponentCount() == DeviceRgbColorSpace.COMPONENT_COUNT, "Illegal component count in title color %s", color);
        this.titleColorNode = this.setProperty(this.titleColorNode, titleColorNode);
    }

    public Color getTitleColor() {
        return ValueNode.getValue(this.titleColorNode);
    }

    public void setTitleColor(Color titleColor) {
        this.setTitleColorNode(ColorNode.valueOf(titleColor));
    }

    public boolean isTitleItalic() {
        return this.titleItalic;
    }

    public void setTitleItalic(boolean titleItalic) {
        this.titleItalic = this.setProperty(this.titleItalic, titleItalic);
    }

    public boolean isTitleBold() {
        return this.titleBold;
    }

    public void setTitleBold(boolean titleBold) {
        this.titleBold = this.setProperty(this.titleBold, titleBold);
    }

    public Destination getDestination() {
        return this.destination;
    }

    public void setDestination(Destination destination) {
        this.destination = this.setProperty(this.destination, destination);
        this.action = this.setProperty(this.action, null);
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = this.setProperty(this.action, action);
        this.destination = this.setProperty(this.destination, null);
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public PdfVersion getRequiredPdfVersion() {
        return new RequiredPdfVersionHelper(super.getRequiredPdfVersion()).requirePdfVersionIfNotNull(PdfVersion.VERSION_1_4, this.titleColorNode).requirePdfVersionIf(PdfVersion.VERSION_1_4, this.titleItalic).requirePdfVersionIf(PdfVersion.VERSION_1_4, this.titleBold).addPdfVersionRequirement(this.action).getRequiredPdfVersion();
    }

    @Override
    public void visit(OutlineTreeNodeVisitor visitor) {
        visitor.visitOutlineItem(this);
    }

    @Override
    public void registerNodes(DocumentNodeRegistry registry) {
        super.registerNodes(registry);
        new DocumentNodeRegistryHelper(registry).registerNode(this.titleNode).registerNode(this.titleColorNode).registerNode(this.destination).registerNode(this.action, PdfVersion.VERSION_1_1);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("titleNode", this.titleNode).append("titleColorNode", this.titleColorNode).append("titleItalic", this.titleItalic).append("titleBold", this.titleBold).append("destination", this.destination).append("action", this.action).append("open", this.open).toString();
    }

    @Override
    protected void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary) {
        final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
        this.titleNode = helper.getNode(dictionary.get(PdfNameConstants.TITLE), StringNode.Factory.INSTANCE);
        final PdfIntegerNumberObject countValue = DocumentReadingContextHelper.asElementOfType(PdfIntegerNumberObject.class, dictionary.get(PdfNameConstants.COUNT));
        this.open = countValue != null && countValue.getValue() > 0;
        this.titleColorNode = helper.getNode(dictionary.get(PdfNameConstants.C), ColorNode.Factory.INSTANCE);
        final PdfIntegerNumberObject value = DocumentReadingContextHelper.asElementOfType(PdfIntegerNumberObject.class, dictionary.get(PdfNameConstants.F));
        if (value != null) {
            final int flags = value.getValue();
            this.titleItalic = OutlineItem.BIT_FIELD_TITLE_ITALIC.isAllSet(flags);
            this.titleBold = OutlineItem.BIT_FIELD_TITLE_BOLD.isAllSet(flags);
        }
        this.destination = helper.getNode(dictionary.get(PdfNameConstants.DEST), Destination.Factory.INSTANCE);
        this.action = helper.getNode(dictionary.get(PdfNameConstants.A), Action.Factory.INSTANCE);
        if (this.destination != null && this.action != null) {
            throw new DocumentReadingException("Both destination and action specified by outline item");
        }
    }

    @Override
    protected void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary) {
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        dictionary.put(PdfNameConstants.PARENT, new PdfObjectReference(context.getIdentifier(this.parent)));
        dictionary.put(PdfNameConstants.PREV, PdfObjectReference.valueOf(context.getIdentifier(this.previousItem)));
        dictionary.put(PdfNameConstants.NEXT, PdfObjectReference.valueOf(context.getIdentifier(this.nextItem)));
        dictionary.put(PdfNameConstants.TITLE, helper.getElement(this.titleNode));
        if (!this.getItems().isEmpty()) {
            final int count = this.getRecursiveVisibleItemCount();
            dictionary.put(PdfNameConstants.COUNT, new PdfIntegerNumberObject(this.open ? count : -count));
        }
        dictionary.put(PdfNameConstants.C, helper.getElement(this.titleColorNode));
        int flags = 0;
        if (this.titleItalic && context.supportsPdfVersion(PdfVersion.VERSION_1_4)) {
            flags = OutlineItem.BIT_FIELD_TITLE_ITALIC.setAll(flags);
        }
        if (this.titleBold && context.supportsPdfVersion(PdfVersion.VERSION_1_4)) {
            flags = OutlineItem.BIT_FIELD_TITLE_BOLD.setAll(flags);
        }
        if (flags != 0) {
            dictionary.put(PdfNameConstants.F, new PdfIntegerNumberObject(flags));
        }
        dictionary.put(PdfNameConstants.DEST, helper.getElement(this.destination));
        dictionary.put(PdfNameConstants.A, helper.getElement(this.action));
    }

    void setParent(OutlineTreeNode parent) {
        this.parent = this.setProperty(this.parent, parent);
    }

    void setPreviousItem(OutlineItem previousItem) {
        this.previousItem = this.setProperty(this.previousItem, previousItem);
    }

    void setNextItem(OutlineItem nextItem) {
        this.nextItem = this.setProperty(this.nextItem, nextItem);
    }
}
