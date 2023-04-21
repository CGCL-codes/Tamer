package org.jenmo.core.domain;

import javax.persistence.EntityManager;
import org.jenmo.common.util.IProcedure.ProcedureException;
import org.jenmo.core.testutil.DbUseCase;
import org.jenmo.core.testutil.MyTimer;
import org.jenmo.core.testutil.DbUseCase.DbUseCaseException;

public abstract class AbstractTestDbPopu extends AbstractTestDb {

    protected static void populateData(final IPopulator populator, boolean closeEm) throws DbUseCaseException {
        DbUseCase dbUc = new DbUseCase() {

            @Override
            protected void reallyExecute(EntityManager em) throws DbUseCaseException {
                try {
                    populator.execute();
                } catch (ProcedureException e) {
                    throw new DbUseCaseException(e);
                }
            }
        };
        dbUc.execute(populator.getEm(), closeEm);
    }

    protected static void runPopulator(IPopulator populator) throws Exception {
        if (populator != null) {
            MyTimer timer = new MyTimer();
            populateData(populator, false);
            timer.end("End Populating data");
        }
    }
}
