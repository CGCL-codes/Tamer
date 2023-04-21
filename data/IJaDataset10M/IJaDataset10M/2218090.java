package sourceforge.pebblesframewor.base.datasource;

import java.sql.*;
import sourceforge.pebblesframewor.api.component.AttributeEnvelope;
import sourceforge.pebblesframewor.api.component.Component;
import sourceforge.pebblesframewor.api.framework.Context;
import sourceforge.pebblesframewor.api.component.*;
import sourceforge.pebblesframewor.api.component.Attribute.ElementType;
import sourceforge.pebblesframewor.base.component.*;
import sourceforge.pebblesframewor.util.PebblesException;

/**
 * JdbcDsFactory:
 * @author JunSun Whang
 * @version $Id: JdbcDsFactory.java 109 2009-03-22 17:19:30Z junsunwhang $
 */
public class JdbcDsFactory implements ComponentFactory {

    public JdbcDsFactory() {
    }

    public Component createActive(Context context, AttributeEnvelope envelope) throws PebblesException {
        return new JdbcDataSource(envelope, true);
    }

    public Component createInert(Context context, AttributeEnvelope envelope) throws PebblesException {
        return new JdbcDataSource(envelope, false);
    }

    public void activateConnection(Context context, DataSource dataSource) throws PebblesException {
        dataSource.populateConnection();
    }

    public AttributeEnvelope getDefaultAttributeEnvelope() {
        AttributeEnvelope envelope = new AttributeEnvelopeImpl();
        Attribute jdbcUrl = new AttributeImpl("jdbcUrl", "Jdbc URL", "JDBC URL used to connect to datasource", ElementType.String, true);
        Attribute username = new AttributeImpl("username", "User name", "Name of user to connect as.", ElementType.String, true);
        Attribute password = new AttributeImpl("password", "Pass word", "Password for the database connection.", ElementType.String, true);
        Attribute connection = new AttributeImpl("connection", "Jdbc connection", "Connection object.", ElementType.Object, false);
        envelope.addElement(jdbcUrl);
        envelope.addElement(username);
        envelope.addElement(password);
        envelope.addElement(connection);
        return envelope;
    }
}
