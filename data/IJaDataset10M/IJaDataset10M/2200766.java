package it.webscience.kpeople.bll.impl.activitihandler;

import it.webscience.kpeople.be.Activity;
import it.webscience.kpeople.bll.exception.KPeopleActivityHandlerException;
import it.webscience.kpeople.bll.impl.SendEventManager;

public class RichiestaContributoAcceptActivityHandler extends BaseActivityHandler implements ActivityHandler {

    /**
     * Costruttore.
     * @param pActivity
     */
    public RichiestaContributoAcceptActivityHandler(Activity pActivity) {
        super(pActivity);
    }

    @Override
    public void execute() throws KPeopleActivityHandlerException {
        SendEventManager sem = new SendEventManager();
        try {
            sem.sendEventAcceptContribute(activity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new KPeopleActivityHandlerException(e.getMessage());
        }
    }
}
