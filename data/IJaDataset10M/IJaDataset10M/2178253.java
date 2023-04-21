package org.springframework.webflow.samples.phonebook.domain;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SearchCriteriaValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(SearchCriteria.class);
    }

    public void validate(Object obj, Errors errors) {
        SearchCriteria query = (SearchCriteria) obj;
        if ((query.getFirstName() == null || query.getFirstName().length() == 0) && (query.getLastName() == null || query.getLastName().length() == 0)) {
            errors.reject("noCriteria", "Please provide some query criteria!");
        }
    }
}
