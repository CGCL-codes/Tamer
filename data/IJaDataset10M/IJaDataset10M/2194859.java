package flm.fiado.dao.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import flm.fiado.bean.Conta;
import flm.fiado.bean.ItemConta;
import flm.fiado.exception.DaoException;
import flm.fiado.util.DbUtil;

public class ItemContaDAO {

    private static String LISTA_CAMPOS = "	ID_PESSOA, " + "	NUM_CONTA, " + "	ID_PRODUTO, " + "	QUANTIDADE, " + "	VL_ITEM ";

    private static final String QUERY_BUSCAR_TODOS = "select " + ItemContaDAO.LISTA_CAMPOS + "from " + "	TB_ITEM_CONTA " + "order by VL_ITEM ";

    private static final String QUERY_BUSCAR_POR_ID = "select " + ItemContaDAO.LISTA_CAMPOS + "from " + "	TB_ITEM_CONTA " + "where " + "	TB_ITEM_CONTA.ID_PESSOA = ? and " + "	TB_ITEM_CONTA.NUM_CONTA = ? and " + "	TB_ITEM_CONTA.ID_PRODUTO = ? " + "order by VL_ITEM ";

    private static final String QUERY_BUSCAR_POR_CONTA = "select " + ItemContaDAO.LISTA_CAMPOS + "from " + "	TB_ITEM_CONTA " + "where " + "	TB_ITEM_CONTA.ID_PESSOA = ? and " + "	TB_ITEM_CONTA.NUM_CONTA = ? " + "order by VL_ITEM ";

    private static final String QUERY_APAGAR_POR_ID = "delete " + "	from TB_ITEM_CONTA " + "where " + "	TB_ITEM_CONTA.ID_PESSOA = ? and " + "	TB_ITEM_CONTA.NUM_CONTA = ? and " + "	TB_ITEM_CONTA.ID_PRODUTO = ? ";

    private static final String QUERY_CRIAR_PRODUTO = "insert into TB_ITEM_CONTA (" + ItemContaDAO.LISTA_CAMPOS + ") " + "values(" + "	?, ?, ?, ?, ?)";

    private static final String QUERY_ATUALIZAR_PRODUTO = "update TB_ITEM_CONTA " + "set " + "	QUANTIDADE = ?, " + "	VL_ITEM = ? " + "where " + "	TB_ITEM_CONTA.ID_PESSOA = ? and " + "	TB_ITEM_CONTA.NUM_CONTA = ? and " + "	TB_ITEM_CONTA.ID_PRODUTO = ? ";

    /**
	 * Este metodo retorna uma lista com todos os objetos Conta armazenados
	 * @return
	 * @throws DaoException 
	 */
    public List<ItemConta> buscarTodos() throws DaoException {
        Connection conn = DbUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<ItemConta> listaTodos = new ArrayList<ItemConta>();
        try {
            statement = conn.prepareStatement(QUERY_BUSCAR_TODOS);
            result = statement.executeQuery();
            while (result.next()) {
                listaTodos.add(getBean(result));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtil.close(conn, statement, result);
        }
        return listaTodos;
    }

    private ItemConta getBean(ResultSet result) throws SQLException {
        ItemConta itemConta = new ItemConta();
        itemConta.setIdPessoa(result.getInt("ID_PESSOA"));
        itemConta.setNumConta(result.getInt("NUM_CONTA"));
        itemConta.setIdProduto(result.getInt("ID_PRODUTO"));
        itemConta.setQuantidade(result.getInt("QUANTIDADE"));
        itemConta.setValor(result.getDouble("VL_ITEM"));
        return itemConta;
    }

    /**
	 * Este metodo retorna o objeto Conta de acordo com os campos chaves
	 * @return
	 * @throws DaoException 
	 */
    public ItemConta buscarPorId(ItemConta itemConta) throws DaoException {
        return buscarPorId(itemConta.getIdPessoa(), itemConta.getNumConta(), itemConta.getIdProduto());
    }

    public ItemConta buscarPorId(int idPessoa, int numConta, int idProduto) throws DaoException {
        Connection conn = DbUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        ItemConta itemConta = null;
        try {
            statement = conn.prepareStatement(QUERY_BUSCAR_POR_ID);
            statement.setInt(1, idPessoa);
            statement.setInt(2, numConta);
            statement.setInt(3, idProduto);
            result = statement.executeQuery();
            if (result.next()) {
                itemConta = getBean(result);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtil.close(conn, statement, result);
        }
        return itemConta;
    }

    /**
	 * Este metodo apaga o objeto Conta de acordo com os campos chave
	 * @throws DaoException 
	 */
    public void apagar(int idPessoa, int numConta, int idProduto) throws DaoException {
        Connection conn = DbUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(QUERY_APAGAR_POR_ID);
            statement.setInt(1, idPessoa);
            statement.setInt(2, numConta);
            statement.setInt(3, idProduto);
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtil.close(conn, statement, result);
        }
    }

    /**
	 * Este metodo apaga o objeto Conta
	 * @throws DaoException 
	 */
    public void apagar(ItemConta itemConta) throws DaoException {
        apagar(itemConta.getIdPessoa(), itemConta.getNumConta(), itemConta.getIdProduto());
    }

    /**
	 * Este metodo salva o objeto Conta 
	 * @throws DaoException 
	 */
    public void salvar(ItemConta itemConta) throws DaoException {
        Connection conn = DbUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(QUERY_ATUALIZAR_PRODUTO);
            statement.setInt(1, itemConta.getQuantidade());
            statement.setDouble(2, itemConta.getValor());
            statement.setInt(3, itemConta.getIdPessoa());
            statement.setInt(4, itemConta.getNumConta());
            statement.setInt(5, itemConta.getIdProduto());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtil.close(conn, statement, result);
        }
    }

    /**
	 * Este metodo cria o objeto Conta e retorna o objeto criado com o novo numConta
	 * preenchido
	 * @throws DaoException 
	 */
    public void criar(ItemConta itemConta) throws DaoException {
        Connection conn = DbUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        try {
            statement = conn.prepareStatement(QUERY_CRIAR_PRODUTO);
            statement.setInt(1, itemConta.getIdPessoa());
            statement.setInt(2, itemConta.getNumConta());
            statement.setInt(3, itemConta.getIdProduto());
            statement.setInt(4, itemConta.getQuantidade());
            statement.setDouble(5, itemConta.getValor());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtil.close(conn, statement, result);
        }
    }

    public List<ItemConta> buscarPorConta(Conta conta) throws DaoException {
        Connection conn = DbUtil.getConnection();
        PreparedStatement statement = null;
        ResultSet result = null;
        List<ItemConta> listaTodos = new ArrayList<ItemConta>();
        try {
            statement = conn.prepareStatement(QUERY_BUSCAR_POR_CONTA);
            statement.setInt(1, conta.getIdPessoa());
            statement.setInt(2, conta.getNumConta());
            result = statement.executeQuery();
            while (result.next()) {
                listaTodos.add(getBean(result));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtil.close(conn, statement, result);
        }
        return listaTodos;
    }
}
