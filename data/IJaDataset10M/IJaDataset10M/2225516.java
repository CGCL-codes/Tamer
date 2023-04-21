package cn.com.believer.songyuanframework.openapi.storage.box.impl.simple.functions;

import cn.com.believer.songyuanframework.openapi.storage.box.functions.PublicShareResponse;

/**
 * @author Jimmy
 * 
 */
public class PublicShareResponseImpl extends BoxResponseImpl implements PublicShareResponse {

    /** public name. */
    private String publicName;

    /**
     * @return the publicName
     */
    public String getPublicName() {
        return this.publicName;
    }

    /**
     * @param publicName
     *            the publicName to set
     */
    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }
}
