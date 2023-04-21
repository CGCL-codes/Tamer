package org.modelingvalue.modelsync.expressions;

import java.util.*;
import org.modelingvalue.modelsync.messages.SourceInformation;
import org.modelingvalue.modelsync.models.FunctionType;
import org.modelingvalue.modelsync.models.TypeRef;

/**
 * @author Wim Bast
 *
 */
public class FunctionTypeReference extends TypeReference implements FunctionType<Object> {

    private final TypeReference contextTypeReference;

    private final TypeReference resultTypeReference;

    private final TypeReference[] argumentTypeReferences;

    private List<Expression> sources;

    public FunctionTypeReference(SourceInformation sourceInformation, TypeReference contextTypeReference, TypeReference resultTypeReference, TypeReference... argumentTypeReferences) {
        super(sourceInformation);
        this.contextTypeReference = contextTypeReference;
        this.resultTypeReference = resultTypeReference;
        this.argumentTypeReferences = argumentTypeReferences;
    }

    private FunctionTypeReference(FunctionTypeReference cloned) {
        super(cloned.getSourceInformation());
        contextTypeReference = cloned.contextTypeReference != null ? cloned.contextTypeReference.clone() : null;
        resultTypeReference = cloned.resultTypeReference.clone();
        argumentTypeReferences = new TypeReference[cloned.getArgumentTypeReferences().length];
        for (int i = 0; i < argumentTypeReferences.length; i++) {
            argumentTypeReferences[i] = cloned.getArgumentTypeReferences()[i].clone();
        }
    }

    @Override
    public void clean() {
        super.clean();
        resultTypeReference.clean();
        for (TypeReference argTypeRef : argumentTypeReferences) {
            argTypeRef.clean();
        }
        sources = null;
    }

    @Override
    public void compile() {
        if (contextTypeReference != null) {
            contextTypeReference.compile();
        }
        resultTypeReference.compile();
        for (TypeReference argTypeRef : argumentTypeReferences) {
            argTypeRef.compile();
        }
        super.compile();
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        if (contextTypeReference != null) {
            contextTypeReference.setContext(context);
        }
        resultTypeReference.setContext(context);
        for (TypeReference argTypeRef : argumentTypeReferences) {
            argTypeRef.setContext(context);
        }
    }

    /**
	 * @return the typeName
	 */
    public TypeReference[] getArgumentTypeReferences() {
        return argumentTypeReferences;
    }

    @Override
    public TypeRef<Object> contextType() {
        return contextTypeReference;
    }

    @Override
    public TypeRef<? extends Object>[] genericParameters() {
        return resultTypeReference.genericParameters();
    }

    @Override
    public Object type() {
        return resultTypeReference.type();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(argumentTypeReferences);
        result = prime * result + resultTypeReference.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (!(obj instanceof FunctionTypeReference)) return false;
        final FunctionTypeReference other = (FunctionTypeReference) obj;
        if (!Arrays.equals(argumentTypeReferences, other.argumentTypeReferences)) return false;
        if (!resultTypeReference.equals(other.resultTypeReference)) return false;
        return true;
    }

    public TypeRef<Object>[] argumentTypes() {
        return argumentTypeReferences;
    }

    public boolean isCompatible(TypeRef<?> objectType, TypeRef<?>[] actualTypes) {
        if ((objectType == null && contextTypeReference != null) || (objectType != null && contextTypeReference == null) || (actualTypes.length != argumentTypes().length)) {
            return false;
        } else {
            if (objectType != null) {
                Boolean compatible = getContext().getModelType().isSubSuper(objectType, contextTypeReference);
                if (compatible == null || !compatible.booleanValue()) {
                    return false;
                }
            }
            for (int i = 0; i < actualTypes.length; i++) {
                Boolean compatible = getContext().getModelType().isSubSuper(actualTypes[i], argumentTypes()[i]);
                if (compatible == null || !compatible.booleanValue()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public List<Expression> getSources() {
        if (sources == null) {
            sources = new ArrayList<Expression>();
            sources.addAll(Arrays.asList(argumentTypeReferences));
        }
        return sources;
    }

    @Override
    public TypeReference clone() {
        return new FunctionTypeReference(this);
    }

    /**
	 * @return the contextTypeReference
	 */
    public TypeReference getContextTypeReference() {
        return contextTypeReference;
    }
}
