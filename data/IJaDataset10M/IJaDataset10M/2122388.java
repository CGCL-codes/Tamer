package com.controlexpenses.controller.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador abstrato que possui propriedades de pagina de erro e sucesso
 * @author josenio.camelo
 *
 */
public abstract class AbstractController extends Object implements Controller {

    /**
	 * P�gina de sucesso
	 */
    private String success;

    /**
	 * P�gina de erro
	 */
    private String error;

    /**
	 * Processa a requisi��o e retorna a pr�xima p�gina.
	 */
    public abstract String exec(HttpServletRequest req, HttpServletResponse res);

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
