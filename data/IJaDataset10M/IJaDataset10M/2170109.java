package net.sf.bacchus.record.generate;

import net.sf.bacchus.record.ControlSupport;
import net.sf.bacchus.record.process.ControlCalculator;
import net.sf.bacchus.record.process.ControlTotal;

/** Updates control totals into the control records. */
public class ControlGenerator extends ControlCalculator<ControlSupport> {

    /** Creates a control generator in default state. */
    public ControlGenerator() {
        super(ControlSupport.class);
    }

    /**
     * Creates a control generator in a state partially through a processing
     * cycle. Use this constructor to restart a processor that has been stopped
     * in the middle of a file.
     * @param file the running totals for the completed company/batches.
     * @param batch the running totals for the current company/batch.
     */
    public ControlGenerator(final ControlTotal file, final ControlTotal batch) {
        super(file, batch, ControlSupport.class);
    }

    /**
     * Updates the control totals in the control record.
     * @param control {@inheritDoc}
     * @param batch {@inheritDoc}
     */
    @Override
    protected void handleBatchControl(final ControlSupport control, final ControlTotal batch) {
        update(control, batch);
    }

    /**
     * Updates the control totals in the control record.
     * @param control {@inheritDoc}
     * @param file {@inheritDoc}
     */
    @Override
    protected void handleFileControl(final ControlSupport control, final ControlTotal file) {
        update(control, file);
    }

    /**
     * Internal utility to update a control record.
     * @param control the control record.
     * @param total the control totals.
     */
    private void update(final ControlSupport control, final ControlTotal total) {
        control.setEntryAddendaCount(total.getEntryAddenda());
        control.setTotalCredit(total.getCredit());
        control.setTotalDebit(total.getDebit());
        control.setEntryHash(total.getEntryHash());
    }
}
