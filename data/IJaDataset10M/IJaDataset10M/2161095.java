package com.cateshop.web.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import com.cateshop.config.ConfigItem;
import com.cateshop.config.InputTemplate;

/**
 * ������Ŀ��ǩ.
 * 
 * @author notXX
 */
public class ConfigItemTag extends TagSupport implements ConfigurableTag {

    private static final Log log = LogFactory.getLog(ConfigItemTag.class);

    private static final long serialVersionUID = 1330931329866246481L;

    /**
     * ������Ŀ.
     */
    private ConfigItem configItem;

    /**
     * ������.
     */
    private String propertyName;

    public void configure(String name, Object value) {
        if (configItem != null) configItem.configure(name, value); else log.warn("configItem or configItem.displayTemplate is null.");
    }

    @Override
    public int doEndTag() throws JspException {
        render();
        return EVAL_PAGE;
    }

    @Override
    public int doStartTag() {
        return EVAL_BODY_INCLUDE;
    }

    /**
     * @return the configItem
     */
    public ConfigItem getConfigItem() {
        return configItem;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param configItem
     *            the configItem to set
     */
    public void setConfigItem(ConfigItem configItem) {
        this.configItem = configItem;
    }

    /**
     * @param propertyName
     *            the propertyName to set
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @param propertyName
     * @return String[]
     * @throws JspException
     */
    @SuppressWarnings("hiding")
    private String[] getValues(String propertyName) throws JspException {
        Object property = RequestUtils.lookup(pageContext, Constants.BEAN_KEY, propertyName, null);
        if (property instanceof String[]) {
            return (String[]) property;
        } else if (property instanceof String) {
            return new String[] { (String) property };
        } else {
            return new String[0];
        }
    }

    /**
     * @throws JspException
     */
    private void render() throws JspException {
        if (configItem == null) return;
        InputTemplate displayTemplate = configItem.getInputTemplate();
        String name;
        name = (propertyName != null ? propertyName : configItem.getName());
        try {
            if (displayTemplate != null) displayTemplate.render(pageContext, name, getValues(name));
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
