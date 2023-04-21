package ar.fi.uba.apolo.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import ar.fi.uba.apolo.command.BackPropiedadesCmd;
import ar.fi.uba.apolo.exception.ElementoExisteException;
import ar.fi.uba.apolo.exception.ElementoNoExisteException;
import ar.fi.uba.apolo.iface.ABMTemplate;
import ar.fi.uba.apolo.impl.ExamenABMImpl;
import ar.fi.uba.apolo.impl.PreguntaABMImpl;

public class BackPropiedadesCtl extends ControladorSeguro {

    public ModelAndView handleData(HttpServletRequest request, HttpServletResponse response, Object command) {
        BackPropiedadesCmd params = (BackPropiedadesCmd) command;
        ABMTemplate elem;
        if (params.getProps().equals(BackPropiedadesCmd.PROPS_PREGUNTA)) {
            elem = new PreguntaABMImpl(request.getSession());
        } else {
            elem = new ExamenABMImpl(request.getSession());
        }
        if (params.getAccion().equals(BackPropiedadesCmd.ACCION_GRABAR)) {
            accionGrabar(elem, params);
        } else if (params.getAccion().equals(BackPropiedadesCmd.ACCION_PROCESAR)) {
            accionProcesar(elem, response);
            return null;
        }
        String view = accionConsulta(elem, params, request);
        request.setAttribute("parametros", params);
        return new ModelAndView(view);
    }

    private String accionConsulta(ABMTemplate elem, BackPropiedadesCmd params, HttpServletRequest request) {
        int id = params.getId();
        if (this.getErrMsg() == null) params = null;
        request.setAttribute("elemento", elem.leerElemento(id, params));
        return elem.getNombrePantallaDetalle();
    }

    private void accionProcesar(ABMTemplate elem, HttpServletResponse response) {
        try {
            response.getWriter().write(elem.stringElementos());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void accionGrabar(ABMTemplate elem, BackPropiedadesCmd params) {
        try {
            String msg = elem.controlar(params);
            if (msg.equals(ABMTemplate.RESPUESTA_OK)) {
                if (params.getOperacion().equals(BackPropiedadesCmd.OPERACION_ALTA)) {
                    elem.crearElemento(params);
                } else if (params.getOperacion().equals(BackPropiedadesCmd.OPERACION_MODIFICACION)) {
                    elem.actualizarElemento(params.getId(), params);
                }
                params.setAccion(BackPropiedadesCmd.ACCION_CERRAR);
            } else this.setErrMsg(msg);
        } catch (ElementoExisteException e) {
            this.setErrMsg(e.getMessage());
        } catch (ElementoNoExisteException e) {
            this.setErrMsg(e.getMessage());
        }
    }
}
