package br.unifor.finance.presentation.command;

import br.unifor.finance.presentation.webconfig.WebConfigReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe Command Gen�rica usada para as a��es comuns �s classes do tipo Command.
 *
 * @author Clebernaice
 * @author Cleilson
 * @author Leonardo
 * @version 1.0, Julho/2007
 */
public abstract class GenericCommand implements Command {

    /**
   * jspShow � o m�todo chamado quando uma url do tipo *.view � interceptada pelo
   * {@link br.unifor.finance.presentation.controller.ApplicationController}.
   * <p>
   * Quando esse m�todo � chamda o arquivo .jsp associado a esse Command � exibido.
   *
   * @param request Objeto solicita��o
   * @param response Objeto resposta
   * @return void
   */
    public void jspShow(HttpServletRequest request, HttpServletResponse response) {
        WebConfigReader webConfigReader = (WebConfigReader) request.getSession(false).getAttribute("webConfigReader");
        try {
            beforeJspShow(request, response);
            request.getRequestDispatcher(webConfigReader.getLayoutPathProperty()).forward(request, response);
        } catch (ServletException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
   * beforeJspShow � o m�todo chamado antes do arquivo .jsp ser exibido.
   * <p>
   * Esse m�todo � geralmente usado para fornecer a p�gina algo que ela necessite.
   * <br>
   * Por exemplo: Colocar uma collection na sessao para que quando o .jsp for exibido
   * ele consiga popular uma tabela corretamente.
   *
   * @param request Objeto solicita��o
   * @param response Objeto resposta
   * @return void
   */
    public void beforeJspShow(HttpServletRequest request, HttpServletResponse response) {
    }

    public Boolean validate(HttpServletRequest request, HttpServletResponse response) {
        return false;
    }
}
