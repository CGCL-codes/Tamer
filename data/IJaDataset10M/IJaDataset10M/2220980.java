package br.com.jnfe.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.helianto.core.Phone;
import org.helianto.partner.Customer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import br.com.jnfe.base.NFe;
import br.com.jnfe.base.ProcEmi;
import br.com.jnfe.base.TpAmb;
import br.com.jnfe.core.Emitente;
import br.com.jnfe.core.EnvNFe;
import br.com.jnfe.core.JNFe;
import br.com.jnfe.core.RetConsReciNFe;
import br.com.jnfe.test.AbstractNFeServiceImplIntegration;

/**
 * 
 * @author Mauricio Fernandes de Castro
 */
@Transactional
public class NFeServiceImpl3RetEnvioLoteIntegrationTests extends AbstractNFeServiceImplIntegration {

    @Test
    public void nfeRetEnvioLote() {
        assertNotNull(nFeMgr);
        List<NFe> nFeList = new ArrayList<NFe>();
        serializerDao.saveOrUpdate(serie);
        Emitente emitente = serie.getEmitente();
        emitente.getPrivateEntity().setMainPhone(new Phone("12", "12345678"));
        partnerDao.saveOrUpdate(serie.getEmitente());
        Customer cliente = criaCliente(serie.getEntity());
        cliente.getPrivateEntity().setMainEmail("email@domain.com");
        cliente.getPrivateEntity().setMainPhone(new Phone("12", "12345678"));
        partnerDao.saveOrUpdate(cliente);
        EnvNFe envNFe = nFeMgr.storeEnvNFe(new EnvNFe(serie, ProcEmi.APLICATIVO_CONTRIB, TpAmb.HOMOLOGACAO.getValue()));
        JNFe nFe = criaNFe(serie, cliente);
        nFe.setDEmi(new Date());
        nFe.setDSaiEnt(new Date());
        nFeMgr.storeNFe(nFe);
        nFeMgr.linkNFe(envNFe, nFe);
        nFeList.add(nFe);
        logger.info("\n\nAGORA VAMOS REALMENTE ENVIAR O LOTE.\n\n");
        envNFe = nFeMgr.nfeEnvioLote(nFeList, envNFe);
        logger.info("\n\nEM SEGUIDA VAMOS VERIFICAR O RECIBO.\n\n");
        RetConsReciNFe retConsReciNFe = nFeMgr.nfeRetRecepcao(envNFe, 3);
        List<? extends NFe> nFeSavedList = nFeMgr.findNFe(envNFe);
        assertEquals(1, nFeSavedList.size());
        assertNotNull(retConsReciNFe);
    }

    private static final Logger logger = LoggerFactory.getLogger(NFeServiceImpl3RetEnvioLoteIntegrationTests.class);
}
