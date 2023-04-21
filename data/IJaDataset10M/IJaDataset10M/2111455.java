package org.mc4j.console.dashboard.match;

import org.mc4j.ems.connection.bean.EmsBean;

/**
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), Apr 5, 2004
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class BeanObjectNameCondition extends AbstractBeanCondition {

    private String filter;

    public String[] getConditionAttributes() {
        return new String[] { "filter" };
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean testCondition(EmsBean bean) {
        if (filter != null) {
            return bean.getBeanName().apply(filter);
        } else {
            return true;
        }
    }
}
