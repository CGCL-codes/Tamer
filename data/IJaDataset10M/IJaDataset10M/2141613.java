package org.jdiagnose.library.web.spring;

import java.util.Locale;
import org.springframework.beans.BeansException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

/**
 * @author jmccrindle
 */
public class VelocityToolboxViewResolver extends AbstractTemplateViewResolver {

    private String toolboxConfigLocation = null;

    private String velocityFormatterAttribute;

    private String dateToolAttribute;

    private String numberToolAttribute;

    public VelocityToolboxViewResolver() {
        setViewClass(org.springframework.web.servlet.view.velocity.VelocityToolboxView.class);
    }

    protected View loadView(String viewName, Locale locale) throws BeansException {
        VelocityToolboxView view = (VelocityToolboxView) super.loadView(viewName, locale);
        view.setVelocityFormatterAttribute(velocityFormatterAttribute);
        view.setDateToolAttribute(dateToolAttribute);
        view.setNumberToolAttribute(numberToolAttribute);
        view.setToolboxConfigLocation(toolboxConfigLocation);
        return view;
    }

    public void setToolboxConfigLocation(String toolboxConfigLocation) {
        this.toolboxConfigLocation = toolboxConfigLocation;
    }

    protected Class requiredViewClass() {
        return org.springframework.web.servlet.view.velocity.VelocityToolboxView.class;
    }

    public void setVelocityFormatterAttribute(String velocityFormatterAttribute) {
        this.velocityFormatterAttribute = velocityFormatterAttribute;
    }

    public void setDateToolAttribute(String dateToolAttribute) {
        this.dateToolAttribute = dateToolAttribute;
    }

    public void setNumberToolAttribute(String numberToolAttribute) {
        this.numberToolAttribute = numberToolAttribute;
    }
}
