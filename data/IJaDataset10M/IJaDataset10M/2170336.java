package com.kongur.network.erp.request.tc;

import com.kongur.network.erp.enums.EnumPlatform;
import com.kongur.network.erp.request.BaseRequest;

/**
 * �Ա��������� -- ����/�޸ı�ע
 * @author gaojf
 * @version $Id: TaobaoRemarksRequest.java,v 0.1 2012-3-14 ����05:47:02 gaojf Exp $
 */
public class RemarksRequest extends BaseRequest {

    /**
     * ����Id
     */
    private Long shopId;

    /**
     * �ⲿ������
     */
    private Long orderId;

    /**
     * ��ע
     */
    private String content;

    /**
     * ƽ̨
     */
    private EnumPlatform platform;

    public EnumPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(EnumPlatform platform) {
        this.platform = platform;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
