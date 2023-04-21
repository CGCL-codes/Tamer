package tiposeventos;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author Anderson
 */
public class NegocioTipoEvento {

    ConexaoTiposEventosBD conexaoTiposEventosBD;

    public NegocioTipoEvento() throws ClassNotFoundException, SQLException {
        conexaoTiposEventosBD = new ConexaoTiposEventosBD();
    }

    public void adicionar(TipoEvento tipoEvento) throws SQLException, ClassNotFoundException {
        conexaoTiposEventosBD.adicionar(tipoEvento);
    }

    public void remover(TipoEvento tipoEvento, HashMap<String, String> operadores, Boolean operacao) throws SQLException, ClassNotFoundException {
        conexaoTiposEventosBD.remover(tipoEvento, operadores, operacao);
    }

    public void remover(TipoEvento tipoEvento, Boolean operacao) throws SQLException, ClassNotFoundException {
        conexaoTiposEventosBD.remover(tipoEvento, operacao);
    }

    public void remover(TipoEvento tipoEvento) throws SQLException, ClassNotFoundException {
        conexaoTiposEventosBD.remover(tipoEvento);
    }

    public void atualizar(TipoEvento tipoEvento) throws SQLException, ClassNotFoundException {
        conexaoTiposEventosBD.atualizar(tipoEvento);
    }

    public TipoEvento procurar(TipoEvento tipoEvento) throws SQLException, ClassNotFoundException {
        return conexaoTiposEventosBD.procurar(tipoEvento);
    }

    public TipoEvento procurar(TipoEvento tipoEvento, HashMap<String, String> operadores) throws SQLException, ClassNotFoundException {
        return conexaoTiposEventosBD.procurar(tipoEvento, operadores);
    }

    public Vector procurarLista(TipoEvento tipoEvento, HashMap<String, String> operadores) throws SQLException, ClassNotFoundException {
        return conexaoTiposEventosBD.procurarLista(tipoEvento, operadores);
    }

    public Vector procurarLista(TipoEvento tipoEvento) throws SQLException, ClassNotFoundException {
        return conexaoTiposEventosBD.procurarLista(tipoEvento);
    }

    public int getQuantidade() throws SQLException, ClassNotFoundException {
        return conexaoTiposEventosBD.getQuantidade();
    }
}
