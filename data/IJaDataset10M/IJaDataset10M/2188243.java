package org.code4flex.generators.model;

import org.code4flex.generators.model.actionscript.classes.ActionScriptClass;
import org.code4flex.generators.model.classes.AbstractClass;

/**
 * 
 * @author Facundo Merighi
 * @version $Revision: 1.2 $
 */
public class FlexStateClassDeclaration extends ActionScriptClass implements AbstractClass {

    private FlexSimpleViewModel viewReference;

    private String view;

    private String childName;

    public void init(FlexSimpleViewModel simpleView) {
        this.setClassName(simpleView.getClassName() + "State");
        this.setChildName(simpleView.getClassName() + "Child");
        this.setView(simpleView.getClassName());
        this.setViewReference(simpleView);
    }

    private void setViewReference(FlexSimpleViewModel simpleView) {
        this.viewReference = simpleView;
    }

    private void setView(String className) {
        this.view = className;
    }

    private void setChildName(String string) {
        this.childName = string;
    }

    public FlexSimpleViewModel getViewReference() {
        return viewReference;
    }

    public String getView() {
        return view;
    }

    public String getChildName() {
        return childName;
    }

    public String getIdView() {
        return this.view + "View";
    }
}
