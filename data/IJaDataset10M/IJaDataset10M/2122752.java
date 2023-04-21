package finaltsb.GUI;

import java.io.*;
import javax.swing.*;

/**
 * Gente la idea con esos 2 Label que clavé es como darle una bienvenida...
 * Surgio un problema que no se porque cdo compilo la clase veo el frame solo 
 * vacio sin nada!! --no me acuerdo..pero seguro es alguna propiedad q estoy poniendo mal
 * se fijan?...
 * Ah otra ficha q me callo es si tengo q poner un panel adentro del frame o no 
 * hacia falta..acá no lo puse..en las demás ventanas seguramente lo voy a usar :P
 * Hace años q no hago esto!! jajaja  es como aprender de cero
 * AH y espero q donde puse el paquete este bien :P si no esas cosas se pueden 
 * cambiar :P
 * 
 * @author  ANA
 */
public class Principal extends javax.swing.JFrame {

    /** Creates new form Principal */
    public Principal() {
        initComponents();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jlbBienvenida = new javax.swing.JLabel();
        jlbContenedorImagen = new javax.swing.JLabel();
        jmBarraMenu = new javax.swing.JMenuBar();
        jmMenu = new javax.swing.JMenu();
        jmiAgregar = new javax.swing.JMenuItem();
        jmiBuscar = new javax.swing.JMenuItem();
        jmiEliminar = new javax.swing.JMenuItem();
        jmiMostrar = new javax.swing.JMenuItem();
        jmiCargaMasiva = new javax.swing.JMenuItem();
        jmiSalir = new javax.swing.JMenuItem();
        jmAbout = new javax.swing.JMenu();
        jmAyuda = new javax.swing.JMenu();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(finaltsb.FinalTSBApp.class).getContext().getResourceMap(Principal.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        setResizable(false);
        jPanel1.setName("jPanel1");
        jlbBienvenida.setText(resourceMap.getString("jlbBienvenida.text"));
        jlbBienvenida.setName("jlbBienvenida");
        jlbContenedorImagen.setIcon(resourceMap.getIcon("jlbContenedorImagen.icon"));
        jlbContenedorImagen.setText(resourceMap.getString("jlbContenedorImagen.text"));
        jlbContenedorImagen.setName("jlbContenedorImagen");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(76, 76, 76).addComponent(jlbContenedorImagen)).addGroup(jPanel1Layout.createSequentialGroup().addGap(113, 113, 113).addComponent(jlbBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(75, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(45, 45, 45).addComponent(jlbBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jlbContenedorImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(36, Short.MAX_VALUE)));
        jmBarraMenu.setName("jmBarraMenu");
        jmMenu.setText(resourceMap.getString("jmMenu.text"));
        jmMenu.setName("jmMenu");
        jmiAgregar.setText(resourceMap.getString("jmiAgregar.text"));
        jmiAgregar.setName("jmiAgregar");
        jmiAgregar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAgregarActionPerformed(evt);
            }
        });
        jmMenu.add(jmiAgregar);
        jmiBuscar.setText(resourceMap.getString("jmiBuscar.text"));
        jmiBuscar.setName("jmiBuscar");
        jmiBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiBuscarActionPerformed(evt);
            }
        });
        jmMenu.add(jmiBuscar);
        jmiEliminar.setText(resourceMap.getString("jmiEliminar.text"));
        jmiEliminar.setName("jmiEliminar");
        jmiEliminar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiEliminarActionPerformed(evt);
            }
        });
        jmMenu.add(jmiEliminar);
        jmiMostrar.setText(resourceMap.getString("jmiMostrar.text"));
        jmiMostrar.setName("jmiMostrar");
        jmiMostrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMostrarActionPerformed(evt);
            }
        });
        jmMenu.add(jmiMostrar);
        jmiCargaMasiva.setText(resourceMap.getString("jmiCargaMasiva.text"));
        jmiCargaMasiva.setName("jmiCargaMasiva");
        jmMenu.add(jmiCargaMasiva);
        jmiSalir.setText(resourceMap.getString("jmiSalir.text"));
        jmiSalir.setName("jmiSalir");
        jmiSalir.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiSalirActionPerformed(evt);
            }
        });
        jmMenu.add(jmiSalir);
        jmBarraMenu.add(jmMenu);
        jmAbout.setText(resourceMap.getString("jmAbout.text"));
        jmAbout.setName("jmAbout");
        jmBarraMenu.add(jmAbout);
        jmAyuda.setText(resourceMap.getString("jmAyuda.text"));
        jmAyuda.setName("jmAyuda");
        jmAyuda.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmAyudaActionPerformed(evt);
            }
        });
        jmBarraMenu.add(jmAyuda);
        setJMenuBar(jmBarraMenu);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        getAccessibleContext().setAccessibleParent(jmAyuda);
        pack();
    }

    private void jmiSalirActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void jmiAgregarActionPerformed(java.awt.event.ActionEvent evt) {
        new Agregar().setVisible(true);
    }

    private void jmiBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        new Buscar().setVisible(true);
    }

    private void jmiEliminarActionPerformed(java.awt.event.ActionEvent evt) {
        new Eliminar().setVisible(true);
    }

    private void jmiMostrarActionPerformed(java.awt.event.ActionEvent evt) {
        new Mostrar().setVisible(true);
    }

    private void jmAyudaActionPerformed(java.awt.event.ActionEvent evt) {
        new Ayuda().setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    private javax.swing.JPanel jPanel1;

    private javax.swing.JLabel jlbBienvenida;

    private javax.swing.JLabel jlbContenedorImagen;

    private javax.swing.JMenu jmAbout;

    private javax.swing.JMenu jmAyuda;

    private javax.swing.JMenuBar jmBarraMenu;

    private javax.swing.JMenu jmMenu;

    private javax.swing.JMenuItem jmiAgregar;

    private javax.swing.JMenuItem jmiBuscar;

    private javax.swing.JMenuItem jmiCargaMasiva;

    private javax.swing.JMenuItem jmiEliminar;

    private javax.swing.JMenuItem jmiMostrar;

    private javax.swing.JMenuItem jmiSalir;
}
