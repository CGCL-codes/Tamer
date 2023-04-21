package org.dhmp.util.xml.validator;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;
import org.dhmp.util.HierarchicalMap;
import org.dhmp.util.xml.Schema;

/**
 * Validates sequence and choice groups.
 */
public class GroupValidator extends Validator {

    public Object validate(String name, Object value, HierarchicalMap constraints, Schema schema, HierarchicalMap messages, int replace) {
        HierarchicalMap constraint = (HierarchicalMap) constraints.get("_constraints");
        if (value == null || "".equals(value)) {
            value = constraint.get("default");
        }
        if (!checkNillable(name, value, constraint, schema, messages)) {
            return FAIL;
        }
        if (value == null) {
            return null;
        }
        if (!(value instanceof HierarchicalMap)) {
            String messageComplement = schema.getDefaultMessage("MSG_GROUP_INVALID_OBJECT");
            Object arguments[] = { value.getClass().getName() };
            messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
            appendMessage(name, "default", constraint, schema, messages, messageComplement);
            return FAIL;
        }
        HierarchicalMap map = (HierarchicalMap) value;
        Number min = (Number) constraint.get("gminOccurs");
        int gminOccurs = (min == null) ? 1 : min.intValue();
        Number max = (Number) constraint.get("gmaxOccurs");
        int gmaxOccurs = (max == null) ? 1 : max.intValue();
        if ("sequence".equals(constraint.get("subType"))) {
            map = SequenceValidator.validate(gminOccurs, gmaxOccurs, name, map, constraints, schema, messages, replace);
        } else if ("choice".equals(constraint.get("subType"))) {
            map = ChoiceValidator.validate(gminOccurs, gmaxOccurs, name, map, constraints, schema, messages, replace);
        }
        if (map == null) return FAIL;
        return map;
    }

    private static class SequenceValidator {

        public static HierarchicalMap validate(int min, int max, String name, HierarchicalMap map, HierarchicalMap constraints, Schema schema, HierarchicalMap messages, int replace) {
            HierarchicalMap constraint = (HierarchicalMap) constraints.get("_constraints");
            Iterator elementIterator = map.entrySet().iterator();
            int occurence = 0;
            Map.Entry element = null;
            while (elementIterator.hasNext()) {
                ++occurence;
                if (max > 0 && occurence > max) {
                    String messageComplement = schema.getDefaultMessage("MSG_GROUP_INVALID_GROUP_OCCURRENCE");
                    Object arguments[] = { name, new Integer(min), ((max > 0) ? (Object) new Integer(max) : "unbound") };
                    messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                    Validator.appendMessage(name, "sequence", constraint, schema, messages, messageComplement);
                    return null;
                }
                Iterator constraintIterator = constraints.entrySet().iterator();
                while (constraintIterator.hasNext()) {
                    Map.Entry currentConstraint;
                    do {
                        currentConstraint = (Map.Entry) constraintIterator.next();
                    } while ("_constraints".equals(currentConstraint.getKey()));
                    HierarchicalMap elementConstraint = (HierarchicalMap) currentConstraint.getValue();
                    if (elementConstraint == null) {
                        map = null;
                        continue;
                    }
                    Validator val = (Validator) elementConstraint.get("_constraints/validator");
                    Number minOccurs = (Number) elementConstraint.get("_constraints/minOccurs");
                    int elmentMinOccurs = (minOccurs == null) ? 1 : minOccurs.intValue();
                    Number maxOccurs = (Number) elementConstraint.get("_constraints/maxOccurs");
                    int elmentMaxOccurs = (maxOccurs == null) ? 1 : maxOccurs.intValue();
                    int elementOccurrence = 0;
                    Object elementKey = null;
                    while (element != null || elementIterator.hasNext()) {
                        element = (element == null) ? (Map.Entry) elementIterator.next() : element;
                        elementKey = element.getKey();
                        if (!currentConstraint.getKey().equals(elementKey)) {
                            break;
                        }
                        ++elementOccurrence;
                        Object value = val.validate(name + "/" + elementKey, element.getValue(), elementConstraint, schema, messages, replace);
                        if (Validator.hasFailed(value)) {
                            map = null;
                        } else if (replace != Schema.DO_NOT_REPLACE_VALUE) {
                            element.setValue(value);
                        }
                        element = null;
                    }
                    if (elementOccurrence == 0 && elmentMinOccurs > 0) {
                        String messageComplement = schema.getDefaultMessage("MSG_GROUP_UNEXPECTED_ELEMENT");
                        Object arguments[] = { currentConstraint.getKey(), elementKey };
                        messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                        Validator.appendMessage(name, "sequence", constraint, schema, messages, messageComplement);
                        return null;
                    }
                    if (elementOccurrence < elmentMinOccurs || (elmentMaxOccurs > 0 && elementOccurrence > elmentMaxOccurs)) {
                        String messageComplement = schema.getDefaultMessage("MSG_GROUP_INVALID_OCCURRENCE");
                        Object arguments[] = { currentConstraint.getKey(), new Integer(elmentMinOccurs), ((elmentMaxOccurs > 0) ? (Object) new Integer(elmentMaxOccurs) : "unbound") };
                        messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                        Validator.appendMessage(name, "sequence", constraint, schema, messages, messageComplement);
                        map = null;
                    }
                    if (!elementIterator.hasNext() && elementOccurrence < elmentMinOccurs) {
                        String messageComplement = schema.getDefaultMessage("MSG_GROUP_MISSING_ELEMENT");
                        Object arguments[] = { currentConstraint.getKey() };
                        messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                        Validator.appendMessage(name, "sequence", constraint, schema, messages, messageComplement);
                        return null;
                    }
                }
            }
            if (element != null) {
                String messageComplement = schema.getDefaultMessage("MSG_GROUP_INVALID_ELEMENT");
                Object arguments[] = { element.getKey() };
                messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                Validator.appendMessage(name, "sequence", constraint, schema, messages, messageComplement);
            }
            if (occurence < min) {
                String messageComplement = schema.getDefaultMessage("MSG_GROUP_INVALID_GROUP_OCCURRENCE");
                Object arguments[] = { name, new Integer(min), ((max > 0) ? (Object) new Integer(max) : "unbound") };
                messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                Validator.appendMessage(name, "sequence", constraint, schema, messages, messageComplement);
                return null;
            }
            return map;
        }
    }

