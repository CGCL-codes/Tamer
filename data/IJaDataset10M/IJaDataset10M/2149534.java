package org.kwantu.zakwantu.util;

import org.apache.wicket.Component;
import org.kwantu.m2.model.StringUtil;
import org.kwantu.zakwantu.RegressionTestable;

/**
 * @author <a href="mailto:petrus@codewave.co.za>Petrus Pelser</a>
 */
public class RegressionTestingUtil {

    public static String getMarkupId(Component component, int regressionTestingId) {
        String prefix = "";
        Component c = component.getParent();
        while (c != null) {
            if (c instanceof RegressionTestable) {
                prefix = ((RegressionTestable) c).getRegressionTestingMarkupId();
                break;
            }
            c = c.getParent();
        }
        return prefix + (StringUtil.isEmpty(prefix) ? "" : ":") + regressionTestingId;
    }
}
