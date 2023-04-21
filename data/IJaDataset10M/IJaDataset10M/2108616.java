package fulmine.rpc;

import static fulmine.util.Utils.CLOSE_BRACE;
import static fulmine.util.Utils.DOT;
import static fulmine.util.Utils.EMPTY_STRING;
import static fulmine.util.Utils.OPEN_BRACE;
import static fulmine.util.Utils.SPACE;
import static fulmine.util.Utils.nullCheck;
import java.util.Arrays;
import fulmine.model.field.AbstractField;
import fulmine.model.field.IField;
import fulmine.util.reference.is;

/**
 * A simple implementation of an {@link IRpcDefinition}.
 * 
 * @author Ramon Servadei
 */
public final class RpcDefinition implements IRpcDefinition {

    /** The RPC name */
    private final String name;

    /** The result type */
    private final Class<? extends IField> resultType;

    /** The argument types */
    private final Class<? extends IField>[] argTypes;

    /**
     * The identity of the remote context that has published this RPC definition
     */
    private String remoteContextIdentity;

    /**
     * Construct the definition with all relevant attributes
     * 
     * @param resultType
     *            the result type
     * @param name
     *            the RPC name
     * @param argTypes
     *            the argument types, in order
     */
    public RpcDefinition(Class<? extends IField> resultType, String name, Class<? extends IField>... argTypes) {
        super();
        nullCheck(argTypes, "No RPC arguments");
        nullCheck(resultType, "No RPC result type");
        nullCheck(name, "No RPC name");
        this.argTypes = argTypes;
        this.name = name;
        this.resultType = resultType;
    }

    /**
     * Construct the definition by parsing a string representation of the RPC
     * definition. A string definition is in the form:
     * 
     * <pre>
     * result name(args)
     * 
     * e.g. BooleanField foo(StringField, IntegerField)
     * </pre>
     * 
     * @param definitionAsString
     *            the string definition of the RPC
     */
    @SuppressWarnings("unchecked")
    public RpcDefinition(String definitionAsString) {
        definitionAsString = definitionAsString.trim();
        int indexOf = definitionAsString.indexOf(SPACE);
        final String packageName = AbstractField.class.getPackage().getName();
        String result = definitionAsString.substring(0, indexOf).trim();
        definitionAsString = definitionAsString.substring(indexOf).trim();
        try {
            this.resultType = (Class<? extends IField>) Class.forName(packageName + DOT + result);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Could not parse result type from: " + definitionAsString, e);
        }
        indexOf = definitionAsString.indexOf(OPEN_BRACE);
        this.name = definitionAsString.substring(0, indexOf).trim();
        definitionAsString = definitionAsString.substring(indexOf + 1).trim().replace(CLOSE_BRACE, EMPTY_STRING);
        this.argTypes = RpcUtils.getArgArray(definitionAsString);
    }

    public Class<? extends IField>[] getArgumentTypes() {
        return this.argTypes;
    }

    public String getName() {
        return this.name;
    }

    public String getRemoteContextIdentity() {
        return this.remoteContextIdentity;
    }

    public Class<? extends IField> getResultType() {
        return this.resultType;
    }

    @Override
    public String toString() {
        return getResultType().getSimpleName() + SPACE + RpcUtils.getSignature(getName(), getArgumentTypes());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.argTypes);
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (is.same(this, obj)) {
            return true;
        }
        if (is.differentClass(this, obj)) {
            return false;
        }
        RpcDefinition other = (RpcDefinition) obj;
        return is.eq(getName(), other.getName()) && is.deepEq(getArgumentTypes(), other.getArgumentTypes());
    }
}
