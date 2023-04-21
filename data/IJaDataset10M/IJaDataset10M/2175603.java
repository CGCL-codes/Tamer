package org.icepdf.core.tag.query;

import org.icepdf.core.tag.TaggedDocument;
import org.icepdf.core.tag.TaggedImage;

/**
 * @author mcollette
 * @since 4.0
 */
public class Not extends Operator {

    public int getArgumentCount() {
        return 1;
    }

    public boolean matches(TaggedDocument td, TaggedImage ti, String tag) {
        return !childExpressions[0].matches(td, ti, tag);
    }
}
