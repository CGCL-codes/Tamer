package org.omg.tacsit.ui.query;

import java.awt.event.ActionEvent;
import java.util.Collection;
import javax.swing.Icon;
import org.omg.tacsit.common.ui.ConfigurableAction;
import org.omg.tacsit.common.ui.table.ListTableModel;
import org.omg.tacsit.controller.Entity;
import org.omg.tacsit.query.EntityQuery;
import org.omg.tacsit.query.QueryManager;

/**
 * An Action which populates the contents of a ListTableModel with the results from a query.
 * @author Matthew Child
 */
public class PopulateTableWithQueryAction extends ConfigurableAction {

    private QueryManager queryManager;

    private EntityQuery query;

    private ListTableModel<Entity> tableModel;

    /**
   * Creates a new instance.
   * @param name The name of the action.
   * @param icon The icon to use for the action.
   */
    public PopulateTableWithQueryAction(String name, Icon icon) {
        super(name, icon);
    }

    /**
   * Gets the query whose results will be used to populate the ListTableModel.
   * @return The query to use.
   */
    public EntityQuery getQuery() {
        return query;
    }

    /**
   * Sets the query whose results will be used to populate the ListTableModel.
   * @param query The query to use.
   */
    public void setQuery(EntityQuery query) {
        this.query = query;
    }

    /**
   * Gets the query manager that will execute the query.
   * @return The query manager that will perform the query.
   */
    public QueryManager getQueryManager() {
        return queryManager;
    }

    /**
   * Sets the query manager that will execute the query.
   * @param queryManager The query manager that will perform the query.
   */
    public void setQueryManager(QueryManager queryManager) {
        this.queryManager = queryManager;
        checkEnabledState();
    }

    /**
   * Gets the table model where the results of the query will be put.
   * @return The table model target.
   */
    public ListTableModel<Entity> getTableModel() {
        return tableModel;
    }

    /**
   * Sets the table model where the results of the query will be put.
   * @param tableModel The table model target.
   */
    public void setTableModel(ListTableModel<Entity> tableModel) {
        this.tableModel = tableModel;
        checkEnabledState();
    }

    @Override
    public boolean isPerformable() {
        return (queryManager != null) && (tableModel != null);
    }

    public void actionPerformed(ActionEvent e) {
        Collection<Entity> entityCollection = queryManager.submitEntityQuery(query);
        tableModel.clear();
        tableModel.addAll(entityCollection);
    }
}
