package com.inature.oce.web.struts.common;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class FormValidator2 {

    private ActionErrors errors = new ActionErrors();

    private String globalMessageKey = null;

    /**
	 * 
	 * @param messageKey
	 */
    public void setGlobalErrorMessage(String messageKey) {
        if (globalMessageKey == null) {
            globalMessageKey = messageKey;
        }
    }

    /**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 */
    public void validateRequiredField(String fieldName, String fieldValue) {
        if ((fieldValue == null) || (fieldValue.trim().length() < 1)) {
            String key = "oce.messages.error.sign";
            errors.add(fieldName, new ActionMessage(key));
            setGlobalErrorMessage("oce.messages.error.requiredField");
        }
    }

    /**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 */
    public void validateIntField(String fieldName, String fieldValue) {
        if (errors.size(fieldName) > 0) {
            return;
        }
        try {
            Integer.parseInt(fieldValue);
        } catch (NumberFormatException nfe) {
            String key = "oce.messages.error.sign";
            errors.add(fieldName, new ActionMessage(key));
            setGlobalErrorMessage("oce.messages.error.requiredInt");
        }
    }

    /**
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @param minLength
	 * @param maxLenght
	 */
    public void validateLength(String fieldName, String fieldValue, int minLength, int maxLenght) {
        if (fieldValue == null) {
            return;
        }
        if (errors.size(fieldName) > 0) {
            return;
        }
        int length = fieldValue.length();
        if ((length < minLength) || (length > maxLenght)) {
            String key = "oce.messages.error.sign";
            errors.add(fieldName, new ActionMessage(key));
            setGlobalErrorMessage("oce.messages.error.fieldValueOutOfRange");
        }
    }

    /**
	 * 
	 * @param fieldName
	 * @param errorKey
	 */
    public void addErrorMessage(String fieldName, String errorKey) {
        String key = "oce.messages.error.sign";
        errors.add(fieldName, new ActionMessage(key));
        setGlobalErrorMessage(errorKey);
    }

    /**
	 * 
	 * @return ActionErrors
	 */
    public ActionErrors getErrors() {
        if (errors.isEmpty()) {
            return null;
        }
        if (globalMessageKey != null) {
            errors.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage(globalMessageKey));
        }
        return errors;
    }

    /**
	 * 
	 * @return
	 */
    public boolean isEmpty() {
        return errors.isEmpty();
    }
}
