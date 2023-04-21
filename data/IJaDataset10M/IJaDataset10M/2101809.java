package org.dyndns.mauroiorio.recoveryBlock;

public interface RecoveryBlockService extends javax.xml.rpc.Service {

    public java.lang.String getRecoveryBlockAddress();

    public org.dyndns.mauroiorio.recoveryBlock.RecoveryBlock getRecoveryBlock() throws javax.xml.rpc.ServiceException;

    public org.dyndns.mauroiorio.recoveryBlock.RecoveryBlock getRecoveryBlock(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
