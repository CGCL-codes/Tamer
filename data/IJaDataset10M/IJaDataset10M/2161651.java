package com.home.ebiz.foundation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.apusic.ebiz.framework.web.controller.AbstractAjaxRestfulController;
import com.apusic.ebiz.model.foundation.MaterialType;
import com.home.ebiz.foundation.service.MaterialTypeService;

@Controller
@RequestMapping(value = "/materialType")
public class MaterialTypeController extends AbstractAjaxRestfulController<MaterialType> {

    @Autowired
    MaterialTypeService materialTypeService;

    public MaterialTypeController() {
        super();
        ajaxRestService = materialTypeService;
    }

    @Override
    protected String getShowPage() {
        return "foundation/materialType";
    }
}
