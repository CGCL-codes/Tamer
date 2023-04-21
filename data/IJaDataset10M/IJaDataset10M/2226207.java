package org.koossery.adempiere.core.contract.itf.request;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IR_ResolutionDTO extends ITransferableObject {

    public String getDescription();

    public void setDescription(String description);

    public String getHelp();

    public void setHelp(String help);

    public String getName();

    public void setName(String name);

    public int getR_Resolution_ID();

    public void setR_Resolution_ID(int r_Resolution_ID);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
