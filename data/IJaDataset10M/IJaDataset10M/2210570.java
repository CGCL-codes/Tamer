package org.xmi.gui.eclipse.repository.perst;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.xmi.gui.eclipse.IDriverExtension;
import org.xmi.gui.eclipse.XMIPluginActivator;
import org.xmi.gui.eclipse.preferences.PreferenceConstants;
import org.xmi.infoset.Difference;
import org.xmi.infoset.ElementProperty;
import org.xmi.infoset.XMIDocument;
import org.xmi.infoset.XMIElement;
import org.xmi.repository.BasicRepositoryDataSource;
import org.xmi.repository.ModelRepositoryDAOImpl;
import org.xmi.repository.ModelStatistic;
import org.xmi.repository.PooledRepositoryDataSource;
import org.xmi.repository.RepositoryDAO;
import org.xmi.repository.RepositoryDataSource;
import org.xmi.repository.RepositoryDriverEnumeration;
import org.xmi.repository.RepositoryException;
import org.xmi.repository.cache.EHCacheManager;

public class PerstDriverExtension implements IDriverExtension {

    private RepositoryDAO dao;

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getClassName() {
        return RepositoryDriverEnumeration.PERST.getName();
    }

    public void initDriver(String path, Properties properties) throws IOException, RepositoryException {
        Properties props = new Properties();
        String datasourceName = XMIPluginActivator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_DATASOURCE);
        RepositoryDataSource source = null;
        if (BasicRepositoryDataSource.class.getName().equals(datasourceName)) {
            source = new BasicRepositoryDataSource("PERST", path + "/.perst", props);
        } else if (PooledRepositoryDataSource.class.getName().equals(datasourceName)) {
            source = new PooledRepositoryDataSource("PERST", path + "/.perst", props);
        } else {
            throw new RuntimeException("data source type not found. (" + datasourceName + ")");
        }
        if (Boolean.parseBoolean(XMIPluginActivator.getDefault().getPreferenceStore().getString(PreferenceConstants.REPOSITORY_CACHE))) {
            source.setCacheManager(new EHCacheManager());
        }
        dao = new ModelRepositoryDAOImpl(source);
        dao.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fireEvents(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });
    }

    public String createExtension(String arg0, String arg1, String name, String namespace, String arg2, String arg3) throws IOException, RepositoryException {
        return dao.createExtension(arg0, arg1, name, namespace, arg2, arg3);
    }

    public String createInstance(String arg0, XMIElement arg1, XMIElement arg2, String arg3, String arg4) throws IOException, RepositoryException {
        return dao.createInstance(arg0, arg1, arg2, arg3, arg4);
    }

    public void createModel(float arg0, String arg1, String arg2, String arg3, String arg4, String arg5, Map<String, String> arg6) throws IOException, RepositoryException {
        dao.createModel(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public void deleteModel(String arg0) throws RepositoryException, IOException {
        dao.deleteModel(arg0);
    }

    public List<XMIElement> evalModelPath(String arg0, String arg1) {
        return dao.evalModelPath(arg0, arg1);
    }

    public XMIDocument getModel(String arg0, int arg1) {
        return dao.getModel(arg0, arg1);
    }

    public XMIElement getModelElement(String arg0, String arg1, int arg2) {
        return dao.getModelElement(arg0, arg1, arg2);
    }

    public Collection<String> getModelNames() throws RepositoryException, IOException {
        return dao.getModelNames();
    }

    public RepositoryDataSource getRepositoryDataSource() {
        return dao.getRepositoryDataSource();
    }

    public ModelStatistic getStatistic(String arg0) {
        return dao.getStatistic(arg0);
    }

    public void importModel(String arg0, XMIDocument arg1) throws RepositoryException, IOException {
        dao.importModel(arg0, arg1);
    }

    public void save(String arg0, ElementProperty arg1) {
        dao.save(arg0, arg1);
    }

    public void save(String arg0, XMIDocument arg1) {
        dao.save(arg0, arg1);
    }

    public void save(String arg0, XMIElement arg1) {
        dao.save(arg0, arg1);
    }

    public Collection<Difference> getModelDiff(String arg0, int arg1, int arg2) throws IOException, RepositoryException {
        return dao.getModelDiff(arg0, arg1, arg2);
    }

    public void patchModel(String arg0, Collection<Difference> arg1) throws IOException, RepositoryException {
        dao.patchModel(arg0, arg1);
    }

    public void setDriver(RepositoryDataSource arg0) {
        dao.setDriver(arg0);
    }

    /** and the change listener support **/
    PropertyChangeSupport listenerList = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerList.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listenerList.removePropertyChangeListener(listener);
    }

    public void fireEvents(String propertyName, Object oldValue, Object newValue) {
        listenerList.firePropertyChange(propertyName, oldValue, newValue);
    }
}
