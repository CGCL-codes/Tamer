package net.simpleframework.workflow;

import java.io.InputStream;
import net.simpleframework.core.bean.AbstractIdDataObjectBean;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class ActivityLobBean extends AbstractIdDataObjectBean {

    private InputStream attributeExt;

    private InputStream variables;

    public InputStream getAttributeExt() {
        return attributeExt;
    }

    public void setAttributeExt(final InputStream attributeExt) {
        this.attributeExt = attributeExt;
    }

    public InputStream getVariables() {
        return variables;
    }

    public void setVariables(final InputStream variables) {
        this.variables = variables;
    }

    private static final long serialVersionUID = 3835969904949989330L;
}
