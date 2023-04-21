package org.springframework.webflow.engine.model;

import org.springframework.util.StringUtils;

/**
 * Model support for exception handlers.
 * <p>
 * Handles exceptions that occur during flow execution. Exception handlers may be attached at the state or flow level.
 * 
 * @author Scott Andrews
 */
public class ExceptionHandlerModel extends AbstractModel {

    private String bean;

    /**
	 * Create an exception handler model
	 * @param bean the name of the bean to handle exceptions
	 */
    public ExceptionHandlerModel(String bean) {
        setBean(bean);
    }

    public boolean isMergeableWith(Model model) {
        return false;
    }

    public void merge(Model model) {
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        if (StringUtils.hasText(bean)) {
            this.bean = bean;
        } else {
            this.bean = null;
        }
    }
}
