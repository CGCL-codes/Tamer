package org.koossery.adempiere.core.contract.itf.server.request;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IR_RequestProcessorLogDTO extends ITransferableObject {

    public byte[] getBinaryData();

    public void setBinaryData(byte[] binaryData);

    public String getDescription();

    public void setDescription(String description);

    public int getR_RequestProcessor_ID();

    public void setR_RequestProcessor_ID(int r_RequestProcessor_ID);

    public int getR_RequestProcessorLog_ID();

    public void setR_RequestProcessorLog_ID(int r_RequestProcessorLog_ID);

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
