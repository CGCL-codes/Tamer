package cn.tworen.demou.handler.invoke;

import org.apache.log4j.Logger;
import cn.tworen.demou.cmd.IInvokeCmd;
import cn.tworen.demou.rtmp.Client;
import cn.tworen.demou.stream.Stream;

/**
 * Created on  2007-1-20
 *
 * Title       : Pause.java
 * Description : 
 * 
 * @author     : LuJinYi
 * @version    : 1.0
 * @Date       : 2007-1-20
 * History     : 
 * 
 */
public class Pause implements IInvoke {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(Pause.class);

    public void invoke(Client client, IInvokeCmd invokeCmd) {
        logger.debug("Pause()");
        Stream stream = client.getStream(invokeCmd.getHeader().getSource());
        if (stream != null) stream.pause((Boolean) (invokeCmd.getParams().get(0)));
    }
}
