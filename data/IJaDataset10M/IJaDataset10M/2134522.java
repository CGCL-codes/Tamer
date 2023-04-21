package net.sf.brightside.aikido.service.clubregistration;

import java.util.List;
import net.sf.brightside.aikido.core.spring.AbstractSpringTest;
import net.sf.brightside.aikido.metamodel.Club;
import net.sf.brightside.aikido.service.crud.RetriveResultsCommand;
import org.hibernate.criterion.DetachedCriteria;
import org.testng.annotations.Test;

public class ClubRegistrationCommandTest extends AbstractSpringTest {

    private ClubRegistrationCommand<Club> clubRegistrationCommand;

    private RetriveResultsCommand<List<Club>> retriveResultsCommand;

    private Club club;

    protected Club createClub() {
        return (Club) applicationContext.getBean(Club.class.getName());
    }

    @SuppressWarnings("unchecked")
    protected RetriveResultsCommand<List<Club>> createRetriveResultsCommand() {
        return (RetriveResultsCommand<List<Club>>) applicationContext.getBean(RetriveResultsCommand.class.getName());
    }

    @SuppressWarnings("unchecked")
    protected ClubRegistrationCommand<Club> createClubRegistrationCommand() {
        return (ClubRegistrationCommand<Club>) applicationContext.getBean(ClubRegistrationCommand.class.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        club = createClub();
        club.setName("SomeClub");
        retriveResultsCommand = createRetriveResultsCommand();
        retriveResultsCommand.setDetachedCriteria(DetachedCriteria.forClass(Club.class));
        clubRegistrationCommand = createClubRegistrationCommand();
    }

    @Test
    public void testExecute() throws Exception {
        clubRegistrationCommand.setClub(club);
        clubRegistrationCommand.execute();
        assertEquals(club.getName(), retriveResultsCommand.execute().get(0).getName());
    }
}
