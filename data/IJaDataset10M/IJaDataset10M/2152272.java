package gnu.chu.anjelica.pad;

import gnu.chu.controles.*;
import gnu.chu.utilidades.*;
import gnu.chu.sql.*;
import java.sql.*;
import java.awt.*;
import java.util.*;
import gnu.chu.interfaces.*;
import java.text.*;
import javax.swing.*;
import gnu.chu.Menu.*;
import java.awt.event.*;
import gnu.chu.anjelica.pad.pdconfig;

/**
*
* <p>T�tulo: pddiscrim </p>
* <p>Descripci�n: Mantenimiento de Discriminadores</p>
* <p>Empresa: miCasa</p>
* <p>Copyright: Copyright (c) 2005
*  Este programa es software libre. Puede redistribuirlo y/o modificarlo bajo
*  los t�rminos de la Licencia P�blica General de GNU seg�n es publicada por
*  la Free Software Foundation, bien de la versi�n 2 de dicha Licencia
*  o bien (seg�n su elecci�n) de cualquier versi�n posterior.
*  Este programa se distribuye con la esperanza de que sea �til,
*  pero SIN NINGUNA GARANT�A, incluso sin la garant�a MERCANTIL impl�cita
*  o sin garantizar la CONVENIENCIA PARA UN PROP�SITO PARTICULAR.
*  V�ase la Licencia P�blica General de GNU para m�s detalles.
*  Deber�a haber recibido una copia de la Licencia P�blica General junto con este programa.
*  Si no ha sido as�, escriba a la Free Software Foundation, Inc.,
*  en 675 Mass Ave, Cambridge, MA 02139, EEUU.
* </p>
* @author chuchiP
* @version 1.0
*/
public class pddiscrim extends ventanaPad implements PAD {

    String s;

    vlike lkConf = new vlike();

    boolean modConsulta = false;

    CPanel Pprinc = new CPanel();

    CPanel Pcabe = new CPanel();

    CLabel cLabel1 = new CLabel();

    CComboBox dis_tipo1E = new CComboBox();

    CLabel cLabel2 = new CLabel();

    CComboBox dis_tipo2E = new CComboBox();

    CGridEditable jt = new CGridEditable(2) {

        public int cambiaLinea(int row, int col) {
            return checkLinea(row);
        }
    };

    CTextField dis_codiE = new CTextField(Types.CHAR, "X", 2);

    CTextField dis_nombE = new CTextField(Types.CHAR, "X", 50);

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public pddiscrim(EntornoUsuario eu, Principal p) {
        this(eu, p, null);
    }

