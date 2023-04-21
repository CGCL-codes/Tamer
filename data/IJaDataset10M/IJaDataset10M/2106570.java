package gnu.chu.anjelica.facturacion;

import gnu.chu.utilidades.*;
import gnu.chu.Menu.*;
import gnu.chu.controles.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import gnu.chu.controles.*;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import java.io.*;
import gnu.chu.sql.vlike;

/**
*
* <p>T�tulo: traspcont</p>
* <p>Descripci�n: Traspaso a Contabilidad de las facturas
* Este Programa genera un fichero ASCII con las facturas selecionadas. Estas
* facturas pueden ser importadas por el programa CONTAPLUS. Lo que exporta son las facturas de VENTAS
* y los datos de los clientes.
*
* @todo habria que implementar el exportar a diferentes programas de contabilidad.
* @todo Posibilidad de exportar proveedores y facturas de compras
*
* </p>
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
public class traspcont extends ventana {

    vlike lkCli = new vlike();

    String FINLINEA;

    JFileChooser ficeleE;

    FileWriter fr;

    String linea;

    condBusFra PcondBus = new condBusFra();

    String s;

    CPanel Pprinc = new CPanel();

    CButton Baceptar = new CButton("Aceptar F4", Iconos.getImageIcon("check"));

    CLabel cLabel1 = new CLabel();

    CLabel cLabel2 = new CLabel();

    CComboBox tip_traspE = new CComboBox();

    CLabel cLabel3 = new CLabel();

    CTextField numAsientoE = new CTextField(Types.DECIMAL, "####9");

    int NUMDEC = 2;

    String CONTRAPART = "47700000    ";

    String CUENTABASE = "700000003   ";

    String CUENTAIVA7 = "477000007   ";

    String CUENTAIVAYREQ7 = "477000107   ";

    String CUENTAIVA16 = "477000016   ";

    String CUENTAIVAYREQ16 = "477000116   ";

    String CUENTAREQ = "475000001   ";

    String CUENTADTOCOM = "665000003   ";

    String CUENTADTOPP = "665000003   ";

    String DEPARTAMENTO = "CAR";

    String CLAVE = "01";

    String MONEDA = "2";

    double CAMBIO = 166.386;

    CTextField rem_direcE = new CTextField();

    CLabel cLabel11 = new CLabel();

    CButton Bbusfic = new CButton(Iconos.getImageIcon("folder"));

    Cgrid jtFra = new Cgrid(9);

    CButton Bcancelar = new CButton("Anular", Iconos.getImageIcon("cancel"));

    CPanel cPanel1 = new CPanel();

    CLabel cLabel5 = new CLabel();

    CTextField rem_importE = new CTextField(Types.DECIMAL, "--,---,--9.99");

    CTextField rem_numfraE = new CTextField(Types.DECIMAL, "###9");

    CLabel cLabel6 = new CLabel();

    CButton Binvert = new CButton(Iconos.getImageIcon("data-undo"));

    CButton Bbusfra = new CButton("Buscar Fras", Iconos.getImageIcon("buscar"));

    CButton BinsFra = new CButton("Buscar Fras", Iconos.getImageIcon("buscar"));

    CCheckBox opIncTras = new CCheckBox();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public traspcont(EntornoUsuario eu, Principal p) {
        EU = eu;
        vl = p.panel1;
        jf = p;
        eje = true;
        setTitulo("Traspaso a Contabilidad");
        try {
            if (jf.gestor.apuntar(this)) jbInit(); else setErrorInit(true);
        } catch (Exception e) {
            e.printStackTrace();
            setErrorInit(true);
        }
    }

    public traspcont(gnu.chu.anjelica.menu p, EntornoUsuario eu) {
        EU = eu;
        vl = p.getLayeredPane();
        setTitulo("Traspaso a Contabilidad");
        eje = false;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
            setErrorInit(true);
        }
    }

    private void jbInit() throws Exception {
        iniciarFrame();
        this.setSize(new Dimension(715, 459));
        this.setVersion("20051015");
        conecta();
        statusBar = new StatusBar(this);
        Pprinc.setLayout(gridBagLayout1);
        Baceptar.setBounds(new Rectangle(328, 2, 106, 27));
        Baceptar.setToolTipText("Traspasar Facturas a Cont.");
        Baceptar.setMargin(new Insets(0, 0, 0, 0));
        Baceptar.setText("Traspasar");
        PcondBus.setBorder(BorderFactory.createLineBorder(Color.black));
        PcondBus.setMaximumSize(new Dimension(691, 201));
        PcondBus.setMinimumSize(new Dimension(691, 201));
        PcondBus.setOpaque(true);
        PcondBus.setPreferredSize(new Dimension(691, 201));
        cLabel2.setText("Tipo Traspaso");
        cLabel2.setBounds(new Rectangle(340, 127, 84, 19));
        tip_traspE.setBounds(new Rectangle(423, 127, 155, 19));
        cLabel3.setText("Num. Asiento");
        cLabel3.setBounds(new Rectangle(343, 151, 75, 18));
        numAsientoE.setBounds(new Rectangle(421, 150, 57, 18));
        rem_direcE.setBounds(new Rectangle(57, 180, 384, 17));
        cLabel11.setText("Directorio");
        cLabel11.setBounds(new Rectangle(2, 180, 56, 17));
        Bbusfic.setBounds(new Rectangle(444, 175, 24, 22));
        Bbusfic.setToolTipText("Buscar Fichero");
        Vector v = new Vector();
        v.addElement("Ejer");
        v.addElement("Emp");
        v.addElement("Fact.");
        v.addElement("Fec.Fra");
        v.addElement("Cli");
        v.addElement("Nombre Cliente");
        v.addElement("Imp.Fra");
        v.addElement("Tra");
        v.addElement("INC");
        jtFra.setCabecera(v);
        jtFra.setMaximumSize(new Dimension(693, 197));
        jtFra.setMinimumSize(new Dimension(693, 197));
        jtFra.setPreferredSize(new Dimension(693, 197));
        jtFra.setBuscarVisible(false);
        jtFra.setAnchoColumna(new int[] { 40, 40, 50, 90, 70, 180, 80, 40, 40 });
        jtFra.setAlinearColumna(new int[] { 2, 2, 2, 1, 2, 0, 2, 1, 1 });
        jtFra.setFormatoColumna(7, "B SN");
        jtFra.setFormatoColumna(8, "B SN");
        jtFra.setFormatoColumna(6, "---,--9.99");
        Bcancelar.setMargin(new Insets(0, 0, 0, 0));
        Bcancelar.setBounds(new Rectangle(447, 2, 106, 27));
        Bcancelar.setToolTipText("Anular Traspaso a Contabilidad");
        cPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
        cPanel1.setMaximumSize(new Dimension(569, 34));
        cPanel1.setMinimumSize(new Dimension(569, 34));
        cPanel1.setPreferredSize(new Dimension(569, 34));
        cPanel1.setLayout(null);
        cLabel5.setBounds(new Rectangle(3, 3, 51, 16));
        cLabel5.setText("Imp.Fras");
        rem_importE.setBounds(new Rectangle(59, 3, 88, 16));
        rem_importE.setEnabled(false);
        rem_numfraE.setBounds(new Rectangle(201, 3, 48, 16));
        rem_numfraE.setEnabled(false);
        cLabel6.setBounds(new Rectangle(160, 3, 41, 16));
        cLabel6.setText("N�Fras");
        Binvert.setBounds(new Rectangle(254, 3, 63, 20));
        Binvert.setToolTipText("Invertir Facturas Selecionadas");
        Binvert.setMargin(new Insets(0, 0, 0, 0));
        Binvert.setText("Inv");
        Bbusfra.setBounds(new Rectangle(582, 174, 107, 22));
        Bbusfra.setToolTipText("Inserta las facturas, borrando las anteriores");
        Bbusfra.setMargin(new Insets(0, 0, 0, 0));
        Bbusfra.setText("Act. Fras");
        BinsFra.setText("F4 Ins. Fras");
        BinsFra.setMargin(new Insets(0, 0, 0, 0));
        BinsFra.setBounds(new Rectangle(469, 174, 107, 22));
        BinsFra.setToolTipText("Inserta las facturas sin borrar las anteriores");
        BinsFra.setVerifyInputWhenFocusTarget(true);
        opIncTras.setText("Inc. Fras. ya Traspasadas");
        opIncTras.setBounds(new Rectangle(491, 151, 196, 18));
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.getContentPane().add(Pprinc, BorderLayout.CENTER);
        Pprinc.add(PcondBus, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 0, 0, 0), 0, 0));
        PcondBus.add(cLabel2, null);
        PcondBus.add(tip_traspE, null);
        PcondBus.add(cLabel3, null);
        PcondBus.add(numAsientoE, null);
        PcondBus.add(rem_direcE, null);
        PcondBus.add(cLabel11, null);
        PcondBus.add(Bbusfic, null);
        PcondBus.add(Bbusfra, null);
        PcondBus.add(BinsFra, null);
        PcondBus.add(opIncTras, null);
        Pprinc.add(jtFra, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 12), 0, 0));
        Pprinc.add(cPanel1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        cPanel1.add(rem_importE, null);
        cPanel1.add(cLabel5, null);
        cPanel1.add(cLabel6, null);
        cPanel1.add(rem_numfraE, null);
        cPanel1.add(Binvert, null);
        cPanel1.add(Baceptar, null);
        cPanel1.add(Bcancelar, null);
    }

    public void iniciarVentana() throws Exception {
        FINLINEA = "\r\n";
        PcondBus.setButton(KeyEvent.VK_F4, BinsFra);
        tip_traspE.addItem("ContaPlus", "CP");
        PcondBus.setButton(KeyEvent.VK_F4, Baceptar);
        PcondBus.iniciar(dtStat, this, vl, EU);
        rem_direcE.setText(EU.dirTmp);
        activarEventos();
        PcondBus.feciniE.requestFocus();
    }

    void activarEventos() {
        Baceptar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Baceptar_actionPerformed();
            }
        });
        Bcancelar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Bcancelar_actionPerformed();
            }
        });
        Bbusfra.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Bbusfra_actionPerformed(true);
            }
        });
        BinsFra.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Bbusfra_actionPerformed(false);
            }
        });
        Bbusfic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Bbusfic_actionPerformed();
            }
        });
        jtFra.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (!jtFra.isEnabled() || jtFra.getSelectedColumn() != 8) return;
                jtFra.setValor(new Boolean(!jtFra.getValBoolean(8)));
                recalcTot();
            }
        });
        Binvert.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!jtFra.isEnabled()) return;
                int nRow = jtFra.getRowCount();
                for (int n = 0; n < nRow; n++) {
                    jtFra.setValor(new Boolean(!jtFra.getValBoolean(n, 8)), n, 8);
                }
                recalcTot();
            }
        });
    }

    void Baceptar_actionPerformed() {
        try {
            if (!PcondBus.checkCampos()) return;
            if (numAsientoE.isNull()) {
                mensajeErr("Numero de Asiento NO VALIDO");
                numAsientoE.requestFocus();
                return;
            }
            if (rem_numfraE.getValorDec() == 0) {
                mensajeErr("Elija al menos una Factura para operar");
                jtFra.requestFocusInicio();
                return;
            }
        } catch (Exception k) {
            Error("Error al Comprobar condiciones de consulta", k);
            return;
        }
        new miThread("") {

            public void run() {
                listFactur();
            }
        };
    }

    void Bcancelar_actionPerformed() {
        try {
            if (!PcondBus.checkCampos()) return;
            if (rem_numfraE.getValorDec() == 0) {
                mensajeErr("Elija al menos una Factura para operar");
                jtFra.requestFocusInicio();
                return;
            }
            int nRow = jtFra.getRowCount();
            for (int n = 0; n < nRow; n++) {
                if (!jtFra.getValBoolean(n, 8)) continue;
                s = "UPDATE v_facvec SET fvc_trasp = 0 " + " WHERE fvc_ano = " + jtFra.getValInt(n, 0) + " AND emp_codi = " + jtFra.getValInt(n, 1) + " AND fvc_nume = " + jtFra.getValInt(n, 2);
                stUp.executeUpdate(s);
            }
            ctUp.commit();
            msgBox("Traspaso CONTABLE ... ANULADO");
        } catch (Exception k) {
            Error("Error al Comprobar condiciones de consulta", k);
            return;
        }
    }

    void listFactur() {
        mensaje("Espere, por favor .. GENERANDO LISTADO");
        this.setEnabled(false);
        int n = 0;
        try {
            String fichero = rem_direcE.getText() + "/xdiario.dat";
            fr = new FileWriter(fichero, false);
            String linea;
            int numAsiento = numAsientoE.getValorInt();
            String fechAsiento;
            String cuenClie, factura;
            int nRow = jtFra.getRowCount();
            Hashtable ht = new Hashtable();
            for (n = 0; n < nRow; n++) {
                if (!jtFra.getValBoolean(n, 8)) continue;
                s = "SELECT c.*,cl.* FROM v_facvec as c,v_cliente as cl " + " WHERE  c.cli_codi = cl.cli_codi " + " AND c.fvc_ano = " + jtFra.getValInt(n, 0) + " AND c.emp_codi = " + jtFra.getValInt(n, 1) + " AND c.fvc_nume = " + jtFra.getValInt(n, 2);
                dtCon1.select(s);
                ht.put(dtCon1.getString("cli_codi"), "");
                fechAsiento = dtCon1.getFecha("fvc_fecfra", "yyyyMMdd");
                cuenClie = Formatear.ajusIzq(Formatear.format(dtCon1.getInt("cue_codi"), "###########9").trim(), 12);
                factura = Formatear.format(dtCon1.getInt("emp_codi"), "#9") + Formatear.format(dtCon1.getInt("fvc_nume"), "999999");
                impLinea(numAsiento, fechAsiento, cuenClie, CONTRAPART, dtCon1.getDouble("fvc_sumtot"), dtCon1.getInt("fvc_nume"), factura, 0, 0, 0, 0);
                if (dtCon1.getDouble("fvc_dtocom") > 0) impLinea(numAsiento, fechAsiento, CUENTADTOCOM, cuenClie, (dtCon1.getDouble("fvc_dtocom") / 100) * dtCon1.getDouble("fvc_sumlin"), dtCon1.getInt("fvc_nume"), factura, 0, 0, 0, 0);
                if (dtCon1.getDouble("fvc_dtopp") > 0) impLinea(numAsiento, fechAsiento, CUENTADTOPP, cuenClie, (dtCon1.getDouble("fvc_dtopp") / 100) * dtCon1.getDouble("fvc_sumlin"), dtCon1.getInt("fvc_nume"), factura, 0, 0, 0, 0);
                impLinea(numAsiento, fechAsiento, CUENTABASE, cuenClie, 0, dtCon1.getInt("fvc_nume"), factura, dtCon1.getDouble("fvc_sumlin"), 0, 0, 0);
                if (dtCon1.getDouble("fvc_poriva") > 0) {
                    impLinea(numAsiento, fechAsiento, dtCon1.getDouble("fvc_porreq") > 0 ? dtCon1.getDouble("fvc_poriva") == 7 ? CUENTAIVAYREQ7 : CUENTAIVAYREQ16 : dtCon1.getDouble("fvc_poriva") == 7 ? CUENTAIVA7 : CUENTAIVA16, cuenClie, 0, dtCon1.getInt("fvc_nume"), factura, dtCon1.getDouble("fvc_impiva"), dtCon1.getDouble("fvc_basimp"), dtCon1.getDouble("fvc_poriva"), dtCon1.getDouble("fvc_porreq"));
                }
                if (dtCon1.getDouble("fvc_porreq") > 0) impLinea(numAsiento, fechAsiento, CUENTAREQ, cuenClie, 0, dtCon1.getInt("fvc_nume"), factura, dtCon1.getDouble("fvc_imprec"), 0, 0, 0);
                numAsiento++;
                s = "UPDATE v_facvec SET fvc_trasp = -1 " + " WHERE fvc_ano = " + jtFra.getValInt(n, 0) + " AND emp_codi = " + jtFra.getValInt(n, 1) + " AND fvc_nume = " + jtFra.getValInt(n, 2);
                stUp.executeUpdate(s);
            }
            fr.close();
            String ficheroAsi = rem_direcE.getText() + "/xsubcuenta.dat";
            fr = new FileWriter(ficheroAsi, false);
            Enumeration en = ht.keys();
            String l, pviNomb, cliCodi;
            while (en.hasMoreElements()) {
                cliCodi = en.nextElement().toString();
                s = "SELECT * FROM V_cliente where cli_codi = " + cliCodi;
                dtStat.selectInto(s, lkCli);
                cuenClie = Formatear.ajusIzq(Formatear.format(dtStat.getInt("cue_codi"), "###########9").trim(), 12);
                s = "select pvi_nomb from v_provincia as p where pai_codi = " + dtStat.getInt("pai_codi") + " and pvi_codi = " + dtStat.getInt("cli_codpo") + "/1000";
                if (!dtStat.select(s)) pviNomb = " PROV: " + Formatear.Redondea(lkCli.getDatoInt("cli_codpo") / 1000, 0) + " NO ENCONTRADA"; else pviNomb = dtStat.getString("pvi_nomb");
                l = cuenClie + Formatear.ajusIzq(lkCli.getString("cli_nomco"), 40) + Formatear.ajusIzq(lkCli.getString("cli_nif"), 15) + Formatear.ajusIzq(lkCli.getString("cli_direc"), 35) + Formatear.ajusIzq(lkCli.getString("cli_pobl"), 25) + Formatear.ajusIzq(pviNomb, 20) + Formatear.ajusIzq(lkCli.getString("cli_codpo"), 5) + "F" + Formatear.space(5) + "FF ";
                fr.write(l + FINLINEA);
            }
            fr.close();
            ctUp.commit();
            this.setEnabled(true);
            mensaje("");
            msgBox("Exportacion de Facturas .. TERMINADO\n Generados FIcheros:\n" + fichero + "\n" + ficheroAsi);
        } catch (Exception k) {
            Error("Error al Exportar Facturas\n Error en Fra: " + jtFra.getValInt(n, 0) + "/" + jtFra.getValInt(n, 1) + "-" + jtFra.getValInt(n, 2) + " De cliente: " + jtFra.getValInt(n, 4), k);
        }
    }

    void impLinea(int numAsiento, String fechAsiento, String cuenta, String contra, double debe, int nFra, String factura, double haber, double base, double iva, double recequ) throws Exception {
        linea = Formatear.format(numAsiento, "#####9") + fechAsiento + cuenta + contra + Formatear.ajusDer("" + Formatear.format(Formatear.Redondea(debe * CAMBIO, 0), "---------9.99"), 16) + Formatear.ajusIzq("N/Fra. " + nFra, 25) + Formatear.ajusDer("" + Formatear.format(Formatear.Redondea(haber * CAMBIO, 0), "---------9.99"), 16) + factura + Formatear.ajusDer(Formatear.format(Formatear.Redondea(base * CAMBIO, 0), "---------9.99"), 16) + Formatear.format(iva, "#9.99") + Formatear.format(recequ, "#9.99") + Formatear.ajusIzq("F-" + factura, 10) + DEPARTAMENTO + CLAVE + "          00     0        0.000000            0.00            0.00 N                     0.00" + MONEDA + Formatear.ajusDer("" + Formatear.format(Formatear.Redondea(debe, NUMDEC), "---------9.99"), 16) + Formatear.ajusDer("" + Formatear.format(Formatear.Redondea(haber, NUMDEC), "---------9.99"), 16) + Formatear.ajusDer("" + Formatear.format(Formatear.Redondea(base, NUMDEC), "---------9.99"), 16) + "F" + Formatear.space(10);
        fr.write(linea + FINLINEA);
    }

    void Bbusfic_actionPerformed() {
        try {
            configurarFile();
            int returnVal = ficeleE.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) rem_direcE.setText(ficeleE.getSelectedFile().getParent());
        } catch (Exception k) {
            fatalError("error al elegir el fichero", k);
        }
    }

    void configurarFile() throws Exception {
        if (ficeleE != null) return;
        ficeleE = new JFileChooser();
        ficeleE.setName("Elegir Directorio");
        ficeleE.setCurrentDirectory(new java.io.File(EU.dirTmp));
    }

    void Bbusfra_actionPerformed(boolean borDatos) {
        try {
            if (!PcondBus.checkCampos()) return;
            s = "SELECT c.*,cl.* FROM v_facvec as c,v_cliente as cl " + " WHERE  c.cli_codi = cl.cli_codi " + PcondBus.getCondWhere() + (!opIncTras.isSelected() ? " AND fvc_trasp=0" : "") + " ORDER BY c.emp_codi, fvc_ano,fvc_nume";
            if (borDatos) jtFra.removeAllDatos();
            rem_numfraE.setValorDec(0);
            rem_importE.setValorDec(0);
            if (!dtCon1.select(s)) {
                mensajeErr("No encontradas FRAS para estas condiciones");
                return;
            }
            boolean swEnc;
            int nRow = jtFra.getRowCount();
            int n;
            do {
                swEnc = false;
                for (n = 0; n < nRow; n++) {
                    if (jtFra.getValInt(n, 0) == dtCon1.getInt("fvc_ano") && jtFra.getValInt(n, 1) == dtCon1.getInt("emp_codi") && jtFra.getValInt(n, 2) == dtCon1.getInt("fvc_nume")) {
                        swEnc = true;
                        break;
                    }
                }
                if (swEnc) continue;
                Vector v = new Vector();
                v.addElement(dtCon1.getString("fvc_ano"));
                v.addElement(dtCon1.getString("emp_codi"));
                v.addElement(dtCon1.getString("fvc_nume"));
                v.addElement(dtCon1.getFecha("fvc_fecfra", "dd-MM-yyyy"));
                v.addElement(dtCon1.getString("cli_codi"));
                v.addElement(dtCon1.getString("cli_nomb"));
                v.addElement(dtCon1.getString("fvc_sumtot"));
                v.addElement(dtCon1.getInt("fvc_trasp") == 0 ? "N" : "S");
                v.addElement("S");
                jtFra.addLinea(v);
            } while (dtCon1.next());
            recalcTot();
            jtFra.requestFocusInicio();
        } catch (Exception k) {
            Error("Error al buscar Facturas", k);
        }
    }

    void recalcTot() {
        int nGiros = 0;
        double impGiros = 0;
        int nRow = jtFra.getRowCount();
        for (int n = 0; n < nRow; n++) {
            if (jtFra.getValBoolean(n, 8)) {
                nGiros++;
                impGiros += jtFra.getValorDec(n, 6);
            }
        }
        rem_numfraE.setValorInt(nGiros);
        rem_importE.setValorDec(impGiros);
    }
}
