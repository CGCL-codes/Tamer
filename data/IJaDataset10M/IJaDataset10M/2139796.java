package org.wdcode.back.tag.ui;

import org.wdcode.common.util.CommonUtil;

/**
 * 扩展struts2 LabelTag
 * @author WD
 * @since JDK6
 * @version 1.0 2010-03-23
 */
public final class LabelTag extends org.apache.struts2.views.jsp.ui.LabelTag {

    private static final long serialVersionUID = -7893159489194752596L;

    /**
	 * 重写方法
	 */
    @Override
    protected void populateParams() {
        value = CommonUtil.isEmpty(value) ? getStack().findString("#attr.temUI.get('value')") : value;
        cssClass = CommonUtil.isEmpty(cssClass) ? "%{#attr.temUI.get('cssClass')}" : cssClass;
        super.populateParams();
    }
}