    public pddiscrim(EntornoUsuario eu, Principal p, Hashtable ht) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;
        try {
            if (ht != null) {
                if (ht.get("modConsulta") != null) modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).booleanValue();
            }
            setTitulo("Mantenimiento Discriminadores");
            if (jf.gestor.apuntar(this)) jbInit(); else setErrorInit(true);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorInit(true);
        }
    }

    public pddiscrim(gnu.chu.anjelica.menu p, EntornoUsuario eu, Hashtable ht) {
        EU = eu;
        vl = p.getLayeredPane();
        eje = false;
        try {
            if (ht != null) {
                if (ht.get("modConsulta") != null) modConsulta = Boolean.valueOf(ht.get("modConsulta").toString()).booleanValue();
            }
            setTitulo("Mantenimiento Discriminadores");
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
            setErrorInit(true);
        }
    }

    private void jbInit() throws Exception {
        Pprinc.setFont(new java.awt.Font("Dialog", 0, 12));
        Pprinc.setLayout(gridBagLayout1);
        iniciarFrame();
        this.setSize(new Dimension(566, 455));
        this.setVersion("2006-04-24 " + (modConsulta ? "-SOLO LECTURA-" : ""));
        strSql = "SELECT distinct(dis_codi) FROM v_discrim WHERE emp_codi = " + EU.em_cod + " and dis_tipo like 'C%'" + getOrderQuery();
        statusBar = new StatusBar(this);
        nav = new navegador(this, dtCons, false, navegador.BTN_EDITAR);
        conecta();
        iniciar(this);
        Pcabe.setBorder(BorderFactory.createRaisedBevelBorder());
        Pcabe.setMaximumSize(new Dimension(496, 27));
        Pcabe.setMinimumSize(new Dimension(496, 27));
        Pcabe.setPreferredSize(new Dimension(496, 27));
        dis_codiE.setMayusc(true);
        Pcabe.setLayout(null);
        cLabel1.setText("Tipo");
        cLabel1.setBounds(new Rectangle(9, 5, 32, 18));
        cLabel2.setText("Clase");
        cLabel2.setBounds(new Rectangle(217, 5, 43, 18));
        dis_tipo2E.setBounds(new Rectangle(258, 5, 223, 18));
        dis_tipo1E.setBounds(new Rectangle(46, 5, 144, 18));
        Baceptar.setMaximumSize(new Dimension(134, 26));
        Baceptar.setMinimumSize(new Dimension(134, 26));
        Baceptar.setPreferredSize(new Dimension(134, 26));
        Bcancelar.setMaximumSize(new Dimension(134, 26));
        Bcancelar.setMinimumSize(new Dimension(134, 26));
        Bcancelar.setPreferredSize(new Dimension(134, 26));
        this.getContentPane().add(Pprinc, BorderLayout.CENTER);
        Pprinc.add(Pcabe, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        Pcabe.add(cLabel1, null);
        Pcabe.add(dis_tipo1E, null);
        Pcabe.add(cLabel2, null);
        Pcabe.add(dis_tipo2E, null);
        Pprinc.add(jt, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 4, 0, 4), 0, 0));
        Pprinc.add(Bcancelar, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 0, 0, 100), 0, 0));
        Pprinc.add(Baceptar, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 100, 0, 0), 0, 0));
        confGrid();
    }

    public void iniciarVentana() throws Exception {
        jt.setButton(KeyEvent.VK_F4, Baceptar);
        activarEventos();
    }

    String getOrderQuery() {
        return " order by dis_codi ";
    }

    void activarEventos() {
        dis_tipo1E.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    llenaClases(dis_tipo1E.getValor().substring(2, 4));
                    verDatos();
                } catch (Exception k) {
                    Error("Error al llenar clases para este Tipo", k);
                }
            }
        });
        dis_tipo2E.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (dis_tipo2E.isEnabled()) verDatos();
                } catch (Exception k) {
                    Error("Error al llenar clases para este Tipo", k);
                }
            }
        });
    }

    private void verDatos() {
        s = "select * from v_discrim where emp_codi = " + EU.em_cod + " AND dis_tipo = '" + dis_tipo1E.getText().charAt(0) + dis_tipo2E.getValor() + "'" + " order by dis_codi ";
        jt.removeAllDatos();
        try {
            if (!dtCon1.select(s)) {
                mensajeErr("NO HAY discriminadores para este TIPO y CLASE");
                return;
            }
            do {
                Vector v = new Vector();
                v.addElement(dtCon1.getString("dis_codi"));
                v.addElement(dtCon1.getString("dis_nomb"));
                jt.addLinea(v);
            } while (dtCon1.next());
            jt.requestFocusInicio();
        } catch (Exception k) {
            Error("Error al ver datos", k);
        }
    }

    public void PADPrimero() {
    }

    public void PADAnterior() {
    }

    public void PADSiguiente() {
    }

    public void PADUltimo() {
    }

    public void PADQuery() {
    }

    public void ej_query1() {
    }

    public void canc_query() {
    }

    public void PADEdit() {
        activar(true);
        mensajeErr("");
        mensaje("Editando discriminadores ...");
        jt.requestFocusInicio();
    }

    public void ej_edit1() {
        try {
            jt.procesaAllFoco();
            if (checkLinea(jt.getSelectedRow()) >= 0) return;
            String disTipo = dis_tipo1E.getText().charAt(0) + dis_tipo2E.getValor();
            s = "delete from v_discrim where emp_codi = " + EU.em_cod + " AND dis_tipo = '" + disTipo + "'";
            dtAdd.executeUpdate(s);
            dtAdd.addNew("v_discrim");
            for (int n = 0; n < jt.getRowCount(); n++) {
                if (jt.getValString(n, 0).trim().equals("")) continue;
                dtAdd.addNew("v_discrim");
                dtAdd.setDato("emp_codi", EU.em_cod);
                dtAdd.setDato("dis_tipo", disTipo);
                dtAdd.setDato("dis_codi", jt.getValString(n, 0));
                dtAdd.setDato("dis_nomb", jt.getValString(n, 1));
                dtAdd.update();
            }
            ctUp.commit();
        } catch (Exception k) {
            Error("Error al actualizar datos", k);
            return;
        }
        mensajeErr("Datos ... Actualizados");
        verDatos();
        mensaje("");
        activaTodo();
    }

    public void canc_edit() {
        activaTodo();
        verDatos();
        mensajeErr("Modificaciones ... Canceladas");
        return;
    }

    public void PADAddNew() {
    }

    public void ej_addnew1() {
    }

    public void canc_addnew() {
    }

    public void PADDelete() {
    }

    public void ej_delete1() {
    }

    public void canc_delete() {
    }

    public void activar(boolean b) {
        nav.setEnabled(!modConsulta);
        dis_tipo1E.setEnabled(!modConsulta);
        dis_tipo2E.setEnabled(!modConsulta);
        jt.setEnabled(b);
        Baceptar.setEnabled(b);
        Bcancelar.setEnabled(b);
    }

    public void afterConecta() throws SQLException, java.text.ParseException {
        dis_tipo1E.addItem("CLIENTE", "C cl");
        dis_tipo1E.addItem("ARTICULO", "A pr");
        dis_tipo1E.addItem("PROVEEDOR", "P pv");
        if (!pdconfig.getConfiguracion(EU.em_cod, dtStat, lkConf)) throw new SQLException("No encontrados configuraciones ");
        llenaClases("cl");
    }

    void llenaClases(String clase) throws SQLException {
        boolean enab = dis_tipo2E.isEnabled();
        dis_tipo2E.setEnabled(false);
        dis_tipo2E.removeAllItems();
        for (int n = 1; n < 5; n++) dis_tipo2E.addItem(lkConf.getString("cfg_dis" + clase + n), "" + n);
        if (clase.equals("cl")) {
            dis_tipo2E.addItem("Repres/Zona", "R");
            dis_tipo2E.addItem("Credito", "C");
        }
        if (clase.equals("pr")) dis_tipo2E.addItem("Camara", "C");
        dis_tipo2E.setEnabled(enab);
    }

    void confGrid() throws Exception {
        Vector v = new Vector();
        v.addElement("Codigo");
        v.addElement("Nombre");
        jt.setCabecera(v);
        jt.setAnchoColumna(new int[] { 50, 400 });
        jt.setMaximumSize(new Dimension(522, 306));
        jt.setMinimumSize(new Dimension(522, 306));
        jt.setPreferredSize(new Dimension(522, 306));
        jt.setAlinearColumna(new int[] { 0, 0 });
        Vector vc = new Vector();
        vc.add(dis_codiE);
        vc.add(dis_nombE);
        jt.setCampos(vc);
    }

    int checkLinea(int row) {
        if (dis_codiE.isNull(true)) return -1;
        if (dis_nombE.isNull()) {
            mensajeErr("Introduzca Descripci�n para este discriminador");
            return 1;
        }
        int nRow = jt.getRowCount();
        for (int n = 0; n < nRow; n++) {
            if (jt.getValString(n, 0).trim().equals(dis_codiE.getText().trim()) && n != row) {
                mensajeErr("Codigo de Discriminador YA existe en linea: " + (n + 1));
                return 0;
            }
            if (jt.getValString(n, 1).trim().equals(dis_nombE.getText().trim()) && n != row) {
                mensajeErr("Descripcion del discriminador esta repetida en Linea: " + (n + 1));
                return 1;
            }
        }
        return -1;
    }
}
