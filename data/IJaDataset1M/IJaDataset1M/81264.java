package org.modelingvalue.modelsync.expressions;

import java.util.*;
import org.modelingvalue.modelsync.elements.NameElement;
import org.modelingvalue.modelsync.messages.SourceInformation;
import org.modelingvalue.modelsync.models.OpaqueModelType;
import org.modelingvalue.modelsync.models.TypeRef;

/**
 * @author Wim Bast
 *
 */
public class SimpleTypeReference extends TypeReference {

    private final NameElement typeName;

    private final TypeReference[] parameters;

    private TypeRef<?> type;

    private GenericParameter<?> genericParameter;

    public SimpleTypeReference(SourceInformation sourceInformation, NameElement typeName, TypeReference... parameters) {
        super(sourceInformation);
        this.typeName = typeName;
        typeName.setParent(this);
        this.parameters = parameters;
    }

    private SimpleTypeReference(SimpleTypeReference cloned, TypeReference[] parameters) {
        this(cloned.getSourceInformation(), cloned.getTypeName().clone(), parameters);
    }

    @Override
    public void clean() {
        super.clean();
        for (TypeReference parameter : parameters) {
            parameter.clean();
        }
        type = null;
        genericParameter = null;
    }

    @Override
    public void compile() {
        super.compile();
        for (TypeReference parameter : parameters) {
            parameter.compile();
        }
    }

    /**
	 * @param context the context to set
	 */
    @Override
    public void setContext(Context context) {
        super.setContext(context);
        for (TypeReference parameter : parameters) {
            parameter.setContext(context);
        }
    }

    /**
	 * @return the typeName
	 */
    public NameElement getTypeName() {
        return typeName;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return typeName.getValue();
    }

    @Override
    public TypeRef<?>[] genericParameters() {
        return resolve().genericParameters();
    }

    @Override
    public Object type() {
        return resolve().type();
    }

    private TypeRef<?> resolve() {
        if (type == null) {
            genericParameter = getContext().getGenericParameter(typeName.getValue(), parameters);
            if (genericParameter != null) {
                type = genericParameter.getType();
            } else {
                type = getContext().getModelType().getType(typeName.getValue(), parameters);
            }
            if (type == null) {
                typeName.newError("TypeNotDefined", typeName.getValue());
                type = OpaqueModelType.VOID;
            }
        }
        return type;
    }

    @Override
    public List<Expression> getSources() {
        return EMPTY_EXPRESSION_LIST;
    }

    @Override
    public TypeReference clone() {
        TypeReference[] params = new TypeReference[parameters.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = parameters[i].clone();
        }
        return new SimpleTypeReference(this, params);
    }

    /**
	 * @return the parameters
	 */
    public TypeReference[] getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(parameters);
        result = prime * result + typeName.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof SimpleTypeReference)) return false;
        final SimpleTypeReference other = (SimpleTypeReference) obj;
        if (!Arrays.equals(parameters, other.parameters)) return false;
        if (!typeName.equals(other.typeName)) return false;
        return true;
    }

    /**
	 * @return the genericParameter
	 */
    public GenericParameter<?> getGenericParameter() {
        resolve();
        return genericParameter;
    }
}
