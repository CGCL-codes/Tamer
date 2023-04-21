package org.mc4j.console.dashboard.components;

import org.mc4j.ems.connection.bean.EmsBean;
import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * Supports a for-each iteration of a set of beans
 *
 * @author Greg Hinkle (ghinkle@users.sourceforge.net), January 2004
 * @version $Revision: 570 $($Author: ghinkl $ / $Date: 2006-04-12 15:14:16 -0400 (Wed, 12 Apr 2006) $)
 */
public class ForEachPanelComponent extends JPanel implements BeanListComponent {

    protected List<EmsBean> beanList;

    protected String idKey;

    protected boolean sorted = false;

    public ForEachPanelComponent() {
    }

    public void init() {
    }

    public void refresh() {
    }

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public void setBeanList(List<EmsBean> list) {
        this.beanList = list;
    }

    public List<EmsBean> getBeanList() {
        return beanList;
    }

    public void setContext(Map context) {
        if (this.beanList == null) {
            throw new IllegalStateException("ForEachPanelComponent: You must specify an appropriate [beanList] attribute.");
        }
        init();
    }
}
