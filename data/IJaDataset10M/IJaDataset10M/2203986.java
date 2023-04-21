package br.com.lawoffice.caixa.extrato.jasper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import br.com.lawoffice.caixa.extrato.ExtratoDTO;

/**
 *
 * Teste de unidade para {@link ExtratoReportJasperPDF}.
 *
 * @author rduarte
 *
 */
public class ExtratoReportJasperPDFTest {

    private ExtratoReportJasperPDF extratoReportPDF;

    @Before
    public void setUp() throws Exception {
        extratoReportPDF = new ExtratoReportJasperPDF();
    }

    @After
    public void tearDown() throws Exception {
        extratoReportPDF = null;
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveDisparaUmaExcecaoQuandoExtratoDTONulo() {
        extratoReportPDF.gerarExtrato(null);
    }

    @Test()
    public void deveRetornaUmMapComParmentros() {
        Map<String, Object> parametros = extratoReportPDF.getMapParametros(new ExtratoDTO("Robson", new BigDecimal(111222.22), new BigDecimal(222300.22), new Date(), new Date()));
        assertNotNull(parametros);
        assertEquals("R$ 111.222,22", parametros.get("SALDO_ANTERIOR"));
        assertEquals("R$ 222.300,22", parametros.get("SALDO_ATUAL"));
        assertEquals(DateFormatUtils.format(new Date(), "dd/MM/yyyy"), parametros.get("DATA_INICIAL"));
        assertEquals(DateFormatUtils.format(new Date(), "dd/MM/yyyy"), parametros.get("DATA_FINAL"));
        assertEquals("Robson", parametros.get("NOME_PESSOA"));
        assertEquals(new Locale("pt", "BR"), parametros.get("REPORT_LOCALE"));
    }

    @Test()
    public void deveRetornaByteArray() {
        byte[] extrato = extratoReportPDF.gerarExtrato(new ExtratoDTO("Robson", new BigDecimal(111222.22), new BigDecimal(222300.22), new Date(), new Date()));
        assertNotNull(extrato);
    }
}
