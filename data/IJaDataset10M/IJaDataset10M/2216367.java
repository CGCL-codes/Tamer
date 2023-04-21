package org.ascape.query.parser;

import org.ascape.query.Validated;

public class QTProperty extends SimpleNode implements Validated {

    public QTProperty(int id) {
        super(id);
    }

    public QTProperty(BoolExprTree p, int id) {
        super(p, id);
    }

    public void validate(Object object) throws ParseException {
        Object parentObject = object;
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (jjtGetChild(i) instanceof QTField) {
                QTField qtField = (QTField) jjtGetChild(i);
                if (parentObject != null) {
                    qtField.validate(parentObject);
                    parentObject = qtField.getValue(parentObject);
                } else {
                    throw new ParseException("Parent is null, cannot get member: " + qtField + " for " + parentObject);
                }
            }
        }
    }

    public Object getComparedValue(Object object) throws ParseException {
        Object foundObject = object;
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (jjtGetChild(i) instanceof QTField) {
                QTField qtField = (QTField) jjtGetChild(i);
                if (foundObject != null) {
                    if (i < jjtGetNumChildren() - 1) {
                        foundObject = qtField.getValue(foundObject);
                    } else {
                        foundObject = qtField.getComparedValue(foundObject);
                    }
                } else {
                    break;
                }
            }
        }
        return foundObject;
    }

    public Class getType() {
        Node node = jjtGetChild(jjtGetNumChildren() - 1);
        if (node instanceof QTField) {
            return ((QTField) node).getDescriptor().getPropertyType();
        } else if (node instanceof QTWildCard) {
            return String.class;
        } else {
            throw new InternalError("Did not expect this node here: " + node);
        }
    }

    public String getName() {
        return ((QTField) jjtGetChild(jjtGetNumChildren() - 1)).getName();
    }
}
