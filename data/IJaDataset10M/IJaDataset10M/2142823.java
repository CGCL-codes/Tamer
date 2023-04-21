package org.identifylife.character.store.service.ontology;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import org.identifylife.character.store.TestData;
import org.identifylife.character.store.model.ontology.Ontology;
import org.identifylife.character.store.repository.ontology.OntologyRepository;
import org.identifylife.character.store.service.NotFoundException;
import org.identifylife.character.store.service.UniqueConstraintException;
import org.identifylife.character.store.service.ontology.impl.OntologyServiceImpl;
import org.identifylife.core.utils.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author dbarnier
 *
 */
public class OntologyServiceTests {

    private OntologyRepository repository;

    private OntologyService service;

    @Before
    public void setUp() throws Exception {
        repository = createMock(OntologyRepository.class);
        service = new OntologyServiceImpl(repository);
    }

    @Test
    public void delete() throws Exception {
        Ontology expected = TestData.TEST_ONTOLOGY();
        expect(repository.getByUuid(expected.getUuid())).andReturn(expected);
        repository.delete(expected);
        replay(repository);
        service.delete(expected.getUuid());
        verify(repository);
    }

    @Test
    public void deleteInvalidUuid() throws Exception {
        expect(repository.getByUuid(TestData.INVALID_UUID)).andReturn(null);
        replay(repository);
        try {
            service.delete(TestData.INVALID_UUID);
            fail("expected NotFoundException");
        } catch (NotFoundException nfe) {
        }
        verify(repository);
    }

    @Test
    public void exists() throws Exception {
        expect(repository.getByUuid(TestData.VALID_UUID)).andReturn(new Ontology());
        replay(repository);
        boolean result = service.exists(TestData.VALID_UUID);
        assertTrue(result);
        verify(repository);
    }

    @Test
    public void existsInvalidUuid() throws Exception {
        expect(repository.getByUuid(TestData.INVALID_UUID)).andReturn(null);
        replay(repository);
        boolean result = service.exists(TestData.INVALID_UUID);
        assertFalse(result);
        verify(repository);
    }

    @Test
    public void getByUuid() throws Exception {
        Ontology expected = TestData.TEST_ONTOLOGY();
        expect(repository.getByUuid(expected.getUuid())).andReturn(expected);
        replay(repository);
        Ontology result = service.getByUuid(expected.getUuid());
        assertNotNull(result);
        assertEquals(expected, result);
        verify(repository);
    }

    @Test
    public void getByInvalidUuid() throws Exception {
        expect(repository.getByUuid(TestData.INVALID_UUID)).andReturn(null);
        replay(repository);
        Ontology result = service.getByUuid(TestData.INVALID_UUID);
        assertNull(result);
        verify(repository);
    }

    @Test
    public void save() throws Exception {
        Ontology expected = TestData.TEST_ONTOLOGY();
        expect(repository.exists(expected.getUuid())).andReturn(false);
        repository.save(expected);
        replay(repository);
        service.save(expected);
        verify(repository);
    }

    @Test
    public void saveDuplicate() throws Exception {
        Ontology expected = TestData.TEST_ONTOLOGY();
        expect(repository.exists(expected.getUuid())).andReturn(true);
        replay(repository);
        try {
            service.save(expected);
            fail("expected UniqueConstraintException");
        } catch (UniqueConstraintException ex) {
        }
        verify(repository);
    }

    @Test
    public void getByTaxonId() throws Exception {
        List<Ontology> expected = CollectionUtils.newArrayList();
        expected.add(TestData.TEST_ONTOLOGY());
        String taxonId = expected.get(0).getTaxon().getUuid();
        expect(repository.getByTaxonId(taxonId)).andReturn(expected);
        replay(repository);
        List<Ontology> results = service.getByTaxonId(taxonId);
        assertNotNull(results);
        assertEquals(expected, results);
        verify(repository);
    }
}
