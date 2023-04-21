package Negócio;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author GABRIEL
 */
public class GerenciadorUsuarioTest {

    private Usuario usuarioTeste;

    private Usuario usuarioAtualizado;

    private Usuario osvaldo;

    private Usuario diego;

    public GerenciadorUsuarioTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        this.usuarioTeste = new Usuario("Joao Pé de Feijão", "Jonny", "1234", "joao@joao.com", new Date(12, 11, 1988));
        this.usuarioAtualizado = new Usuario("Joao Pé de Feijão", "Jonny", "4321", "joao@joao.com", new Date(12, 11, 1988));
        this.osvaldo = new Usuario("Osvaldo Matyak", "omatyak", "osvaldo", "osvaldo@osvaldo.com", new Date(10, 05, 1967));
        this.diego = new Usuario("Diego Gadens", "didi", "1234", "diego@diego.com", new Date(19, 04, 1980));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of adicionarUsuario method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarUsuario1() {
        System.out.println("adicionarUsuario1");
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Usuário criado com sucesso.";
        System.out.println(instance.numeroRegistros());
        String result = instance.adicionarUsuario(this.usuarioTeste);
        System.out.println(instance.numeroRegistros());
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarUsuario method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarUsuario2() {
        System.out.println("adicionarUsuario2");
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Já existe um usuário cadastrado com o E-mail digitado.";
        System.out.println(instance.numeroRegistros());
        String result = instance.adicionarUsuario(this.usuarioTeste);
        System.out.println(instance.numeroRegistros());
        assertEquals(expResult, result);
    }

    /**
     * Test of atualizarUsuario method, of class GerenciadorUsuario.
     */
    @Test
    public void testAtualizarUsuario() {
        System.out.println("atualizarUsuario");
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Dados atualizados com sucesso.";
        String result = instance.atualizarUsuario(this.usuarioAtualizado);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoNick1() {
        System.out.println("adicionarAmigoNick1");
        String nickUser = "Jonny";
        String nickAmigo = "Jonny";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Não é possível adicionar um amigo com o mesmo Nick que você.";
        String result = instance.adicionarAmigoNick(nickUser, nickAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoNick2() {
        System.out.println("adicionarAmigoNick2");
        String nickUser = "Jonny";
        String nickAmigo = "omatyak";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Nenhum usuário foi encontrado com esse Nick.";
        String result = instance.adicionarAmigoNick(nickUser, nickAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoNick3() {
        System.out.println("adicionarAmigoNick3");
        String nickUser = "Jonny";
        String nickAmigo = "omatyak";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        instance.adicionarUsuario(this.osvaldo);
        String expResult = nickAmigo + " foi adicionado com sucesso à sua Lista de Amigos.";
        String result = instance.adicionarAmigoNick(nickUser, nickAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoNick4() {
        System.out.println("adicionarAmigoNick4");
        String nickUser = "Jonny";
        String nickAmigo = "omatyak";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Você já adicionou o contato " + nickAmigo + " na sua Lista de Amigos.";
        String result = instance.adicionarAmigoNick(nickUser, nickAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoEmail1() {
        System.out.println("adicionarAmigoEmail1");
        String nickUsuario = "Jonny";
        String emailAmigo = "diego@diego.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Nenhum usuário foi encontrado com esse E-mail.";
        String result = instance.adicionarAmigoEmail(nickUsuario, emailAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoEmail2() {
        System.out.println("adicionarAmigoEmail2");
        String nickUsuario = "Jonny";
        String emailAmigo = "joao@joao.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Não é possível adicionar um amigo com o seu E-mail.";
        String result = instance.adicionarAmigoEmail(nickUsuario, emailAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoEmail3() {
        System.out.println("adicionarAmigoEmail3");
        String nickUsuario = "Jonny";
        String emailAmigo = "diego@diego.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        instance.adicionarUsuario(this.diego);
        String expResult = emailAmigo + " foi adicionado com sucesso à sua Lista de Amigos.";
        String result = instance.adicionarAmigoEmail(nickUsuario, emailAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of adicionarAmigoEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testAdicionarAmigoEmail4() {
        System.out.println("adicionarAmigoEmail3");
        String nickUsuario = "Jonny";
        String emailAmigo = "diego@diego.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Você já adicionou o contato " + emailAmigo + " na sua Lista de Amigos.";
        String result = instance.adicionarAmigoEmail(nickUsuario, emailAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of removerAmigoNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testRemoverAmigoNick1() {
        System.out.println("removerAmigoNick1");
        String nickUsuario = "Jonny";
        String nickAmigo = "omatyak";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = nickAmigo + " agora não se encontra mais na sua Lista de Amigos.";
        String result = instance.removerAmigoNick(nickUsuario, nickAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of removerAmigoNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testRemoverAmigoNick2() {
        System.out.println("removerAmigoNick2");
        String nickUsuario = "Jonny";
        String nickAmigo = "omatyak";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Não há usuário com o Nick " + nickAmigo + " na sua Lista de Amigos.";
        String result = instance.removerAmigoNick(nickUsuario, nickAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of removerAmigoEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testRemoverAmigoEmail1() {
        System.out.println("removerAmigoEmail1");
        String nickUsuario = "Jonny";
        String emailAmigo = "diego@diego.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = emailAmigo + " não se encontra mais na sua Lista de Amigos.";
        String result = instance.removerAmigoEmail(nickUsuario, emailAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of removerAmigoEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testRemoverAmigoEmail2() {
        System.out.println("removerAmigoEmail2");
        String nickUsuario = "Jonny";
        String emailAmigo = "diego@diego.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = "Não há usuário com o E-mail " + emailAmigo + " na sua Lista de Amigos.";
        String result = instance.removerAmigoEmail(nickUsuario, emailAmigo);
        assertEquals(expResult, result);
    }

    /**
     * Test of buscarUsuarioEmail method, of class GerenciadorUsuario.
     */
    @Test
    public void testBuscarUsuarioEmail() {
        System.out.println("buscarUsuarioEmail");
        String email = "osvaldo@osvaldo.com";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = this.osvaldo.getEmail();
        String result = instance.buscarUsuarioEmail(email).getEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of buscarUsuarioNick method, of class GerenciadorUsuario.
     */
    @Test
    public void testBuscarUsuarioNick() {
        System.out.println("buscarUsuarioNick");
        String nickName = "didi";
        GerenciadorUsuario instance = new GerenciadorUsuario();
        String expResult = this.diego.getNickname();
        String result = instance.buscarUsuarioNick(nickName).getNickname();
        assertEquals(expResult, result);
    }
}
