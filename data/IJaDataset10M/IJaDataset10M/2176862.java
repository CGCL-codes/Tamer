package synthlabgui.widgets.configPanel.keyboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import javax.swing.JPanel;
import synthlab.api.Port;
import synthlabgui.widgets.configPanel.AbstractConfigPanel;

/**
 * A configure component in which contains a keyboard
 * */
public class KeyboardPanel extends JPanel implements AbstractConfigPanel, KeyboardListener {

    ArrayList<Port> inputPorts = new ArrayList<Port>();

    private static final long serialVersionUID = 7066858859998499895L;

    public KeyboardPanel() {
        Keyboard k = new Keyboard(14);
        k.addKeyboardListener(this);
        add(k);
        setOpaque(true);
    }

    private HashMap<Double, Port> ref = new HashMap<Double, Port>();

    private HashMap<Port, Boolean> available = new HashMap<Port, Boolean>();

    @Override
    public void notifyPort(double value) {
        if (!inputPorts.isEmpty()) {
            if (!ref.containsKey(value)) {
                for (Port p : inputPorts) {
                    if (available.get(p)) {
                        System.out.println("Port " + p.getName() + " link " + value);
                        available.put(p, false);
                        ref.put(value, p);
                        p.setValues(value);
                        break;
                    }
                }
            }
        }
    }

    public void releasePort(double value) {
        Port p = ref.get(value);
        if (p != null) {
            System.out.println("Port " + p.getName() + " release " + value);
            available.put(p, true);
            ref.remove(value);
            p.setValues(-1);
        }
    }

    @Override
    public void setPort(Port port) {
        inputPorts.add(0, port);
    }

    public void addPort(Port port) {
        inputPorts.add(port);
        available.put(port, true);
    }

    @Override
    public void keyActionPerformed(KeyboardEvent e) {
        if (e.getType() == KeyboardEvent.PRESSED) {
            notifyPort(e.getValue());
            System.out.println("Send " + e.getValue());
        } else {
            releasePort(e.getValue());
            System.out.println("Remove " + e.getValue());
        }
    }

    @Override
    public void setState(boolean enabled) {
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
