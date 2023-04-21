package qvoipm.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import qvoipm.dados.Fim;
import qvoipm.dados.Inicio;
import qvoipm.dados.Qualidade;

/**
 *
 * @author Rodrigo
 */
public class BD {

    private static Connection conexao;

    public static final int ABERTA = 0;

    public static final int FECHADA = 1;

    public static final int INEXISTENTE = 2;

    private BD() {
    }

    /**
     * Retorna conexao
     * @return Conexao com o BD.
     */
    private static Connection getConexao() throws SQLException, ClassNotFoundException {
        if (conexao != null) return conexao; else {
            Class.forName("com.mysql.jdbc.Driver");
            conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/qvoipm", "qvoipm", "oriondcuel");
            return conexao;
        }
    }

    /**
     * Retorna o valor do ultimo codigo adicionado a
     * uma determinada tabela.
     * @param tabela Nome da tabela
     * @return valor do ultimo campo codigo adicionado
     */
    private static int getCodigo(String tabela) throws SQLException, ClassNotFoundException {
        ResultSet resultado = getConexao().createStatement().executeQuery("select AUTO_INCREMENT FROM information_schema.TABLES WHERE " + "TABLE_SCHEMA='qvoipm' AND TABLE_NAME='" + tabela + "'");
        resultado.next();
        return resultado.getInt(1) - 1;
    }

    /**
     * Cria nova chamada no banco de dados a partir de um
     * pacote de requisição de chamada, e retorna o codigo
     * da chamada.
     * @param inicio Pacote de requisição da chamada
     * @return Código da chamada
     */
    public static int novaChamada(Inicio inicio) {
        try {
            getConexao().createStatement().executeUpdate("INSERT INTO qvoipm.Chamadas VALUES " + "(null, INET_ATON('" + inicio.getIpOrigem().toString().substring(1) + "'), INET_ATON('" + inicio.getIpDestino().toString().substring(1) + "'), TRUE, " + inicio.getDataHora().getTime() + ", null, " + inicio.getFrequencia() + ", 0, 0, 0, 0, 0)");
            return getCodigo("Chamadas");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**
     * Insere pacote de qualidade no banco de dados
     * @param qualidade Pacote de qualidade
     */
    public static void inserirQualidade(Qualidade qualidade) {
        try {
            getConexao().createStatement().executeUpdate("INSERT INTO qvoipm.Qualidade VALUES " + "(null, " + qualidade.getChamada() + ", " + qualidade.getCodec() + ", " + qualidade.getJitter() + ", " + qualidade.getAtraso() + ", " + qualidade.getPerda() + ", " + qualidade.getFatorR() + ")");
            getConexao().createStatement().executeUpdate("UPDATE qvoipm.Chamadas SET " + "nPacotes=nPacotes+1, sJitter=sJitter+" + qualidade.getJitter() + ", sAtraso=sAtraso+" + qualidade.getAtraso() + ", sPerda=sPerda+" + qualidade.getPerda() + ", sFatorR=sFatorR+" + qualidade.getFatorR() + " WHERE id=" + qualidade.getChamada());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Finaliza chamada.
     * @param fim Pacote de finalização
     */
    public static void fecharChamada(Fim fim) {
        try {
            getConexao().createStatement().executeUpdate("UPDATE qvoipm.Chamadas SET dataFim=" + fim.getDataHora().getTime() + ", aberta=FALSE WHERE id=" + fim.getChamada());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Verifica estado da chamada
     * @param id Id da chamada
     * @return ABERTA, FECHADA ou INEXISTENTE
     */
    public static int statusChamada(int id) {
        try {
            ResultSet resultado = getConexao().createStatement().executeQuery("SELECT aberta FROM qvoipm.Chamadas WHERE id=" + id);
            if (!resultado.next()) return INEXISTENTE;
            return (resultado.getBoolean(1)) ? ABERTA : FECHADA;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return INEXISTENTE;
    }
}
