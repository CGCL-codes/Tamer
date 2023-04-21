package com.ds;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 01/11/2009
 * Time: 09:41:09
 */
public interface Trayable {

    public Image getTrayIcon();

    public void iconClicked();

    public String getTitle();
}
