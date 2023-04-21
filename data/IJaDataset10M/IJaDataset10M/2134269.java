package uk.org.ogsadai.client.toolkit.presentation.jersey;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import org.w3c.dom.Document;
import uk.org.ogsadai.client.toolkit.ResourceProperty;
import uk.org.ogsadai.client.toolkit.ResourcePropertyValue;
import uk.org.ogsadai.client.toolkit.TerminationTime;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.ClientServerCompatibilityException;
import uk.org.ogsadai.client.toolkit.exception.PropertyNameInvalidException;
import uk.org.ogsadai.client.toolkit.exception.RemoteServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ResourceNotDestroyedException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.client.toolkit.exception.ServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ServerException;
import uk.org.ogsadai.client.toolkit.exception.TerminationTimeChangeRejectedException;
import uk.org.ogsadai.client.toolkit.exception.UnableToSetTerminationTimeException;
import uk.org.ogsadai.client.toolkit.exception.UnhandledException;
import uk.org.ogsadai.client.toolkit.resource.BaseResource;
import uk.org.ogsadai.client.toolkit.resource.SimpleResourceProperty;
import uk.org.ogsadai.client.toolkit.resource.SimpleResourcePropertyValue;
import uk.org.ogsadai.client.toolkit.resource.SimpleTerminationTime;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourcePropertyName;
import uk.org.ogsadai.resource.ResourcePropertyUnknownException;
import uk.org.ogsadai.resource.ResourceType;
import uk.org.ogsadai.service.rest.drer.jaxb.response.ErrorType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class JerseyResource extends BaseResource {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2011.";

    private String mURL;

    private String mBaseURL;

    private Client mClient;

    /**
     * Creates a new Jersey resource proxy for the resource at the given URL
     * with the given ID.
     * 
     * @param url
     *            URL of the resource service
     * @param resourceID
     *            ID of the resource
     * @param resourceType
     *            type of the resource
     */
    public JerseyResource(URL url, ResourceID resourceID, ResourceType resourceType) {
        super(resourceID, resourceType);
        StringBuilder builder = new StringBuilder();
        mBaseURL = url.toString();
        builder.append(mBaseURL);
        if (!mBaseURL.endsWith("/")) {
            builder.append("/");
        }
        builder.append(resourceID.toString()).append("/");
        mURL = builder.toString();
        initClient();
    }

    /**
     * Creates a new Jersey resource proxy for the resource at the given URL.
     * The URL must include the resource ID.
     * 
     * @param url
     *            resource URL including the resource ID
     * @param resourceType
     *            type of the resource
     */
    public JerseyResource(URL url, ResourceType resourceType) {
        super(JerseyUtilities.getResourceID(url), resourceType);
        mBaseURL = JerseyUtilities.getParent(url).toString();
        mURL = url.toString();
        if (!mURL.endsWith("/")) {
            mURL += ("/");
        }
        initClient();
    }

    private void initClient() {
        DefaultClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(ClientConfig.FEATURE_DISABLE_XML_SECURITY, true);
        mClient = Client.create(config);
    }

    /**
     * Returns the URL of the resource, including the resource ID.
     * 
     * @return full URL
     */
    public String getURL() {
        return mURL;
    }

    /**
     * Returns the resource service URL without the resource ID.
     * 
     * @return service URL
     */
    public String getBaseURL() {
        return mBaseURL;
    }

    /**
     * Returns the Jersey client for configuration.
     * 
     * @return Jersey client
     */
    public Client getClient() {
        return mClient;
    }

    /**
     * Sets the Jersey client if a user wishes to provide their own
     * configuration.
     */
    public void setClient(Client client) {
        mClient = client;
    }

    @Override
    public void setTimeout(int timeout) {
        mClient.setReadTimeout(timeout);
        mClient.setConnectTimeout(timeout);
    }

    @Override
    public void addServerCommsProperty(String propertyName, Object value) {
        mClient.getProperties().put(propertyName, value);
    }

    @Override
    public ResourceProperty getResourceProperty(ResourcePropertyName name) throws ServerCommsException, ServerException, ResourceUnknownException, ClientServerCompatibilityException, PropertyNameInvalidException {
        if (mClient == null) {
            initClient();
        }
        WebResource resource = mClient.resource(mURL + "properties/" + name.getLocalPart());
        try {
            Document response = resource.accept(MediaType.APPLICATION_XML).get(Document.class);
            ResourcePropertyValue value = new SimpleResourcePropertyValue(response.getDocumentElement().getChildNodes());
            ResourceProperty property = new SimpleResourceProperty(name, value);
            return property;
        } catch (ClientHandlerException e) {
            throw new RemoteServerCommsException(mURL, e.getCause());
        } catch (UniformInterfaceException e) {
            try {
                checkException(resource, e);
            } catch (ResourcePropertyUnknownException unknown) {
                throw new PropertyNameInvalidException(name);
            } catch (ClientException clientExc) {
            }
            throw new UnhandledException(e);
        }
    }

    @Override
    public ResourceProperty[] getMultipleResourceProperties(ResourcePropertyName[] names) throws ServerCommsException, ServerException, ClientServerCompatibilityException, ResourceUnknownException, PropertyNameInvalidException {
        ResourceProperty[] result = new ResourceProperty[names.length];
        for (int i = 0; i < names.length; i++) {
            result[i] = getResourceProperty(names[i]);
        }
        return result;
    }

    @Override
    public void destroy() throws ServerCommsException, ServerException, ClientServerCompatibilityException, ResourceUnknownException, ResourceNotDestroyedException {
        if (mClient == null) {
            initClient();
        }
        WebResource resource = mClient.resource(mURL);
        try {
            resource.delete();
        } catch (UniformInterfaceException e) {
            Status status = Status.fromStatusCode(e.getResponse().getStatus());
            if (status == Status.NOT_FOUND) {
                throw new ResourceUnknownException(getResourceID());
            } else {
                throw new UnhandledException(e.getCause());
            }
        }
    }

    @Override
    public TerminationTime setTerminationTime(Date requestedTerminationTime) throws ServerCommsException, ServerException, ClientServerCompatibilityException, ResourceUnknownException, UnableToSetTerminationTimeException, TerminationTimeChangeRejectedException {
        if (mClient == null) {
            initClient();
        }
        WebResource resource = mClient.resource(mURL + "lifetime/terminationtime");
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            String formattedTime = df.format(requestedTerminationTime);
            resource.type(MediaType.TEXT_PLAIN).put(formattedTime);
            Date terminationTime = getTerminationTime();
            return new SimpleTerminationTime(new Date(), terminationTime);
        } catch (UniformInterfaceException e) {
            Status status = Status.fromStatusCode(e.getResponse().getStatus());
            if (status == Status.NOT_FOUND) {
                throw new ResourceUnknownException(getResourceID());
            } else if (status == Status.FORBIDDEN) {
                throw new TerminationTimeChangeRejectedException(this, e.getCause());
            } else {
                throw new UnhandledException(e.getCause());
            }
        }
    }

    private void checkException(WebResource resource, UniformInterfaceException e) throws ResourceUnknownException, ClientException, ServerException, ResourcePropertyUnknownException {
        Status status = Status.fromStatusCode(e.getResponse().getStatus());
        if (status == Status.INTERNAL_SERVER_ERROR) {
            throw new ServerException(ErrorID.SERVER_ERROR_WITH_HOST, new Object[] { resource.getURI().getHost() }, e);
        } else {
            try {
                ErrorType errorType = e.getResponse().getEntity(ErrorType.class);
                JerseyUtilities.handleError(errorType);
            } catch (ClientHandlerException exc) {
            }
            throw new RemoteServerCommsException(resource.getURI().toString(), e);
        }
    }
}
