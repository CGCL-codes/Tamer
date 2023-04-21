package net.sf.brightside.aikido.service.clubregistration;

import net.sf.brightside.aikido.core.command.Command;
import net.sf.brightside.aikido.metamodel.Club;

public interface ClubRegistrationCommand<E> extends Command<E> {

    public Club getClub();

    public void setClub(Club club);
}
