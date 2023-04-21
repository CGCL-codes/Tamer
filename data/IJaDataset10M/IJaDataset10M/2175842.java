package Dao;

import Model.Atividade;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Esta é uma interface com a assinatura dos métodos que realizam operações no banco de dados
 * relacionada as atividades, é subclasse da interface DAO.
 *
 * @author Tiago Peres
 * @version 3.0
 * @since 3.0
 */
public interface AtividadeDao<T extends Atividade> extends Dao<T> {

    /**
     *
     * @author Tiago Peres
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     * @param idGrupo id do grupo PET, periodoInicio e periodoFim periodo em que a atividade foi realizada .
     * @return Lista de todas as atividades do grupo PET e entra as datas passadas como parâmetro.
     */
    ArrayList<T> listarTodos(int idGrupo, Date periodoInicio, Date periodoFim) throws SQLException;
}
