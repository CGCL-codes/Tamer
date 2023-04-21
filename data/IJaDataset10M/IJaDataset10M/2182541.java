package wtanaka.praya.gui;

import wtanaka.praya.Protocol;
import wtanaka.praya.Recipient;
import wtanaka.praya.obj.Message;

/**
 * A MessageDrop is a place where some number of protocols can drop
 * their messages.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2002/06/28 07:24:57 $
 **/
public interface UIFactory {

    /**
    * Creates a new MessageDrop.  There are initially no protocols
    * dropping messages in the message drop.
    **/
    MessageDrop createMessageDrop();

    /**
    * We need to be able to default to dropping messages somewhere.
    * This is the main message drop.  it is guaranteed to be around.
    **/
    MessageDrop mainMessageDrop();

    /**
    * Creates a new Protocol list.  It is likely that this object will
    * be a singleton.
    **/
    ProtocolList createProtocolList();

    /**
    * Creates a new Recipient list.  It is likely that this object
    * will be a singleton.
    **/
    RecipientList createRecipientList(Protocol p);

    /**
    * Creates a compositor, initially targeted at the given recipient.
    * This method should do everything needed to set the compositor
    * visible.  A reference is returned in case someone wants it, but
    * you can ignore it.
    *
    **/
    Compositor createCompositor(Recipient recipient);

    /**
    * Displays information about the recipient, in a fashion
    * particular to the user interface.
    **/
    void showRecipientInfo(Recipient recipient);

    /**
    * Creates a reply widget for the recipient, in a fashion
    * particular to the user interface.
    **/
    void replyToRecipient(Recipient recipient);

    /**
    * Creates a MessageWidget from the given message.
    **/
    MessageWidget createMessageWidget(Message m);

    /**
    * Creates a chat window for the given recipients
    **/
    ChatWidget createChatWidget(Recipient recipient);
}
