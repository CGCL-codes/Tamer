package com.google.gwt.eclipse.core.uibinder.sse;

import com.google.gdt.eclipse.core.SseUtilities;
import com.google.gwt.eclipse.core.uibinder.UiBinderConstants;
import com.google.gwt.eclipse.core.uibinder.UiBinderXmlModelUtilities;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.wst.css.core.text.ICSSPartitions;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.core.internal.text.rules.StructuredTextPartitionerForXML;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Partitions the UiBinder XML file into XML partitions and CSS partitions.
 * <p>
 * Typically with Eclipse text editors, a custom partitioning can be defined by
 * overriding
 * {@link org.eclipse.jface.text.source.SourceViewerConfiguration#getConfiguredDocumentPartitioning(org.eclipse.jface.text.source.ISourceViewer)}
 * . However, with SSE, this method is declared final. In order to have our
 * partitioner used, we are required to create a model handler, document loader,
 * and model loader for UiBinder XML files.
 */
@SuppressWarnings("restriction")
public class StructuredTextPartitionerForUiBinderXml extends StructuredTextPartitionerForXML {

    /**
   * These are a subset of the XML region types where CSS cannot exist.
   */
    private static final Set<String> CSS_DISALLOWED_TYPES = new HashSet<String>(Arrays.asList(new String[] { DOMRegionContext.XML_EMPTY_TAG_CLOSE, DOMRegionContext.XML_END_TAG_OPEN, DOMRegionContext.XML_TAG_ATTRIBUTE_EQUALS, DOMRegionContext.XML_TAG_ATTRIBUTE_NAME, DOMRegionContext.XML_TAG_ATTRIBUTE_VALUE, DOMRegionContext.XML_TAG_CLOSE, DOMRegionContext.XML_TAG_NAME, DOMRegionContext.XML_TAG_OPEN }));

    private static final Set<String> OPEN_TYPES = Collections.singleton(DOMRegionContext.XML_TAG_OPEN);

    private static final Set<String> CLOSED_TYPES = new HashSet<String>(Arrays.asList(new String[] { DOMRegionContext.XML_TAG_CLOSE, DOMRegionContext.XML_EMPTY_TAG_CLOSE }));

    @Override
    public String getPartitionType(ITextRegion region, int offset) {
        if (isCssPartition(region, offset)) {
            return ICSSPartitions.STYLE;
        } else {
            return super.getPartitionType(region, offset);
        }
    }

    @Override
    public IDocumentPartitioner newInstance() {
        return new StructuredTextPartitionerForUiBinderXml();
    }

    /**
   * Determines whether the given XML model-backed region is an inlined CSS
   * partition.
   */
    private boolean isCssPartition(ITextRegion textRegion, int offset) {
        if (CSS_DISALLOWED_TYPES.contains(textRegion.getType())) {
            return false;
        }
        IStructuredDocumentRegion region = fStructuredDocument.getRegionAtCharacterOffset(offset);
        IStructuredDocumentRegion tagOpenRegion = SseUtilities.findStructuredDocumentRegion(OPEN_TYPES, CLOSED_TYPES, region, false);
        if (tagOpenRegion == null) {
            return false;
        }
        return isStyleElement(tagOpenRegion);
    }

    /**
   * Determines whether the given region is a UiBinder style element (opening or
   * closing) that can contain a CSS block.
   * 
   * @param styleElementRegion the region to check. If null, method returns
   *          false.
   */
    private boolean isStyleElement(IStructuredDocumentRegion styleElementRegion) {
        if (styleElementRegion == null) {
            return false;
        }
        String uiBinderPrefix = null;
        ITextRegionList regions = styleElementRegion.getRegions();
        for (int i = 0; i < regions.size(); i++) {
            ITextRegion styleElementChildTextRegion = regions.get(i);
            if (styleElementChildTextRegion.getType().equals(DOMRegionContext.XML_TAG_NAME)) {
                if (uiBinderPrefix == null) {
                    uiBinderPrefix = UiBinderXmlModelUtilities.resolveUiBinderNamespacePrefix(styleElementRegion.getParentDocument());
                }
                String tagName = styleElementRegion.getText(styleElementChildTextRegion);
                if (tagName.equalsIgnoreCase(uiBinderPrefix + ":" + UiBinderConstants.UI_BINDER_STYLE_ELEMENT_NAME)) {
                    return true;
                }
            }
        }
        return false;
    }
}
