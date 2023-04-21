package com.yeep.universedesign.ui.client;

import java.util.ArrayList;
import java.util.List;
import com.yeep.universedesign.Context;
import com.yeep.universedesign.model.Association;
import com.yeep.universedesign.model.Category;
import com.yeep.universedesign.model.Column;
import com.yeep.universedesign.model.Metric;
import com.yeep.universedesign.model.MetricType;
import com.yeep.universedesign.model.ModelFactory;
import com.yeep.universedesign.model.Table;
import com.yeep.universedesign.model.Universe;
import com.yeep.universedesign.model.UnvColumn;
import com.yeep.universedesign.model.UnvConnection;
import com.yeep.universedesign.model.UnvTable;
import com.yeep.universedesign.service.UniverseService;

/**
 * Service which handle all UI Actions
 */
public class ActionHandler {

    private static ActionHandler instance = new ActionHandler();

    private UniverseService universeService;

    private ActionHandler() {
        this.universeService = (UniverseService) Context.getInstance().getBean("universeService");
    }

    public static ActionHandler getInstance() {
        return instance;
    }

    /************************ Buisness Methods ***************************/
    public void saveUnvConnection(UnvConnection unvConnection) {
        this.universeService.saveUnvConnection(unvConnection);
    }

    public UnvConnection loadUnvConnection(String id) {
        return id == null ? null : this.universeService.loadUnvConnection(id);
    }

    public List<UnvConnection> loadUnvConnections() {
        return this.universeService.loadUnvConnections();
    }

    public boolean testConnection(UnvConnection unvConnection) {
        return this.universeService.testConnection(unvConnection);
    }

    public void saveUniverse(Universe universe) {
        this.universeService.saveUniverse(universe);
    }

    public Universe loadUniverse(String id) {
        return id == null ? null : this.universeService.loadUniverse(id);
    }

    public List<Universe> loadUniverses() {
        return this.universeService.loadUniverses();
    }

    public List<Table> loadTables() {
        List<Table> ret = null;
        Universe universe = Context.getUniverse();
        if (universe != null) {
            ret = this.universeService.loadTables(universe.getUnvConnection());
        }
        return ret;
    }

    public UnvTable getUnvTable(Table table) {
        if (table == null) return null;
        UnvTable unvTable = ModelFactory.createUnvTable();
        unvTable.setName(table.getTableName());
        unvTable.setAliasName(table.getTableName());
        if (table.getColumns() != null) {
            for (Column column : table.getColumns()) {
                UnvColumn unvColumn = ModelFactory.createUnvColumn();
                unvColumn.setName(column.getName());
                unvColumn.setType(column.getType());
                unvColumn.setComments(column.getComments());
                unvTable.addUnvColumn(unvColumn);
            }
        }
        return unvTable;
    }

    public void addUnvTableToUniverse(UnvTable unvTable) {
        Universe universe = Context.getUniverse();
        if (universe == null || unvTable == null) return;
        universe.addUnvTable(unvTable);
    }

    public boolean isUnvTableExists(UnvTable unvTable) {
        Universe universe = Context.getUniverse();
        if (universe == null || unvTable == null) return false;
        return universe.contains(unvTable);
    }

    public void deleteUnvTable(UnvTable table) {
        Universe universe = Context.getUniverse();
        if (universe != null) {
            universe.removeUnvTable(table);
        }
    }

    public List<UnvTable> getUniverseTables() {
        return new ArrayList<UnvTable>(Context.getUniverse().getTables());
    }

    public void addAssociationToUniverse(Association association, Universe universe) {
        if (universe == null || association == null) return;
        universe.addAssociation(association);
    }

    public void removeAssociationFromUniverse(Association association) {
        if (Context.getUniverse() == null || association == null) return;
        Context.getUniverse().removeAssociation(association);
    }

    public String getDefaultAssociationSQL(UnvColumn srcColumn, UnvColumn destColumn) {
        return this.universeService.getDefaultAssociationSQL(srcColumn, destColumn);
    }

    public Metric createMetric(MetricType metricType, Metric parent) {
        Universe unv = Context.getUniverse();
        Metric metric = null;
        if (unv != null) {
            metric = ModelFactory.createMetric(metricType);
            metric.setParent(parent);
            Context.getUniverse().addMetric(metric);
        }
        return metric;
    }

    public void deleteMetric(Metric metric) {
        Universe unv = Context.getUniverse();
        if (unv != null) {
            unv.removeMetric(metric);
        }
    }
}
