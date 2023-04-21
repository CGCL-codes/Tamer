package br.unb.cic.gerval.server.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import br.unb.cic.gerval.client.NegocioException;
import br.unb.cic.gerval.client.rpc.vo.Linha;
import br.unb.cic.gerval.client.rpc.vo.PlanoTeste;
import br.unb.cic.gerval.client.rpc.vo.Problema;
import br.unb.cic.gerval.client.rpc.vo.Produto;
import br.unb.cic.gerval.client.rpc.vo.ProdutoTeste;
import br.unb.cic.gerval.client.rpc.vo.Teste;
import br.unb.cic.gerval.client.rpc.vo.TesteRealizado;
import br.unb.cic.gerval.client.rpc.vo.Usuario;

public class TesteBOTest extends AbstractTransactionalSpringContextTests {

    private TesteBO testeBO;

    private SimplesBO usuarioBO;

    private SimplesBO produtoBO;

    private SimplesBO linhaBO;

    protected void onSetUpBeforeTransaction() throws Exception {
        this.testeBO = (TesteBO) applicationContext.getBean("testeBO");
        this.usuarioBO = (SimplesBO) applicationContext.getBean("usuarioBO");
        this.produtoBO = (SimplesBO) applicationContext.getBean("produtoBO");
        this.linhaBO = (SimplesBO) applicationContext.getBean("linhaBO");
    }

    protected String[] getConfigLocations() {
        return new String[] { "applicationContext.xml", "testes.xml" };
    }

    public void test() throws NegocioException {
        Teste teste = new Teste();
        teste.setEstado(Teste.TESTE_SOLICITADO);
        teste.setDataSolicitacao(new Date());
        teste.setOpSMP("1212");
        teste.setPrazo(new Integer(5));
        Usuario lider = (Usuario) applicationContext.getBean("lider");
        Usuario adm = (Usuario) applicationContext.getBean("adm");
        boolean excecao = false;
        try {
            usuarioBO.saveOrUpdate(lider, null);
        } catch (NegocioException e1) {
            excecao = true;
        }
        assertFalse(excecao);
        teste.setSolicitador(adm);
        Produto p = (Produto) applicationContext.getBean("produto");
        Linha l = p.getLinha();
        linhaBO.saveOrUpdate(l, adm);
        Usuario responsavel = p.getResponsavel();
        usuarioBO.saveOrUpdate(responsavel, adm);
        produtoBO.saveOrUpdate(p, adm);
        Set produtos = new HashSet();
        ProdutoTeste produtoTeste = new ProdutoTeste(p, teste);
        produtoTeste.setManual("manual");
        produtoTeste.setNumeroSerie("123");
        produtoTeste.setVersaoSoftware("1.0");
        produtos.add(produtoTeste);
        Set planosTeste = new HashSet();
        planosTeste.add(new PlanoTeste(teste, "opa"));
        teste.setPlanosTestes(planosTeste);
        teste.setProdutos(produtos);
        testeBO.solicitarTeste(teste);
        assertTrue(teste.getId().intValue() >= 0);
        assertTrue(teste.getEstado().equals(Teste.TESTE_SOLICITADO));
        Teste t2 = (Teste) testeBO.get(teste.getId());
        verificaTeste(teste, produtoTeste, t2);
        Usuario tecnico = (Usuario) applicationContext.getBean("tecnico");
        usuarioBO.saveOrUpdate(tecnico, adm);
        teste.setTecnicoAlocado(tecnico);
        boolean lancouExcecao = false;
        try {
            testeBO.alocarTecnico(teste, tecnico);
        } catch (NegocioException e) {
            lancouExcecao = true;
        }
        assertTrue(lancouExcecao);
        try {
            lancouExcecao = false;
            testeBO.alocarTecnico(teste, adm);
        } catch (NegocioException e) {
            lancouExcecao = true;
        }
        assertFalse(lancouExcecao);
        assertTrue(teste.getEstado().equals(Teste.TESTE_ALOCADO_A_TECNICO));
        assertEquals(teste.getTecnicoAlocado().getId(), tecnico.getId());
        Teste t3 = (Teste) testeBO.get(teste.getId());
        verificaTeste(teste, produtoTeste, t3);
        assertEquals(teste.getTecnicoAlocado().getLogin(), t3.getTecnicoAlocado().getLogin());
        Set problemas = new HashSet();
        problemas.add(new Problema(teste, "prob1"));
        teste.setProblemas(problemas);
        testeBO.salvarRVP(teste, tecnico);
        testeBO.salvarRVP(teste, tecnico);
        Teste t4 = (Teste) testeBO.get(teste.getId());
        verificaTeste(teste, produtoTeste, t4);
        assertEquals(teste.getTecnicoAlocado().getLogin(), t4.getTecnicoAlocado().getLogin());
        lancouExcecao = false;
        try {
            testeBO.finalizarRVP(teste, tecnico);
        } catch (NegocioException e) {
            lancouExcecao = true;
        }
        assertTrue(lancouExcecao);
    }

