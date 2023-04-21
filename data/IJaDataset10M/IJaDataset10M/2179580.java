package com.hyk.proxy.server.gae.util;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.hyk.proxy.common.Constants;
import com.hyk.proxy.server.gae.config.XmlConfig;
import com.hyk.proxy.server.gae.rpc.service.BandwidthStatisticsServiceImpl;

/**
 *
 */
public class ClearStatRecordHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!XmlConfig.getInstance().getAppId().equals(Constants.MASTER_APPID)) {
            BandwidthStatisticsServiceImpl.clearRecord();
        } else {
            AppIDVerifyTask.verifyAppIDs();
        }
        resp.setStatus(200);
        resp.getWriter().write("OK");
    }
}
