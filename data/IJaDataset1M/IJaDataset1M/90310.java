package com.patientis.business.output;

import com.patientis.business.controllers.DefaultCustomController;
import com.patientis.model.common.IBaseModel;
import com.patientis.model.med.MedOrderModel;
import com.patientis.model.order.OrderModel;

/**
 * @author gcaulton
 *
 */
public class OrderOutputController extends DefaultCustomController {

    /**
	 * @see com.patientis.business.controllers.DefaultCustomController#getCustomControlDisplay(com.patientis.model.common.IBaseModel, com.patientis.model.common.IBaseModel)
	 */
    @Override
    public String getCustomControlDisplay(IBaseModel mainModel, IBaseModel controlModel) throws Exception {
        if (mainModel instanceof OrderModel) {
            OrderModel order = (OrderModel) mainModel;
            StringBuffer sb = new StringBuffer(1024);
            sb.append(order.getHtmlDocumentDisplay());
            return sb.toString();
        } else {
            return null;
        }
    }
}
