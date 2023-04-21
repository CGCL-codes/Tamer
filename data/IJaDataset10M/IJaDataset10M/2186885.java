package br.com.simtecnologia.access.controll;

import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.simtecnologia.access.controll.annotation.ControlledResource;

/**
 * Uma sessão de login usada para verificar o acesso a um @{@link ControlledResource}.
 * @author Tomaz Lavieri
 */
public interface LoginSession {

    /**
	 * Informa se a requisição esta sendo feita através de uma sessão logada.
	 */
    public boolean isLogged();

    /**
	 * Informa se a sessão logada libera o acesso ao recurso indicado.
	 * @param method O recurso a ser acessado.
	 */
    public boolean hasAccess(ResourceMethod method);
}
