package mx.ipn.presentacion.facturacion;

import mx.ipn.presentacion.facturacion.ui.Ui_VerFactura;
import com.trolltech.qt.gui.QDialog;
import mx.ipn.presentacion.ConexionConNegocios;
import mx.ipn.presentacion.facturacion.ui.*;
import mx.ipn.to.BitacoraSTO;
import mx.ipn.to.DetalleFacturaTO;
import mx.ipn.to.FacturaTO;
import com.trolltech.qt.gui.QTableWidgetItem;
import java.util.GregorianCalendar;

public class Reporte extends QDialog {

    Ui_Reporte rep;

    public FacturaTO facturato;

    FacturaTO[] arrfacturato;

    BitacoraSTO[] servicios;

    DetalleFacturaTO[] arrdetalle;

    QTableWidgetItem newItem = null;

    GregorianCalendar f1 = new GregorianCalendar();

    int idfactura;

    double iva = 0.0;

    double tarifa = 0.0;

    double total = 0.0;

    boolean pordia = false;

    Double val = null;

    String fechabuscar = "";

    String contenido = "<html>" + "<head><style type=\"text/css\">" + "<!--" + ".Estilo1 {" + "	font-family: Arial, Helvetica, sans-serif;" + "	color: #000066;" + "}" + ".Estilo2 {color: #990000}" + "-->" + "</style></head><body>";

    String tabla = "";

    String renglon = "";

    String totales = "";

    public Reporte() {
        rep = new Ui_Reporte();
        rep.setupUi(this);
        rep.tableWidget.setRowCount(1);
        rep.tableWidget.itemClicked.connect(this, "selectFactura()");
        rep.pushButton.clicked.connect(this, "ver()");
        rep.pushButton_2.clicked.connect(this, "buscar()");
        rep.radioButton.clicked.connect(this, "mes()");
        rep.radioButton_2.clicked.connect(this, "dia()");
        this.show();
    }

    public Reporte(QDialog parent) {
    }

    public void construye() {
        contenido = contenido + tabla;
        contenido = contenido + renglon;
        contenido = contenido + totales;
        System.out.println(contenido);
        View ver = new View(contenido);
    }

    public void ver() {
        String sver = rep.comboBox_2.currentText();
        if (sver.compareToIgnoreCase("General") == 0) {
            tabla = "<h1 align=\"center\" class=\"Estilo1\">Reporte de Facturas</h1>" + "<p align=\"center\">" + fechabuscar + "</p>" + "<p align=\"center\">Reporte General</p>" + "<p align=\"center\">&nbsp;</p>" + "<table width=\"731\" height=\"83\" border=\"1\" align=\"center\" bordercolor=\"#999999\">" + "  <tr> <th scope=\"col\"><span class=\"Estilo2\">ID</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">RFC</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">Fecha</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">Tarifa</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">Total</span></th>" + "</tr>";
        }
        if (sver.compareToIgnoreCase("Detallado") == 0) {
            llena(arrfacturato);
            tabla = "<h1 align=\"center\" class=\"Estilo1\">Reporte de Facturas</h1>" + "<p align=\"center\">" + fechabuscar + "</p>" + "<p align=\"center\">Reporte Detallado</p>" + "<p align=\"center\">&nbsp;</p>" + "<table width=\"731\" height=\"83\" border=\"1\" align=\"center\" bordercolor=\"#999999\">" + "  <tr> <th scope=\"col\"><span class=\"Estilo2\">Factura</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">RFC</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">Operacion</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">Observaciones</span></th>" + "<th scope=\"col\"><span class=\"Estilo2\">Tarifa</span></th>" + "</tr>";
        }
        construye();
    }

    public int buscarservicio(int idoperacion) {
        int pos = -1;
        for (int j = 0; j < (servicios.length); j++) {
            System.out.println(servicios[j].getIdOperacion() + "vs" + idoperacion);
            if (servicios[j].getIdOperacion() == idoperacion) pos = j;
        }
        return pos;
    }

