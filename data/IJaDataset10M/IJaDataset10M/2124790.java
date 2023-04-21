package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class StartClient extends JFrame {

    private static final long serialVersionUID = 2932410336628308785L;

    public static Properties clientProperties = new Properties();

    public Player player;

    private Items items;

    JLabel lblName, lblPassword;

    JTextField txtName;

    public static JTextField chatInput;

    JPasswordField txtPassword;

    JButton btnConnect, btnExit;

    JButton btnAtt, btnDef, btnSpecial;

    public static ChatOutput chatOutput;

    JTable areaTable;

    JTable charackter;

    public static ConnectionManager cm = new ConnectionManager();

    ClientActionLis clientActLis = new ClientActionLis();

    public StartClient() {
        super("DOS - Client");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                clientProperties.put("width", "" + getSize().width);
                clientProperties.put("height", "" + getSize().height);
                clientProperties.put("posX", "" + getLocation().x);
                clientProperties.put("posY", "" + getLocation().y);
                try {
                    FileWriter writer = new FileWriter("config.txt");
                    clientProperties.store(writer, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        Reader reader;
        try {
            reader = new FileReader("config.txt");
            clientProperties.load(reader);
            this.setSize(Integer.parseInt(clientProperties.get("width").toString()), Integer.parseInt(clientProperties.get("height").toString()));
            this.setLocation(Integer.parseInt(clientProperties.get("posX").toString()), Integer.parseInt(clientProperties.get("posY").toString()));
        } catch (FileNotFoundException e) {
            this.setSize(500, 500);
            this.setLocation(0, 0);
        } catch (IOException e) {
            this.setSize(500, 500);
            this.setLocation(0, 0);
        }
        player = new Player();
        items = new Items();
        this.setLayout(new BorderLayout());
        createNorthGUI();
        createWestGUI();
        createCenterGUI();
        createEastGUI();
        createSouthGUI();
        this.setVisible(true);
        SenderThread st = new SenderThread();
        st.start();
        EmpaengerThread et = new EmpaengerThread();
        et.start();
    }

    private void createNorthGUI() {
        JPanel pnlInput = new JPanel(new FlowLayout());
        lblName = new JLabel("Name:");
        pnlInput.add(lblName);
        txtName = new JTextField("");
        txtName.setPreferredSize(new Dimension(96, 20));
        pnlInput.add(txtName);
        lblPassword = new JLabel("Password:");
        pnlInput.add(lblPassword);
        txtPassword = new JPasswordField("");
        txtPassword.setPreferredSize(new Dimension(96, 20));
        pnlInput.add(txtPassword);
        btnConnect = new JButton("connect");
        btnConnect.addActionListener(clientActLis);
        btnConnect.setActionCommand("connect");
        pnlInput.add(btnConnect);
        this.add(pnlInput, BorderLayout.NORTH);
    }

    private void createWestGUI() {
        String[] colNames = { " Area ", "Players" };
        String[][] rowData = { { "village", "0" }, { "road", "0" }, { "forest", "0" }, { "glade", "0" } };
        areaTable = new JTable(rowData, colNames);
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(areaTable);
        pane.setPreferredSize(new Dimension(100, 0));
        this.add(pane, BorderLayout.WEST);
    }

    private void createCenterGUI() {
        JPanel pnlCenter = new JPanel(new BorderLayout());
        JPanel pnlButtons1 = new JPanel(new GridLayout(5, 1));
        btnAtt = new JButton("A");
        btnDef = new JButton("D");
        btnSpecial = new JButton("S");
        ActionButtonsLis abl = new ActionButtonsLis(this);
        btnAtt.addActionListener(abl);
        btnAtt.setActionCommand("1");
        btnDef.addActionListener(abl);
        btnDef.setActionCommand("3");
        btnSpecial.addActionListener(abl);
        btnSpecial.setActionCommand("2");
        pnlButtons1.add(btnAtt);
        pnlButtons1.add(btnDef);
        pnlButtons1.add(btnSpecial);
        pnlButtons1.add(new JButton("4"));
        pnlButtons1.add(new JButton("5"));
        pnlCenter.add(pnlButtons1, BorderLayout.WEST);
        String[] colNames = { " % ", "Value", " % " };
        String[][] rowData = { { "'you'", "Name", "" + player.enemy.name }, { player.values[0] + "", "Life", "" + player.enemy.values[0] }, { player.values[1] + "", "Att", "" + player.enemy.values[1] }, { player.values[2] + "", "Def", "" + player.enemy.values[2] }, { player.values[3] + "", "Crit", "" + player.enemy.values[3] }, { "", "", "" }, { player.items[0], "item", "a" }, { "" + items.db.get(player.items[0]).values[1], "item", "b" }, { "", "item", "c" }, { "", "item", "d" } };
        charackter = new JTable(rowData, colNames);
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(charackter);
        pnlCenter.add(pane, BorderLayout.CENTER);
        JPanel pnlButtons2 = new JPanel(new GridLayout(5, 1));
        pnlButtons2.add(new JButton("1"));
        pnlButtons2.add(new JButton("2"));
        pnlButtons2.add(new JButton("3"));
        pnlButtons2.add(new JButton("4"));
        pnlButtons2.add(new JButton("5"));
        pnlCenter.add(pnlButtons2, BorderLayout.EAST);
        this.add(pnlCenter, BorderLayout.CENTER);
    }

    private void createEastGUI() {
    }

    private void createSouthGUI() {
        Box boxSouth = new Box(BoxLayout.Y_AXIS);
        JScrollPane pane = new JScrollPane();
        chatOutput = new ChatOutput();
        chatOutput.setPreferredSize(new Dimension(this.getWidth() - 20, 200));
        chatOutput.setEditable(false);
        pane.setViewportView(chatOutput);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatInput = new JTextField();
        chatInput.setPreferredSize(new Dimension(this.getWidth() - 20, 20));
        chatInput.addActionListener(new SendChatListener());
        boxSouth.add(chatInput);
        boxSouth.add(pane);
        boxSouth.setPreferredSize(new Dimension(this.getWidth() - 20, 220));
        this.add(boxSouth, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new StartClient();
    }
}
