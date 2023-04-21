package org.ombu.test;

import org.ombu.client.CoordinatorControllerLocal;
import org.ombu.services.at.IServiceLogic;

public class SendPreparedServiceLogic implements IServiceLogic {

    public CoordinatorControllerLocal coordinationEJB;

    public SendPreparedServiceLogic(CoordinatorControllerLocal coordinationEJB) {
        super();
        this.coordinationEJB = coordinationEJB;
    }

    @Override
    public void run(final long participantId, final String secret) throws Exception {
        new Thread(new Runnable() {

            public void run() {
                coordinationEJB.prepared(participantId, secret);
            }
        }).start();
    }
}
