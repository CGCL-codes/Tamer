package org.koossery.adempiere.core.contract.itf.request;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IR_StatusDTO extends ITransferableObject {

    public String getDescription();

    public void setDescription(String description);

    public String getHelp();

    public void setHelp(String help);

    public String getName();

    public void setName(String name);

    public int getNext_Status_ID();

    public void setNext_Status_ID(int next_Status_ID);

    public int getR_Status_ID();

    public void setR_Status_ID(int r_Status_ID);

    public int getR_StatusCategory_ID();

    public void setR_StatusCategory_ID(int r_StatusCategory_ID);

    public int getSeqNo();

    public void setSeqNo(int seqNo);

    public int getTimeoutDays();

    public void setTimeoutDays(int timeoutDays);

    public int getUpdate_Status_ID();

    public void setUpdate_Status_ID(int update_Status_ID);

    public String getValue();

    public void setValue(String value);

    public String getIsClosed();

    public void setIsClosed(String isClosed);

    public String getIsDefault();

    public void setIsDefault(String isDefault);

    public String getIsFinalClose();

    public void setIsFinalClose(String isFinalClose);

    public String getIsOpen();

    public void setIsOpen(String isOpen);

    public String getIsWebCanUpdate();

    public void setIsWebCanUpdate(String isWebCanUpdate);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
