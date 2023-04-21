package product;

import biz.com.bosspolis.crm.AppConstants;
import com.eis.ds.businesstieradapter.ServiceDelegate;
import com.eis.ds.businesstieradapter.ServiceDelegateFactory;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.dto.DTOArea;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;

public class TestDeleteCategoryTypeCommand {

    public static void main(String[] args) {
        DSLogger log = LoggerFactory.getInstance().getLogger(TestDeleteCategoryTypeCommand.class.getName());
        DTOArea dto = new DTOArea();
        dto.setCommand("product.deleteCategoryTypeCommand");
        dto.addParamItem(AppConstants.KEY_CATEGORYTYPEID, new Integer(1));
        ServiceDelegate sd = ServiceDelegateFactory.getServiceDelegate();
        sd.setDTOArea(dto);
        sd.execute();
        dto = sd.getDTOArea();
        if (dto.hasException()) {
            ((CommandException) dto.getExceptions().get(0)).printStackTrace();
        }
    }
}
