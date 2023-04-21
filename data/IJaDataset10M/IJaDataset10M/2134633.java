package org.alfredlibrary.test.utilitarios.fisica.cinematica.movimentoretilineouniformementevariado;

import junit.framework.Assert;
import org.alfredlibrary.utilitarios.fisica.cinematica.movimentoretilineouniformementevariado.EspacoInicial;
import org.junit.Test;

/**
 * Classe de Teste para o Utilitário EspacoInicial.
 * 
 * @author Rodrigo Moreira Fagundes
 * @since 27/05/2010
 */
public class EspacoInicialTest {

    @Test
    public void calcular() {
        Assert.assertEquals(1d, EspacoInicial.calcular(15d, 5d, 2d, 2d));
    }
}
