package net.sf.brightside.aikido.service.spring;

import net.sf.brightside.aikido.core.spring.AbstractSpringTest;
import net.sf.brightside.aikido.metamodel.Club;
import net.sf.brightside.aikido.metamodel.Dojo;
import net.sf.brightside.aikido.service.DeleteDojoCommand;
import net.sf.brightside.aikido.service.GetByNameCommand;
import net.sf.brightside.aikido.service.SaveCommand;
import org.testng.annotations.Test;

public class DeleteDojoCommandTest extends AbstractSpringTest {

    DeleteDojoCommand deleteDojoCommand;

    Dojo dojo;

    Club club;

    SaveCommand saveCommand;

    GetByNameCommand getByNameCommand;

    public DeleteDojoCommand createDeleteDojoCommand() {
        return (DeleteDojoCommand) applicationContext.getBean(DeleteDojoCommand.class.getName());
    }

    public Club createClub() {
        return (Club) applicationContext.getBean(Club.class.getName());
    }

    public Dojo createDojo() {
        return (Dojo) applicationContext.getBean(Dojo.class.getName());
    }

    public SaveCommand createSaveCommand() {
        return (SaveCommand) applicationContext.getBean(SaveCommand.class.getName());
    }

    public GetByNameCommand createGetByNameCommand() {
        return (GetByNameCommand) applicationContext.getBean(GetByNameCommand.class.getName());
    }

    @Override
    public void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        deleteDojoCommand = createDeleteDojoCommand();
        dojo = createDojo();
        club = createClub();
        saveCommand = createSaveCommand();
        getByNameCommand = createGetByNameCommand();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeleteClub() {
        saveCommand.setObject(club);
        saveCommand.execute();
        dojo.getClubs().add(club);
        saveCommand.setObject(dojo);
        saveCommand.execute();
        assertNotNull(dojo);
        deleteDojoCommand.setDojo(dojo);
        assertTrue(deleteDojoCommand.execute());
    }
}
