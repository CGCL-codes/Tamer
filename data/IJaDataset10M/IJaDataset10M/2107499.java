package org.objectwiz.plugin.uibuilder.model.renderer.table;

import org.objectwiz.core.dataset.DataSet;
import org.objectwiz.core.ui.rendering.DatasetNotSupportedException;
import org.objectwiz.core.ui.rendering.DatasetRendererOperationHandler;
import org.objectwiz.core.ui.rendering.table.TableOperationHandler;
import org.objectwiz.core.ui.rendering.table.TableView;
import org.objectwiz.plugin.customview.model.ColumnsDisplaySettings;
import org.objectwiz.plugin.uibuilder.renderer.UIBuilderRendererProxy;

/**
 * Proxy for the table renderer in the UIBuilder.
 *
 * It restricts accessible columns using default {@link ColumnsDisplaySettings}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class TableRendererUIBuilderProxy extends UIBuilderRendererProxy<TableOperationHandler> {

    @Override
    public DataSet getExposedDataset(DataSet sourceDs) {
        return sourceDs;
    }

    @Override
    public TableOperationHandler createClientSideHandler() {
        return new ClientSideTableRendererOperationHandler();
    }

    public class ClientSideTableRendererOperationHandler implements TableOperationHandler {

        private DataSet ds;

        private TableView view;

        @Override
        public Class<? extends DatasetRendererOperationHandler> getHandlerInterface() {
            return TableOperationHandler.class;
        }

        @Override
        public TableView getTableView() {
            if (view == null) throw new IllegalStateException("View not defined, call setDataSet() before");
            return view;
        }

        @Override
        public void setDataSet(DataSet dataset) throws DatasetNotSupportedException {
            ds = dataset;
            view = TableView.create(dataset);
        }

        @Override
        public DataSet getDataset() {
            return ds;
        }
    }
}
