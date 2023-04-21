package de.waldheinz.dependencies;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.InputStream;
import org.junit.Test;

/**
 * 
 * 
 *
 * @author Matthias Treydte <waldheinz at gmail.com>
 */
public class XMLCoDomainStoreTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private XMLCoDomainStore load() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("de/waldheinz/dependencies/resources/CoDomains1.xml");
        XMLCoDomainStore instance = new XMLCoDomainStore();
        instance.read(is);
        return instance;
    }

    /**
     * Test of read method, of class XMLCoDomainStore.
     * @throws Exception on error.
     */
    @Test
    public void testLoad() throws Exception {
        load();
    }

    @Test
    public void testStore() throws Exception {
        XMLCoDomainStore cds = load();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        cds.write(os);
        System.out.println(os.toString());
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        cds = new XMLCoDomainStore();
        cds.read(is);
    }

    /**
     * Test of getCoDomainIDs method, of class XMLCoDomainStore.
     * @throws IOException on read error.
     */
    @Test
    public void testGetCoDomainIDs() throws IOException {
        CoDomainStore store = load();
        assertEquals(2, store.getCoDomainIDs().size());
    }
}
