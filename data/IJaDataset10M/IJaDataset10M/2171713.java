package com.guanda.swidgex.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import com.guanda.swidgex.api.IMouseAwareWidget;
import com.guanda.swidgex.api.IWidgetContext;
import com.guanda.swidgex.api.IWidgetListener;

public abstract class AbstractWidgetWithProgress<V> extends SwingWorker<JComponent, V> implements IMouseAwareWidget {

    private static final long serialVersionUID = 1L;

    protected JComponent fContainer;

    protected IWidgetContext fContext;

    protected JProgressBar fProgressBar;

    protected JLabel fMessage;

    protected JComponent fContent;

    protected List<IWidgetListener> listeners;

    public AbstractWidgetWithProgress() {
        fContainer = new JComponent() {

            private static final long serialVersionUID = 1L;
        };
        listeners = Collections.synchronizedList(new ArrayList<IWidgetListener>());
    }

    @Override
    public void init(IWidgetContext context) {
        fContainer.setLayout(createProgressLayout());
        fContext = context;
        fMessage = new JLabel();
        fMessage.setForeground(Color.white);
        fContainer.add(fMessage);
        fProgressBar = new JProgressBar();
        fContainer.add(fProgressBar);
        addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    fProgressBar.setValue((Integer) evt.getNewValue());
                }
            }
        });
        aboutToRun();
        execute();
    }

    protected void setIndeterminate(boolean b) {
        fProgressBar.setIndeterminate(b);
    }

    protected void setMessage(String msg) {
        fMessage.setText(msg);
    }

    protected void aboutToRun() {
    }

    @Override
    protected void done() {
        try {
            fContainer.remove(fProgressBar);
            fContainer.remove(fMessage);
            fContainer.setLayout(createContentLayout());
            fContent = get();
            updateParentBackground(fContainer, fContent.getBackground());
            fContainer.add(fContent, BorderLayout.CENTER);
            fContainer.validate();
            fireContentUpdated();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fireContentUpdated() {
        synchronized (listeners) {
            for (IWidgetListener l : listeners) l.contentUpdated();
        }
    }

    private void updateParentBackground(Container parent, Color bg) {
        if (parent != null) {
            parent.setBackground(bg);
            parent.repaint();
            updateParentBackground(parent.getParent(), bg);
        }
    }

    protected LayoutManager createContentLayout() {
        return new BorderLayout();
    }

    protected LayoutManager createProgressLayout() {
        return new FlowLayout();
    }

    @Override
    public boolean isMouseAware(Component c) {
        if (c == fContainer) return true; else if (c == fContent) return true; else if (c == fProgressBar) return true;
        return false;
    }

    @Override
    public void addWidgetListener(IWidgetListener l) {
        if (!listeners.contains(l)) listeners.add(l);
    }

    @Override
    public void removeWidgetListener(IWidgetListener l) {
        if (listeners.contains(l)) listeners.remove(l);
    }

    @Override
    public JComponent getComponent() {
        return fContainer;
    }
}
