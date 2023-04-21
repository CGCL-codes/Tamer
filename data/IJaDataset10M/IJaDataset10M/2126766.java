package controlador;

import java.util.Vector;
import logica.SistemaGranDT;
import logica.Torneo;
import logica.Usuario;
import logica.administradores.AdministradorDAOs;
import vistas.InscribirTorneoAmigosFrame;

public class InscribirTorneoAmigosControlador {

    private SistemaGranDT logica;

    private AdministradorDAOs adminDAO;

    private InscribirTorneoAmigosFrame frame;

    public InscribirTorneoAmigosControlador() {
        super();
        this.logica = SistemaGranDT.getInstance();
        this.adminDAO = logica.getAdminDAO();
        this.frame = new InscribirTorneoAmigosFrame();
        this.frame.setControlador(this);
        this.frame.setVisible(true);
    }

    public void buscarTorneosPorDuenio(String busqueda) {
        String[] cortado = busqueda.split(" ");
        Vector<Torneo> torneos;
        if (cortado.length == 1) torneos = adminDAO.getTorneosPorDuenio(cortado[0], cortado[0]); else if (cortado.length == 2) torneos = adminDAO.getTorneosPorDuenio(cortado[0], cortado[1]); else torneos = adminDAO.getTorneosPorDuenio("", "");
        for (Torneo t : torneos) {
            this.frame.getListEncontradosModel().addElement(t);
        }
    }

    public void buscarTorneosPorNombre(String busqueda) {
        Vector<Torneo> torneos;
        if (busqueda.isEmpty()) torneos = adminDAO.getTorneosPorNombre(""); else torneos = adminDAO.getTorneosPorNombre(busqueda);
        for (Torneo t : torneos) this.frame.getListEncontradosModel().addElement(t);
    }

    public InscribirTorneoAmigosFrame getFrame() {
        return frame;
    }

    public void setFrame(InscribirTorneoAmigosFrame frame) {
        this.frame = frame;
    }

    public String postularse(Torneo t) {
        Usuario u = logica.getUsuarioActual();
        if (t == null) return "Debe seleccionar el torneo al que desea postularse."; else {
            if (adminDAO.verificarSiEsCreador(t, u)) {
                return "No puede postularse a un torneo del que es creador, ya participa!";
            } else {
                if (adminDAO.verificarSiEsParticipante(t, u)) return "Ya esta participando en el torneo."; else if (adminDAO.verificarSiEsPostulado(t, u)) return "Ya se encuentra postulado en el torneo, debe esperar confirmacion por parte del creador.";
            }
            adminDAO.postularse(t, logica.getUsuarioActual());
            return null;
        }
    }
}