    public void llena(FacturaTO[] arrfacturato) {
        tabla = "";
        renglon = "";
        renglon = "";
        contenido = "<html>" + "<head><style type=\"text/css\">" + "<!--" + ".Estilo1 {" + "	font-family: Arial, Helvetica, sans-serif;" + "	color: #000066;" + "}" + ".Estilo2 {color: #990000}" + "-->" + "</style></head><body>";
        int idf;
        int pos = -1;
        for (int i = 0; i < arrfacturato.length; i++) {
            idf = arrfacturato[i].getIdFactura();
            arrdetalle = (DetalleFacturaTO[]) ConexionConNegocios.invocaServicio("selectDetalleFactura", DetalleFacturaTO[].class);
            if (arrdetalle != null) {
                servicios = (BitacoraSTO[]) ConexionConNegocios.invocaServicio("verServicios", BitacoraSTO[].class);
                if (servicios != null) {
                    for (int j = 0; j < (arrdetalle.length); j++) {
                        if (arrdetalle[j].getIdFactura() == idfactura) {
                            if ((pos = buscarservicio(arrdetalle[j].getIdOperacion())) != -1) {
                                i++;
                                renglon = renglon + "<tr><td>" + arrfacturato[j].getIdFactura() + "</td>" + "<td>" + arrfacturato[j].getRegistro() + "</td>" + "<td>" + servicios[pos].getIdOperacion() + "</td>" + "<td>" + servicios[pos].getObservaciones() + "</td>" + "<td>" + servicios[pos].getCostoEstimado() + "</td>" + "</tr>";
                            }
                        }
                    }
                }
            }
        }
    }

    public void calcula() {
        iva = 0.0;
        tarifa = 0.0;
        total = 0.0;
        int cont = rep.tableWidget.rowCount();
        for (int i = 0; i < cont; i++) {
            newItem = rep.tableWidget.item(i, 3);
            if (newItem != null) {
                if (newItem.text().compareToIgnoreCase("") != 0) {
                    val = new Double(newItem.text());
                    tarifa = tarifa + val.doubleValue();
                }
            }
        }
        iva = tarifa * 0.15;
        total = tarifa + iva;
        rep.lineEdit_2.setText(String.valueOf(tarifa));
        rep.lineEdit_3.setText(String.valueOf(iva));
        rep.lineEdit_4.setText(String.valueOf(total));
        totales = "<tr bordercolor=\"#FFFFFF\">" + "<td>&nbsp;</td>" + "<td>&nbsp;</td>" + "<td>&nbsp;</td>" + "<td>&nbsp;</td>" + "<td>&nbsp;</td>" + "</tr>" + "<tr>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td>Total</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "</tr>" + "<tr>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td>Importe</td>" + "<td>" + tarifa + "</td></tr>" + "<tr>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td>Iva</td>" + "<td>" + iva + "</td>" + "</tr>" + "<tr>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td bordercolor=\"#FFFFFF\">&nbsp;</td>" + "<td>Total</td>" + "<td>" + total + "</td>" + "</tr>" + "</table>" + "<p align=\"left\">&nbsp;</p>" + "<p align=\"left\">&nbsp;</p>" + "</body>" + "</html>";
    }

    public void dia() {
        pordia = true;
        rep.lineEdit_5.setEnabled(true);
    }

    public void mes() {
        pordia = false;
        rep.lineEdit_5.setEnabled(false);
    }

    public void selectFactura() {
        QTableWidgetItem newItem = null;
        int fila = rep.tableWidget.currentRow();
        newItem = rep.tableWidget.item(fila, 0);
        idfactura = Integer.parseInt(newItem.text());
        for (int j = 0; j < arrfacturato.length; j++) {
            if (arrfacturato[j].getIdFactura() == idfactura) facturato = arrfacturato[j];
        }
    }

