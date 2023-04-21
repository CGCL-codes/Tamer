package app.controlador;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import com.thoughtworks.xstream.XStream;
import vo.ArticuloVO;
import vo.PendienteVO;
import app.modelo.BusinessdelegateCD;
import framework.controlador.Controlador;
import framework.modelo.Businessdelegate;
import framework.vista.Vista;

public class ControladorSolicitudDeFabricacion extends Controlador {

    public ControladorSolicitudDeFabricacion(Businessdelegate mod, Vista v) {
        super(mod, v);
    }

    @SuppressWarnings("unchecked")
    public Vector obtenerAEnviarYPendientes() {
        return ((BusinessdelegateCD) (this.getModelo())).obtenerAEnviarYPendientes();
    }

    @SuppressWarnings("unchecked")
    public Vector obtenerArticulos() {
        return ((BusinessdelegateCD) (this.getModelo())).obtenerArticulos();
    }

    @SuppressWarnings("unchecked")
    public Vector obtenerPendientesConArticulo(ArticuloVO articulo) {
        return ((BusinessdelegateCD) (this.getModelo())).obtenerPendientesConArticulo(articulo);
    }

    public void registrarAPedirAFabrica() {
        ((BusinessdelegateCD) (this.getModelo())).registrarAPedirAFabrica();
    }

    @SuppressWarnings("unchecked")
    public void generarSolicitudDeFabricacionXML() {
        @SuppressWarnings("unused") Vector pendientes;
        PendienteVO pendiente;
        @SuppressWarnings("unused") Vector articulos;
        ArticuloVO articulo;
        int cantidad = 0;
        articulos = this.obtenerArticulos();
        if (articulos.size() != 0) {
            String file = "SolFabr.xml";
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e1) {
                System.out.println("No se pudo generar el XML SolFabr");
            }
            PrintStream printStream = new PrintStream(out);
            XStream xstream = new XStream();
            xstream.alias("Articulo", ArticuloVO.class);
            for (int i = 0; i < articulos.size(); i++) {
                articulo = (ArticuloVO) articulos.get(i);
                pendientes = this.obtenerPendientesConArticulo(articulo);
                if (pendientes.size() != 0) {
                    for (int j = 0; j < pendientes.size(); j++) {
                        pendiente = (PendienteVO) pendientes.get(j);
                        cantidad = cantidad + pendiente.getAPedirAFabrica();
                    }
                    cantidad = cantidad * 2;
                    articulo.setStock(cantidad);
                    String afabricar = xstream.toXML(articulo);
                    printStream.println(afabricar);
                }
                cantidad = 0;
            }
            printStream.close();
            this.registrarAPedirAFabrica();
        }
    }
}
