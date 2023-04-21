package Main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import Windows.Messenger;
import Windows.Network;
import Windows.Pc;
import Windows.User;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Window extends javax.swing.JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public static JDesktopPane JDesktop;

    private JLabel Pc;

    private JLabel Messenger;

    private JLabel User;

    private JLabel Network;

    public static boolean pcStat = false;

    public static boolean messengerStat = false;

    public static boolean userStat = false;

    public static boolean networkStat = false;

    static Icon ic;

    static JLabel lab;

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	* Auto-generated main method to display this JFrame
	*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Window inst = new Window();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public Window() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            String checkName = Config.name;
            if (checkName.length() == 0) {
                Config.name = "test";
                Config.id = 2;
                Config.mail = "test@showus.de";
            }
            this.setTitle("ShowUs.de - " + Config.name);
            ic = new ImageIcon(new URL("http://showus.de/img/logo.jpg"));
            lab = new JLabel(ic);
            {
                JDesktop = new JDesktopPane();
                JDesktop.setBackground(new java.awt.Color(255, 255, 255));
                {
                    Pc = new JLabel();
                    JDesktop.add(Pc);
                    Pc.setBounds(10, 11, 116, 48);
                    Pc.setText("PC");
                    Pc.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/pc.png")));
                    Pc.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            try {
                                PcMouseClicked(evt);
                            } catch (PropertyVetoException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                {
                    Messenger = new JLabel();
                    JDesktop.add(Messenger);
                    Messenger.setBounds(10, 65, 116, 48);
                    Messenger.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/messenger.png")));
                    Messenger.setText("Messenger");
                    Messenger.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            try {
                                MessengerMouseClicked(evt);
                            } catch (PropertyVetoException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                {
                    User = new JLabel();
                    JDesktop.add(User);
                    User.setText("User");
                    User.setBounds(10, 119, 116, 48);
                    User.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/user.png")));
                    User.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            try {
                                UserMouseClicked(evt);
                            } catch (PropertyVetoException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                {
                    Network = new JLabel();
                    JDesktop.add(Network);
                    Network.setText("Netzwerk");
                    Network.setBounds(10, 178, 116, 14);
                    Network.setIcon(new ImageIcon(getClass().getClassLoader().getResource("img/network.png")));
                    Network.setSize(116, 48);
                    Network.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            try {
                                NetworkMouseClicked(evt);
                            } catch (PropertyVetoException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addComponent(JDesktop, 0, 566, Short.MAX_VALUE));
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addComponent(JDesktop, 0, 792, Short.MAX_VALUE));
            pack();
            this.setSize(800, 600);
            lab.setSize(JDesktop.getWidth(), JDesktop.getHeight());
            JDesktop.add(lab, new Integer(-30001));
            JDesktop.setLayer(lab, -31001);
            getRootPane().setOpaque(true);
            ((JComponent) this.getContentPane()).setOpaque(false);
            this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("img/icon.png")).getImage());
            this.addComponentListener(new ComponentAdapter() {

                public void componentResized(ComponentEvent evt) {
                    thisComponentResized(evt);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PcMouseClicked(MouseEvent evt) throws PropertyVetoException {
        if (pcStat == false) {
            Pc pcWindow = new Windows.Pc();
            JDesktop.add(pcWindow);
            pcWindow.setVisible(true);
            pcWindow.setClosable(true);
            pcWindow.setBounds(194, 134, 400, 300);
            pcWindow.setSelected(true);
            pcStat = true;
            System.out.println("open");
        } else {
            System.out.println("pc already opened");
        }
    }

    private void MessengerMouseClicked(MouseEvent evt) throws PropertyVetoException {
        if (messengerStat == false) {
            Messenger messengerWindow = new Windows.Messenger();
            JDesktop.add(messengerWindow);
            messengerWindow.setVisible(true);
            messengerWindow.setClosable(true);
            messengerWindow.setBounds(194, 134, 400, 300);
            messengerWindow.setSelected(true);
            messengerStat = true;
            System.out.println("open");
        } else {
            System.out.println("Messenger already opened");
        }
    }

    private void NetworkMouseClicked(MouseEvent evt) throws PropertyVetoException {
        if (networkStat == false) {
            Network networkWindow = new Windows.Network();
            JDesktop.add(networkWindow);
            networkWindow.setVisible(true);
            networkWindow.setClosable(true);
            networkWindow.setBounds(194, 134, 400, 300);
            networkWindow.setSelected(true);
            networkStat = true;
            System.out.println("open");
        } else {
            System.out.println("network already opened");
        }
    }

    private void UserMouseClicked(MouseEvent evt) throws PropertyVetoException {
        if (userStat == false) {
            User userWindow = new Windows.User();
            JDesktop.add(userWindow);
            userWindow.setVisible(true);
            userWindow.setClosable(true);
            userWindow.setBounds(194, 134, 400, 300);
            userWindow.setSelected(true);
            userStat = true;
        } else {
            System.out.println("User already opened");
        }
    }

    private void thisComponentResized(ComponentEvent evt) {
        lab.setSize(JDesktop.getWidth(), JDesktop.getHeight());
        System.out.println(lab.getSize());
        lab.repaint();
    }
}
