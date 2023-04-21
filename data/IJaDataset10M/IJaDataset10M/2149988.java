package org.objectwiz.uibuilder.model.widget.runtime;

import org.objectwiz.uibuilder.model.action.result.DisplayDatasetActionResult;
import org.objectwiz.uibuilder.model.action.result.InBoardDisplayDatasetActionResult;
import org.objectwiz.uibuilder.runtime.ParsedComponent;
import org.objectwiz.uibuilder.model.widget.CollectionWidget;

/**
 * Runtime counterpart of {@link CollectionWidget}.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class ParsedCollectionWidget extends ParsedWidget {

    private InBoardDisplayDatasetActionResult datasetResult;

    public ParsedCollectionWidget(ParsedComponent parent, CollectionWidget widget, InBoardDisplayDatasetActionResult datasetResult) {
        super(parent, widget, null);
        this.datasetResult = datasetResult;
    }

    /**
     * The {@link DisplayDatasetActionResult} that contains all the necessary data
     * and logic for interacting with the dataset corresponding to the collection
     * to display.
     */
    public InBoardDisplayDatasetActionResult getDatasetResult() {
        datasetResult.attachUIBuilderApi(getApi());
        return datasetResult;
    }
}
