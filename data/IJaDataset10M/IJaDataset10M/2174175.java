package com.intel.gpe.client2.common.panels.wrappers;

import java.awt.Component;
import com.intel.gui.controls2.configurable.IConfigurable;

/**
 * 
 * @author Alexander Lukichev
 * @version $Id: OKCancelPanel.java,v 1.2 2006/12/15 15:27:36 dizhigul Exp $
 *
 */
public interface OKCancelPanel {

    public boolean applyValues();

    public boolean cancelValues();

    public Component getComponent();
}
