package br.softwaresolutions.virtualstore.command;

import br.softwaresolutions.virtualstore.exception.CommandException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import br.softwaresolutions.virtualstore.exception.CommandException;

/**
 * @author robson.ferreira
 * Interface de comandos. Define o metodo <code>execute</code> onde cada subclasse deve 
 * informar o <code>request</code> implementar o comando especificado.
 */
public interface CommandAction {

    public String execute(HttpServletRequest req) throws CommandException;
}
