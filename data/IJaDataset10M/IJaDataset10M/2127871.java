package hermes;

import javax.jms.JMSException;

/**
 * Factory interface to create a selector.
 * 
 * @author colincrist@hermesjms.com
 * @version $Id: MessageSelectorFactory.java,v 1.1 2005/09/01 20:42:26 colincrist Exp $
 */
public interface MessageSelectorFactory {

    public MessageSelector create(String selector) throws JMSException;
}
