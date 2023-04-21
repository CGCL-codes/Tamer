package com.liferay.portlet.shopping.service.http;

import com.liferay.portal.shared.util.StackTraceUtil;
import com.liferay.portlet.shopping.service.spring.ShoppingCartServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.rmi.RemoteException;

/**
 * <a href="ShoppingCartServiceSoap.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ShoppingCartServiceSoap {

    public static void deleteCart(java.lang.String cartId) throws RemoteException {
        try {
            ShoppingCartServiceUtil.deleteCart(cartId);
        } catch (Exception e) {
            String stackTrace = StackTraceUtil.getStackTrace(e);
            _log.error(stackTrace);
            throw new RemoteException(stackTrace);
        }
    }

    public static com.liferay.portlet.shopping.model.ShoppingCartModel emptyCart(java.lang.String sessionId, java.lang.String companyId) throws RemoteException {
        try {
            com.liferay.portlet.shopping.model.ShoppingCart returnValue = ShoppingCartServiceUtil.emptyCart(sessionId, companyId);
            return returnValue;
        } catch (Exception e) {
            String stackTrace = StackTraceUtil.getStackTrace(e);
            _log.error(stackTrace);
            throw new RemoteException(stackTrace);
        }
    }

    public static com.liferay.portlet.shopping.model.ShoppingCartModel getCart(java.lang.String sessionId, java.lang.String companyId) throws RemoteException {
        try {
            com.liferay.portlet.shopping.model.ShoppingCart returnValue = ShoppingCartServiceUtil.getCart(sessionId, companyId);
            return returnValue;
        } catch (Exception e) {
            String stackTrace = StackTraceUtil.getStackTrace(e);
            _log.error(stackTrace);
            throw new RemoteException(stackTrace);
        }
    }

    public static java.util.Map getItems(java.lang.String companyId, java.lang.String itemIds) throws RemoteException {
        try {
            java.util.Map returnValue = ShoppingCartServiceUtil.getItems(companyId, itemIds);
            return returnValue;
        } catch (Exception e) {
            String stackTrace = StackTraceUtil.getStackTrace(e);
            _log.error(stackTrace);
            throw new RemoteException(stackTrace);
        }
    }

    public static com.liferay.portlet.shopping.model.ShoppingCartModel updateCart(java.lang.String sessionId, java.lang.String companyId, java.lang.String itemIds, java.lang.String couponIds, int altShipping, boolean insure) throws RemoteException {
        try {
            com.liferay.portlet.shopping.model.ShoppingCart returnValue = ShoppingCartServiceUtil.updateCart(sessionId, companyId, itemIds, couponIds, altShipping, insure);
            return returnValue;
        } catch (Exception e) {
            String stackTrace = StackTraceUtil.getStackTrace(e);
            _log.error(stackTrace);
            throw new RemoteException(stackTrace);
        }
    }

    public static boolean hasAdmin(java.lang.String cartId) throws RemoteException {
        try {
            boolean returnValue = ShoppingCartServiceUtil.hasAdmin(cartId);
            return returnValue;
        } catch (Exception e) {
            String stackTrace = StackTraceUtil.getStackTrace(e);
            _log.error(stackTrace);
            throw new RemoteException(stackTrace);
        }
    }

    private static Log _log = LogFactory.getLog(ShoppingCartServiceSoap.class);
}
