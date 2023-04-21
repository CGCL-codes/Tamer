package br.gov.component.demoiselle.crud.message;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrudMessagesTest extends TestCase {

    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetLabel() {
        String label = CrudMessages.CRUD_DELETE_OK.getLabel();
        assertEquals("Entidade excluída com sucesso.", label);
    }
}
