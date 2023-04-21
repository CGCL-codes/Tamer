package com.kn.controller.utils.bean;

import java.util.List;

/** Create this kind of Attribtes for bean tag 
<beans>
	<bean class="com.kn.struts.DoExample.Action" name="/page/com/kn/struts/DoExample"/>
</beans>
 * @author k11nguye
 *
 */
public class XMLActionBeanAttribute extends BeanBase {

    private String attrName;

    private List attrNameChild;

    private String attrClass;

    public List getAttrNameChild() {
        return attrNameChild;
    }

    public void setAttrNameChild(List attrNameChild) {
        this.attrNameChild = attrNameChild;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrClass() {
        return attrClass;
    }

    public void setAttrClass(String attrClass) {
        this.attrClass = attrClass;
    }
}
