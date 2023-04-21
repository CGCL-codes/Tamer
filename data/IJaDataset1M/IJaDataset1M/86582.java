package org.objectwiz.mock;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.management.Notification;
import org.objectwiz.Application;
import org.objectwiz.PersistenceUnit;
import org.objectwiz.metadata.BusinessBean;
import org.objectwiz.spi.server.ServerAdapter;

/**
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class MockServerAdapter extends ServerAdapter {

    private Map<String, Map<String, BusinessBean>> beans = new HashMap();

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getDeployDir() throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getDataDir() throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object resolveBean(BusinessBean bean) throws Exception {
        try {
            return Class.forName(bean.getBeanFullReference()).newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to resolve bean based on class name", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while instanciating bean", e);
        }
    }

    @Override
    public Set<String> getUserRoles(Principal principal) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<PersistenceUnit> discoverPersistenceUnits(Application application) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> Map<String, T> discoverBeans(Class<T> intf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, BusinessBean> discoverBusinessBeans(Application application) throws Exception {
        return beans.get(application.getName());
    }

    @Override
    public void handleJMXNotification(Notification notification) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
