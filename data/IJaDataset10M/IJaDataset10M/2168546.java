package com.feature50.app.forms;

import com.feature50.clarity.Form;
import com.feature50.clarity.FormException;
import com.feature50.clarity.ClarityProgressMonitor;
import com.feature50.clarity.FormUIField;
import com.feature50.app.model.BindingBean;
import javax.beans.binding.Binding;
import javax.beans.binding.BindingContext;
import javax.swing.JButton;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BindingForm extends Form {

    private BindingBean bean;

    @FormUIField
    private JButton copy;

    protected void createUI(Container container) throws FormException {
        loadFormUI("BindingFormUI");
    }

    protected void getData(ClarityProgressMonitor monitor) throws FormException {
        bean = new BindingBean("Ben", "Galbraith");
    }

    protected void createBindings(BindingContext ctx) throws FormException {
        ctx.addBinding(new Binding(bean, "${first}", $("first"), "text"));
    }

    protected void addListeners(Container container) throws FormException {
        copy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bean.setFirst("foo");
                System.out.println(bean.getFirst() + ", " + bean.getLast());
                updateBoundComponents();
            }
        });
    }
}
