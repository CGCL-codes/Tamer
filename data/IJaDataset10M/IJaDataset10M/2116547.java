package com.pcmsolutions.gui.desktop;

import com.pcmsolutions.device.EMU.E4.gui.TitleProvider;
import com.pcmsolutions.gui.ComponentGenerationException;
import com.pcmsolutions.gui.FrameMenuBarProvider;
import com.pcmsolutions.system.Expirable;
import com.pcmsolutions.system.ZDisposable;
import com.pcmsolutions.system.paths.DesktopName;
import com.pcmsolutions.system.paths.ViewPath;
import javax.swing.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 10-Oct-2003
 * Time: 20:16:06
 * To change this template use Options | File Templates.
 */
public interface DesktopElement extends TitleProvider, ZDisposable, FrameMenuBarProvider, Expirable, Serializable, Comparable {

    public DesktopName getName();

    public ViewPath getViewPath();

    public JComponent getComponent() throws ComponentGenerationException;

    public boolean isComponentGenerated();

    public DesktopNodeDescriptor getNodalDescriptor();

    public ActivityContext getActivityContext();

    public String retrieveComponentSessionString();

    public void updateComponentSession(String sessStr);

    public void setSessionString(String ss);

    public String getSessionString();

    public DesktopElement getCopy();
}
