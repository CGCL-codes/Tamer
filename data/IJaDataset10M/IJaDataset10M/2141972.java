package com.docum.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.web.jsf.FacesContextUtils;
import com.docum.domain.po.common.Company;
import com.docum.service.BaseService;

@FacesConverter(value = "companyConverter")
public class CompanyConverter implements Converter {

    private BaseService getBaseService(FacesContext ctx) {
        BaseService svc = (BaseService) FacesContextUtils.getWebApplicationContext(ctx).getBean("baseService");
        return svc;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return getBaseService(context).getObject(Company.class, Long.parseLong(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Company company;
        if (value instanceof Company) {
            company = (Company) value;
            if (company.getId() == null) throw new IllegalArgumentException("Cannot convert Company object with null id.");
            return company.getId().toString();
        } else {
            return "";
        }
    }
}
