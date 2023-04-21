package com.netbreeze.swing;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.event.*;
import com.netbreeze.util.*;
import org.apache.log4j.Category;

/**
 * A panel showing the output of a method execution.
 * Used by MethodsPanel.
 *
 * @author Henrik Kniberg
 */
class MethodResultPanel extends JPanel {

    static Category cat = Category.getInstance(MethodResultPanel.class);

    JLabel label;

    SmallBeanView value = null;

    BeansContext context;

    boolean isVoid = false;

    public MethodResultPanel(BeansContext context) {
        this.context = context;
        label = new JLabel("Return value:  ");
        setLayout(new BorderLayout());
        add("West", label);
    }

    public MethodResultPanel() {
        this(SwingEnvironment.getBeansContext());
    }

    /**
   * Designates the type of return value
   */
    public void setResultType(Class type) {
        if (type == Void.TYPE) {
            label.setText("(no return value)");
            isVoid = true;
        } else {
            String name = Utility.getShortClassName(type);
            label.setText("Return value (" + name + "):  ");
            isVoid = false;
        }
        invalidate();
        validate();
    }

    /**
   * Sets the actual return value to be displayed.
   */
    public void setResultValue(Object object) {
        try {
            if (!(isVoid && object == null)) {
                if (value != null) {
                    remove(value);
                }
                value = new SmallBeanView(context, object, true, true, true);
                add("Center", value);
                invalidate();
                validate();
            }
        } catch (Exception err) {
            cat.error("An error occurred", err);
        }
    }
}
