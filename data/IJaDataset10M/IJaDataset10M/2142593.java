package com.doxological.doxquery.types;

import java.util.*;
import javax.xml.namespace.QName;
import com.doxological.doxquery.grammar.ParsedObject;
import com.doxological.doxquery.grammar.Token;

/**
 * <p>.</p>
 *
 * @author John Snelson
 * @todo add parse location information - jpcs
 */
public class AttributeDefinition extends Definition {

    private QName typeName_;

    public AttributeDefinition(QName name, QName typeName) {
        super(Kind.ATTRIBUTE, name);
        typeName_ = typeName;
    }

    public QName getTypeName() {
        return typeName_;
    }

    public String toQuery() {
        String result = "define attribute";
        if (getName() != null) {
            result += " " + getName().toString();
        }
        if (typeName_ != null) {
            result += " of type " + typeName_.toString();
        }
        return result;
    }
}
