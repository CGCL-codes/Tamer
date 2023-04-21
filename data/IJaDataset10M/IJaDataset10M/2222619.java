package fr.gfi.gfinet.server.info;

import static org.testng.AssertJUnit.assertEquals;
import java.sql.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import fr.gfi.gfinet.server.dto.Time;

/**
 * TestCase pour l'entit� Collaborator.
 * 
 * @author Tiago Fernandez
 * @since 11 sept. 07
 */
public class CollaboratorTestCase {

    public static final Log logger = LogFactory.getLog(CollaboratorTestCase.class);

    /**
	 * @see fr.gfi.gfinet.server.info.Collaborator#calculateAge()
	 * @throws Throwable
	 */
    @Test
    public void testCalculatetAge() throws Throwable {
        Collaborator col = new Collaborator();
        col.setBirthDate(Date.valueOf("1984-04-12"));
        int result = col.calculateAge();
        assertEquals(23, result);
        col.setBirthDate(Date.valueOf("1984-11-09"));
        result = col.calculateAge();
        assertEquals(22, result);
    }

    /**
	 * @see fr.gfi.gfinet.server.info.Collaborator#calculateSeniority()
	 * @throws Throwable
	 */
    @Test
    public void testCalculatetSeniority() throws Throwable {
        Collaborator col = new Collaborator();
        col.setHiringDate(Date.valueOf("2002-02-12"));
        Time sen = col.calculateSeniority();
        logger.debug(sen.years + " years " + sen.months + " months ");
        col.setHiringDate(Date.valueOf("2002-02-20"));
        sen = col.calculateSeniority();
        logger.debug(sen.years + " years " + sen.months + " months ");
        col.setHiringDate(Date.valueOf("2002-11-18"));
        sen = col.calculateSeniority();
        logger.debug(sen.years + " years " + sen.months + " months ");
        col.setHiringDate(Date.valueOf("2008-11-18"));
        sen = col.calculateSeniority();
        logger.debug(sen.years + " years " + sen.months + " months ");
    }
}
