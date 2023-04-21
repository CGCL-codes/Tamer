package net.sf.brightside.bonko.service;

import net.sf.brightside.bonko.core.command.Command;

public interface Delete extends Command<Boolean> {

    void setObject(Object object);

    Object getObject();
}
