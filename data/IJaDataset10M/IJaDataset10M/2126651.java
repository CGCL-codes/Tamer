package net.sf.refactorit.classmodel.references;

import net.sf.refactorit.classmodel.BinCIType;
import net.sf.refactorit.classmodel.BinClass;
import net.sf.refactorit.classmodel.BinConstructor;
import net.sf.refactorit.classmodel.BinMethod;
import net.sf.refactorit.classmodel.BinParameter;
import net.sf.refactorit.classmodel.BinTypeRef;
import net.sf.refactorit.classmodel.Project;
import net.sf.refactorit.common.util.ClassUtil;
import net.sf.refactorit.common.util.CollectionUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The purpose of this BinItemReference class is to store methods. Supports
 *  method type parameters. We cannot store them the usual way, because
 *  to restore method, we need to know type parameters, and to restore
 *  type parameters, we need the method already to be restored.
 *
 * @author Arseni Grigorjev
 */
public final class BinMethodOrConstructorReference extends CacheableReference {

    private String name;

    private BinItemReference owner;

    private final GenericTypeReference[] paramReferences;

    private GenericTypeReference returnTypeReference = null;

    private int methodSize;

    public BinMethodOrConstructorReference(final BinMethod method) {
        super(method, method.getOwner().getProject());
        name = method.getName();
        owner = method.getOwner().getBinCIType().createReference();
        final GenericTypeReferenceManager manager = new GenericTypeReferenceManager(method.getTypeParameters());
        final BinParameter[] parameters = method.getParameters();
        paramReferences = new GenericTypeReference[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            paramReferences[i] = manager.getReferenceFor(parameters[i].getTypeRef());
        }
        if (!(method instanceof BinConstructor)) {
            returnTypeReference = manager.getReferenceFor(method.getReturnType());
        }
        if (method.getCompilationUnit() != null) {
            methodSize = method.getEndLine() - method.getStartLine();
        } else {
            methodSize = -1;
        }
    }

    public boolean isConstructorReference() {
        return returnTypeReference == null;
    }

    public Object findItem(final Project project) {
        try {
            final List methods = getCandidateMethods((BinCIType) owner.restore(project));
            filterByNameAndParameterCount(methods);
            if (methods.size() > 1) {
                restoreReferences(project);
                filterByParameterTypes(methods);
                if (methods.size() > 1 && isConstructorReference()) {
                    filterByMethodSize(methods);
                } else if (methods.size() > 1) {
                    filterByReturnType(methods);
                    if (methods.size() > 1) {
                        filterByMethodSize(methods);
                    }
                }
            }
            if (methods.size() == 0) {
                return null;
            } else if (methods.size() == 1) {
                return methods.get(0);
            }
            return null;
        } finally {
            forgetRestored();
        }
    }

    /**
   * @param ownerType
   * @return array of all declared constructors, if dealing with constructor
   *    reference; array of all declared methods, if dealing with method
   *    reference.
   */
    private List getCandidateMethods(final BinCIType ownerType) {
        if (!isConstructorReference()) {
            return CollectionUtil.toMutableList(ownerType.getDeclaredMethods());
        } else {
            return CollectionUtil.toMutableList(((BinClass) ownerType).getConstructors());
        }
    }

    private void forgetRestored() {
        for (int i = 0; i < paramReferences.length; i++) {
            paramReferences[i].forget();
        }
        if (returnTypeReference != null) {
            returnTypeReference.forget();
        }
    }

    private void restoreReferences(final Project project) {
        for (int i = 0; i < paramReferences.length; i++) {
            paramReferences[i].restore(project);
        }
        if (returnTypeReference != null) {
            returnTypeReference.restore(project);
        }
    }

    private void filterByNameAndParameterCount(final List methods) {
        for (final Iterator it = methods.iterator(); it.hasNext(); ) {
            final BinMethod method = (BinMethod) it.next();
            final BinParameter[] methodParams = method.getParameters();
            if (!isConstructorReference() && !method.getName().equals(name)) {
                it.remove();
            } else if (methodParams.length != paramReferences.length) {
                it.remove();
            }
        }
    }

