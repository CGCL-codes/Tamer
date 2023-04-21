package org.alfredlibrary.test.utilitarios.fisica.cinematica.movimentocircularuniforme;

import junit.framework.Assert;
import org.alfredlibrary.AlfredException;
import org.alfredlibrary.utilitarios.fisica.cinematica.movimentocircularuniforme.Periodo;
import org.junit.Test;

/**
 * Classe de Teste para o Utilitário Periodo.
 * 
 * @author Rodrigo Moreira Fagundes
 * @since 27/05/2010
 */
public class PeriodoTest {

    @Test
    public void calcular() {
        Assert.assertEquals(2d, Periodo.calcular(2 * Math.PI, 2d));
        Assert.assertEquals(2d, Periodo.calcularPorFrequencia(0.5d));
        Assert.assertEquals(2d, Periodo.calcularPorVelocidadeAngular(Math.PI));
    }

    @Test
    public void calcularDivisaoPorZeroVelocidade() {
        try {
            Periodo.calcular(0d, 2d);
            Assert.fail();
        } catch (AlfredException ae) {
        }
    }

    @Test
    public void calcularDivisaoPorZeroFrequencia() {
        try {
            Periodo.calcularPorFrequencia(0d);
            Assert.fail();
        } catch (AlfredException ae) {
        }
    }

    @Test
    public void calcularDivisaoPorZeroVelocidadeAngular() {
        try {
            Periodo.calcularPorVelocidadeAngular(0d);
            Assert.fail();
        } catch (AlfredException ae) {
        }
    }
}
