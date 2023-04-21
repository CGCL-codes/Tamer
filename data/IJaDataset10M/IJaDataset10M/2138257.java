package org.koossery.adempiere.core.contract.itf.server.alert;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IAD_AlertProcessorLogDTO extends ITransferableObject {

    public int getAd_AlertProcessor_ID();

    public void setAd_AlertProcessor_ID(int ad_AlertProcessor_ID);

    public int getAd_AlertProcessorLog_ID();

    public void setAd_AlertProcessorLog_ID(int ad_AlertProcessorLog_ID);

    public byte[] getBinaryData();

    public void setBinaryData(byte[] binaryData);

    public String getDescription();

    public void setDescription(String description);

    public String getReference();

    public void setReference(String reference);

    public String getSummary();

    public void setSummary(String summary);

    public String getTextMsg();

    public void setTextMsg(String textMsg);

    public String getIsError();

    public void setIsError(String isError);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
