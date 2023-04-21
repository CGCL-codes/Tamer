package net.kano.joscar.rvcmd;

import net.kano.joscar.DefensiveTools;
import net.kano.joscar.rv.RvCommandFactory;
import net.kano.joscar.rvcmd.addins.AddinsAcceptRvCmd;
import net.kano.joscar.rvcmd.addins.AddinsRejectRvCmd;
import net.kano.joscar.rvcmd.addins.AddinsReqRvCmd;
import net.kano.joscar.rvcmd.chatinvite.ChatInvitationRvCmd;
import net.kano.joscar.rvcmd.chatinvite.ChatInviteRejectRvCmd;
import net.kano.joscar.rvcmd.directim.DirectIMAcceptRvCmd;
import net.kano.joscar.rvcmd.directim.DirectIMRejectRvCmd;
import net.kano.joscar.rvcmd.directim.DirectIMReqRvCmd;
import net.kano.joscar.rvcmd.getfile.GetFileAcceptRvCmd;
import net.kano.joscar.rvcmd.getfile.GetFileRejectRvCmd;
import net.kano.joscar.rvcmd.getfile.GetFileReqRvCmd;
import net.kano.joscar.rvcmd.icon.SendBuddyIconRvCmd;
import net.kano.joscar.rvcmd.sendbl.SendBuddyListRvCmd;
import net.kano.joscar.rvcmd.sendfile.FileSendAcceptRvCmd;
import net.kano.joscar.rvcmd.sendfile.FileSendRejectRvCmd;
import net.kano.joscar.rvcmd.sendfile.FileSendReqRvCmd;
import net.kano.joscar.rvcmd.trillcrypt.AbstractTrillianCryptRvCmd;
import net.kano.joscar.rvcmd.trillcrypt.TrillianCryptAcceptRvCmd;
import net.kano.joscar.rvcmd.trillcrypt.TrillianCryptBeginRvCmd;
import net.kano.joscar.rvcmd.trillcrypt.TrillianCryptCloseRvCmd;
import net.kano.joscar.rvcmd.trillcrypt.TrillianCryptMsgRvCmd;
import net.kano.joscar.rvcmd.trillcrypt.TrillianCryptReqRvCmd;
import net.kano.joscar.rvcmd.voice.VoiceAcceptRvCmd;
import net.kano.joscar.rvcmd.voice.VoiceRejectRvCmd;
import net.kano.joscar.rvcmd.voice.VoiceReqRvCmd;
import net.kano.joscar.snaccmd.CapabilityBlock;
import net.kano.joscar.snaccmd.icbm.AbstractRvIcbm;
import net.kano.joscar.snaccmd.icbm.RecvRvIcbm;
import net.kano.joscar.snaccmd.icbm.RvCommand;
import java.util.List;

/**
 * A default RV command factory that generates instances of the
 * <code>RvCommand</code>s defined in the <code>net.kano.joscar.rvcmd</code>
 * subpackages.
 * <br>
 * <br>
 * This factory can generate RV commands from the following types of
 * capabilities:
 * <ul>
 * <li> <code>CapabilityBlock.BLOCK_ADDINS </li>
 * <li> <code>CapabilityBlock.BLOCK_CHAT </li>
 * <li> <code>CapabilityBlock.BLOCK_DIRECTIM </li>
 * <li> <code>CapabilityBlock.BLOCK_FILE_GET </li>
 * <li> <code>CapabilityBlock.BLOCK_FILE_SEND </li>
 * <li> <code>CapabilityBlock.BLOCK_ICON </li>
 * <li> <code>CapabilityBlock.BLOCK_SENDBUDDYLIST </li>
 * <li> <code>CapabilityBlock.BLOCK_TRILLIANCRYPT </li>
 * <li> <code>CapabilityBlock.BLOCK_VOICE </li>
 * </ul>
 */
public class DefaultRvCommandFactory implements RvCommandFactory {

    /** The capabilities supported by this factory. */
    private static final List<CapabilityBlock> SUPPORTED_CAPS = DefensiveTools.asUnmodifiableList(CapabilityBlock.BLOCK_FILE_SEND, CapabilityBlock.BLOCK_TRILLIANCRYPT, CapabilityBlock.BLOCK_SENDBUDDYLIST, CapabilityBlock.BLOCK_CHAT, CapabilityBlock.BLOCK_DIRECTIM, CapabilityBlock.BLOCK_FILE_GET, CapabilityBlock.BLOCK_ICON, CapabilityBlock.BLOCK_ADDINS, CapabilityBlock.BLOCK_VOICE);

    public List<CapabilityBlock> getSupportedCapabilities() {
        return SUPPORTED_CAPS;
    }

    public RvCommand genRvCommand(RecvRvIcbm rvIcbm) {
        DefensiveTools.checkNull(rvIcbm, "rvIcbm");
        CapabilityBlock block = rvIcbm.getCapability();
        int rvStatus = rvIcbm.getRvStatus();
        if (block.equals(CapabilityBlock.BLOCK_FILE_SEND)) {
            if (rvStatus == AbstractRvIcbm.RVSTATUS_REQUEST) {
                return new FileSendReqRvCmd(rvIcbm);
            } else if (rvStatus == AbstractRvIcbm.RVSTATUS_DENY) {
                return new FileSendRejectRvCmd(rvIcbm);
            } else if (rvStatus == AbstractRvIcbm.RVSTATUS_ACCEPT) {
                return new FileSendAcceptRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_TRILLIANCRYPT)) {
            int status = AbstractTrillianCryptRvCmd.getTrillianCmdType(rvIcbm);
            if (status == AbstractTrillianCryptRvCmd.CMDTYPE_REQUEST) {
                return new TrillianCryptReqRvCmd(rvIcbm);
            } else if (status == AbstractTrillianCryptRvCmd.CMDTYPE_ACCEPT) {
                return new TrillianCryptAcceptRvCmd(rvIcbm);
            } else if (status == AbstractTrillianCryptRvCmd.CMDTYPE_BEGIN) {
                return new TrillianCryptBeginRvCmd(rvIcbm);
            } else if (status == AbstractTrillianCryptRvCmd.CMDTYPE_MESSAGE) {
                return new TrillianCryptMsgRvCmd(rvIcbm);
            } else if (status == AbstractTrillianCryptRvCmd.CMDTYPE_CLOSE) {
                return new TrillianCryptCloseRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_SENDBUDDYLIST)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new SendBuddyListRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_CHAT)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new ChatInvitationRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_DENY) {
                return new ChatInviteRejectRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_DIRECTIM)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new DirectIMReqRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_ACCEPT) {
                return new DirectIMAcceptRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_DENY) {
                return new DirectIMRejectRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_FILE_GET)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new GetFileReqRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_ACCEPT) {
                return new GetFileAcceptRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_DENY) {
                return new GetFileRejectRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_ICON)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new SendBuddyIconRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_ADDINS)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new AddinsReqRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_ACCEPT) {
                return new AddinsAcceptRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_DENY) {
                return new AddinsRejectRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else if (block.equals(CapabilityBlock.BLOCK_VOICE)) {
            if (rvStatus == RvCommand.RVSTATUS_REQUEST) {
                return new VoiceReqRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_ACCEPT) {
                return new VoiceAcceptRvCmd(rvIcbm);
            } else if (rvStatus == RvCommand.RVSTATUS_DENY) {
                return new VoiceRejectRvCmd(rvIcbm);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String toString() {
        return "DefaultRvCommandFactory";
    }
}
