package net.ar.webonswing.own.adapters;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import javax.swing.*;
import net.ar.guia.helpers.*;
import net.ar.guia.own.interfaces.*;
import net.ar.guia.own.interfaces.listeners.*;

public class JComponentAdapter extends ContainerAdapter implements Clickable, HasToolTip {

    public JComponentAdapter() {
    }

    public JComponentAdapter(JComponent aJComponent) {
        super(aJComponent);
    }

    protected JComponent getJComponent() {
        return (JComponent) container;
    }

    public PanelComponent getTopParent() {
        return (PanelComponent) getWrapper(((RootPaneContainer) getJComponent().getTopLevelAncestor()).getRootPane());
    }

    public void doClick() {
        fireClickEvent(getJComponent());
    }

    public void addClickListener(ClickListener aClickListener) {
    }

    public void removeClickListener(ClickListener aClickListener) {
    }

    public String getToolTipText() {
        return getJComponent().getToolTipText();
    }

    public void setToolTipText(String toolTipText) {
        getJComponent().setToolTipText(toolTipText);
    }

    public void fireClickEvent(JComponent aComponent) {
        try {
            MouseEvent theClickEvent = new MouseEvent(aComponent, 500, System.currentTimeMillis(), 16, 1, 1, 1, false);
            Method theMethod = Component.class.getDeclaredMethod("processEvent", new Class[] { AWTEvent.class });
            theMethod.setAccessible(true);
            theMethod.invoke(aComponent, new Object[] { theClickEvent });
        } catch (Exception e) {
            throw new GuiaException(e);
        }
    }
}
