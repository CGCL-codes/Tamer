package br.com.visualmidia.persistence;

import br.com.visualmidia.exception.BusinessException;

public class RemoveIncomingToPay extends GDTransaction {

    private static final long serialVersionUID = 3257563988543092792L;

    private String id;

    public RemoveIncomingToPay(String id) {
        this.id = id;
    }

    protected void execute(PrevalentSystem system) throws BusinessException {
        if (system.incoming.containsKey(id)) system.incoming.remove(id);
    }
}
