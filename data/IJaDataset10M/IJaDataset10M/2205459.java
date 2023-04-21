package ch.iserver.ace.net.impl.protocol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import ch.iserver.ace.DocumentDetails;
import ch.iserver.ace.net.NetworkServiceCallback;
import ch.iserver.ace.net.RemoteDocumentProxy;
import ch.iserver.ace.net.impl.NetworkServiceImpl;
import ch.iserver.ace.net.impl.RemoteDocumentProxyExt;
import ch.iserver.ace.net.impl.RemoteDocumentProxyImpl;
import ch.iserver.ace.net.impl.RemoteUserProxyExt;
import ch.iserver.ace.net.impl.protocol.RequestImpl.DocumentInfo;

/**
 *
 */
public class DocumentDiscoveryResponseFilter extends AbstractRequestFilter {

    private static Logger LOG = Logger.getLogger(DocumentDiscoveryPrepareFilter.class);

    public DocumentDiscoveryResponseFilter(RequestFilter successor) {
        super(successor);
    }

    public void process(Request request) {
        if (request.getType() == ProtocolConstants.PUBLISHED_DOCUMENTS) {
            List docs = (List) request.getPayload();
            if (docs.size() > 0) {
                Iterator iter = docs.iterator();
                List proxies = new ArrayList(docs.size());
                while (iter.hasNext()) {
                    DocumentInfo doc = (DocumentInfo) iter.next();
                    RemoteDocumentProxy proxy = createProxy(doc);
                    proxies.add(proxy);
                }
                NetworkServiceCallback callback = NetworkServiceImpl.getInstance().getCallback();
                RemoteDocumentProxy[] proxyArr = (RemoteDocumentProxy[]) proxies.toArray(new RemoteDocumentProxy[0]);
                callback.documentDiscovered(proxyArr);
            } else {
                LOG.debug("no documents in response");
            }
        } else {
            super.process(request);
        }
    }

    private RemoteDocumentProxy createProxy(DocumentInfo doc) {
        String docId = doc.getDocId();
        String docName = doc.getName();
        String userId = doc.getUserId();
        RemoteUserProxyExt user = SessionManager.getInstance().getSession(userId).getUser();
        DocumentDetails details = new DocumentDetails(docName);
        RemoteDocumentProxyExt proxy = new RemoteDocumentProxyImpl(docId, details, user);
        user.addSharedDocument(proxy);
        return proxy;
    }
}
