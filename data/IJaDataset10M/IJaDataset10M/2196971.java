package org.mobicents.protocols.xcap.diff.dom;

import org.mobicents.protocols.xcap.diff.BuildPatchException;
import org.mobicents.protocols.xcap.diff.XcapDiffFactory;
import org.mobicents.protocols.xcap.diff.component.DocumentPatchComponentBuilder;
import org.mobicents.protocols.xcap.diff.dom.utils.DOMXmlUtils;
import org.mobicents.protocols.xml.patch.dom.DOMXmlPatchOperationsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * DOM Impl for {@link DocumentPatchComponentBuilder}.
 * 
 * @author baranowb
 * @author martins
 * 
 */
public class DOMDocumentPatchComponentBuilder implements DocumentPatchComponentBuilder<Element, Document, Node, Node> {

    private static final String ELEMENT_NAME = "document";

    private static final String SEL_ATTR_NAME = "sel";

    private static final String PREVIOUS_ETAG_ATTR_NAME = "previous-etag";

    private static final String NEW_ETAG_ATTR_NAME = "new-etag";

    private final DOMXcapDiffPatchBuilder xcapDiffPatchBuilder;

    private final DOMXmlPatchOperationsBuilder xmlPatchOperationsBuilder = new DOMXmlPatchOperationsBuilder();

    private Element bodyNotChangedElement;

    public DOMDocumentPatchComponentBuilder(DOMXcapDiffPatchBuilder xcapDiffPatchBuilder) {
        this.xcapDiffPatchBuilder = xcapDiffPatchBuilder;
    }

    /**
	 * 
	 * @return
	 */
    public DOMXcapDiffPatchBuilder getXcapDiffPatchBuilder() {
        return xcapDiffPatchBuilder;
    }

    @Override
    public Element buildPatchComponent(String sel, String previousETag, String newETag, Element[] patchingInstructions) throws BuildPatchException {
        Element patchComponent = null;
        try {
            patchComponent = DOMXmlUtils.createWellFormedDocumentFragment(ELEMENT_NAME, XcapDiffFactory.XCAP_DIFF_NAMESPACE_URI).getDocumentElement();
        } catch (Throwable e) {
            throw new BuildPatchException("Failed to create DOM element", e);
        }
        patchComponent.setAttribute(SEL_ATTR_NAME, sel);
        if (previousETag != null) {
            patchComponent.setAttribute(PREVIOUS_ETAG_ATTR_NAME, previousETag);
        }
        if (newETag != null) {
            patchComponent.setAttribute(NEW_ETAG_ATTR_NAME, newETag);
        }
        if (patchingInstructions != null) {
            Document document = patchComponent.getOwnerDocument();
            Node importedNode = null;
            for (Element patchingIntruction : patchingInstructions) {
                importedNode = document.importNode(patchingIntruction, true);
                patchComponent.appendChild(importedNode);
            }
        }
        return patchComponent;
    }

    @Override
    public Element getBodyNotChangedPatchingInstruction() throws BuildPatchException {
        if (bodyNotChangedElement == null) {
            try {
                bodyNotChangedElement = DOMXmlUtils.createWellFormedDocumentFragment("body-not-changed", XcapDiffFactory.XCAP_DIFF_NAMESPACE_URI).getDocumentElement();
            } catch (Throwable e) {
                throw new BuildPatchException("failed to create body-not-changed element", e);
            }
        }
        return bodyNotChangedElement;
    }

    @Override
    public DOMXmlPatchOperationsBuilder getXmlPatchOperationsBuilder() {
        return xmlPatchOperationsBuilder;
    }
}
