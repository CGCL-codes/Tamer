package org.jaffa.applications.jaffa.modules.admin.components.sessionexplorer.ui;

import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.FormBase;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.session.SessionManager;
import org.jaffa.presentation.portlet.session.UserSession;
import org.jaffa.presentation.portlet.widgets.controller.GridController;
import org.jaffa.presentation.portlet.widgets.model.GridModel;
import org.jaffa.presentation.portlet.widgets.model.GridModelRow;

/**
 *
 * @author  PaulE
 * @version 1.0
 */
public class ComponentDetailsForm extends FormBase {

    private static Logger log = Logger.getLogger(ComponentDetailsForm.class);

    public static final String NAME = "admin_sessionExplorerComponentDetailsForm";

    private GridModel w_compProps = null;

    /** Holds value of property id. */
    private String id;

    /** Holds value of property name. */
    private String name;

    private Component m_component = null;

    public void initForm() {
        String sessionId = ((SessionExplorerComponent) getComponent()).getSessionId();
        UserSession us = SessionManager.getSession(sessionId);
        if (us != null) {
            String compId = ((SessionExplorerComponent) getComponent()).getCompId();
            m_component = us.getComponent(compId);
        } else {
            m_component = null;
        }
        w_compProps = null;
        getWidgetCache().addModel("componentProps", null);
    }

    public GridModel getComponentListWM() {
        if (w_compProps == null) {
            w_compProps = (GridModel) getWidgetCache().getModel("componentProps");
            if (w_compProps == null) {
                w_compProps = getComponentsModel();
                getWidgetCache().addModel("componentProps", w_compProps);
            }
        }
        return w_compProps;
    }

    public void setComponentListWV(String value) {
        GridController.updateModel(value, getComponentListWM());
    }

    private GridModel getComponentsModel() {
        GridModel model = new GridModel();
        if (m_component == null) {
            return model;
        }
        try {
            java.beans.BeanInfo info = java.beans.Introspector.getBeanInfo(m_component.getClass());
            if (info != null) {
                java.beans.PropertyDescriptor[] pds = info.getPropertyDescriptors();
                for (int i = 0; i < pds.length; i++) {
                    java.beans.PropertyDescriptor pd = pds[i];
                    java.lang.reflect.Method method = pd.getReadMethod();
                    if (method != null && method.getDeclaringClass().isInstance(m_component)) {
                        GridModelRow row = model.newRow();
                        row.addElement("NAME", pd.getName());
                        row.addElement("CLASS", method.getReturnType().getName());
                        row.addElement("VALUE", method.invoke(m_component, new Object[] {}));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Can't Introspect Component Object (Class=" + m_component.getClass().getName() + ")", e);
        }
        return model;
    }

    /** Getter for property id.
     * @return Value of property id.
     */
    public String getId() {
        if (m_component != null) {
            return m_component.getComponentId();
        } else {
            return null;
        }
    }

    /** Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        if (m_component != null) {
            return m_component.getComponentDefinition().getComponentName();
        } else {
            return null;
        }
    }
}
