package com.pennychecker.swing.presenter.ext.component;

import com.pennychecker.swing.mvp.event.HandlerRegistration;
import com.pennychecker.swing.mvp.event.MvpEvent;
import com.pennychecker.swing.presenter.ext.event.ClickEvent;
import com.pennychecker.swing.presenter.ext.event.ClickHandler;
import com.pennychecker.swing.presenter.ext.event.HasClickHandlers;
import com.pennychecker.swing.presenter.ext.listener.SwingClickListener;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author iolaus
 */
public class SDropDown extends JComboBox implements HasClickHandlers {

    private final HandlerAdapter handlerAdapter;

    public SDropDown() {
        super();
        handlerAdapter = new HandlerAdapter();
    }

    public SDropDown(Vector<?> items) {
        super(items);
        handlerAdapter = new HandlerAdapter();
    }

    public SDropDown(Object[] items) {
        super(items);
        handlerAdapter = new HandlerAdapter();
    }

    public SDropDown(ComboBoxModel aModel) {
        super(aModel);
        handlerAdapter = new HandlerAdapter();
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        super.addActionListener(new SwingClickListener(handlerAdapter));
        return handlerAdapter.addHandler(handler, ClickEvent.TYPE);
    }

    @Override
    public void fireEvent(MvpEvent<?> event) {
        handlerAdapter.fireEvent(new ClickEvent());
    }
}
