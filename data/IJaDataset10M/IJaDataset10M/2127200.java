package org.argouml.uml.ui.foundation.core;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLRadioButton;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.UMLVisibilityPanel;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

public class PropPanelOperation extends PropPanelModelElement {

    public PropPanelOperation() {
        super("Operation", _operationIcon, 3);
        Class mclass = MOperation.class;
        Class[] namesToWatch = { MStereotype.class, MNamespace.class, MClassifier.class };
        setNameEventListening(namesToWatch);
        addCaption(Argo.localize("UMLMenu", "label.name"), 1, 0, 0);
        addField(nameField, 1, 0, 0);
        addCaption(Argo.localize("UMLMenu", "label.stereotype"), 2, 0, 0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), stereotypeBox), 2, 0, 0);
        addCaption(Argo.localize("UMLMenu", "label.owner"), 3, 0, 0);
        JList ownerList = new UMLList(new UMLReflectionListModel(this, "owner", false, "getOwner", null, null, null), true);
        ownerList.setBackground(getBackground());
        ownerList.setForeground(Color.blue);
        JScrollPane ownerScroll = new JScrollPane(ownerList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addField(ownerScroll, 3, 0, 0);
        addCaption(Argo.localize("UMLMenu", "label.visibility"), 4, 0, 1);
        addField(new UMLVisibilityPanel(this, mclass, 2, false), 4, 0, 0);
        addCaption(Argo.localize("UMLMenu", "label.modifiers"), 0, 1, 0);
        JPanel modPanel = new JPanel(new GridLayout(0, 2));
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"), this, new UMLReflectionBooleanProperty("isAbstract", mclass, "isAbstract", "setAbstract")));
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"), this, new UMLReflectionBooleanProperty("isLeaf", mclass, "isLeaf", "setLeaf")));
        modPanel.add(new UMLCheckBox(localize("root"), this, new UMLReflectionBooleanProperty("isRoot", mclass, "isRoot", "setRoot")));
        modPanel.add(new UMLCheckBox(localize("query"), this, new UMLReflectionBooleanProperty("isQuery", mclass, "isQuery", "setQuery")));
        modPanel.add(new UMLCheckBox(localize("static"), this, new UMLEnumerationBooleanProperty("ownerscope", mclass, "getOwnerScope", "setOwnerScope", MScopeKind.class, MScopeKind.CLASSIFIER, MScopeKind.INSTANCE)));
        addField(modPanel, 0, 1, 0);
        addCaption("Concurrency:", 1, 1, 1);
        JPanel concurPanel = new JPanel(new GridLayout(0, 2));
        ButtonGroup group = new ButtonGroup();
        UMLRadioButton sequential = new UMLRadioButton("sequential", this, new UMLEnumerationBooleanProperty("concurrency", mclass, "getConcurrency", "setConcurrency", MCallConcurrencyKind.class, MCallConcurrencyKind.SEQUENTIAL, null));
        group.add(sequential);
        concurPanel.add(sequential);
        UMLRadioButton synchd = new UMLRadioButton("guarded", this, new UMLEnumerationBooleanProperty("concurrency", mclass, "getConcurrency", "setConcurrency", MCallConcurrencyKind.class, MCallConcurrencyKind.GUARDED, null));
        group.add(synchd);
        concurPanel.add(synchd);
        UMLRadioButton concur = new UMLRadioButton("concurrent", this, new UMLEnumerationBooleanProperty("concurrency", mclass, "getConcurrency", "setConcurrency", MCallConcurrencyKind.class, MCallConcurrencyKind.CONCURRENT, null));
        group.add(concur);
        concurPanel.add(concur);
        addField(concurPanel, 1, 1, 0);
        addCaption(Argo.localize("UMLMenu", "label.parameters"), 0, 2, .5);
        JList paramList = new UMLList(new UMLReflectionListModel(this, "parameter", true, "getParameters", "setParameters", "addParameter", null), true);
        paramList.setForeground(Color.blue);
        paramList.setVisibleRowCount(1);
        paramList.setFont(smallFont);
        addField(new JScrollPane(paramList), 0, 2, 0.5);
        addCaption(Argo.localize("UMLMenu", "label.raisedsignals"), 1, 2, 0.5);
        JList exceptList = new UMLList(new UMLReflectionListModel(this, "signal", true, "getRaisedSignals", "setRaisedSignals", "addRaisedSignal", null), true);
        exceptList.setForeground(Color.blue);
        exceptList.setVisibleRowCount(1);
        exceptList.setFont(smallFont);
        addField(new JScrollPane(exceptList), 1, 2, 0.5);
        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateUp", null);
        new PropPanelButton(this, buttonPanel, _navBackIcon, Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction", "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction", "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _operationIcon, Argo.localize("UMLMenu", "button.add-new-operation"), "buttonAddOperation", null);
        new PropPanelButton(this, buttonPanel, _parameterIcon, Argo.localize("UMLMenu", "button.add-parameter"), "buttonAddParameter", null);
        new PropPanelButton(this, buttonPanel, _signalIcon, localize("Add raised signal"), "buttonAddRaisedSignal", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-operation"), "removeElement", null);
    }

    public MClassifier getReturnType() {
        MClassifier type = null;
        Object target = getTarget();
        if (target instanceof MOperation) {
            java.util.List params = ((MOperation) target).getParameters();
            if (params != null) {
                Iterator iter = params.iterator();
                MParameter param;
                while (iter.hasNext()) {
                    param = (MParameter) iter.next();
                    if (param.getKind() == MParameterDirectionKind.RETURN) {
                        type = param.getType();
                        break;
                    }
                }
            }
        }
        return type;
    }

    public void setReturnType(MClassifier type) {
        Object target = getTarget();
        if (target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            Collection params = oper.getParameters();
            MParameter param;
            if (type == null) {
                if (params != null) {
                    Iterator iter = params.iterator();
                    while (iter.hasNext()) {
                        param = (MParameter) iter.next();
                        if (param.getKind() == MParameterDirectionKind.RETURN) {
                            oper.removeParameter(param);
                            break;
                        }
                    }
                }
            } else {
                MParameter retParam = null;
                if (params != null) {
                    Iterator iter = params.iterator();
                    while (iter.hasNext()) {
                        param = (MParameter) iter.next();
                        if (param.getKind() == MParameterDirectionKind.RETURN) {
                            retParam = param;
                            break;
                        }
                    }
                }
                if (retParam == null) {
                    retParam = UmlFactory.getFactory().getCore().buildParameter(oper, MParameterDirectionKind.RETURN);
                }
                retParam.setType(type);
            }
        }
    }

    public java.util.List getParameters() {
        java.util.List params = null;
        Object target = getTarget();
        if (target instanceof MOperation) {
            params = ((MOperation) target).getParameters();
        }
        return params;
    }

    public void setParameters(Collection newParams) {
        Object target = getTarget();
        if (target instanceof MOperation) {
            if (newParams instanceof java.util.List) {
                ((MOperation) target).setParameters((java.util.List) newParams);
            } else {
                ((MOperation) target).setParameters(new ArrayList(newParams));
            }
        }
    }

    public void addParameter(Integer indexObj) {
        buttonAddParameter();
    }

    public Object getOwner() {
        Object owner = null;
        Object target = getTarget();
        if (target instanceof MOperation) {
            owner = ((MOperation) target).getOwner();
        }
        return owner;
    }

    public Collection getRaisedSignals() {
        Collection signals = null;
        Object target = getTarget();
        if (target instanceof MOperation) {
            signals = ((MOperation) target).getRaisedSignals();
        }
        return signals;
    }

    public void setRaisedSignals(Collection signals) {
        Object target = getTarget();
        if (target instanceof MOperation) {
            ((MOperation) target).setRaisedSignals(signals);
        }
    }

    public void addRaisedSignal(Integer index) {
        Object target = getTarget();
        if (target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MSignal newSignal = oper.getFactory().createSignal();
            oper.getOwner().getNamespace().addOwnedElement(newSignal);
            oper.addRaisedSignal(newSignal);
            navigateTo(newSignal);
        }
    }

    public void buttonAddParameter() {
        Object target = getTarget();
        if (target instanceof MOperation) {
            navigateTo(CoreFactory.getFactory().buildParameter((MOperation) target));
        }
    }

    public void buttonAddOperation() {
        Object target = getTarget();
        if (target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MClassifier owner = oper.getOwner();
            if (owner != null) {
                MOperation newOper = UmlFactory.getFactory().getCore().buildOperation(owner);
                newOper.addMElementListener(((MElementListener) ProjectBrowser.TheInstance.getActiveDiagram().presentationFor(owner)));
                navigateTo(newOper);
            }
        }
    }

    public void buttonAddRaisedSignal() {
        Object target = getTarget();
        if (target instanceof MOperation) {
            addRaisedSignal(new Integer(1));
        }
    }

    public void navigateUp() {
        Object target = getTarget();
        if (target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MClassifier owner = oper.getOwner();
            if (owner != null) {
                navigateTo(owner);
            }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Operation") || baseClass.equals("BehavioralFeature") || baseClass.equals("Feature");
    }

    /**
     *   Appropriate namespace is the namespace of our class,
     *      not the class itself
     */
    protected MNamespace getDisplayNamespace() {
        MNamespace ns = null;
        Object target = getTarget();
        if (target instanceof MAttribute) {
            MAttribute attr = ((MAttribute) target);
            MClassifier owner = attr.getOwner();
            if (owner != null) {
                ns = owner.getNamespace();
            }
        }
        return ns;
    }
}
