package eu.pisolutions.ocelot.document.page;

import eu.pisolutions.lang.ToStringBuilder;
import eu.pisolutions.ocelot.content.Rectangle;
import eu.pisolutions.ocelot.document.IndirectDocumentNode;
import eu.pisolutions.ocelot.document.PdfNameConstants;
import eu.pisolutions.ocelot.document.content.resources.ContentResources;
import eu.pisolutions.ocelot.document.io.DelegatingDocumentNodeFactory;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistry;
import eu.pisolutions.ocelot.document.io.DocumentNodeRegistryHelper;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContext;
import eu.pisolutions.ocelot.document.io.DocumentPdfObjectCreationContextHelper;
import eu.pisolutions.ocelot.document.io.DocumentReadingContext;
import eu.pisolutions.ocelot.document.io.DocumentReadingContextHelper;
import eu.pisolutions.ocelot.document.page.visitor.PageTreeNodeVisitor;
import eu.pisolutions.ocelot.document.value.RectangleNode;
import eu.pisolutions.ocelot.document.value.ValueNode;
import eu.pisolutions.ocelot.object.PdfDictionaryObject;
import eu.pisolutions.ocelot.object.PdfObject;
import eu.pisolutions.ocelot.object.PdfObjectReference;
import eu.pisolutions.ocelot.version.PdfVersion;
import eu.pisolutions.ocelot.version.RequiredPdfVersionHelper;

/**
 * Node in a {@link PageTree}.
 * <p>
 * A <code>PageTreeNode</code> can be the child of one {@link PageTree}.
 * </p>
 *
 * <dl>
 * <dt><b>Specification:</b></dt>
 * <dd>PDF 1.7, 3.6.2</dd>
 * </dl>
 *
 * @author Laurent Pireyn
 * @see PageTree
 * @see Page
 */
public abstract class PageTreeNode extends IndirectDocumentNode {

    public static final class Factory extends DelegatingDocumentNodeFactory<PageTreeNode> {

        public static final Factory INSTANCE = new Factory();

        private Factory() {
            super();
            this.addFactory(PdfNameConstants.PAGES, PageTree.Factory.INSTANCE);
            this.addFactory(PdfNameConstants.PAGE, Page.Factory.INSTANCE);
        }

        @Override
        protected Object getFactoryKey(DocumentReadingContext context, boolean indirect, PdfObject object) {
            DocumentReadingContextHelper.requireIndirect(indirect);
            final PdfDictionaryObject dictionary = DocumentReadingContextHelper.asPdfObjectOfType(PdfDictionaryObject.class, object);
            return DocumentReadingContextHelper.getRequiredPdfDictionaryObjectType(dictionary);
        }
    }

    private PageTree parent;

    private RectangleNode mediaBoxNode;

    private RectangleNode cropBoxNode;

    private PageRotationNode rotationNode;

    private ContentResources resources;

    PageTreeNode() {
        super();
    }

    public final PageTree getParent() {
        return this.parent;
    }

    public final RectangleNode getMediaBoxNode() {
        return this.mediaBoxNode;
    }

    public final void setMediaBoxNode(RectangleNode mediaBoxNode) {
        this.mediaBoxNode = this.setProperty(this.mediaBoxNode, mediaBoxNode);
    }

    public final RectangleNode getInheritedMediaBoxNode() {
        if (this.mediaBoxNode != null) {
            return this.getMediaBoxNode();
        }
        if (this.parent != null) {
            return this.parent.getInheritedMediaBoxNode();
        }
        return null;
    }

    public final Rectangle getMediaBox() {
        return ValueNode.getValue(this.mediaBoxNode);
    }

    public final void setMediaBox(Rectangle mediaBox) {
        this.setMediaBoxNode(RectangleNode.valueOf(mediaBox));
    }

    public final Rectangle getInheritedMediaBox() {
        return ValueNode.getValue(this.getInheritedMediaBoxNode());
    }

    public final RectangleNode getCropBoxNode() {
        return this.cropBoxNode;
    }

    public final void setCropBoxNode(RectangleNode cropBoxNode) {
        this.cropBoxNode = this.setProperty(this.cropBoxNode, cropBoxNode);
    }

    public final RectangleNode getInheritedCropBoxNode() {
        if (this.cropBoxNode != null) {
            return this.getCropBoxNode();
        }
        if (this.parent != null) {
            return this.parent.getInheritedCropBoxNode();
        }
        return null;
    }

    public final Rectangle getCropBox() {
        return ValueNode.getValue(this.cropBoxNode);
    }

    public final void setCropBox(Rectangle cropBox) {
        this.setCropBoxNode(RectangleNode.valueOf(cropBox));
    }

    public final Rectangle getInheritedCropBox() {
        return ValueNode.getValue(this.getInheritedCropBoxNode());
    }