    public boolean rango(java.util.Date fecha, int mes, int anio, String smes) {
        boolean flag = false;
        f1.setTime(fecha);
        if (pordia) {
            if (FacturaP.isNumeric(rep.lineEdit_5.text())) {
                int dia = Integer.parseInt(rep.lineEdit_5.text());
                fechabuscar = "del " + dia + " de " + smes + " de " + anio;
                if (f1.get(GregorianCalendar.DAY_OF_MONTH) == dia && f1.get(GregorianCalendar.MONTH) == mes && f1.get(GregorianCalendar.YEAR) == anio) flag = true;
            }
        } else {
            fechabuscar = "" + smes + " de " + anio;
            if (f1.get(GregorianCalendar.MONTH) == mes && f1.get(GregorianCalendar.YEAR) == anio) flag = true;
        }
        return flag;
    }

    public void buscar() {
        String mes = rep.comboBox.currentText();
        contenido = "<html>" + "<head><style type=\"text/css\">" + "<!--" + ".Estilo1 {" + "	font-family: Arial, Helvetica, sans-serif;" + "	color: #000066;" + "}" + ".Estilo2 {color: #990000}" + "-->" + "</style></head><body>";
        tabla = "";
        renglon = "";
        totales = "";
        if (rep.lineEdit.text().compareToIgnoreCase("") != 0 && FacturaP.isNumeric(rep.lineEdit.text())) {
            int anio = Integer.parseInt(rep.lineEdit.text());
            arrfacturato = (FacturaTO[]) ConexionConNegocios.invocaServicio("selectFactura", FacturaTO[].class);
            if (arrfacturato != null) {
                rep.tableWidget.clearContents();
                rep.tableWidget.setRowCount(arrfacturato.length);
                for (int i = 0; i < arrfacturato.length; i++) rep.tableWidget.insertRow(i);
                int i = 0;
                for (int j = 0; j < arrfacturato.length; j++) {
                    if (rango(arrfacturato[j].getFecha(), getMes(mes), anio, mes)) {
                        newItem = new QTableWidgetItem(String.valueOf(arrfacturato[j].getIdFactura()));
                        rep.tableWidget.setItem(i, 0, newItem);
                        newItem = new QTableWidgetItem(arrfacturato[j].getRegistro());
                        rep.tableWidget.setItem(i, 1, newItem);
                        newItem = new QTableWidgetItem(String.valueOf(arrfacturato[j].getFecha()));
                        rep.tableWidget.setItem(i, 2, newItem);
                        newItem = new QTableWidgetItem(String.valueOf(arrfacturato[j].getTarifa()));
                        rep.tableWidget.setItem(i, 3, newItem);
                        newItem = new QTableWidgetItem(String.valueOf(arrfacturato[j].getTotal()));
                        rep.tableWidget.setItem(i, 4, newItem);
                        i++;
                        renglon = renglon + "<tr><td>" + arrfacturato[j].getIdFactura() + "</td>" + "<td>" + arrfacturato[j].getRegistro() + "</td>" + "<td>" + arrfacturato[j].getFecha() + "</td>" + "<td>" + arrfacturato[j].getTarifa() + "</td>" + "<td>" + arrfacturato[j].getTotal() + "</td>" + "</tr>";
                    }
                }
                calcula();
            }
        }
    }

    public int getMes(String mes) {
        int nmes = -1;
        if (mes.compareToIgnoreCase("Enero") == 0) nmes = 0;
        if (mes.compareToIgnoreCase("Febrero") == 0) nmes = 1;
        if (mes.compareToIgnoreCase("Marzo") == 0) nmes = 2;
        if (mes.compareToIgnoreCase("Abril") == 0) nmes = 3;
        if (mes.compareToIgnoreCase("Mayo") == 0) nmes = 4;
        if (mes.compareToIgnoreCase("Junio") == 0) nmes = 5;
        if (mes.compareToIgnoreCase("Julio") == 0) nmes = 6;
        if (mes.compareToIgnoreCase("Agosto") == 0) nmes = 7;
        if (mes.compareToIgnoreCase("Septiembre") == 0) nmes = 8;
        if (mes.compareToIgnoreCase("Octubre") == 0) nmes = 9;
        if (mes.compareToIgnoreCase("Noviembre") == 0) nmes = 10;
        if (mes.compareToIgnoreCase("Diciembre") == 0) nmes = 11;
        return nmes;
    }
}
