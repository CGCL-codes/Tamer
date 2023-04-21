package goldengate.ftp.core.control;

import java.nio.charset.Charset;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.string.StringDecoder;

/**
 * Exactly same as StringDecoder from Netty
 *
 * @author Frederic Bregier
 *
 */
public class FtpControlStringDecoder extends StringDecoder {

    /**
	 *
	 */
    public FtpControlStringDecoder() {
    }

    /**
     * @see StringDecoder
     * @param arg0
     */
    public FtpControlStringDecoder(String arg0) {
        super(Charset.forName(arg0));
    }

    /**
     * @see StringDecoder
     * @param arg0
     */
    public FtpControlStringDecoder(Charset arg0) {
        super(arg0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        return super.decode(ctx, channel, msg);
    }
}
