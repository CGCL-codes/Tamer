package org.jraptor.domain.dao;

import static junit.framework.Assert.assertEquals;
import static org.jraptor.domain.Arg.asc;
import java.util.Calendar;
import java.util.List;
import org.jraptor.domain.AbstractDomainTest;
import org.jraptor.domain.model.Individual;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:goran.oberg@jraptor.org">Goran Oberg</a>
 * @version $Rev: 141 $ $Date: 2008-12-21 18:41:26 -0500 (Sun, 21 Dec 2008) $
 */
@Transactional
public class AbstractDaoExpressionTest extends AbstractDomainTest {

    @Autowired
    private IndividualDao individualDao;

    @Test
    public void countByTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1980, 1, 1);
        long count = individualDao.countBy("DobLessThan", calendar.getTime());
        assertEquals(3, count);
    }

    @Test
    public void findAllByTest() {
        List<Individual> individuals = individualDao.findAllBy("FirstName", "Joe");
        assertEquals(2, individuals.size());
        assertEquals("joe.bloggs@gmail.com", individuals.get(0).getEmail());
        assertEquals("joe.doe@gmail.com", individuals.get(1).getEmail());
        individuals = individualDao.findAllBy("FirstName", "Joe", asc("dob"));
        assertEquals(2, individuals.size());
        assertEquals("joe.doe@gmail.com", individuals.get(0).getEmail());
        assertEquals("joe.bloggs@gmail.com", individuals.get(1).getEmail());
    }

    @Test
    public void findByTest() {
        Individual individual = individualDao.findBy("Email", "joe.bloggs@gmail.com");
        assertEquals(Long.valueOf(3), individual.getId());
        individual = individualDao.findBy("EmailIlike", "Joe.Bloggs%");
        assertEquals(Long.valueOf(3), individual.getId());
        Calendar after = Calendar.getInstance();
        after.set(1960, 1, 1);
        Calendar before = Calendar.getInstance();
        before.set(1980, 1, 1);
        individual = individualDao.findBy("FirstNameLikeAndDobBetween", "Joe", after.getTime(), before.getTime());
        assertEquals(Long.valueOf(4), individual.getId());
    }
}
