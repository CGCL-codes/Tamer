package wesodi.persistence.mocks;

import wesodi.persistence.facade.DistributedProductFacadeLocal;
import wesodi.persistence.facade.bean.DistributionPointFacadeBean;

public class DistributionPointFacadeMock extends DistributionPointFacadeBean {

    public DistributionPointFacadeMock(DistributedProductFacadeLocal distributedProductBean) {
        super(distributedProductBean);
    }
}
