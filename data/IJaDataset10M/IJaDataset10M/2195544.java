package com.openthinks.woms.product.rest;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import com.openthinks.woms.product.service.SimplifiedProductService;
import com.openthinks.woms.rest.GenericRestfulQueryController;
import com.openthinks.woms.rest.Message;

/**
 * Product finding controller
 * 
 * @author Zhang Junlong
 * 
 */
@Results({ @Result(name = "success", type = "redirectAction", params = { "actionName", "productFind" }) })
public class ProductFindController extends GenericRestfulQueryController {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Object model = new SimplyQuery();

    private SimplifiedProductService simplifiedProductService;

    public void setSimplifiedProductService(SimplifiedProductService simplifiedProductService) {
        this.simplifiedProductService = simplifiedProductService;
    }

    @Override
    public Object getModel() {
        return model;
    }

    @Override
    public void validate() {
    }

    @Override
    public String find() {
        SimplyQuery sq = (SimplyQuery) model;
        try {
            model = simplifiedProductService.find(sq.getKeywords(), sq.getPageSize(), sq.getPage());
        } catch (Exception e) {
            e.printStackTrace();
            model = new Message(Message.FAILURE, e.getMessage());
        }
        return "success";
    }
}
