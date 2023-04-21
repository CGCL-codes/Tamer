package com.matrixbi.adans.ocore.server.service;

import java.util.List;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.matrixbi.adans.ocore.client.olap.Connection;
import com.matrixbi.adans.ocore.client.ExplorerObject;
import com.matrixbi.adans.ocore.client.report.Report;
import com.matrixbi.adans.ocore.client.service.PersistenceService;
import com.matrixbi.adans.ocore.server.persistence.ConnectionCatalog;
import com.matrixbi.adans.ocore.server.persistence.ObjectCatalog;
import com.matrixbi.adans.ocore.server.persistence.ReportIO;

@SuppressWarnings("serial")
public class PersistenceServiceImpl extends RemoteServiceServlet implements PersistenceService {

    private static ConnectionCatalog connectionCatalog;

    private static ObjectCatalog objectCatalog;

    public static ConnectionCatalog getConnectionCatalog() {
        if (connectionCatalog == null) {
            connectionCatalog = new ConnectionCatalog();
        }
        return connectionCatalog;
    }

    public static ObjectCatalog getObjectCatalog() {
        if (objectCatalog == null) {
            objectCatalog = new ObjectCatalog();
        }
        return objectCatalog;
    }

    public List<Connection> getConnections() {
        return getConnectionCatalog().getConnections();
    }

    public static Connection getConnection(String conex) {
        return getConnectionCatalog().getConnection(conex);
    }

    public ExplorerObject getObjectsInTree() {
        try {
            getObjectCatalog().loadFromXML();
            return getObjectCatalog().getAllTree();
        } catch (Exception e) {
            return null;
        }
    }

    public Report getReport(String id) {
        return ReportIO.getReport(id);
    }

    @Override
    public Boolean saveReport(Report report) {
        return ReportIO.saveReport(report);
    }

    @Override
    public Boolean saveNewReportInObjects(Report report) {
        try {
            ExplorerObject r = new ExplorerObject();
            r.setId(report.getId());
            r.setName(report.getName());
            r.setType("Reports");
            r.setSubtype(report.getType().toString());
            r.setPermission("public");
            System.out.println("d");
            getObjectCatalog().getObject("rFolder").insert(r, 0);
            getObjectCatalog().saveToXML();
            return true;
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Connection getConnectionById(String id) {
        return getConnection(id);
    }
}
