package org.weblayouttag.worker.impl;

import org.apache.commons.lang.StringUtils;
import org.weblayouttag.tag.WebLayoutTag;
import org.weblayouttag.tag.field.AbstractFieldTag;
import javax.servlet.jsp.PageContext;

/**
 * @author Andy Marek
 * @version Nov 11, 2005
 */
public class LayoutIdFieldWorker extends AbstractFieldWorker {

    private String prefix;

    private String suffix;

    public void execute(PageContext pc, WebLayoutTag tag) {
        if (tag instanceof AbstractFieldTag) {
            AbstractFieldTag fieldTag = (AbstractFieldTag) tag;
            if (fieldTag.getLayoutId() == null) {
                fieldTag.setLayoutId(StringUtils.defaultString(prefix) + fieldTag.getProperty() + StringUtils.defaultString(suffix));
            }
        }
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
