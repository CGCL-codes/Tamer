package der.ponto;

import org.junit.Test;

/**
 *
 * @author Marcius
 */
public class StatusCanceladoTest {

    public StatusCanceladoTest() {
    }

    /**
     * Test of autorizar method, of class StatusCancelado.
     */
    @Test(expected = StatusException.class)
    public void testAutorizar() throws Exception {
        StatusCancelado instance = new StatusCancelado(new Ocorrencia());
        instance.autorizar();
    }

    /**
     * Test of validar method, of class StatusCancelado.
     */
    @Test(expected = StatusException.class)
    public void testValidar() throws Exception {
        StatusCancelado instance = new StatusCancelado(new Ocorrencia());
        instance.validar();
    }

    /**
     * Test of cancelar method, of class StatusCancelado.
     */
    @Test(expected = StatusException.class)
    public void testCancelar() throws Exception {
        StatusCancelado instance = new StatusCancelado(new Ocorrencia());
        instance.cancelar();
    }

    /**
     * Test of voltar method, of class StatusCancelado.
     */
    @Test(expected = StatusException.class)
    public void testVoltar() throws Exception {
        StatusCancelado instance = new StatusCancelado(new Ocorrencia());
        instance.voltar();
    }
}
