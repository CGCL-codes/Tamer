package es.caib.sistra.back.action.tramiteVersion;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;
import es.caib.regtel.persistence.delegate.DelegateRegtelUtil;
import es.caib.regtel.persistence.delegate.RegistroTelematicoDelegate;
import es.caib.sistra.back.action.BaseController;
import es.caib.sistra.modelInterfaz.ConstantesDominio;
import es.caib.sistra.persistence.delegate.DelegateUtil;
import es.caib.sistra.persistence.delegate.IdiomaDelegate;

/**
 * Guarda la lista de tramiteVersions de un tramiteVersion en el contexto.
 */
public class TramiteVersionController extends BaseController {

    protected static Log log = LogFactory.getLog(TramiteVersionController.class);

    public void perform(ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) throws ServletException, IOException {
        try {
            log.debug("Entramos en TramiteVersionController");
            IdiomaDelegate delegate = DelegateUtil.getIdiomaDelegate();
            List lenguajes = delegate.listarLenguajes();
            request.setAttribute("listaLenguajes", lenguajes);
            RegistroTelematicoDelegate dlgRte = DelegateRegtelUtil.getRegistroTelematicoDelegate();
            List organosDestino = dlgRte.obtenerServiciosDestino();
            List oficinasRegistro = dlgRte.obtenerOficinasRegistro();
            List tiposAsunto = dlgRte.obtenerTiposAsunto();
            request.setAttribute("listaorganosdestino", ajustarTamListaDesplegable(organosDestino, MAX_COMBO_DESC, "DESCRIPCION"));
            request.setAttribute("listaoficinasregistro", ajustarTamListaDesplegable(oficinasRegistro, MAX_COMBO_DESC, "DESCRIPCION"));
            request.setAttribute("listatiposasunto", ajustarTamListaDesplegable(tiposAsunto, MAX_COMBO_DESC, "DESCRIPCION"));
            request.setAttribute("listaunidadesadministrativas", ajustarTamListaDesplegable(obtenerValoresDominio(ConstantesDominio.DOMINIO_SAC_UNIDADES_ADMINISTRATIVAS), MAX_COMBO_DESC, "DESCRIPCION"));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
