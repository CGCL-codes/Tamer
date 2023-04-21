package es.devel.opentrats.booking.service.business;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.*;
import es.devel.opentrats.booking.util.OpenTratsBookingUtil;
import java.io.InputStream;
import org.apache.log4j.Logger;

/**
 *
 * @author Fran Serrano
 */
public class ReportService {

    private static ReportService ref = null;

    private String ruta = "";

    private InputStream logo;

    private Connection connection;

    /**
     * Creates a new instance of ReportService
     */
    private ReportService() {
        this.connection = ConnectionService.getInstance().getConnection();
    }

    public static synchronized ReportService getInstance() {
        if (ref == null) {
            ref = new ReportService();
        }
        return ref;
    }

    public synchronized void HistorialCliente(String refcliente) {
        try {
            Map parametros = new java.util.HashMap();
            parametros.put("refcliente", refcliente);
            logo = getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/logo.jpg");
            parametros.put("logo", this.logo);
            JasperPrint jasperprint = null;
            jasperprint = JasperFillManager.fillReport(getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/HistorialCliente.jasper"), parametros, this.connection);
            JasperViewer.viewReport(jasperprint, false);
        } catch (Exception e) {
            e.printStackTrace();
            OpenTratsBookingUtil.Mensaje("Error de generación de informe:\n\n" + e.getMessage() + "\n\n" + e.toString(), "Error de informe...", "e");
            Logger.getRootLogger().error(e.toString());
        }
    }

    public synchronized void Empleados() {
        try {
            Map parametros = new java.util.HashMap();
            logo = getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/logo.jpg");
            parametros.put("logo", this.logo);
            JasperPrint jasperprint = null;
            jasperprint = JasperFillManager.fillReport(getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/Empleados.jasper"), parametros, this.connection);
            JasperViewer.viewReport(jasperprint, false);
        } catch (Exception e) {
            OpenTratsBookingUtil.Mensaje("Error de generación de informe:\n\n" + e.getMessage() + "\n\n" + e.toString(), "Error de informe...", "e");
            Logger.getRootLogger().error(e.toString());
        }
    }

    public synchronized void AgendaDia(int refempleado, Date fecha) {
        try {
            Map parametros = new java.util.HashMap();
            String consulta = "select cit.*,e.nombre as ne,e.apellidos as ae,c.nombre as nc,c.apellidos as ac,c.tfno as tfno,c.movil as movil from citas as cit,clientes as c,empleados as e where cit.dia like '" + new SimpleDateFormat("yyyy-MM-dd").format(fecha) + "' and cit.refempleado=" + refempleado + " and cit.refempleado=e.idempleado and cit.refcliente=c.codcliente group by cit.hora_inicio order by cit.hora_inicio asc";
            parametros.put("consulta", consulta);
            parametros.put("fecha", "'" + new SimpleDateFormat("yyyy-MM-dd").format(fecha) + "'");
            logo = getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/logo.jpg");
            parametros.put("logo", this.logo);
            JasperPrint jasperprint = null;
            jasperprint = JasperFillManager.fillReport(getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/AgendaDia.jasper"), parametros, this.connection);
            JasperViewer.viewReport(jasperprint, false);
            List lista = jasperprint.getPages();
            if (lista.isEmpty() == false) {
                JasperPrintManager.printReport(jasperprint, true);
            } else {
                OpenTratsBookingUtil.Mensaje("El empleado no tiene nada para el día", "Ninguna cita...", "i");
            }
        } catch (Exception e) {
            OpenTratsBookingUtil.Mensaje("Error de generación de informe:\n\n" + e.getMessage() + "\n\n" + e.toString(), "Error de informe...", "e");
            Logger.getRootLogger().error(e.toString());
        }
    }

    public synchronized void AgendaColumnas(int columna, Date fecha) {
        try {
            Map parametros = new java.util.HashMap();
            String consulta = "select cit.*,e.nombre as ne,e.apellidos as ae,c.nombre as nc,c.apellidos as ac,c.tfno as tfno,c.movil as movil from citas as cit,clientes as c,empleados as e where cit.dia like '" + new SimpleDateFormat("yyyy-MM-dd").format(fecha) + "' and cit.cabina=" + columna + " and cit.refdelegacion=" + EnvironmentService.getInstance().getDelegation() + " and cit.refempleado=e.idempleado and cit.refcliente=c.codcliente group by cit.hora_inicio order by cit.hora_inicio asc";
            parametros.put("consulta", consulta);
            logo = getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/logo.jpg");
            parametros.put("logo", this.logo);
            JasperPrint jasperprint = null;
            jasperprint = JasperFillManager.fillReport(getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/AgendaCabinas.jasper"), parametros, this.connection);
            List lista = jasperprint.getPages();
            if (!lista.isEmpty()) {
                JasperViewer.viewReport(jasperprint, false);
            } else {
                OpenTratsBookingUtil.Mensaje("La cabina está libre", "Ninguna cita...", "i");
            }
        } catch (Exception e) {
            OpenTratsBookingUtil.Mensaje("Error de generación de informe:\n\n" + e.getMessage() + "\n\n" + e.toString(), "Error de informe...", "e");
            Logger.getRootLogger().error(e.toString());
        }
    }

    public synchronized void Presupuesto(int refpresupuesto) {
        try {
            Map parametros = new HashMap();
            Integer presupuesto = new Integer(refpresupuesto);
            parametros.put("idpresupuesto", presupuesto);
            logo = getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/logo.jpg");
            parametros.put("logo", this.logo);
            JasperPrint jasperprint = null;
            jasperprint = JasperFillManager.fillReport(getClass().getResourceAsStream("/es/devel/opentrats/booking/resources/reports/Presupuesto.jasper"), parametros, this.connection);
            JasperViewer.viewReport(jasperprint, false);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getRootLogger().error(e.toString());
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}
