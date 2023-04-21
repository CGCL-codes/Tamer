package colatex.executor;

import colatex.error.ColatexError;
import colatex.proyectos.InfoProyecto;
import colatex.proyectos.Proyectos;
import java.rmi.Naming;
import java.util.Map;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author miguel
 */
public class ProyectosExecutor extends Executor {

    public ProyectosExecutor(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public ProyectosExecutor(Map<String, String> parameters) {
        super(parameters);
    }

    public String doExecuteOperation() {
        try {
            String action = this.getParameter("action");
            if (action == null) throw new Exception("Servlet action no especificada");
            Proyectos proyectos = (Proyectos) Naming.lookup("rmi://localhost/proyectos");
            if (action.equals("get")) {
                String nombre = this.getParameter("name");
                Vector<InfoProyecto> vProyectos = proyectos.getProyectos();
                this.code = ColatexError.OK;
                this.message = "Ok";
                this.res += "<projects>\n";
                for (InfoProyecto proyecto : vProyectos) {
                    if (nombre == null || nombre.equals(proyecto.getNombre())) {
                        this.res += "   <project>\n";
                        this.res += "       <name>" + proyecto.getNombre() + "</name>\n";
                        this.res += "       <description>" + proyecto.getDescripcion() + "</description>\n";
                        this.res += "       <tempDir>" + proyecto.getDirTemporal() + "</tempDir>\n";
                        this.res += "       <compCommand>" + proyecto.getComandoCompilacion() + "</compCommand>\n";
                        this.res += "   </project>\n";
                    }
                }
                this.res += this.XMLStatus(this.code, this.message);
                this.res += "</projects>\n";
            }
            if (action.equals("add")) {
                String nombre = this.getParameter("name");
                if (nombre == null) {
                    this.code = ColatexError.ERR_NOMBRE_VACIO;
                    this.message = "Empty name";
                } else {
                    String descripcion = this.getParameter("description");
                    String tempDir = this.getParameter("tempDir");
                    String compCommand = this.getParameter("compCommand");
                    InfoProyecto nuevoProyecto = new InfoProyecto(nombre, descripcion, tempDir, compCommand);
                    if (proyectos.addProyecto(nuevoProyecto)) {
                        this.code = ColatexError.OK;
                        this.message = "Ok, new proyect created: " + nombre;
                    } else {
                        this.code = ColatexError.ERR_PROYECTO_YA_EXISTE;
                        this.message = "Proyect already exists: " + nombre;
                    }
                }
                this.res += this.XMLStatus(this.code, this.message);
            }
            if (action.equals("delete")) {
                String nombre = this.getParameter("name");
                if (nombre == null) {
                    this.code = ColatexError.ERR_NOMBRE_VACIO;
                    this.message = "Empty name";
                } else {
                    if (!proyectos.existeProyecto(nombre)) {
                        this.code = ColatexError.ERR_PROYECTO_NO_EXISTE;
                        this.message = "Unknown proyect: " + nombre;
                    } else {
                        proyectos.deleteProyecto(nombre);
                        this.code = ColatexError.OK;
                        this.message = "Ok, proyect deleted: " + nombre;
                    }
                }
                this.res += this.XMLStatus(this.code, this.message);
            }
            if (action.equals("update")) {
                String nombre = this.getParameter("name");
                if (nombre == null) {
                    this.code = ColatexError.ERR_NOMBRE_VACIO;
                    this.message = "Empty name";
                } else {
                    if (!proyectos.existeProyecto(nombre)) {
                        this.code = ColatexError.ERR_PROYECTO_NO_EXISTE;
                        this.message = "Unknown proyect: " + nombre;
                    } else {
                        String nuevaDesc = this.getParameter("description");
                        String tempDir = this.getParameter("tempDir");
                        String compCommand = this.getParameter("compCommand");
                        InfoProyecto datos = new InfoProyecto(nombre, nuevaDesc, tempDir, compCommand);
                        proyectos.updateProyecto(datos);
                        this.code = ColatexError.OK;
                        this.message = "Ok, proyect updated: " + nombre;
                    }
                }
                this.res += this.XMLStatus(this.code, this.message);
            }
        } catch (Exception ex) {
            this.code = ColatexError.ERR_EXCEPCION_GENERICA;
            this.message = colatex.util.Util.shortStackTraceStr(ex);
            this.res = this.XMLStatusOnly(this.code, this.message);
        }
        return this.res;
    }
}
