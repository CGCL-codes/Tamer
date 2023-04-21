package servidor;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.xml.ws.Endpoint;

/**
 *
 * @author  tuza
 */
public class Principal extends javax.swing.JFrame {

    private MenuBar menubar = new MenuBar();

    private Menu menu1 = new Menu("Configure");

    private MenuItem menu1Item1 = new MenuItem("Set ingredients directory");

    private static String ingredientsDirectory = "ingredients";

    private static String howtosDirectory = "howtos";

    /** Creates new form Principal */
    public Principal() {
        initComponents();
        String ip = Main.getIP();
        String wsAddress = "http://" + ip + ":8765/Gpi/Servicio";
        Endpoint.publish(wsAddress, new Servicio(this));
        log("Web service was published successfuly.\n" + "WSDL URL: " + wsAddress + "?WSDL");
    }

    class EscuchaMenu implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(menu1Item1)) {
                log("** Ingredients folder set to:\n" + getIngredientsDirectory() + "\n");
                saveConf();
            }
        }
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        errorLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Fresita's server");
        outputArea.setColumns(20);
        outputArea.setRows(5);
        jScrollPane1.setViewportView(outputArea);
        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(errorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE).addGap(286, 286, 286).addComponent(jButton1))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(29, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(errorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton1))));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    public void log(String msj) {
        outputArea.append(msj + "\n");
        Runnable autoscroll = new Runnable() {

            public void run() {
                JScrollBar vbar = jScrollPane1.getVerticalScrollBar();
                vbar.setValue(vbar.getMaximum());
            }
        };
        SwingUtilities.invokeLater(autoscroll);
    }

    public static String getIngredientsDirectory() {
        return Principal.ingredientsDirectory;
    }

    public boolean readConf() {
        File archivo;
        File dir = new File("lib");
        if (dir.isDirectory()) {
            try {
                archivo = new File(dir, "server.conf");
                FileInputStream f = new FileInputStream(archivo);
                DataInputStream leer = new DataInputStream(f);
                String carpeta = leer.toString();
                if (carpeta.compareTo("") == 0 || carpeta.compareTo(" ") == 0) {
                    throw new Exception();
                }
                return true;
            } catch (Exception e) {
                this.log("**Error**   There is no ingredients folder selected.");
                e.printStackTrace();
                return false;
            }
        } else {
            this.log("**Error**   There is no ingredients folder selected.");
            return false;
        }
    }

    private void saveConf() {
        File dir = new File("lib");
        File archivo;
        try {
            if (!dir.isDirectory()) {
                dir.mkdir();
            }
            archivo = new File(dir, "server.conf");
            FileOutputStream f = new FileOutputStream(archivo);
            DataOutputStream escribir = new DataOutputStream(f);
            escribir.writeUTF(getIngredientsDirectory());
        } catch (Exception e) {
        }
    }

    private javax.swing.JLabel errorLabel;

    private javax.swing.JButton jButton1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea outputArea;
}
