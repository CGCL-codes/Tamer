package org.awelements.table.csv;

import java.util.HashMap;
import java.util.Map;
import org.awelements.table.Column;

public class CSVColumnRendererFactory {

    private Map<Column, CSVColumnRenderer> mRenderers = new HashMap();

    public CSVColumnRenderer getRenderer(Column column) {
        CSVColumnRenderer renderer = mRenderers.get(column);
        if (renderer == null) {
            renderer = newRenderer(column);
            mRenderers.put(column, renderer);
        }
        return renderer;
    }

    private CSVColumnRenderer newRenderer(Column column) {
        return new CSVDefaultColumnRenderer();
    }
}
