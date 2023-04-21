package Formularios;

import analizador.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.synth.SynthLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

public class VentanaPrincipal extends javax.swing.JFrame {

    private Canal muestras[];

    private Dibujo dibujo[];

    private ControlCanal controlCanal[];

    private ControlMonitor controlMonitor;

    private Comunicable comunicador;

    private ModuloExterno moduloExterno;

    private ControlValidador controlValidador;

    public VentanaPrincipal() {
        BasicLookAndFeel met = new MetalLookAndFeel();
        BasicLookAndFeel winC = new WindowsClassicLookAndFeel();
        BasicLookAndFeel syn = new SynthLookAndFeel();
        BasicLookAndFeel mot = new MotifLookAndFeel();
        BasicLookAndFeel win = new WindowsLookAndFeel();
        initComponents();
        initComponentsPropio();
        this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - this.getSize().width) / 2, (Toolkit.getDefaultToolkit().getScreenSize().height - this.getSize().height) / 2);
    }

    private void initComponentsPropio() {
        URL b = getClass().getResource("/Recursos/icono.gif");
        Image a = new ImageIcon(b).getImage();
        this.setIconImage(a);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panelDibujos.setLayout(gbl);
        controlMonitor = new ControlMonitor();
        comunicador = new Comunicador();
        moduloExterno = new ModuloExterno(comunicador);
        controlValidador = new ControlValidador(moduloExterno);
        controlValidador.inicializarModuloExterno(false, 1000);
        dibujo = new Dibujo[8];
        controlCanal = new ControlCanal[8];
        muestras = new Canal[8];
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 100;
        gbc.ipady = 30;
        for (int i = 0; i < 8; i++) {
            muestras[i] = new Canal(i);
            dibujo[i] = new Dibujo(i);
            controlCanal[i] = new ControlCanal(muestras[i], dibujo[i]);
            controlMonitor.addObserver(controlCanal[i]);
            moduloExterno.addObserver(muestras[i]);
            gbl.setConstraints(controlCanal[i], gbc);
            panelDibujos.add(controlCanal[i]);
            gbc.gridy = i + 1;
        }
        gbc.ipadx = 610;
        gbc.ipady = 30;
        gbc.gridx = 1;
        gbc.gridy = 0;
        for (int i = 0; i < 8; i++) {
            gbl.setConstraints(dibujo[i], gbc);
            panelDibujos.add(dibujo[i]);
            gbc.gridy = i + 1;
        }
        gbl.setConstraints(controlMonitor.getPanel(), gbc);
        panelDibujos.add(controlMonitor.getPanel());
        this.getContentPane().setBackground(menu.getBackground());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Salir();
            }
        });
    }

    public void Salir() {
        this.comunicador.cerrarSerie();
        System.exit(0);
    }

    private void initComponents() {
        panelCapturar = new javax.swing.JPanel();
        botonCapturar = new javax.swing.JButton();
        panelModo = new javax.swing.JPanel();
        comboBoxModo = new javax.swing.JComboBox();
        panelFrecuencia = new javax.swing.JPanel();
        comboBoxFrecuencia = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        panelZoom = new javax.swing.JPanel();
        botonZoomIn = new javax.swing.JButton();
        botonZoomOut = new javax.swing.JButton();
        panelDibujos = new javax.swing.JPanel();
        menu = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        menuAbrir = new javax.swing.JMenuItem();
        menuGuardar = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JSeparator();
        menuSalir = new javax.swing.JMenuItem();
        menuAyuda = new javax.swing.JMenu();
        jSeparator14 = new javax.swing.JSeparator();
        menuAcercaDe = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analizador Lógico");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        panelCapturar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Muestras"));
        botonCapturar.setFont(new java.awt.Font("Dialog", 1, 10));
        botonCapturar.setText("Capturar");
        botonCapturar.setFocusable(false);
        botonCapturar.setRequestFocusEnabled(false);
        botonCapturar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCapturarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelCapturarLayout = new javax.swing.GroupLayout(panelCapturar);
        panelCapturar.setLayout(panelCapturarLayout);
        panelCapturarLayout.setHorizontalGroup(panelCapturarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelCapturarLayout.createSequentialGroup().addContainerGap().addComponent(botonCapturar).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panelCapturarLayout.setVerticalGroup(panelCapturarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelCapturarLayout.createSequentialGroup().addComponent(botonCapturar).addContainerGap(13, Short.MAX_VALUE)));
        panelModo.setBorder(javax.swing.BorderFactory.createTitledBorder("Modo"));
        comboBoxModo.setFont(new java.awt.Font("Dialog", 1, 10));
        comboBoxModo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Síncrono", "Asíncrono" }));
        comboBoxModo.setFocusable(false);
        comboBoxModo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxModoActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout panelModoLayout = new javax.swing.GroupLayout(panelModo);
        panelModo.setLayout(panelModoLayout);
        panelModoLayout.setHorizontalGroup(panelModoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelModoLayout.createSequentialGroup().addContainerGap().addComponent(comboBoxModo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panelModoLayout.setVerticalGroup(panelModoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelModoLayout.createSequentialGroup().addComponent(comboBoxModo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(22, Short.MAX_VALUE)));
        panelFrecuencia.setBorder(javax.swing.BorderFactory.createTitledBorder("Frecuencia de muestreo"));
        comboBoxFrecuencia.setFont(new java.awt.Font("Dialog", 1, 10));
        comboBoxFrecuencia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "5", "10", "25", "50", "100", "500", "1000" }));
        comboBoxFrecuencia.setFocusable(false);
        comboBoxFrecuencia.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxFrecuenciaActionPerformed(evt);
            }
        });
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 9));
        jLabel1.setText("KHz ( 1000 uSeg/muestra )");
        javax.swing.GroupLayout panelFrecuenciaLayout = new javax.swing.GroupLayout(panelFrecuencia);
        panelFrecuencia.setLayout(panelFrecuenciaLayout);
        panelFrecuenciaLayout.setHorizontalGroup(panelFrecuenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelFrecuenciaLayout.createSequentialGroup().addContainerGap().addComponent(comboBoxFrecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(82, Short.MAX_VALUE)));
        panelFrecuenciaLayout.setVerticalGroup(panelFrecuenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelFrecuenciaLayout.createSequentialGroup().addGroup(panelFrecuenciaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(comboBoxFrecuencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addContainerGap(22, Short.MAX_VALUE)));
        panelZoom.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Zoom"));
        botonZoomIn.setFont(new java.awt.Font("Dialog", 1, 10));
        botonZoomIn.setText("In");
        botonZoomIn.setFocusable(false);
        botonZoomIn.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonZoomInMouseClicked(evt);
            }
        });
        botonZoomOut.setFont(new java.awt.Font("Dialog", 1, 10));
        botonZoomOut.setText("Out");
        botonZoomOut.setFocusable(false);
        botonZoomOut.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botonZoomOutMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout panelZoomLayout = new javax.swing.GroupLayout(panelZoom);
        panelZoom.setLayout(panelZoomLayout);
        panelZoomLayout.setHorizontalGroup(panelZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelZoomLayout.createSequentialGroup().addContainerGap().addComponent(botonZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(botonZoomOut, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE).addContainerGap()));
        panelZoomLayout.setVerticalGroup(panelZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(panelZoomLayout.createSequentialGroup().addGroup(panelZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(botonZoomOut).addComponent(botonZoomIn)).addContainerGap(13, Short.MAX_VALUE)));
        panelDibujos.setBackground(new java.awt.Color(0, 0, 0));
        panelDibujos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        javax.swing.GroupLayout panelDibujosLayout = new javax.swing.GroupLayout(panelDibujos);
        panelDibujos.setLayout(panelDibujosLayout);
        panelDibujosLayout.setHorizontalGroup(panelDibujosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 804, Short.MAX_VALUE));
        panelDibujosLayout.setVerticalGroup(panelDibujosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 312, Short.MAX_VALUE));
        menuArchivo.setText("Archivo");
        menuAbrir.setText("Abrir");
        menuAbrir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(menuAbrir);
        menuGuardar.setText("Guardar");
        menuGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(menuGuardar);
        menuArchivo.add(jSeparator13);
        menuSalir.setText("Salir");
        menuSalir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(menuSalir);
        menu.add(menuArchivo);
        menuAyuda.setText("Ayuda");
        menuAyuda.add(jSeparator14);
        menuAcercaDe.setText("Acerca de Analizardor");
        menuAcercaDe.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAcercaDeActionPerformed(evt);
            }
        });
        menuAyuda.add(menuAcercaDe);
        menu.add(menuAyuda);
        setJMenuBar(menu);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(panelDibujos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(panelCapturar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelModo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelFrecuencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelZoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(21, 21, 21).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(panelFrecuencia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(panelCapturar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(panelModo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(panelZoom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(panelDibujos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(73, Short.MAX_VALUE)));
        pack();
    }

    private void menuAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Alumnos:\n" + "* Veneranda, Guillermo Federico\n" + "* Jost, Mauricio Gastón\n\n" + "Materia:\n" + "* Sistemas de Computación\n" + "Carrera:\n" + "* Ingeniería en Computación\n" + "* FCEFyN - UNC\n\n" + "Año:\n" + "* 2008\n", "Acerca de...", JOptionPane.INFORMATION_MESSAGE);
    }

    private void botonCapturarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            controlValidador.solicitarInicioMuestreo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void botonZoomInMouseClicked(java.awt.event.MouseEvent evt) {
        ControlMonitor.getControlMonitor().zoomTodo(+1);
    }

    private void botonZoomOutMouseClicked(java.awt.event.MouseEvent evt) {
        ControlMonitor.getControlMonitor().zoomTodo(-1);
    }

    private void menuAbrirActionPerformed(java.awt.event.ActionEvent evt) {
        moduloExterno.cargarArchivo();
        ControlMonitor.getControlMonitor().inicializar();
    }

    private void menuGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        comunicador.guardarMuestras();
    }

    private void comboBoxFrecuenciaActionPerformed(java.awt.event.ActionEvent evt) {
        String velocidad = (String) this.comboBoxFrecuencia.getSelectedItem();
        this.jLabel1.setText("KHz (" + Integer.toString(1000 / (Integer.valueOf(velocidad))) + " uSeg/muestra )");
        int freqHz = Integer.valueOf(velocidad) * 1000;
        try {
            controlValidador.solicitarCambioFreqHz(freqHz);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Frecuencia no valida.", "Error", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        }
    }

    private void comboBoxModoActionPerformed(java.awt.event.ActionEvent evt) {
        String modo = (String) this.comboBoxModo.getSelectedItem();
        boolean es_asincrono = modo.equals("Asíncrono");
        this.comboBoxFrecuencia.setEnabled(!es_asincrono);
        this.jLabel1.setEnabled(!es_asincrono);
        controlValidador.solicitarCambioModo(es_asincrono);
    }

    private void menuSalirActionPerformed(java.awt.event.ActionEvent evt) {
        Salir();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    private javax.swing.JButton botonCapturar;

    private javax.swing.JButton botonZoomIn;

    private javax.swing.JButton botonZoomOut;

    private javax.swing.JComboBox comboBoxFrecuencia;

    private javax.swing.JComboBox comboBoxModo;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JSeparator jSeparator13;

    private javax.swing.JSeparator jSeparator14;

    private javax.swing.JMenuBar menu;

    private javax.swing.JMenuItem menuAbrir;

    private javax.swing.JMenuItem menuAcercaDe;

    private javax.swing.JMenu menuArchivo;

    private javax.swing.JMenu menuAyuda;

    private javax.swing.JMenuItem menuGuardar;

    private javax.swing.JMenuItem menuSalir;

    private javax.swing.JPanel panelCapturar;

    private javax.swing.JPanel panelDibujos;

    private javax.swing.JPanel panelFrecuencia;

    private javax.swing.JPanel panelModo;

    private javax.swing.JPanel panelZoom;
}
