package org.torweg.pulse.component.survey.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import junit.framework.TestCase;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;

/**
 * Tests {@code Survey}.
 * 
 * 
 * @see "components/survey/xsl/example.xsl" for a matching XSL-template to
 *      display the survey
 * 
 * @author Daniel Dietz
 * @version $Revision: 1602 $
 */
public class TestSurvey extends TestCase {

    /**
	 * The logger.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSurvey.class);

    /**
	 * The name for the test-survey.
	 */
    public static final String SURVEY_NAME = "test-survey-name";

    /**
	 * Indicates whether to remove test-{@code Survey} from database after
	 * testing.
	 */
    private static final boolean CLEAN_UP = true;

    /**
	 * @throws Exception .
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        new TestingEnvironment();
    }

    /**
	 * @throws Exception .
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected final void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test {@code Survey}.
	 * 
	 * @throws Exception .
	 */
    public final void testSurvey() throws Exception {
        Survey survey = getSurvey(false);
        Session s = Lifecycle.getHibernateDataSource().createNewSession();
        Transaction tx = s.beginTransaction();
        try {
            s.saveOrUpdate(survey);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        Survey survey_ldd = null;
        s = Lifecycle.getHibernateDataSource().createNewSession();
        tx = s.beginTransaction();
        try {
            survey_ldd = (Survey) s.get(Survey.class, survey.getId());
            survey_ldd.getQuestions().size();
            survey_ldd.getRoleIds().size();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new PulseException("Error: " + e.getLocalizedMessage(), e);
        } finally {
            s.close();
        }
        assertEquals(survey.getId(), survey_ldd.getId());
        assertEquals(survey.getQuestions().size(), survey_ldd.getQuestions().size());
        assertEquals(survey.getQuestion(0), survey_ldd.getQuestion(0));
        marshallAndUnmarshall(survey);
        marshallAndUnmarshall(survey_ldd);
        if (CLEAN_UP) {
            s = Lifecycle.getHibernateDataSource().createNewSession();
            tx = s.beginTransaction();
            try {
                s.delete(survey);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new PulseException("Error: " + e.getLocalizedMessage(), e);
            } finally {
                s.close();
            }
        }
    }

    /**
	 * Tries to <tt>marshall</tt> and <tt>unmarshall</tt> a {@code Survey} .
	 * 
	 * @param survey
	 *            the {@code Survey} to check
	 * 
	 * @throws JAXBException .
	 * @throws PropertyException .
	 */
    private void marshallAndUnmarshall(final Survey survey) throws JAXBException, PropertyException {
        Marshaller marshaller = Lifecycle.getJAXBContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter out = new StringWriter();
        marshaller.marshal(survey, out);
        LOGGER.info(System.getProperty("line.separator") + out.toString());
        Unmarshaller unmarshaller = Lifecycle.getJAXBContext().createUnmarshaller();
        Survey unmarshalledSurvey = (Survey) unmarshaller.unmarshal(new StringReader(out.toString()));
        assertEquals(SURVEY_NAME, survey.getName());
        assertEquals(SURVEY_NAME, unmarshalledSurvey.getName());
        assertEquals(survey.getQuestions().size(), unmarshalledSurvey.getQuestions().size());
        assertEquals(survey.getQuestion(0), unmarshalledSurvey.getQuestion(0));
    }

    /**
	 * @param persisted
	 *            {@code true} to get a persistent version of the test- {@code
	 *            Survey}, {@code false} otherwise
	 * 
	 * @return {@code Survey}
	 */
    public static final Survey getSurvey(final boolean persisted) {
        Survey survey = new Survey(SURVEY_NAME, getQuestions());
        survey.setStartDate(new Date());
        survey.setEndDate(new Date());
        if (!persisted) {
            return survey;
        } else {
            Session s = Lifecycle.getHibernateDataSource().createNewSession();
            Transaction tx = s.beginTransaction();
            try {
                s.saveOrUpdate(survey);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw new PulseException("Error: " + e.getLocalizedMessage(), e);
            } finally {
                s.close();
            }
            return survey;
        }
    }

    /**
	 * @return {@code List&lt;AbstractQuestion&gt;}
	 */
    public static final List<AbstractQuestion> getQuestions() {
        List<AbstractQuestion> quests = new ArrayList<AbstractQuestion>();
        quests.add(new OpenQuestion(null, true));
        quests.add(new OpenQuestion("\\d+", false));
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(6), true, null, null, null));
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(5), false, null, null, null));
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(4), false, 3, null, null));
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(6), false, 3, true, null));
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(6), false, 3, true, true));
        quests.add(TestQuestions.getQuestionGroup());
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(6), false, 1, true, false));
        quests.add(new SelectQuestion(TestQuestions.getValueCollection(6), false, 1, true, true));
        return quests;
    }
}
