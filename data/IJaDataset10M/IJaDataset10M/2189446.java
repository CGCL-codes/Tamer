package net.sf.brightside.aikido.service;

import java.util.List;
import net.sf.brightside.aikido.core.command.Command;
import net.sf.brightside.aikido.metamodel.Aikidoist;
import net.sf.brightside.aikido.metamodel.Practice;

public interface ListAikidoistsAssociateWithPracticeCommand extends Command<List<Aikidoist>> {

    public void setPractice(Practice practice);

    public Practice getPractice();
}
