package gnu.chu.utilidades;

import java.awt.BorderLayout;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.event.InternalFrameEvent;
import gnu.chu.Menu.*;
import gnu.chu.interfaces.*;
import gnu.chu.controles.*;
import gnu.chu.sql.*;
import javax.swing.*;

public class ventanaPad extends ventana {

    public boolean swThread = false;

    public CButton Baceptar = new CButton("Aceptar F4", Iconos.getImageIcon("check"));

    public CButton Bcancelar = new CButton("Cancelar", Iconos.getImageIcon("cancel"));

    protected BorderLayout borderLayout1 = new BorderLayout();

    public navegador nav;

    public boolean alta = false;

    public PAD panel;

    public static final int ALL = 0;

    public static final int INDICE = 1;

    public static final int RESTO = 2;

    public String strSql;

    public DatosTabla dtCons = new DatosTabla();

    public DatosTabla dtBloq = new DatosTabla();

    public DatosTabla dtAdd = new DatosTabla();

    public int pid = 1;

    private boolean sWrgSelect = true;

    public ventanaPad() {
        Baceptar.setMargin(new Insets(0, 0, 0, 0));
        Bcancelar.setMargin(new Insets(0, 0, 0, 0));
        try {
            Integer aux = Integer.valueOf(Thread.currentThread().getName());
            pid = aux.intValue();
        } catch (Exception j) {
        }
    }

    public void iniciar(PAD p, boolean activ) throws Exception {
        iniciar(p, activ, true);
    }

