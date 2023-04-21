package br.ufrj.cad.functionaltest.banca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import br.ufrj.cad.fwk.model.ObjetoPersistente;
import br.ufrj.cad.fwk.util.UtilData;
import br.ufrj.cad.model.banca.BancaService;
import br.ufrj.cad.model.bo.Banca;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.bo.UsuarioComum;
import br.ufrj.cad.model.to.BancaTO;
import br.ufrj.cad.unittest.util.DatabaseUtil;
import br.ufrj.cad.view.banca.BancaHelper;

public class BancaServiceTest {

    private static final String NOME_NOVA_BANCA = "AUABABABA";

    Usuario usuario = new UsuarioComum();

    private BancaService service;

    private long idnovobanca;

    @Before
    public void setUp() throws Exception {
        service = BancaService.getInstance();
        inserirBanca();
    }

    @After
    public void shutDown() throws Exception {
        apagarBanca();
    }

    public void inserirBanca() {
        UtilData.setDataAtual(UtilData.criaData(27, GregorianCalendar.MARCH, 2006));
        BancaTO novoBancaTO = new BancaTO();
        novoBancaTO.setNumAta(NOME_NOVA_BANCA);
        ObjetoPersistente novoBanca = service.salvaBanca(novoBancaTO, usuario, false);
        List<String[]> res = DatabaseUtil.executeQuery("SELECT BANC_SQ_BANCA, BANC_TX_NUMERO_ATA FROM BANCA WHERE BANC_SQ_BANCA = " + novoBanca.getId());
        assertEquals(1, res.size());
        assertEquals(novoBanca.getId().toString(), res.get(0)[0]);
        assertEquals(NOME_NOVA_BANCA, res.get(0)[1]);
        idnovobanca = novoBanca.getId();
    }

    @Test
    public void obtemBancasSemFiltro() {
        List<Banca> bancas = new Banca().findAll();
        assertNotNull(bancas);
    }

    @Test
    public void obtemBancaPorId() {
        Banca bancaNoBanco = service.obtemBancaPorId(idnovobanca);
        assertNotNull(bancaNoBanco);
    }

    @Test
    public void alterarBanca() {
        Banca banca = service.obtemBancaPorId(idnovobanca);
        banca.setNumAta("xxxxx");
        BancaTO to = BancaHelper.obtemTOBanca(banca);
        service.salvaBanca(to, usuario, true);
        List<String[]> res = DatabaseUtil.executeQuery("SELECT BANC_SQ_BANCA, BANC_TX_NUMERO_ATA FROM BANCA WHERE BANC_SQ_BANCA = " + idnovobanca);
        assertEquals("xxxxx", res.get(0)[1]);
    }

    public void apagarBanca() {
        Banca banca = service.obtemBancaPorId(idnovobanca);
        banca.delete();
        banca = service.obtemBancaPorId(idnovobanca);
        assertNull(banca);
    }
}
