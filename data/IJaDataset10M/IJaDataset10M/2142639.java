package server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class BillSysServerMain implements ActionListener, ServerIF {

    /**
	 * The default port to listen on.
	 */
    public static final int DEFAULT_PORT = 5555;

    private int portNumber = DEFAULT_PORT;

    private final String VERSION = "BillSys 2.0";

    BillSysServer serv;

    /**
	 * Start of the GUI interface
	 */
    private JFrame frame = new JFrame("Server");

    private JLabel version = new JLabel("   " + VERSION);

    /**
	 * Used only to support the look of the application
	 */
    private JLabel space = new JLabel(" ");

    private JLabel space1 = new JLabel(" ");

    private JLabel space2 = new JLabel(" ");

    private JLabel space3 = new JLabel(" ");

    private JButton connect = new JButton("Connect");

    private JButton stop = new JButton("Stop");

    private JButton start = new JButton("Start");

    private JButton database = new JButton("Start DB");

    private JLabel status = new JLabel(" ");

    private JLabel port = new JLabel("   Port:");

    private JTextField portOutput = new JTextField("" + DEFAULT_PORT, 5);

    private JTextArea msgDrift = new JTextArea(5, 32);

    private JButton clear = new JButton("Clear");

    /**
	 * Panel hierarchy
	 */
    JPanel topPanel = new JPanel();

    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    JPanel mainPanel = new JPanel();

    JScrollPane scrollpane1 = new JScrollPane(msgDrift);

    /**
	   * Initializes the GUI interface
	   */
    public BillSysServerMain(int portInt) {
        portNumber = portInt;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder("  SERVER INFO  "));
        topPanel.setLayout(new GridLayout(3, 4, 10, 20));
        topPanel.add(version);
        topPanel.add(space);
        topPanel.add(status);
        topPanel.add(connect);
        topPanel.add(port);
        topPanel.add(portOutput);
        topPanel.add(stop);
        stop.setEnabled(false);
        topPanel.add(start);
        start.setEnabled(false);
        topPanel.add(space1);
        topPanel.add(space2);
        topPanel.add(space3);
        topPanel.add(database);
        database.setEnabled(false);
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Activity: "));
        scrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        msgDrift.setLineWrap(true);
        msgDrift.setEditable(false);
        bottomPanel.add(scrollpane1);
        bottomPanel.add(clear);
        mainPanel.setLayout(new GridLayout(2, 1, 5, 5));
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
        connect.addActionListener(this);
        stop.addActionListener(this);
        start.addActionListener(this);
        clear.addActionListener(this);
        database.addActionListener(this);
        frame.add(mainPanel);
        frame.setSize(420, 350);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void startServer(int port) {
        try {
            serv = new BillSysServer(port, this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
            displayMsgDrift("Error: Can't setup connection!" + " Terminating client.");
            System.exit(1);
        }
    }

    /**
	   * Method that waits for action events to take place in
	   * order to trigger an action or something to be done
	   *
	   * @param e Action event
	   */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == start) {
            portNumber = Integer.parseInt(portOutput.getText());
            serv.setPort(portNumber);
            serv.billSysServerStarter();
            database.setEnabled(true);
            start.setEnabled(false);
            stop.setEnabled(true);
        } else if (e.getSource() == stop) {
            serv.closeConnections();
            connect.setEnabled(true);
            stop.setEnabled(false);
            database.setEnabled(false);
        } else if (e.getSource() == clear) {
            msgDrift.setText("");
        } else if (e.getSource() == connect) {
            startServer(portNumber);
            connect.setEnabled(false);
            start.setEnabled(true);
            stop.setEnabled(true);
        } else if (e.getSource() == database) {
            serv.connectMySQL();
            database.setEnabled(false);
            stop.setEnabled(true);
        }
    }

    /**
	   * Displays a message in the msgDrift text area. This area
	   * is used to display internal messages.
	   *
	   * @param msg
	   */
    private void displayMsgDrift(String msg) {
        msgDrift.append(msg + "\n" + "\n");
    }

    /**
	   * Method that when called returns a string with date and time.
	   * Special usefull when generating time stamps
	   *
	   * @return String containing the current date and time
	   */
    private String getDateInfo() {
        Calendar now = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy-kkmmss");
        return sdf.format(now.getTime());
    }

    public void display(String message) {
        displayMsgDrift("SERVER > " + message);
    }

    public static void main(String[] args) {
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            port = DEFAULT_PORT;
        }
        BillSysServerMain server = new BillSysServerMain(port);
    }
}
