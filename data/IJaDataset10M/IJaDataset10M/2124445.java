package solicitudesApp.Functions;

import java.sql.SQLException;
import java.util.Date;
import javax.sql.rowset.CachedRowSet;
import sMySQLappTemplate.Core.Command;
import sMySQLappTemplate.Core.Fechas;
import sMySQLappTemplate.Core.FeatureTemplate;
import solicitudesApp.Core.ApplicationsAppCore;
import solicitudesApp.GUI.FormularioAyMSolicitud;

public class NuevaSolicitud extends ModificarSolicitud {

    public NuevaSolicitud(ApplicationsAppCore app) {
        super(app);
    }

    protected void registarBoton(ApplicationsAppCore app) {
        app.registerButtonForApplicationsTools(new addSolicitud(this), "/images/Whack Notepad ++.png", "Nueva Solicitud");
    }

    @SuppressWarnings("unchecked")
    private class addSolicitud extends Command {

        public addSolicitud(FeatureTemplate feature) {
            super(feature);
        }

        @Override
        public Object ExecCommand(Object... args) {
            cargarTiposSolicitudes();
            cargarTiposRequisitos();
            new FormularioAyMSolicitud(this.receiver, true);
            return null;
        }
    }

    public void cargarTiposSolicitudes() {
        try {
            this.appCore.populateComboBox(dataTiposSolicitudes, "select * from Tipos_Solicitud;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String queryInteresado(String dato) {
        return new String("SELECT " + dato + " " + "FROM Personas JOIN Interesados " + "WHERE DNI = Persona_DNI AND " + "DNI = " + interesadoDNI + ";");
    }

    public int validar(int dni, String t_solicitud) {
        interesadoDNI = String.valueOf(dni);
        try {
            CachedRowSet rta = this.appCore.sendConsult("select Persona_DNI " + "from Interesados " + "where Persona_DNI = " + dni + ";");
            if (rta.next()) {
                cargarDatosInteresado();
                this.appCore.sendCommand("insert into Solicitudes(fechaRegistro, Estado, Interesado_Persona_DNI, Tipo_Solicitud_Denominacion) " + "values ('" + Fechas.convertirDateADateSQL(new Date()) + "','pendiente','" + dni + "','" + t_solicitud + "');");
                rta = this.appCore.sendConsult("select max(idSolicitud)from Solicitudes");
                rta.next();
                int id = rta.getInt(1);
                this.appCore.sendCommand("insert into Pases(fecha, Solicitud_idSolicitud, Destino_Denominacion) values (CURDATE(), " + id + ", 'Administracion DCIC');");
                ((ApplicationsAppCore) this.appCore).cargarTablaSolicitudes();
                idSolActual = id;
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
