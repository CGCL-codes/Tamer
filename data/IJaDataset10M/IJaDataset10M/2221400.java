package net.sf.brightside.aikido.service.spring;

import java.util.Date;
import net.sf.brightside.aikido.core.command.Command;
import net.sf.brightside.aikido.metamodel.Aikidoist;
import net.sf.brightside.aikido.metamodel.Rank;

public interface UpdateAikidoistCommand extends Command<Aikidoist> {

    public void setAikidoist(Aikidoist Aikidoist);

    public Aikidoist getAikidoist();

    public void setRank(Rank rank);

    public Rank getRank();

    public void setLastPassedExamDate(Date date);

    public Date getLastPassedExamDate();
}
