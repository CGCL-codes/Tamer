package com.mockturtlesolutions.snifflib.mcmctools.database;

import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorage;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryMaintenance;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorageNameQuery;
import com.mockturtlesolutions.snifflib.reposconfig.database.ReposConfig;
import com.mockturtlesolutions.snifflib.statmodeltools.database.StatisticalModelStorage;
import java.util.List;
import com.mockturtlesolutions.snifflib.reposconfig.database.*;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryElement;
import com.mockturtlesolutions.snifflib.mcmctools.workbench.MCMCTraceFrame;

public class MCMCTraceXML extends RepositoryStorageXML implements MCMCTraceStorage {

    public MCMCTraceXML(RepositoryConnectivity conn, String repos) {
        super(conn, repos);
    }

    public Class getDefaultGraphicalEditorClass() {
        return (MCMCTraceFrame.class);
    }

    public Class getStorageTransferAgentClass() {
        return (MCMCTraceTransferAgent.class);
    }

    public Class getDOMStorageClass() {
        return (MCMCTraceDOM.class);
    }

    public void addElementIntoTrace(int j, RepositoryElement elem) {
        ((MCMCTraceDOM) this.getDOM()).addElementIntoTrace(j, elem);
        this.writeXML(this.getXMLFilename());
    }

    public boolean addElementToTrace(RepositoryElement elem) {
        boolean out = ((MCMCTraceDOM) this.getDOM()).addElementToTrace(elem);
        this.writeXML(this.getXMLFilename());
        return (out);
    }

    public void clearTrace() {
        ((MCMCTraceDOM) this.getDOM()).clearTrace();
        this.writeXML(this.getXMLFilename());
    }

    public RepositoryElement getElement(int j) {
        return (((MCMCTraceDOM) this.getDOM()).getElement(j));
    }

    public RepositoryElement removeElement(int k) {
        RepositoryElement out = ((MCMCTraceDOM) this.getDOM()).removeElement(k);
        this.writeXML(this.getXMLFilename());
        return (out);
    }

    public int sizeOfTrace() {
        return (((MCMCTraceDOM) this.getDOM()).sizeOfTrace());
    }

    public RepositoryElement setElement(int k, RepositoryElement elem) {
        RepositoryElement out = ((MCMCTraceDOM) this.getDOM()).setElement(k, elem);
        this.writeXML(this.getXMLFilename());
        return (out);
    }
}
