package example.talk;

import com.ibm.aglet.Aglet;
import com.ibm.aglet.AgletProxy;
import com.ibm.aglet.Message;
import com.ibm.aglet.event.MobilityAdapter;
import com.ibm.aglet.event.MobilityEvent;

/**
 * @version     1.00    96/12/19
 * @author      Mitsuru Oshima
 */
public class TalkSlave extends Aglet {

    transient String name = "Unknown";

    transient TalkWindow window = null;

    AgletProxy masterProxy = null;

    public TalkSlave() {
    }

    public void onCreation(Object o) {
        masterProxy = (AgletProxy) o;
        addMobilityListener(new MobilityAdapter() {

            public void onArrival(MobilityEvent ev) {
                window = new TalkWindow(TalkSlave.this);
                window.pack();
                window.show();
                try {
                    name = getProperty("user.name");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private String getProperty(String key) {
        return System.getProperty(key, "Unknown");
    }

    public void onDisposing() {
        if (window != null) {
            window.dispose();
            window = null;
        }
    }

    public boolean handleMessage(Message msg) {
        if (msg.sameKind("dialog")) {
            window.show();
        } else if (msg.sameKind("text")) {
            String str = (String) msg.getArg();
            if (window.isVisible() == false) {
                window.show();
            }
            window.appendText(str);
            return true;
        } else if (msg.sameKind("bye")) {
            window.appendText("Bye Bye..");
            try {
                Thread.currentThread().sleep(3000);
            } catch (Exception ex) {
            }
            msg.sendReply();
            dispose();
        }
        return false;
    }

    public void sendText(String text) {
        try {
            if (masterProxy == null) {
                return;
            }
            masterProxy.sendMessage(new Message("text", name + " : " + text));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void print(String m) {
        System.out.println("Receiver : " + m);
    }
}
