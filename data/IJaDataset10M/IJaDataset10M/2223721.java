package product;

import persistence.com.bosspolis.crm.product.Product;
import biz.com.bosspolis.crm.AppConstants;
import com.eis.ds.businesstieradapter.ServiceDelegate;
import com.eis.ds.businesstieradapter.ServiceDelegateFactory;
import com.eis.ds.core.commandpattern.CommandException;
import com.eis.ds.core.dto.DTOArea;
import com.eis.ds.core.log.DSLogger;
import com.eis.ds.core.log.LoggerFactory;

public class TestUpdateProductCommand {

    public static void main(String[] args) {
        DSLogger log = LoggerFactory.getInstance().getLogger(TestUpdateProductCommand.class.getName());
        DTOArea dto = new DTOArea();
        dto.setCommand("product.updateProductCommand");
        Product p = new Product();
        p.setProductId(3);
        p.setProductName("T42 222");
        p.setBrand("IBM");
        p.setDescription("Laptop");
        p.setModel("T42 222");
        dto.addParamItem(AppConstants.KEY_PRODUCT, p);
        ServiceDelegate sd = ServiceDelegateFactory.getServiceDelegate();
        sd.setDTOArea(dto);
        sd.execute();
        dto = sd.getDTOArea();
        if (dto.hasException()) {
            ((CommandException) dto.getExceptions().get(0)).printStackTrace();
        }
    }
}
