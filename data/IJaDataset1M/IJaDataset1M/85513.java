package org.gocha.grid;

/**
 * Адаптер подписчика сетки данных
 * @author gocha
 */
public class DataGridAdapter implements DataGridListener {

    public void dataGridEvent(DataGridEvent event) {
        if (event == null) return;
        if (event instanceof DataGridModelEvent) dataGridModelEvent((DataGridModelEvent) event);
    }

    /**
     * Сообщает о событии изменения модели данных
     * @param event События изменения модели данных
     */
    protected void dataGridModelEvent(DataGridModelEvent event) {
        if (event.getDataGrid() == null) return;
        DataGrid grid = event.getDataGrid();
        switch(event.getAction()) {
            case DataGridModelEvent.INSERTING:
                dataGridModelInserting(grid);
                break;
            case DataGridModelEvent.INSERTED:
                dataGridModelInserted(grid);
                break;
            case DataGridModelEvent.CANCEL_INSERT:
                dataGridModelCancelInsert(grid);
                break;
        }
    }

    /**
     * Сообщает о начале вставки данных
     * @param grid Сетка данных
     */
    protected void dataGridModelInserting(DataGrid grid) {
    }

    /**
     * Сообщает о вставки данных завершено
     * @param grid Сетка данных
     */
    protected void dataGridModelInserted(DataGrid grid) {
    }

    /**
     * Сообщает о отменена вставка данных
     * @param grid Сетка данных
     */
    protected void dataGridModelCancelInsert(DataGrid grid) {
    }
}
