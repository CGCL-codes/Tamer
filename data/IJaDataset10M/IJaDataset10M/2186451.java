package com.potix.zk.au;

import com.potix.zk.ui.Component;

/**
 * A response to select the whole content of the specified component
 * at the client
 * <p>data[0]: the uuid of the component to select content
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class AuSelectAll extends AuResponse {

    public AuSelectAll(Component comp) {
        super("selAll", comp, comp.getUuid());
    }
}
