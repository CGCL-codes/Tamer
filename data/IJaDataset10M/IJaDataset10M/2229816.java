package net.sf.brightside.aikido.service.spring;

import net.sf.brightside.aikido.core.command.Command;
import net.sf.brightside.aikido.metamodel.Club;
import net.sf.brightside.aikido.metamodel.Dojo;

public interface AddClubDojoCommand extends Command {

    public void setClub(Club club);

    public Club getClub();

    public void setDojo(Dojo dojo);

    public Dojo getDojo();
}
