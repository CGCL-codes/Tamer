package application;

import gnu.io.SerialPortEvent;
import info.clearthought.layout.TableLayout;
import io.CommunicationListener;
import io.IOClient;
import io.SerialCommunicator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Monitor extends JPanel {

    IOClient serial;

    private JTextArea console;

    private JTextField input;

    public Monitor(String comm) {
        super();
        serial = SerialCommunicator.getCommunicator(comm);
        CommunicationListener listen = new CommunicationListener() {

            public void communicationRequested(SerialPortEvent event) {
                byte[] arr = getComm().receive();
                String data = new String(arr);
                getConsole().setText(data);
            }
        };
        serial.addListener(listen);
        serial.connect();
        initGUI();
    }

    public IOClient getComm() {
        return serial;
    }

    private JTextField getInput() {
        if (input == null) {
            input = new JTextField();
            input.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    byte[] arr = getInput().getText().getBytes();
                    getComm().send(arr);
                    getInput().setText("");
                }
            });
        }
        return input;
    }

    private JTextArea getConsole() {
        if (console == null) {
            console = new JTextArea();
        }
        return console;
    }

    private void initGUI() {
        TableLayout layout = new TableLayout(new double[][] { { .99 }, { TableLayout.FILL, 5, 20 } });
        setLayout(layout);
        add(getConsole(), "0,0,0,0,f,f");
        add(getInput(), "0,2,0,2,f,f");
    }
}
