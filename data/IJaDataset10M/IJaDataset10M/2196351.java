package database;

import entidades.Hibernate;
import java.util.ArrayList;

/**
 *
 * @author 0213101
 */
public interface OperacoesDAO {

    /**
     *
     */
    Hibernate h = new Hibernate();

    /**
     *
     * @param obj
     * @return
     */
    public boolean inserir(Object obj);

    /**
     *
     * @return
     */
    public ArrayList pesquisar();

    /**
     *
     * @param obj
     * @return
     */
    public boolean editar(Object obj);

    /**
     *
     * @param obj
     * @return
     */
    public boolean excluir(Object obj);
}
