package cn.tworen.demou.remoting;

import java.util.Date;
import javax.servlet.http.HttpServletResponse;
import net.sf.cindy.Buffer;
import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.util.Charset;
import org.apache.log4j.Logger;
import cn.tworen.demou.remoting.amf.AmfDecode;
import cn.tworen.demou.remoting.amf.AmfEncode;
import cn.tworen.demou.remoting.cmd.BaseCmd;
import cn.tworen.demou.remoting.cmd.Cmd;
import cn.tworen.demou.remoting.cmd.CmdExecutor;
import cn.tworen.demou.remoting.cmd.RemoteCmd;

/**
 * Echo http request.
 * 
 * @author <a href="chenrui@gmail.com">Roger Chen</a>
 * @version $id$
 */
public class AmfSessionHandler extends SessionHandlerAdapter {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(AmfSessionHandler.class);

    private AmfDecode decode = new AmfDecode();

    private CmdExecutor executor = new CmdExecutor();

    public void objectReceived(Session session, Object obj) throws Exception {
        HttpRequest request = (HttpRequest) obj;
        Buffer buffer = BufferFactory.allocate(request.getContent().length);
        buffer.put(request.getContent());
        buffer.flip();
        logger.debug(buffer.dump());
        try {
            AmfMessage message = decode.decode(buffer);
            for (Body body : message.getBodies()) {
                this.executor.routeCmd(message.getHeaders(), body, session, request, message.getVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exceptionCaught(Session session, Throwable cause) {
        session.close();
        System.err.println(cause);
    }
}
