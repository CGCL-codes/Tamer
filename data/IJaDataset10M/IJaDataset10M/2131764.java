package com.kongur.network.erp.domain.tc.dto;

import java.io.Serializable;

/**
 * �˿�
 * 
 * @author zhengwei
 */
public class RefundDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8783500165495332887L;

    /**
     * �ڲ��˿��
     */
    private Long refundId;

    /**
     * �ⲿ�˿��
     */
    private Long outRefundId;

    /**
     * �ܾ��˿�ԭ��
     */
    private String rejectRefundReason;

    /**
     * �ڲ��˿
     * 
     * @return
     */
    public Long getRefundId() {
        return refundId;
    }

    public void setRefundId(Long refundId) {
        this.refundId = refundId;
    }

    public Long getOutRefundId() {
        return outRefundId;
    }

    public void setOutRefundId(Long outRefundId) {
        this.outRefundId = outRefundId;
    }

    public String getRejectRefundReason() {
        return rejectRefundReason;
    }

    public void setRejectRefundReason(String rejectRefundReason) {
        this.rejectRefundReason = rejectRefundReason;
    }
}
