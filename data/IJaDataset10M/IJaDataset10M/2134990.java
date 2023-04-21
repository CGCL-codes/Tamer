package org.koossery.adempiere.core.contract.itf.client;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IASP_ClientLevelDTO extends ITransferableObject {

    public int getAsP_ClientLevel_ID();

    public void setAsP_ClientLevel_ID(int asP_ClientLevel_ID);

    public int getAsP_Level_ID();

    public void setAsP_Level_ID(int asP_Level_ID);

    public int getAsP_Module_ID();

    public void setAsP_Module_ID(int asP_Module_ID);

    public String getHelp();

    public void setHelp(String help);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
