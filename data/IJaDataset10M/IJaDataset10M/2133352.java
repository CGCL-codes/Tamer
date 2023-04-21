package org.alfredlibrary.test.validadores;

import junit.framework.Assert;
import org.alfredlibrary.AlfredException;
import org.alfredlibrary.utilitarios.inscricaoestadual.InscricaoEstadual.PadraoInscricaoEstadual;
import org.alfredlibrary.validadores.InscricaoEstadual;
import org.junit.Test;

/**
 * Classe de Teste para o Validador de Inscrição Estadual.
 * 
 * @author Rodrigo Moreira Fagundes 
 * @since 15/06/2010
 */
public class InscricaoEstadualTest {

    @Test
    public void testarValidarInscricaoEstadualValida() {
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.ACRE, "01.40.7423-0")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.ALAGOAS, "240000048")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.AMAPA, "030123459")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.BAHIA, "123456-36")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.BAHIA, "623456-99")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.CEARA, "06000001-5")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.DISTRITO_FEDERAL, "073.00001.001-09")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.ESPIRITO_SANTO, "99999999-0")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.GOIAS, "10.987.654-7")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.MARANHAO, "12000038-5")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.MATO_GROSSO, "0013000001-9")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.PARA, "15-999999-5")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.PARAIBA, "16.004.017-5")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.PARANA, "123.45678-50")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.PERNAMBUCO, "18.1.001.0000004-9")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.PIAUI, "01234567-9")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.RIO_DE_JANEIRO, "99.999.99-3")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.RIO_GRANDE_DO_NORTE, "20.040.040-1")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.RIO_GRANDE_DO_SUL, "224/3658792")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.RONDONIA, "101002135")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.RORAIMA, "24006628-1")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.SANTA_CATARINA, "251.040.852")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.SAO_PAULO_INDUSTRIAIS_COMERCIANTES, "110.042.490.114")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.SAO_PAULO_PRODUTOR_RURAL, "P-01100424.3/002")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.SERGIPE, "27123456-3")) {
            Assert.fail();
        }
        if (!InscricaoEstadual.isValido(PadraoInscricaoEstadual.TOCANTINS, "29.01.022783-6")) {
            Assert.fail();
        }
    }

    @Test
    public void testarValidarInscricaoEstadualInvalida() {
        if (InscricaoEstadual.isValido(PadraoInscricaoEstadual.ACRE, "01.40.7423-1")) {
            Assert.fail();
        }
        try {
            if (InscricaoEstadual.isValido(PadraoInscricaoEstadual.ALAGOAS, "2400000")) {
                Assert.fail();
            }
        } catch (AlfredException ae) {
        }
        if (InscricaoEstadual.isValido(PadraoInscricaoEstadual.SAO_PAULO_PRODUTOR_RURAL, "-01100424.3/002")) {
            Assert.fail();
        }
    }
}
