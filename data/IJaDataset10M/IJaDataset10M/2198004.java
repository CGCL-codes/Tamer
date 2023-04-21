package br.usp.ime.dojo.core.repositories;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepositoryFactoryTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getDojoRoomRepositoryMustReturnSomeKindOfDojoRoomRepository() {
        assertTrue(RepositoryFactory.createDojoRoomRepository() instanceof DojoRoomRepository);
    }

    @After
    public void tearDown() throws Exception {
    }
}
