package org.monet.kernel.producers;

import java.net.URI;
import java.sql.ResultSet;
import java.util.EventObject;
import java.util.HashMap;
import org.monet.kernel.constants.Database;
import org.monet.kernel.constants.ErrorCode;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.model.MonetEvent;
import org.monet.kernel.model.ServiceProvider;
import org.monet.kernel.model.Task;
import org.monet.kernel.model.definition.ServiceProviderDefinition;

public class ProducerServiceLink extends Producer {

    public ProducerServiceLink() {
        super();
    }

    public ServiceProvider load(String id) {
        ServiceProvider serviceLink;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        ResultSet resultSet;
        parameters.put(Database.QueryFields.ID, id);
        resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.SERVICE_LINK_LOAD, parameters);
        try {
            resultSet.next();
            serviceLink = new ServiceProvider();
            serviceLink.setId(resultSet.getString("id"));
            serviceLink.setCode(resultSet.getString("code"));
            serviceLink.setLabel(resultSet.getString("label"));
            serviceLink.setUsername(resultSet.getString("username"));
            serviceLink.setPassword(resultSet.getString("password"));
            URI serviceUri = null;
            String serviceUriLiteral = resultSet.getString("service_url");
            if (serviceUriLiteral != null) serviceUri = URI.create(serviceUriLiteral);
            serviceLink.setServiceUri(serviceUri);
            ServiceProviderDefinition definition = this.getDictionary().getServiceProviderDefinition(serviceLink.getCode());
            serviceLink.setDefinition(definition);
        } catch (Exception oException) {
            throw new DataException(ErrorCode.LOAD_SERVICELINK, id, oException);
        } finally {
            this.agentDatabase.closeQuery(resultSet);
        }
        return serviceLink;
    }

    public void save(ServiceProvider serviceLink) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, serviceLink.getId());
        parameters.put(Database.QueryFields.CODE, serviceLink.getCode());
        parameters.put(Database.QueryFields.LABEL, serviceLink.getLabel());
        parameters.put(Database.QueryFields.USERNAME, serviceLink.getUsername());
        parameters.put(Database.QueryFields.PASSWORD, serviceLink.getPassword());
        String serviceUri = null;
        if (serviceLink.getServiceUri() != null) serviceUri = serviceLink.getServiceUri().toString();
        parameters.put(Database.QueryFields.SERVICE_URL, serviceUri);
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SERVICE_LINK_SAVE, parameters);
    }

    public ServiceProvider create(ServiceProvider serviceLink) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        ServiceProviderDefinition definition = this.getDictionary().getServiceProviderDefinition(serviceLink.getCode());
        serviceLink.setDefinition(definition);
        parameters.put(Database.QueryFields.CODE, serviceLink.getCode());
        parameters.put(Database.QueryFields.LABEL, serviceLink.getLabel());
        parameters.put(Database.QueryFields.USERNAME, serviceLink.getUsername());
        parameters.put(Database.QueryFields.PASSWORD, serviceLink.getPassword());
        String serviceUri = null;
        if (serviceLink.getServiceUri() != null) serviceUri = serviceLink.getServiceUri().toString();
        parameters.put(Database.QueryFields.SERVICE_URL, serviceUri);
        String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.SERVICE_LINK_CREATE, parameters);
        serviceLink.setId(id);
        this.oAgentNotifier.notify(new MonetEvent(MonetEvent.SERVICE_LINK_CREATED, null, serviceLink));
        return serviceLink;
    }

    public void delete(String id) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, id);
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.SERVICE_LINK_REMOVE, parameters);
    }

    public Object newObject() {
        return new Task();
    }

    @Override
    public void loadAttribute(EventObject oEventObject, String sAttribute) {
    }
}
