package org.strutsgears.tags;

import javax.servlet.jsp.JspException;
import org.springframework.beans.factory.BeanFactory;
import org.strutsgears.service.FilterValues;
import org.strutsgears.util.SpringBeanFactory;
import org.strutsgears.util.exception.SpringBeanFactoryException;

public class SpringManager extends BaseGearsTag {

    protected String beanId;

    protected String name;

    protected String filterValue;

    public SpringManager() {
        beanId = null;
        name = null;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected BeanFactory getBeanFactory() throws JspException {
        BeanFactory factory;
        try {
            factory = SpringBeanFactory.getFactory(pageContext.getServletContext());
        } catch (SpringBeanFactoryException e) {
            throw new JspException(e);
        }
        return factory;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public FilterValues getFilterValues() throws JspException {
        if (this.filterValue != null && !this.filterValue.equals("")) {
            Object filterValuesObject = null;
            filterValuesObject = pageContext.getRequest().getAttribute(this.filterValue);
            if (filterValuesObject != null && !(filterValuesObject instanceof FilterValues)) throw new JspException("Filter values wrong definition or class cast error, check your filterValue parameter");
            return (FilterValues) filterValuesObject;
        }
        return null;
    }
}
