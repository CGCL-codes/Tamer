package br.com.jnfe.core.filter.classic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.Serializable;
import org.helianto.core.User;
import org.helianto.core.test.UserTestSupport;
import org.helianto.partner.Division;
import org.junit.Before;
import org.junit.Test;
import br.com.jnfe.base.TpAmb;
import br.com.jnfe.base.TpServico;

/**
 * 
 * @author Mauricio Fernandes de Castro
 */
public class ServicoFilterTests {

    @Test
    public void constructor() {
        ServicoFilter servicoFilter = new ServicoFilter();
        assertTrue(servicoFilter instanceof Serializable);
        assertEquals("", servicoFilter.getTpServico());
        assertEquals(' ', servicoFilter.getTpAmb());
    }

    @Test
    public void userConstructor() {
        User user = new User();
        ServicoFilter servicoFilter = new ServicoFilter(user);
        assertSame(servicoFilter.getUser(), user);
        assertEquals("", servicoFilter.getTpServico());
        assertEquals(' ', servicoFilter.getTpAmb());
    }

    public static String C1 = "servico.ambiente.emitente.partnerRegistry.entity.id = 0 ";

    public static String C2 = "AND servico.tpServico = 'Consulta situa��o' ";

    public static String C3 = "AND servico.emitente.id = 2147483647 ";

    public static String C4 = "AND servico.tpAmb = '2' ";

    @Test
    public void select() {
        assertEquals(C1, filter.createCriteriaAsString(false));
    }

    @Test
    public void createCriteriaAsStringTpServico() {
        filter.setTpServico(TpServico.CONSULTA_SIT.getNome());
        assertEquals(C1 + C2, filter.createCriteriaAsString(false));
    }

    @Test
    public void createCriteriaAsStringServicoNameLike() {
        Division emitente = new Division();
        emitente.setId(Integer.MAX_VALUE);
        filter.setEmitente(emitente);
        assertEquals(C1 + C3, filter.createCriteriaAsString(false));
    }

    @Test
    public void createCriteriaAsStringTpAmb() {
        filter.setTpAmb(TpAmb.HOMOLOGACAO.getValue());
        assertEquals(C1 + C4, filter.createCriteriaAsString(false));
    }

    private ServicoFilter filter;

    @Before
    public void setUp() {
        filter = new ServicoFilter(UserTestSupport.createUser());
    }
}
