package br.org.skenp.control;

/**
 * @author Davy Diegues Duran
 *
 */
public interface Command {

    public Object execute(Object... parameters);
}
