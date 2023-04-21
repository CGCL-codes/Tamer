package org.alfredlibrary.test.utilitarios.tributo.IR;

import java.io.File;
import junit.framework.Assert;
import org.alfredlibrary.formatadores.Data;
import org.alfredlibrary.utilitarios.tributo.IR.IRRF;
import org.junit.Test;

/**
 * Classe de Teste para o utilitário de IR.
 * 
 * @author Rodrigo Moreira Fagundes
 * @since 01/10/2010
 */
public class IRRFTest {

    private static final File ARQUIVO = new File("src" + System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "org" + System.getProperty("file.separator") + "alfredlibrary" + System.getProperty("file.separator") + "utilitarios" + System.getProperty("file.separator") + "tributos" + System.getProperty("file.separator") + "IRRF.xml");

    @Test
    public void testIRRF() {
        Assert.assertEquals(712.05325D, IRRF.calcular(5000D, IRRF.obterAliquotas(ARQUIVO, Data.formatar("01/10/2009", "dd/MM/yyyy"), null), true));
    }
}
