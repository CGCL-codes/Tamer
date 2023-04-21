package com.producteev4j.services.version0;

import com.producteev4j.exceptions.ProducteevException;
import com.producteev4j.model.json.activity.ActivityListResponseWrapperImpl;
import com.producteev4j.model.request.ProducteevGetRequest;
import com.producteev4j.model.request.ProducteevRequest;
import com.producteev4j.model.response.ActivityWrapper;
import com.producteev4j.transport.ProducteevTransport;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jcarrey
 * Date: 2/07/11
 * Time: 21:06
 * To change this template use File | Settings | File Templates.
 */
public class ProducteevActivityServiceV0Impl implements ProducteevActivityService {

    private static final String ACTIVITY_CONTEXT_PATH = "/activities";

    private static final String ACTIVITY_SHOW_ACTIVITIES_SERVICE = "/show_activities.json";

    private static final String ACTIVITY_SHOW_NOTIFICATIONS_SERVICE = "/show_notifications.json";

    private static final String ACTIVITY_SET_READ_SERVICE = "/notifications_set_read.json";

    private static final String ACTIVITY_SHOW_ACTIVITIES_ENDPOINT = ProducteevServiceV0.PRODUCTEEV_API_ENDPOINT + ACTIVITY_CONTEXT_PATH + ACTIVITY_SHOW_ACTIVITIES_SERVICE;

    private static final String ACTIVITY_SHOW_NOTIFICATIONS_ENDPOINT = ProducteevServiceV0.PRODUCTEEV_API_ENDPOINT + ACTIVITY_CONTEXT_PATH + ACTIVITY_SHOW_NOTIFICATIONS_SERVICE;

    private static final String ACTIVITY_SET_READ_ENDPOINT = ProducteevServiceV0.PRODUCTEEV_API_ENDPOINT + ACTIVITY_CONTEXT_PATH + ACTIVITY_SET_READ_SERVICE;

    private static final String ID_LABEL_PARAM = "id_label";

    private static final String LAST_ID_PARAM = "last_id";

    private static final String PAGE_PARAM = "page";

    @Override
    public List<? extends ActivityWrapper> getActivities(ProducteevTransport transport, String userToken, Long idDashboard, Long lastId, Long page) throws ProducteevException {
        ProducteevRequest request = new ProducteevGetRequest();
        request.setEndpoint(ACTIVITY_SHOW_ACTIVITIES_ENDPOINT);
        if (idDashboard != null) {
            request.getParams().putValue(ID_LABEL_PARAM, idDashboard);
        }
        if (lastId != null) {
            request.getParams().putValue(LAST_ID_PARAM, lastId);
        }
        if (page != null) {
            request.getParams().putValue(PAGE_PARAM, page);
        }
        request.setResponseClass(ActivityListResponseWrapperImpl.class);
        return (ProducteevValidator.assertNoError((ActivityListResponseWrapperImpl) transport.process(request, userToken))).getActivities();
    }

    @Override
    public List<? extends ActivityWrapper> getNotifications(ProducteevTransport transport, String userToken, Long idDashboard, Long lastId, Long page) throws ProducteevException {
        ProducteevRequest request = new ProducteevGetRequest();
        request.setEndpoint(ACTIVITY_SHOW_NOTIFICATIONS_ENDPOINT);
        if (idDashboard != null) {
            request.getParams().putValue(ID_LABEL_PARAM, idDashboard);
        }
        if (lastId != null) {
            request.getParams().putValue(LAST_ID_PARAM, lastId);
        }
        if (page != null) {
            request.getParams().putValue(PAGE_PARAM, page);
        }
        request.setResponseClass(ActivityListResponseWrapperImpl.class);
        return (ProducteevValidator.assertNoError((ActivityListResponseWrapperImpl) transport.process(request, userToken))).getActivities();
    }

    @Override
    public List<? extends ActivityWrapper> setNotificationRead(ProducteevTransport transport, String userToken, long idDashboard, long lastId) throws ProducteevException {
        ProducteevRequest request = new ProducteevGetRequest();
        request.setEndpoint(ACTIVITY_SET_READ_ENDPOINT);
        request.getParams().putValue(ID_LABEL_PARAM, idDashboard);
        request.getParams().putValue(LAST_ID_PARAM, lastId);
        request.setResponseClass(ActivityListResponseWrapperImpl.class);
        return (ProducteevValidator.assertNoError((ActivityListResponseWrapperImpl) transport.process(request, userToken))).getActivities();
    }
}
