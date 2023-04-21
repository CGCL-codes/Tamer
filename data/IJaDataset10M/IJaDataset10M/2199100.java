package com.socialnetworkshirts.twittershirts.web;

import com.socialnetworkshirts.twittershirts.Constants;
import com.socialnetworkshirts.twittershirts.data.DataService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mbs
 */
public class ConfomatServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String twitterUserId = request.getParameter(Constants.PARAM_TWITTER_USER_ID);
        if (twitterUserId == null) {
            twitterUserId = DataService.getInstance().getDefaultTwitterUserId();
        }
        request.setAttribute(Constants.PARAM_TWITTER_USER_ID, twitterUserId);
        request.setAttribute(Constants.PARAM_PRODUCT_TYPE, DataService.getInstance().getProductTypes().get(0));
        setCacheHeader(response, Constants.DEFAULT_CACHE_TIME);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
