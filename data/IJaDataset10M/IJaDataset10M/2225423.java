package com.docum.ui.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.springframework.web.jsf.FacesContextUtils;
import com.docum.domain.po.common.SurveyPlace;
import com.docum.service.BaseService;

@FacesConverter(value = "surveyPlaceConverter")
public class SurveyPlaceConverter implements Converter {

    private BaseService getBaseService(FacesContext ctx) {
        BaseService svc = (BaseService) FacesContextUtils.getWebApplicationContext(ctx).getBean("baseService");
        return svc;
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return getBaseService(context).getObject(SurveyPlace.class, Long.parseLong(value));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        SurveyPlace sp;
        if (value instanceof SurveyPlace) {
            sp = (SurveyPlace) value;
            if (sp.getId() == null) throw new IllegalArgumentException("Cannot convert SurveyPlace object with null id.");
            return sp.getId().toString();
        } else {
            return "";
        }
    }
}
