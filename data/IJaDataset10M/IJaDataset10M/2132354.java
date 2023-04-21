package com.kanteron.PacsViewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.util.Vector;
import javax.swing.*;

public class Preferences extends JFrame {

    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 2;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 1;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.ipadx = 0;
            gridBagConstraints1.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints1.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(getJPanel(), 1));
            jPanel.add(getJPanel11(), null);
            jPanel.add(getJPanel12(), null);
            jPanel.add(getJPanel13(), null);
        }
        return jPanel;
    }

    private JPanel getJPanel11() {
        if (jPanel11 == null) {
            jLabel1 = new JLabel();
            jLabel1.setText(Messages.getString("Preferences.0"));
            jLabel1.setLocation(new Point(14, 30));
            jLabel1.setSize(new Dimension(203, 16));
            jLabel1.setFont(new Font(Messages.getString("Preferences.1"), 1, 12));
            jLabel1.setHorizontalAlignment(0);
            jPanel11 = new JPanel();
            jPanel11.setLayout(null);
            jPanel11.setPreferredSize(new Dimension(400, 60));
            jPanel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.activeCaption, 1), Messages.getString("Preferences.2"), 0, 0, new Font(Messages.getString("Preferences.3"), 1, 12), Color.blue));
            jPanel11.add(jLabel1, null);
            jPanel11.add(getJTextFieldRuta(), null);
            jPanel11.add(getJButtonExplorar(), null);
        }
        return jPanel11;
    }

    private JPanel getJPanel12() {
        if (jPanel12 == null) {
            jLabel11 = new JLabel();
            jLabel11.setFont(new Font(Messages.getString("Preferences.4"), 1, 12));
            jLabel11.setText(Messages.getString("Preferences.5"));
            jLabel11.setBounds(new Rectangle(14, 31, 191, 16));
            jLabel11.setHorizontalAlignment(0);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = -1;
            gridBagConstraints.gridy = -1;
            jPanel12 = new JPanel();
            jPanel12.setLayout(null);
            jPanel12.setPreferredSize(new Dimension(400, 10));
            jPanel12.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.orange, 1), Messages.getString("Preferences.6"), 0, 0, new Font(Messages.getString("Preferences.7"), 1, 12), Color.blue));
            jPanel12.add(jLabel11, null);
            jPanel12.add(getJTextFieldAncho(), null);
        }
        return jPanel12;
    }

    private JPanel getJPanel13() {
        if (jPanel13 == null) {
            jLabel1111 = new JLabel();
            jLabel1111.setBounds(new Rectangle(16, 121, 269, 16));
            jLabel1111.setHorizontalAlignment(0);
            jLabel1111.setText(Messages.getString("Preferences.8"));
            jLabel1111.setFont(new Font(Messages.getString("Preferences.9"), 1, 12));
            jLabel111 = new JLabel();
            jLabel111.setBounds(new Rectangle(14, 30, 185, 16));
            jLabel111.setHorizontalAlignment(0);
            jLabel111.setText(Messages.getString("Preferences.10"));
            jLabel111.setFont(new Font(Messages.getString("Preferences.11"), 1, 12));
            jPanel13 = new JPanel();
            jPanel13.setLayout(null);
            jPanel13.setPreferredSize(new Dimension(400, 150));
            jPanel13.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green, 1), Messages.getString("Preferences.12"), 0, 0, new Font(Messages.getString("Preferences.13"), 1, 12), Color.blue));
            jPanel13.add(jLabel111, null);
            jPanel13.add(jLabel1111, null);
            jPanel13.add(getJTextFieldCarpetaOrigen(), null);
            jPanel13.add(getJTextFieldCarpetaDestino(), null);
            jPanel13.add(getJButtonExplorarOrigen(), null);
            jPanel13.add(getJButtonExplorarDestino(), null);
        }
        return jPanel13;
    }

    private JPanel getJPanel14() {
        if (jPanel14 == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints6.gridx = 1;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.anchor = 10;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.gridx = 2;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = 17;
            gridBagConstraints4.insets = new Insets(10, 10, 10, 10);
            gridBagConstraints4.fill = 0;
            gridBagConstraints4.ipadx = 0;
            gridBagConstraints4.ipady = 0;
            gridBagConstraints4.gridwidth = 1;
            gridBagConstraints4.gridheight = 1;
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.gridy = 0;
            jPanel14 = new JPanel();
            jPanel14.setLayout(new GridBagLayout());
            jPanel14.add(getJButtonAceptar(), gridBagConstraints6);
            jPanel14.add(getJButtonCancelar(), gridBagConstraints5);
            jPanel14.add(getJButtonRestaurar(), gridBagConstraints4);
        }
        return jPanel14;
    }

    private JButton getJButtonAceptar() {
        if (jButtonAceptar == null) {
            jButtonAceptar = new JButton();
            jButtonAceptar.setText(Messages.getString("Preferences.14"));
            jButtonAceptar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    accept();
                }
            });
        }
        return jButtonAceptar;
    }

    private JButton getJButtonCancelar() {
        if (jButtonCancelar == null) {
            jButtonCancelar = new JButton();
            jButtonCancelar.setText(Messages.getString("Preferences.15"));
            jButtonCancelar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });
        }
        return jButtonCancelar;
    }

    private void cancel() {
        dispose();
    }

    private void accept() {
        saveValues();
        dispose();
    }

    private void saveValues() {
        if (configFile != null) {
            configFile.setKeyValue(Messages.getString("Preferences.16"), jTextFieldRuta.getText());
            configFile.setKeyValue(Messages.getString("Preferences.17"), jTextFieldAncho.getText());
            configFile.setKeyValue(Messages.getString("Preferences.18"), jTextFieldCarpetaOrigen.getText());
            configFile.setKeyValue(Messages.getString("Preferences.19"), jTextFieldCarpetaDestino.getText());
        } else {
            System.out.println(Messages.getString("Preferences.20"));
        }
        if (parent != null) parent.openPreferences(true);
    }

    public void saveNodes(Vector values) {
        int total = values.size();
        String number = (new StringBuilder(String.valueOf(total / 4))).toString();
        configFile.setKeyValue(ConfigFile.NUMBER, number);
        configFile.setKeyValue(ConfigFile.OS, parent.getOS());
        configFile.setKeyValue(ConfigFile.PARSER, parent.getParser());
        int numero = 1;
        for (int i = 0; i < total; i += 4) {
            String str = (new StringBuilder(String.valueOf((String) values.elementAt(i)))).toString();
            System.out.println((new StringBuilder(String.valueOf(ConfigFile.AE))).append(numero).append(Messages.getString("Preferences.21")).append(str).toString());
            configFile.setKeyValue((new StringBuilder(String.valueOf(ConfigFile.AE))).append(numero).toString(), str);
            str = (new StringBuilder(String.valueOf((String) values.elementAt(i + 1)))).toString();
            System.out.println((new StringBuilder(String.valueOf(ConfigFile.IP))).append(numero).append(Messages.getString("Preferences.22")).append(str).toString());
            configFile.setKeyValue((new StringBuilder(String.valueOf(ConfigFile.IP))).append(numero).toString(), str);
            str = (new StringBuilder(String.valueOf((String) values.elementAt(i + 2)))).toString();
            System.out.println((new StringBuilder(String.valueOf(ConfigFile.PORT))).append(numero).append(Messages.getString("Preferences.23")).append(str).toString());
            configFile.setKeyValue((new StringBuilder(String.valueOf(ConfigFile.PORT))).append(numero).toString(), str);
            str = (new StringBuilder(String.valueOf((String) values.elementAt(i + 3)))).toString();
            configFile.setKeyValue((new StringBuilder(String.valueOf(ConfigFile.EXP))).append(numero).toString(), str);
            numero++;
        }
    }

    private void reset() {
        setDefaultValues();
    }

    private void setDefaultValues() {
        if (configFile != null) {
            jTextFieldRuta.setText(configFile.getKeyValue(Messages.getString("Preferences.24")));
            jTextFieldAncho.setText(configFile.getKeyValue(Messages.getString("Preferences.25")));
            validate();
            saveValues();
        } else {
            System.out.println(Messages.getString("Preferences.26"));
        }
    }

    public void setInitValues() {
        configFile.writeInitData();
        parent.initData();
    }

    private JButton getJButtonRestaurar() {
        if (jButtonRestaurar == null) {
            jButtonRestaurar = new JButton();
            jButtonRestaurar.setText(Messages.getString("Preferences.27"));
            jButtonRestaurar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    reset();
                }
            });
        }
        return jButtonRestaurar;
    }

    private JTextField getJTextFieldRuta() {
        if (jTextFieldRuta == null) {
            jTextFieldRuta = new JTextField();
            jTextFieldRuta.setBounds(new Rectangle(13, 58, 379, 19));
            jTextFieldRuta.setBackground(Color.white);
            jTextFieldRuta.setEditable(false);
        }
        return jTextFieldRuta;
    }

    private JButton getJButtonExplorar() {
        if (jButtonExplorar == null) {
            jButtonExplorar = new JButton();
            jButtonExplorar.setText(Messages.getString("Preferences.28"));
            jButtonExplorar.setSize(new Dimension(110, 24));
            jButtonExplorar.setLocation(new Point(282, 83));
            jButtonExplorar.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    obtenerRuta();
                }
            });
        }
        return jButtonExplorar;
    }

    protected void obtenerRuta() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(0);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == 0) {
            File file = fc.getSelectedFile();
            String str = file.getAbsolutePath();
            jTextFieldRuta.setText(str);
        }
    }

    private JTextField getJTextFieldAncho() {
        if (jTextFieldAncho == null) {
            jTextFieldAncho = new JTextField();
            jTextFieldAncho.setBounds(new Rectangle(228, 30, 55, 23));
            jTextFieldAncho.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
            jTextFieldAncho.setHorizontalAlignment(11);
            jTextFieldAncho.setText(Messages.getString("Preferences.29"));
            jTextFieldAncho.setFont(new Font(Messages.getString("Preferences.30"), 0, 12));
        }
        return jTextFieldAncho;
    }

    private JTextField getJTextFieldCarpetaOrigen() {
        if (jTextFieldCarpetaOrigen == null) {
            jTextFieldCarpetaOrigen = new JTextField();
            jTextFieldCarpetaOrigen.setBounds(new Rectangle(13, 55, 377, 22));
            jTextFieldCarpetaOrigen.setBackground(Color.white);
            jTextFieldCarpetaOrigen.setEditable(false);
        }
        return jTextFieldCarpetaOrigen;
    }

    private JTextField getJTextFieldCarpetaDestino() {
        if (jTextFieldCarpetaDestino == null) {
            jTextFieldCarpetaDestino = new JTextField();
            jTextFieldCarpetaDestino.setBounds(new Rectangle(14, 146, 378, 20));
            jTextFieldCarpetaDestino.setBackground(Color.white);
            jTextFieldCarpetaDestino.setEditable(false);
        }
        return jTextFieldCarpetaDestino;
    }

    private JButton getJButtonExplorarOrigen() {
        if (jButtonExplorarOrigen == null) {
            jButtonExplorarOrigen = new JButton();
            jButtonExplorarOrigen.setBounds(new Rectangle(312, 83, 77, 24));
            jButtonExplorarOrigen.setText(Messages.getString("Preferences.31"));
            jButtonExplorarOrigen.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    obtenerOrigen();
                }
            });
        }
        return jButtonExplorarOrigen;
    }

    protected void obtenerOrigen() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(1);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == 0) {
            File file = fc.getSelectedFile();
            String str = file.getAbsolutePath();
            jTextFieldCarpetaOrigen.setText(str);
        }
    }

    private JButton getJButtonExplorarDestino() {
        if (jButtonExplorarDestino == null) {
            jButtonExplorarDestino = new JButton();
            jButtonExplorarDestino.setBounds(new Rectangle(311, 173, 77, 24));
            jButtonExplorarDestino.setText(Messages.getString("Preferences.32"));
            jButtonExplorarDestino.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    obtenerDestino();
                }
            });
        }
        return jButtonExplorarDestino;
    }

    protected void obtenerDestino() {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(1);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == 0) {
            File file = fc.getSelectedFile();
            String str = file.getAbsolutePath();
            jTextFieldCarpetaDestino.setText(str);
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Preferences thisClass = new Preferences(null);
                thisClass.setDefaultCloseOperation(3);
                thisClass.setVisible(true);
            }
        });
    }

    public Preferences(PacsViewer parent) {
        jContentPane = null;
        jPanel = null;
        jPanel11 = null;
        jPanel12 = null;
        jPanel13 = null;
        jPanel14 = null;
        jButtonAceptar = null;
        jButtonCancelar = null;
        jButtonRestaurar = null;
        jLabel1 = null;
        jTextFieldRuta = null;
        jButtonExplorar = null;
        jLabel11 = null;
        jTextFieldAncho = null;
        jLabel111 = null;
        jLabel1111 = null;
        jTextFieldCarpetaOrigen = null;
        jTextFieldCarpetaDestino = null;
        jButtonExplorarOrigen = null;
        jButtonExplorarDestino = null;
        configFile = null;
        this.parent = null;
        initialize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 408) / 2, (screenSize.height - 500) / 2);
        this.parent = parent;
    }

    public void getInitValues() {
        configFile = new ConfigFile();
        configFile.readFile();
        String str = null;
        int number = Integer.parseInt(configFile.getKeyValue(ConfigFile.NUMBER));
        System.out.println(number);
        str = (new StringBuilder()).append(configFile.getKeyValue((new StringBuilder(String.valueOf(ConfigFile.OS))).toString())).toString();
        System.out.println(Messages.getString("Preferences.33") + str);
        parent.setOS(str);
        str = (new StringBuilder()).append(configFile.getKeyValue((new StringBuilder(String.valueOf(ConfigFile.PARSER))).toString())).toString();
        System.out.println(Messages.getString("Preferences.34") + str);
        parent.setParser(str);
        for (int i = 1; i <= number; i++) {
            Vector vect = new Vector();
            str = (new StringBuilder()).append(configFile.getKeyValue((new StringBuilder(String.valueOf(ConfigFile.AE))).append(i).toString())).toString();
            System.out.println((new StringBuilder(Messages.getString("Preferences.35"))).append(i).append(Messages.getString("Preferences.36")).append(str).toString());
            vect.add(str);
            str = (new StringBuilder()).append(configFile.getKeyValue((new StringBuilder(String.valueOf(ConfigFile.IP))).append(i).toString())).toString();
            System.out.println((new StringBuilder(Messages.getString("Preferences.37"))).append(i).append(Messages.getString("Preferences.38")).append(str).toString());
            vect.add(str);
            str = (new StringBuilder()).append(configFile.getKeyValue((new StringBuilder(String.valueOf(ConfigFile.PORT))).append(i).toString())).toString();
            System.out.println((new StringBuilder(Messages.getString("Preferences.39"))).append(i).append(Messages.getString("Preferences.40")).append(str).toString());
            vect.add(str);
            str = (new StringBuilder()).append(configFile.getKeyValue((new StringBuilder(String.valueOf(ConfigFile.EXP))).append(i).toString())).toString();
            System.out.println((new StringBuilder(Messages.getString("Preferences.41"))).append(i).append(Messages.getString("Preferences.42")).append(str).toString());
            if (str.equals(Messages.getString("Preferences.43"))) vect.add(true); else vect.add(false);
            parent.addRowTableNodos(vect);
        }
    }

    private void initialize() {
        setSize(408, 500);
        setMaximumSize(new Dimension(408, 500));
        setMinimumSize(new Dimension(408, 500));
        setResizable(false);
        setContentPane(getJContentPane());
        setTitle(Messages.getString("Preferences.44"));
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJPanel(), Messages.getString("Preferences.45"));
            jContentPane.add(getJPanel14(), Messages.getString("Preferences.46"));
        }
        return jContentPane;
    }

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane;

    private JPanel jPanel;

    private JPanel jPanel11;

    private JPanel jPanel12;

    private JPanel jPanel13;

    private JPanel jPanel14;

    private JButton jButtonAceptar;

    private JButton jButtonCancelar;

    private JButton jButtonRestaurar;

    private JLabel jLabel1;

    private JTextField jTextFieldRuta;

    private JButton jButtonExplorar;

    private JLabel jLabel11;

    private JTextField jTextFieldAncho;

    private JLabel jLabel111;

    private JLabel jLabel1111;

    private JTextField jTextFieldCarpetaOrigen;

    private JTextField jTextFieldCarpetaDestino;

    private JButton jButtonExplorarOrigen;

    private JButton jButtonExplorarDestino;

    private ConfigFile configFile;

    private PacsViewer parent;
}
