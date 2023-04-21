package net.sf.lipermi.handler.filter;

import net.sf.lipermi.call.IRemoteMessage;

/**
 * A interface to define a protocol filter to
 * intercept messages and make any needed modification
 * (ie. encryptation/compression).
 * 
 * @author lipe
 * @date   07/10/2006
 */
public interface IProtocolFilter {

    IRemoteMessage readObject(Object obj);

    Object prepareWrite(IRemoteMessage message);
}
