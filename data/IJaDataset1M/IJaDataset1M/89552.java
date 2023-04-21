package net.sf.robobo.executor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import net.sf.robobo.Configuracao;
import net.sf.robobo.data.Mensagem;

/**
 * @author partenon
 *
 */
public class Database implements IExecutor {

    /**
	 * Inclui uma mensagem no banco de dados
	 */
    public boolean executar(Mensagem mensagem) throws Exception {
        String servidor = Configuracao.getString("plugin.executor.database.servidor");
        int porta = Configuracao.getString("plugin.executor.database.porta") != null ? Integer.parseInt(Configuracao.getString("plugin.executor.database.porta")) : 3306;
        String usuario = Configuracao.getString("plugin.executor.database.usuario");
        String senha = Configuracao.getString("plugin.executor.database.senha");
        String db = Configuracao.getString("plugin.executor.database.db");
        String tabela = Configuracao.getString("plugin.executor.database.tabela");
        String driver = Configuracao.getString("plugin.executor.database.driver");
        String url = "jdbc:mysql://" + servidor + ":" + porta + "/" + db;
        Class.forName(driver);
        Connection conexao = DriverManager.getConnection(url, usuario, senha);
        Statement statement = conexao.createStatement();
        statement.execute("INSERT INTO " + tabela + " (id, assunto, mensagem) VALUES ('" + mensagem.getId() + "', '" + mensagem.getAssunto() + "', '" + mensagem.getMensagem() + "')");
        return true;
    }
}
