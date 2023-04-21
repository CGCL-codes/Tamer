package cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.functions;

import cn.com.believer.songyuanframework.openapi.storage.box.constant.BoxConstant;
import cn.com.believer.songyuanframework.openapi.storage.box.functions.LogoutRequest;

/**
 * @author Jimmy
 * 
 */
public class LogoutRequestImpl extends BoxRequestImpl implements LogoutRequest {

    /** auth token. */
    private String authToken;

    /**
     * @return the authToken
     */
    public String getAuthToken() {
        return this.authToken;
    }

    /**
     * @param authToken
     *            the authToken to set
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return action name
     */
    public String getActionName() {
        return BoxConstant.ACTION_NAME_LOGOUT;
    }
}
