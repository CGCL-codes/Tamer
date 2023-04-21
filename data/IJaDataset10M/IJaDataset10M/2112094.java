package devolverVideo;

import org.junit.Assert;
import Primeiro.Video;
import Primeiro.Cliente;
import Primeiro.Locadora;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Antonio Jr
 */
public class DevolverVideo {

    Locadora locadora;

    Locadora locadoraTeste;

    Cliente locador1;

    Cliente locador2;

    Video origem;

    Video senhorDosAneis;

    Video hulk;

    public DevolverVideo() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        locadora = Locadora.getInstancia();
        locador1 = new Cliente(locadora, "Antonio");
        locador2 = new Cliente(locadora, "Wagner");
        origem = new Video(123, "A origem", 2.3f);
        senhorDosAneis = new Video(124, "Senhor dos Aneis", 2.0f);
        hulk = new Video(125, "Hulk", 4.0f);
        locadora.adicionarVideo(hulk);
        locadora.adicionarVideo(origem);
        locadora.adicionarVideo(senhorDosAneis);
    }

    @Test
    public void testeConsultarVideo() {
        Assert.assertEquals("A origem", locadora.consultarFilme(123).getTitulo());
    }

    @Test
    public void testeDevolverVideoNaoLocado() {
        assertEquals(-1, locadora.devolverVideo(senhorDosAneis.getCodigo(), locador1, true));
    }

    @Test
    public void testeDevolucaoVideoComPagamentoDeTaxa() {
        assertEquals(true, locadora.locarVideo(locador1, origem.getCodigo()));
        assertEquals(3, locadora.devolverVideo(origem.getCodigo(), locador1, true));
    }

    @Test
    public void testeDevolucaoVideoSemPagamentoDeTaxa() {
        assertEquals(true, locadora.locarVideo(locador2, hulk.getCodigo()));
        assertEquals(2, locadora.devolverVideo(hulk.getCodigo(), locador1, false));
    }

    @Test
    public void testePagamentoDeTaxaSeparado() {
        testeDevolucaoVideoSemPagamentoDeTaxa();
        assertEquals(true, locadora.pagarTaxa(null, locador1, hulk.getTitulo()));
    }

    @After
    public void tearDown() {
    }
}
