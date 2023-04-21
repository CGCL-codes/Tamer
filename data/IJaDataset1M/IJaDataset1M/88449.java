package com.koutra.dist.proc.designer.model.faucet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.commons.dbcp.BasicDataSource;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import com.koutra.dist.proc.designer.Messages;
import com.koutra.dist.proc.designer.model.Connection;
import com.koutra.dist.proc.designer.model.ContentType;
import com.koutra.dist.proc.designer.model.Faucet;
import com.koutra.dist.proc.designer.model.ModelElement;
import com.koutra.dist.proc.designer.model.ValidationError;
import com.koutra.dist.proc.model.ISink;

public class ResultSetFaucet extends Faucet {

    public static final String TAG_QUERY = "query";

    public static final String TAG_DRIVER_CLASS_NAME = "driverClassName";

    public static final String TAG_USER_NAME = "username";

    public static final String TAG_PASSWORD = "password";

    public static final String TAG_JDBC_URI = "jsbcUri";

    /** 
	 * A static array of property descriptors.
	 * There is one IPropertyDescriptor entry per editable property.
	 * @see #getPropertyDescriptors()
	 * @see #getPropertyValue(Object)
	 * @see #setPropertyValue(Object, Object)
	 */
    private static IPropertyDescriptor[] descriptors;

    /** Property ID to use for the source path. */
    public static final String QUERY = "ResultSetFaucet.Query";

    /** Property ID to use for the driver class name. */
    public static final String DRIVER_CLASS_NAME = "ResultSetFaucet.DriverClassName";

    /** Property ID to use for the user name. */
    public static final String USER_NAME = "ResultSetFaucet.UserName";

    /** Property ID to use for the password. */
    public static final String PASSWORD = "ResultSetFaucet.Password";

    /** Property ID to use for the JDBC uri. */
    public static final String JDBC_URI = "ResultSetFaucet.JDBCUri";

