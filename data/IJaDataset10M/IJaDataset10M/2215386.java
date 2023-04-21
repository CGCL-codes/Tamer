package org.frameworkset.spi.remote.http;

import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;

/**
 * <p>Title: HttpFuture.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-5 ����09:12:05
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpFuture extends BaseFutureCall {

    public HttpFuture(RPCMessage srcmsg, RPCAddress address, RPCIOHandler handler) {
        super(srcmsg, address, handler);
    }

    @Override
    protected RPCMessage _call() throws Exception {
        RPCMessage msg = Client.sendMessage(this.srcmsg, this.address);
        return msg;
    }
}
