package net.kano.joustsim.oscar.oscar.service.icbm.ft;

import net.kano.joscar.rvcmd.sendfile.FileSendReqRvCmd;
import net.kano.joscar.rvcmd.sendfile.FileSendAcceptRvCmd;
import net.kano.joscar.rvcmd.sendfile.FileSendRejectRvCmd;
import net.kano.joscar.snaccmd.CapabilityBlock;
import net.kano.joscar.rv.RvSession;

class FileTransferRequestMaker implements RvRequestMaker {

    private FileTransfer transfer;

    public FileTransferRequestMaker(FileTransfer transfer) {
        this.transfer = transfer;
    }

    public void sendRvRequest() {
        sendRvRequest(FileSendReqRvCmd.REQINDEX_FIRST);
    }

    public void sendRvRequest(int newIndex) {
        getRvSession().sendRv(new FileSendReqRvCmd(newIndex, transfer.getInvitationMessage(), transfer.getRvSessionInfo().getConnectionInfo(), transfer.getRequestFileInfo()));
    }

    private RvSession getRvSession() {
        return transfer.getRvSessionInfo().getRvSession();
    }

    public void sendRvAccept() {
        getRvSession().sendRv(new FileSendAcceptRvCmd());
    }

    public void sendRvReject() {
        getRvSession().sendRv(new FileSendRejectRvCmd());
    }

    public CapabilityBlock getCapabilityBlock() {
        return CapabilityBlock.BLOCK_FILE_SEND;
    }
}
