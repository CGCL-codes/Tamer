package br.com.visualmidia.persistence;

import br.com.visualmidia.exception.BusinessException;

public class SetConfigure extends GDTransaction {

    private static final long serialVersionUID = 3257853198839854899L;

    private boolean status;

    public SetConfigure(boolean status) {
        super();
        this.status = status;
    }

    @Override
    protected void execute(PrevalentSystem system) throws BusinessException {
        system.systemConfigure = status;
    }
}
