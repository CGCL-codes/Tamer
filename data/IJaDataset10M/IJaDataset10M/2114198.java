package br.uniriotec.pssgbd.gerencia.model.bo;

import java.util.ArrayList;
import java.util.List;
import br.uniriotec.pssgbd.gerencia.model.entidade.Cidade;
import br.uniriotec.pssgbd.gerencia.model.entidade.Cliente;

/**
 * Classe que simula (mock) a classe ClienteBO manipulando o cliente em mem�ria.
 * Esta classe poder� ser substitu�da sem causar grandes impactos na implementa��o da aplica��o.
 * Basta alterar a chamada ao BO nas classes servlet.
 * @author Flavio
 *
 */
public class ClienteBO extends BaseBO<Cliente> {

    private static ClienteBO instancia;

    private static List<Cliente> listaCliente = null;

    public static ClienteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteBO();
            listaCliente = new ArrayList<Cliente>();
        }
        return instancia;
    }

    public void incluir(Cliente cliente) throws Exception {
        listaCliente.add(cliente);
    }

    public List<Cidade> listarCidades() throws Exception {
        List<Cidade> listaCidades = new ArrayList<Cidade>();
        Cidade cidade = null;
        cidade = new Cidade();
        cidade.setCodCidade(1);
        cidade.setNome("Rio de Janeiro");
        listaCidades.add(cidade);
        cidade = new Cidade();
        cidade.setCodCidade(2);
        cidade.setNome("S�o Paulo");
        listaCidades.add(cidade);
        cidade = new Cidade();
        cidade.setCodCidade(3);
        cidade.setNome("Belo Horizonte");
        listaCidades.add(cidade);
        return listaCidades;
    }

    public Cliente consultar(Long id) throws Exception {
        return listaCliente.get(id.intValue());
    }

    public void alterar(Cliente cliente) throws Exception {
        listaCliente.remove(cliente);
        listaCliente.add(cliente);
    }

    @Override
    public void excluir(Cliente cliente) throws Exception {
        listaCliente.remove(cliente);
    }

    @Override
    public List<Cliente> listar() throws Exception {
        return listaCliente;
    }
}
