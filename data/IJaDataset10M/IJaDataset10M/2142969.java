package org.plazmaforge.bsolution.carservice.common.beans;

import org.plazmaforge.bsolution.document.common.beans.DocumentHeader;
import org.plazmaforge.bsolution.product.common.beans.ProductDocument;

/**
 * 
 * @author Oleh Hapon
 * $Id: CarMtrlPurchaseReport.java,v 1.2 2010/04/28 06:22:48 ohapon Exp $
 */
public class CarMtrlPurchaseReport extends ProductDocument {

    public static final String CLASS_ID = "CAR_MTRL_PURCHASE_REPORT";

    private DocumentHeader carMtrlPurchaseRequest;

    private DocumentHeader carServOrder;

    public DocumentHeader getCarMtrlPurchaseRequest() {
        return carMtrlPurchaseRequest;
    }

    public void setCarMtrlPurchaseRequest(DocumentHeader carMtrlPurchaseRequest) {
        this.carMtrlPurchaseRequest = carMtrlPurchaseRequest;
    }

    public String getCarMtrlPurchaseRequestTitle() {
        return carMtrlPurchaseRequest == null ? "" : carMtrlPurchaseRequest.getShortTitle();
    }

    public DocumentHeader getCarServOrder() {
        return carServOrder;
    }

    public void setCarServOrder(DocumentHeader carServOrder) {
        this.carServOrder = carServOrder;
    }

    public String getCarServOrderTitle() {
        return carServOrder == null ? "" : carServOrder.getShortTitle();
    }
}
