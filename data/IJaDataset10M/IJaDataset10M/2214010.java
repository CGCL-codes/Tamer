package test;

import static org.junit.Assert.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import root.plumbum2.Publicacao;

public class PublicacaoTest {

    private Publicacao pub;

    @Before
    public void setUp() throws Exception {
        pub = new Publicacao("Bau.py", "Jogo super legal e divertido");
    }

    @Test
    public void testGetDescricaoItem() {
        Assert.assertEquals("Jogo super legal e divertido", pub.getDescricaoItem());
    }

    @Test
    public void testGetNomeItem() {
        Assert.assertEquals("Bau.py", pub.getNomeItem());
    }

    @Test
    public void testGetIdPublicacaoPedido() {
        Assert.assertNotNull(pub.getIdPublicacaoPedido());
    }

    @Test
    public void testSetNomeDonoPublicacao() {
        Assert.assertNull(pub.getNomeDonoPublicacao());
        pub.setNomeDonoPublicacao("Joao Frederico VIII");
        Assert.assertEquals("Joao Frederico VIII", pub.getNomeDonoPublicacao());
    }

    @Test
    public void testGetNomeDonoPublicacao() {
        pub.setNomeDonoPublicacao("Joao Frederico VIII");
        Assert.assertEquals("Joao Frederico VIII", pub.getNomeDonoPublicacao());
    }

    @Test
    public void testSetLoginDonoPublicacao() {
        Assert.assertNull(pub.getLoginDonoPublicacao());
        pub.setLoginDonoPublicacao("jofre8");
        Assert.assertEquals("jofre8", pub.getLoginDonoPublicacao());
    }

    @Test
    public void testGetLoginDonoPublicacao() {
        pub.setLoginDonoPublicacao("jofre8");
        Assert.assertEquals("jofre8", pub.getLoginDonoPublicacao());
    }
}
