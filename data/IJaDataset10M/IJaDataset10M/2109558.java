package gemini.castor.ui.server.service.impl;

import gemini.basic.manager.ShoppingManager;
import gemini.basic.model.Bill;
import gemini.castor.ui.client.service.BillService;
import gemini.castor.ui.server.service.AbstractSingleService;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BillServiceImpl extends AbstractSingleService<ShoppingManager> implements BillService {

    @SuppressWarnings("unused")
    private final Log logger = LogFactory.getLog(BillServiceImpl.class);

    @Override
    public List<Bill> getBillByDistributorCode(String id) {
        List<Bill> resultList = getServerManager().getBillByDistributorCode(id);
        ArrayList<Bill> result = null;
        if (resultList != null) {
            result = new ArrayList<Bill>(resultList.size());
            for (Bill bill : resultList) {
                result.add(duplicateExcludeFields(bill));
            }
        }
        return result;
    }
}
