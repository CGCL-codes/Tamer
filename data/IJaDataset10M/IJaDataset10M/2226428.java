package cn.tworen.demou.handler;

import org.apache.log4j.Logger;
import cn.tworen.demou.cmd.IBufferCmd;
import cn.tworen.demou.cmd.ICmd;
import cn.tworen.demou.rtmp.Client;

/**
 * Created on  2007-2-1
 *
 * Title       : JavaBeanInvokeHandler.java
 * Description : 
 * 
 * @author     : LuJinYi
 * @version    : 1.0
 * @Date       : 2007-2-1
 * History     : 
 * 
 */
public class JavaBeanInvokeHandler implements IListener {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(JavaBeanInvokeHandler.class);

    public void recevie(Client client, ICmd cmd) {
        logger.debug("JavaBeanInvokeHandler");
        IBufferCmd bufferCmd = (IBufferCmd) cmd;
    }
}
