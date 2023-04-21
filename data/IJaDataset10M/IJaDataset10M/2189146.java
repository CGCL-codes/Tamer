package lablog.controller;

import java.util.List;
import lablog.util.orm.auto.Hardware;
import lablog.util.orm.base.ORMBase;

public class HardwareController extends AbstractController<Hardware> {

    public HardwareController() {
        super(Hardware.class);
    }

    @Override
    public List<Hardware> findAssociated(ORMBase element) {
        return null;
    }

    @Override
    public Hardware copy(Hardware element) {
        return null;
    }
}
