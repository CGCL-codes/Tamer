package br.org.acessobrasil.portal.plugin;

import javax.servlet.http.HttpServletRequest;
import br.org.acessobrasil.portal.Gerenciador;

/**
 * Interface do plugin, para extender as funcionalidades do gerenciador 
 * @author Fabio Issamu Oshiro
 */
public interface Plugin {

    /**
	 * Informa a requisi��o do usu�rio
	 * @param request requisi��o do usu�rio
	 */
    public void setHttpServletRequest(HttpServletRequest request);

    /**
	 * Retorna o HTML tratado pelo plugin
	 * @return
	 */
    public String getHtml();

    /**
	 * Atribui o nome do plugin
	 * @param nome nome do plugin, normalmente o nome da classe
	 */
    public void setNome(String nome);

    /**
	 * Retorna o nome do plugin
	 * @return o nome do plugin
	 */
    public String getNome();

    public void setNuPagina(long nu_pagina);

    /**
	 * Indica qual p�gina � utilizada como padr�o
	 * para este plugin
	 * @return nu_pagina
	 */
    public long getNuPagina();

    /**
	 * Recebe o Template da �rea do plugin
	 * @param template
	 */
    public void setTemplate(String template);

    /**
	 * Recebe a inst�ncia do gerenciador
	 * @param gerenciador
	 */
    public void setGerenciador(Gerenciador gerenciador);
}
