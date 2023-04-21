package lg.controllers.localidades;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lg.controllers.CommandAbstractController;
import lg.servicios.api.LocalidadesServicio;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Nicolas
 *
 */
public class LocalidadesListarController extends CommandAbstractController {

    private LocalidadesServicio localidadesServicio;

    @Override
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException ex) throws Exception {
        return new ModelAndView(successView);
    }

    /**
	 * @return the localidadesServicio
	 */
    public LocalidadesServicio getLocalidadesServicio() {
        return localidadesServicio;
    }

    /**
	 * @param localidadesServicio the localidadesServicio to set
	 */
    public void setLocalidadesServicio(LocalidadesServicio localidadesServicio) {
        this.localidadesServicio = localidadesServicio;
    }
}
