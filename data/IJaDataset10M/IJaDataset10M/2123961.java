package com.volantis.styling.impl.engine.sheet;

import com.volantis.styling.engine.StyleSheetMerger;
import com.volantis.styling.impl.sheet.Styler;

/**
 * Merge {@link Styler} from one list into another. 
 */
public interface StylerListMerger extends StyleSheetMerger {

    /**
     * Merge the {@link Styler}s in the delta list into the mutable list.
     *
     * @param list The list into which the {@link Styler}s are merged.
     * @param delta The list of {@link Styler}s to merge.
     */
    void merge(MutableStylerList list, StylerList delta);
}