    private static class ChoiceValidator {

        public static HierarchicalMap validate(int min, int max, String name, HierarchicalMap map, HierarchicalMap constraints, Schema schema, HierarchicalMap messages, int replace) {
            int occurence = 0;
            HierarchicalMap constraint = (HierarchicalMap) constraints.get("_constraints");
            Iterator elementIterator = map.entrySet().iterator();
            while (elementIterator.hasNext()) {
                ++occurence;
                if (max > 0 && occurence > max) {
                    Validator.appendMessage(name, "maxOccurs", constraint, schema, messages, null);
                    return null;
                }
                Map.Entry element = (Map.Entry) elementIterator.next();
                HierarchicalMap entryConstraint;
                entryConstraint = (HierarchicalMap) constraints.get(element.getKey());
                if (entryConstraint == null) {
                    String messageComplement = schema.getDefaultMessage("MSG_GROUP_INVALID_ELEMENT");
                    Object arguments[] = { element.getKey() };
                    messageComplement = MessageFormat.format(messageComplement.toString(), arguments);
                    Validator.appendMessage(name, "choice", constraint, schema, messages, messageComplement);
                    map = null;
                    continue;
                }
                Validator val = (Validator) entryConstraint.get("_constraints/validator");
                Object value = val.validate(name + "/" + element.getKey(), element.getValue(), entryConstraint, schema, messages, replace);
                if (Validator.hasFailed(value)) {
                    map = null;
                    continue;
                } else if (replace != Schema.DO_NOT_REPLACE_VALUE) {
                    element.setValue(value);
                }
            }
            if (occurence < min) {
                Validator.appendMessage(name, "minOccurs", constraint, schema, messages, null);
                return null;
            }
            return map;
        }
    }

    public int compareTo(Object o1, Object o2) {
        return 0;
    }

    public Object newValue(Object value) {
        return null;
    }
}
