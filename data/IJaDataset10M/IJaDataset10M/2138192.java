package org.marcont2.rulegenerator.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.marcont2.rulegenerator.VariableMatcher;
import org.marcont2.rulegenerator.util.Equals;

/**
 * Class representing Rule in the model
 * @author Piotr Piotrowski
 */
public class Rule implements Cloneable, Serializable {

    /**
     * Name of the rule
     */
    private String name;

    /**
     * If this rule terminates the execution
     */
    private boolean terminate;

    /**
     * List of premises
     */
    private ArrayList<Premise> premises = new ArrayList<Premise>();

    /**
     * List of consequents
     */
    private ArrayList<Consequent> consequents = new ArrayList<Consequent>();

    /**
     * List of calls
     */
    private ArrayList<Call> calls = new ArrayList<Call>();

    /**
     * Callection of all calls that reference this rule
     */
    private ArrayList<Call> references = new ArrayList<Call>();

    /** Creates a new instance of Rule */
    public Rule() {
    }

    /**
     * Creates a new instance of Rule with the specified values
     * @param name name of the rule
     * @param terminate if this rule should terminate
     */
    public Rule(String name, boolean terminate) {
        this.name = name;
        this.terminate = terminate;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Rule == false) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Rule rhs = (Rule) obj;
        return new Equals().append(name, rhs.name).append(terminate, rhs.terminate).isEquals();
    }

    public Object clone() {
        try {
            Rule retVal = (Rule) super.clone();
            retVal.premises = (ArrayList<Premise>) premises.clone();
            retVal.consequents = (ArrayList<Consequent>) consequents.clone();
            retVal.calls = (ArrayList<Call>) calls.clone();
            retVal.references = (ArrayList<Call>) references.clone();
            return retVal;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * Adds premise to this rule
     * @param premise premise to be added
     */
    public void addPremise(Premise premise) {
        premises.add(premise);
    }

    /**
     * Removes premise from this rule
     * @param premise premise to be removed
     * @return if operation was successful
     */
    public boolean removePremise(Premise premise) {
        if (isPremiseReferenced(premise)) {
            return false;
        } else {
            correctPremiseReferences(premise);
            premises.remove(premise);
            return true;
        }
    }

    /**
     * Method changing the premise equal to oldValue to contain values from newValue
     * @param oldValue object to identify the premise to change
     * @param newValue object containing new values for the premise
     */
    public void changePremise(Premise oldValue, Premise newValue) {
        int index = premises.indexOf(oldValue);
        Premise tmp = premises.get(index);
        tmp.setSubject(newValue.getSubject());
        tmp.setSubjectRegexp(newValue.isSubjectRegexp());
        tmp.setPredicate(newValue.getPredicate());
        tmp.setPredicateRegexp(newValue.isPredicateRegexp());
        tmp.setObject(newValue.getObject());
        tmp.setObjectRegexp(newValue.isObjectRegexp());
        tmp.setObjectDatatype(newValue.getObjectDatatype());
        tmp.setObjectXmlLang(newValue.getObjectXmlLang());
    }

    /**
     * Adds consequent to this rule
     * @param consequent consequent to be added
     */
    public void addConsequent(Consequent consequent) {
        consequents.add(consequent);
    }

    /**
     * Removes consequent from this rule
     * @param consequent consequent to be removed
     * @return if operation was successful
     */
    public boolean removeConsequent(Consequent consequent) {
        if (isConsequentReferenced(consequent)) {
            return false;
        } else {
            correctConsequentReferences(consequent);
            consequents.remove(consequent);
            return true;
        }
    }

    /**
     * Method changing the consequent equal to oldValue to contain values from newValue
     * @param oldValue object to identify the consequent to change
     * @param newValue object containing new values for the consequent
     */
    public void changeConsequent(Consequent oldValue, Consequent newValue) {
        int index = consequents.indexOf(oldValue);
        Consequent tmp = consequents.get(index);
        tmp.setSubject(newValue.getSubject());
        tmp.setPredicate(newValue.getPredicate());
        tmp.setObject(newValue.getObject());
        tmp.setObjectDatatype(newValue.getObjectDatatype());
        tmp.setObjectXmlLang(newValue.getObjectXmlLang());
    }

    /**
     * Adds call to this rule
     * @param call call to be added
     */
    public void addCall(Call call) {
        calls.add(call);
        call.getRule().references.add(call);
    }

    /**
     * Removes call from this rule
     * @param call call to be removed
     * @return if operation was successful
     */
    public boolean removeCall(Call call) {
        int index = calls.indexOf(call);
        call = calls.get(index);
        if (call.canBeRemoved()) {
            calls.remove(call);
            call.getRule().references.remove(call);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes all calls from the rule removing references in other rules.
     * @return true if the operation succeeded
     */
    public boolean removeCalls() {
        while (!calls.isEmpty()) {
            Call c = calls.remove(calls.size() - 1);
            c.getRule().references.remove(c);
        }
        return true;
    }

    /**
     * Change the rule provided call calls
     * @param call call to be changed
     * @param newRule new rule the call should call
     * @return if the operation succeeded
     */
    public boolean changeCall(Call call, Rule newRule) {
        int index = calls.indexOf(call);
        call = calls.get(index);
        if (call.canBeRemoved()) {
            call.getRule().references.remove(call);
            call.setRule(newRule);
            newRule.references.add(call);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Validates if the provided new value is valid for the provided premise
     * @param premise the new value can be applyed to
     * @param newValue new value for the premise
     * @return if the value is valid for the premise
     */
    public boolean validatePremise(Premise premise, String newValue) {
        int index = premises.indexOf(premise);
        Map<String, Collection<?>> refs = VariableMatcher.getReferences(newValue);
        boolean valid = refs.get("consequents").isEmpty();
        if (valid) {
            Collection<Integer> prems = (Collection<Integer>) refs.get("premises");
            valid = validateIndexes(index, prems);
        }
        if (valid) {
            valid = validateParamsRefs(refs);
        }
        return valid;
    }

    /**
     * Validates if the provided new value is valid for the provided consequent
     * @param consequent the new value can be applyed to
     * @param newValue new value for the consequent
     * @return if the value is valid for the consequent
     */
    public boolean validateConsequent(Consequent consequent, String newValue) {
        int index = consequents.indexOf(consequent);
        Map<String, Collection<?>> refs = VariableMatcher.getReferences(newValue);
        boolean valid = true;
        if (valid) {
            Collection<Integer> prems = (Collection<Integer>) refs.get("premises");
            valid = validateIndexes(premises.size(), prems);
        }
        if (valid) {
            Collection<Integer> cons = (Collection<Integer>) refs.get("consequents");
            valid = validateIndexes(index, cons);
        }
        if (valid) {
            valid = validateParamsRefs(refs);
        }
        return valid;
    }

    /**
     * Validates if the provided new value is valid for parameters
     * @param newValue new value
     * @return if the value is valid for parameters
     */
    public boolean validateParameter(String newValue) {
        Map<String, Collection<?>> refs = VariableMatcher.getReferences((String) newValue);
        boolean valid = true;
        if (valid) {
            Collection<Integer> prems = (Collection<Integer>) refs.get("premises");
            valid = validateIndexes(premises.size(), prems);
        }
        if (valid) {
            Collection<Integer> cons = (Collection<Integer>) refs.get("consequents");
            valid = validateIndexes(consequents.size(), cons);
        }
        if (valid) {
            valid = validateParamsRefs(refs);
        }
        return valid;
    }

    /**
     * Checks if all Integers in col are strictly below upperBound
     * @param upperBound bound below which should be all Integers
     * @param col collection of Integers which should be below upperBound
     * @return true if all Integers from col are strictly below upperBound
     */
    private boolean validateIndexes(int upperBound, Collection<Integer> col) {
        boolean valid = true;
        for (int i : col) {
            if (i >= upperBound) {
                valid = false;
                break;
            }
        }
        return valid;
    }

    /**
     * Checks if references to parameters in refs are valid for this rule
     * @param refs map which has collection of parameter names under key "parameters"
     * @return true if all the params references are valid
     */
    private boolean validateParamsRefs(Map<String, Collection<?>> refs) {
        return getParams().containsAll(refs.get("parameters"));
    }

    /**
     * Checks whether provided call is the only one referencing this rule that
     * has provided parameter.
     * @param call call to check
     * @param paramName parameter to check
     * @return true if removing call call will remove parameter param from this rule
     */
    public boolean isOnlyCallWithParameter(Call call, String paramName) {
        for (Call c : references) {
            if (!c.equals(call)) {
                for (Parameter p : c.getParams()) {
                    if (p.getName().equals(paramName)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Corrects references to all premises being after the given one in the event of removing the premise.
     * @param premise premise which will be removed
     */
    private void correctPremiseReferences(Premise premise) {
        int index = premises.indexOf(premise);
        for (Reference ref : premises) {
            ref.removePremiseReference(index);
        }
        for (Reference ref : consequents) {
            ref.removePremiseReference(index);
        }
        for (Call call : calls) {
            for (Reference ref : call.getParams()) {
                ref.removePremiseReference(index);
            }
        }
    }

    /**
     * Checks if given consequent is referenced by any other consequent or parameter.
     * @param consequent consequent to which references are to be checked
     * @return true if this given consequent is referenced
     */
    private boolean isConsequentReferenced(Consequent consequent) {
        int index = consequents.indexOf(consequent);
        for (Reference ref : consequents) {
            if (ref.isConsequentReferenced(index)) {
                return true;
            }
        }
        for (Call call : calls) {
            for (Reference ref : call.getParams()) {
                if (ref.isConsequentReferenced(index)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Changes any references to parameter oldName to newName
     * @param oldName references to change
     * @param newName reference to replace the oldName
     */
    public void correctParamReferences(String oldName, String newName) {
        for (Reference ref : premises) {
            ref.correctParameterReference(oldName, newName);
        }
        for (Reference ref : consequents) {
            ref.correctParameterReference(oldName, newName);
        }
        for (Call call : calls) {
            for (Reference ref : call.getParams()) {
                ref.correctParameterReference(oldName, newName);
            }
        }
    }

    /**
     * Corrects references to all consequents being after the given one in the event of removing the consequent.
     * @param consequent consequent which will be removed
     */
    private void correctConsequentReferences(Consequent consequent) {
        int index = consequents.indexOf(consequent);
        for (Reference ref : consequents) {
            ref.removeConsequentReference(index);
        }
        for (Call call : calls) {
            for (Reference ref : call.getParams()) {
                ref.removeConsequentReference(index);
            }
        }
    }

    /**
     * Returns if this rule is being referenced by any call
     * @return true if any call references this rule
     */
    public boolean isReferenced() {
        return !references.isEmpty();
    }

    /**
     * Returns if removing of this rule is safe: will not break any dependencies.
     * @return if removing of this rule is safe
     */
    public boolean canBeRemoved() {
        if (isReferenced()) {
            return false;
        }
        for (Call c : calls) {
            if (!c.canBeRemoved()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if given premise is referenced by any other premise, consequent or parameter.
     * @return true if this given premise is referenced
     * @param premise premise to which references are to be checked
     */
    private boolean isPremiseReferenced(Premise premise) {
        int index = premises.indexOf(premise);
        for (Reference ref : premises) {
            if (ref.isPremiseReferenced(index)) {
                return true;
            }
        }
        for (Reference ref : consequents) {
            if (ref.isPremiseReferenced(index)) {
                return true;
            }
        }
        for (Call call : calls) {
            for (Reference ref : call.getParams()) {
                if (ref.isPremiseReferenced(index)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if any field in this rule references given parameter
     * @param paramName parameter name to which references to check
     * @return true if the parameter is referenced
     */
    public boolean isParamReferenced(String paramName) {
        for (Reference ref : premises) {
            if (ref.isParameterReferenced(paramName)) {
                return true;
            }
        }
        for (Reference ref : consequents) {
            if (ref.isParameterReferenced(paramName)) {
                return true;
            }
        }
        for (Call call : calls) {
            for (Reference ref : call.getParams()) {
                if (ref.isParameterReferenced(paramName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns name of this rule
     * @return name of this rule
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this rule
     * @param name name of this rule
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns if this rule terminates the execution
     * @return if this rule terminates the execution
     */
    public boolean isTerminate() {
        return terminate;
    }

    /**
     * Sets if this rule terminates the execution
     * @param terminate if this rule terminates the execution
     */
    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    /**
     * Returns list of the premises
     * @return list of the premises
     */
    public List<Premise> getPremises() {
        return premises;
    }

    /**
     * Returns list of the consequents
     * @return list of the consequents
     */
    public List<Consequent> getConsequents() {
        return consequents;
    }

    /**
     * Returns list of the calls
     * @return list of the calls
     */
    public List<Call> getCalls() {
        return calls;
    }

    /**
     * Returns call equal to the provided one stored in this object
     * @param call call equal to the one we want to obtain from the model
     * @return call from the model equal to the one provided
     */
    public Call getCall(Call call) {
        int index = calls.indexOf(call);
        return calls.get(index);
    }

    /**
     * Returns collection of this rules parameters
     * @return collection of parameters
     */
    public Collection<String> getParams() {
        Set<String> params = new TreeSet<String>();
        for (Call c : references) {
            for (Parameter p : c.getParams()) {
                params.add(p.getName());
            }
        }
        return params;
    }

    public String toString() {
        return name;
    }
}
