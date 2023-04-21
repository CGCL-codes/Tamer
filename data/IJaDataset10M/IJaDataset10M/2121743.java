package org.nomicron.round12.module;

import org.nomicron.suber.enums.TallyType;
import org.nomicron.suber.model.object.Book;
import org.nomicron.suber.model.object.TimeDrop;
import org.nomicron.suber.module.SuberModule;

/**
 * Liff Module
 */
public class LiffModule extends SuberModule {

    /**
     * Initialization method called on startup--override to initialize constants.
     */
    @Override
    public void init() {
        getLiffTimeDrop();
    }

    private TimeDrop getLiffTimeDrop() {
        String objectKey = "liffTimeDrop";
        TimeDrop timeDrop = getMetaFactory().getTimeDropFactory().getTimeDropByObjectKey(objectKey);
        if (timeDrop == null) {
            timeDrop = new TimeDrop();
            timeDrop.setObjectKey(objectKey);
            timeDrop.setTimeDropBook(getLiffTimeDropBook());
            timeDrop.setEffectiveBook(getBookOfWords());
            timeDrop.setElectionBook(getLiffElectionBook());
            timeDrop.setElectionTitle("Liff Election");
            timeDrop.setElectionDescription("Rule 324");
            timeDrop.setElectionTallyType(TallyType.NOMICOUNT);
            timeDrop.save();
        }
        return timeDrop;
    }

    private Book getBookOfWords() {
        return getMetaFactory().getBookFactory().getBookByObjectKey("wordsReferendumBook");
    }

    private Book getLiffTimeDropBook() {
        return getMetaFactory().getBookFactory().getBookByObjectKey("liffTimeDropBook");
    }

    public Book getLiffElectionBook() {
        return getMetaFactory().getBookFactory().getBookByObjectKey("liffElectionBook");
    }
}
