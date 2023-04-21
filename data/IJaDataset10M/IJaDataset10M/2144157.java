package org.icepdf.core.tag.query;

import org.icepdf.core.tag.TaggedDocument;
import org.icepdf.core.tag.TaggedImage;
import java.util.List;
import java.util.ArrayList;

/**
 * @author mcollette
 * @since 4.0
 */
public class DocumentResult {

    private TaggedDocument document;

    private List images;

    DocumentResult(TaggedDocument doc, TaggedImage initial) {
        document = doc;
        images = new ArrayList(8);
        addImage(initial);
    }

    void addImage(TaggedImage ti) {
        images.add(ti);
    }

    public TaggedDocument getDocument() {
        return document;
    }

    public List getImages() {
        return images;
    }
}