    public void testSolicitarSalvar() throws NegocioException {
        Teste teste = new Teste();
        teste.setEstado(Teste.TESTE_ALOCADO_A_TECNICO);
        teste.setDataSolicitacao(new Date());
        teste.setOpSMP("1212");
        teste.setPrazo(new Integer(5));
        Usuario lider = (Usuario) applicationContext.getBean("lider");
        usuarioBO.saveOrUpdate(lider, null);
        teste.setSolicitador(lider);
        Produto p = (Produto) applicationContext.getBean("produto");
        Linha l = p.getLinha();
        linhaBO.saveOrUpdate(l, null);
        Usuario responsavel = p.getResponsavel();
        usuarioBO.saveOrUpdate(responsavel, null);
        produtoBO.saveOrUpdate(p, null);
        Set produtos = new HashSet();
        ProdutoTeste produtoTeste = new ProdutoTeste(p, teste);
        produtoTeste.setManual("manual");
        produtoTeste.setNumeroSerie("123");
        produtoTeste.setVersaoSoftware("1.0");
        produtos.add(produtoTeste);
        teste.setProdutos(produtos);
        testeBO.solicitarTeste(teste);
        assertTrue(teste.getId().intValue() >= 0);
        Integer id = teste.getId();
        Usuario tecnico = (Usuario) applicationContext.getBean("tecnico");
        usuarioBO.saveOrUpdate(tecnico, null);
        testeBO.alocarTecnico(teste, tecnico);
        Teste t3 = (Teste) testeBO.get(teste.getId());
        verificaTeste(teste, produtoTeste, t3);
        assertEquals(teste.getTecnicoAlocado(), t3.getTecnicoAlocado());
        Teste teste2 = (Teste) testeBO.get(id);
        HashSet trs = new HashSet();
        trs.add(new TesteRealizado(teste2, "blablabla"));
        trs.add(new TesteRealizado(teste2, "blebleble"));
        teste2.setTestesRealizados(trs);
        testeBO.salvarRVP(teste, (Usuario) usuarioBO.get(new Integer(1)));
        teste2 = (Teste) testeBO.get(id);
        assertEquals(2, teste2.getTestesRealizados().size());
        teste2 = (Teste) testeBO.get(id);
        trs = new HashSet();
        trs.add(new TesteRealizado(teste2, "bliblibli"));
        trs.add(new TesteRealizado(teste2, "blobloblo"));
        teste2.setTestesRealizados(trs);
        testeBO.salvarRVP(teste, (Usuario) usuarioBO.get(new Integer(1)));
        teste2 = (Teste) testeBO.get(id);
        assertEquals(2, teste2.getTestesRealizados().size());
    }

    private void verificaTeste(Teste teste, ProdutoTeste produtoTeste, Teste t2) {
        assertEquals(teste, t2);
        assertEquals(teste.getProdutos(), t2.getProdutos());
        ProdutoTeste ptDoBanco = (ProdutoTeste) t2.getProdutos().iterator().next();
        assertEquals(ptDoBanco, produtoTeste);
        assertEquals(ptDoBanco.getManual(), produtoTeste.getManual());
        assertEquals(ptDoBanco.getNumeroSerie(), produtoTeste.getNumeroSerie());
        assertEquals(ptDoBanco.getProduto(), produtoTeste.getProduto());
        assertEquals(ptDoBanco.getTeste(), produtoTeste.getTeste());
        assertEquals(ptDoBanco.getVersaoSoftware(), produtoTeste.getVersaoSoftware());
        if (teste.getProblemas() != null) {
            assertEquals(teste.getProblemas().size(), t2.getProblemas().size());
        }
        assertEquals(teste.getPlanosTestes().size(), t2.getPlanosTestes().size());
    }
}
