package caraacara;

import java.sql.*;

/**
 *
 * @author HP
 */
public class BancoDados {

    private final String bancodeDados = "caraacara";

    private final String tabela = "config";

    private Connection connection;

    private Statement statem;

    private ResultSet resultSet;

    private ResultSetMetaData rsMetaData;

    /**
     * Creates a new instance of BancoDados
     */
    public BancoDados() {
        openConnection(bancodeDados);
        createStatement();
    }

    /** Pega nome do jogador */
    public String getNome() {
        ResultSet resposta = runQuery(statem, "SELECT * FROM `" + tabela + "` WHERE propriedade = 'nome'");
        String retVal;
        try {
            if (resposta.getFetchSize() == 0) return "";
            resposta.next();
            retVal = resposta.getString("valor_string");
        } catch (SQLException ex) {
            retVal = "";
            ex.printStackTrace();
        }
        return retVal;
    }

    /** Atualiza nome do jogador */
    public void setNome(String nome) {
        nome = nome.trim();
        runQuery(statem, "UPDATE `" + tabela + "` SET  valor_string = '" + nome + "' WHERE propriedade = 'nome'");
    }

    /** Pega n�mero de cartas secretas */
    public int getNumCartasSecretas() {
        ResultSet resposta = runQuery(statem, "SELECT * FROM `" + tabela + "` WHERE propriedade = 'num_cartas_secretas'");
        int retVal;
        try {
            if (resposta.getFetchSize() == 0) return 3;
            resposta.next();
            retVal = resposta.getInt("VALOR_INT");
        } catch (SQLException ex) {
            retVal = 3;
            ex.printStackTrace();
        }
        return retVal;
    }

    /** Atualiza n�mero de cartas secretas */
    public void setNumCartasSecretas(int numCartasSec) {
        runQuery(statem, "UPDATE `" + tabela + "` SET  valor_int = '" + numCartasSec + "' WHERE propriedade = 'num_cartas_secretas'");
    }

    /** Conecta � base de dados*/
    private void openConnection(String nomeBase) {
        String url = "jdbc:odbc:" + nomeBase;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException cnfex) {
            System.err.println("Falhou ao carregar JDBC/ODBC driver.");
            cnfex.printStackTrace();
            System.exit(1);
        } catch (SQLException sqlex) {
            System.err.println(" N�o consegui conectar! ");
            sqlex.printStackTrace();
            System.exit(1);
        }
    }

    /** Roda query no banco de dados, retornando resultado
     */
    private ResultSet runQuery(Statement state, String query) {
        try {
            return state.executeQuery(query);
        } catch (SQLException sqlex) {
        }
        return null;
    }

    /** Cria statement com banco de dados */
    private void createStatement() {
        try {
            statem = connection.createStatement();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    /** Fecha statement com o banco de dados
     */
    private void closeStatement() {
        try {
            statem.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    /** Fecha conexao com o banco de dados
     */
    private void closeConnection() {
        try {
            connection.close();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        closeStatement();
        closeConnection();
        super.finalize();
    }
}
