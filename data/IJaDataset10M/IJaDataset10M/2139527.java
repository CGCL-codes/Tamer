package lablog.controller;

import java.util.List;
import lablog.util.orm.auto.Subgroup;
import lablog.util.orm.base.ORMBase;

public class SubgroupController extends AbstractController<Subgroup> {

    public SubgroupController() {
        super(Subgroup.class);
    }

    @Override
    public List<Subgroup> findAssociated(ORMBase element) {
        return null;
    }

    @Override
    public Subgroup copy(Subgroup element) {
        return null;
    }
}
