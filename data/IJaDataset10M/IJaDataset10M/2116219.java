package pedido.repositorio;

import java.sql.Statement;
import java.sql.*;
import pedido.entidades.Pedido;
import excecoes.RepositorioException;
import util.PersistenceMechanismException;
import util.PersistenceMechanismRDBMS;

public class RepositorioPedidoBD implements RepositorioPedido {

    private PersistenceMechanismRDBMS conexao;

    private PreparedStatement atualizar;

    private PreparedStatement consultar;

    private PreparedStatement inserir;

    private PreparedStatement excluir;

    public RepositorioPedidoBD() {
        try {
            this.setConexao(PersistenceMechanismRDBMS.getInstance());
            this.getConexao().connect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PersistenceMechanismRDBMS getConexao() {
        return conexao;
    }

    public void setConexao(PersistenceMechanismRDBMS conexao) {
        this.conexao = conexao;
    }

    public PreparedStatement getAtualizar() {
        return atualizar;
    }

    public void setAtualizar(PreparedStatement atualizar) {
        this.atualizar = atualizar;
    }

    public PreparedStatement getConsultar() {
        return consultar;
    }

    public void setConsultar(PreparedStatement consultar) {
        this.consultar = consultar;
    }

    public PreparedStatement getInserir() {
        return inserir;
    }

    public void setInserir(PreparedStatement inserir) {
        this.inserir = inserir;
    }

    public PreparedStatement getExcluir() {
        return excluir;
    }

    public void setExcluir(PreparedStatement excluir) {
        this.excluir = excluir;
    }

    @Override
    public void atualizar(Pedido pedido) throws RepositorioException, PersistenceMechanismException {
    }

    @Override
    public boolean existe(Pedido pedido) throws RepositorioException {
        return false;
    }

    @Override
    public IteratorPedido getIterator() {
        return null;
    }

    @Override
    public void inserir(Pedido pedido) throws RepositorioException {
    }

    @Override
    public Pedido procurar(Pedido pedido) throws RepositorioException {
        return null;
    }

    @Override
    public void remover(Pedido pedido) throws RepositorioException {
    }
}
