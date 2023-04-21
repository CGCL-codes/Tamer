package projeto.lp2.grupo6.testes;

import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import projeto.lp2.grupo6.logica.Estabelecimento;
import projeto.lp2.grupo6.logica.SistemaRecomendacaoFacade;
import projeto.lp2.grupo6.logica.TipoDeAlmocoServido;
import projeto.lp2.grupo6.logica.Usuario;

/**
 * Aluno: 
 *      Guilherme Monteiro
 *      Italo Guedes
 *      Tiago Leite
 * 
 * @author guilhermemg
 * @author tiagoln
 * @author italogas
 *
 */
public class SistemaRecomendacaoFacadeTest {

    SistemaRecomendacaoFacade sis;

    String caminhoArquivoUsuario = "caminhoDeArquivoDeUsuarioTest";

    String caminhoArquivoEstabececimentos = "caminhoDeArquivoDeEstabelecimentosTest";

    String filtros = "ALaCarte;PratoFeito;SelfService";

    Estabelecimento e1;

    Estabelecimento e2;

    Estabelecimento e3;

    Usuario u1;

    Usuario u2;

    Usuario u3;

    Usuario u4;

    List<Usuario> listaUsuarios = new ArrayList<Usuario>();

    List<Estabelecimento> listaEstabelecimentos = new ArrayList<Estabelecimento>();

    @Before
    public void criaObjetos() {
        try {
            sis = new SistemaRecomendacaoFacade("src/Arquivos/opinioes-dos-usuarios-v2.data", "src/Arquivos/lista_estabelecimentos_projeto_lp2-v2.data");
            e1 = new Estabelecimento("nomeEstab1", "rua 1", TipoDeAlmocoServido.A_LA_CARTE);
            e2 = new Estabelecimento("nomeEstab2", "rua 2", TipoDeAlmocoServido.SELF_SERVICE);
            e3 = new Estabelecimento("nomeEstab3", "rua 3", TipoDeAlmocoServido.PRATO_FEITO);
            u1 = new Usuario("NomeDoUsuario1");
            u2 = new Usuario("NomeDoUsuario2");
            u3 = new Usuario("NomeDoUsuario3");
            u4 = new Usuario("NomeDoUsuario4");
            u1.adicionaOpiniao(e1, 2);
            u1.adicionaOpiniao(e2, -2);
            u1.adicionaOpiniao(e3, 4);
            u2.adicionaOpiniao(e1, 0);
            u2.adicionaOpiniao(e2, 0);
            u2.adicionaOpiniao(e3, 0);
            u3.adicionaOpiniao(e1, 0);
            u3.adicionaOpiniao(e2, -3);
            u3.adicionaOpiniao(e3, 5);
            u4.adicionaOpiniao(e1, 0);
            u4.adicionaOpiniao(e2, -3);
            u4.adicionaOpiniao(e3, 5);
            listaUsuarios.add(u1);
            listaUsuarios.add(u2);
            listaUsuarios.add(u3);
            listaUsuarios.add(u4);
        } catch (Exception ex) {
            Assert.fail("Nao esperava erro na criacao de objetos: " + ex.getMessage());
        }
    }

    @Test
    public void testaConstrutor() {
        try {
            sis = new SistemaRecomendacaoFacade(null, caminhoArquivoEstabececimentos);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em caminho de arquivo de usuarios nulo", ex.getMessage());
        }
        try {
            sis = new SistemaRecomendacaoFacade(caminhoArquivoUsuario, null);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em caminho de arquivo de estabelecimentos nulo", ex.getMessage());
        }
    }

    @Test
    public void testaCriaUsuario() {
        try {
            sis.criaUsuario("NomeDoUsuario");
        } catch (Exception ex) {
            Assert.fail("Nao esperava erro na criacao de usuario: " + ex.getMessage());
        }
    }

