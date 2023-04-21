package business;

import business.EmpregadoBO;
import business.EmpregadoException;
import java.math.RoundingMode;
import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EmpregadoBOTestSalario {

    public EmpregadoBOTestSalario() {
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
     * Testes do método que verifica se o valor do salário do empregado está entre 500.00 e 100000.00.
     * 
     * @author Zeno
     * 
     */
    @Test(expected = EmpregadoException.class)
    public void testVerificaSalarioNulo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    @Test(expected = EmpregadoException.class)
    public void testSalarioPrimeiroArgumentoComTamanhoMenorQueTamanhoMaximo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(499.99).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    @Test
    public void testSalarioPrimeiroArgumentoComTamanhoMaximo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(500.00).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    @Test
    public void testSalarioPrimeiroArgumentoComTamanhoMaiorQueTamanhoMaximo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(500.01).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    @Test
    public void testSalarioSegundoArgumentoComTamanhoMenorQueTamanhoMaximo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(99999.99).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    @Test
    public void testSalarioSegundoArgumentoComTamanhoMaximo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(100000.00).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    @Test(expected = EmpregadoException.class)
    public void testSalarioSegundoArgumentoComTamanhoMaiorQueTamanhoMaximo() throws EmpregadoException {
        BigDecimal salario = new BigDecimal(100000.01).setScale(2, RoundingMode.HALF_EVEN);
        salarioValido(salario);
    }

    private void salarioValido(BigDecimal salario) throws EmpregadoException {
        boolean valido = EmpregadoBO.getInstance().validaSalario(salario);
        assertEquals(true, valido);
    }

    private void salarioInvalido(BigDecimal salario) throws EmpregadoException {
        boolean invalido = EmpregadoBO.getInstance().validaSalario(salario);
    }
}
