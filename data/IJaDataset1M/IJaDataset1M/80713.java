package fr.cnes.sitools.dataset.opensearch.runnables;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Status;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.resource.ResourceException;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.dataset.opensearch.OpenSearchApplication;
import fr.cnes.sitools.dataset.opensearch.OpenSearchStore;
import fr.cnes.sitools.dataset.opensearch.model.Opensearch;
import fr.cnes.sitools.util.RIAPUtils;

/**
 * OpensearchRefreshRunnable, runnable class for refreshing indexes
 * 
 * 
 * @author m.gond (AKKA Technologies)
 */
public class OpensearchRefreshRunnable extends OpensearchRunnable {

    /**
   * Default constructor
   * 
   * @param os
   *          the opensearch
   * @param store
   *          the store
   * @param solrUrl
   *          the url of solr
   * @param application
   *          the OpensearchApplication
   * @param context
   *          the context
   */
    public OpensearchRefreshRunnable(Opensearch os, OpenSearchStore store, String solrUrl, Context context, OpenSearchApplication application) {
        this.os = os;
        this.store = store;
        this.solrUrl = solrUrl;
        this.context = context;
        this.application = application;
    }

    @Override
    public void run() {
        Request reqPOST = new Request(Method.POST, RIAPUtils.getRiapBase() + solrUrl + "/" + os.getId() + "/refresh");
        ArrayList<Preference<MediaType>> objectMediaType = new ArrayList<Preference<MediaType>>();
        objectMediaType.add(new Preference<MediaType>(MediaType.APPLICATION_ALL_XML));
        reqPOST.getClientInfo().setAcceptedMediaTypes(objectMediaType);
        Client client = this.context.getClientDispatcher();
        client.getConnectTimeout();
        org.restlet.Response r = null;
        try {
            r = this.context.getClientDispatcher().handle(reqPOST);
            if (r == null || Status.isError(r.getStatus().getCode())) {
                os.setStatus("INACTIVE");
                store.update(os);
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL);
            }
            @SuppressWarnings("unchecked") XstreamRepresentation<Response> repr = (XstreamRepresentation<Response>) r.getEntity();
            Response resp = (Response) repr.getObject();
            if (resp.getSuccess()) {
                if (this.application.isCancelled()) {
                    this.os.setStatus("INACTIVE");
                } else {
                    this.os.setStatus("ACTIVE");
                    os.setLastImportDate(new Date(new GregorianCalendar().getTime().getTime()));
                }
                store.update(os);
            } else {
                this.os.setStatus("INACTIVE");
                this.os.setErrorMsg(resp.getMessage());
                store.update(os);
            }
        } finally {
            RIAPUtils.exhaust(r);
        }
    }
}
