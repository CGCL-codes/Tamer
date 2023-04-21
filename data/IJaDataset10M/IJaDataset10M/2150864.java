package br.com.sms.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import br.com.sms.conexao.Conexao;
import br.com.sms.dominio.Cidade;
import br.com.sms.dominio.Cliente;
import br.com.sms.enuns.Situacao;
import br.com.sms.util.Util;

public class ClienteDAO {

    private EntityManager manager;

    private Conexao conexao;

    public ClienteDAO() {
        if (conexao == null) conexao = new Conexao();
    }

    public Cliente salvar(Cliente cliente) {
        CidadeDAO cidadeDAO = new CidadeDAO();
        Cidade cidadeRecuperada = cidadeDAO.recuperarCidade(cliente);
        if (cidadeRecuperada == null) {
            if (cliente.getCidade() != null && cliente.getCidade().getUf() != null) {
                Cidade cidade = new Cidade(cliente.getCidade().getNome(), cliente.getCidade().getUf());
                cidade = cidadeDAO.salvar(cidade);
                cliente.setCidade(cidade);
            }
        } else {
            cliente.setCidade(cidadeRecuperada);
        }
        manager = conexao.getEntityManger();
        manager.getTransaction().begin();
        if (cliente.getId() > 0) {
            manager.merge(cliente);
        } else {
            manager.persist(cliente);
        }
        manager.getTransaction().commit();
        conexao.finalizaConexao();
        return cliente;
    }

    @SuppressWarnings("unchecked")
    public List<Cliente> pesquisar(Cliente cliente) {
        manager = conexao.getEntityManger();
        String sql = montarSql(cliente);
        Query query = manager.createQuery(sql);
        preencherClausula(query, cliente);
        List<Cliente> lista = query.getResultList();
        conexao.finalizaConexao();
        return lista;
    }

    /**
	 * Realiza a exclusão lógica de Cliente, setando o campo situação para INATIVO
	 * @param cliente
	 */
    public void excluir(Cliente cliente) {
        manager = conexao.getEntityManger();
        cliente = manager.find(Cliente.class, cliente.getId());
        cliente.setSituacao(Situacao.INATIVO);
        manager.getTransaction().begin();
        manager.merge(cliente);
        manager.getTransaction().commit();
        conexao.finalizaConexao();
    }

    public static String montarSql(Cliente cliente) {
        StringBuilder sql = new StringBuilder();
        boolean possuiWhere = false;
        sql.append("select c from Cliente c ");
        if (Util.isPreenchido(cliente.getNome())) {
            sql.append("where c.nome like :nome ");
            possuiWhere = true;
        }
        if (cliente.getCidade() != null && cliente.getCidade().getId() > 0) {
            if (possuiWhere) {
                sql.append("and c.cidade.id = :cidade ");
            } else {
                sql.append("where c.cidade.id = :cidade ");
                possuiWhere = true;
            }
        }
        if (cliente.getCidade() != null && cliente.getCidade().getUf() != null) {
            if (possuiWhere) {
                sql.append("and c.cidade.uf = :uf ");
            } else {
                sql.append("where c.cidade.uf = :uf ");
                possuiWhere = true;
            }
        }
        if (Util.isPreenchido(cliente.getEmail())) {
            if (possuiWhere) sql.append("and c.email like :email "); else sql.append("where c.email like :email ");
        }
        if (Util.isPreenchido(cliente.getCnpj())) {
            if (possuiWhere) sql.append("and c.cnpj like :cnpj "); else sql.append("where c.cnpj like :cnpj ");
        }
        return sql.toString();
    }

    public static void preencherClausula(Query query, Cliente cliente) {
        if (cliente.getCidade() != null && cliente.getCidade().getId() > 0) {
            query.setParameter("cidade", cliente.getCidade().getId());
        }
        if (cliente.getCidade() != null && cliente.getCidade().getUf() != null) {
            query.setParameter("uf", cliente.getCidade().getUf());
        }
        if (Util.isPreenchido(cliente.getNome())) {
            query.setParameter("nome", "%" + cliente.getNome().toUpperCase() + "%");
        }
        if (Util.isPreenchido(cliente.getEmail())) {
            query.setParameter("email", "%" + cliente.getEmail() + "%");
        }
        if (Util.isPreenchido(cliente.getCnpj())) {
            query.setParameter("cnpj", "%" + cliente.getCnpj() + "%");
        }
    }

    public Cliente recuperarCliente(int id) {
        manager = conexao.getEntityManger();
        Cliente cliente = manager.find(Cliente.class, id);
        conexao.finalizaConexao();
        return cliente;
    }

    @SuppressWarnings("unchecked")
    public List<Cliente> recuperarTodos() {
        manager = conexao.getEntityManger();
        Query query = manager.createQuery("select c from Cliente c");
        List<Cliente> listaClientes = query.getResultList();
        conexao.finalizaConexao();
        return listaClientes;
    }
}
