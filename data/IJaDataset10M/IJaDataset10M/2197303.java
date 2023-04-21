package org.jquantlib.termstructures.yieldcurves;

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.termstructures.RateHelper;
import org.jquantlib.time.Date;

public abstract class RelativeDateRateHelper extends RateHelper {

    protected Date evaluationDate;

    public RelativeDateRateHelper(final double d) {
        super(d);
        QL.validateExperimentalMode();
        this.evaluationDate = new Settings().evaluationDate();
        this.evaluationDate.addObserver(this);
    }

    public RelativeDateRateHelper(final Handle<Quote> quote) {
        super(quote);
        this.evaluationDate = new Settings().evaluationDate();
        this.evaluationDate.addObserver(this);
    }

    protected abstract void initializeDates();

    @Override
    public void update() {
        final Date newEvaluationDate = new Settings().evaluationDate();
        if (!evaluationDate.equals(newEvaluationDate)) {
            evaluationDate = newEvaluationDate;
            initializeDates();
        }
        super.update();
    }
}
