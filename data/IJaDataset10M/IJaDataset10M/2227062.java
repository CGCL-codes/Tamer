package Cosmo.transformation.metaCosmoModels;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MetaCoupledModel extends MetaCosmoModel {

    protected List subComponents;

    protected List couplings;

    protected List allStateVariableNames;

    protected List allStateVariableValues;

    protected List allAttributeModifiers;

    protected List allNsmVariableNames;

    protected List allStateVariableDatatypes;

    protected List allNsmDataTypes;

    public MetaCoupledModel() {
        super();
        subComponents = new ArrayList();
        couplings = new ArrayList();
        allStateVariableNames = new ArrayList();
        allStateVariableValues = new ArrayList();
        allNsmVariableNames = new ArrayList();
        allStateVariableDatatypes = new ArrayList();
        allNsmDataTypes = new ArrayList();
        allAttributeModifiers = new ArrayList();
    }

    public void addSubComponent(MetaCosmoModel model) {
        subComponents.add(model);
    }

    public void removeSubComponent(MetaCosmoModel model) {
        subComponents.remove(model);
    }

    public List getSubComponents() {
        return new ArrayList(subComponents);
    }

    public void setSubComponents(List subComponents) {
        this.subComponents = subComponents;
    }

    public MetaCosmoModel getSubComponentByName(String name) {
        MetaCosmoModel model = null;
        Iterator itr = subComponents.iterator();
        while (model == null && itr.hasNext()) {
            MetaCosmoModel next = (MetaCosmoModel) itr.next();
            if (next.getName().equalsIgnoreCase(name)) model = next;
        }
        return model;
    }

    public List getCouplings() {
        return new ArrayList(couplings);
    }

    public void addCoupling(MetaCoupling c) {
        couplings.add(c);
    }

    public void removeCoupling(MetaCoupling c) {
        couplings.remove(c);
    }

    public List getStateVariables() {
        return new ArrayList(allStateVariableNames);
    }

    public List getStateDatatypes() {
        return new ArrayList(allStateVariableDatatypes);
    }

    public List getStateValues() {
        return new ArrayList(allStateVariableValues);
    }

    public List getAttributeModifiers() {
        return new ArrayList(allAttributeModifiers);
    }

    public void setStateVariables(List allStateVariableNames) {
        this.allStateVariableNames = allStateVariableNames;
    }

    public void setStateDatatypes(List allStateVariableDatatypes) {
        this.allStateVariableDatatypes = allStateVariableDatatypes;
    }

    public void setStateValues(List allStateVariableValues) {
        this.allStateVariableValues = allStateVariableValues;
    }

    public void setAttributeModifiers(List allAttributeModifiers) {
        this.allAttributeModifiers = allAttributeModifiers;
    }

    public List getNsmVariables() {
        return new ArrayList(allNsmVariableNames);
    }

    public void setNsmVariables(List allNsmVariableNames) {
        this.allNsmVariableNames = allNsmVariableNames;
    }

    public List getNsmDataTypes() {
        return allNsmDataTypes;
    }

    public void setNsmDataTypes(List allTypes) {
        allNsmDataTypes = allTypes;
    }
}
