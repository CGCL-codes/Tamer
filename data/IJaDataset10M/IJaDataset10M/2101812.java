package org.mobicents.ssf.servlet;

import java.util.ArrayList;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

/**
 * <p>
 * 各リスナに対してJMSのメッセージを通知するためのクラスです。
 * </p>
 * <p>
 * JmsDispatcherServletはこのクラスを取得して、リスナとして登録を行います。
 * JmsDispatcherServlet自体をJMSのMessageListenerとして、コンテナに直接登録することでも対応可能です。
 * </p>
 * 
 * NOTE:リスナをクリアするために、destroy-methodとして"destroy"を設定すること。
 * 
 * @author nisihara
 * 
 */
public class DispatcherMessageListener implements SessionAwareMessageListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<MessageListener> listeners = new ArrayList<MessageListener>();

    public void addListener(MessageListener listener) {
        this.listeners.add(listener);
    }

    public void destroy() {
        listeners.clear();
    }

    public void onMessage(Message arg0, Session arg1) throws JMSException {
        for (MessageListener listener : listeners) {
            try {
                listener.onMessage(arg0);
            } catch (Exception e) {
                logger.error("Could not dispatch the message to the listener.[msg=" + arg0 + "][listener=" + listener + "]", e);
            }
        }
    }
}
