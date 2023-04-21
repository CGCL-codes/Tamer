package org.infoset.xml.memory;

import java.io.Writer;
import java.util.*;
import java.net.*;
import org.infoset.xml.*;

/**
 *
 * @author  R. Alexander Milowski
 */
public class MemoryElementEnd extends MemoryItem implements ElementEnd {

    int line;

    int column;

    Name name;

    NamespaceScope scope;

    protected TypeDefinition typeDefinition;

    protected Name typeName;

    protected int validity;

    protected int validationAttempted;

    protected List errors;

    /** Creates a new instance of MemoryElementEnd */
    public MemoryElementEnd(Infoset infoset, Name name, NamespaceScope scope) {
        super(infoset, 0, null);
        this.name = name;
        this.scope = scope;
        this.line = 0;
        this.column = 0;
        this.validity = NOT_KNOWN;
        this.validationAttempted = ATTEMPTED_NONE;
        this.typeDefinition = MemoryElement.anyType;
        this.typeName = MemoryElement.anyType.getName();
        this.errors = null;
    }

    public Item copyOfItem(boolean copyAll) {
        MemoryElementEnd end = new MemoryElementEnd(infoset, name, scope);
        end.line = line;
        end.column = column;
        end.validity = validity;
        end.validationAttempted = validationAttempted;
        end.typeDefinition = typeDefinition;
        end.errors = errors;
        return end;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setName(String localName) {
        this.name = infoset.createName(localName);
    }

    public void setName(URI namespaceName, String localName) {
        this.name = infoset.createName(namespaceName, localName);
    }

    /** Returns the type of type node.
    */
    public ItemType getType() {
        return ItemType.ElementEndItem;
    }

    protected void setIndex(int i) {
    }

    public NamespaceScope getNamespaceScope() {
        return scope;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getValidity() {
        return validity;
    }

    public int getValidationAttempted() {
        return validationAttempted;
    }

    public void setValidity(int validity, int attempted) {
        this.validity = validity;
        this.validationAttempted = attempted;
    }

    public TypeDefinition getTypeDefinition() {
        return typeDefinition;
    }

    public void setTypeDefinition(TypeDefinition typeDef) {
        typeDefinition = typeDef;
    }

    public Iterator getErrors() {
        return errors == null ? Collections.EMPTY_LIST.iterator() : errors.iterator();
    }

    public void setErrors(List errorStringList) {
        errors = new ArrayList();
        errors.addAll(errorStringList);
    }

    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList();
        }
        errors.add(error);
    }

    public String toString() {
        return "[end child " + name + "]";
    }

    public Name getTypeName() {
        return typeName;
    }

    public void setTypeName(Name typeName) {
        this.typeName = typeName;
        this.typeDefinition = null;
    }
}