    @Test
    public void testaCriaOpiniao() {
        try {
            sis.criaOpiniao(e1, -1);
        } catch (Exception ex) {
            Assert.fail("Nao esperava erro na criacao de opiniao: " + ex.getMessage());
        }
        try {
            sis.criaOpiniao(e1, 6);
        } catch (Exception ex) {
            Assert.assertEquals("Valor invalido para nota", ex.getMessage());
        }
        try {
            sis.criaOpiniao(e1, -6);
        } catch (Exception ex) {
            Assert.assertEquals("Valor invalido para nota", ex.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void testaAdicionaUsuarioNaLista() throws Exception {
        sis.adicionaUsuarioNaLista(null);
    }

    @Test
    public void testaGetUsuarios() {
        try {
            SistemaRecomendacaoFacade.getUsuarios();
        } catch (Exception ex) {
            Assert.fail("Nao esperava erro em getUsuarios: " + ex.getMessage());
        }
    }

    @Test
    public void testaGetEstabelecimentos() {
        try {
            SistemaRecomendacaoFacade.getEstabelecimentos();
        } catch (Exception ex) {
            Assert.fail("Nao esperava erro em getEstabelecimentos: " + ex.getMessage());
        }
    }

    @Test(expected = Exception.class)
    public void testaSalvarUsuario() throws Exception {
        sis.salvarUsuario(null);
    }

    @Test
    public void testaComparaAlgoritmos() {
        ArrayList<Integer> resultadosComparaAlgoritmos = new ArrayList<Integer>();
        resultadosComparaAlgoritmos.add(4);
        resultadosComparaAlgoritmos.add(3);
        try {
            sis.comparaAlgoritmos(3, listaUsuarios);
        } catch (Exception ex) {
            Assert.fail("Nao esperava erro em comparaAlgoritmo: " + ex.getMessage());
        }
        try {
            sis.comparaAlgoritmos(-1, listaUsuarios);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em quantidade de itens negativa", ex.getMessage());
        }
        try {
            Assert.assertEquals(sis.comparaAlgoritmos(3, listaUsuarios), resultadosComparaAlgoritmos);
        } catch (Exception ex) {
            Assert.fail("Erro na comparacao de comparaAlgoritmos com resultado esperado: " + ex.getMessage());
        }
    }

    @Test
    public void testaGeraRecomendacao1() {
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, -1, filtros);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em quantidade de itens negativa", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, 0, filtros);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em quantidade de itens nula", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, 2, null);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em filtros nulo", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, 2, "");
        } catch (Exception ex) {
            Assert.assertEquals("Erro em filtros vazio", ex.getMessage());
        }
    }

    @Test
    public void testaGeraRecomendacao2() {
        try {
            Usuario u1 = null;
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, u1, 0, filtros);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em usuario nulo", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, new Usuario("nome1"), 0, filtros);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em quantidade de itens nula", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, new Usuario("Nome2"), -2, null);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em filtros nulo", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, new Usuario("nome1"), 2, null);
        } catch (Exception ex) {
            Assert.assertEquals("Erro em filtros nulo", ex.getMessage());
        }
        try {
            SistemaRecomendacaoFacade.geraRecomendacao(listaUsuarios, new Usuario("nome1"), 2, "");
        } catch (Exception ex) {
            Assert.assertEquals("Erro em filtros vazio", ex.getMessage());
        }
    }

    @Test
    public void testaGeraEstatistica() throws Exception {
        Estabelecimento e1 = new Estabelecimento("nome estab 1", "rua 1", TipoDeAlmocoServido.PRATO_FEITO);
        Estabelecimento e2 = new Estabelecimento("nome2", "rua 2", TipoDeAlmocoServido.SELF_SERVICE);
        List<Usuario> listaUsuariosP = new ArrayList<Usuario>();
        Usuario u1 = new Usuario("nome1");
        Usuario u2 = new Usuario("nome2");
        u1.adicionaOpiniao(e1, 2);
        u1.adicionaOpiniao(e2, -2);
        u2.adicionaOpiniao(e1, 0);
        u2.adicionaOpiniao(e2, 0);
        listaUsuariosP.add(u1);
        listaUsuariosP.add(u2);
        try {
            SistemaRecomendacaoFacade.geraEstatistica(null, null);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Erro em estabelecimento nulo");
        }
        try {
            SistemaRecomendacaoFacade.geraEstatistica(e1, null);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Erro em lista de usuarios nula");
        }
        try {
            SistemaRecomendacaoFacade.geraEstatistica(e1, new ArrayList<Usuario>());
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Erro em lista de usuarios vazia");
        }
        try {
            SistemaRecomendacaoFacade.geraEstatistica(e1, listaUsuariosP);
            List<Integer> listaEstatisticas = new ArrayList<Integer>();
            listaEstatisticas.add(1);
            listaEstatisticas.add(0);
            listaEstatisticas.add(1);
            Assert.assertEquals(SistemaRecomendacaoFacade.geraEstatistica(e1, listaUsuariosP), listaEstatisticas);
        } catch (Exception e) {
            Assert.fail("Nao esperava erro em geraEstatistica() 1 feito corretamente");
        }
        try {
            SistemaRecomendacaoFacade.geraEstatistica(e1, listaUsuariosP);
            List<Integer> listaEstatisticas = new ArrayList<Integer>();
            listaEstatisticas.add(0);
            listaEstatisticas.add(1);
            listaEstatisticas.add(1);
            Assert.assertEquals(SistemaRecomendacaoFacade.geraEstatistica(e2, listaUsuariosP), listaEstatisticas);
        } catch (Exception e) {
            Assert.fail("Nao esperava erro em geraEstatistica() 2 feito corretamente");
        }
    }
}
