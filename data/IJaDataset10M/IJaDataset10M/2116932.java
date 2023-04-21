package nhap.rep;

import nhap.rep.manager.Row;
import nhap.rep.manager.util.RowVisitorAction;

public class EndOfLineRowVisitorAction<F extends Row<? extends Field>> implements RowVisitorAction<F> {

    public EndOfLineRowVisitorAction() {
    }

    @Override
    public void after(F object) {
    }

    @Override
    public void before(F object) {
    }
}
