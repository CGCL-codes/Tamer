package org.digitall.projects.gdigitall.expedientes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLEditorKit;
import org.digitall.extras.ekit.Editor;
import org.digitall.lib.components.JIntEntry;
import org.digitall.lib.components.JTFecha;
import org.digitall.projects.gdigitall.lib.components.JCombo;
import org.digitall.projects.gdigitall.lib.components.JEntry;
import org.digitall.projects.gdigitall.lib.components.JTArea;
import org.digitall.projects.gdigitall.lib.components.Login;
import org.digitall.projects.gdigitall.lib.components.SelectorFecha;
import org.digitall.projects.gdigitall.lib.components.dateListen;
import org.digitall.projects.gdigitall.lib.html.HTMLBrowser;
import org.digitall.projects.gdigitall.lib.misc.OP_Proced;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;

public class frmPases extends BasicPrimitivePanel implements ActionListener, KeyListener {

    private JPanel jPanel1 = new JPanel();

    private JPanel jPanel4 = new JPanel();

    private JLabel jLabel6 = new JLabel();

    private JLabel jLabel14 = new JLabel();

    private JLabel jLabel15 = new JLabel();

    private JLabel jLabel25 = new JLabel();

    private JLabel jLabel26 = new JLabel();

    private JLabel jLabel17 = new JLabel();

    private JLabel jLabel16 = new JLabel();

    private JLabel jLabel18 = new JLabel();

    private JLabel jLabel19 = new JLabel();

    private JLabel jLabel27 = new JLabel();

    private JPanel jPanel5 = new JPanel();

    private JLabel jLabel28 = new JLabel();

    private JLabel jLabel110 = new JLabel();

    private JLabel jLabel29 = new JLabel();

    private JLabel jLabel111 = new JLabel();

    private JLabel jLabel112 = new JLabel();

    private JLabel jLabel114 = new JLabel();

    private JLabel jLabel7 = new JLabel();

    private JLabel jLabel4 = new JLabel();

    private JLabel jLabel8 = new JLabel();

    private JLabel jLabel1 = new JLabel();

    private JLabel jLabel2 = new JLabel();

    private JLabel jLabel3 = new JLabel();

    private JLabel jLabel5 = new JLabel();

    private JCombo jcestadop = new JCombo();

    private JCombo jcoficemi = new JCombo();

    private JCombo jcemisor = new JCombo();

    private JCombo jcreceptor = new JCombo();

    private JCombo jcoficrec = new JCombo();

    private JCombo jcorganismo = new JCombo();

    private JCombo jcorganismo2 = new JCombo();

    private JButton boficemi = new JButton();

    private JButton baceptar = new JButton();

    private JButton bcancelar = new JButton();

    private JButton bemisor = new JButton();

    private JEntry jtidoficemi = new JEntry();

    private JTFecha jtfecha = new JTFecha();

    private JEntry jthora = new JEntry();

    private JIntEntry jtfoliose = new JIntEntry();

    private JEntry jtidemisor = new JEntry();

    private JEntry jtidreceptor = new JEntry();

    private JTFecha jtfecha_rec = new JTFecha();

    private JEntry jthora_rec = new JEntry();

    private JEntry jtnroexp = new JEntry();

    private JEntry jtidoficrec = new JEntry();

    private JEntry jtcatastro = new JEntry();

    private JEntry jtidorganismo = new JEntry();

    private JEntry jtidoficrec1 = new JEntry();

    private JEntry jtidorganismo2 = new JEntry();

    private JButton breceptor = new JButton();

    private JButton boficrec = new JButton();

    private JButton bactivar = new JButton();

    private JButton beditor = new JButton();

    private JScrollPane jScrollPane2 = new JScrollPane();

    private JScrollPane jScrollPane3 = new JScrollPane();

    private JTArea jtobservrec = new JTArea();

    private JTabbedPane jTabbedPane1 = new JTabbedPane();

    private JEditorPane jtobservemi = new JEditorPane();

    private String idinst = "", Query = "", catastro = "", accion = "", nroexpte = "", folios = "0", idoficrec = "0", idreceptor = "0", fechapase = "", horapase = "";

    private boolean nuevo = false;

    private String sidreceptor = "0", sidoficrec = "0", sidemisor = "0", sidoficemi = "0", sidorganismo = "0", sidorganismo2 = "0";

    private JPanel centralPanel = new JPanel();

    private Pases parentMain;

