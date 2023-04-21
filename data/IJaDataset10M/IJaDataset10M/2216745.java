package org.gocha.grid.buttons;

import org.gocha.grid.*;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import org.gocha.grid.images.Icons;

/**
 * Кнопка автоматических размеров для всех строк
 * @author gocha
 */
public class DataGridButtonDoInsert extends DataGridButton {

    /**
     * Конструктор по умолчанию
     */
    public DataGridButtonDoInsert() {
        Icon i = Icons.getIcon(Icons.INSERT_COMMIT);
        setIcon(i);
        setToolTipText("Вставить");
        setEnabled(false);
    }

    private void checkEnable() {
        setEnabled(getDataGrid().insertingRowVisible() && getDataGrid().isAllowInsert());
    }

    private DataGridAdapter listener = new DataGridAdapter() {

        @Override
        protected void dataGridModelEvent(DataGridModelEvent event) {
            checkEnable();
        }
    };

    @Override
    protected void dataGridPropertyChanged(PropertyChangeEvent evt) {
        checkEnable();
    }

    @Override
    protected void dataGridAttach() {
        super.dataGridAttach();
        getDataGrid().addDataGridListener(listener);
        checkEnable();
    }

    @Override
    protected void dataGridDetach() {
        super.dataGridDetach();
        getDataGrid().removeDataGridListener(listener);
        setEnabled(false);
    }

    @Override
    protected void onClick() {
        getDataGrid().doInsert();
    }
}
