package br.unb.cic.gerval.server.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import br.unb.cic.gerval.client.rpc.vo.Linha;
import br.unb.cic.gerval.client.rpc.vo.Perfil;
import br.unb.cic.gerval.client.rpc.vo.Produto;
import br.unb.cic.gerval.client.rpc.vo.ProdutoTeste;
import br.unb.cic.gerval.client.rpc.vo.Teste;
import br.unb.cic.gerval.client.rpc.vo.Usuario;

public class TesteTest extends AbstractTransactionalSpringContextTests {

    /**
	 *  Atenção, nao criar os getters e setters dos DAOs
	 */
    protected DAO produtoDAO;

    protected DAO usuarioDAO;

    protected DAO linhaDAO;

    protected DAO perfilDAO;

    protected DAO testeDAO;

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        produtoDAO = (DAO) applicationContext.getBean("produtoDAO");
        usuarioDAO = (DAO) applicationContext.getBean("usuarioDAO");
        linhaDAO = (DAO) applicationContext.getBean("linhaDAO");
        perfilDAO = (DAO) applicationContext.getBean("perfilDAO");
        testeDAO = (DAO) applicationContext.getBean("testeDAO");
    }

    protected String[] getConfigLocations() {
        return new String[] { "applicationContext.xml", "testes.xml" };
    }

    public void test() {
        assertNotNull(testeDAO);
        int num = testeDAO.getAll().size();
        Teste t = criarTeste();
        testeDAO.saveOrUpdate(t);
    }

    private Teste criarTeste() {
        Teste t = new Teste();
        t.setEstado(Teste.TESTE_SOLICITADO);
        t.setPrazo(new Integer(4));
        t.setDataFimTeste(new Date());
        t.setDataInicioTeste(new Date());
        t.setDataSolicitacao(new Date());
        t.setOpSMP("1211");
        t.setSolicitador(criarResponsavel());
        t.setProdutos(criarProdutos(t));
        return t;
    }

    private Set criarProdutos(Teste t) {
        Set produtos = new HashSet();
        Produto p = criarProduto();
        ProdutoTeste pt = new ProdutoTeste(p, t);
        produtos.add(pt);
        return produtos;
    }

    protected Produto criarProduto() {
        Produto p = new Produto();
        p.setNome("nome");
        p.setCodigo("codigo");
        Linha linha = criarLinha();
        p.setLinha(linha);
        Usuario responsavel = criarResponsavel();
        p.setResponsavel(responsavel);
        produtoDAO.saveOrUpdate(p);
        return p;
    }

    protected Usuario criarResponsavel() {
        Usuario u = new Usuario();
        u.setEmail("email");
        u.setLogin("login");
        u.setNome("nome");
        Collection perfis = new ArrayList();
        Perfil p = (Perfil) perfilDAO.getAll().iterator().next();
        perfis.add(p);
        u.setPerfis(new HashSet(perfis));
        u.setSenha("3");
        u.setId(usuarioDAO.saveOrUpdate(u));
        return u;
    }

    protected Linha criarLinha() {
        Linha l = new Linha();
        l.setNome("nome");
        l.setId(linhaDAO.saveOrUpdate(l));
        return l;
    }
}
