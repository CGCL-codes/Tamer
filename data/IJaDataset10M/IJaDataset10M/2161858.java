package business;

import business.EmpregadoBO;
import business.EmpregadoException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmpregadoBOTestSexo {

    public EmpregadoBOTestSexo() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Testes do método que verifica se o sexo do empregado é masculino ou feminino.
     * 
     * @author Zeno
     * 
     */
    @Test
    public void testVerificaSexoMasculino() throws EmpregadoException {
        String sexo = "Masculino";
        sexoValido(sexo);
    }

    @Test
    public void testVerificaSexoFeminino() throws EmpregadoException {
        String sexo = "Feminino";
        sexoValido(sexo);
    }

    @Test(expected = EmpregadoException.class)
    public void testVerificaSexoAssexuado() throws EmpregadoException {
        String sexo = "Assexuado";
        sexoInvalido(sexo);
    }

    private void sexoValido(String sexo) throws EmpregadoException {
        boolean valido = EmpregadoBO.getInstance().validaSexo(sexo);
        assertEquals(true, valido);
    }

    private void sexoInvalido(String sexo) throws EmpregadoException {
        boolean invalido = EmpregadoBO.getInstance().validaSexo(sexo);
    }
}
