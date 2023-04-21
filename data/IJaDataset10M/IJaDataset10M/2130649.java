package com.controlexpenses.controller.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador abstrato que possui uma tela de formul�rio e as telas de erro ou sucesso.
 * @author josenio.camelo
 *
 */
public abstract class FormController extends AbstractController {

    /**
	 * P�gina que cont�m o formul�rio.
	 */
    private String view;

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    @Override
    public final String exec(HttpServletRequest req, HttpServletResponse res) {
        if (req.getMethod().equals("POST")) {
            return process(req, res);
        } else {
            return view(req, res);
        }
    }

    /**
	 * Trata requisi��es do tipo GET. Normalmente � usada para adicionar informa��es usadas
	 * na renderiza��o da p�gina do formul�rio
	 * @param req Request
	 * @param res Response
	 * @return p�gina do formul�rio.
	 */
    public abstract String view(HttpServletRequest req, HttpServletResponse res);

    /**
	 * Trata requisi��es do tipo POST. Processa a a��o do formul�rio.
	 * @param req Request
	 * @param res Response
	 * @return pr�xima p�gina.
	 */
    public abstract String process(HttpServletRequest req, HttpServletResponse res);
}
