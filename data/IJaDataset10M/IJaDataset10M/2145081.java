package net.community.chest.net.proto.text.ssh.message.auth;

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
 * @author Lyor G.
 * @since Jul 12, 2009 10:12:23 AM
 */
public class UserAuthPublicKeyOK extends AbstractSSHMsgEncoder<UserAuthPublicKeyOK> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5907788273593365464L;

    public UserAuthPublicKeyOK() {
        super(SSHMsgCode.SSH_MSG_USERAUTH_PK_OK);
    }

    private String _pkAlgorithmName;

    public String getPkAlgorithmName() {
        return _pkAlgorithmName;
    }

    public void setPkAlgorithmName(String pkAlgorithmName) {
        _pkAlgorithmName = pkAlgorithmName;
    }

    private byte[] _pkBlobData;

    public byte[] getPkBlobData() {
        return _pkBlobData;
    }

    public void setPkBlobData(byte[] pkBlobData) {
        _pkBlobData = pkBlobData;
    }

    @Override
    public UserAuthPublicKeyOK read(InputStream in) throws IOException {
        setPkAlgorithmName(SSHProtocol.readASCIIString(in));
        setPkBlobData(SSHProtocol.readBlobData(in));
        return this;
    }

    @Override
    public UserAuthPublicKeyOK decode(SSHInputDataDecoder in) throws IOException {
        if (null == in) throw new IOException("decode(" + getMsgCode() + ") no " + SSHInputDataDecoder.class.getSimpleName() + " instance");
        setPkAlgorithmName(in.readASCII());
        setPkBlobData(in.readBlob());
        return this;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        SSHProtocol.writeNonNullStringBytes(out, true, getPkAlgorithmName());
        SSHProtocol.writeStringBytes(out, getPkBlobData());
    }

    @Override
    public void encode(SSHOutputDataEncoder out) throws IOException {
        if (null == out) throw new IOException("encode(" + getMsgCode() + ") no " + SSHOutputDataEncoder.class.getSimpleName() + " instance");
        out.writeASCII(getPkAlgorithmName());
        out.write(getPkBlobData());
    }
}
