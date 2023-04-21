package net.sf.brightside.aikido.service.queries;

import java.util.List;
import net.sf.brightside.aikido.core.command.Command;

public interface GetAllByTypeCommand<Type> extends Command<List<Type>> {

    public void setType(Class<Type> clazz);

    public Class<Type> getType();
}
