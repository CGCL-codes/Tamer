package it.blueocean.acanto.web.platform.debug;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.protocol.http.RequestLogger;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.IRequestLogger.RequestData;
import org.apache.wicket.protocol.http.RequestLogger.SessionData;
import org.apache.wicket.util.lang.Bytes;

/**
 * @author jcompagner
 */
public class RequestsPage extends WebPage {

    private static final long serialVersionUID = 1L;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm:ss.SSS");

    /**
	 * Construct.
	 * 
	 * @param sessionData
	 */
    public RequestsPage(final SessionData sessionData) {
        add(new Image("bug"));
        if (sessionData == null) {
            add(new Label("id").setVisible(false));
            add(new Label("sessionInfo").setVisible(false));
            add(new Label("startDate").setVisible(false));
            add(new Label("lastRequestTime").setVisible(false));
            add(new Label("numberOfRequests").setVisible(false));
            add(new Label("totalTimeTaken").setVisible(false));
            add(new Label("size").setVisible(false));
            add(new WebMarkupContainer("sessionid"));
        } else {
            add(new Label("id", new Model(sessionData.getSessionId())));
            add(new Label("sessionInfo", new Model((Serializable) sessionData.getSessionInfo())));
            add(new Label("startDate", new Model(sdf.format(sessionData.getStartDate()))));
            add(new Label("lastRequestTime", new Model(sdf.format(sessionData.getLastActive()))));
            add(new Label("numberOfRequests", new Model(sessionData.getNumberOfRequests())));
            add(new Label("totalTimeTaken", new Model(sessionData.getTotalTimeTaken())));
            add(new Label("size", new Model(Bytes.bytes(sessionData.getSessionSize()))));
            add(new WebMarkupContainer("sessionid").setVisible(false));
        }
        IModel requestsModel = new AbstractReadOnlyModel() {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            public ArrayList<RequestData> getObject() {
                List<RequestData> requests = getRequestLogger().getRequests();
                if (sessionData != null) {
                    ArrayList<RequestData> returnValues = new ArrayList<RequestData>();
                    for (int i = 0; i < requests.size(); i++) {
                        RequestData data = requests.get(i);
                        if (sessionData.getSessionId().equals(data.getSessionId())) {
                            returnValues.add(data);
                        }
                    }
                    return returnValues;
                }
                return new ArrayList<RequestData>(requests);
            }
        };
        PageableListView listView = new PageableListView("requests", requestsModel, 50) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem item) {
                RequestData rd = (RequestData) item.getModelObject();
                item.add(new Label("id", new Model(rd.getSessionId())).setVisible(sessionData == null));
                item.add(new Label("startDate", new Model(sdf.format(rd.getStartDate()))));
                item.add(new Label("timeTaken", new Model(rd.getTimeTaken())));
                item.add(new Label("eventTarget", new Model(rd.getEventTarget())));
                item.add(new Label("responseTarget", new Model(rd.getResponseTarget())));
                item.add(new Label("alteredObjects", new Model(rd.getAlteredObjects()))).setEscapeModelStrings(false);
                item.add(new Label("sessionSize", new Model(Bytes.bytes(rd.getSessionSize().longValue()))));
            }
        };
        add(listView);
        PagingNavigator navigator = new PagingNavigator("navigator", listView);
        add(navigator);
    }

    IRequestLogger getRequestLogger() {
        WebApplication webApplication = (WebApplication) Application.get();
        final IRequestLogger requestLogger;
        if (webApplication.getRequestLogger() == null) {
            requestLogger = new RequestLogger();
        } else {
            requestLogger = webApplication.getRequestLogger();
        }
        return requestLogger;
    }
}
