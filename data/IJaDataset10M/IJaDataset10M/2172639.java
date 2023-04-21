package org.argouml.uml.ui;

import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import javax.swing.JMenuItem;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 *   This class extends JMenuItem to invoke a method upon selection.
 *   The method must have the form of "void method(MModelElement );".
 *   @author Curt Arnold
 */
public class UMLTreeMenuItem extends JMenuItem implements ActionListener {

    private Object _actionObj;

    private MModelElement _element;

    private Method _action;

    private boolean _requiresElement;

    /**
     *   Creates a new menu item.
     *   @param caption Caption for menu item.
     *   @param actionObj object on which method will be invoked.
     *   @param action name of method.
     *   @param index integer value passed to method, typically position in list.
     */
    public UMLTreeMenuItem(String caption, Object actionObj, String action, boolean requiresElement) {
        super(caption);
        _actionObj = actionObj;
        _requiresElement = requiresElement;
        try {
            _action = _actionObj.getClass().getMethod(action, new Class[] { MModelElement.class });
        } catch (Exception e) {
            System.out.println("Exception in " + _action + " popup.");
            System.out.println(e.toString());
            setEnabled(false);
        }
        addActionListener(this);
    }

    /**
     *   This method is invoked when the menu item is selected.
     *   @param event
     */
    public void actionPerformed(final java.awt.event.ActionEvent event) {
        try {
            Object[] argValue = { _element };
            _action.invoke(_actionObj, argValue);
        } catch (Exception e) {
            System.out.println(e.toString() + " in UMLListMenuItem.actionPerformed()");
        }
    }

    public void setModelElement(MModelElement element) {
        _element = element;
        if (_element == null && _requiresElement) {
            setEnabled(false);
        }
    }
}
