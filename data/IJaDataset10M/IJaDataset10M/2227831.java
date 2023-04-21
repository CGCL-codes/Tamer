package wsl.fw.message;

import wsl.fw.datasource.DataManager;
import wsl.fw.util.CKfw;
import wsl.fw.util.Config;
import wsl.fw.util.Log;
import java.util.Vector;
import wsl.fw.resource.ResId;

/**
 * Listener (handler) for phone  messages. Receives the message and passes it
 * to a gui for manual processing.
 */
public class PhoneMessageListener extends MessageListenerBase {

    private static final String _ident = "$Date: 2002/06/11 23:11:42 $  $Revision: 1.1.1.1 $ " + "$Archive: /Framework/Source/wsl/fw/message/PhoneMessageListener.java $ ";

    public static final ResId WARNING_UNEXPECTED_TYPE = new ResId("PhoneMessageListener.warning.UnexpectedType");

    String _mtHomePhone;

    String _mtMobilePhone;

    String _messageTypes[];

    /**
     * Constructor.
     * @param args, command line args to be passed to base class.
     */
    public PhoneMessageListener(String args[]) {
        super(args);
        _mtHomePhone = Config.getProp(CKfw.MESSAGE_TYPE_HOME_PHONE);
        _mtMobilePhone = Config.getProp(CKfw.MESSAGE_TYPE_MOBILE_PHONE);
        Vector v = new Vector();
        if (_mtHomePhone != null) v.add(_mtHomePhone);
        if (_mtMobilePhone != null) v.add(_mtMobilePhone);
        _messageTypes = (String[]) v.toArray(new String[0]);
    }

    /**
     * Override to get the type of DataManager to create.
     * @return the class of the desired data manager, a Class object that
     *   specifies DataManager or a subclass.
     */
    protected Class getDataManagerClass() {
        return DataManager.class;
    }

    /**
     * Virtual to be overridden by concrete subclasses to get the message types
     * that this Message Listener is to handle.
     * @return an array of Strings defining the desired message types.
     */
    protected String[] getMessageTypes() {
        return _messageTypes;
    }

    /**
     * Virtual to be overridden by concrete subclasses to handle the processing
     * of a message.
     * @param msg, the message to process.
     * @return one of the OM_ constants that indicate whether the message has
     *   been processed and how it should be updated in the DB.
     */
    protected int onMessage(String msgType, Message msg) {
        int rv = OM_NOT_PROCESSED;
        if ((_mtMobilePhone != null && _mtMobilePhone.equals(msgType)) || (_mtHomePhone != null && _mtHomePhone.equals(msgType))) rv = processPhone(msg); else Log.warning(WARNING_UNEXPECTED_TYPE.getText() + " " + msgType);
        return rv;
    }

    /**
     * Process a Phone message.
     * @param msg, the message to process (send).
     * @return one of the OM_ constants that indicate whether the message has
     *   been processed and how it should be updated in the DB.
     */
    protected int processPhone(Message msg) {
        return OM_NOT_PROCESSED;
    }

    /**
     * Main entrypoint.
     * @param args, command line arguments.
     */
    public static void main(String args[]) {
    }
}
