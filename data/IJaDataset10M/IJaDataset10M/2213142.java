package hermes.store;

import javax.jms.JMSException;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: MessageReplayer.java,v 1.1 2005/08/07 09:02:51 colincrist Exp $
 */
public interface MessageReplayer {

    public void start() throws JMSException;

    public void stop() throws JMSException;

    public void pause() throws JMSException;

    public void resume() throws JMSException;
}
