package se.inera.ifv.casebox.services;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import java.io.InputStream;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import org.easymock.Capture;
import org.junit.Test;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.ifv.casebox.core.service.QuestionService;
import se.inera.ifv.deletequestionsresponder.v1.DeleteQuestionsType;

/**
 * @author P�r Wen�ker
 *
 */
public class TestDeleteQuestionsImpl {

    @Test
    public void testDeleteQuestions() throws Exception {
        Capture<String> careUnit = new Capture<String>();
        Capture<Set<Long>> ids = new Capture<Set<Long>>();
        QuestionService questionService = createMock(QuestionService.class);
        questionService.deleteQuestionsForCareUnit(capture(careUnit), capture(ids));
        expectLastCall();
        DeleteQuestionsImpl impl = new DeleteQuestionsImpl();
        impl.setQuestionService(questionService);
        replay(questionService);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("DeleteQuestionsResponder_0.9.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(DeleteQuestionsType.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        @SuppressWarnings("unchecked") JAXBElement<DeleteQuestionsType> t = (JAXBElement<DeleteQuestionsType>) unmarshaller.unmarshal(is);
        AttributedURIType address = new AttributedURIType();
        address.setValue("careUnit1");
        impl.deleteQuestions(address, t.getValue());
        assertNotNull(careUnit.getValue());
        assertNotNull(ids.getValue());
        assertEquals("careUnit1", careUnit.getValue());
        assertEquals(8, ids.getValue().size());
        assertTrue(ids.getValue().contains(1L));
        assertTrue(ids.getValue().contains(5L));
        assertTrue(ids.getValue().contains(6L));
        assertTrue(ids.getValue().contains(12L));
        assertTrue(ids.getValue().contains(19L));
        assertTrue(ids.getValue().contains(23L));
        assertTrue(ids.getValue().contains(26L));
        assertTrue(ids.getValue().contains(29L));
    }
}
