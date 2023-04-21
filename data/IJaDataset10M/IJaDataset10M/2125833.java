package com.tensegrity.palobrowser.dbtree;

import java.text.NumberFormat;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.palo.api.Connection;

/**
 * <code>ConnectionPropertySource</code>
 * 
 * @author Stepan Rutz
 * @version $ID$
 */
public class ConnectionPropertySource implements IPropertySource {

    private static final String PROPERTY_NAME = ConnectionPropertySource.class.getName() + ".name", PROPERTY_TYPE = ConnectionPropertySource.class.getName() + ".type", PROPERTY_SERVER = ConnectionPropertySource.class.getName() + ".server", PROPERTY_SERVICE = ConnectionPropertySource.class.getName() + ".service", PROPERTY_USERNAME = ConnectionPropertySource.class.getName() + ".username", PROPERTY_DATABASECOUNT = ConnectionPropertySource.class.getName() + ".databasecount";

    private Connection connection;

    private IPropertyDescriptor[] propertyDescriptors;

    public ConnectionPropertySource(Connection connection) {
        this.connection = connection;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (propertyDescriptors == null) {
            PropertyDescriptor nameDescriptor = new PropertyDescriptor(PROPERTY_NAME, DbTreeMessages.getString("ConnectionPropertySource.Name"));
            nameDescriptor.setCategory(DbTreeMessages.getString("ConnectionPropertySource.Global"));
            PropertyDescriptor typeDescriptor = new PropertyDescriptor(PROPERTY_TYPE, DbTreeMessages.getString("ConnectionPropertySource.ObjectType"));
            typeDescriptor.setCategory(DbTreeMessages.getString("ConnectionPropertySource.Global"));
            PropertyDescriptor serverDescriptor = new PropertyDescriptor(PROPERTY_SERVER, DbTreeMessages.getString("ConnectionPropertySource.Server"));
            serverDescriptor.setCategory(DbTreeMessages.getString("ConnectionPropertySource.Connection"));
            PropertyDescriptor serviceDescriptor = new PropertyDescriptor(PROPERTY_SERVICE, DbTreeMessages.getString("ConnectionPropertySource.Service"));
            serviceDescriptor.setCategory(DbTreeMessages.getString("ConnectionPropertySource.Connection"));
            PropertyDescriptor usernameDescriptor = new PropertyDescriptor(PROPERTY_USERNAME, DbTreeMessages.getString("ConnectionPropertySource.Username"));
            usernameDescriptor.setCategory(DbTreeMessages.getString("ConnectionPropertySource.Connection"));
            PropertyDescriptor databaseCountDescriptor = new PropertyDescriptor(PROPERTY_DATABASECOUNT, DbTreeMessages.getString("ConnectionPropertySource.NumberOfDatabases"));
            databaseCountDescriptor.setCategory(DbTreeMessages.getString("ConnectionPropertySource.Information"));
            propertyDescriptors = new IPropertyDescriptor[] { nameDescriptor, serverDescriptor, serviceDescriptor, usernameDescriptor, databaseCountDescriptor };
        }
        return propertyDescriptors;
    }

    public boolean isPropertySet(Object id) {
        return false;
    }

    public void setPropertyValue(Object id, Object value) {
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(PROPERTY_NAME)) return "Connection@" + System.identityHashCode(connection); else if (id.equals(PROPERTY_TYPE)) return connection.getClass().getName(); else if (id.equals(PROPERTY_SERVER)) return connection.getServer(); else if (id.equals(PROPERTY_SERVICE)) return connection.getService(); else if (id.equals(PROPERTY_USERNAME)) return connection.getUsername(); else if (id.equals(PROPERTY_DATABASECOUNT)) return NumberFormat.getIntegerInstance().format(connection.getDatabaseCount());
        return null;
    }

    public void resetPropertyValue(Object id) {
    }

    public Object getEditableValue() {
        return null;
    }
}
