package pl.radical.open.gg;

import pl.radical.open.gg.event.MessageListener;

/**
 * Created on 2005-01-29
 * 
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 */
public interface IChat {

    IChat sendMessage(String messageBody) throws GGException;

    void addChatListener(MessageListener messageListener);

    void removeChatListener(MessageListener messageListener);
}
