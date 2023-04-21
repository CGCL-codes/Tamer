package uk.org.ogsadai.client.toolkit.presentation.gt;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.Stub;
import org.apache.axis.AxisFault;
import org.apache.axis.message.MessageElement;
import org.globus.axis.message.addressing.AddressingHeaders;
import org.globus.axis.message.addressing.Constants;
import org.globus.axis.message.addressing.To;
import org.apache.axis.types.URI.MalformedURIException;
import org.oasis.wsrf.faults.BaseFaultTypeDescription;
import org.oasis.wsrf.lifetime.Destroy;
import org.oasis.wsrf.lifetime.SetTerminationTime;
import org.oasis.wsrf.lifetime.SetTerminationTimeResponse;
import org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse;
import org.oasis.wsrf.properties.GetMultipleResourceProperties_Element;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import uk.org.ogsadai.client.toolkit.Resource;
import uk.org.ogsadai.client.toolkit.ResourceProperty;
import uk.org.ogsadai.client.toolkit.ResourcePropertyValue;
import uk.org.ogsadai.client.toolkit.TerminationTime;
import uk.org.ogsadai.client.toolkit.exception.AxisServerCommsException;
import uk.org.ogsadai.client.toolkit.exception.ClientServerCompatibilityException;
import uk.org.ogsadai.client.toolkit.exception.ClientToolkitException;
import uk.org.ogsadai.client.toolkit.exception.InvalidURIException;
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
import uk.org.ogsadai.resource.ResourceType;
import uk.org.ogsadai.service.gt.dataresource.GTDataResourceInformationServicePortType;
import uk.org.ogsadai.service.gt.dataresource.service.DataResourceInformationServiceLocator;

