package org.primordion.user.app.MultiApp;

import org.primordion.xholon.base.IMessage;
import org.primordion.xholon.base.XholonWithPorts;

/**
 * Hello World. This is the detailed behavior of a sample Xholon application.
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.1 (Created on October 10, 2005)
 */
public class XhHelloWorld extends XholonWithPorts implements CeHelloWorld {

    public static final int P_PARTNER = 0;

    public static final int P_PARTNER_OTHERAPP = 1;

    public static final int SIGNAL_ONE = 100;

    public int state = 0;

    public String roleName = null;

    /**
	 * Constructor.
	 */
    public XhHelloWorld() {
    }

    public void initialize() {
        super.initialize();
        state = 0;
        roleName = null;
    }

    public void act() {
        switch(xhc.getId()) {
            case HelloWorldSystemCE:
                processMessageQ();
                break;
            case HelloCE:
                if (state == 0) {
                    if (isBound(port[P_PARTNER])) {
                        System.out.println(getName() + " --> " + port[P_PARTNER].getName());
                        port[P_PARTNER].sendMessage(SIGNAL_ONE, null, this);
                        state++;
                    }
                    if (isBound(port[P_PARTNER_OTHERAPP])) {
                        System.out.println(getName() + " --> " + port[P_PARTNER_OTHERAPP].getName());
                        port[P_PARTNER_OTHERAPP].sendMessage(SIGNAL_ONE, null, this);
                        state++;
                    }
                } else if (state == 1) {
                    if (isBound(port[P_PARTNER_OTHERAPP])) {
                        System.out.println(getName() + " --> " + port[P_PARTNER_OTHERAPP].getName());
                        port[P_PARTNER_OTHERAPP].sendMessage(SIGNAL_ONE, null, this);
                        state++;
                    }
                }
                break;
            default:
                break;
        }
        super.act();
    }

    public void processReceivedMessage(IMessage msg) {
        int event = msg.getSignal();
        switch(xhc.getId()) {
            case HelloCE:
                switch(state) {
                    case 1:
                    case 2:
                        switch(event) {
                            case SIGNAL_ONE:
                                System.out.println(getName() + " --> " + msg.getSender().getName());
                                msg.getSender().sendMessage(SIGNAL_ONE, null, this);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                break;
            case WorldCE:
                switch(state) {
                    case 0:
                        switch(event) {
                            case SIGNAL_ONE:
                                System.out.println(getName() + " --> " + msg.getSender().getName());
                                msg.getSender().sendMessage(SIGNAL_ONE, null, this);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                System.out.println("XhHelloWorld: message with no receiver " + msg);
                break;
        }
    }

    public String getName() {
        return getApp() + "#" + super.getName();
    }

    public String toString() {
        String outStr = getName();
        if ((port != null) && (port.length > 0)) {
            outStr += " [";
            for (int i = 0; i < port.length; i++) {
                if (port[i] != null) {
                    outStr += " port:" + port[i].getName();
                }
            }
            outStr += "]";
        }
        outStr += " state=" + state;
        return outStr;
    }
}