    public final Rectangle getActualCropBox() {
        final Rectangle cropBox = this.getInheritedCropBox();
        if (cropBox != null) {
            return cropBox;
        }
        return this.getInheritedMediaBox();
    }

    public final PageRotationNode getRotationNode() {
        return this.rotationNode;
    }

    public final void setRotationNode(PageRotationNode rotationNode) {
        this.rotationNode = this.setProperty(this.rotationNode, rotationNode);
    }

    public final PageRotationNode getInheritedRotationNode() {
        if (this.rotationNode != null) {
            return this.getRotationNode();
        }
        if (this.parent != null) {
            return this.parent.getInheritedRotationNode();
        }
        return null;
    }

    public final PageRotation getRotation() {
        return ValueNode.getValue(this.rotationNode);
    }

    public final void setRotation(PageRotation rotation) {
        this.setRotationNode(PageRotationNode.valueOf(rotation));
    }

    public final PageRotation getInheritedRotation() {
        return ValueNode.getValue(this.getInheritedRotationNode());
    }

    public final ContentResources getResources() {
        return this.resources;
    }

    public final void setResources(ContentResources resources) {
        this.resources = this.setProperty(this.resources, resources);
    }

    public final ContentResources getOrCreateResources() {
        if (this.resources == null) {
            this.resources = new ContentResources();
            this.modify();
        }
        return this.resources;
    }

    public final ContentResources getInheritedResources() {
        if (this.resources != null) {
            return this.resources;
        }
        if (this.parent != null) {
            return this.parent.getInheritedResources();
        }
        return null;
    }

    @Override
    public PdfVersion getRequiredPdfVersion() {
        return new RequiredPdfVersionHelper().addPdfVersionRequirement(this.resources).getRequiredPdfVersion();
    }

    public abstract void visit(PageTreeNodeVisitor visitor);

    public final void readFromPdfObject(DocumentReadingContext context, PdfObject object) {
        final DocumentReadingContextHelper helper = new DocumentReadingContextHelper(context);
        final PdfDictionaryObject dictionary = DocumentReadingContextHelper.asPdfObjectOfType(PdfDictionaryObject.class, object);
        this.parent = helper.getIndirectNode(PdfObjectReference.getIdentifier(DocumentReadingContextHelper.asElementOfType(PdfObjectReference.class, dictionary.get(PdfNameConstants.PARENT))), PageTree.Factory.INSTANCE);
        this.mediaBoxNode = helper.getNode(dictionary.get(PdfNameConstants.MEDIA_BOX), RectangleNode.Factory.INSTANCE);
        this.cropBoxNode = helper.getNode(dictionary.get(PdfNameConstants.CROP_BOX), RectangleNode.Factory.INSTANCE);
        this.rotationNode = helper.getNode(dictionary.get(PdfNameConstants.ROTATE), PageRotationNode.Factory.INSTANCE);
        this.resources = helper.getNode(dictionary.get(PdfNameConstants.RESOURCES), ContentResources.Factory.INSTANCE);
        this.readSpecificEntriesFrom(context, dictionary);
    }

    @Override
    public void registerNodes(DocumentNodeRegistry registry) {
        new DocumentNodeRegistryHelper(registry).registerNode(this.mediaBoxNode).registerNode(this.cropBoxNode).registerNode(this.rotationNode).registerNode(this.resources);
    }

    public final PdfDictionaryObject createPdfObject(DocumentPdfObjectCreationContext context) {
        final DocumentPdfObjectCreationContextHelper helper = new DocumentPdfObjectCreationContextHelper(context);
        final PdfDictionaryObject dictionary = new PdfDictionaryObject();
        if (this.parent != null) {
            dictionary.put(PdfNameConstants.PARENT, new PdfObjectReference(context.getIdentifier(this.parent)));
        }
        dictionary.put(PdfNameConstants.MEDIA_BOX, helper.getElement(this.mediaBoxNode));
        dictionary.put(PdfNameConstants.CROP_BOX, helper.getElement(this.cropBoxNode));
        if (this.rotationNode != null && this.rotationNode.getValue() != PageRotation.ROTATE_0) {
            dictionary.put(PdfNameConstants.ROTATE, helper.getElement(this.rotationNode));
        }
        dictionary.put(PdfNameConstants.RESOURCES, helper.getElement(this.resources));
        this.addSpecificEntriesTo(context, dictionary);
        return dictionary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("mediaBoxNode", this.mediaBoxNode).append("cropBoxNode", this.cropBoxNode).append("rotationNode", this.rotationNode).append("resources", this.resources).toString();
    }

    protected abstract void readSpecificEntriesFrom(DocumentReadingContext context, PdfDictionaryObject dictionary);

    protected abstract void addSpecificEntriesTo(DocumentPdfObjectCreationContext context, PdfDictionaryObject dictionary);

    final void setParent(PageTree parent) {
        this.parent = this.setProperty(this.parent, parent);
    }
}
