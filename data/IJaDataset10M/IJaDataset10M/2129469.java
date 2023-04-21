package net.sf.brightside.luxurycruise.service.crud.impl;

import net.sf.brightside.luxurycruise.core.persistence.PersistenceFacade;
import net.sf.brightside.luxurycruise.core.persistence.impl.PersistenceFacadeHibernate;
import net.sf.brightside.luxurycruise.service.crud.Command;

public class WriteCommand<Type> implements Command<Type> {

    private Type input;

    private PersistenceFacade facade;

    public WriteCommand(Type input) {
        this.input = input;
    }

    public WriteCommand() {
    }

    @Override
    public Type execute() {
        return facade.write(input);
    }

    public Type getInput() {
        return input;
    }

    public void setInput(Type input) {
        this.input = input;
    }

    public PersistenceFacade getFacade() {
        return facade;
    }

    public void setFacade(PersistenceFacade facade) {
        this.facade = facade;
    }
}