    public void iniciar(PAD p, boolean activ, boolean navegador) throws Exception {
        panel = p;
        this.getContentPane().setLayout(borderLayout1);
        if (navegador) this.getContentPane().add(nav, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        navActivarAll();
        dtCons.setLanzaDBCambio(false);
        Baceptar.setMnemonic('A');
        Baceptar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panel.ej_Baceptar(e);
            }
        });
        Bcancelar.setMnemonic('C');
        Bcancelar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                panel.ej_Bcancelar(e);
            }
        });
        if (activ) panel.activar(false);
    }

    public void iniciar(PAD p) throws Exception {
        iniciar(p, true);
    }

    /**
   * @deprecated Usar iniciar(PAD) (con minusculas)
   *
   * @param p PAD  Ventana
   * @throws Exception si no puede con ello.
   */
    public void Iniciar(PAD p) throws Exception {
        iniciar(p, true);
    }

    /**
  * Regenera la select, actual.
  */
    public void rgSelect() throws SQLException {
        strSql = strSql.trim();
        if (!strSql.toUpperCase().startsWith("SELECT")) {
            dtCons.addNew(strSql);
            dtCons.setNOREG(true);
            return;
        }
        if (dtCons.select(strSql, false) == false) mensajeErr(" -- NO ENCONTRADOS REGISTROS -- ");
    }

    public void matar(boolean cerrarConexion) {
        super.matar(cerrarConexion);
    }

    /**
   * Asigna si se tiene que lanzar el rgSelect
   * cuando conecte con la base de datos
   *
   * Por defecto si conecta
   *
   */
    public void setLanzaRgSelect(boolean sino) {
        sWrgSelect = sino;
    }

    /**
   * Retorna si realiza un rgSelect cuando
   * conecte con la base de datos
   *
   *
   */
    public boolean getLanzaRgSelect() {
        return sWrgSelect;
    }

    /**
   * Conecta a La base de Datos.
   */
    public void conecta() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, java.text.ParseException {
        super.conecta();
        dtCons.setConexion(ct);
        dtBloq.setConexion(ct);
        dtAdd.setConexion(ct);
        afterConecta();
        if (sWrgSelect) rgSelect();
    }

    public void afterConecta() throws SQLException, java.text.ParseException {
    }

    /**
   * Monta las conciciones where de una Select.
   */
    public String creaWhere(String sel, String c[]) {
        return creaWhere(sel, c, c.length);
    }

    public String creaWhere(String sel, String c[], boolean incEmp) {
        return creaWhere(sel, c, c.length, incEmp);
    }

    public String creaWhere(String sel, String c[], int nc) {
        return creaWhere(sel, c, nc, true);
    }

    /**
	 * Monta las conciciones where de una Select.
	 */
    public String creaWhere(String sel, String c[], int nc, boolean incEmp) {
        int n;
        String s = "";
        for (n = 0; n < nc; n++) {
            if (c[n] == null) continue;
            if (c[n].compareTo("") == 0) continue;
            if (s.compareTo("") == 0) s = c[n]; else s = s + " AND " + c[n];
        }
        if (!s.equals("")) {
            if (incEmp) s = sel + " WHERE emp_codi = " + EU.em_cod + " AND " + s; else s = sel + " WHERE " + s;
        } else {
            if (incEmp) s = sel + " WHERE emp_codi = " + EU.em_cod; else s = sel;
        }
        return s;
    }

    /**
	* Bloquea el registro activo
	*/
    public boolean bloqueaRegistro(String s) {
        try {
            if (!dtBloq.select(s, true)) {
                SQLException e = new SQLException("bloqueaRegistro: Registro Seleccionado NO Encontrado\nSelect: " + s, "", 0);
                fatalError("", e);
                return false;
            }
        } catch (Exception k) {
            if (fatalError("bloqueaRegistro: Abrir Cursor For Update\n", k) == PopError.REINTENTAR) {
                return bloqueaRegistro(s);
            }
            return false;
        }
        return true;
    }

    /**
 	* Rutina a ejecutarse cuando se pulsa el BOTON Aceptar.
 	*/
    public void ej_Baceptar(ActionEvent e) {
        switch(nav.getPulsado()) {
            case navegador.EDIT:
                panel.ej_edit();
                break;
            case navegador.QUERY:
                panel.ej_query();
                break;
            case navegador.ADDNEW:
                panel.ej_addnew();
                break;
            case navegador.DELETE:
                panel.ej_delete();
                break;
        }
    }

    /**
 	* Rutina a ejecutarse cuando se pulsa el BOTON Cancelar
 	*/
    public void ej_Bcancelar(ActionEvent e) {
        switch(nav.getPulsado()) {
            case navegador.EDIT:
                panel.canc_edit();
                break;
            case navegador.QUERY:
                panel.canc_query();
                break;
            case navegador.ADDNEW:
                panel.canc_addnew();
                break;
            case navegador.DELETE:
                panel.canc_delete();
                break;
        }
    }

    public void activaTodo() {
        panel.activar(false);
        this.setEnabled(true);
        navActivarAll();
        nav.requestFocus();
    }

    /**
   *  Activa Navegador
   */
    public void navActivarAll() {
        if (panel != null) panel.salirEnabled(true);
        if (nav == null) return;
        switch(EU.modo) {
            case PAD.NORMAL:
                if (dtCons.getNOREG()) {
                    nav.setEnabled(navegador.TODOS, false);
                    nav.setEnabled(navegador.ADDNEW, true);
                    nav.setEnabled(navegador.QUERY, true);
                } else nav.setEnabled(navegador.TODOS, true);
                break;
            case PAD.CONSULTA:
                if (dtCons.getNOREG()) {
                    mensajes.mensajeUrgente("NO ENCONTRADOS REGISTROS .. Este programa se Finalizara");
                    matar();
                }
                nav.setEnabled(navegador.TODOS, true);
                nav.removeBoton(navegador.ADDNEW);
                nav.removeBoton(navegador.DELETE);
                nav.removeBoton(navegador.EDIT);
                nav.setEnabled(navegador.CHOSE, true);
                validate();
                repaint();
                break;
            case PAD.ALTA:
                nav.setEnabled(navegador.TODOS, false);
                nav.pulsado = navegador.ADDNEW;
                panel.PADAddNew();
        }
    }

    public boolean ej_select(String s) {
        try {
            return (dtCon1.select(s));
        } catch (Exception k) {
            fatalError("ej_select ", k);
            return false;
        }
    }

    public void ej_addnew() {
        if (!checkAddNew()) return;
        if (!swThread) panel.ej_addnew1(); else new PADThread(panel, PAD.ej_addnew);
    }

    public boolean checkAddNew() {
        return true;
    }

    public void ej_edit() {
        if (!checkEdit()) return;
        if (!swThread) panel.ej_edit1(); else new PADThread(panel, PAD.ej_edit);
    }

    public boolean checkEdit() {
        return true;
    }

    public void ej_delete() {
        if (!checkDelete()) return;
        if (!swThread) panel.ej_delete1(); else new PADThread(panel, PAD.ej_delete);
    }

    public boolean checkDelete() {
        return true;
    }

    public void ej_query() {
        if (!checkQuery()) return;
        if (!swThread) panel.ej_query1(); else new PADThread(panel, PAD.ej_query);
    }

    public boolean checkQuery() {
        return true;
    }

    public void PADChose() {
    }

    public void PADAddNew() {
        new PADThread(panel, PAD.PADADDNEW);
    }

    public void PADAddNew1() {
    }

    public void PADEdit() {
        new PADThread(panel, PAD.PADEDIT);
    }

    public void PADEdit1() {
    }

    public void PADQuery() {
        new PADThread(panel, PAD.PADQUERY);
    }

    public void PADQuery1() {
    }

    public void PADDelete() {
        new PADThread(panel, PAD.PADDELETE);
    }

    public void PADDelete1() {
    }
}
