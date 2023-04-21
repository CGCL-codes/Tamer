package br.usp.ime.dojo.core.repositories.impl.memoria;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import br.usp.ime.dojo.core.entities.DojoRoom;
import br.usp.ime.dojo.core.exceptions.DuplicatedRoomException;
import br.usp.ime.dojo.core.repositories.DojoRoomRepository;

public class DojoRoomRepositoryInMemoryTest {

    DojoRoomRepository djr;

    @Before
    public void setUp() throws Exception {
        djr = DojoRoomRepositoryInMemory.getInstance();
        djr.clearAll();
    }

    @Test
    public void getDojoRoomListMustReturnAnEmptyListWhenNoDojoRoomsWereCreated() {
        assertEquals(0, djr.getDojoRoomList().size());
    }

    @Test
    public void DojoRoomListMustHaveSizeOneWhenOneDojoRoomWasCreated() throws DuplicatedRoomException {
        djr.addRoom(new DojoRoom(null));
        assertEquals(1, djr.getDojoRoomList().size());
    }

    @Test
    public void DojoRoomRepositoryInMemoryMustBeASingleton() {
        DojoRoomRepository djr1 = DojoRoomRepositoryInMemory.getInstance();
        DojoRoomRepository djr2 = DojoRoomRepositoryInMemory.getInstance();
        assertSame(djr1, djr2);
    }

    @Test
    public void clearAllMustCleanTheRepository() throws DuplicatedRoomException {
        djr.addRoom(new DojoRoom("Room 1", "Wise guys room"));
        djr.addRoom(new DojoRoom("Room 2", "Not so wise guys room"));
        assertEquals(2, djr.count());
        djr.clearAll();
        assertEquals(0, djr.count());
    }

    @After
    public void tearDown() throws Exception {
    }
}
