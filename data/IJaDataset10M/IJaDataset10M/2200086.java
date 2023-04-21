package org.koossery.adempiere.core.contract.itf.workflow;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IAD_WF_ProcessDataDTO extends ITransferableObject {

    public int getAd_WF_Process_ID();

    public void setAd_WF_Process_ID(int ad_WF_Process_ID);

    public int getAd_WF_ProcessData_ID();

    public void setAd_WF_ProcessData_ID(int ad_WF_ProcessData_ID);

    public String getAttributeName();

    public void setAttributeName(String attributeName);

    public String getAttributeValue();

    public void setAttributeValue(String attributeValue);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
