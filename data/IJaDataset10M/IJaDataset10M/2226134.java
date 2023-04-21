package net.community.chest.net.proto.text.ssh.message.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.community.chest.net.proto.text.ssh.SSHMsgCode;
import net.community.chest.net.proto.text.ssh.SSHProtocol;
import net.community.chest.net.proto.text.ssh.io.SSHInputDataDecoder;
import net.community.chest.net.proto.text.ssh.io.SSHOutputDataEncoder;
import net.community.chest.net.proto.text.ssh.message.AbstractSSHMsgEncoder;

/**
 * <P>Copyright as per GPLv2</P>
 *
 * See RFC 4254
 * @author Lyor G.
 * @since Jul 12, 2009 11:08:51 AM
 */
public class ChannelWindowAdjust extends AbstractSSHMsgEncoder<ChannelWindowAdjust> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5574001725888398803L;

    public ChannelWindowAdjust() {
        super(SSHMsgCode.SSH_MSG_CHANNEL_WINDOW_ADJUST);
    }

    private int _recipientChannel;

    public int getRecipientChannel() {
        return _recipientChannel;
    }

    public void setRecipientChannel(int recipientChannel) {
        _recipientChannel = recipientChannel;
    }

    private int _numBytesToAdd;

    public int getNumBytesToAdd() {
        return _numBytesToAdd;
    }

    public void setNumBytesToAdd(int numBytesToAdd) {
        _numBytesToAdd = numBytesToAdd;
    }

    @Override
    public ChannelWindowAdjust read(final InputStream in) throws IOException {
        setRecipientChannel(SSHProtocol.readUint32(in));
        setNumBytesToAdd(SSHProtocol.readUint32(in));
        return this;
    }

    @Override
    public ChannelWindowAdjust decode(SSHInputDataDecoder in) throws IOException {
        if (null == in) throw new IOException("decode(" + getMsgCode() + ") no " + SSHInputDataDecoder.class.getSimpleName() + " instance");
        setRecipientChannel(in.readInt());
        setNumBytesToAdd(in.readInt());
        return this;
    }

    @Override
    public void write(final OutputStream out) throws IOException {
        SSHProtocol.writeUint32(out, getRecipientChannel());
        SSHProtocol.writeUint32(out, getNumBytesToAdd());
    }

    @Override
    public void encode(SSHOutputDataEncoder out) throws IOException {
        if (null == out) throw new IOException("encode(" + getMsgCode() + ") no " + SSHOutputDataEncoder.class.getSimpleName() + " instance");
        out.writeInt(getRecipientChannel());
        out.writeInt(getNumBytesToAdd());
    }
}