    /**
   * NOTA: el tramitre administrativo es el sgte. Una vez creado el documento, se crea el pase, el mismo esta con estado = Enviado,
   * cuando la oficina receptora lo recibe al documento con su pase (de manera fisica), entonces esta debera registrar tal recepcion,
   * y el estado del pase sera Recibido. Esta seria la situacion ideal del sistema, el cual ya esta preparado,
   * lo cual hasta q se de esta situacion se debera utilizar el Estado=Grabado, por el contrario
   * cuando se llegue a la situacion ideal se debera inhabilitar este estado, 
   * <ok capoooo entendes no???, jijijji ta medio dificil pero gueee>
   * 
   * FORMULARIO PARA CARGAR O MODIFICAR LOS PASES DE UN DOCUMENTO. TAMBIEN FUNCIONA PARA REGISTRAR LA RECEPCION DE UN DOCUMENTO EN LA OFICINA CORRESPONDIENTE
   * 
   * @param IDReceptor: ID DEL PERSONAL RECEPTOR
   * @param IDOficRec: ID DE LA OFICINA RECEPTORA
   * @param Folios: CANTIDAD DE FOLIOS
   * @param Accion: CUANDO ESTA VBLE NO ES VACIA INDICA Q SE TRATA DE UNA RECEPCION DEL PASE.
   * @param NroCatastro: NUMERO DE CATASTRO DEL DOCUMENTO AL QUE SE ESTA POR AGREGAR/MODIFICAR PASES
   * @param NroExpte: NUMERO DE DOCUMENTO
   * @param IDInst: ID DEL DOCUMENTO
   * @param SQLQuery: INDICA MODIFICACION
   */
    public frmPases(String SQLQuery, String IDInst, String NroExpte, String NroCatastro, String Accion, String Folios, String IDOficRec, String IDReceptor) {
        try {
            Query = SQLQuery;
            accion = Accion;
            nroexpte = NroExpte;
            catastro = NroCatastro;
            folios = Folios;
            idinst = IDInst;
            idoficrec = IDOficRec;
            idreceptor = IDReceptor;
            jbInit();
        } catch (SQLException e) {
            OP_Proced.Mensaje(e.getMessage().toUpperCase(), "Acceso a la Base de Datos no Autorizado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        beditor.setText("Editor");
        this.setSize(new Dimension(796, 532));
        this.setTitle("Agregar/Modificar un Pase de un Documento");
        jLabel6.setText("Fecha Pase:");
        jLabel6.setBounds(new Rectangle(330, 15, 70, 15));
        jtfecha.setBounds(new Rectangle(330, 30, 90, 20));
        jtfecha.setBackground(new Color(255, 255, 220));
        jtfecha.setDisabledTextColor(new Color(153, 15, 76));
        jtfecha.setFont(new Font("Dialog", 1, 12));
        jLabel14.setText("Estado:");
        jLabel14.setBounds(new Rectangle(595, 15, 45, 15));
        jcestadop.setBounds(new Rectangle(595, 30, 125, 20));
        jPanel4.setLayout(null);
        jPanel4.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jLabel15.setText("Oficina Emisora:");
        jLabel15.setBounds(new Rectangle(170, 80, 101, 15));
        jcoficemi.setBounds(new Rectangle(170, 95, 545, 20));
        boficemi.setMnemonic('p');
        boficemi.setText("...");
        boficemi.setBounds(new Rectangle(720, 94, 45, 23));
        jtidoficemi.setBounds(new Rectangle(10, 95, 130, 20));
        jtidoficemi.setText("0");
        jLabel25.setText("Buscar Ofic. Emisora:");
        jLabel25.setBounds(new Rectangle(10, 80, 130, 15));
        baceptar.setText("Aceptar");
        baceptar.setBounds(new Rectangle(495, 490, 105, 25));
        baceptar.setMnemonic('a');
        bcancelar.setText("Cancelar");
        bcancelar.setBounds(new Rectangle(620, 490, 110, 25));
        bcancelar.setMnemonic('c');
        jPanel1.setBounds(new Rectangle(5, 20, 780, 55));
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jPanel1.setLayout(null);
        jLabel26.setText(" Datos Principales: ");
        jLabel26.setBounds(new Rectangle(20, 10, 140, 15));
        jLabel26.setFont(new Font("Dialog", 1, 14));
        jLabel26.setOpaque(true);
        jLabel17.setText("Texto del Pase:");
        jLabel17.setBounds(new Rectangle(105, 185, 94, 24));
        jthora.setBounds(new Rectangle(435, 30, 70, 20));
        jthora.setBackground(new Color(255, 255, 220));
        jthora.setFont(new Font("Dialog", 1, 12));
        jLabel16.setText("Hora Pase:");
        jLabel16.setBounds(new Rectangle(435, 15, 64, 15));
        jtfoliose.setBounds(new Rectangle(520, 30, 41, 20));
        jLabel18.setText("Folios:");
        jLabel18.setBounds(new Rectangle(520, 15, 41, 15));
        bemisor.setMnemonic('p');
        bemisor.setText("...");
        bemisor.setBounds(new Rectangle(720, 149, 45, 23));
        bemisor.setEnabled(false);
        jtidemisor.setBounds(new Rectangle(10, 150, 130, 20));
        jtidemisor.setText("0");
        jtidemisor.setEditable(false);
        jcemisor.setBounds(new Rectangle(170, 150, 545, 20));
        jcemisor.setEnabled(false);
        jLabel19.setText("Emisor:");
        jLabel19.setBounds(new Rectangle(170, 135, 46, 15));
        jLabel27.setText("Buscar Emisor:");
        jLabel27.setBounds(new Rectangle(10, 135, 91, 15));
        jPanel5.setLayout(null);
        jPanel5.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        jLabel28.setText("Buscar Receptor:");
        jLabel28.setBounds(new Rectangle(10, 160, 102, 20));
        jLabel110.setText("Receptor:");
        jLabel110.setBounds(new Rectangle(180, 165, 57, 15));
        jcreceptor.setBounds(new Rectangle(180, 180, 535, 20));
        jcreceptor.setEnabled(false);
        jtidreceptor.setBounds(new Rectangle(10, 180, 141, 20));
        jtidreceptor.setText("0");
        jtidreceptor.setEditable(false);
        breceptor.setMnemonic('p');
        breceptor.setText("...");
        breceptor.setBounds(new Rectangle(725, 178, 45, 24));
        breceptor.setEnabled(false);
        jLabel29.setText("Buscar Ofic. Receptora:");
        jLabel29.setBounds(new Rectangle(10, 107, 141, 20));
        jtidoficrec.setBounds(new Rectangle(10, 125, 141, 20));
        jtidoficrec.setText("0");
        jScrollPane2.setBounds(new Rectangle(10, 245, 755, 125));
        jtobservrec.setBounds(new Rectangle(0, 0, 0, 15));
        jtobservrec.setLineWrap(true);
        jtobservrec.setMargin(new Insets(5, 5, 5, 5));
        jtobservrec.setWrapStyleWord(true);
        jLabel111.setText("Observaciones:");
        jLabel111.setBounds(new Rectangle(10, 230, 94, 15));
        jcoficrec.setBounds(new Rectangle(180, 125, 535, 20));
        boficrec.setMnemonic('p');
        boficrec.setText("...");
        boficrec.setBounds(new Rectangle(725, 123, 45, 24));
        jLabel112.setText("Ofic. Receptora:");
        jLabel112.setBounds(new Rectangle(180, 110, 96, 15));
        jthora_rec.setBounds(new Rectangle(315, 20, 70, 20));
        jthora_rec.setEditable(false);
        jLabel114.setText("Hora Recepcion:");
        jLabel114.setBounds(new Rectangle(215, 20, 98, 20));
        jLabel114.setOpaque(true);
        jtfecha_rec.setBounds(new Rectangle(115, 20, 80, 20));
        jtfecha_rec.setEditable(false);
        jLabel7.setText("Fecha Recepcion:");
        jLabel7.setBounds(new Rectangle(10, 20, 104, 20));
        jLabel7.setOpaque(true);
        jtnroexp.setBounds(new Rectangle(10, 30, 165, 20));
        jtnroexp.setEditable(false);
        jLabel4.setText("N� Documento:");
        jLabel4.setBounds(new Rectangle(10, 15, 94, 15));
        jtcatastro.setBounds(new Rectangle(190, 30, 115, 20));
        jtcatastro.setEditable(false);
        jLabel8.setText("N� Catastro:");
        jLabel8.setBounds(new Rectangle(190, 15, 75, 15));
        jcorganismo.setBounds(new Rectangle(170, 45, 545, 20));
        jLabel1.setText("Organismo:");
        jLabel1.setBounds(new Rectangle(170, 30, 72, 15));
        jLabel2.setText("Buscar Organismo:");
        jLabel2.setBounds(new Rectangle(10, 30, 117, 15));
        jtidorganismo.setBounds(new Rectangle(10, 45, 130, 19));
        jtidorganismo.setText("0");
        jTabbedPane1.setBounds(new Rectangle(5, 85, 780, 400));
        jcorganismo2.setBounds(new Rectangle(180, 75, 535, 20));
        jtidoficrec1.setBounds(new Rectangle(5, 80, 155, 20));
        jtidorganismo2.setText("0");
        jtidorganismo2.setBounds(new Rectangle(10, 75, 141, 19));
        jLabel3.setText("Organismo:");
        jLabel3.setBounds(new Rectangle(180, 60, 72, 15));
        jLabel5.setText("Buscar Organismos:");
        jLabel5.setBounds(new Rectangle(10, 60, 124, 15));
        bactivar.setBounds(new Rectangle(735, 25, 40, 25));
        bactivar.setMargin(new Insets(2, 15, 2, 14));
        beditor.setBounds(new Rectangle(10, 185, 88, 24));
        beditor.setMargin(new Insets(2, 5, 2, 14));
        beditor.setMnemonic('e');
        jtobservemi.setEditable(false);
        jtobservemi.setEditorKit(new HTMLEditorKit());
        jScrollPane3.setBounds(new Rectangle(10, 210, 755, 160));
        centralPanel.setBounds(new Rectangle(0, 0, 790, 520));
        centralPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        centralPanel.setLayout(null);
        jPanel5.add(jLabel5, null);
        jPanel5.add(jLabel3, null);
        jPanel5.add(jtidorganismo2, null);
        jPanel5.add(jLabel28, null);
        jPanel5.add(jLabel110, null);
        jPanel5.add(jcreceptor, null);
        jPanel5.add(jtidreceptor, null);
        jPanel5.add(breceptor, null);
        jPanel5.add(jLabel29, null);
        jPanel5.add(jtidoficrec, null);
        jScrollPane2.getViewport().add(jtobservrec, null);
        jPanel5.add(jScrollPane2, null);
        jPanel5.add(jLabel111, null);
        jPanel5.add(jcoficrec, null);
        jPanel5.add(boficrec, null);
        jPanel5.add(jLabel112, null);
        jPanel5.add(jtfecha_rec, null);
        jPanel5.add(jLabel7, null);
        jPanel5.add(jthora_rec, null);
        jPanel5.add(jLabel114, null);
        jPanel5.add(jcorganismo2, null);
        jScrollPane3.getViewport().add(jtobservemi, null);
        jPanel4.add(jScrollPane3, null);
        jPanel4.add(beditor, null);
        jPanel4.add(jtidorganismo, null);
        jPanel4.add(jLabel2, null);
        jPanel4.add(jLabel1, null);
        jPanel4.add(jLabel27, null);
        jPanel4.add(jLabel19, null);
        jPanel4.add(jcemisor, null);
        jPanel4.add(jtidemisor, null);
        jPanel4.add(bemisor, null);
        jPanel4.add(jLabel25, null);
        jPanel4.add(jtidoficemi, null);
        jPanel4.add(jLabel17, null);
        jPanel4.add(jcoficemi, null);
        jPanel4.add(boficemi, null);
        jPanel4.add(jLabel15, null);
        jPanel4.add(jcorganismo, null);
        jTabbedPane1.addTab("Datos del Emisor", jPanel4);
        jTabbedPane1.addTab("Datos del Receptor", jPanel5);
        centralPanel.add(jLabel26, null);
        centralPanel.add(jPanel1, null);
        centralPanel.add(jTabbedPane1, null);
        centralPanel.add(bcancelar, null);
        centralPanel.add(baceptar, null);
        jPanel1.add(jLabel8, null);
        jPanel1.add(jtcatastro, null);
        jPanel1.add(jLabel4, null);
        jPanel1.add(jtnroexp, null);
        jPanel1.add(jLabel18, null);
        jPanel1.add(jtfoliose, null);
        jPanel1.add(jLabel16, null);
        jPanel1.add(jthora, null);
        jPanel1.add(jcestadop, null);
        jPanel1.add(jLabel14, null);
        jPanel1.add(jtfecha, null);
        jPanel1.add(jLabel6, null);
        jPanel1.add(bactivar, null);
        this.add(centralPanel, null);
        jthora.addKeyListener(new dateListen(false));
        jtidoficrec.addKeyListener(this);
        jtidoficemi.addKeyListener(this);
        jtidemisor.addKeyListener(this);
        jtidreceptor.addKeyListener(this);
        baceptar.addActionListener(this);
        bcancelar.addActionListener(this);
        boficemi.addActionListener(this);
        bemisor.addActionListener(this);
        boficrec.addActionListener(this);
        breceptor.addActionListener(this);
        jtidorganismo.addKeyListener(this);
        jtidorganismo2.addKeyListener(this);
        bactivar.addActionListener(this);
        beditor.addActionListener(this);
        jtnroexp.setText(nroexpte);
        jtcatastro.setText(catastro);
        jthora_rec.addKeyListener(new dateListen(false));
        jcreceptor.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    jtidreceptor.setText(OP_Proced.getCampo("SELECT idpersona FROM personas WHERE (apellido||', '||nombre)='" + jcreceptor.getItemTexto() + "'"));
                    sidreceptor = jtidreceptor.getText();
                }
            }
        });
        jcoficrec.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    jtidoficrec.setText(OP_Proced.getCampo("SELECT tiposoficina.idtipo FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idorganismo=" + jtidorganismo2.getTexto() + " AND tiposoficina.idarea=tiposarea.idarea" + " AND tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'='" + jcoficrec.getSelectedItem() + "'"));
                    sidoficrec = jtidoficrec.getText();
                }
            }
        });
        jcemisor.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    jtidemisor.setText(OP_Proced.getCampo("SELECT idpersona FROM personas WHERE (apellido||', '||nombre)='" + jcemisor.getItemTexto() + "'"));
                    sidemisor = jtidemisor.getText();
                }
            }
        });
        jcoficemi.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    jtidoficemi.setText(OP_Proced.getCampo("SELECT tiposoficina.idtipo FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idorganismo=" + jtidorganismo.getTexto() + " AND tiposoficina.idarea=tiposarea.idarea" + " AND tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'='" + jcoficemi.getSelectedItem() + "'"));
                    sidoficemi = jtidoficemi.getText();
                }
            }
        });
        jcorganismo.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                jtidorganismo.setText(OP_Proced.getCampo("SELECT idorganismo FROM files.tiposorganismo WHERE descripcion='" + jcorganismo.getSelectedItem() + "'"));
                sidorganismo = jtidorganismo.getText();
            }
        });
        jcorganismo2.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    jtidorganismo2.setText(OP_Proced.getCampo("SELECT idorganismo FROM files.tiposorganismo WHERE descripcion='" + jcorganismo2.getSelectedItem() + "'"));
                    sidorganismo2 = jtidorganismo2.getText();
                }
            }
        });
        jcestadop.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    if (jcestadop.getItemCount() >= 1) {
                        if (jcestadop.getSelectedItem().toString().equals("Grabado")) {
                            ActivaCampo(true);
                        } else {
                            ActivaCampo(false);
                            if (jcestadop.getSelectedItem().toString().equals("Recibido")) {
                                jcestadop.setEnabled(false);
                                ActivaCampo2(false);
                            } else {
                                ActivaCampo2(true);
                            }
                        }
                    }
                }
            }
        });
        if (Query.length() > 0) {
            ResultSet Reg = OP_Proced.exConsulta(Query);
            if (Reg.next()) {
                nuevo = false;
                idinst = Reg.getString(1);
                fechapase = Reg.getString(2);
                horapase = Reg.getString(3);
                jtfecha.setText(OP_Proced.Fecha2(Reg.getString(2), true));
                jthora.setText(OP_Proced.Hora(Reg.getString(3), true, false));
                OP_Proced.CargaCombo(jcoficemi, "SELECT tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'" + " FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idtipo=" + Reg.getString(4) + " AND tiposoficina.idarea=tiposarea.idarea", "");
                jtidoficemi.setText(Reg.getString(4));
                sidoficemi = Reg.getString(4);
                jtidorganismo.setText(OP_Proced.getCampo("SELECT idorganismo FROM files.tiposoficina WHERE idtipo=" + Reg.getString(4)));
                sidorganismo = jtidorganismo.getText();
                OP_Proced.CargaCombo(jcorganismo, "SELECT descripcion FROM files.tiposorganismo WHERE idorganismo=" + jtidorganismo.getTexto(), "");
                OP_Proced.CargaCombo(jcemisor, "SELECT apellido||', '||nombre FROM personas WHERE idpersona=" + Reg.getString(5), "");
                jtidemisor.setText(Reg.getString(5));
                sidemisor = Reg.getString(5);
                jtobservemi.setText(Reg.getString(6));
                OP_Proced.CargaCombo(jcoficrec, "SELECT tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'" + " FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idtipo=" + Reg.getString(7) + " AND tiposoficina.idarea=tiposarea.idarea", "");
                jtidoficrec.setText(Reg.getString(7));
                sidoficrec = Reg.getString(7);
                jtidorganismo2.setText(OP_Proced.getCampo("SELECT idorganismo FROM files.tiposoficina WHERE idtipo=" + Reg.getString(7)));
                sidorganismo2 = jtidorganismo2.getText();
                OP_Proced.CargaCombo(jcorganismo2, "SELECT descripcion FROM files.tiposorganismo WHERE idorganismo=" + jtidorganismo2.getTexto(), "");
                OP_Proced.CargaCombo(jcreceptor, "SELECT apellido||', '||nombre FROM personas WHERE idpersona=" + Reg.getString(8), "");
                jtidreceptor.setText(Reg.getString(8));
                jtobservrec.setText(Reg.getString(9));
                jtfoliose.setText(Reg.getString(10));
                if (!accion.equals("")) {
                    jtfecha_rec.setText(OP_Proced.FechaHora(true, false));
                    jthora_rec.setText(OP_Proced.FechaHora(false, true));
                    OP_Proced.CargaCombo(jcreceptor, "SELECT apellido||', '||nombre FROM personas WHERE alias='" + OP_Proced.getSQLUser() + "'", "");
                    ActivaReceptor("Recibido", false);
                } else {
                    jtfecha_rec.setText(OP_Proced.Fecha2(OP_Proced.TransformaNull_Texto(Reg.getString(11)), true));
                    jthora_rec.setText(OP_Proced.Hora(OP_Proced.TransformaNull_Texto(Reg.getString(12)), true, false));
                    ActivaReceptor(Reg.getString(13), true);
                }
                jtfecha_rec.setEnabled(true);
                jthora_rec.setEnabled(true);
                jthora_rec.setEditable(true);
            }
        } else {
            nuevo = true;
            jtidreceptor.setEditable(false);
            breceptor.setEnabled(false);
            jcreceptor.setEnabled(false);
            jtobservrec.setEditable(false);
            jtfecha.setText(OP_Proced.FechaHora2(true, false));
            jthora.setText(OP_Proced.FechaHora2(false, true));
            jcestadop.addItem("Grabado");
            OP_Proced.CargaCombo(jcoficemi, "SELECT tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'" + " FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idtipo=" + idoficrec + " AND tiposoficina.idarea=tiposarea.idarea", "");
            jtidoficemi.setText(idoficrec);
            sidoficemi = jtidoficemi.getText();
            OP_Proced.CargaCombo(jcorganismo, "SELECT descripcion FROM files.tiposorganismo WHERE idorganismo=213", "");
            OP_Proced.CargaCombo(jcemisor, "SELECT apellido||', '||nombre FROM personas WHERE alias='" + OP_Proced.getSQLUser() + "'", "");
            OP_Proced.CargaCombo(jcorganismo2, "SELECT descripcion FROM files.tiposorganismo WHERE idorganismo=213", "");
            if (folios.length() > 0) {
                jtfoliose.setText(folios);
            } else {
                jtfoliose.setText(OP_Proced.getCampo("SELECT cantfolios FROM files.instlegal WHERE idinst=" + idinst));
            }
            jtfecha_rec.setEnabled(false);
            jthora_rec.setEnabled(false);
            jthora_rec.setEditable(false);
        }
        boficemi.setVisible(false);
        bemisor.setVisible(false);
        boficrec.setVisible(false);
        breceptor.setVisible(false);
        bactivar.setVisible(false);
        jtfecha.setEditable(false);
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
        super.setParentInternalFrame(_e);
        getParentInternalFrame().setInfo("Frm. Pases");
    }

    /** EN EL CASO DE SER UNA RECEPCION SE ACTIVA ESTO CAMPOS DE OTRA FORMA NO*/
    private void ActivaReceptor(String estado, boolean activa) {
        OP_Proced.CargaCombo(jcestadop, "SELECT descripcion FROM files.tablacombo WHERE combo='jcestadop'", estado);
        jtidoficemi.setEditable(activa);
        jcoficemi.setEnabled(activa);
        jtidorganismo.setEditable(activa);
        jcorganismo.setEnabled(activa);
        if (estado.equals("Recibido")) jcestadop.setEnabled(false);
    }

    private void ActivaCampo2(boolean op) {
        jtidoficemi.setEditable(op);
        jcoficemi.setEnabled(op);
        jtidorganismo.setEditable(op);
        jcorganismo.setEnabled(op);
        jtidorganismo2.setEditable(op);
        jtidoficrec.setEditable(op);
        jcoficrec.setEnabled(op);
        boficrec.setEnabled(op);
    }

    private void ActivaCampo(boolean op) {
        System.out.println("ActivaCampo");
        bemisor.setEnabled(op);
        jtidemisor.setEditable(op);
        jcemisor.setEnabled(op);
        breceptor.setEnabled(op);
        jtidreceptor.setEditable(op);
        jcreceptor.setEnabled(op);
        jtidorganismo2.setEditable(op);
        jcorganismo.setEnabled(op);
        jtidoficrec.setEditable(op);
        jcoficrec.setEnabled(op);
        boficrec.setEnabled(op);
        jthora_rec.setEditable(op);
    }

    private void ImprimirTextoPase() {
        int result = JOptionPane.showConfirmDialog((Component) null, "�Desea imprimir el Texto del Pase?", "Texto del Pase", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                HTMLBrowser navegador = new HTMLBrowser(OP_Proced.getRutaInforme() + OP_Proced.getSeparador() + "texto_pase" + idinst + ".html");
                navegador.setModal(true);
                navegador.setVisible(true);
            } catch (FileNotFoundException f) {
                f.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == bcancelar) {
                getParentInternalFrame().close();
            } else if (e.getSource() == baceptar) {
                if (new Integer(jtfoliose.getText()) > 0) {
                    if (Query.length() > 0) {
                        String Where = "";
                        String Q = "UPDATE files.pases SET fechapase='" + OP_Proced.Fecha2(jtfecha.getText(), false) + "',horapase='" + OP_Proced.Hora(jthora.getText(), false, false) + "',idoficemi=" + sidoficemi + ",idemisor=" + sidemisor + ",observemi='" + jtobservemi.getText() + "',idoficrec=" + sidoficrec + ",idreceptor=" + sidreceptor + ",observrec='" + jtobservrec.getTexto() + "',cfoliorec=" + jtfoliose.getText() + ",estadopase='" + jcestadop.getSelectedItem() + "', fecha_rec=" + OP_Proced.TransformaTexto_Null(OP_Proced.Fecha2(jtfecha_rec.getText(), false)) + ", hora_rec=" + OP_Proced.TransformaTexto_Null(OP_Proced.Hora(jthora_rec.getTexto(), false, false)) + " WHERE idinst=" + idinst + " AND fechapase='" + fechapase + "' AND horapase='" + horapase + "'";
                        System.out.println(Q);
                        if (OP_Proced.exActualizar('m', Q)) {
                            ResultSet Reg = OP_Proced.exConsulta("SELECT idinstref FROM files.instxinst WHERE idinst=" + idinst);
                            while (Reg.next()) {
                                String QQ = "UPDATE files.pases SET idoficemi=" + sidoficemi + ",idemisor=" + sidemisor + ",observemi='" + jtobservemi.getText() + "',idoficrec=" + sidoficrec + ",idreceptor=" + sidreceptor + ",observrec='" + jtobservrec.getTexto() + "',cfoliorec=" + jtfoliose.getText() + ",estadopase='" + jcestadop.getSelectedItem() + "',fecha_rec=" + OP_Proced.TransformaTexto_Null(OP_Proced.Fecha2(jtfecha_rec.getText(), false)) + ",hora_rec=" + OP_Proced.TransformaTexto_Null(OP_Proced.Hora(jthora_rec.getTexto(), false, false)) + " WHERE idinst=" + Reg.getString(1) + " AND fechapase='" + OP_Proced.Fecha2(jtfecha.getText(), false) + "' AND horapase='" + jthora.getTexto() + "'";
                                System.out.println(QQ);
                                OP_Proced.exActualizar('m', QQ);
                            }
                            parentMain.ActualizaTabla();
                            getParentInternalFrame().close();
                        }
                    } else {
                        if (!jtidoficrec.getTexto().equals("0")) {
                            System.out.println("jtidemisor.getTexto(): " + jtidemisor.getTexto());
                            if (!jtidoficemi.getTexto().equals(jtidoficrec.getTexto())) {
                                String Q = "INSERT INTO files.pases VALUES(" + idinst + ",'" + OP_Proced.Fecha2(jtfecha.getText(), false) + "','" + jthora.getTexto() + "'," + sidoficemi + "," + sidemisor + ",'" + jtobservemi.getText() + "'," + sidoficrec + "," + sidreceptor + ",'" + jtobservrec.getTexto() + "'," + jtfoliose.getText() + "," + OP_Proced.TransformaTexto_Null(OP_Proced.Fecha2(jtfecha_rec.getText(), false)) + "," + OP_Proced.TransformaTexto_Null(OP_Proced.Hora(jthora_rec.getTexto(), false, false)) + ",'" + jcestadop.getSelectedItem() + "','')";
                                System.out.println(Q);
                                if (OP_Proced.exActualizar('a', Q)) {
                                    ResultSet Reg = OP_Proced.exConsulta("SELECT idinstref FROM files.instxinst WHERE idinst=" + idinst);
                                    while (Reg.next()) {
                                        String QQ = "INSERT INTO files.pases VALUES(" + Reg.getString(1) + ",'" + OP_Proced.Fecha2(jtfecha.getText(), false) + "','" + jthora.getTexto() + "'," + sidoficemi + "," + sidemisor + ",'" + jtobservemi.getText() + "'," + sidoficrec + "," + sidreceptor + ",'" + jtobservrec.getTexto() + "'," + jtfoliose.getText() + "," + OP_Proced.TransformaTexto_Null(OP_Proced.Fecha2(jtfecha_rec.getText(), false)) + "," + OP_Proced.TransformaTexto_Null(OP_Proced.Hora(jthora_rec.getTexto(), false, false)) + ",'" + jcestadop.getSelectedItem() + "','')";
                                        System.out.println(QQ);
                                        OP_Proced.exActualizar('a', QQ);
                                    }
                                    parentMain.ActualizaTabla();
                                    getParentInternalFrame().close();
                                }
                                if (jtidoficrec.getTexto().equals("213")) {
                                    OP_Proced.exActualizar('a', "UPDATE files.instlegal SET estadoexp='Archivo Gral.' WHERE idinst=" + idinst);
                                }
                            } else {
                                OP_Proced.Mensaje("La Oficina Receptora no puede ser la misma que la Oficina Emisora", "Oficina Receptora invalida");
                            }
                        } else {
                            OP_Proced.Mensaje("Debe indicar una oficina receptora", "Oficina invalida");
                        }
                    }
                } else {
                    OP_Proced.Mensaje("La cantidad de Folios debe ser mayor a 0", "Cantidad de Folios incorrecta");
                }
            } else if (e.getSource() == boficrec) {
                Oficinas oficrec = new Oficinas();
                oficrec.setVisible(true);
                OP_Proced.CargaCombo(jcoficrec, "SELECT tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'" + " FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idtipo=" + oficrec.getIDOfic() + " AND tiposoficina.idarea=tiposarea.idarea", "");
                OP_Proced.CargaCombo(jcorganismo2, "SELECT descripcion FROM files.tiposorganismo WHERE idorganismo=" + oficrec.getIDOrg(), "");
            } else if (e.getSource() == boficemi) {
                Oficinas oficemi = new Oficinas();
                oficemi.setVisible(true);
                OP_Proced.CargaCombo(jcoficemi, "SELECT tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'" + " FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.idtipo=" + oficemi.getIDOfic() + " AND tiposoficina.idarea=tiposarea.idarea", "");
                OP_Proced.CargaCombo(jcorganismo, "SELECT descripcion FROM files.tiposorganismo WHERE idorganismo=" + oficemi.getIDOrg(), "");
            } else if (e.getSource() == breceptor) {
            } else if (e.getSource() == bemisor) {
            } else if (e.getSource() == bactivar) {
            } else if (e.getSource() == beditor) {
                Editor editor = new Editor("Texto del Pase", "Correspondiente al Documento N� " + jtnroexp.getText(), true, "texto_pase" + idinst, nuevo, jtobservemi.getText());
                OP_Proced.CentraVentana(editor);
                editor.setModal(true);
                editor.setVisible(true);
                if (editor.getCodigoFuente().length() > 0) {
                    jtobservemi.setText(editor.getCodigoFuente());
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void keyReleased(KeyEvent k) {
    }

    public void keyTyped(KeyEvent k) {
    }

    public void keyPressed(KeyEvent k) {
        int id = 99999999;
        String texto = "", Q = "", idorg = "";
        JCombo combo;
        if (k.getKeyCode() == KeyEvent.VK_ENTER) {
            if (k.getSource() == jtidoficemi || k.getSource() == jtidoficrec) {
                if (k.getSource() == jtidoficemi) {
                    texto = jtidoficemi.getTexto();
                    idorg = jtidorganismo.getTexto();
                    combo = jcoficemi;
                } else {
                    texto = jtidoficrec.getTexto();
                    idorg = jtidorganismo2.getTexto();
                    combo = jcoficrec;
                }
                try {
                    id = Integer.parseInt(texto);
                } catch (NumberFormatException n) {
                }
                Q = "SELECT tiposoficina.descripcion||' - ('||tiposarea.descripcion||')'" + " FROM files.tiposoficina,files.tiposarea" + " WHERE tiposoficina.estado<>'*'" + " AND (idtipo=" + id + " OR upper(tiposoficina.descripcion) LIKE upper('%" + texto + "%'))" + " AND tiposoficina.idorganismo=" + idorg + " AND tiposarea.idorganismo=" + idorg + " AND tiposoficina.idarea=tiposarea.idarea ORDER BY tiposoficina.descripcion";
                System.out.println(Q);
                combo.setForeground(Color.red);
                OP_Proced.CargaCombo(combo, Q, "");
            } else if (k.getSource() == jtidemisor || k.getSource() == jtidreceptor) {
                if (k.getSource() == jtidemisor) {
                    texto = jtidemisor.getTexto();
                    combo = jcemisor;
                } else {
                    texto = jtidreceptor.getTexto();
                    combo = jcreceptor;
                }
                try {
                    id = Integer.parseInt(texto);
                } catch (NumberFormatException n) {
                }
                Q = "SELECT apellido||', '||nombre FROM personas WHERE estado<>'*' AND (idpersona=" + id + " or upper(apellido) LIKE upper('%" + texto + "%') or upper(nombre) LIKE upper('%" + texto + "%')) order by apellido,nombre";
                OP_Proced.CargaCombo(combo, Q, "");
            } else if (k.getSource() == jtidorganismo || k.getSource() == jtidorganismo2) {
                if (k.getSource() == jtidorganismo) {
                    texto = jtidorganismo.getTexto();
                    combo = jcorganismo;
                } else {
                    texto = jtidorganismo2.getTexto();
                    combo = jcorganismo2;
                }
                try {
                    id = Integer.parseInt(texto);
                } catch (NumberFormatException n) {
                }
                Q = "SELECT descripcion FROM files.tiposorganismo WHERE estado<>'*'" + " AND (idorganismo=" + id + " OR UPPER(descripcion) LIKE UPPER('%" + texto + "%')) ORDER BY descripcion";
                System.out.println(Q);
                OP_Proced.CargaCombo(combo, Q, "");
            }
        }
    }

    public void setParentMain(Pases parentMain) {
        this.parentMain = parentMain;
    }
}
