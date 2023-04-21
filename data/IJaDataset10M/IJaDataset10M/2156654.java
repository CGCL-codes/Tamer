package com.od.jtimeseries.ui.timeserious.action;

import javax.swing.*;
import java.beans.PropertyVetoException;

/**
 * Created by IntelliJ IDEA.
 * User: GA2EBBU
 * Date: 15/08/11
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractArrangeInternalFrameAction extends AbstractAction {

    public AbstractArrangeInternalFrameAction(String name, Icon icon) {
        super(name, icon);
    }

    protected void deiconify(JInternalFrame f) {
        try {
            f.setIcon(false);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
