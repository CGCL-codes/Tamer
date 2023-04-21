package ch.mypics.wtt.ejb.services;

import java.util.Collection;
import java.util.Map;
import javax.ejb.EJBLocalObject;
import ch.mypics.wtt.beans.Bean;

/**
 * 
 * @author <a href="mailto:mhaessig@okta.ch">Mathias H&auml;ssig</a>
 */
public interface EntityBeanAdapterLocal extends EJBLocalObject {

    public Collection getBeans(String table);

    public Collection getBeans(String table, Map search);

    public void save(Bean bean);

    public void create(Bean bean);

    public void delete(Bean bean);
}
