package com.adobe.epubcheck.opf;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import com.adobe.epubcheck.api.Report;
import com.adobe.epubcheck.ocf.OCFPackage;

public class XRefChecker {

    public static final int RT_GENERIC = 0;

    public static final int RT_HYPERLINK = 1;

    public static final int RT_IMAGE = 2;

    public static final int RT_OBJECT = 3;

    public static final int RT_STYLESHEET = 4;

    public static final int RT_SVG_PAINT = 0x10;

    public static final int RT_SVG_CLIP_PATH = 0x11;

    public static final int RT_SVG_SYMBOL = 0x12;

    private class Reference {

        String resource;

        int lineNumber;

        String refResource;

        String fragment;

        int type;

        public Reference(String srcResource, int srcLineNumber, String refResource, String fragment, int type) {
            this.fragment = fragment;
            this.lineNumber = srcLineNumber;
            this.refResource = refResource;
            this.resource = srcResource;
            this.type = type;
        }
    }

    private class Anchor {

        String id;

        int lineNumber;

        int type;

        public Anchor(String id, int lineNumber, int type) {
            this.id = id;
            this.lineNumber = lineNumber;
            this.type = type;
        }
    }

    private class Resource {

        String resource;

        String mimeType;

        Hashtable anchors;

        boolean inSpine;

        boolean hasValidItemFallback;

        boolean hasValidImageFallback;

        Resource(String resource, String type, boolean inSpine, boolean hasValidItemFallback, boolean hasValidImageFallback) {
            this.mimeType = type;
            this.resource = resource;
            this.inSpine = inSpine;
            this.hasValidItemFallback = hasValidItemFallback;
            this.hasValidImageFallback = hasValidImageFallback;
            this.anchors = new Hashtable();
        }
    }

    Hashtable resources = new Hashtable();

    HashSet undeclared = new HashSet();

    Vector references = new Vector();

    Report report;

    OCFPackage ocf;

    public XRefChecker(OCFPackage ocf, Report report) {
        this.ocf = ocf;
        this.report = report;
    }

    public void registerResource(String resource, String mimeType, boolean inSpine, boolean hasValidItemFallback, boolean hasValidImageFallback) {
        if (resources.get(resource) != null) throw new IllegalArgumentException("duplicate resource: " + resource);
        resources.put(resource, new Resource(resource, mimeType, inSpine, hasValidItemFallback, hasValidImageFallback));
    }

    public void registerAnchor(String resource, int lineNumber, String id, int type) {
        Resource res = (Resource) resources.get(resource);
        if (res == null) throw new IllegalArgumentException("unregistered resource: " + resource);
        if (res.anchors.get(id) != null) throw new IllegalArgumentException("duplicate id: " + id);
        res.anchors.put(id, new Anchor(id, lineNumber, type));
    }

    public void registerReference(String srcResource, int srcLineNumber, String refResource, String refFragment, int type) {
        if (refResource.startsWith("data:")) return;
        references.add(new Reference(srcResource, srcLineNumber, refResource, refFragment, type));
    }

    public void registerReference(String srcResource, int srcLineNumber, String ref, int type) {
        if (ref.startsWith("data:")) return;
        int hash = ref.indexOf("#");
        String refResource;
        String refFragment;
        if (hash >= 0) {
            refResource = ref.substring(0, hash);
            refFragment = ref.substring(hash + 1);
        } else {
            refResource = ref;
            refFragment = null;
        }
        registerReference(srcResource, srcLineNumber, refResource, refFragment, type);
    }

    public void checkReferences() {
        Enumeration refs = references.elements();
        while (refs.hasMoreElements()) {
            Reference ref = (Reference) refs.nextElement();
            checkReference(ref);
        }
    }

    private void checkReference(Reference ref) {
        Resource res = (Resource) resources.get(ref.refResource);
        if (res == null) {
            if (!ocf.hasEntry(ref.refResource)) report.error(ref.resource, ref.lineNumber, "'" + ref.refResource + "': referenced resource missing in the package"); else if (!undeclared.contains(ref.refResource)) {
                undeclared.add(ref.refResource);
                report.error(ref.resource, ref.lineNumber, "'" + ref.refResource + "': referenced resource exists, but not declared in the OPF file");
            }
            return;
        }
        if (ref.fragment == null) {
            switch(ref.type) {
                case RT_SVG_PAINT:
                case RT_SVG_CLIP_PATH:
                case RT_SVG_SYMBOL:
                    report.error(ref.resource, ref.lineNumber, "fragment identifier missing in reference to '" + ref.refResource + "'");
                    break;
                case RT_HYPERLINK:
                    if (res.mimeType != null && !OPFChecker.isBlessedItemType(res.mimeType) && !OPFChecker.isDeprecatedBlessedItemType(res.mimeType) && !res.hasValidItemFallback) report.error(ref.resource, ref.lineNumber, "hyperlink to non-standard resource '" + ref.refResource + "' of type '" + res.mimeType + "'");
                    if (!res.inSpine) report.warning(ref.resource, ref.lineNumber, "hyperlink to resource outside spine '" + ref.refResource + "'");
                    break;
                case RT_IMAGE:
                    if (res.mimeType != null && !OPFChecker.isBlessedImageType(res.mimeType) && !res.hasValidImageFallback) report.error(ref.resource, ref.lineNumber, "non-standard image resource '" + ref.refResource + "' of type '" + res.mimeType + "'");
                    break;
                case RT_STYLESHEET:
                    break;
            }
        } else {
            switch(ref.type) {
                case RT_HYPERLINK:
                    if (res.mimeType != null && !OPFChecker.isBlessedItemType(res.mimeType) && !OPFChecker.isDeprecatedBlessedItemType(res.mimeType) && !res.hasValidItemFallback) report.error(ref.resource, ref.lineNumber, "hyperlink to non-standard resource '" + ref.refResource + "' of type '" + res.mimeType + "'");
                    if (!res.inSpine) report.warning(ref.resource, ref.lineNumber, "hyperlink to resource outside spine '" + ref.refResource + "'");
                    break;
                case RT_IMAGE:
                    report.error(ref.resource, ref.lineNumber, "fragment identifier used for image resource '" + ref.refResource + "'");
                    break;
                case RT_STYLESHEET:
                    report.error(ref.resource, ref.lineNumber, "fragment identifier used for stylesheet resource '" + ref.refResource + "'");
                    break;
            }
            Anchor anchor = (Anchor) res.anchors.get(ref.fragment);
            if (anchor == null) {
                report.error(ref.resource, ref.lineNumber, "'" + ref.fragment + "': fragment identifier is not defined in '" + ref.refResource + "'");
                return;
            } else {
                switch(ref.type) {
                    case RT_SVG_PAINT:
                    case RT_SVG_CLIP_PATH:
                        if (anchor.type != ref.type) report.error(ref.resource, ref.lineNumber, "fragment identifier '" + ref.fragment + "' defines incompatible resource type in '" + ref.refResource + "'");
                        break;
                    case RT_SVG_SYMBOL:
                    case RT_HYPERLINK:
                        if (anchor.type != ref.type && anchor.type != RT_GENERIC) report.error(ref.resource, ref.lineNumber, "fragment identifier '" + ref.fragment + "' defines incompatible resource type in '" + ref.refResource + "'");
                        break;
                }
            }
        }
    }
}
