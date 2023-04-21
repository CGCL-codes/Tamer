package org.plazmaforge.studio.reportdesigner.dialogs.crosstab;

import org.plazmaforge.studio.reportdesigner.dialogs.AbstractFolderProvider;
import org.plazmaforge.studio.reportdesigner.model.crosstab.Crosstab;
import org.plazmaforge.studio.reportdesigner.model.crosstab.CrosstabDataset;
import org.plazmaforge.studio.reportdesigner.model.dataset.DatasetRun;

public abstract class AbstractCrosstabItemProvider extends AbstractFolderProvider {

    protected Crosstab getCrosstabElement() {
        return (Crosstab) getDesignElement(Crosstab.class);
    }

    protected CrosstabDataset getCrosstabDataset() {
        Crosstab chart = getCrosstabElement();
        if (chart == null) {
            return null;
        }
        return chart.getDataset();
    }

    protected DatasetRun getDatasetRun() {
        CrosstabDataset dataset = getCrosstabDataset();
        return dataset == null ? null : dataset.getDatasetRun();
    }
}
