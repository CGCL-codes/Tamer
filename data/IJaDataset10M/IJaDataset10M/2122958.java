package org.libreplan.business.planner.entities.consolidations;

import java.math.BigDecimal;
import org.joda.time.LocalDate;
import org.libreplan.business.advance.entities.AdvanceMeasurement;
import org.libreplan.business.util.deepcopy.DeepCopy;
import org.libreplan.business.util.deepcopy.OnCopy;
import org.libreplan.business.util.deepcopy.Strategy;
import org.libreplan.business.workingday.IntraDayDate;

/**
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
public class NonCalculatedConsolidatedValue extends ConsolidatedValue {

    private NonCalculatedConsolidation consolidation;

    @OnCopy(Strategy.SHARE)
    private AdvanceMeasurement advanceMeasurement;

    public static NonCalculatedConsolidatedValue create() {
        return create(new NonCalculatedConsolidatedValue());
    }

    public static NonCalculatedConsolidatedValue create(LocalDate date, BigDecimal value, IntraDayDate taskEndDate) {
        return create(new NonCalculatedConsolidatedValue(date, value, taskEndDate));
    }

    public static NonCalculatedConsolidatedValue create(LocalDate date, BigDecimal value, AdvanceMeasurement advanceMeasurement, IntraDayDate taskEndDate) {
        return create(new NonCalculatedConsolidatedValue(date, value, advanceMeasurement, taskEndDate));
    }

    protected NonCalculatedConsolidatedValue(LocalDate date, BigDecimal value, AdvanceMeasurement advanceMeasurement, IntraDayDate taskEndDate) {
        this(date, value, taskEndDate);
        this.advanceMeasurement = advanceMeasurement;
    }

    protected NonCalculatedConsolidatedValue(LocalDate date, BigDecimal value, IntraDayDate taskEndDate) {
        super(date, value, taskEndDate);
    }

    /**
     * Constructor for {@link DeepCopy}. DO NOT USE!
     */
    public NonCalculatedConsolidatedValue() {
    }

    public void setAdvanceMeasurement(AdvanceMeasurement advanceMeasurement) {
        this.advanceMeasurement = advanceMeasurement;
    }

    public AdvanceMeasurement getAdvanceMeasurement() {
        return advanceMeasurement;
    }

    public void setConsolidation(NonCalculatedConsolidation consolidation) {
        this.consolidation = consolidation;
    }

    public NonCalculatedConsolidation getConsolidation() {
        return consolidation;
    }

    @Override
    public boolean isCalculated() {
        return false;
    }
}