    private void filterByParameterTypes(final List methods) {
        for (final Iterator it = methods.iterator(); it.hasNext(); ) {
            final BinMethod method = (BinMethod) it.next();
            final BinParameter[] methodParams = method.getParameters();
            for (int i = 0; i < methodParams.length; i++) {
                if (!paramReferences[i].typesEqual(methodParams[i].getTypeRef())) {
                    it.remove();
                    break;
                }
            }
        }
    }

    private void filterByReturnType(final List methods) {
        for (final Iterator it = methods.iterator(); it.hasNext(); ) {
            final BinMethod method = (BinMethod) it.next();
            if (!returnTypeReference.typesEqual(method.getReturnType())) {
                it.remove();
            }
        }
    }

    private void filterByMethodSize(final List methods) {
        if (methodSize >= 0) {
            Object best = methods.get(0);
            int bestDifference = Integer.MAX_VALUE;
            for (final Iterator it = methods.iterator(); it.hasNext(); ) {
                final BinMethod method = (BinMethod) it.next();
                final int diff = Math.abs(methodSize - (method.getEndLine() - method.getStartLine()));
                if (diff < bestDifference) {
                    bestDifference = diff;
                    best = method;
                }
            }
            methods.clear();
            methods.add(best);
        } else {
            final Object first = methods.get(0);
            methods.clear();
            methods.add(first);
        }
    }

    public String toString() {
        return super.toString() + "(" + (isConstructorReference() ? "ConstructorReference" : "MethodReference") + ", name=" + name + ", params: " + Arrays.asList(paramReferences) + ")";
    }
}

final class GenericTypeReferenceManager {

    private Map methodTypeParameters;

    public GenericTypeReferenceManager(final BinTypeRef[] typeParameters) {
        methodTypeParameters = new HashMap(typeParameters.length);
        for (int i = 0; i < typeParameters.length; i++) {
            methodTypeParameters.put(typeParameters[i], new MethodTypeParameterReference(typeParameters[i], typeParameters[i].getSupertypes().length));
        }
        BinTypeRef[] supertypes;
        for (int i = 0; i < typeParameters.length; i++) {
            supertypes = typeParameters[i].getSupertypes();
            for (int j = 0; j < supertypes.length; j++) {
                ((MethodTypeParameterReference) methodTypeParameters.get(typeParameters[i])).addSupertype(getReferenceFor(supertypes[j]));
            }
        }
    }

    public GenericTypeReference getReferenceFor(final BinTypeRef typeRef) {
        GenericTypeReference reference = (GenericTypeReference) methodTypeParameters.get(typeRef);
        if (reference != null) {
            return reference;
        } else {
            reference = new BinItemReferenceWrapper(typeRef);
            if (typeRef.getTypeArguments() != null && typeRef.getTypeArguments().length > 0) {
                reference.declareArguments(typeRef.getTypeArguments().length);
                for (int i = 0; i < typeRef.getTypeArguments().length; i++) {
                    reference.addTypeArgument(getReferenceFor(typeRef.getTypeArguments()[i]));
                }
            }
        }
        return reference;
    }
}

class GenericTypeReference {

    private GenericTypeReference[] typeArguments = null;

    private int lastArgumentIndex = 0;

    public void restore(final Project project) {
        if (hasTypeArguments()) {
            for (int i = 0; i < typeArguments.length; i++) {
                typeArguments[i].restore(project);
            }
        }
    }

    public void forget() {
        if (hasTypeArguments()) {
            for (int i = 0; i < typeArguments.length; i++) {
                typeArguments[i].forget();
            }
        }
    }

    public final void declareArguments(final int length) {
        typeArguments = new GenericTypeReference[length];
    }

