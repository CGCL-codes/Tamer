package org.middleheaven.core.reflection.inspection;

import java.util.Comparator;
import org.middleheaven.util.classification.BooleanClassifier;

public abstract class ParameterizableMemberIntrospectionCriteriaBuilder<T, M> extends MemberIntrospectionCriteriaBuilder<T, M> {

    protected ParameterizableMemberIntrospectionCriteriaBuilder(IntrospectionCriteriaBuilder<T> builder) {
        super(builder);
    }

    protected void addSortingByQuantityOfParameters() {
        this.comparator = new Comparator<M>() {

            @Override
            public int compare(M a, M b) {
                return getParameterCount(a) - getParameterCount(b);
            }
        };
    }

    protected void addParamterTypeFilter(final Class<?>[] parameterTypes) {
        logicClassifier.add(new BooleanClassifier<M>() {

            @Override
            public Boolean classify(M obj) {
                return hasParameterTypes(obj, parameterTypes);
            }
        });
    }

    protected abstract boolean hasParameterTypes(M obj, Class<?>[] parameterTypes);

    protected abstract int getParameterCount(M member);
}
