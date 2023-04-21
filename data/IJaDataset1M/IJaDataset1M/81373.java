package ca.etsmtl.ihe.xdsitest.servlets;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import javax.servlet.UnavailableException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.context.MessageContext;
import ca.etsmtl.ihe.xdsitest.profile.AppProfile;
import ca.etsmtl.ihe.xdsitest.profile.ProfileNotCreatedException;
import ca.etsmtl.ihe.xdsitest.registry.XdsRegistryMessageUtil;
import ca.etsmtl.ihe.xdsitest.registry.message.RegistryMessage;
import ca.etsmtl.ihe.xdsitest.registry.message.RegistryMessageException;
import ca.etsmtl.ihe.xdsitest.registry.message.RegistryMessageFactory;
import ca.etsmtl.ihe.xdsitest.util.FailedException;
import ca.etsmtl.ihe.xdsitest.util.GlobalConfiguration;

/**
 * This is the base class for specific implementation (profile) 
 * of request processor.
 * 
 * A request processor is used by RegistrySerlvet to process http post request. 
 * 
 * @author Renaud Berube
 *
 */
public abstract class RequestProcessor {

    protected RegistryMessageFactory messageFactory = null;

    protected URL registryUrl = null;

    protected URL repositoryUrl = null;

    protected URL proxyUrl = null;

    protected OMFactory omFactory = OMAbstractFactory.getOMFactory();

    protected RequestProcessor() throws UnavailableException {
        try {
            GlobalConfiguration config = GlobalConfiguration.getInstance();
            registryUrl = config.getUrl(RequestProcessor.class, "upstreamRegistry.url");
            repositoryUrl = config.getUrl(RequestProcessor.class, "upstreamRepository.url");
            proxyUrl = config.getUrl(RequestProcessor.class, "proxyRepository.url");
            messageFactory = RegistryMessageFactory.newInstance(AppProfile.getInstance());
        } catch (ProfileNotCreatedException e) {
            throw new UnavailableException((new StringBuilder("Initialization (")).append(e).append(").").toString());
        } catch (FailedException e) {
            throw new UnavailableException((new StringBuilder("Initialization (")).append(e).append(").").toString());
        } catch (RegistryMessageException e) {
            throw new UnavailableException((new StringBuilder("Initialization (")).append(e).append(").").toString());
        }
    }

    public abstract OMElement processPost(MessageContext msgCtxIn, OMElement response, ServletHelper helper) throws IOException;

    @SuppressWarnings("serial")
    protected static class InternalOperationException extends Exception {

        public InternalOperationException(String message, Throwable cause) {
            super(message, cause);
        }

        public InternalOperationException(Throwable cause) {
            super(cause);
        }
    }

    @SuppressWarnings("serial")
    protected static class InternalRegistryCallException extends InternalOperationException {

        public final RegistryMessage request;

        public final RegistryMessage response;

        public InternalRegistryCallException(RegistryMessage request, RegistryMessage response, RegistryMessageException cause) {
            super((new StringBuilder("|Request ")).append(request == null ? "--" : request.getRootName()).append("|ResponseStatus ").append(response == null ? "--" : ((Object) (response.getResponseStatus()))).append("|Exception ").append(cause == null ? "--" : cause.toString()).append("|").toString(), cause);
            this.request = request;
            this.response = response;
        }
    }

    @SuppressWarnings("serial")
    protected static class InternalRegistryStateException extends InternalOperationException {

        public InternalRegistryStateException(String message) {
            super(message, null);
        }
    }
}
