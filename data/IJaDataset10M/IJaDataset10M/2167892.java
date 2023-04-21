package der.ponto;

import org.junit.Test;

/**
 *
 * @author Marcius
 */
public class StatusValidadoTest {

    /**
     * Test of autorizar method, of class StatusValidado.
     */
    @Test(expected = StatusException.class)
    public void testAutorizar() throws Exception {
        StatusValidado instance = new StatusValidado(new Ocorrencia());
        instance.autorizar();
    }

    /**
     * Test of validar method, of class StatusValidado.
     */
    @Test(expected = StatusException.class)
    public void testValidar() throws Exception {
        StatusValidado instance = new StatusValidado(new Ocorrencia());
        instance.validar();
    }

    /**
     * Test of cancelar method, of class StatusValidado.
     */
    @Test(expected = StatusException.class)
    public void testCancelar() throws Exception {
        StatusValidado instance = new StatusValidado(new Ocorrencia());
        instance.cancelar();
    }

    /**
     * Test of voltar method, of class StatusValidado.
     */
    @Test(expected = StatusException.class)
    public void testVoltar() throws Exception {
        StatusValidado instance = new StatusValidado(new Ocorrencia());
        instance.voltar();
    }
}
