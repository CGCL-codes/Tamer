package wtanaka.praya.module;

import javax.swing.*;
import wtanaka.praya.Protocol;
import wtanaka.praya.obj.Message;
import wtanaka.praya.obj.Graphical;

/**
 * Opens a new window for messages with score >= 10.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2001/10/10 06:10:47 $
 **/
public class Popup extends Module {

    public static final int numProtocolsExpected = 1;

    public Popup(Protocol[] prots) {
        super(prots);
    }

    public void receiveMessage(Message m) {
        if (m.getScore() >= 10) {
            JDialog wind = new JDialog();
            boolean show = true;
            wind.getContentPane().setLayout(new java.awt.GridLayout(0, 1));
            if (m instanceof Graphical) {
                wind.getContentPane().add(((Graphical) m).renderAsComponent());
                wind.pack();
            } else {
                String msg = ((Message) m).renderAsString();
                if (msg.trim().equals("")) show = false;
                if (show) {
                    JTextArea text = new JTextArea(msg);
                    text.setEditable(false);
                    wind.getContentPane().add(new JScrollPane(text));
                    wind.pack();
                    text.setLineWrap(true);
                    text.setWrapStyleWord(true);
                }
            }
            if (show) {
                java.awt.Dimension dims = wind.getSize();
                dims.height += 20;
                if (dims.width > 640) dims.width = 600;
                if (dims.height > 440) dims.height = 440;
                wind.setSize(dims);
                wind.setTitle(((Message) m).getSubject() + " (" + ((Message) m).getFrom() + ")");
                wind.setVisible(true);
                wind.setTitle(((Message) m).getSubject() + " (" + ((Message) m).getFrom() + ")");
            }
        }
    }

    public void install() {
    }

    public void remove() {
    }

    public boolean usesMessageScore() {
        return true;
    }
}
