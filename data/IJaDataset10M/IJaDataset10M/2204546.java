package com.cateshop.web.taglib;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��{@link ConfigurableTag �����õı�ǩ}��д��������Ϣ�ı�ǩ.
 * 
 * @author notXX
 */
public class ConfigureTag extends TagSupport implements ValueReceiverTag {

    private static final long serialVersionUID = -6192602961897658268L;

    private static final Log log = LogFactory.getLog(ConfigureTag.class);

    /**
     * ������.
     */
    private String name;

    /**
     * ����ֵ.
     */
    private Object value;

    /**
     * �Ƿ��Ѿ����ù��ǩ��.
     */
    private boolean configured = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public int doEndTag() {
        if (value != null) {
            configureParent();
        }
        return EVAL_PAGE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int doStartTag() {
        configured = false;
        if (value != null) {
            configureParent();
            return SKIP_BODY;
        } else {
            return EVAL_BODY_INCLUDE;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release() {
        name = null;
        value = null;
        configured = false;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * ���ø���ǩ.
     */
    private void configureParent() {
        if (configured) return;
        Tag parent = getParent();
        if (parent instanceof ConfigurableTag) {
            try {
                ConfigurableTag configurableTag = (ConfigurableTag) parent;
                configurableTag.configure(name, value);
            } catch (RuntimeException e) {
                log.error("error in configure(String, Object)", e);
            }
        } else {
            log.warn(parent + " not instance of ConfigurableTag." + " configure() skiped.");
        }
        configured = true;
        value = null;
    }
}
