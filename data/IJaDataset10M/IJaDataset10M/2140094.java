package org.timepedia.chronoscope.client.render;

import org.timepedia.chronoscope.client.Dataset;

/**
 * Maps a {@link Dataset} to the {@link DatasetRenderer} that will be
 * responsible for rendering that dataset.
 * 
 * @author chad takahashi
 */
public class DatasetRendererMap {

    public DatasetRenderer get(Dataset dataset) {
        DatasetRenderer renderer = null;
        String key = dataset.getPreferredRenderer();
        if (key != null) {
            renderer = newDatasetRenderer(key);
        }
        if (renderer == null) {
            renderer = new LineXYRenderer();
        }
        return renderer;
    }

    /**
   * Creates a new instance of a {@link DatasetRenderer} for the specified key.
   */
    protected DatasetRenderer newDatasetRenderer(String key) {
        DatasetRenderer renderer = null;
        if ("line".equalsIgnoreCase(key)) {
            renderer = new LineXYRenderer();
        } else if ("bar".equalsIgnoreCase(key)) {
            renderer = new BarChartXYRenderer();
        }
        return renderer;
    }
}
