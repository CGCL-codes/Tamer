package Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import Model.Extensao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Esta é uma classe que realiza operações no banco de dados
 * relacionada as atividades de extensao, é subclasse de JDBCDao e implementa
 * a interface ExtensaoDAO.
 *
 * @author Eduardo Silva Rosa
 * @version 3.0
 * @since 3.0
 */
public class JDBCExtensaoDao extends JDBCAtividadePublicaDao<Extensao> implements ExtensaoDao {

    /**
     * Construtor da classe que recebe uma conexão de uma classe a nível de Controller.
     *
     * @author Eduardo Silva Rosa
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     * @param Conexão com o banco de dados MySQL, recebida de uma classe a nível de Controller.
     */
    public JDBCExtensaoDao(Connection conn) throws SQLException {
        super(conn);
    }

    /**
     * Construtor da classe.
     *
     * @author Eduardo Silva Rosa
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     */
    public JDBCExtensaoDao() throws SQLException {
        super(null);
    }

    /**
     * Recebe uma atividade de extensao, parâmetro "extensao", e o insere no banco de dados.
     *
     * @author Eduardo Silva Rosa
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     * @param atividade extensao que será inserida no banco de dados.
     * @return o próximo id, da sequência de incremento, que terá a próxima atividade extensao a ser inserida.
     */
    @Override
    public int salvar(Extensao extensao) throws SQLException {
        int proximoId = super.salvar(extensao);
        Statement st = getConexao().createStatement();
        String sql = "insert into extensao values(" + proximoId + ");";
        System.out.println(sql);
        st.executeUpdate(sql);
        st.close();
        return proximoId;
    }

    /**
     * Busca uma atividade de extensao do banco de dados, que tenha o id passado como parâmetro.
     *
     * @author Eduardo Silva Rosa
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     * @param id da atividade extensao a ser recuperada.
     * @return atividade extensao recuperada do banco de dados.
     */
    @Override
    public Extensao recuperarPorId(int id) throws SQLException {
        Statement st = getConexao().createStatement();
        String sql = "select a.id as id, a.id_grupo as id_grupo, titulo, parceiros, descricao, justificativa," + " datainicio, datafim, comentario, resultadosesperados, resultadosalcancados," + " coletiva, natureza.id as id_natureza, natureza.nome as nome_natureza, publicoalvo " + "from atividadepublica as atp, atividade as a, natureza, extensao as x " + "where atp.id = " + id + " and atp.id = a.id and (atp.id_natureza = natureza.id or (atp.id_natureza = 1 and natureza.id_grupo is null)) and a.id = x.id";
        System.out.println(sql);
        ResultSet rs = st.executeQuery(sql);
        Extensao e = null;
        if (rs.next()) {
            e = new Extensao();
            preencher(e, rs);
        }
        st.close();
        return e;
    }

    /**
     * Carrega do banco de dados uma lista de todas as atividade de extensao
     * do grupo PET em determinada data passadas por parâmetro.
     *
     * @author Eduardo Silva Rosa
     * @throws SQLException
     * @version 3.0
     * @since 3.0
     * @param idGrupo id do grupo PET, periodoInicio, periodoFim, datas em que ocorreu a atividade de extensao.
     * @return Lista de todas as atividades de extensao do grupo PET e entre as datas passadas como parâmetro.
     */
    @Override
    public ArrayList<Extensao> listarTodos(int idGrupo, Date periodoInicio, Date periodoFim) throws SQLException {
        SimpleDateFormat formatoSQL = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Extensao> atividades = new ArrayList<Extensao>();
        Statement st = getConexao().createStatement();
        String sql = "select a.id, a.id_grupo as id_grupo, titulo, parceiros, descricao, justificativa," + " datainicio, datafim, comentario, resultadosesperados, resultadosalcancados," + " coletiva, natureza.id as id_natureza, natureza.nome as nome_natureza, publicoalvo " + "from atividadepublica as atp, atividade as a, extensao as x, natureza " + "where atp.id = a.id and a.id = x.id and (atp.id_natureza = natureza.id or (atp.id_natureza = 1 and natureza.id_grupo is null)) and a.id_grupo = " + idGrupo + " and " + "((datainicio is null and datafim is null) ||" + "((datainicio >= '" + formatoSQL.format(periodoInicio) + "' or (datainicio is null and datafim >= '" + formatoSQL.format(periodoInicio) + "')) and " + "(datafim <= '" + formatoSQL.format(periodoFim) + "' or (datafim is null and datainicio <= '" + formatoSQL.format(periodoFim) + "')))) order by titulo";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Extensao e = new Extensao();
            preencher(e, rs);
            atividades.add(e);
        }
        st.close();
        return atividades;
    }
}