/**
 * OGSA-DAI GT client-side resource proxy.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class GTResource extends BaseResource implements Resource {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007-2008";

    /** The URL. */
    private URL mURL;

    /** The stub. */
    private GTDataResourceInformationServicePortType mStub = null;

    /** The sub to the specific port typed used by sub-classes. */
    private Stub mSpecificStub = null;

    /**
     * Generate headers.
     * 
     * @param resourceID
     *            the resource ID.
     * @param url
     *            the URL.
     * @return the addressing headers     
     * @throws InvalidURIException
     *             the invalid URI exception
     */
    public static AddressingHeaders generateHeaders(final String url, final ResourceID resourceID) throws InvalidURIException {
        try {
            AddressingHeaders headers = new AddressingHeaders();
            headers.setTo(new To(url));
            MessageElement key = new MessageElement(uk.org.ogsadai.common.Constants.RESOURCE_ID_KEY_NS, uk.org.ogsadai.common.Constants.RESOURCE_ID_KEY, new QName(resourceID.toString()));
            headers.addReferenceParameter(key);
            return headers;
        } catch (final MalformedURIException e) {
            throw new InvalidURIException(url);
        }
    }

    /**
     * Constructor.
     * 
     * @param resourceID
     *            the resource ID.
     * @param url
     *            the URL.
     * @param resourceType
     *            the resource type.
     * @throws ServerException
     *             the server exception.
     * @throws ClientToolkitException
     *             the client toolkit exception.
     */
    public GTResource(final URL url, final ResourceID resourceID, final ResourceType resourceType) throws ServerException, ClientToolkitException {
        super(resourceID, resourceType);
        try {
            mURL = url;
            final DataResourceInformationServiceLocator locator = new DataResourceInformationServiceLocator();
            mStub = locator.getGTDataResourceInformationServicePortTypePort(mURL);
            AddressingHeaders headers = GTResource.generateHeaders(getURLString(), resourceID);
            ((Stub) mStub)._setProperty(Constants.ENV_ADDRESSING_SHARED_HEADERS, headers);
        } catch (final ServiceException e) {
            throw new ServerException(ErrorID.CTK_FAILED_TO_GENERATE_RESOURCE_STUBS, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResourceProperty getResourceProperty(final ResourcePropertyName name) throws ServerCommsException, ServerException, ResourceUnknownException, PropertyNameInvalidException, ClientServerCompatibilityException {
        try {
            final QName property = new QName(name.getNamespace(), name.getLocalPart());
            final GetResourcePropertyResponse grpr = mStub.getResourceProperty(property);
            MessageElement[] messageElements = grpr.get_any();
            ResourcePropertyValue[] values = new ResourcePropertyValue[messageElements.length];
            for (int i = 0; i < messageElements.length; ++i) {
                values[i] = new SimpleResourcePropertyValue(messageElements[i].getChildNodes());
            }
            return new SimpleResourceProperty(name, values);
        } catch (final Exception ex) {
            mapPropertiesExcpetion(ex);
            mapExcpetion(ex);
            throw new UnhandledException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResourceProperty[] getMultipleResourceProperties(final ResourcePropertyName[] names) throws ServerCommsException, ServerException, ResourceUnknownException, PropertyNameInvalidException, ClientServerCompatibilityException {
        try {
            final QName[] properties = new QName[names.length];
            for (int i = 0; i < names.length; i++) {
                properties[i] = new QName(names[i].getNamespace(), names[i].getLocalPart());
            }
            final GetMultipleResourceProperties_Element gmrpe = new GetMultipleResourceProperties_Element(properties);
            final GetMultipleResourcePropertiesResponse gmrpr = mStub.getMultipleResourceProperties(gmrpe);
            Map nameToPropValueListMap = new HashMap();
            for (int i = 0; i < names.length; i++) {
                nameToPropValueListMap.put(new QName(names[i].getNamespace(), names[i].getLocalPart()), new ArrayList());
            }
            MessageElement[] messageElements = gmrpr.get_any();
            for (int i = 0; i < messageElements.length; ++i) {
                ResourcePropertyValue value = new SimpleResourcePropertyValue(messageElements[i].getChildNodes());
                String localName = messageElements[i].getLocalName();
                String namespaceURI = messageElements[i].getNamespaceURI();
                QName qName = new QName(namespaceURI, localName);
                List list = (List) nameToPropValueListMap.get(qName);
                list.add(value);
            }
            final ResourceProperty[] resourceProperties = new ResourceProperty[names.length];
            for (int i = 0; i < names.length; i++) {
                QName qName = new QName(names[i].getNamespace(), names[i].getLocalPart());
                ArrayList valuesList = (ArrayList) nameToPropValueListMap.get(qName);
                resourceProperties[i] = new SimpleResourceProperty(names[i], (ResourcePropertyValue[]) valuesList.toArray(new ResourcePropertyValue[valuesList.size()]));
            }
            return resourceProperties;
        } catch (final Exception ex) {
            mapPropertiesExcpetion(ex);
            mapExcpetion(ex);
            throw new UnhandledException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() throws ServerCommsException, ServerException, ResourceUnknownException, ClientServerCompatibilityException, ResourceNotDestroyedException {
        try {
            final Destroy d = new Destroy();
            mStub.destroy(d);
        } catch (final org.oasis.wsrf.lifetime.ResourceNotDestroyedFaultType resourceEx) {
            throw new ResourceNotDestroyedException(this, resourceEx.getCause());
        } catch (final Exception ex) {
            mapExcpetion(ex);
            throw new UnhandledException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public TerminationTime setTerminationTime(final Date requestedTerminationTime) throws ServerCommsException, ServerException, ResourceUnknownException, TerminationTimeChangeRejectedException, UnableToSetTerminationTimeException {
        try {
            Calendar calander = null;
            if (requestedTerminationTime != null) {
                calander = Calendar.getInstance();
                calander.setTime(requestedTerminationTime);
            }
            final SetTerminationTime stt = new SetTerminationTime();
            stt.setRequestedTerminationTime(calander);
            final SetTerminationTimeResponse sttr = mStub.setTerminationTime(stt);
            if (sttr.getNewTerminationTime() != null) {
                return new SimpleTerminationTime(sttr.getCurrentTime(), sttr.getNewTerminationTime());
            } else {
                return new SimpleTerminationTime(sttr.getCurrentTime().getTime(), null);
            }
        } catch (final org.oasis.wsrf.lifetime.TerminationTimeChangeRejectedFaultType resourceEx) {
            throw new TerminationTimeChangeRejectedException(this, resourceEx.getCause());
        } catch (final org.oasis.wsrf.lifetime.UnableToSetTerminationTimeFaultType resourceEx) {
            throw new UnableToSetTerminationTimeException(requestedTerminationTime, this, resourceEx.getCause());
        } catch (final Exception ex) {
            mapExcpetion(ex);
            throw new UnhandledException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setTimeout(int timeout) {
        ((org.apache.axis.client.Stub) mStub).setTimeout(timeout);
        if (mSpecificStub != null) {
            ((org.apache.axis.client.Stub) mSpecificStub).setTimeout(timeout);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addServerCommsProperty(String propertyName, Object value) {
        ((Stub) mStub)._setProperty(propertyName, value);
        if (mSpecificStub != null) {
            mSpecificStub._setProperty(propertyName, value);
        }
    }

    /**
     * Gets the URL.
     * 
     * @return the URL
     */
    public URL getURL() {
        return mURL;
    }

    /**
     * Gets the URL string.
     * 
     * @return the URL string
     */
    public String getURLString() {
        return mURL.toExternalForm();
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return getURLString() + "/" + getResourceID();
    }

    /** 
     * Sets the specific stub.  This is the stub used by a subclass
     * instance to invoke the specific (i.e. non-generic) operations. 
     * 
     * @param specificStub 
     *     Specific stub.
     */
    protected void setSpecificStub(final Stub specificStub) {
        mSpecificStub = specificStub;
    }

    /**
     * Maps an GT exception to a client toolkt exception.
     * 
     * @param ex
     *     GT exception
     * @throws ServerException
     *             if an internal error occurs at the server.
     * @throws ResourceUnknownException
     *             if the resource is unknown to the server.
     * @throws ServerCommsException
     *             if an error occurs communicating with the server.
     */
    protected void mapExcpetion(Exception ex) throws ServerException, ServerCommsException, ResourceUnknownException {
        try {
            throw ex;
        } catch (final uk.org.ogsadai.service.gt.intrinsics.ServerFaultType serverEx) {
            throw new ServerException(new ErrorID(serverEx.getId()), serverEx.getCause());
        } catch (final uk.org.ogsadai.service.gt.resolver.ResourceUnknownFaultType resourceEx) {
            final ResourceID resourceID = new ResourceID(resourceEx.getParameter(0));
            throw new ResourceUnknownException(resourceID);
        } catch (final uk.org.ogsadai.service.gt.resolver.ServerFaultType serverEx) {
            throw new ServerException(new ErrorID(serverEx.getId()), serverEx.getCause());
        } catch (final org.oasis.wsrf.resource.ResourceUnknownFaultType resourceEx) {
            throw new ResourceUnknownException(getResourceID());
        } catch (final AxisFault axisEx) {
            throw new AxisServerCommsException(this, ex);
        } catch (final RemoteException remoteEx) {
            throw new RemoteServerCommsException(this, ex);
        } catch (final Exception exception) {
        }
    }

    /**
     * Map a properties exception.
     * 
     * @param ex
     *            the exception to be mapped.
     * @throws PropertyNameInvalidException
     *             the property name invalid exception.
     */
    protected void mapPropertiesExcpetion(Exception ex) throws PropertyNameInvalidException {
        try {
            throw ex;
        } catch (final org.oasis.wsrf.properties.InvalidResourcePropertyQNameFaultType resourceEx) {
            ResourcePropertyName name = null;
            BaseFaultTypeDescription[] desc = resourceEx.getDescription();
            if ((desc != null) && (desc.length > 0)) {
                String propertyName = desc[0].get_value();
                QName qName = QName.valueOf(propertyName);
                name = new ResourcePropertyName(qName.getLocalPart(), qName.getNamespaceURI());
            }
            throw new PropertyNameInvalidException(name);
        } catch (final Exception exception) {
        }
    }
}
