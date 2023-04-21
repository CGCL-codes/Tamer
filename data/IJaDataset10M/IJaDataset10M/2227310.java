package lg.controllers.excursiones;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lg.controllers.SimpleAbstractController;
import lg.domain.bean.Excursion;
import lg.domain.bean.Salida;
import lg.servicios.api.ExcursionServicio;
import lg.servicios.api.SalidasServicio;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Nicolas
 *
 */
public class DetalleExcursionController extends SimpleAbstractController {

    private ExcursionServicio excursionServicio;

    private SalidasServicio salidasServicio;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Excursion exc = excursionServicio.buscarPorId(id);
        Date fechaDesde = new Date();
        Integer cantDias = 7;
        exc.setSalidas(salidasServicio.buscarProximasDeExc(exc, fechaDesde, cantDias));
        request.setAttribute("excursion", exc);
        return new ModelAndView(successView);
    }

    /**
	 * @return the excursionServicio
	 */
    public ExcursionServicio getExcursionServicio() {
        return excursionServicio;
    }

    /**
	 * @param excursionServicio the excursionServicio to set
	 */
    public void setExcursionServicio(ExcursionServicio excursionServicio) {
        this.excursionServicio = excursionServicio;
    }

    public SalidasServicio getSalidasServicio() {
        return salidasServicio;
    }

    public void setSalidasServicio(SalidasServicio salidasServicio) {
        this.salidasServicio = salidasServicio;
    }
}
