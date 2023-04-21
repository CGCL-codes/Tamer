package org.horen.ui.panes.providers;

import java.sql.SQLException;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.horen.core.db.DataBase;
import org.horen.task.PriorityModel;

/**
 * Provides the defined priority models to a structured viewer.
 * 
 * @author Steffen
 */
public class PriorityModelContentProvider implements IStructuredContentProvider {

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    @Override
    public Object[] getElements(Object inputElement) {
        try {
            PriorityModel[] all = PriorityModel.getAll();
            return all;
        } catch (SQLException e) {
            DataBase.getInstance().handleException(e);
        }
        return new Object[0];
    }

    @Override
    public void dispose() {
    }
}
