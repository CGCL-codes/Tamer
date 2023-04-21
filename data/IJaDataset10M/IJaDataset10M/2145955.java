package net.sf.brightside.xlibrary.service.viewRegisteredStudents;

import net.sf.brightside.xlibrary.core.command.Command;
import net.sf.brightside.xlibrary.metamodel.Student;
import net.sf.brightside.xlibrary.service.Get;

public interface GetAllStudents<E> extends Command<E> {

    Get<E> getGetCommand();

    void setGetCommand(Get<E> getCommand);
}
