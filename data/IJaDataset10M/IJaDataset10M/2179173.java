package org.jumpmind.symmetric.service.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.jumpmind.symmetric.load.IBatchListener;
import org.jumpmind.symmetric.load.IColumnFilter;
import org.jumpmind.symmetric.load.IDataLoader;
import org.jumpmind.symmetric.load.IDataLoaderFilter;
import org.jumpmind.symmetric.load.IDataLoaderStatistics;
import org.jumpmind.symmetric.model.Node;
import org.jumpmind.symmetric.service.IDataLoaderService;
import org.jumpmind.symmetric.transport.IIncomingTransport;
import org.jumpmind.symmetric.transport.ITransportManager;

public class MockDataLoaderService implements IDataLoaderService {

    public void addColumnFilter(String tableName, IColumnFilter filter) {
    }

    public IDataLoaderStatistics loadDataBatch(String batchData) throws IOException {
        return null;
    }

    public IDataLoader openDataLoader(BufferedReader reader) throws IOException {
        return null;
    }

    public void addDataLoaderFilter(IDataLoaderFilter filter) {
    }

    public boolean loadData(Node remote, Node local) throws IOException {
        return true;
    }

    public boolean loadData(IIncomingTransport reader) {
        return false;
    }

    public void loadData(InputStream in, OutputStream out) throws IOException {
    }

    public void removeDataLoaderFilter(IDataLoaderFilter filter) {
    }

    public void setDataLoaderFilters(List<IDataLoaderFilter> filters) {
    }

    public void setTransportManager(ITransportManager transportManager) {
    }

    public void addBatchListener(IBatchListener listener) {
    }
}
