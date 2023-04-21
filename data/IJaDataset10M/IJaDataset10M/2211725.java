package lg.controllers.vehiculos;

import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lg.commands.vehiculo.VehiculoForm;
import lg.domain.bean.Vehiculo;
import lg.servicios.api.SeccionServicio;
import lg.servicios.api.VehiculosServicio;
import lg.utiles.Info;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author Nicolas
 *
 */
public class AgregarVehiculosController extends SimpleFormController {

    private VehiculosServicio vehiculosServicio;

    private SeccionServicio seccionServicio;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        VehiculoForm form = (VehiculoForm) command;
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setCapacidad(form.getCapacidad());
        vehiculo.setImagen(form.getImagen());
        vehiculo.setMarca(form.getMarca());
        vehiculo.setModelo(form.getModelo());
        vehiculo.setPatente(form.getPatente());
        vehiculosServicio.guardarNuevo(vehiculo);
        ArrayList<Info> mensajes = new ArrayList<Info>(0);
        mensajes.add(new Info(Info.SUCCESS, "Vehiculo agregado correctamente"));
        request.setAttribute("mensajes", mensajes);
        request.getRequestDispatcher("/vehiculos/listar.html").forward(request, response);
        request.setAttribute("vehiculos", vehiculosServicio.buscarTodosVehiculos());
        String accion = "/vehiculos/listar.html";
        request.getSession().setAttribute("posicionador", seccionServicio.posicionadorSegunAccion(accion));
        return new ModelAndView(getSuccessView());
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    /**
	 * @return the vehiculosServicio
	 */
    public VehiculosServicio getVehiculosServicio() {
        return vehiculosServicio;
    }

    /**
	 * @param vehiculosServicio the vehiculosServicio to set
	 */
    public void setVehiculosServicio(VehiculosServicio vehiculosServicio) {
        this.vehiculosServicio = vehiculosServicio;
    }

    /**
	 * @return the seccionServicio
	 */
    public SeccionServicio getSeccionServicio() {
        return seccionServicio;
    }

    /**
	 * @param seccionServicio the seccionServicio to set
	 */
    public void setSeccionServicio(SeccionServicio seccionServicio) {
        this.seccionServicio = seccionServicio;
    }
}
