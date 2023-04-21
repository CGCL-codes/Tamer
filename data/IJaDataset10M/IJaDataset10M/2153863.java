package mainFrame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingUtilities;
import utils.Lan;
import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
public class CreateLanFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    {
        try {
            javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel jPanel1;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel jError;

    private JButton jButton3;

    private JButton jButton2;

    private JButton jButton1;

    private JList jList1;

    private JTextField jTextField5;

    private JTextField jTextField4;

    private JTextField jTextField3;

    private JTextField jTextField2;

    private JTextField jTextField1;

    private JLabel jLabel2;

    private JLabel jLabel1;

    private Interfaces inters = new Interfaces();

    InetAddress dirLocal = null;

    private Lan lan = new Lan();

    /**
	* Auto-generated main method to display this JFrame
	*/
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                CreateLanFrame inst = new CreateLanFrame();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public CreateLanFrame() {
        super();
        initGUI();
        try {
            dirLocal = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(null);
            this.setTitle("Creaci�n de un Hub");
            this.setResizable(false);
            {
                jPanel1 = new JPanel();
                getContentPane().add(jPanel1);
                jPanel1.setBounds(7, 12, 510, 177);
                jPanel1.setBorder(BorderFactory.createTitledBorder("Interfaces"));
                jPanel1.setLayout(null);
                {
                    jLabel1 = new JLabel();
                    jPanel1.add(jLabel1);
                    jLabel1.setText("Nombre de la Interface:");
                    jLabel1.setBounds(17, 19, 152, 14);
                }
                {
                    jLabel2 = new JLabel();
                    jPanel1.add(jLabel2);
                    jLabel2.setText("IP local:");
                    jLabel2.setBounds(17, 45, 152, 14);
                }
                {
                    jLabel3 = new JLabel();
                    jPanel1.add(jLabel3);
                    jLabel3.setText("Puerto de lectura:");
                    jLabel3.setBounds(17, 71, 152, 14);
                }
                {
                    jLabel4 = new JLabel();
                    jPanel1.add(jLabel4);
                    jLabel4.setText("IP destino:");
                    jLabel4.setBounds(17, 97, 152, 14);
                }
                {
                    jLabel5 = new JLabel();
                    jPanel1.add(jLabel5);
                    jLabel5.setText("Puerto de escritura:");
                    jLabel5.setBounds(17, 123, 152, 14);
                }
                {
                    jTextField1 = new JTextField();
                    jPanel1.add(jTextField1);
                    jTextField1.setText("eth0");
                    jTextField1.setBounds(181, 12, 108, 21);
                    jTextField1.setToolTipText("Nombre ficticio de la interface.\nPor cuestiones de comodidad para\nobservar el funcionamiento.");
                }
                {
                    jTextField2 = new JTextField();
                    jPanel1.add(jTextField2);
                    jTextField2.setBounds(181, 42, 108, 21);
                    jTextField2.setText("127.0.0.1");
                    jTextField2.setEditable(false);
                    jTextField2.setToolTipText("IP local ficticia. No utilizada.");
                    jTextField2.setFocusable(false);
                }
                {
                    jTextField3 = new JTextField();
                    jPanel1.add(jTextField3);
                    jTextField3.setText("6000");
                    jTextField3.setBounds(181, 68, 52, 21);
                }
                {
                    jTextField4 = new JTextField();
                    jPanel1.add(jTextField4);
                    jTextField4.setBounds(181, 94, 108, 21);
                    jTextField4.setToolTipText("IP real del dispositivo remoto.");
                    jTextField4.setText("localhost");
                }
                {
                    jTextField5 = new JTextField();
                    jPanel1.add(jTextField5);
                    jTextField5.setText("6001");
                    jTextField5.setBounds(181, 120, 52, 21);
                }
                {
                    jButton1 = new JButton();
                    jPanel1.add(jButton1);
                    jButton1.setText("Agregar");
                    jButton1.setBounds(181, 149, 115, 21);
                    jButton1.addMouseListener(new MouseAdapter() {

                        public void mouseClicked(MouseEvent evt) {
                            jButton1MouseClicked(evt);
                        }
                    });
                }
                {
                    ListModel jList1Model = new DefaultComboBoxModel();
                    jList1 = new JList();
                    jPanel1.add(jList1);
                    jList1.setModel(jList1Model);
                    jList1.setBounds(333, 22, 165, 144);
                    jList1.setBorder(BorderFactory.createTitledBorder(null, "Interfaces actuales:", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
                    jList1.setBackground(new java.awt.Color(192, 192, 192));
                    jList1.setFocusable(false);
                }
                {
                    jError = new JLabel();
                    jPanel1.add(jError);
                    jError.setBounds(17, 151, 159, 10);
                }
            }
            {
                jButton2 = new JButton();
                getContentPane().add(jButton2);
                jButton2.setText("Listo");
                jButton2.setBounds(378, 195, 139, 21);
                jButton2.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent evt) {
                        jButton2MouseClicked(evt);
                    }
                });
            }
            {
                jButton3 = new JButton();
                getContentPane().add(jButton3);
                jButton3.setText("Salir");
                jButton3.setBounds(12, 195, 86, 21);
                jButton3.addMouseListener(new MouseAdapter() {

                    public void mouseClicked(MouseEvent evt) {
                        jButton3MouseClicked(evt);
                    }
                });
            }
            pack();
            this.setSize(537, 255);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] split(String str) {
        ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        while (i < str.length()) {
            String aux = "";
            while (i < str.length() && str.charAt(i) != '.') {
                aux = aux + str.charAt(i);
                i++;
            }
            list.add(aux);
            i++;
        }
        String[] salida = new String[list.size()];
        for (int j = 0; j < salida.length; j++) {
            salida[j] = list.get(j);
        }
        return salida;
    }

    private void jButton1MouseClicked(MouseEvent evt) {
        boolean error = false;
        int puertoLectura = 0, puertoEscritura = 0;
        puertoLectura = Integer.parseInt(jTextField3.getText());
        puertoEscritura = Integer.parseInt(jTextField5.getText());
        String addr = jTextField4.getText();
        InetAddress remoteDir = null;
        try {
            if (addr.equalsIgnoreCase("localhost")) remoteDir = InetAddress.getByName(addr); else {
                String[] auxStr = split(addr);
                byte[] aux = new byte[4];
                for (int i = 0; i < auxStr.length; i++) aux[i] = (byte) Integer.parseInt(auxStr[i]);
                remoteDir = InetAddress.getByAddress(aux);
            }
            lan.addInterface(dirLocal, puertoLectura, remoteDir, puertoEscritura);
            ((DefaultComboBoxModel) jList1.getModel()).addElement(jTextField1.getText());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (!error) jError.setText("");
    }

    private void jButton3MouseClicked(MouseEvent evt) {
        this.dispose();
    }

    private void jButton2MouseClicked(MouseEvent evt) {
        System.out.println("Hub creado..");
        this.setVisible(false);
    }
}
