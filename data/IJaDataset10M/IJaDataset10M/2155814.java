package org.koossery.adempiere.core.contract.itf.data.replication;

import com.koossery.fmwk.be.transferableObject.ITransferableObject;

public interface IAD_Replication_LogDTO extends ITransferableObject {

    public int getAd_Replication_Log_ID();

    public void setAd_Replication_Log_ID(int ad_Replication_Log_ID);

    public int getAd_Replication_Run_ID();

    public void setAd_Replication_Run_ID(int ad_Replication_Run_ID);

    public int getAd_ReplicationTable_ID();

    public void setAd_ReplicationTable_ID(int ad_ReplicationTable_ID);

    public String getP_Msg();

    public void setP_Msg(String p_Msg);

    public String getIsReplicated();

    public void setIsReplicated(String isReplicated);

    public String getIsActive();

    public void setIsActive(String _isActive);
}
