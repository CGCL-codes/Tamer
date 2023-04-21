package com.ewaloo.impl.cms.loader.ptl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.portlet.Portlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.atlantal.api.cache.CacheException;
import org.atlantal.api.cache.CacheObject;
import org.atlantal.api.portal.app.ptl.PortletManager;
import org.atlantal.api.portal.ptl.Layout;
import org.atlantal.impl.portal.descriptor.PortletDD;
import org.atlantal.impl.portal.descriptor.PortletInfoDD;
import org.atlantal.impl.portal.descriptor.PortletPreferencesDD;
import org.atlantal.impl.portal.descriptor.SupportsDD;
import org.atlantal.impl.portal.portlet.PortletConfigImpl;
import org.atlantal.impl.portal.ptl.JSR168PortletInstance;
import org.atlantal.impl.portal.ptl.PortletInstance;
import org.atlantal.utils.AtlantalException;
import org.atlantal.utils.session.Session;
import com.ewaloo.impl.cache.loader.EwSQLCacheLoader;

/**
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 */
public class MySQLPortletLoader extends EwSQLCacheLoader {

    private static final Logger LOGGER = Logger.getLogger(MySQLPortletLoader.class);

    static {
        LOGGER.setLevel(Level.INFO);
    }

    private static final String SQL_GET_PORTLET = "SELECT portlet.*, portlet_type.class AS class" + " FROM portlet INNER JOIN portlet_type" + " ON portlet.id_type = portlet_type.id";

    private static final String SQL_EXIST_PORTLET = "SELECT portlet.id_object, portlet.name" + " FROM portlet WHERE";

    /**
     * {@inheritDoc}
     */
    public void init() throws CacheException {
        super.init();
        super.setMultiValues(false);
    }

    /**
     * @param id id
     * @return string
     */
    protected String getSQLRead(Object id) {
        return SQL_GET_PORTLET + " WHERE" + " portlet.id_object = '" + id + "'" + " OR portlet.name = '" + id + "'";
    }

    /**
     * @param id id
     * @return string
     */
    protected String getSQLExist(Object id) {
        return SQL_EXIST_PORTLET + " portlet.id_object = '" + id + "'" + " OR portlet.name = '" + id + "'";
    }

    /**
     * {@inheritDoc}
     */
    protected void exists(CacheObject handle, ResultSet rs) throws SQLException {
        Integer id = Integer.valueOf(rs.getInt("id_object"));
        handle.setName(id);
        handle.setLoadArguments(id);
        handle.setAlias(rs.getString("name"));
    }

    /**
     * {@inheritDoc}
     */
    protected Object read(Session session, CacheObject handle, ResultSet rs) throws CacheException {
        try {
            Integer id = Integer.valueOf(rs.getInt("id_object"));
            String name = rs.getString("name");
            String title = rs.getString("title");
            String portletClass = rs.getString("class");
            PortletManager portletmanager = (PortletManager) getService(handle);
            Class c = portletmanager.loadClass(portletClass);
            Object portletObject = c.newInstance();
            PortletInstance portlet;
            if (!(portletObject instanceof PortletInstance)) {
                portlet = new JSR168PortletInstance((Portlet) portletObject);
            } else {
                portlet = (PortletInstance) portletObject;
            }
            portlet.setPortletManager(portletmanager);
            portlet.setId(id);
            portlet.setName(name);
            portlet.setTitle(title);
            portlet.setWidth(rs.getString("width"));
            portlet.setStyle(rs.getString("style"));
            portlet.setScripts(rs.getString("scripts"));
            portlet.setTemplate(rs.getString("template"));
            PortletDD def = new PortletDD();
            def.setPortletClass(portletClass);
            PortletInfoDD info = new PortletInfoDD();
            info.setTitle(title);
            info.setShortTitle("");
            info.setKeywords("");
            def.setPortletInfo(info);
            def.setPortletName(name);
            def.setPortletPreferences(new PortletPreferencesDD());
            def.setResourceBundle(null);
            def.setSecurityRoleRefs(new ArrayList());
            List supports = new ArrayList();
            SupportsDD support;
            support = new SupportsDD();
            support.setMimeType("text/plain");
            List modes = new ArrayList();
            modes.add("view");
            support.setPortletModes(modes);
            supports.add(support);
            support = new SupportsDD();
            support.setMimeType("text/html");
            modes.add("view");
            modes.add("edit");
            modes.add("help");
            modes.add("config");
            support.setPortletModes(modes);
            supports.add(support);
            def.setSupports(supports);
            portlet.setPortletDefinition(def);
            handle.setName(id);
            handle.setLoadArguments(id);
            CacheObject portletParams = handle.get(id + "params", id, MySQLPortletParamsLoader.class);
            portletParams.addDependent(handle);
            handle.addDependent(portletParams);
            Map params = (Map) portletParams.getWrappedObject(session);
            def.setInitParams(params);
            if (portlet instanceof Layout) {
                Layout layout = (Layout) portlet;
                CacheObject portletChildren = handle.get(id + "children", id, MySQLPortletChildrenLoader.class);
                portletChildren.setLoadArguments(id);
                portletChildren.addDependent(handle);
                handle.addDependent(portletChildren);
                layout.setChildren(portletChildren);
            }
            PortletContext context = portletmanager.getPortletContext(null);
            PortletConfig config = new PortletConfigImpl(context, def);
            portlet.init(config);
            portlet.init();
            return portlet;
        } catch (IllegalAccessException e) {
            throw new CacheException(e);
        } catch (InstantiationException e) {
            throw new CacheException(e);
        } catch (SQLException e) {
            throw new CacheException(e);
        } catch (PortletException e) {
            throw new CacheException(e);
        } catch (AtlantalException e) {
            throw new CacheException(e);
        }
    }
}