    static {
        descriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor(QUERY, Messages.ResultSetFaucet_QueryPropertyDescription), new TextPropertyDescriptor(DRIVER_CLASS_NAME, Messages.ResultSetFaucet_DriverClassNamePropertyDescription), new TextPropertyDescriptor(USER_NAME, Messages.ResultSetFaucet_UsernamePropertyDescription), new TextPropertyDescriptor(PASSWORD, Messages.ResultSetFaucet_PasswordPropertyDescription), new TextPropertyDescriptor(JDBC_URI, Messages.ResultSetFaucet_JDBCUriPropertyDescription) };
        for (int i = 0; i < descriptors.length; i++) {
            if (descriptors[i].getId().equals(QUERY)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        return null;
                    }
                });
            } else if (descriptors[i].getId().equals(DRIVER_CLASS_NAME)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        try {
                            Class<?> clazz = Class.forName((String) value);
                            if (clazz == null) return Messages.ResultSetFaucet_ClassLoadValidationError1 + value;
                        } catch (Exception e) {
                            return Messages.ResultSetFaucet_ClassLoadValidationError2 + value;
                        }
                        return null;
                    }
                });
            } else if (descriptors[i].getId().equals(USER_NAME)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        return null;
                    }
                });
            } else if (descriptors[i].getId().equals(PASSWORD)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        return null;
                    }
                });
            } else if (descriptors[i].getId().equals(JDBC_URI)) {
                ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {

                    public String isValid(Object value) {
                        return null;
                    }
                });
            }
        }
    }

    protected String query;

    protected String driverClassName;

    protected String username;

    protected String password;

    protected String jdbcUri;

    public ResultSetFaucet() {
        query = "";
        driverClassName = "";
        username = "";
        password = "";
        jdbcUri = "";
    }

    @Override
    public ModelElement deepCopy() {
        ResultSetFaucet retVal = new ResultSetFaucet();
        retVal.query = query;
        if (connection != null && connection.getSink() != null) {
            retVal.connection = (Connection) connection.deepCopy();
            retVal.connection.connect(retVal, connection.getSink());
        }
        return retVal;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<IPropertyDescriptor> superList = new ArrayList<IPropertyDescriptor>(Arrays.asList(super.getPropertyDescriptors()));
        superList.addAll(Arrays.asList(descriptors));
        IPropertyDescriptor[] retVal = new IPropertyDescriptor[superList.size()];
        int counter = 0;
        for (IPropertyDescriptor pd : superList) {
            retVal[counter++] = pd;
        }
        return retVal;
    }

    /**
	 * Return the property value for the given propertyId, or null.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array 
	 * to obtain the value of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
    public Object getPropertyValue(Object propertyId) {
        if (QUERY.equals(propertyId)) {
            return query;
        } else if (DRIVER_CLASS_NAME.equals(propertyId)) {
            return driverClassName;
        } else if (USER_NAME.equals(propertyId)) {
            return username;
        } else if (PASSWORD.equals(propertyId)) {
            return password;
        } else if (JDBC_URI.equals(propertyId)) {
            return jdbcUri;
        } else {
            return super.getPropertyValue(propertyId);
        }
    }

    /**
	 * Set the property value for the given property id.
	 * If no matching id is found, the call is forwarded to the superclass.
	 * <p>The property view uses the IDs from the IPropertyDescriptors array to set the values
	 * of the corresponding properties.</p>
	 * @see #descriptors
	 * @see #getPropertyDescriptors()
	 */
    public void setPropertyValue(Object propertyId, Object value) {
        if (QUERY.equals(propertyId)) {
            String oldValue = query;
            query = (String) value;
            firePropertyChange(QUERY, oldValue, value);
        } else if (DRIVER_CLASS_NAME.equals(propertyId)) {
            String oldValue = driverClassName;
            driverClassName = (String) value;
            firePropertyChange(DRIVER_CLASS_NAME, oldValue, value);
        } else if (USER_NAME.equals(propertyId)) {
            String oldValue = username;
            username = (String) value;
            firePropertyChange(USER_NAME, oldValue, value);
        } else if (PASSWORD.equals(propertyId)) {
            String oldValue = password;
            password = (String) value;
            firePropertyChange(PASSWORD, oldValue, value);
        } else if (JDBC_URI.equals(propertyId)) {
            String oldValue = jdbcUri;
            jdbcUri = (String) value;
            firePropertyChange(jdbcUri, oldValue, value);
        } else {
            super.setPropertyValue(propertyId, value);
        }
    }

    @Override
    public boolean supportsOutput(ContentType contentType) {
        switch(contentType) {
            case ResultSet:
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ResultSetFaucet[" + query + "]";
    }

    protected com.koutra.dist.proc.faucet.ResultSetFaucet representation;

    @Override
    public void setUpExecution() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(jdbcUri);
        representation = new com.koutra.dist.proc.faucet.ResultSetFaucet(UUID.randomUUID().toString(), ds, query);
        ModelElement sink = (ModelElement) getSinkConnection().getSink();
        sink.setUpExecution();
        ISink sinkRepresentation = (ISink) sink.getExecutionRepresentation();
        representation.hookupSink(sinkRepresentation);
    }

    @Override
    public Object getExecutionRepresentation() {
        if (representation == null) setUpExecution();
        return representation;
    }

    @Override
    public void dehydrate(IMemento memento) {
        memento.putString(TAG_QUERY, query);
        memento.putString(TAG_DRIVER_CLASS_NAME, driverClassName);
        memento.putString(TAG_USER_NAME, username);
        memento.putString(TAG_PASSWORD, password);
        memento.putString(TAG_JDBC_URI, jdbcUri);
        super.dehydrate(memento);
    }

    @Override
    public void hydrate(IMemento memento) {
        query = memento.getString(TAG_QUERY);
        driverClassName = memento.getString(TAG_DRIVER_CLASS_NAME);
        username = memento.getString(TAG_USER_NAME);
        password = memento.getString(TAG_PASSWORD);
        jdbcUri = memento.getString(TAG_JDBC_URI);
        super.hydrate(memento);
    }

    @Override
    public List<ValidationError> validate() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(jdbcUri);
        try {
            ds.getConnection();
        } catch (SQLException e) {
            return Collections.singletonList(new ValidationError(this, Messages.ResultSetFaucet_ConnectValidationMessage));
        }
        return Collections.emptyList();
    }
}
