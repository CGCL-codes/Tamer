package com.sshtools.common.ui;

import java.awt.*;
import javax.swing.*;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public interface Tab {

    /**
*
*
* @return
*/
    public String getTabContext();

    /**
*
*
* @return
*/
    public Icon getTabIcon();

    /**
*
*
* @return
*/
    public String getTabTitle();

    /**
*
*
* @return
*/
    public String getTabToolTipText();

    /**
*
*
* @return
*/
    public int getTabMnemonic();

    /**
*
*
* @return
*/
    public Component getTabComponent();

    /**
*
*
* @return
*/
    public boolean validateTab();

    /**
*
*/
    public void applyTab();

    /**
*
*/
    public void tabSelected();
}
