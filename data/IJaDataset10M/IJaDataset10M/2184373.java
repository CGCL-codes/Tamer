package db.coted.model.general;

import db.coted.model.AbstractCTDSchema;
import db.coted.model.AbstractCTDView;

public class ViwPersonMain extends AbstractCTDView<ViwPersonMain> {

    public final COLUMN a = COLUMN();

    public final COLUMN b = COLUMN();

    public final COLUMN c = COLUMN();

    protected ViwPersonMain(final AbstractCTDSchema schema) {
        super(schema);
    }
}
