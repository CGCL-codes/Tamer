package lokahi.messaging;

import lokahi.core.common.exception.UnsupportedParameterException;

/**
 * @author Stephen Toback
 * @version $Id: Message.java,v 1.2 2006/03/07 20:18:54 drtobes Exp $
 */
public interface Message {

    String send(String[] obj, boolean hasAttachement, String... params) throws UnsupportedParameterException;
}
