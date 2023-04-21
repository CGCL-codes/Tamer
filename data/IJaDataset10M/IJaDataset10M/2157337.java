package org.argouml.uml.ui.behavior.state_machines;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import org.argouml.application.api.Argo;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import ru.novosoft.uml.behavior.state_machines.MEvent;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;

public abstract class PropPanelEvent extends PropPanelModelElement {

    protected JScrollPane paramScroll;

    public PropPanelEvent(String name, int columns) {
        this(name, null, columns);
    }

    public PropPanelEvent(String name, ImageIcon icon, int columns) {
        super(name, icon, columns);
        Class mclass = MEvent.class;
        JList paramList = new UMLList(new UMLReflectionListModel(this, "parameter", true, "getParameters", "setParameters", "addParameter", null), true);
        paramList.setForeground(Color.blue);
        paramList.setVisibleRowCount(1);
        paramList.setFont(smallFont);
        paramScroll = new JScrollPane(paramList);
        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateUp", null);
        new PropPanelButton(this, buttonPanel, _navBackIcon, Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction", "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction", "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _parameterIcon, Argo.localize("UMLMenu", "button.add-parameter"), "buttonAddParameter", null);
    }

    public java.util.List getParameters() {
        java.util.List params = null;
        Object target = getTarget();
        if (target instanceof MEvent) {
            params = ((MEvent) target).getParameters();
        }
        return params;
    }

    public void setParameters(Collection newParams) {
        Object target = getTarget();
        if (target instanceof MEvent) {
            if (newParams instanceof java.util.List) {
                ((MEvent) target).setParameters((java.util.List) newParams);
            } else {
                ((MEvent) target).setParameters(new ArrayList(newParams));
            }
        }
    }

    public void addParameter(Integer indexObj) {
        buttonAddParameter();
    }

    public void buttonAddParameter() {
        Object target = getTarget();
        if (target instanceof MEvent) {
            MEvent ev = (MEvent) target;
            MParameter newParam = ev.getFactory().createParameter();
            newParam.setKind(MParameterDirectionKind.INOUT);
            ev.addParameter(newParam);
            navigateTo(newParam);
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Event");
    }
}
