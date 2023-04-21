package br.com.jnfe.util;

import static org.junit.Assert.assertEquals;
import org.helianto.core.KeyType;
import org.helianto.core.Operator;
import org.helianto.core.Province;
import org.helianto.core.test.OperatorTestSupport;
import org.helianto.partner.Partner;
import org.helianto.partner.test.PartnerTestSupport;
import org.junit.Before;
import org.junit.Test;
import br.com.jnfe.test.SerieTestSupport;

/**
 * 
 * @author Mauricio Fernandes de Castro
 */
public class CNPJUtilsTests {

    /**
	 * Caso encontre uma chave associada diretamente ao parceiro,
	 * oculta a chave associada ao cadastro.
	 */
    @Test
    public void key() {
        Partner partner = PartnerTestSupport.createPartner();
        partner.getPrivateEntity().addKeyValuePair(cnpjKey, "KEYVALUE1");
        assertEquals("KEYVALUE1", CNPJUtils.getKey(partner, "CNPJ"));
        partner.addKeyValuePair(cnpjKey, "KEYVALUE2");
        assertEquals("KEYVALUE2", CNPJUtils.getKey(partner, "CNPJ"));
        assertEquals("KEYVALUE2", CNPJUtils.getCNPJ(partner));
    }

    public void uf() {
    }

    KeyType cnpjKey;

    Province uf;

    @Before
    public void setUp() {
        Operator operator = OperatorTestSupport.createOperator();
        cnpjKey = SerieTestSupport.garanteChaveCNPJNoOperador(operator);
        uf = SerieTestSupport.garanteProvinciaNoOperador(operator);
    }
}
