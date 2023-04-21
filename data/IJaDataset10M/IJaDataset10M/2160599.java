package org.programmerplanet.intracollab.web.admin.component;

import org.apache.commons.lang.StringUtils;
import org.programmerplanet.intracollab.model.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Comment goes here...
 * 
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2009 Joseph Fifield
 */
public class ComponentValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(Component.class);
    }

    public void validate(Object obj, Errors errors) {
        Component component = (Component) obj;
        if (StringUtils.isEmpty(component.getName())) {
            errors.rejectValue("name", "error.required");
        }
    }
}
