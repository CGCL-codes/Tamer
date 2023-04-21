package org.apache.myfaces.view.facelets.compiler;

import javax.faces.view.facelets.Tag;
import org.apache.myfaces.view.facelets.tag.TagLibrary;

final class TrimmedTagUnit extends TagUnit {

    public TrimmedTagUnit(TagLibrary library, String namespace, String name, Tag tag, String id) {
        super(library, namespace, name, tag, id);
    }
}