    public final void addTypeArgument(final GenericTypeReference typeArgumentReference) {
        typeArguments[lastArgumentIndex++] = typeArgumentReference;
    }

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        if (hasTypeArguments()) {
            buf.append("<");
            for (int i = 0; i < typeArguments.length; i++) {
                buf.append(typeArguments[i]);
            }
            buf.append(">");
        }
        return buf.toString();
    }

    public final boolean hasTypeArguments() {
        return typeArguments != null;
    }

    public boolean typesEqual(final BinTypeRef typeToCheck) {
        final boolean realTypeHasArguments = typeToCheck.getTypeArguments() != null && typeToCheck.getTypeArguments().length > 0;
        final boolean storedTypeHasArguments = hasTypeArguments();
        if (!realTypeHasArguments && !storedTypeHasArguments) {
            return true;
        } else if ((!realTypeHasArguments && storedTypeHasArguments) || (realTypeHasArguments && !storedTypeHasArguments)) {
            return false;
        } else if (typeArguments.length != typeToCheck.getTypeArguments().length) {
            return false;
        } else {
            boolean result = true;
            final BinTypeRef[] realTypeArguments = typeToCheck.getTypeArguments();
            for (int i = 0; i < typeArguments.length; i++) {
                result &= typeArguments[i].typesEqual(realTypeArguments[i]);
                if (!result) {
                    break;
                }
            }
            return result;
        }
    }
}

final class BinItemReferenceWrapper extends GenericTypeReference {

    private BinItemReference binItemReference;

    private BinTypeRef restoredType = null;

    public BinItemReferenceWrapper(final BinTypeRef type) {
        binItemReference = type.createReference();
    }

    public void restore(final Project project) {
        if (!hasRestoredType()) {
            restoredType = (BinTypeRef) binItemReference.restore(project);
        }
        super.restore(project);
    }

    public void forget() {
        restoredType = null;
        super.forget();
    }

    public boolean hasRestoredType() {
        return restoredType != null;
    }

    public String toString() {
        return "" + binItemReference + (hasRestoredType() ? "|RESTORED|" : "|NOT RESTORED|") + super.toString();
    }

    public boolean typesEqual(final BinTypeRef typeRef) {
        return typeRef.equals(restoredType) && super.typesEqual(typeRef);
    }
}

/**
 * A structure, that holds information about method type parameter:<br>
 *  1) name of type parameter<br>
 *  2) supertypes of type parameter<br>
 */
final class MethodTypeParameterReference extends GenericTypeReference {

    private final String name;

    private GenericTypeReference[] supertypes;

    private int lastSupertypeIndex = 0;

    public MethodTypeParameterReference(final BinTypeRef typeParameter, final int supertypesCount) {
        name = typeParameter.getQualifiedName();
        supertypes = new GenericTypeReference[supertypesCount];
    }

    public void addSupertype(final GenericTypeReference supertypeReference) {
        supertypes[lastSupertypeIndex++] = supertypeReference;
    }

    public boolean hasSupertypes() {
        return supertypes != null;
    }

    public String toString() {
        final StringBuffer result = new StringBuffer();
        result.append(ClassUtil.getShortClassName(this) + "(" + name);
        if (hasSupertypes()) {
            result.append(" extends {");
            for (int i = 0; i < supertypes.length; i++) {
                result.append(supertypes[i]);
            }
            result.append('}');
        }
        result.append(')');
        return result.toString() + super.toString();
    }

    public void restore(final Project project) {
        if (hasSupertypes()) {
            for (int i = 0; i < supertypes.length; i++) {
                supertypes[i].restore(project);
            }
        }
        super.restore(project);
    }

    public void forget() {
        if (hasSupertypes()) {
            for (int i = 0; i < supertypes.length; i++) {
                supertypes[i].forget();
            }
        }
        super.forget();
    }

    public boolean supertypesEqual(final BinTypeRef type) {
        final BinTypeRef[] typeSupertypes = type.getSupertypes();
        for (int i = 0, max = typeSupertypes.length; i < max; i++) {
            boolean found = false;
            for (int s = 0; s < supertypes.length; s++) {
                if (supertypes[s].typesEqual(typeSupertypes[i])) {
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public boolean typesEqual(final BinTypeRef typeRef) {
        return typeRef.getQualifiedName().equals(name) && supertypesEqual(typeRef) && super.typesEqual(typeRef);
    }
}
